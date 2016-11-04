package com.voyageone.service.bean.cms.jumei2;

import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;

/**
 * 用于聚美专场v2中，组合传递商品信息
 * Created by jonas on 2016/10/19.
 *
 * @version 2.8.0
 * @since 2.8.0
 */
public class JmProduct {
    private CmsBtJmProductModel jmProduct;
    private CmsBtJmPromotionProductModel jmPromotionProduct;

    public JmProduct() {
    }

    public JmProduct(CmsBtJmProductModel jmProduct, CmsBtJmPromotionProductModel jmPromotionProduct) {
        this.jmProduct = jmProduct;
        this.jmPromotionProduct = jmPromotionProduct;
    }

    public CmsBtJmProductModel getJmProduct() {
        return jmProduct;
    }

    public void setJmProduct(CmsBtJmProductModel jmProduct) {
        this.jmProduct = jmProduct;
    }

    public CmsBtJmPromotionProductModel getJmPromotionProduct() {
        return jmPromotionProduct;
    }

    public void setJmPromotionProduct(CmsBtJmPromotionProductModel jmPromotionProduct) {
        this.jmPromotionProduct = jmPromotionProduct;
    }
}