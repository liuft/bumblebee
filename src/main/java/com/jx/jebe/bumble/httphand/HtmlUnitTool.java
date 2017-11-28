package com.jx.jebe.bumble.httphand;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.jx.jebe.bumble.buz.EnterpriseBuz;


import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HtmlUnitTool {
    private static WebClient wc ;
    private static HtmlUnitTool tool = null;
    private static Object lock = new Object();
    private HtmlUnitTool(){
        wc = new WebClient(BrowserVersion.CHROME,"127.0.0.1",8888);
//        wc = new WebClient();
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setCssEnabled(false);

        wc.getOptions().setRedirectEnabled(true);
        wc.getCookieManager().setCookiesEnabled(true);
        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        wc.getOptions().setTimeout(30000);
    }

    public static HtmlUnitTool getHtmlTool(){
        if(null == tool){
            synchronized (lock){
                if(null == tool){
                    tool = new HtmlUnitTool();
                }
            }
        }
        return tool;
    }
    public HtmlPage getPage(String url)throws Exception{

        return wc.getPage(url);
    }

    public WebClient getWc() {
        return wc;
    }

    /**
     * 设立提交申请
     * @param page
     * @return
     * @throws Exception
     */
    public String sendSetup(HtmlPage page)throws Exception{
        String enterpriseid = "";
        return enterpriseid;
    }
    public static void main(String[] argo){

        WebClient webClient = HtmlUnitTool.getHtmlTool().getWc();
        try {
            WebRequest request = new WebRequest(new URL("http://wsdj.baic.gov.cn/system/login.do"));
            request.setHttpMethod(HttpMethod.POST);
            wc.getCookieManager().addCookie(new Cookie("wsdj.baic.gov.cn","yunsuo_session_verify","a88dc6ebf24b49e75be4150318c52343"));
            wc.getCookieManager().addCookie(new Cookie("wsdj.baic.gov.cn","SESSION","724881d0-640c-4bb3-9e5c-b7b64fb776c9"));
            List<NameValuePair> list = new ArrayList<NameValuePair>();

            list.add(new NameValuePair("login_name","robbin1995"));
            list.add(new NameValuePair("user_pwd","XWLZ2017"));
            list.add(new NameValuePair("verify_code","TDTQ"));

            request.setRequestParameters(list);
            request.setProxyHost("127.0.0.1");
            request.setProxyPort(8888);
            WebResponse response = webClient.getWebConnection().getResponse(request);

            JSONObject ret = JSONObject.parseObject(response.getContentAsString());
            //demo 测试。生产中是反过来的，生产中从wdhttphandler调用htmlunit
            if(ret.containsKey("result") && ret.getString("result").equals("3")){//登录成功

                try {
                    String checknumber = EnterpriseBuz.enterpriseBuz.getEntercheckedNameCode("908764");
                    String cardno = EnterpriseBuz.enterpriseBuz.getEntercheckedNameCerNo("908764");
                    WDHttpHandler.loadWDHandler().addnewsetupByajax(checknumber,cardno,null);
//                    WDHttpHandler.loadWDHandler().basicInfo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }







            Set<Cookie> cookies = webClient.getCookieManager().getCookies();
            Iterator it = cookies.iterator();
            while (it.hasNext()){
                Cookie cookie = (Cookie) it.next();
                System.out.println(cookie.getName()+"======"+cookie.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




//        HtmlPage page = null;
//        try {
//            page = HtmlUnitTool.getHtmlTool().getPage("http://wsdj.baic.gov.cn");
////            wc.getWebConnection().getResponse(new Http);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(page != null){
//            DomElement login_name = page.getElementById("login_name");
//            DomElement password = page.getElementById("user_pwd");
//            DomElement verify_code = page.getElementById("verify_code");
//
//            login_name.setAttribute("value","robbin1995");
//            password.setAttribute("value","XWLZ2017");
//            verify_code.setAttribute("value","3J54");
//
//            wc.getCookieManager().clearCookies();
//            wc.getCookieManager().addCookie(new Cookie("wsdj.baic.gov.cn","yunsuo_session_verify","a88dc6ebf24b49e75be4150318c52343"));
//            wc.getCookieManager().addCookie(new Cookie("wsdj.baic.gov.cn","SESSION","185a1367-8cb0-48a8-b781-dd2ab5bd056a"));
//
//            try {
//                System.out.println("login page "+page.getElementById("btnCheck").click().getWebResponse().getContentAsString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
