package com.jx.jebe.bumble.task;


import com.jx.jebe.bumble.buz.WangdengBuz;
import com.jx.jebe.bumble.dao.entity.SetupTaskEnitty;
import com.jx.jebe.bumble.httphand.WDHttpHandler;

import java.util.List;

/**
 * Created by xiaowei on 17/11/1.
 */
public class WDTask extends AbstractTask{

    //自动网登的任务开启
    public void runtask() throws Exception {
        List<SetupTaskEnitty> list = WangdengBuz.wdbuz.loadSetUplist();
        if(null != list){
            for(SetupTaskEnitty se : list){
                long account = se.getAcc_id();

                String cookies = WDHttpHandler.loadWDHandler().getLoginCookie(account);

            }
        }
    }
}
