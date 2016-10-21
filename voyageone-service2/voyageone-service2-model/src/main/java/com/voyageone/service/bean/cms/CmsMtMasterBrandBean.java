package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsMtMasterBrandModel;

/**
 * Created by gjl on 2016/10/10.
 */
public class CmsMtMasterBrandBean extends CmsMtMasterBrandModel {
    protected String key;
    protected String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
