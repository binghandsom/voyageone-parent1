package com.voyageone.cms.modelbean;

public class RuleTranslater  extends BaseModel{
    private String id;
    private String rule_name;
    private String rule_translate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRule_name() {
        return rule_name;
    }
    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }
    public String getRule_translate() {
        return rule_translate;
    }
    public void setRule_translate(String rule_translate) {
        this.rule_translate = rule_translate;
    }
    
}
