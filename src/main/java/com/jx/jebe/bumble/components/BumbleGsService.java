package com.jx.jebe.bumble.components;

import com.alibaba.fastjson.JSONObject;
import com.jx.jebe.bumble.buz.WangdengBuz;
import com.jx.jebe.bumble.client.contract.IBumbleGsService;
import com.jx.spat.gaea.server.contract.annotation.ServiceBehavior;

import java.util.Map;

/**
 * Created by xiaowei on 17/10/31.
 */
@ServiceBehavior
public class BumbleGsService implements IBumbleGsService {

    public String startWangDeng(long l, long l1, Map<String, String> map) throws Exception {
        long rid = WangdengBuz.wdbuz.addNewGswdTask(l,l1,map);
        JSONObject job = new JSONObject();
        if(rid > 0){
            job.put("errcode","1000");
            job.put("msg","新任务添加成功!");
        }else {
            job.put("errcode","1001");
            job.put("msg","新任务添加失败!");
        }
        return job.toString();
    }
}
