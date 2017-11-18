package com.jx.jebe.bumble.buz;

import com.jx.jebe.bumble.service.ServiceFactory;
import com.jx.service.enterprise.contract.ILvEnterpriseDicDataService;
import com.jx.service.enterprise.contract.ILvEnterprisePersonService;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.contract.ILvEnterpriseService;
import com.jx.service.enterprise.entity.LvEnterpriseEntity;
import com.jx.service.enterprise.entity.LvEnterprisePersonEntity;
import com.jx.service.enterprise.entity.LvEnterpriseRoleRelationEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaowei on 17/11/2.
 */
public class EnterpriseBuz {

    public static EnterpriseBuz enterpriseBuz = new EnterpriseBuz();
    private static ILvEnterpriseService es = ServiceFactory.getLvEnterpriseService();
    private static ILvEnterpriseDicDataService eds = ServiceFactory.getLvEnterpriseDicDataService();
    private static ILvEnterprisePersonService eps = ServiceFactory.getLvEnterprisePersonService();
    private static ILvEnterpriseRoleRelationService ers = ServiceFactory.getLvEnterpriseRoleRelationService();

    /**
     * 获取企业核名已核批准号
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public String getEntercheckedNameCode(String enterpriseid)throws Exception{
       return es.getExtValueByEnterpriseIdAndKey(enterpriseid,"checkedNameCode");
    }

    /**
     * 获取企业基本信息
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public LvEnterpriseEntity getEnterpriseBasicInfo(long enterpriseid)throws Exception{
        return es.getEnterpriseById(enterpriseid);
    }

    /**
     * 获取股东信息
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public List<LvEnterprisePersonEntity> getShareHolderList(long enterpriseid)throws Exception{
        List<LvEnterprisePersonEntity>  plist = new ArrayList<LvEnterprisePersonEntity>();
        plist.addAll(eps.getPersonListByEnterpriseIdAndRoleType(enterpriseid,"naturalPartner"));
        plist.addAll(eps.getPersonListByEnterpriseIdAndRoleType(enterpriseid,"legalPartner"));

        return plist;
    }
    /**
     * 获取企业核名申请人身份证号
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public String getEntercheckedNameCerNo(String enterpriseid)throws Exception{
        return es.getExtValueByEnterpriseIdAndKey(enterpriseid,"checkedNameCerNo");
    }
}
