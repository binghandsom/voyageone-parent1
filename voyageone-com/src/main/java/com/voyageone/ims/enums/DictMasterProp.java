package com.voyageone.ims.enums;

import com.voyageone.ims.bean.DictMasterPropBean;

/**
 * 字典可用的主数据属性
 *
 * Created by Jonas on 9/11/15.
 */
public enum DictMasterProp {

    STYLE("FEED-适合风格"),

    DESC_EN("FEED-详细说明-英"),

    CROWD("FEED-适合人群"),

    GEM("FEED-产品宝石"),

    COLOR("FEED-颜色"),

    MATERIAL("FEED-材质");

    private String desc;

    DictMasterProp(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public DictMasterPropBean toBean() {

        DictMasterPropBean bean = new DictMasterPropBean();

        bean.setName(name());

        bean.setDesc(desc());

        return bean;
    }
}
