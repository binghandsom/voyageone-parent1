package com.voyageone.common.masterdate.schema.field;


import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.masterdate.schema.Util.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.enums.ValueTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.property.Property;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.rule.ValueTypeRule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

public abstract class Field {
    protected String id;
    protected String name;

    protected FieldTypeEnum type;
    protected List<Rule> rules = new ArrayList();
    protected List<Property> properties = new ArrayList();

    // input ["common":"0","1":"product","2":"item"]
    protected String inputLevel;
    public String getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
    }

    protected String inputOrgId;
    public String getInputOrgId() {
        return inputOrgId;
    }

    public void setInputOrgId(String inputOrgId) {
        this.inputOrgId = inputOrgId;
    }

    protected String dataSource;
    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    protected int isDisplay;
    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    protected Field defaultValueField;

    public Field() {
    }

    public Field getChildFieldById(String id) {
        List<Field> fieldList = null;
        switch(type) {
            case COMPLEX:
                fieldList = ((ComplexField)this).getFieldList();
                break;
            case MULTICOMPLEX:
                fieldList = ((MultiComplexField)this).getFieldList();
                break;
        }

        Field result = null;
        if (fieldList != null) {
            for (Field field : fieldList) {
                if(field.getId() != null && field.getId().equals(id)) {
                    return field;
                } else {
                    result = field.getChildFieldById(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    public Field getChildFieldByName(String name) {
        List<Field> fieldList = null;
        switch(type) {
            case COMPLEX:
                fieldList = ((ComplexField)this).getFieldList();
                break;
            case MULTICOMPLEX:
                fieldList = ((MultiComplexField)this).getFieldList();
                break;
        }

        Field result = null;
        if (fieldList != null) {
            for (Field field : fieldList) {
                if(field.getName() != null && field.getName().equals(name)) {
                    return field;
                } else {
                    result = field.getChildFieldByName(name);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    public Element toElement() throws TopSchemaException {
        Element fieldNode = XmlUtils.createRootElement("field");
        if(StringUtil.isEmpty(this.id)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, (String)null);
        } else if(this.type != null && !StringUtil.isEmpty(this.type.value())) {
            FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(this.type.value());
            if(fieldEnum == null) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, this.id);
            } else {
                fieldNode.addAttribute("id", this.id);
                fieldNode.addAttribute("name", this.name);
                fieldNode.addAttribute("type", this.type.value());
                Element propertiesNode;
                Iterator i$;
                Element propertyNode;
                if(this.rules != null && !this.rules.isEmpty()) {
                    propertiesNode = XmlUtils.appendElement(fieldNode, "rules");
                    i$ = this.rules.iterator();

                    while(i$.hasNext()) {
                        Rule propertie = (Rule)i$.next();
                        propertyNode = propertie.toElement(this.id);
                        XmlUtils.appendElement(propertiesNode, propertyNode);
                    }
                }

                if(this.defaultValueField != null) {
                    propertiesNode = this.toDefaultValueElement();
                    if(propertiesNode != null) {
                        XmlUtils.appendElement(fieldNode, propertiesNode);
                    }
                }

                if(this.properties != null && !this.properties.isEmpty()) {
                    propertiesNode = XmlUtils.appendElement(fieldNode, "properties");
                    i$ = this.properties.iterator();

                    while(i$.hasNext()) {
                        Property propertie1 = (Property)i$.next();
                        propertyNode = XmlUtils.appendElement(propertiesNode, "property");
                        propertyNode.addAttribute("key", propertie1.getKey());
                        propertyNode.addAttribute("value", propertie1.getValue());
                    }
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Rule addValueTypeRule(ValueTypeEnum typeEnum) {
        if(typeEnum == null) {
            return null;
        } else {
            ValueTypeRule rule = new ValueTypeRule(typeEnum.value());
            this.add((Rule)rule);
            return rule;
        }
    }

    public Rule addValueTypeRule(String valueType) {
        if(StringUtil.isEmpty(valueType)) {
            return null;
        } else {
            ValueTypeRule rule = new ValueTypeRule(valueType);
            this.add((Rule)rule);
            return rule;
        }
    }

    public void setFieldRequired() {
        this.addRule(RuleTypeEnum.REQUIRED_RULE, "true");
    }

    public abstract Element toParamElement() throws TopSchemaException;

    public abstract Element toDefaultValueElement() throws TopSchemaException;

    public abstract void initDefaultField();

    public void add(Rule rule) {
        this.rules.add(rule);
    }

    public void add(Property property) {
        this.properties.add(property);
    }

    public Rule addRule(RuleTypeEnum ruleType) {
        Rule rule = SchemaFactory.createRule(ruleType);
        this.add(rule);
        return rule;
    }

    public Rule addRule(RuleTypeEnum ruleType, String value) {
        Rule rule = SchemaFactory.createRule(ruleType);
        rule.setValue(value);
        this.add(rule);
        return rule;
    }

    public Rule addRule(RuleTypeEnum ruleType, String value, String exProperty) {
        Rule rule = SchemaFactory.createRule(ruleType);
        rule.setValue(value);
        rule.setExProperty(exProperty);
        this.add(rule);
        return rule;
    }

    public void addRules(List<Rule> ruleList) {
        if(ruleList != null && !ruleList.isEmpty()) {
            Iterator i$ = ruleList.iterator();

            while(i$.hasNext()) {
                Rule rule = (Rule)i$.next();
                this.rules.add(rule);
            }
        }

    }

    public Rule addCustomRule(String ruleType, String ruleValue) {
        Rule rule = SchemaFactory.createCustomRule(ruleType, ruleValue);
        this.add(rule);
        return rule;
    }

    public Rule addCustomRule(String ruleType, String ruleValue, String exProperty) {
        Rule rule = SchemaFactory.createCustomRule(ruleType, ruleValue, exProperty);
        this.add(rule);
        return rule;
    }

    public Property addProperty(String key, String value) {
        Property property = new Property(key, value);
        this.add(property);
        return property;
    }

    public Rule addIntervalRule(RuleTypeEnum ruleType, String ruleValue, Boolean isInclude) {
        Rule rule = SchemaFactory.createRule(ruleType);
        rule.setValue(ruleValue);
        if(isInclude.booleanValue()) {
            rule.setExProperty("include");
        } else {
            rule.setExProperty("not include");
        }

        this.add(rule);
        return rule;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldTypeEnum getType() {
        return this.type;
    }
    public void setType(FieldTypeEnum type) {
        this.type = type;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    public Rule getRuleByName(String ruleName) {
        Iterator i$ = this.rules.iterator();

        Rule rule;
        do {
            if(!i$.hasNext()) {
                return null;
            }

            rule = (Rule)i$.next();
        } while(!rule.getName().equals(ruleName));

        return rule;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }
        Field that = (Field) obj;
        return this.id.equals(that.getId());
    }
}
