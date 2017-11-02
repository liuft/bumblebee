package com.jx.jebe.bumble.buz;

import com.jx.jebe.bumble.dao.AccDaoHandler;
import com.jx.jebe.bumble.dao.entity.GSAccountEntity;

/**
 * Created by xiaowei on 17/11/2.
 */
public class AccountBuz {
    private AccountBuz(){

    }
    private static AccountBuz accountBuz = null;
    private static Object lock  = new Object();
    public static AccountBuz getAccountBuz(){
        if(null == accountBuz){
            synchronized (lock){
                if(null == accountBuz){
                    accountBuz = new AccountBuz();
                }
            }
        }
        return accountBuz;
    }
    public GSAccountEntity getGsacountByid(long accid)throws Exception{
        return AccDaoHandler.getAccDaoHandler().loadGSAccountByid(accid);
    }
}
