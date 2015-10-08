package com.voyageone.cms.modelbean;

import java.util.List;

import com.voyageone.ims.enums.MasterPropTypeEnum;

public class PropertyValue  extends BaseModel{
    private String channel_id;
    private int level;
    private String level_value;
    private  int prop_id;
    private String prop_value;
    private String uuid;
    private String parent;
    private MasterPropTypeEnum type;
    private List<PropertyValue> values;
    
    public String getChannel_id() {
        return channel_id;
    }
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getLevel_value() {
        return level_value;
    }
    public void setLevel_value(String level_value) {
        this.level_value = level_value;
    }
    public int getProp_id() {
        return prop_id;
    }
    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }
    public String getProp_value() {
        return prop_value;
    }
    public void setProp_value(String prop_value) {
        this.prop_value = prop_value;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getParent() {
        return parent;
    }
    public void setParent(String parent) {
        this.parent = parent;
    }
    public List<PropertyValue> getValues() {
        return values;
    }
    public void setValues(List<PropertyValue> values) {
        this.values = values;
    }
    public MasterPropTypeEnum getType() {
        return type;
    }
    public void setType(MasterPropTypeEnum type) {
        this.type = type;
    }
    
}
