package com.jx.jebe.bumble.httphand;

import com.alibaba.fastjson.JSONObject;
import com.jx.jebe.bumble.buz.AccountBuz;
import com.jx.jebe.bumble.dao.entity.GSAccountEntity;
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
    public void addnewsetup(String checknum,String checkcardno)throws Exception{
        String url = "";

    }
}
