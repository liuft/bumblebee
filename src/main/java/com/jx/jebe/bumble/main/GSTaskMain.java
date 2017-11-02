package com.jx.jebe.bumble.main;

import com.jx.jebe.bumble.beans.TaskEntity;
import com.jx.jebe.bumble.task.AbstractTask;
import com.jx.spat.gaea.server.bootstrap.Main;

import java.util.*;

/**
 * Created by xiaowei on 17/11/1.
 */
public class GSTaskMain extends Main{
    private static List<TaskEntity> list = new ArrayList<TaskEntity>();
    static {
        initTaskConfig();
    }

    private static void initTaskConfig() {
        
    }

    public static void main(String[] args)throws Exception{
        if(null != list && list.size() > 0){
            for(final TaskEntity te : list){
                new Timer().schedule(new TimerTask(){
                    public void run() {
                        try {
                            AbstractTask tt = (AbstractTask) te.getClz().newInstance();
                            try {
                                tt.runtask();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                },te.getSimpleTriggerexpression(),te.getSimpleTriggerexpression());
            }
        }
        try {
            Main.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
