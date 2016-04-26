package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
public class CmsBtFeedInfoBhfoModel extends CmsBtFeedInfoModel {

    private String MPN;
    private String Material;

    public String getMPN() {
        return MPN;
    }

    public void setMPN(String MPN) {
        this.MPN = MPN;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }
}
