package com.voyageone.service.model.jumei.businessmodel;
import com.voyageone.service.model.jumei.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 商品导入保存信息
 */
public class CmsBtJmProductImportSaveInfo {
    CmsBtJmProductModel productModel;//商品
    List<CmsBtJmSkuModel> listSkuModel;//规格
    CmsBtJmPromotionProductModel promotionProductModel;//活动商品
    List<CmsBtJmPromotionSkuModel> listPromotionSkuModel;//活动规格


    public List<CmsBtJmProductImagesModel> getListCmsBtJmProductImagesModel() {
        return listCmsBtJmProductImagesModel;
    }

    public void setListCmsBtJmProductImagesModel(List<CmsBtJmProductImagesModel> listCmsBtJmProductImagesModel) {
        this.listCmsBtJmProductImagesModel = listCmsBtJmProductImagesModel;
    }

    List<CmsBtJmProductImagesModel> listCmsBtJmProductImagesModel;//商品聚美图片


    public CmsBtJmProductImportSaveInfo() {
        this.setListSkuModel(new ArrayList<>());
        this.setListPromotionSkuModel(new ArrayList<>());
        this.setListCmsBtJmProductImagesModel(new ArrayList<>());
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
