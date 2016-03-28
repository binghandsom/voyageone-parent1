package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.label.Label;
import com.voyageone.common.masterdate.schema.label.LabelGroup;
import org.dom4j.Element;

import java.util.Map;

public class LabelField extends Field {
    private LabelGroup labelGroup = new LabelGroup();

    public LabelField() {
        super.type = FieldTypeEnum.LABEL;
    }

    public Label addLabel(String name, String value, String desc) {
        if(name != null && value != null && desc != null) {
            Label label = new Label();
            label.setName(name);
            label.setValue(value);
            label.setDesc(desc);
            this.labelGroup.add(label);
            return label;
        } else {
            return null;
        }
    }


    public LabelGroup addSubLabelGroup(String name) {
        if(name == null) {
            return null;
        } else {
            LabelGroup newLabelGroup = new LabelGroup();
            newLabelGroup.setName(name);
            this.labelGroup.add(newLabelGroup);
            return newLabelGroup;
        }
    }

    public void add(Label label) {
        if(label != null) {
            this.labelGroup.add(label);
        }
    }

    public LabelGroup getLabelGroup() {
        return this.labelGroup;
    }

    public void setLabelGroup(LabelGroup labelGroup) {
        if(labelGroup != null) {
            this.labelGroup = labelGroup;
        }
    }

    public Element toParamElement() throws TopSchemaException {
        Element fieldNode = XmlUtils.createRootElement("field");
        if(StringUtil.isEmpty(this.id)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, null);
        } else if(this.type != null && !StringUtil.isEmpty(this.type.value())) {
            FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(this.type.value());
            if(fieldEnum == null) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, this.id);
            } else {
                fieldNode.addAttribute("id", this.id);
                fieldNode.addAttribute("name", this.name);
                fieldNode.addAttribute("type", this.type.value());
                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Element toDefaultValueElement() throws TopSchemaException {
        return null;
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.LABEL);
    }

    public Element toElement() throws TopSchemaException {
        Element fieldEle = super.toElement();
        Element lgEle = this.labelGroup.toElement();
        XmlUtils.appendElement(fieldEle, lgEle);
        return fieldEle;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueMap) {
    }

    @Override
    public Object getFieldValueFromMap(Map<String, Object> valueMap) {
        return StringUtil.getStringValue(valueMap.get(id));
    }

    @Override
    public void getFieldValueToMap(Map<String,Object> valueMap) {
    }

}

