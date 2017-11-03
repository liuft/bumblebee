package com.jx.jebe.bumble.buz;

import com.jx.jebe.bumble.service.ServiceFactory;
import com.jx.service.enterprise.contract.ILvEnterpriseService;

/**
 * Created by xiaowei on 17/11/2.
 */
public class EnterpriseBuz {

    public static EnterpriseBuz enterpriseBuz = new EnterpriseBuz();
    private static ILvEnterpriseService es = ServiceFactory.getLvEnterpriseService();

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
     * 获取企业核名申请人身份证号
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public String getEntercheckedNameCerNo(String enterpriseid)throws Exception{
        return es.getExtValueByEnterpriseIdAndKey(enterpriseid,"checkedNameCerNo");
    }
}
