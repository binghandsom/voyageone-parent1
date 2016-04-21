package com.voyageone.task2.cms.bean;


import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * Created by Leo on 15-12-11.
 */
public class SxProductBean {
    private CmsBtProductModel cmsBtProductModel;
    private CmsBtProductModel_Group_Platform cmsBtProductModelGroupPlatform;
    private CmsBtFeedInfoModel cmsBtFeedInfoModel;

    public SxProductBean(CmsBtProductModel cmsBtProductModel, CmsBtProductModel_Group_Platform cmsBtProductModelGroupPlatform, CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        this.cmsBtProductModelGroupPlatform = cmsBtProductModelGroupPlatform;
        this.cmsBtProductModel = cmsBtProductModel;
        this.cmsBtFeedInfoModel = cmsBtFeedInfoModel;
    }

    public CmsBtProductModel getCmsBtProductModel() {
        return cmsBtProductModel;
    }

    public void setCmsBtProductModel(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public CmsBtProductModel_Group_Platform getCmsBtProductModelGroupPlatform() {
        return cmsBtProductModelGroupPlatform;
    }

    public void setCmsBtProductModelGroupPlatform(CmsBtProductModel_Group_Platform cmsBtProductModelGroupPlatform) {
        this.cmsBtProductModelGroupPlatform = cmsBtProductModelGroupPlatform;
    }

    public CmsBtFeedInfoModel getCmsBtFeedInfoModel() {
        return cmsBtFeedInfoModel;
    }

    public void setCmsBtFeedInfoModel(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        this.cmsBtFeedInfoModel = cmsBtFeedInfoModel;
    }
}
