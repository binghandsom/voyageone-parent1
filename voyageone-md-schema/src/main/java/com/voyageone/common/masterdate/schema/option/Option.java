package com.voyageone.common.masterdate.schema.option;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import org.dom4j.Element;

public class Option {
    protected String displayName;
    protected String value;
    protected DependGroup dependGroup;

    public Option() {
    }

    public Element toElement() throws TopSchemaException {
        Element option = XmlUtils.createRootElement("option");
        if(StringUtil.isEmpty(this.displayName)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33001, (String)null);
        } else if(StringUtil.isEmpty(this.value)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33002, (String)null);
        } else {
            option.addAttribute("displayName", this.displayName);
            option.addAttribute("value", this.value);
            if(this.dependGroup != null && !this.dependGroup.isEmpty()) {
                Element depend = this.dependGroup.toElement();
                XmlUtils.appendElement(option, depend);
            }

            return option;
        }
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

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String DisplayName() {
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

    public String getValue() {
        return this.value;
    }
}
