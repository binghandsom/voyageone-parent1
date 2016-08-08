package com.voyageone.service.bean.cms.jumei;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

/**
 * Created by dell on 2016/8/5.
 */
public class UpdateJmParameter {
    public CmsBtProductModel getCmsBtProductModel() {
        return cmsBtProductModel;
    }

    public void setCmsBtProductModel(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    public CmsBtJmPromotionModel getCmsBtJmPromotionModel() {
        return cmsBtJmPromotionModel;
    }

    public void setCmsBtJmPromotionModel(CmsBtJmPromotionModel cmsBtJmPromotionModel) {
        this.cmsBtJmPromotionModel = cmsBtJmPromotionModel;
    }

    public CmsBtJmPromotionProductModel getCmsBtJmPromotionProductModel() {
        return cmsBtJmPromotionProductModel;
    }

    public void setCmsBtJmPromotionProductModel(CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel) {
        this.cmsBtJmPromotionProductModel = cmsBtJmPromotionProductModel;
    }

    public ShopBean getShopBean() {
        return shopBean;
    }

    public void setShopBean(ShopBean shopBean) {
        this.shopBean = shopBean;
    }

    //商品表 mongo
    public CmsBtProductModel cmsBtProductModel;
    //聚美活动表
    public CmsBtJmPromotionModel cmsBtJmPromotionModel;
    //聚美活动商品I包
    public CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel;
    //聚美平台信息
    public CmsBtProductModel_Platform_Cart platform;
    public ShopBean shopBean;
}
