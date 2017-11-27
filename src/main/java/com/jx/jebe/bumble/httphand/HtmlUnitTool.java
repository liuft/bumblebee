package com.jx.jebe.bumble.httphand;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;



import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

public class HtmlUnitTool {
    private static WebClient wc ;
    private static HtmlUnitTool tool = null;
    private static Object lock = new Object();
    private HtmlUnitTool(){
        wc = new WebClient(BrowserVersion.FIREFOX_45,"127.0.0.1",8888);
//        wc = new WebClient();
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setThrowExceptionOnScriptError(true);
        wc.getOptions().setCssEnabled(true);

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
        HtmlPage page = null;
        try {
            page = wc.getPage("http://wsdj.baic.gov.cn");
//            wc.getWebConnection().getResponse(new Http);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(page != null){
            DomElement login_name = page.getElementById("login_name");
            DomElement password = page.getElementById("user_pwd");
            DomElement verify_code = page.getElementById("verify_code");

            login_name.setAttribute("value","robbin1995");
            password.setAttribute("value","XWLZ2017");
            verify_code.setAttribute("value","076T");

            wc.getCookieManager().clearCookies();
            wc.getCookieManager().addCookie(new Cookie("wsdj.baic.gov.cn","yunsuo_session_verify","d7055eb88596115ec4158311a6132673"));
            wc.getCookieManager().addCookie(new Cookie("wsdj.baic.gov.cn","SESSION","b03e6cfb-f872-4f6b-89e0-24f9601df21d"));

            page.getElementById("btnCheck").click();
        }
        return wc.getPage(url);
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


        HtmlPage page = null;
        try {
            page = HtmlUnitTool.getHtmlTool().getPage("http://wsdj.baic.gov.cn");
//            wc.getWebConnection().getResponse(new Http);
        } catch (Exception e) {
            e.printStackTrace();
        }

       System.out.println(page.getWebResponse().getContentAsString());


//        if(page != null){
//            DomElement login_name = page.getElementById("login_name");
//            DomElement password = page.getElementById("user_pwd");
//            DomElement verify_code = page.getElementById("verify_code");
//
//
//            String ret_code = "";
//            HtmlImage image = (HtmlImage) page.getElementById("verify-code");
//            try {
//                ImageReader reader = image.getImageReader();
//                BufferedImage bufferedImage = reader.read(0);
//                ret_code = new Tesseract().doOCR(bufferedImage);
//                ImageIO.write(bufferedImage,"jpg",new File("d:\\ttt.jpg"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println(ret_code);
//            //page.getElementById("verify-code").getAttribute("src").getBytes();
//            login_name.setAttribute("value","robbin1995");//.setNodeValue("robbin1995");
//            password.setAttribute("value","XWLZ2017");//.setNodeValue("XWLZ2017");
//            verify_code.setAttribute("value",ret_code);
//
//            try {
//                HtmlPage pp = page.getElementById("btnCheck").click();
////                Object pp =  page.executeJavaScript("login()").getJavaScriptResult();
//                wc.waitForBackgroundJavaScript(3000l);
//                System.out.println(pp.asText());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            try {
//                WebRequest request =  new WebRequest(new URL("http://wsdj.baic.gov.cn/system/login.do"));
//                request.setHttpMethod(HttpMethod.POST);
//                List<NameValuePair> list = new ArrayList<NameValuePair>();
//                list.add(new NameValuePair("login_name","robbin1995"));
//                list.add(new NameValuePair("user_pwd","XWLZ2017"));
//                list.add(new NameValuePair("verify_code",ret_code));
//
//
//                request.setRequestParameters(list);
//                Map<String,String> map = request.getAdditionalHeaders();
//                Set<Cookie> set = wc.getCookieManager().getCookies();
//                StringBuilder sb = new StringBuilder();
//                Iterator it = set.iterator();
//                while (it.hasNext()){
//                    Cookie c = (Cookie) it.next();
//                    sb.append(c.toString()+";");
//                }
//
//                request.getAdditionalHeaders().put("Cookie",sb.substring(0,sb.length() - 1));
//                System.out.println("====="+wc.getWebConnection().getResponse(request).getContentAsString());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//            ScriptResult sr = page.executeJavaScript("$('#btnCheck').click();");
//            wc.waitForBackgroundJavaScript(30000);
//
//
//            System.out.println(sr.getNewPage().getWebResponse().getContentAsString());
//            System.out.println("*********/--------------");
//            System.out.println("cookie"+wc.getCookieManager().getCookies().toString());
//        }
    }
}
