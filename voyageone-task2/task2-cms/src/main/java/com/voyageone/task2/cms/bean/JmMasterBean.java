package com.voyageone.task2.cms.bean;

import com.voyageone.components.jumei.Bean.JmBrandBean;
import com.voyageone.components.jumei.Bean.JmCurrencyBean;
import com.voyageone.components.jumei.Bean.JmWarehouseBean;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
public class JmMasterBean {
    private String code;

    private String key;

    private String name1;

    private String name2;

    private String name3;

    private String name4;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public JmMasterBean(JmBrandBean jmBrandBean,String modifier){
        this.code = "0";
        this.key = jmBrandBean.getId().toString();
        this.name1 = jmBrandBean.getName();
        this.name2 = jmBrandBean.getEnName();
        this.creater = modifier;
        this.modifier = modifier;
    }
    public JmMasterBean(JmCurrencyBean jmCurrencyBean,String modifier){
        this.code = "1";
        this.key = jmCurrencyBean.getArea_code().toString();
        this.name1 = jmCurrencyBean.getArea_currency_name();
        this.name2 = jmCurrencyBean.getArea_currency_symbol();
        this.creater = modifier;
        this.modifier = modifier;
    }
    public JmMasterBean(JmWarehouseBean jmWarehouseBean,String modifier){
        this.code = "2";
        this.key = jmWarehouseBean.getShipping_system_id().toString();
        this.name1 = jmWarehouseBean.getShipping_system_name();
        this.name2 = jmWarehouseBean.getArea_name();
        this.creater = modifier;
        this.modifier = modifier;
    }

    public JmMasterBean(){

    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
