package com.jx.jebe.bumble.service;

import com.jx.service.enterprise.contract.*;
import com.jx.spat.gaea.client.GaeaInit;
import com.jx.spat.gaea.client.proxy.builder.ProxyFactory;

/**
 * Created by xiaowei on 17/11/2.
 */
public class ServiceFactory {
    static {
        String url = "/opt/argo/jebeweb/gaea.config";
        GaeaInit.init(url);

    }

    public static ILvEnterpriseAddressTemplateService getLvEnterpriseAddressTemplateService(){
        return ProxyFactory.create(ILvEnterpriseAddressTemplateService.class,"tcp://enterprise/LvEnterpriseAddressTemplateService");
    }
    public static ILvEnterpriseAddressService getLvEnterpriseAddressService(){
        return ProxyFactory.create(ILvEnterpriseAddressService.class,"tcp://enterprise/LvEnterpriseAddressService");
    }
    public static ILvEnterpriseService getLvEnterpriseService(){

        return ProxyFactory.create(ILvEnterpriseService.class,"tcp://enterprise/LvEnterpriseService");
    }
    public static ILvEnterprisePersonService getLvEnterprisePersonService(){
        return ProxyFactory.create(ILvEnterprisePersonService.class,"tcp://enterprise/LvEnterprisePersonService");
    }
    public static ILvEnterpriseRoleRelationService getLvEnterpriseRoleRelationService(){
        return ProxyFactory.create(ILvEnterpriseRoleRelationService.class,"tcp://enterprise/LvEnterpriseRoleRelationService");
    }
    public static ILvEnterpriseDicDataService getLvEnterpriseDicDataService(){
        return ProxyFactory.create(ILvEnterpriseDicDataService.class,"tcp://enterprise/LvEnterpriseDicDataService");
    }
}
