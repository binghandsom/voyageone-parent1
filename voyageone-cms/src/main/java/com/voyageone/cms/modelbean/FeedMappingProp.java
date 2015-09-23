package com.voyageone.cms.modelbean;

import com.voyageone.ims.enums.MasterPropTypeEnum;

/**
 * 扩展的主数据属性的数据实体类。基于 ims_mt_prop 添加 ims_bt_feed_prop_mapping_ignore 的 is_ignore 属性
 *
 * Created by Jonas on 9/2/15.
 */
public class FeedMappingProp extends MasterProperty {
    private boolean is_ignore;

    public boolean is_ignore() {
        return is_ignore;
    }

    public boolean getIs_ignore() {
        return is_ignore;
    }

    public void setIs_ignore(boolean is_ignore) {
        this.is_ignore = is_ignore;
    }

    public MasterPropTypeEnum getEProp_type() {
        return MasterPropTypeEnum.valueOf(getProp_type());
    }

    public void setEProp_type(MasterPropTypeEnum type) {
        setProp_type(type.getValue());
    }

    public String getTypeName() {
        return getEProp_type().getName();
    }
}
