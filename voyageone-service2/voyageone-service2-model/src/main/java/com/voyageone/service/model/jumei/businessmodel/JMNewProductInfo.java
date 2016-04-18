package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/12.
 */
public class JMNewProductInfo {
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
    Map<Integer, CmsBtJmSkuModel> mapCmsBtJmSkuModel = new HashMap<>();//key:skuid
    Map<String, CmsBtJmSkuModel> mapCodeCmsBtJmSkuModel = new HashMap<>();//key:SkuCode
    Map<Integer,CmsBtJmPromotionSkuModel> mapSkuIdCmsBtJmPromotionSkuModel=new HashMap<>();//key:skuid value:CmsBtJmPromotionSkuModel

    public Map<Integer, CmsBtJmPromotionSkuModel> getMapSkuIdCmsBtJmPromotionSkuModel() {
        return mapSkuIdCmsBtJmPromotionSkuModel;
    }

    public void setMapSkuIdCmsBtJmPromotionSkuModel(Map<Integer, CmsBtJmPromotionSkuModel> mapSkuIdCmsBtJmPromotionSkuModel) {
        this.mapSkuIdCmsBtJmPromotionSkuModel = mapSkuIdCmsBtJmPromotionSkuModel;
    }

    public Map<String, CmsBtJmSkuModel> getMapCodeCmsBtJmSkuModel() {
        return mapCodeCmsBtJmSkuModel;
    }

    public void setMapCodeCmsBtJmSkuModel(Map<String, CmsBtJmSkuModel> mapCodeCmsBtJmSkuModel) {
        this.mapCodeCmsBtJmSkuModel = mapCodeCmsBtJmSkuModel;
    }

    public void loadData() {
        mapCmsBtJmSkuModel.clear();
        for (CmsBtJmSkuModel model : listCmsBtJmSku) {
            mapCmsBtJmSkuModel.put(model.getId(), model);
            mapCodeCmsBtJmSkuModel.put(model.getSkuCode(), model);
        }
        for (CmsBtJmPromotionSkuModel model : listCmsBtJmPromotionSku) {
            mapSkuIdCmsBtJmPromotionSkuModel.put(model.getCmsBtJmSkuId(), model);
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
