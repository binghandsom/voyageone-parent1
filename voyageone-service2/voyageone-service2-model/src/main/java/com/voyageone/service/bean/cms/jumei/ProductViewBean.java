package com.voyageone.service.bean.cms.jumei;

import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.util.MapModel;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/3.
 */
public class ProductViewBean {
    CmsBtJmProductModel modelJmProduct;

    CmsBtJmPromotionProductModel modelJmPromotionProduct;
    List<MapModel>  skuList;
    public CmsBtJmPromotionProductModel getModelJmPromotionProduct() {
        return modelJmPromotionProduct;
    }

    public void setModelJmPromotionProduct(CmsBtJmPromotionProductModel modelJmPromotionProduct) {
        this.modelJmPromotionProduct = modelJmPromotionProduct;
    }

    public CmsBtJmProductModel getModelJmProduct() {
        return modelJmProduct;
    }

    public void setModelJmProduct(CmsBtJmProductModel modelJmProduct) {
        this.modelJmProduct = modelJmProduct;
    }


    public List<MapModel> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<MapModel> skuList) {
        this.skuList = skuList;
    }
}
