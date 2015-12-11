package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
public class CmsMtCommonPropDefModel_Option extends BaseMongoMap {

    public CmsMtCommonPropDefModel_Option() {
    }

    public CmsMtCommonPropDefModel_Option(Map m) {
        this.putAll(m);
    }

    public String getDisplayName() {
        return (String) getAttribute("displayName");
    }

    public void setDisplayName(String displayName) {
        setAttribute("displayName", displayName);
    }

    public String getValue() {
        return (String) getAttribute("value");
    }

    public void setValue(String value) {
        setAttribute("value", value);
    }

}
