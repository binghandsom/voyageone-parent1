package com.voyageone.service.model.cms.enums;

import com.voyageone.service.bean.cms.DictionaryMasterPropBean;

/**
 * 字典可用的主数据属性
 *
 * Created by Jonas on 9/11/15.
 */
public enum DictionaryMasterProp {

    STYLE("FEED-适合风格"),

    DESC_EN("FEED-详细说明-英"),

    CROWD("FEED-适合人群"),

    GEM("FEED-产品宝石"),

    COLOR("FEED-颜色"),

    MATERIAL("FEED-材质");

    private String desc;

    DictionaryMasterProp(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public DictionaryMasterPropBean toBean() {

        DictionaryMasterPropBean bean = new DictionaryMasterPropBean();

        bean.setName(name());

        bean.setDesc(desc());

        return bean;
    }
}
