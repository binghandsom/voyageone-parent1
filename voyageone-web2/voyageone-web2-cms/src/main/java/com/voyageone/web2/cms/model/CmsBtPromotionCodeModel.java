package com.voyageone.web2.cms.model;

import com.voyageone.cms.service.model.CmsBtProductModel;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionCodeModel extends CmsBtPromotionGroupModel {

    private String productCode;

    private String productName;

    private double salePrice;

    private double promotionPrice;

    private String imageUrlSale;

    private String imageUrlSaleOut;

    public CmsBtPromotionCodeModel(CmsBtProductModel productInfo, int cartId, int promotionId, String operator) {
        super(productInfo, cartId, promotionId, operator);
        this.setProductCode(productInfo.getFields().getCode());
        this.setProductName(productInfo.getFields().getName());
    }

    public CmsBtPromotionCodeModel() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getImageUrlSale() {
        return imageUrlSale;
    }

    public void setImageUrlSale(String imageUrlSale) {
        this.imageUrlSale = imageUrlSale;
    }

    public String getImageUrlSaleOut() {
        return imageUrlSaleOut;
    }

    public void setImageUrlSaleOut(String imageUrlSaleOut) {
        this.imageUrlSaleOut = imageUrlSaleOut;
    }
}
