package com.jx.jebe.bumble.httphand;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jx.jebe.bumble.buz.AccountBuz;
import com.jx.jebe.bumble.buz.WangdengBuz;
import com.jx.jebe.bumble.dao.entity.GSAccountEntity;
import com.jx.jebe.bumble.dao.entity.SetupTaskEnitty;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaowei on 17/11/2.
 */
public class WDHttpHandler {
    public WDHttpHandler(){

    }
    private static final String start_new_wangdeng_url = "http://wsdj.baic.gov.cn/page/company/add/set_up_my_names.html";
    //企业基本信息
    private static  String enterprise_basic_info_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/basic_info.html";
    //企业股东信息
    private static  String Share_holder_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/inv.html?gid=7b53aabf46b84a84964ede956c61174e";
    //主要人员信息
    private static  String main_member_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/member.html?gid=7b53aabf46b84a84964ede956c61174e";
    //企业联系人信息
    private static  String contact_info_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/contact.html?gid=7b53aabf46b84a84964ede956c61174e";
    //文件上传
    private static  String file_upload_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/upload.html?gid=7b53aabf46b84a84964ede956c61174e";
    //补充信息
    private static  String ext_info_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/entsnd.html?gid=7b53aabf46b84a84964ede956c61174e";
    //备案填报
    private static  String ent_ba_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/entba.html?gid=7b53aabf46b84a84964ede956c61174e";
    //预览页面
    private static  String pre_view_url = "http://wsdj.baic.gov.cn/ebaic/page/apply-wd/setup/preview.html?gid=7b53aabf46b84a84964ede956c61174e";


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
        String index_url = "http://wsdj.baic.gov.cn/";

        try {
            HttpHelpers.getHttpHelpers().sendgetrequest(index_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String login_url = "http://wsdj.baic.gov.cn/system/login.do";
        try {
            System.out.println(HttpHelpers.getHttpHelpers().sendPostRequest(login_url,"{login_name:'robbin1995',user_pwd:'XWLZ2017',verify_code:'"+WDHttpHandler.loadWDHandler().readVerfiycode()+"'}"));
//            Map<String,String> map = new HashMap<String, String>();
//            map.put("login_name","robbin1995");
//            map.put("user_pwd","XWLZ2017");
//            map.put("verify_code",WDHttpHandler.loadWDHandler().readVerfiycode());
//            System.out.println(HttpHelpers.getHttpHelpers().postForm(login_url,map));
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
     * 调用
     * @param checknum
     * @param checkcardno
     * @throws Exception
     */
    public String addnewsetup(String checknum,String checkcardno,SetupTaskEnitty se)throws Exception{
        String url = "";
        String checknuminput = "comp_j_notNo_1006_text";
        String checkcardoinput = "comp_j_cerNo_1007_input";

        HtmlPage page = HtmlUnitTool.getHtmlTool().getPage(start_new_wangdeng_url);
        DomElement checknumbdom = page.getElementById(checknuminput);
        DomElement checkcarddom = page.getElementById(checkcardoinput);

        checknumbdom.setNodeValue(checknum);
        checkcarddom.setNodeValue(checkcardno);

        //提交
        String enpriseid = HtmlUnitTool.getHtmlTool().sendSetup(page);

        if(null != se){
            se.setEnter_id(enpriseid);
            se.setCurrent_step(1);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        basicInfo(se);
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
        enterprise_basic_info_url = enterprise_basic_info_url + "??gid="+se.getEnter_id();
        HtmlPage base_page = HtmlUnitTool.getHtmlTool().getPage(enterprise_basic_info_url);
        if(base_page != null){

        }

        if(se != null){
            se.setCurrent_step(2);
            WangdengBuz.wdbuz.updateTaskEntity(se);
        }
        shareHolderInfo(se);
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
