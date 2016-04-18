package com.voyageone.service.model.cms.mongo.feed;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/4/18.
 * @version 2.0.0
 */
public class CmsBtFeedCategoryAttributeModel extends ChannelPartitionModel {
    private String catId;
    private String catPath;
    private Map<String, List<String>> attribute;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public Map<String, List<String>> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, List<String>> attribute) {
        this.attribute = attribute;
    }
}
