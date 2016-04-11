package com.voyageone.service.bean.cms;


import com.voyageone.service.model.cms.enums.DictionaryMasterProp;

/**
 * 枚举 DictMasterProp 的映射
 *
 * Created by Jonas on 9/11/15.
 */
public class DictionaryMasterPropBean {

    private String name;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public DictionaryMasterProp toEnum() {
        return DictionaryMasterProp.valueOf(getName());
    }
}
