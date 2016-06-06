package com.voyageone.service.bean.cms.jumei2;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionTagProductModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/5/27.
 */
public class ProductSaveInfo {
    public CmsBtJmPromotionProductModel productModel=new CmsBtJmPromotionProductModel();
    public List<CmsBtJmPromotionSkuModel> skuList=new ArrayList<>();
    public List<CmsBtJmPromotionTagProductModel> tagList=new ArrayList<>();
//    public CmsBtJmPromotionProductModel getProduct() {
//        return product;
//    }
//
//    public void setProduct(CmsBtJmPromotionProductModel product) {
//        this.product = product;
//    }
//
//    public List<CmsBtJmPromotionSkuModel> getSkuList() {
//        return skuList;
//    }
//
//    public void setSkuList(List<CmsBtJmPromotionSkuModel> skuList) {
//        this.skuList = skuList;
//    }
//
//    public List<CmsBtJmPromotionTagProductModel> getTagList() {
//        return tagList;
//    }
//
//    public void setTagList(List<CmsBtJmPromotionTagProductModel> tagList) {
//        this.tagList = tagList;
//    }
}