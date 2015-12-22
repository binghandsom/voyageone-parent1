package com.voyageone.web2.cms.model;

import com.voyageone.cms.service.model.CmsBtProductModel;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionCodeModel extends CmsBtPromotionGroupModel {

    private Long productId;

    private String productCode;

    private String productName;

    private Double salePrice;

    private Double promotionPrice;

    private String imageUrlSale;

    private String imageUrlSaleOut;

    private String image;

    private Integer skuCount;

    private String sizeType;

    private Double msrp;

    public CmsBtPromotionCodeModel(CmsBtProductModel productInfo, int cartId, int promotionId, String operator) {
        super(productInfo, cartId, promotionId, operator);
        this.setProductId(productInfo.getProdId());
        this.setProductCode(productInfo.getFields().getCode());
        this.setProductName(productInfo.getFields().getProductName());
        this.setSalePrice(productInfo.getFields().getSalePriceStart());
        this.setMsrp(productInfo.getGroups().getMsrpEnd());
    }

    public CmsBtPromotionCodeModel() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Double promotionPrice) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(Integer skuCount) {
        this.skuCount = skuCount;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public Double getMsrp() {
        return msrp;
    }

    public void setMsrp(Double msrp) {
        this.msrp = msrp;
    }
}
