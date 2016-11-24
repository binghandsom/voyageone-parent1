package com.voyageone.service.bean.cms;

import com.taobao.api.internal.util.StringUtils;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionCodesBean extends CmsBtPromotionGroupsBean {

    private Long productId;

    private String productCode;

    private String productName;

    private Double salePrice;

    private Double retailPrice;

    private Double promotionPrice;

    private String image_url_1;

    private String image_url_2;

    private String image_url_3;

    private String image;

    private Integer skuCount;

    private String sizeType;

    private Double msrp;

    private Integer tagId;

    private String tagPath;

    private String tagPathName;

    private Double msrpUS;

    private String property1;

    private String property2;

    private String property3;

    private String property4;

    /**
     * 库存数量
     */
    private Integer quantity;

    private String time;
   private double maxMsrpRmb;
   private double minMsrpRmb;
   private double maxRetailPrice;
   private double minRetailPrice;
    private List<String> tagNameList;
    private double maxSalePrice;
   private double minSalePrice;
   private double maxPromotionPrice;
   private double minPromotionPrice;
   private double maxMsrpUsd;
   private double minMsrpUsd;
    private List<CmsBtPromotionSkuBean> skus;

    public CmsBtPromotionCodesBean(CmsBtProductModel productInfo, CmsBtProductGroupModel groupModel, int promotionId, String operator, Integer cartId) {
        super(productInfo, groupModel, promotionId, operator);
        this.setProductId(productInfo.getProdId());
        this.setProductCode(productInfo.getCommon().getFields().getCode());
        this.setProductName(StringUtils.isEmpty(productInfo.getCommon().getFields().getOriginalTitleCn()) ? productInfo.getCommon().getFields().getProductNameEn() : productInfo.getCommon().getFields().getOriginalTitleCn());
//        this.setProductName(productInfo.getFields().getProductNameEn());
        CmsBtProductModel_Platform_Cart ptfObj = productInfo.getPlatform(cartId);
        if (ptfObj == null) {
            return;
        }
        if (ptfObj.getSkus() == null || ptfObj.getSkus().isEmpty()) {
            return;
        }
        this.setSalePrice(ptfObj.getSkus().get(0).getDoubleAttribute("priceSale"));
        this.setRetailPrice(ptfObj.getSkus().get(0).getDoubleAttribute("priceRetail"));
        this.setMsrp(ptfObj.getSkus().get(0).getDoubleAttribute("priceMsrp"));
    }

    public CmsBtPromotionCodesBean() {
    }

    public List<String> getTagNameList() {
        return tagNameList;
    }

    public void setTagNameList(List<String> tagNameList) {
        this.tagNameList = tagNameList;
    }

    public double getMaxMsrpRmb() {
        return maxMsrpRmb;
    }

    public void setMaxMsrpRmb(double maxMsrpRmb) {
        this.maxMsrpRmb = maxMsrpRmb;
    }

    public double getMinMsrpRmb() {
        return minMsrpRmb;
    }

    public void setMinMsrpRmb(double minMsrpRmb) {
        this.minMsrpRmb = minMsrpRmb;
    }

    public double getMaxRetailPrice() {
        return maxRetailPrice;
    }

    public void setMaxRetailPrice(double maxRetailPrice) {
        this.maxRetailPrice = maxRetailPrice;
    }

    public double getMinRetailPrice() {
        return minRetailPrice;
    }

    public void setMinRetailPrice(double minRetailPrice) {
        this.minRetailPrice = minRetailPrice;
    }

    public double getMaxSalePrice() {
        return maxSalePrice;
    }

    public void setMaxSalePrice(double maxSalePrice) {
        this.maxSalePrice = maxSalePrice;
    }

    public double getMinSalePrice() {
        return minSalePrice;
    }

    public void setMinSalePrice(double minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    public double getMaxPromotionPrice() {
        return maxPromotionPrice;
    }

    public void setMaxPromotionPrice(double maxPromotionPrice) {
        this.maxPromotionPrice = maxPromotionPrice;
    }

    public double getMinPromotionPrice() {
        return minPromotionPrice;
    }

    public void setMinPromotionPrice(double minPromotionPrice) {
        this.minPromotionPrice = minPromotionPrice;
    }

    public double getMaxMsrpUsd() {
        return maxMsrpUsd;
    }

    public void setMaxMsrpUsd(double maxMsrpUsd) {
        this.maxMsrpUsd = maxMsrpUsd;
    }

    public double getMinMsrpUsd() {
        return minMsrpUsd;
    }

    public void setMinMsrpUsd(double minMsrpUsd) {
        this.minMsrpUsd = minMsrpUsd;
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

    public String getImage_url_1() {
        return image_url_1;
    }

    public void setImage_url_1(String image_url_1) {
        this.image_url_1 = image_url_1;
    }

    public String getImage_url_2() {
        return image_url_2;
    }

    public void setImage_url_2(String image_url_2) {
        this.image_url_2 = image_url_2;
    }

    public String getImage_url_3() {
        return image_url_3;
    }

    public void setImage_url_3(String image_url_3) {
        this.image_url_3 = image_url_3;
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

//    public Integer getTagId() {
//        return tagId;
//    }
//
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
//
//    public String getTagPath() {
//        return tagPath;
//    }
//
    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

//    public String getTagPathName() {
//        return tagPathName;
//    }
//
    public void setTagPathName(String tagPathName) {
        this.tagPathName = tagPathName;
    }

    public Double getMsrpUS() {
        return msrpUS;
    }

    public void setMsrpUS(Double msrpUS) {
        this.msrpUS = msrpUS;
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public String getProperty3() {
        return property3;
    }

    public void setProperty3(String property3) {
        this.property3 = property3;
    }

    public String getProperty4() {
        return property4;
    }

    public void setProperty4(String property4) {
        this.property4 = property4;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<CmsBtPromotionSkuBean> getSkus() {
        if (skus == null) {
            skus = new ArrayList<>();
        }
        return skus;
    }

    public void setSkus(List<CmsBtPromotionSkuBean> skus) {
        this.skus = skus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
