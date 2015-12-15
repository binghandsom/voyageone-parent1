package com.voyageone.cms.modelbean;

import com.voyageone.cms.service.FeedPropMappingType;

/**
 * 映射 ims_bt_feed_prop_mapping 表的数据实体
 *
 * Created by Jonas on 9/2/15.
 */
public class FeedPropMapping {

    private int id;

    private String channel_id;

    private int main_category_id;

    private long prop_id;

    private String conditions;

    private FeedPropMappingType type;

    private String value;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getMain_category_id() {
        return main_category_id;
    }

    public void setMain_category_id(int main_category_id) {
        this.main_category_id = main_category_id;
    }

    public long getProp_id() {
        return prop_id;
    }

    public void setProp_id(long prop_id) {
        this.prop_id = prop_id;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public int getType() {
        return type.value();
    }

    public FeedPropMappingType getEType() {
        return type;
    }

    public void setEnumType(FeedPropMappingType type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = FeedPropMappingType.valueOf(type);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
