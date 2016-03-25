package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品导入保存信息
 */
public class CmsBtJmProductImportSaveInfo {
    CmsBtJmProductModel productModel;
    List<CmsBtJmSkuModel> listSkuModel;
    CmsBtJmPromotionProductModel promotionProductModel;
    List<CmsBtJmPromotionSkuModel> listPromotionSkuModel;

    public  CmsBtJmProductImportSaveInfo()
    {
        this.setListSkuModel(new ArrayList<>());
        this.setListPromotionSkuModel(new ArrayList<>());
    }
    public List<CmsBtJmSkuModel> getListSkuModel() {
        return listSkuModel;
    }
    public void setListSkuModel(List<CmsBtJmSkuModel> listSkuModel) {
        this.listSkuModel = listSkuModel;
    }
    public List<CmsBtJmPromotionSkuModel> getListPromotionSkuModel() {
        return listPromotionSkuModel;
    }
    public void setListPromotionSkuModel(List<CmsBtJmPromotionSkuModel> listPromotionSkuModel) {
        this.listPromotionSkuModel = listPromotionSkuModel;
    }
    public CmsBtJmProductModel getProductModel() {
        return productModel;
    }
    public void setProductModel(CmsBtJmProductModel productModel) {
        this.productModel = productModel;
    }
    public CmsBtJmPromotionProductModel getPromotionProductModel() {
        return promotionProductModel;
    }
    public void setPromotionProductModel(CmsBtJmPromotionProductModel promotionProductModel) {
        this.promotionProductModel = promotionProductModel;
    }
}
