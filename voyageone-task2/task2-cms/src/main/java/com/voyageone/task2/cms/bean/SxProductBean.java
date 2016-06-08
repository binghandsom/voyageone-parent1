package com.voyageone.task2.cms.bean;


import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * Created by Leo on 15-12-11.
 */
public class SxProductBean {
    private CmsBtProductBean cmsBtProductModel;
    private CmsBtProductGroupModel cmsBtProductModelGroupPlatform;
    private CmsBtFeedInfoModel cmsBtFeedInfoModel;

    public SxProductBean(CmsBtProductBean cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public SxProductBean(CmsBtProductBean cmsBtProductModel, CmsBtProductGroupModel cmsBtProductModelGroupPlatform, CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        this.cmsBtProductModelGroupPlatform = cmsBtProductModelGroupPlatform;
        this.cmsBtProductModel = cmsBtProductModel;
        this.cmsBtFeedInfoModel = cmsBtFeedInfoModel;
    }

    public CmsBtProductBean getCmsBtProductModel() {
        return cmsBtProductModel;
    }

    public void setCmsBtProductModel(CmsBtProductBean cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public CmsBtProductGroupModel getCmsBtProductModelGroupPlatform() {
        return cmsBtProductModelGroupPlatform;
    }

    public void setCmsBtProductModelGroupPlatform(CmsBtProductGroupModel cmsBtProductModelGroupPlatform) {
        this.cmsBtProductModelGroupPlatform = cmsBtProductModelGroupPlatform;
    }

    public CmsBtFeedInfoModel getCmsBtFeedInfoModel() {
        return cmsBtFeedInfoModel;
    }

    public void setCmsBtFeedInfoModel(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
        this.cmsBtFeedInfoModel = cmsBtFeedInfoModel;
    }
}
