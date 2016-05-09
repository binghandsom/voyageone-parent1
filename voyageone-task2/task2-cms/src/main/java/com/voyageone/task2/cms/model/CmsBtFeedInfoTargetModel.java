package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author james.li on 2016/5/9.
 * @version 2.0.0
 */
public class CmsBtFeedInfoTargetModel extends CmsBtFeedInfoModel {
    private String attributeNames;
    private String attributeValues;

    public String getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(String attributeNames) {
        this.attributeNames = attributeNames;
    }

    public String getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues;
    }
}
