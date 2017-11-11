package com.jx.jebe.bumble.httphand;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jx.jebe.bumble.tools.TestTess4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaowei on 17/10/31.
 */
public class HttpHelpers {

    public static CloseableHttpClient httpClient = null;
    public static HttpClientContext context = null;
    public static CookieStore cookieStore = null;
    public static RequestConfig requestConfig = null;
    private static String verfy_code = "";

    static {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpClient = HttpClientBuilder.create().
                setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).
                setRedirectStrategy(new DefaultRedirectStrategy()).
                setDefaultRequestConfig(requestConfig).
                setDefaultCookieStore(cookieStore).build();
    }

    private HttpHelpers(){

    }
    private static HttpHelpers httpHelpers = null;
    private static Object lock = new Object();
    public static HttpHelpers getHttpHelpers(){
        if(null == httpHelpers){
            synchronized (lock){
                if(null == httpHelpers){
                    httpHelpers = new HttpHelpers();
                }
            }
        }
        return httpHelpers;
    }

    private String getCookiestr()throws Exception{
        StringBuilder sb = new StringBuilder();
        if(cookieStore != null){
            List<Cookie> list = cookieStore.getCookies();
            for(Cookie cookie : list){
                sb.append(cookie.getName()+"="+cookie.getValue()+"; ");
            }
        }
        if(sb.length() > 2){
            return sb.substring(0,sb.length() - 2);
        }
        return sb.toString();
    }
    public InputStream loadverfycode(String url)throws Exception{
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        Header[] headers = new Header[8];
        headers[0] = new BasicHeader("Accept","image/webp,image/apng,image/*,*/*;q=0.8");
        headers[1] = new BasicHeader("Accept-Encoding",":gzip, deflate");
        headers[2] = new BasicHeader("Accept-Language","zh-CN,zh;q=0.8");
        headers[3] = new BasicHeader("Connection","keep-alive");
        headers[4] = new BasicHeader("Cookie",getCookiestr());
        headers[5] = new BasicHeader("Host","wsdj.baic.gov.cn");
        headers[6] = new BasicHeader("Referer","http://wsdj.baic.gov.cn/");
        headers[7] = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        httpGet.setHeaders(headers);
        try {
            response = request(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getEntity().getContent();
    }


    public CloseableHttpResponse request(String url)throws Exception{
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            if(null != verfy_code && !"".equals(verfy_code)){
                cookieStore.addCookie(new BasicClientCookie("yunsuo_session_verify",verfy_code));
//                context.getCookieStore().addCookie(new BasicClientCookie("yunsuo_session_verify",verfy_code));
            }

            List<Cookie> beforecookies = null;
            if (null != cookieStore) {
                beforecookies = cookieStore.getCookies();
            }

            if (null != beforecookies) {
                for (Cookie cookie : beforecookies) {
                    System.out.println(url + " before  cookie " + cookie.getName() + "==" + cookie.getValue());
                }
            } else {
                System.out.println(url + " before  cookie null.......");
            }

            response = httpClient.execute(httpGet, context);
            List<Cookie> after = context.getCookieStore().getCookies();
            for (Cookie cookie : after) {
                if(cookie.getName().equalsIgnoreCase("yunsuo_session_verify")){
                    verfy_code = cookie.getValue();
                }
                System.out.println(url + " after cookie " + cookie.getName() + "==" + cookie.getValue());
            }
        }catch (Exception e){
            if(null != response){
                response.close();
            }
        }
        return response;
    }
    public String sendgetrequest(String url)throws Exception{
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {

            response = request(url);
            HttpEntity entity = response.getEntity();



        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public String postForm(String actionurl, Map<String,String> map)throws Exception{
        HttpPost post = new HttpPost(actionurl);
//        CloseableHttpClient httpClient = HttpClients.createDefault();

        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        post.addHeader("Connection","keep-alive");
        post.addHeader("Referer","http://wsdj.baic.gov.cn/");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        post.addHeader("X-Requested-With","XMLHttpRequest");

        List<NameValuePair> plist = new ArrayList<NameValuePair>();
        if(null != map && map.size() > 0){
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry entry = (Map.Entry) it.next();
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey()+"",entry.getValue()+"");
                plist.add(nameValuePair);
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(plist,"utf-8");
        post.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(post, context);
        if(response != null){
            int retcode = response.getStatusLine().getStatusCode();
            String retmsg = EntityUtils.toString(response.getEntity());
            System.out.println(retcode+"            "+retmsg);
            return retmsg;
        }

        return null;

    }
    public String sendPostRequest(String url,Object data)throws Exception{
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        httpPost.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        httpPost.addHeader("Accept-Encoding","gzip, deflate");
        httpPost.addHeader("Accept","*/*");
        httpPost.addHeader("Cookie",getCookiestr());
        httpPost.addHeader("Host","wsdj.baic.gov.cn");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        httpPost.addHeader("Connection","keep-alive");
        httpPost.addHeader("Referer","http://wsdj.baic.gov.cn/");
        httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        httpPost.addHeader("X-Requested-With","XMLHttpRequest");


        try {
            List<Cookie> beforecookies = null;
            if (null != cookieStore) {
                beforecookies = cookieStore.getCookies();
            }

            if (null != cookieStore) {
                beforecookies = cookieStore.getCookies();
            }

            if (null != beforecookies) {
                for (Cookie cookie : beforecookies) {
                    System.out.println(url + " before  cookie " + cookie.getName() + "==" + cookie.getValue());
                }
            } else {
                System.out.println(url + " before  cookie null.......");
            }
            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), "utf-8");

            httpPost.setEntity(requestEntity);

            response = httpClient.execute(httpPost, context);

            List<Cookie> after = context.getCookieStore().getCookies();
            for (Cookie cookie : after) {
                System.out.println(url + " after cookie " + cookie.getName() + "==" + cookie.getValue());
            }
            if (response.getStatusLine().getStatusCode() != 200) {

                System.out.println("request url failed, http code=" + response.getStatusLine().getStatusCode()
                        + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);

        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
