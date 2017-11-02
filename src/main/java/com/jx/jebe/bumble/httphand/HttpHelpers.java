package com.jx.jebe.bumble.httphand;

/**
 * Created by xiaowei on 17/10/31.
 */
public class HttpHelpers {

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
    public String logings(String username,String pwd)throws Exception{
        String sessionid = "";
        return sessionid;
    }

}
