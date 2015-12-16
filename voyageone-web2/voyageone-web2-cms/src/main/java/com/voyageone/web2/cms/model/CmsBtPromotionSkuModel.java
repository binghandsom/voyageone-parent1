package com.voyageone.web2.cms.model;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.cms.bean.CmsPromotionSkuBean;

import java.util.List;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionSkuModel extends CmsBtPromotionGroupModel {
    private String productCode;

    private List<CmsPromotionSkuBean> productSkus;


    public CmsBtPromotionSkuModel(CmsBtProductModel productInfo, int cartId, int promotionId, String operator) {
        super(productInfo, cartId, promotionId, operator);
        this.setProductCode(productInfo.getFields().getCode());
        productInfo.getSkus().forEach(sku -> {
            productSkus.add(new CmsPromotionSkuBean(sku.getSku(),1));
        });

    }

    public CmsBtPromotionSkuModel() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public List<CmsPromotionSkuBean> getProductSkus() {
        return productSkus;
    }

    public void setProductSkus(List<CmsPromotionSkuBean> productSkus) {
        this.productSkus = productSkus;
    }
}
