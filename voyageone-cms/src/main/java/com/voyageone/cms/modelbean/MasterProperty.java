package com.voyageone.cms.modelbean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MasterProperty implements Comparable<MasterProperty> {
    /**
     * 属性id.
     */
    private int prop_id;

    /**
     * 类目id.
     */
    private int category_id;
    /**
     * 属性名.
     */
    private String prop_name;
    /**
     * 属性类型.
     */
    private int prop_type;
    /**
     * 默认值.
     */
    private String prop_value_default;
    /**
     * 是否父属性.
     */
    private int is_parent;
    /**
     * 父属性id.
     */
    private int parent_prop_id;
    /**
     * 是否是顶层属性.
     */
    private int is_top_prop;
    /**
     * 备注.
     */
    private String content;
    /**
     * 是否必填.
     */
    private Integer is_required;
    /**
     * 创建时间.
     */
    private Timestamp created;
    /**
     * 创建者.
     */
    private String creater;
    /**
     * 更新时间
     */
    private Timestamp modified;
    /**
     * 更新者.
     */
    private String modifier;
    /**
     * 属性选项列表.
     */
    private List<PropertyOption> propertyOptions;
    /**
     * 属性规则列表.
     */
    private List<PropertyRule> rules;
    /**
     * 自属性列表.
     */
    private List<MasterProperty> properties;
    /**
     * 属性值列表.
     */
    private List<PropertyValue> values = new ArrayList<PropertyValue>();

    public List<PropertyValue> getValues() {
        return values;
    }

    public void setValues(List<PropertyValue> values) {
        this.values = values;
    }

    public int getProp_id() {
        return prop_id;
    }

    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public int getProp_type() {
        return prop_type;
    }

    public void setProp_type(int prop_type) {
        this.prop_type = prop_type;
    }

    public int getIs_parent() {
        return is_parent;
    }

    public void setIs_parent(int is_parent) {
        this.is_parent = is_parent;
    }

    public int getParent_prop_id() {
        return parent_prop_id;
    }

    public void setParent_prop_id(int parent_prop_id) {
        this.parent_prop_id = parent_prop_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getProp_value_default() {
        return prop_value_default;
    }

    public void setProp_value_default(String prop_value_default) {
        this.prop_value_default = prop_value_default;
    }

    public List<PropertyOption> getPropertyOptions() {
        return propertyOptions;
    }

    public void setPropertyOptions(List<PropertyOption> propertyOptions) {
        this.propertyOptions = propertyOptions;
    }

    public List<PropertyRule> getRules() {
        return rules;
    }

    public void setRules(List<PropertyRule> rules) {
        this.rules = rules;
    }

    public int getIs_top_prop() {
        return is_top_prop;
    }

    public void setIs_top_prop(int is_top_prop) {
        this.is_top_prop = is_top_prop;
    }

    public List<MasterProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MasterProperty> properties) {
        this.properties = properties;
    }

    public Integer getIs_required() {
        return is_required;
    }

    public void setIs_required(Integer is_required) {
        this.is_required = is_required;
    }

    @Override
    public int compareTo(MasterProperty arg) {
        return this.getIs_required().compareTo(arg.getIs_required());
    }
}
