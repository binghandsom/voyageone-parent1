package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.MultiInputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.value.Value;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public abstract class Rule {

    protected String name;
    protected String value;
    protected String exProperty;
    protected DependGroup dependGroup;

    protected boolean isTypeInNamespace(FieldTypeEnum[] namespace, FieldTypeEnum type) {
        for (FieldTypeEnum typeEnum : namespace) {
            if (typeEnum.equals(type)) {
                return true;
            }
        }

        return false;
    }

    protected List<String> getFieldValues(Field field) {
        List<String> values = new ArrayList<>();
        switch(field.getType()) {
            case INPUT:
                InputField inputField = (InputField)field;
                values.add(inputField.getValue());
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField)field;
                values.add(singleCheckField.getValue().getValue());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField)field;
                for (Value aVList : multiCheckField.getValues()) {
                    values.add(aVList.getValue());
                }

                return values;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField)field;
                for (Value aViList : multiInputField.getValues()) {
                    values.add(aViList.getValue());
                }
        }

        return values;
    }

    protected List<String> getOptionValues(Field field) {
        List<String> values = new ArrayList<>();
        switch(field.getType()) {
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField)field;
                for (Option option1 : singleCheckField.getOptions()) {
                    values.add(option1.getValue());
                }

                return values;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField)field;
                for (Option anOptions2 : multiCheckField.getOptions()) {
                    values.add(anOptions2.getValue());
                }
        }

        return values;
    }

    public Rule(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Rule(String name, String value, String exProperty) {
        this.name = name;
        this.value = value;
        this.exProperty = exProperty;
    }

    public Element toElement(String fieldId) throws TopSchemaException {
        Element rule = XmlUtils.createRootElement("rule");
        if(StringUtil.isEmpty(this.name)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_31001, fieldId);
        } else if(StringUtil.isEmpty(this.value)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_31002, fieldId);
        } else {
            rule.addAttribute("name", this.name);
            rule.addAttribute("value", this.value);
            if(!StringUtil.isEmpty(this.exProperty)) {
                rule.addAttribute("exProperty", this.exProperty);
            }

            if(this.name.equals(RuleTypeEnum.MAX_TARGET_SIZE_RULE.value())) {
                MaxTargetSizeRule depend = (MaxTargetSizeRule)this;
                if(!StringUtil.isEmpty(depend.getUnit())) {
                    rule.addAttribute("unit", depend.getUnit());
                }
            }

            if(this.name.equals(RuleTypeEnum.MIN_TARGET_SIZE_RULE.value())) {
                MinTargetSizeRule depend1 = (MinTargetSizeRule)this;
                if(!StringUtil.isEmpty(depend1.getUnit())) {
                    rule.addAttribute("unit", depend1.getUnit());
                }
            }

            if(this.name.equals(RuleTypeEnum.MAX_LENGTH_RULE.value())) {
                MaxLengthRule depend2 = (MaxLengthRule)this;
                if(!StringUtil.isEmpty(depend2.getUnit())) {
                    rule.addAttribute("unit", depend2.getUnit());
                }
            }

            if(this.name.equals(RuleTypeEnum.MIN_LENGTH_RULE.value())) {
                MinLengthRule depend3 = (MinLengthRule)this;
                if(!StringUtil.isEmpty(depend3.getUnit())) {
                    rule.addAttribute("unit", depend3.getUnit());
                }
            }

            if(this.name.equals(RuleTypeEnum.TIP_RULE.value())) {
                TipRule depend4 = (TipRule)this;
                if(!StringUtil.isEmpty(depend4.getUrl())) {
                    rule.addAttribute("url", depend4.getUrl());
                }
            }

            if(this.name.equals(RuleTypeEnum.DEV_TIP_RULE.value())) {
                DevTipRule depend5 = (DevTipRule)this;
                if(!StringUtil.isEmpty(depend5.getUrl())) {
                    rule.addAttribute("url", depend5.getUrl());
                }
            }

            if(this.dependGroup != null && !this.dependGroup.isEmpty()) {
                Element depend6 = this.dependGroup.toElement();
                XmlUtils.appendElement(rule, depend6);
            }

            return rule;
        }
    }

    public Rule() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DependGroup getDependGroup() {
        return this.dependGroup;
    }

    public void setDependGroup(DependGroup dependGroup) {
        this.dependGroup = dependGroup;
    }

    public DependGroup addDependGroup() {
        DependGroup dependGroup = new DependGroup();
        this.setDependGroup(dependGroup);
        return dependGroup;
    }

    public DependGroup addDependGroup(String fieldId, String value) {
        DependGroup dependGroup = new DependGroup();
        DependExpress dependExpress = dependGroup.addDependExpress();
        dependExpress.setFieldId(fieldId);
        dependExpress.setValue(value);
        this.setDependGroup(dependGroup);
        return dependGroup;
    }

    public DependGroup addDependGroup(String fieldId, String value, String symbol) {
        DependGroup dependGroup = new DependGroup();
        DependExpress dependExpress = dependGroup.addDependExpress();
        dependExpress.setFieldId(fieldId);
        dependExpress.setValue(value);
        dependExpress.setSymbol(symbol);
        this.setDependGroup(dependGroup);
        return dependGroup;
    }

    public String getExProperty() {
        return this.exProperty;
    }

    public void setExProperty(String exProperty) {
        this.exProperty = exProperty;
    }

    public boolean isValueIntervalInclude() {
        return "include".equals(this.exProperty);
    }

    public void specialAttribute(Element rule) {
    }
}
