package com.voyageone.cms.modelbean;

public class PropertyRule {
    private int prop_rule_id;
    private int prop_id;
    private String prop_rule_name;
    private String prop_rule_value;
    private String prop_rule_unit;
    private String prop_rule_url;
    private String prop_rule_exProperty;
    private String prop_rule_relationship;
    private String prop_rule_relationship_operator;
    private String created;
    private String creater;
    private String modified;
    private String modifier;
    
    private RuleTranslater ruleTranslater;

    public int getProp_rule_id() {
        return prop_rule_id;
    }
    public void setProp_rule_id(int prop_rule_id) {
        this.prop_rule_id = prop_rule_id;
    }
    public int getProp_id() {
        return prop_id;
    }
    public void setProp_id(int prop_id) {
        this.prop_id = prop_id;
    }
    public String getProp_rule_name() {
        return prop_rule_name;
    }
    public void setProp_rule_name(String prop_rule_name) {
        this.prop_rule_name = prop_rule_name;
    }
    public String getProp_rule_value() {
        return prop_rule_value;
    }
    public void setProp_rule_value(String prop_rule_value) {
        this.prop_rule_value = prop_rule_value;
    }
    public String getProp_rule_unit() {
        return prop_rule_unit;
    }
    public void setProp_rule_unit(String prop_rule_unit) {
        this.prop_rule_unit = prop_rule_unit;
    }
    public String getProp_rule_url() {
        return prop_rule_url;
    }
    public void setProp_rule_url(String prop_rule_url) {
        this.prop_rule_url = prop_rule_url;
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
    public RuleTranslater getRuleTranslater() {
        return ruleTranslater;
    }
    public void setRuleTranslater(RuleTranslater ruleTranslater) {
        this.ruleTranslater = ruleTranslater;
    }
	public String getProp_rule_exProperty() {
		return prop_rule_exProperty;
	}
	public void setProp_rule_exProperty(String prop_rule_exProperty) {
		this.prop_rule_exProperty = prop_rule_exProperty;
	}
	public String getProp_rule_relationship() {
		return prop_rule_relationship;
	}
	public void setProp_rule_relationship(String prop_rule_relationship) {
		this.prop_rule_relationship = prop_rule_relationship;
	}
	public String getProp_rule_relationship_operator() {
		return prop_rule_relationship_operator;
	}
	public void setProp_rule_relationship_operator(String prop_rule_relationship_operator) {
		this.prop_rule_relationship_operator = prop_rule_relationship_operator;
	}
    
}
