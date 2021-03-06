package com.jx.jebe.bumble.buz;

import com.jx.jebe.bumble.dao.DaoHandler;
import com.jx.jebe.bumble.dao.entity.SetupTaskEnitty;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaowei on 17/10/31.
 */
public class WangdengBuz {
    public static WangdengBuz wdbuz = new WangdengBuz();
    public long addNewGswdTask(long taskid, long enterpriseid, Map<String,String> map)throws Exception{
        SetupTaskEnitty setupTaskEnitty = new SetupTaskEnitty();
        setupTaskEnitty.setSetup_task_state(0);
        setupTaskEnitty.setSetup_enterprise_id(enterpriseid);
        setupTaskEnitty.setAcc_id(0);
        setupTaskEnitty.setSetup_workflow_tid(taskid);
        return DaoHandler.daoHandler.insertGSTask(setupTaskEnitty);
    }
    public List<SetupTaskEnitty> loadSetUplist()throws Exception{
        List<SetupTaskEnitty> stlist = null;
        return stlist;
    }
    public void updateTaskEntity(SetupTaskEnitty setupTaskEnitty)throws Exception{
        if(setupTaskEnitty != null){
            setupTaskEnitty.setSetup_lastrun_date(new Date());
            DaoHandler.daoHandler.updateObject(setupTaskEnitty);
        }
    }
}
