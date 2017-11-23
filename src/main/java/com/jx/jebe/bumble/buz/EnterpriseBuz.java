package com.jx.jebe.bumble.buz;

import com.jx.jebe.bumble.beans.PersonExtEntity;
import com.jx.jebe.bumble.service.ServiceFactory;
import com.jx.service.enterprise.contract.*;
import com.jx.service.enterprise.entity.LvEnterpriseAddressTemplateEntity;
import com.jx.service.enterprise.entity.LvEnterpriseEntity;
import com.jx.service.enterprise.entity.LvEnterprisePersonEntity;
import com.jx.service.enterprise.entity.LvEnterpriseRoleRelationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaowei on 17/11/2.
 */
public class EnterpriseBuz {

    public static EnterpriseBuz enterpriseBuz = new EnterpriseBuz();
    private static ILvEnterpriseService es = ServiceFactory.getLvEnterpriseService();
    private static ILvEnterpriseDicDataService eds = ServiceFactory.getLvEnterpriseDicDataService();
    private static ILvEnterprisePersonService eps = ServiceFactory.getLvEnterprisePersonService();
    private static ILvEnterpriseRoleRelationService ers = ServiceFactory.getLvEnterpriseRoleRelationService();
    private static ILvEnterpriseAddressService ars = ServiceFactory.getLvEnterpriseAddressService();
    private static ILvEnterpriseAddressTemplateService ats = ServiceFactory.getLvEnterpriseAddressTemplateService();

    /**
     * 获取企业核名已核批准号
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public String getEntercheckedNameCode(String enterpriseid)throws Exception{
        String str_temp = "(京怀)名称预核(内)字[2017]第0413843号";//es.getExtValueByEnterpriseIdAndKey(enterpriseid,"checkedNameCode");
        String ret = "";
        Pattern pattern = Pattern.compile("\\d{7}");
        Matcher matcher = pattern.matcher(str_temp);
        if(matcher.find()){
            ret = matcher.group();
        }

       return ret;
    }
    public static void main(String[] args){
        try {
            System.out.println(EnterpriseBuz.enterpriseBuz.getEntercheckedNameCode(""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LvEnterprisePersonEntity getEnterpriseFinalContact(long enterpriseid)throws Exception{
        LvEnterprisePersonEntity personEntity= null;
        List<LvEnterprisePersonEntity> list = eps.getPersonListByEnterpriseIdAndRoleType(enterpriseid,"finance");
        if(null != list && list.size() > 0){
            personEntity = list.get(0);
        }
        return personEntity;
    }

    /**
     * 根据人员id，企业库id
     * @param personid
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public PersonExtEntity loadPersonExtBypersonidAndEnterpriseid(long personid,long enterpriseid)throws Exception{
        PersonExtEntity pee = null;
        return pee;
    }
    /**
     * 企业联系人
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public LvEnterprisePersonEntity getEnterpriseContacts(long enterpriseid)throws Exception{
        LvEnterprisePersonEntity personEntity = null;
        List<LvEnterprisePersonEntity> list = eps.getPersonListByEnterpriseIdAndRoleType(enterpriseid,"contacts");
        if(null != list && list.size() > 0){
            personEntity = list.get(0);
        }
        return personEntity;
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
     * 获取产权所有人
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public String getOwenerName(long enterpriseid)throws Exception{

        String holdername = "";
        //先获取地址类型
        String addtype = es.getExtValueByEnterpriseIdAndKey(enterpriseid+"","addressType");
        //addtyp 1,孵化器地址 2，自有地址
        if("1".equals(addtype)){
            LvEnterpriseAddressTemplateEntity lvEnterpriseAddressTemplateEntity = ats.getEnterpriseAddressTemplateByEnterpriseId(enterpriseid+"");
            if(null != lvEnterpriseAddressTemplateEntity){
                holdername = lvEnterpriseAddressTemplateEntity.getTheOwnerName();
            }
        }else if("2".equals(addtype)){
            holdername = ars.getEnterpriseAddressDataMapByEnterpriseIdAndDataKey(enterpriseid,"theOwnerName");
        }
        return holdername;
    }
    /**
     *
     * @param enterpriseid
     * @return
     * @throws Exception
     */
    public String getBussinessDuration(long enterpriseid)throws Exception{

        return "20";
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
