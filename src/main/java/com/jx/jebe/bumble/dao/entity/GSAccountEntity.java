package com.jx.jebe.bumble.dao.entity;

/**
 * Created by xiaowei on 17/10/31.
 */

import com.bj58.sfft.utility.dao.annotation.Column;
import com.bj58.sfft.utility.dao.annotation.Id;
import com.bj58.sfft.utility.dao.annotation.Table;

@Table(name = "t_bum_account")
public class GSAccountEntity {
    @Id(insertable = true)
    @Column(name = "acc_id")
    private long acc_id;
    @Column(name = "acc_login_name")
    private String acc_login_name;
    @Column(name = "acc_login_pwd")
    private String acc_login_pwd;
    @Column(name = "acc_state")
    private int acc_state;

    public long getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(long acc_id) {
        this.acc_id = acc_id;
    }

    public String getAcc_login_name() {
        return acc_login_name;
    }

    public void setAcc_login_name(String acc_login_name) {
        this.acc_login_name = acc_login_name;
    }

    public String getAcc_login_pwd() {
        return acc_login_pwd;
    }

    public void setAcc_login_pwd(String acc_login_pwd) {
        this.acc_login_pwd = acc_login_pwd;
    }

    public int getAcc_state() {
        return acc_state;
    }

    public void setAcc_state(int acc_state) {
        this.acc_state = acc_state;
    }
}
