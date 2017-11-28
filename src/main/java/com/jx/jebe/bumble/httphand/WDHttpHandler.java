package com.jx.jebe.bumble.httphand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jx.jebe.bumble.beans.PersonExtEntity;
import com.jx.jebe.bumble.buz.AccountBuz;
import com.jx.jebe.bumble.buz.EnterpriseBuz;
import com.jx.jebe.bumble.buz.WangdengBuz;
import com.jx.jebe.bumble.dao.entity.GSAccountEntity;
import com.jx.jebe.bumble.dao.entity.SetupTaskEnitty;
import com.jx.service.enterprise.entity.LvEnterpriseEntity;
import com.jx.service.enterprise.entity.LvEnterprisePersonEntity;

import net.sourceforge.tess4j.Tesseract;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;



/**
 * Created by xiaowei on 17/11/2.
 */
public class WDHttpHandler {
    private WDHttpHandler(){

    }
    private static final String start_new_wangdeng_url = "http://wsdj.baic.gov.cn/page/company/add/set_up_my_names.html";
    //企业基本信息
    private static  String enterprise_basic_info_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/basic_info.html";
    //企业股东信息
    private static  String Share_holder_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/inv.html";
    //主要人员信息
    private static  String main_member_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/member.html";
    //企业联系人信息
    private static  String contact_info_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/contact.html";
    //文件上传
    private static  String file_upload_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/upload.html";
    //补充信息
    private static  String ext_info_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/entsnd.html";
    //备案填报
    private static  String ent_ba_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/entba.html";
    //预览页面
    private static  String pre_view_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/preview.html";

    //测试企业demo
    private static String enter_id_demo = "3091657e30a84273a0aa34b96322374b";

    private static WDHttpHandler wdHttpHandler = null;
    private static Object lock = new Object();
    private static Map<Long,String> logmap = new HashMap<Long,String>();
    public static WDHttpHandler loadWDHandler(){
        if(null == wdHttpHandler){
            synchronized (lock){
                if(null == wdHttpHandler){
                    wdHttpHandler = new WDHttpHandler();
                }
            }
        }
        return wdHttpHandler;
    }
    public String getLoginCookie(long accid)throws Exception{
        String cookies = "";
        if(logmap.containsKey(accid)){
            cookies = logmap.get(accid);
        }else {
            cookies = loginGSByaccount(accid);
            if(null != cookies && !cookies.equals("")){
                logmap.put(accid,cookies);
            }
        }
        return cookies;
    }
    private String loginGSByaccount(long accid)throws Exception{
        String result = "";
        GSAccountEntity accountEntity =  AccountBuz.getAccountBuz().getGsacountByid(accid);
        if(null != accountEntity){
            String login_url = "http://wsdj.baic.gov.cn/system/login.do";
            //1,验证码错误.2 用户名密码错误 3正确，并跳转到相应的地址
            //4后台用户不能在前台登陆。5身份验证没有通过校验
            result = HttpHelpers.getHttpHelpers().sendPostRequest(login_url,"login_name=robbin1995&user_pwd=XWLZ2017&verify_code="+readVerfiycode());

        }
        return result;
    }



    public static void main(String[] args){

        String checknumber = null;
        try {
            checknumber = EnterpriseBuz.enterpriseBuz.getEntercheckedNameCode("908764");
            String cardno = EnterpriseBuz.enterpriseBuz.getEntercheckedNameCerNo("908764");
            WDHttpHandler.loadWDHandler().addnewsetup(checknumber,cardno,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String readVerfiycode() {
        String retcode = "";
        String imagestr = "";

        try {
            imagestr = "http://wsdj.baic.gov.cn/system/getVerifyCode.do?"+URLEncoder.encode(new Date()+"","utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        File file = new File("d:\\ttt.jpg");
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(HttpHelpers.getHttpHelpers().loadverfycode(imagestr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            retcode = new Tesseract().doOCR(bufferedImage);
            if(retcode.contains(" ")){
                retcode = retcode.replaceAll(" ","");
            }
            System.out.println(retcode);
            ImageIO.write(bufferedImage,"jpg",file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retcode;
    }
    /**
     * 通过异步请求去新建设立。
     * @param checknum
     * @param checkcardno
     * @throws Exception
     */
    public String addnewsetupByajax(String checknum,String checkcardno,SetupTaskEnitty se)throws Exception{
        String retid_id = "";
        if(null == checknum || "".equals(checknum) || null == checkcardno || "".equals(checkcardno))
            return retid_id;
        String post_url = "http://wsdj.baic.gov.cn/cp/setup/checkin/getCatIdByEntInfo.do";
        WebRequest request = new WebRequest(new URL(post_url), HttpMethod.POST);
        //设置代理地址和端口为了fidller 监控
        request.setProxyHost("127.0.0.1");
        request.setProxyPort(8888);

        String v_json = "{\"data\":[{\"name\":\"notNoAndCerNoPanel\",\"vtype\":\"formpanel\",\"data\":{\"notNo\":\""+checknum+"\",\"cerNo\":\""+checkcardno+"\"}}]}";
        List<com.gargoylesoftware.htmlunit.util.NameValuePair> vp_list = new ArrayList<com.gargoylesoftware.htmlunit.util.NameValuePair>();
        com.gargoylesoftware.htmlunit.util.NameValuePair parme = new com.gargoylesoftware.htmlunit.util.NameValuePair("postData",v_json);
        vp_list.add(parme);

        request.setRequestParameters(vp_list);
        WebResponse response = HtmlUnitTool.getHtmlTool().getWc().getWebConnection().getResponse(request);
        String ret_str_temp = response.getContentAsString();

        System.out.println("ret_str_temp =============>"+ret_str_temp);
        JSONObject retobj = JSONObject.parseObject(ret_str_temp);
        System.out.println("--------------->"+retobj.toString());

        if(null != retobj && retobj.containsKey("data")){//返回结果
            JSONArray array = retobj.getJSONArray("data");
            String enter_id_str = "";//工商局系统企业库id
            for(int i= 0 ,len = array.size();i<len;i++){
                JSONObject jjo = array.getJSONObject(i);
                if(jjo.getString("name").equals("nameId")){
                    enter_id_str = jjo.getString("data");
                    break;
                }
            }
            if(null != se && !enter_id_str.equals("")){
                se.setEnter_id(enter_id_str);
            }
        }

        return retid_id;
    }
    /**
     * 通过按钮调用请求。click不稳定。也可以使用异步请求方式
     * @param checknum
     * @param checkcardno
     * @throws Exception
     */
    public String addnewsetup(String checknum,String checkcardno,SetupTaskEnitty se)throws Exception{
        String url = "";

        if(null == checknum || "".equals(checknum) || null == checkcardno || "".equals(checkcardno))
            return url;

        String checknuminput = "comp_j_notNo_1006_text";
        String checkcardoinput = "comp_j_cerNo_1007_input";

        HtmlPage page = HtmlUnitTool.getHtmlTool().getPage(start_new_wangdeng_url);

        DomElement checknumbdom = page.getElementById(checknuminput);
        DomElement checkcarddom = page.getElementById(checkcardoinput);

        checknumbdom.setNodeValue(checknum);
        checkcarddom.setNodeValue(checkcardno);

        //提交
        String enpriseid = HtmlUnitTool.getHtmlTool().sendSetup(page);
        DomNodeList<HtmlElement> domNodeList = page.getElementById("notNoAndCerNoPanel").getElementsByTagName("span");
        if(domNodeList != null && domNodeList.size() > 0){
            Iterator it = domNodeList.iterator();
            while (it.hasNext()){
                HtmlElement element = (HtmlElement) it.next();
                String style = element.getAttribute("class");
                if(style.equals("button sure")){
                    Page cpage = element.click();
                    Thread.sleep(5000l);//需要一些时间等待异步操作完成
                    WebResponse response = cpage.getWebResponse();
                    System.out.println("click response is "+response.getContentAsString());
                    break;
                }
            }
        }
        if(null != se){
            se.setEnter_id(enpriseid);
            se.setCurrent_step(1);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
//        basicInfo(se);
        return enpriseid;
    }

    /**
     * 基本信息
     * @param se
     * @return
     * @throws Exception
     */
    public String basicInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";
        //demo调试，se 为空。生产时放开校验
//        if(se == null)
//            return ret;
//        enterprise_basic_info_url = enterprise_basic_info_url + "?gid="+se.getEnter_id();
        enterprise_basic_info_url = enterprise_basic_info_url + "?gid=" + enter_id_demo;
        LvEnterpriseEntity lvEnterpriseEntity = null;
        //EnterpriseBuz.enterpriseBuz.getEnterpriseBasicInfo(se.getSetup_enterprise_id());//在调试过程中使用demo，生产中使用正常查询



        HtmlPage base_page = HtmlUnitTool.getHtmlTool().getPage(enterprise_basic_info_url);
        if(base_page != null){
            //注册资本 lvEnterpriseEntity.getRegCapital()
            base_page.getElementById("comp_j_12890644_1017_input").setNodeValue("100");
            //营业期限EnterpriseBuz.enterpriseBuz.getBussinessDuration(lvEnterpriseEntity.getEnterpriseId())
            base_page.getElementById("comp_j_05158801_1018_out").setNodeValue("20");
            //是否属于特殊行业
            base_page.getElementById("comp_j_75387240_1019_text").setNodeValue("不是特殊行业");
            //住所(经营场所) lvEnterpriseEntity.getAddressFormat()
            base_page.getElementById("comp_j_51043267_1021_input").setNodeValue("北京市海淀区海淀西大街36号5层515-01");
            //生成经营地 lvEnterpriseEntity.getAddressFormat()
            base_page.getElementById("comp_j_50507866_1023_input").setNodeValue("北京市海淀区海淀西大街36号5层515-01");
            //住所产权人 EnterpriseBuz.enterpriseBuz.getOwenerName(lvEnterpriseEntity.getEnterpriseId())
            base_page.getElementById("comp_j_91287559_1024_input").setNodeValue("北京嘉成优创企业文化有限公司");
            //住所产权类型
            base_page.getElementById("comp_j_37967443_1025_text").setNodeValue("有房产证");
            //是否在以下区域
            base_page.getElementById("comp_j_77573978_1028_text").setNodeValue("");
            //房屋用途
            base_page.getElementById("comp_j_60393665_1030_text").setNodeValue("商业");
            //*是否市级国有资产监督管理机构履行出资人职责的公司以及该公司设立的控股50%以上的公司：
            base_page.getElementById("comp_j_02642723_1031_input").setNodeValue("0");
            //执照副本数
            base_page.getElementById("comp_j_55509076_1032_input").setNodeValue("1");

            String range_demo = "技术开发、技术推广、技术咨询、技术服务、技术转让；应用软件服务；软件开发；软件咨询；产品设计；模型设计；包装装潢设计；经济贸易咨询；公共关系服务；会议服务；工程和技术研究与试验发展；数据处理（数据处理中的银行卡中心、PUE值在1.5以上的云计算数据中心除外）";
            //经营范围  lvEnterpriseEntity.getOperatingRange()
            base_page.getElementById("comp_j_84941608_1040_input").setNodeValue(range_demo);
            base_page.getElementById("comp_j_84941608_1040_input").blur();

            base_page.getElementById("applySetupBasic_query_button").click();//保存按钮s

        }



        if(se != null){
            se.setCurrent_step(2);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
//        shareHolderInfo(se);
        return ret;
    }

    /**
     * 股东信息
     * @param se
     * @return
     * @throws Exception
     */
    public String shareHolderInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";


        List<LvEnterprisePersonEntity> slist = EnterpriseBuz.enterpriseBuz.getShareHolderList(se.getSetup_enterprise_id());

        if(null != slist && slist.size() > 0){
            Map<String,LvEnterprisePersonEntity> pmap = new HashMap<String, LvEnterprisePersonEntity>();
            for(LvEnterprisePersonEntity lve : slist){
                pmap.put(lve.getName(),lve);
            }

            HtmlPage page = HtmlUnitTool.getHtmlTool().getPage(Share_holder_url+"?gid="+se.getEnter_id());
            DomElement parent = page.getElementById("personList").getNextElementSibling();
            Iterable<DomElement> it = parent.getChildElements();
            Iterator iterable = it.iterator();
            while (iterable.hasNext()){
                LvEnterprisePersonEntity lve = null;
                DomElement dom = (DomElement) iterable.next();
                DomElement nameDom = dom.getElementsByTagName("p").get(0);
                if(nameDom != null){
                    String holder_name = nameDom.asText();
                    lve = pmap.get(holder_name);
                    PersonExtEntity personExtEntity = EnterpriseBuz.enterpriseBuz.loadPersonExtBypersonidAndEnterpriseid(lve.getId(),se.getSetup_enterprise_id());

                    if(lve != null && personExtEntity != null){
                        HtmlPage npp = dom.click();

                        //身份证号
                        npp.getElementById("comp_j_49475201_1008_input").setNodeValue(lve.getIdNum());
                        //民族
                        npp.getElementById("comp_j_57562681_1010_input").setNodeValue(personExtEntity.getMz_code());
                        //省份
                        npp.getElementById("comp_j_04391941_1014_input").setNodeValue(personExtEntity.getProvnice_code());
                        //具体地址
                        npp.getElementById("comp_j_26570285_1016_input").setNodeValue(personExtEntity.getId_address());

                        npp.getElementById("comp_j_46491663_1018_input").setNodeValue(personExtEntity.getChuzi());
                        npp.getElementById("comp_j_97250463_1019_input").setNodeValue(personExtEntity.getChuzi_type());
                        npp.getElementById("comp_j_61157142_1020_text").setNodeValue(personExtEntity.getChuzi_time());

                        //保存按钮
                        npp.getElementById("applySetupInvPersonEdit_query_button").click();
                    }
                }

            }

        }


        if(se != null){
            se.setCurrent_step(3);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        mainMenberInfo(se);
        return ret;
    }

    /**
     * 主要成员
     * @param se
     * @return
     * @throws Exception
     */
    public String mainMenberInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";
        if(se != null){
            se.setCurrent_step(4);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        contactInfo(se);
        return ret;
    }

    /**
     * 企业联系人
     * @param se
     * @return
     * @throws Exception
     */
    public String contactInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";

        LvEnterpriseEntity enterpriseEntity =EnterpriseBuz.enterpriseBuz.getEnterpriseBasicInfo(se.getSetup_enterprise_id());
        HtmlPage contact_page = HtmlUnitTool.getHtmlTool().getPage(contact_info_url+"?gid="+se.getEnter_id());
        if(null != contact_page){
            LvEnterprisePersonEntity contact_person = EnterpriseBuz.enterpriseBuz.getEnterpriseContacts(se.getSetup_enterprise_id());
            LvEnterprisePersonEntity final_contact = EnterpriseBuz.enterpriseBuz.getEnterpriseFinalContact(se.getSetup_enterprise_id());

            contact_page.getElementById("comp_j_57649086_1003_text").setNodeValue(contact_person.getName());
            contact_page.getElementById("comp_j_55136522_1005_input").setNodeValue(contact_person.getIdType());
            contact_page.getElementById("comp_j_52029036_1006_input").setNodeValue(contact_person.getIdNum());
            contact_page.getElementById("comp_j_58672077_1007_input").setNodeValue(contact_person.getPhoneNum());
            contact_page.getElementById("comp_j_15078009_1008_input").setNodeValue(contact_person.getFixedPhone());
            contact_page.getElementById("comp_j_05103545_1009_input").setNodeValue(contact_person.getEmail());

            contact_page.getElementById("comp_j_20051369_1012_text").setNodeValue(final_contact.getName());
            contact_page.getElementById("comp_j_01681784_1014_input").setNodeValue(final_contact.getIdType());
            contact_page.getElementById("comp_j_61445331_1015_input").setNodeValue(final_contact.getIdNum());
            contact_page.getElementById("comp_j_77565821_1016_input").setNodeValue(final_contact.getPhoneNum());
        }



        if(se != null){
            se.setCurrent_step(5);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }



        fileUploadInfo(se);
        return ret;
    }

    /**
     * 文件上传信息
     * @param se
     * @return
     * @throws Exception
     */
    public String fileUploadInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";
        if(se != null){
            se.setCurrent_step(6);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        extMessageInfo(se);
        return ret;
    }

    /**\
     * 补充信息
     * @param se
     * @return
     * @throws Exception
     */
    public String extMessageInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";

        if(se == null)
            return ret;
        HtmlPage message_page = HtmlUnitTool.getHtmlTool().getPage(ext_info_url+"?gid="+se.getEnter_id());
        if(null == message_page)
            return ret;


        //从业人数
        message_page.getElementById("comp_j_45693887_1015_input");
        //本市人数
        message_page.getElementById("comp_j_22648004_1016_input");
        //外地人数
        message_page.getElementById("comp_j_37000686_1017_input");
        //其中女性人数
        message_page.getElementById("comp_j_95617940_1018_input");
        //安置下岗失业人数
        message_page.getElementById("comp_j_84490117_1019_input");

        if(se != null){
            se.setCurrent_step(7);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        entBaInfo(se);
        return ret;
    }

    /**
     * 备案填报

     * @param se
     * @return
     * @throws Exception
     */
    public String entBaInfo(SetupTaskEnitty se)throws Exception{
        String ret = "";
        if(se != null){
            se.setCurrent_step(8);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        preViewPage(se);
        return ret;
    }

    /**
     * 预览页面

     * @param se
     * @return
     * @throws Exception
     */
    public String preViewPage(SetupTaskEnitty se)throws Exception{
        String ret = "";
        if(se != null){
            se.setSetup_task_state(2);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        return ret;
    }

}
