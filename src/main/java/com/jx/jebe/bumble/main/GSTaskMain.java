package com.jx.jebe.bumble.main;

import com.jx.spat.gaea.server.bootstrap.Main;

import java.util.Properties;

/**
 * Created by xiaowei on 17/11/1.
 */
public class GSTaskMain extends Main{
    static {
        initTaskConfig();
    }

    private static void initTaskConfig() {

    }

    public static void main(String[] args){
        try {
            Main.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
