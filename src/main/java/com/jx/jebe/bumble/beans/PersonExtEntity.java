package com.jx.jebe.bumble.beans;

/**
 * Created by xiaowei on 17/11/22.
 */
public class PersonExtEntity {
    private long person_id;
    private long enterprise_id;
    private String mz_str;
    private String mz_code;
    private String provnice_code;
    private String provnice;
    private String city_code;
    private String city;
    private String id_address;
    private String chuzi;
    private String chuzi_type;
    private String chuzi_time;

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public long getEnterprise_id() {
        return enterprise_id;
    }

    public void setEnterprise_id(long enterprise_id) {
        this.enterprise_id = enterprise_id;
    }

    public String getMz_str() {
        return mz_str;
    }

    public void setMz_str(String mz_str) {
        this.mz_str = mz_str;
    }

    public String getMz_code() {
        return mz_code;
    }

    public void setMz_code(String mz_code) {
        this.mz_code = mz_code;
    }

    public String getProvnice_code() {
        return provnice_code;
    }

    public void setProvnice_code(String provnice_code) {
        this.provnice_code = provnice_code;
    }

    public String getProvnice() {
        return provnice;
    }

    public void setProvnice(String provnice) {
        this.provnice = provnice;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId_address() {
        return id_address;
    }

    public void setId_address(String id_address) {
        this.id_address = id_address;
    }

    public String getChuzi() {
        return chuzi;
    }

    public void setChuzi(String chuzi) {
        this.chuzi = chuzi;
    }

    public String getChuzi_type() {
        return chuzi_type;
    }

    public void setChuzi_type(String chuzi_type) {
        this.chuzi_type = chuzi_type;
    }

    public String getChuzi_time() {
        return chuzi_time;
    }

    public void setChuzi_time(String chuzi_time) {
        this.chuzi_time = chuzi_time;
    }
}
