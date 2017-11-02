package com.jx.jebe.bumble.beans;

/**
 * Created by xiaowei on 17/11/1.
 */
public class TaskEntity {
    private String runtype;//
    private int SimpleTriggerexpression;
    private String CronTriggerexpression;
    private Class clz;

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getRuntype() {
        return runtype;
    }

    public void setRuntype(String runtype) {
        this.runtype = runtype;
    }

    public int getSimpleTriggerexpression() {
        return SimpleTriggerexpression;
    }

    public void setSimpleTriggerexpression(int simpleTriggerexpression) {
        SimpleTriggerexpression = simpleTriggerexpression;
    }

    public String getCronTriggerexpression() {
        return CronTriggerexpression;
    }

    public void setCronTriggerexpression(String cronTriggerexpression) {
        CronTriggerexpression = cronTriggerexpression;
    }
}
