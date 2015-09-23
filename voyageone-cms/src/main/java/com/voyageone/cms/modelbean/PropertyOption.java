package com.voyageone.cms.modelbean;

public class PropertyOption {
    
    private int prop_option_id;
    private int prop_id;
    private String prop_option_name;
    private String prop_option_value;
    private String created;
    private String creater;
    private String modified;
    private String modifier;
    
    public int getProp_option_id() {
        return prop_option_id;
    }
    public void setProp_option_id(int prop_option_id) {
        this.prop_option_id = prop_option_id;
    }
    public int getProp_id() {
        return prop_id;
    }
    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }
    public String getProp_option_name() {
        return prop_option_name;
    }
    public void setProp_option_name(String prop_option_name) {
        this.prop_option_name = prop_option_name;
    }
    public String getProp_option_value() {
        return prop_option_value;
    }
    public void setProp_option_value(String prop_option_value) {
        this.prop_option_value = prop_option_value;
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
