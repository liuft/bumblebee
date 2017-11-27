package com.jx.jebe.bumble.httphand;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;

import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.InputStream;
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
                setDefaultCookieStore(cookieStore).
                setProxy(new HttpHost("127.0.0.1",8888,null)).
                build();
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

    public static void main(String[] args){
        String login_url = "http://wsdj.baic.gov.cn/system/login.do";
        String body = "login_name=robbin1995&user_pwd=XWLZ2017&verify_code=vlh6";
        try {
            System.out.println(HttpHelpers.getHttpHelpers().sendPostRequest(login_url,body));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        httpGet.addHeader("Accept","image/webp,image/apng,image/*,*/*;q=0.8");
        httpGet.addHeader("Accept-Encoding",":gzip, deflate");
        httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        System.out.println(url+"            "+getCookiestr());
        httpGet.addHeader("Cookie",getCookiestr());
        httpGet.addHeader("Host","wsdj.baic.gov.cn");
        httpGet.addHeader("Proxy-Connection","keep-alive");
        httpGet.addHeader("Referer","http://wsdj.baic.gov.cn/");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

        try {
            response = request(httpGet);

            Header[] responseAllHeaders = response.getAllHeaders();
            if(null != responseAllHeaders && responseAllHeaders.length > 0){
                StringBuilder sb = new StringBuilder();
                for(Header header : responseAllHeaders){
                    sb.append(header.getName()+"="+header.getValue());
                    sb.append(";");
                }
                System.out.println("response "+url+" "+sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getEntity().getContent();
    }


    public CloseableHttpResponse request(HttpGet httpGet)throws Exception{
//        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet, context);


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
            httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpGet.addHeader("Accept-Encoding","gzip, deflate");
            httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.8");
            httpGet.addHeader("Cache-Control","max-age=0");
            System.out.println(url+"        "+getCookiestr());
            httpGet.addHeader("Cookie",getCookiestr());
            httpGet.addHeader("Host","wsdj.baic.gov.cn");
            httpGet.addHeader("Proxy-Connection","keep-alive");
            httpGet.addHeader("Upgrade-Insecure-Requests","1");
            httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

            response = request(httpGet);
            Header[] responseAllHeaders = response.getAllHeaders();
            if(null != responseAllHeaders && responseAllHeaders.length > 0){
                StringBuilder sb = new StringBuilder();
                for(Header header : responseAllHeaders){
                    sb.append(header.getName()+"="+header.getValue());
                    sb.append(";");
                }
                System.out.println("response "+url+" "+sb.toString());
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
    public String postForm(String actionurl, Map<String,String> map)throws Exception{
        HttpPost post = new HttpPost(actionurl);
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        post.addHeader("Accept","*/*");
        post.addHeader("Accept-Encoding","gzip, deflate");
        post.addHeader("Accept-Language","zh-CN,zh;q=0.8");
//        post.addHeader("Content-Length","56");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println(actionurl+"          "+getCookiestr());
        post.addHeader("Cookie",getCookiestr());
        post.addHeader("Host","wsdj.baic.gov.cn");
        post.addHeader("Origin","http://wsdj.baic.gov.cn");
        post.addHeader("Proxy-Connection","keep-alive");
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

        httpPost.addHeader("Accept","*/*");
        httpPost.addHeader("Accept-Encoding","gzip, deflate");
        httpPost.addHeader("Accept-Language","zh-CN,zh;q=0.8");
//        httpPost.addHeader("Content-Length","56");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println(url+"          "+getCookiestr());
//        httpPost.addHeader("Cookie",getCookiestr());
        httpPost.addHeader("Cookie","Cookie: SESSION=ef1aed13-e576-47f4-b869-217e551a27dc; yunsuo_session_verify=e0eff6d66750a68710ede97b53930ff2");
        httpPost.addHeader("Host","wsdj.baic.gov.cn");
        httpPost.addHeader("Origin","http://wsdj.baic.gov.cn");
        httpPost.addHeader("Proxy-Connection","keep-alive");
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
//            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), "utf-8");
            String datastr = data.toString();

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(datastr.getBytes());
            httpPost.setEntity(byteArrayEntity);

            response = httpClient.execute(httpPost, context);


            Header[] responseAllHeaders = response.getAllHeaders();
            if(null != responseAllHeaders && responseAllHeaders.length > 0){
                StringBuilder sb = new StringBuilder();
                for(Header header : responseAllHeaders){
                    sb.append(header.getName()+"="+header.getValue());
                    sb.append(";");
                }
                System.out.println("response "+url+" "+sb.toString());
            }

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
