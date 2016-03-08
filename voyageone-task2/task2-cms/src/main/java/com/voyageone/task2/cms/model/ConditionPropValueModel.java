package com.voyageone.task2.cms.model;

/**
 * Created by Leo on 15-9-18.
 */
public class ConditionPropValueModel {
    private String channel_id;
    private String condition_expression;
    private String platform_prop_id;
    private String prop_value;
    private String creater;
    private String created;
    private String modifier;
    private String modified;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCondition_expression() {
        return condition_expression;
    }

    public void setCondition_expression(String condition_expression) {
        this.condition_expression = condition_expression;
    }

    public String getPlatform_prop_id() {
        return platform_prop_id;
    }

    public void setPlatform_prop_id(String platform_prop_id) {
        this.platform_prop_id = platform_prop_id;
    }

    public String getProp_value() {
        return prop_value;
    }

    public void setProp_value(String prop_value) {
        this.prop_value = prop_value;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
