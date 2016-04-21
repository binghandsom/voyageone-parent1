package com.voyageone.task2.cms.bean;


import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * Created by Leo on 15-12-11.
 */
public class SxProductBean {
    private CmsBtProductModel cmsBtProductModel;
    private CmsBtProductGroupModel cmsBtProductModelGroupPlatform;

    public SxProductBean(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public SxProductBean(CmsBtProductModel cmsBtProductModel, CmsBtProductGroupModel cmsBtProductModelGroupPlatform) {
        this.cmsBtProductModelGroupPlatform = cmsBtProductModelGroupPlatform;
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public CmsBtProductModel getCmsBtProductModel() {
        return cmsBtProductModel;
    }

    public void setCmsBtProductModel(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public CmsBtProductGroupModel getCmsBtProductModelGroupPlatform() {
        return cmsBtProductModelGroupPlatform;
    }

    public void setCmsBtProductModelGroupPlatform(CmsBtProductGroupModel cmsBtProductModelGroupPlatform) {
        this.cmsBtProductModelGroupPlatform = cmsBtProductModelGroupPlatform;
    }
}
