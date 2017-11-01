package com.jx.jebe.bumble.dao;

import com.jx.blackface.servicetool.common.CommonService;
import com.jx.blackface.servicetool.common.IDHelper;
import com.jx.jebe.bumble.dao.entity.SetupTaskEnitty;

import java.util.Date;
import java.util.List;

/**
 * Created by xiaowei on 17/10/31.
 */
public class DaoHandler extends CommonService {
    public static DaoHandler daoHandler = null;
    private static Object lock = new Object();
    static {
        if(null == daoHandler){
            synchronized (lock){
               if(null == daoHandler){
                   daoHandler = new DaoHandler();
               }
            }
        }
    }

    public long insertGSTask(SetupTaskEnitty setupTaskEnitty)throws Exception{
        long rid = 0;
        if(null != setupTaskEnitty){
            rid = IDHelper.getUniqueID();
            setupTaskEnitty.setSetup_id(rid);
            setupTaskEnitty.setSetup_add_date(new Date());
            insertObjec(setupTaskEnitty);
        }
        return rid;
    }
    public void updateGSTask(SetupTaskEnitty setupTaskEnitty)throws Exception{
        updateObject(setupTaskEnitty);
    }
    public int loadCountbycondition(String condition)throws Exception{
        return getCountBycondition(SetupTaskEnitty.class,condition);
    }
    public List<SetupTaskEnitty> getGSTaskListbypage(String condition,int pageindex,int pagesize,String sortby)throws Exception{

        return (List<SetupTaskEnitty>) getListBypage(SetupTaskEnitty.class,condition,pageindex,pagesize,sortby);
    }
    public SetupTaskEnitty loadSetupTaskEntityByid(long gstaskid)throws Exception{
        return (SetupTaskEnitty) getObjectByid(gstaskid,SetupTaskEnitty.class);
    }
}
