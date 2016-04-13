package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/12.
 */
public class JMProductUpdateInfo {
    CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct;
    CmsBtJmProductModel modelCmsBtJmProduct;
    List<CmsBtJmPromotionSkuModel> listCmsBtJmPromotionSku;
    List<CmsBtJmSkuModel> listCmsBtJmSku;
    List<CmsBtJmProductImagesModel> listCmsBtJmProductImages;

    public List<CmsMtMasterInfoModel> getListCmsMtMasterInfo() {
        return listCmsMtMasterInfo;
    }

    public void setListCmsMtMasterInfo(List<CmsMtMasterInfoModel> listCmsMtMasterInfo) {
        this.listCmsMtMasterInfo = listCmsMtMasterInfo;
    }

    List<CmsMtMasterInfoModel> listCmsMtMasterInfo;
    public List<CmsBtJmProductImagesModel> getListCmsBtJmProductImages() {
        return listCmsBtJmProductImages;
    }

    public void setListCmsBtJmProductImages(List<CmsBtJmProductImagesModel> listCmsBtJmProductImages) {
        this.listCmsBtJmProductImages = listCmsBtJmProductImages;
    }

    public Map<Integer, CmsBtJmSkuModel> getMapCmsBtJmSkuModel() {
        return mapCmsBtJmSkuModel;
    }

    public void setMapCmsBtJmSkuModel(Map<Integer, CmsBtJmSkuModel> mapCmsBtJmSkuModel) {
        this.mapCmsBtJmSkuModel = mapCmsBtJmSkuModel;
    }

    Map<Integer, CmsBtJmSkuModel> mapCmsBtJmSkuModel = new HashMap<>();

    public void loadData() {
        mapCmsBtJmSkuModel.clear();
        for (CmsBtJmSkuModel model : listCmsBtJmSku) {
            mapCmsBtJmSkuModel.put(model.getId(), model);
        }
    }

    public CmsBtJmPromotionProductModel getModelCmsBtJmPromotionProduct() {
        return modelCmsBtJmPromotionProduct;
    }

    public void setModelCmsBtJmPromotionProduct(CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct) {
        this.modelCmsBtJmPromotionProduct = modelCmsBtJmPromotionProduct;
    }

    public CmsBtJmProductModel getModelCmsBtJmProduct() {
        return modelCmsBtJmProduct;
    }

    public void setModelCmsBtJmProduct(CmsBtJmProductModel modelCmsBtJmProduct) {
        this.modelCmsBtJmProduct = modelCmsBtJmProduct;
    }

    public List<CmsBtJmPromotionSkuModel> getListCmsBtJmPromotionSku() {
        return listCmsBtJmPromotionSku;
    }

    public void setListCmsBtJmPromotionSku(List<CmsBtJmPromotionSkuModel> listCmsBtJmPromotionSku) {
        this.listCmsBtJmPromotionSku = listCmsBtJmPromotionSku;
    }

    public List<CmsBtJmSkuModel> getListCmsBtJmSku() {
        return listCmsBtJmSku;
    }

    public void setListCmsBtJmSku(List<CmsBtJmSkuModel> listCmsBtJmSku) {
        this.listCmsBtJmSku = listCmsBtJmSku;
    }
}
