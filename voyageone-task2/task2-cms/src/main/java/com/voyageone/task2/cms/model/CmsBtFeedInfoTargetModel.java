package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author james.li on 2016/5/9.
 * @version 2.0.0
 */
public class CmsBtFeedInfoTargetModel extends CmsBtFeedInfoModel {
    private String attributeNames;
    private String attributeValues;
    private String variationThemes;
    private String secattributes;

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

    public String getVariationThemes() {
        return variationThemes;
    }

    public void setVariationThemes(String variationThemes) {
        this.variationThemes = variationThemes;
    }

    public String getSecattributes() {
        return secattributes;
    }

    public void setSecattributes(String secattributes) {
        this.secattributes = secattributes;
    }
}
