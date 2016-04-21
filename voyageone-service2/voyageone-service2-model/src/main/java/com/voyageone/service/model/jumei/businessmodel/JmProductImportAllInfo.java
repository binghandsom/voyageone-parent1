package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/4/21.
 */
public class JmProductImportAllInfo {
    CmsBtJmPromotionModel modelCmsBtJmPromotion;
    List<CmsBtJmImportProduct> listProductModel = new ArrayList<>();
    List<CmsBtJmImportSku> listSkuModel = new ArrayList<>();
    List<CmsBtJmImportSpecialImage> listSpecialImageModel = new ArrayList<>();

    public List<CmsBtJmImportSpecialImage> getListSpecialImageModel() {
        return listSpecialImageModel;
    }

    public void setListSpecialImageModel(List<CmsBtJmImportSpecialImage> listSpecialImageModel) {
        this.listSpecialImageModel = listSpecialImageModel;
    }

    public CmsBtJmPromotionModel getModelCmsBtJmPromotion() {
        return modelCmsBtJmPromotion;
    }

    public void setModelCmsBtJmPromotion(CmsBtJmPromotionModel modelCmsBtJmPromotion) {
        this.modelCmsBtJmPromotion = modelCmsBtJmPromotion;
    }

    public List<CmsBtJmImportProduct> getListProductModel() {
        return listProductModel;
    }

    public void setListProductModel(List<CmsBtJmImportProduct> listProductModel) {
        this.listProductModel = listProductModel;
    }

    public List<CmsBtJmImportSku> getListSkuModel() {
        return listSkuModel;
    }

    public void setListSkuModel(List<CmsBtJmImportSku> listSkuModel) {
        this.listSkuModel = listSkuModel;
    }
}
