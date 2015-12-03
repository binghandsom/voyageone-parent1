package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.masterdate.schema.Util.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.property.Property;
import com.voyageone.common.masterdate.schema.rule.Rule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

public abstract class OptionsField extends Field {
    protected List<Option> options = new ArrayList();

    public OptionsField() {
    }

    public void add(Option option) {
        if(option != null) {
            this.options.add(option);
        }
    }

    public Option addOption() {
        Option option = new Option();
        this.add(option);
        return option;
    }

    public Option addOption(String displayName, String value) {
        if(displayName != null && value != null) {
            Option option = new Option();
            option.setDisplayName(displayName);
            option.setValue(value);
            this.add(option);
            return option;
        } else {
            return null;
        }
    }

    public List<Option> getOptions() {
        return this.options;
    }

    public void setOptions(List<Option> options) {
        if(options != null) {
            this.options = options;
        }
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

                if(this.options != null && !this.options.isEmpty()) {
                    propertiesNode = XmlUtils.appendElement(fieldNode, "options");
                    i$ = this.options.iterator();

                    while(i$.hasNext()) {
                        Option propertie1 = (Option)i$.next();
                        propertyNode = propertie1.toElement();
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
                        Property propertie2 = (Property)i$.next();
                        propertyNode = XmlUtils.appendElement(propertiesNode, "property");
                        propertyNode.addAttribute("key", propertie2.getKey());
                        propertyNode.addAttribute("value", propertie2.getValue());
                    }
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }
}
