package com.jx.jebe.bumble.task;


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jx.jebe.bumble.buz.EnterpriseBuz;
import com.jx.jebe.bumble.buz.WangdengBuz;
import com.jx.jebe.bumble.dao.entity.SetupTaskEnitty;
import com.jx.jebe.bumble.httphand.WDHttpHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaowei on 17/11/1.
 */
public class WDTask extends AbstractTask{
    private static Map<String,Method> step_map = new HashMap<String, Method>();
    static {
        try {
            step_map.put("1",WDHttpHandler.class.getMethod("basicInfo",String.class));
            step_map.put("2",WDHttpHandler.class.getMethod("shareHolderInfo",String.class));
            step_map.put("3",WDHttpHandler.class.getMethod("mainMenberInfo",String.class));
            step_map.put("4",WDHttpHandler.class.getMethod("contactInfo",String.class));
            step_map.put("5",WDHttpHandler.class.getMethod("fileUploadInfo",String.class));
            step_map.put("6",WDHttpHandler.class.getMethod("extMessageInfo",String.class));
            step_map.put("7",WDHttpHandler.class.getMethod("entBaInfo",String.class));
            step_map.put("8",WDHttpHandler.class.getMethod("preViewPage",String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    //自动网登的任务开启
    public void runtask() throws Exception {
        List<SetupTaskEnitty> list = WangdengBuz.wdbuz.loadSetUplist();
        if(null != list){
            for(SetupTaskEnitty se : list){

                int step = se.getCurrent_step();

                if(step > 0){
                    startTask(se);
                }else {
                    long account = se.getAcc_id();
                    String enterprise_id = se.getSetup_enterprise_id()+"";

                    String checknumber = EnterpriseBuz.enterpriseBuz.getEntercheckedNameCode(enterprise_id);
                    String cardno = EnterpriseBuz.enterpriseBuz.getEntercheckedNameCerNo(enterprise_id);
                    WDHttpHandler.loadWDHandler().addnewsetup(checknumber,cardno,se);

                }
                Thread.sleep(5000l);

            }
        }
    }

    public void startTask(SetupTaskEnitty se)throws Exception{
        Method  method = step_map.get(se.getCurrent_step()+"");
        method.invoke(WDHttpHandler.loadWDHandler(),se);
    }

}
