package com.jx.jebe.bumble.dao;

import com.jx.blackface.servicetool.common.CommonService;
import com.jx.blackface.servicetool.common.IDHelper;
import com.jx.jebe.bumble.dao.entity.GSAccountEntity;

import java.util.List;

/**
 * Created by xiaowei on 17/11/2.
 */
public class AccDaoHandler extends CommonService {

    private AccDaoHandler(){

    }
    private static Object lock = new Object();
    private static AccDaoHandler accDaoHandler = null;
    public static AccDaoHandler getAccDaoHandler(){
        if(null == accDaoHandler){
            synchronized (lock){
                if(null == accDaoHandler){
                    accDaoHandler = new AccDaoHandler();
                }
            }
        }
        return accDaoHandler;
    }
    public long addNewAccount(GSAccountEntity accountEntity)throws Exception{
        long rid = 0;
        if(null != accountEntity){
            long aid = IDHelper.getUniqueID();
            accountEntity.setAcc_id(aid);
            insertObjec(accountEntity);
            rid = aid;
        }
        return rid;
    }
    public GSAccountEntity loadGSAccountByid(long accid)throws Exception{
        return (GSAccountEntity) getObjectByid(accid,GSAccountEntity.class);
    }
    public List<GSAccountEntity> getAccountListbypage(String condition,int pageindex,int pagesize,String sortby)throws Exception{
        return (List<GSAccountEntity>) getListBypage(GSAccountEntity.class,condition,pageindex,pagesize,sortby);
    }
    public int getCountbycondition(String condition)throws Exception{
        return getCountBycondition(GSAccountEntity.class,condition);
    }
    public void updateAccount(GSAccountEntity gsAccountEntity)throws Exception{
        updateObject(gsAccountEntity);
    }
}
