package com.jx.jebe.bumble.httphand;

import com.alibaba.fastjson.JSONObject;
import com.jx.jebe.bumble.buz.AccountBuz;
import com.jx.jebe.bumble.dao.entity.GSAccountEntity;

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
        String cookies = "";
        GSAccountEntity accountEntity =  AccountBuz.getAccountBuz().getGsacountByid(accid);
        if(null != accountEntity){
            cookies = HttpHelpers.getHttpHelpers().logings(accountEntity.getAcc_login_name(),accountEntity.getAcc_login_pwd());

        }
        return cookies;
    }
}
