package com.jx.jebe.bumble.httphand;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import net.sourceforge.tess4j.Tesseract;


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

    public static void main(String[] argo){
        WebClient wc = new WebClient(BrowserVersion.FIREFOX_45);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setCssEnabled(false);
        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        wc.getOptions().setTimeout(30000);

        HtmlPage page = null;
        try {
            page = wc.getPage("http://wsdj.baic.gov.cn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(page != null){
            DomElement login_name = page.getElementById("login_name");
            DomElement password = page.getElementById("user_pwd");
            DomElement verify_code = page.getElementById("verify_code");


            String ret_code = "";
            HtmlImage image = (HtmlImage) page.getElementById("verify-code");
            try {
                ImageReader reader = image.getImageReader();
                BufferedImage bufferedImage = reader.read(0);
                ret_code = new Tesseract().doOCR(bufferedImage);
                ImageIO.write(bufferedImage,"jpg",new File("d:\\ttt.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(ret_code);
            //page.getElementById("verify-code").getAttribute("src").getBytes();
            login_name.setNodeValue("robbin1995");
            password.setNodeValue("XWLZ2017");
            verify_code.setNodeValue(ret_code);

            try {
                HtmlPage pp = page.getElementById("btnCheck").click();
                System.out.println(pp.asXml());
            } catch (IOException e) {
                e.printStackTrace();
            }

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
        }
    }
}
