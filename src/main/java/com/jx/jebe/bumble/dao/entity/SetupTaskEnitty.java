package com.jx.jebe.bumble.dao.entity;

import com.bj58.sfft.utility.dao.annotation.Column;
import com.bj58.sfft.utility.dao.annotation.Id;
import com.bj58.sfft.utility.dao.annotation.Table;

import java.util.Date;

/**
 * Created by xiaowei on 17/10/31.
 */
@Table(
        name = "t_bum_entsetup_tasklist"
)
public class SetupTaskEnitty {
    @Id(insertable = true)
    @Column(name = "setup_id")
    private long setup_id;
    @Column(name = "setup_add_date")
    private Date setup_add_date;
    @Column(name = "setup_workflow_tid")
    private long setup_workflow_tid;
    @Column(name = "setup_enterprise_id")
    private long setup_enterprise_id;
    @Column(name = "setup_lastrun_date")
    private Date setup_lastrun_date;
    @Column(name = "acc_id")
    private long acc_id;
    @Column(name = "setup_task_state")
    private int setup_task_state;
    @Column(name = "setup_task_sucdate")
    private Date setup_task_sucdate;

    public long getSetup_id() {
        return setup_id;
    }

    public void setSetup_id(long setup_id) {
        this.setup_id = setup_id;
    }

    public Date getSetup_add_date() {
        return setup_add_date;
    }

    public void setSetup_add_date(Date setup_add_date) {
        this.setup_add_date = setup_add_date;
    }

    public long getSetup_workflow_tid() {
        return setup_workflow_tid;
    }

    public void setSetup_workflow_tid(long setup_workflow_tid) {
        this.setup_workflow_tid = setup_workflow_tid;
    }

    public long getSetup_enterprise_id() {
        return setup_enterprise_id;
    }

    public void setSetup_enterprise_id(long setup_enterprise_id) {
        this.setup_enterprise_id = setup_enterprise_id;
    }

    public Date getSetup_lastrun_date() {
        return setup_lastrun_date;
    }

    public void setSetup_lastrun_date(Date setup_lastrun_date) {
        this.setup_lastrun_date = setup_lastrun_date;
    }

    public long getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(long acc_id) {
        this.acc_id = acc_id;
    }

    public int getSetup_task_state() {
        return setup_task_state;
    }

    public void setSetup_task_state(int setup_task_state) {
        this.setup_task_state = setup_task_state;
    }

    public Date getSetup_task_sucdate() {
        return setup_task_sucdate;
    }

    public void setSetup_task_sucdate(Date setup_task_sucdate) {
        this.setup_task_sucdate = setup_task_sucdate;
    }
}
