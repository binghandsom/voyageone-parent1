package com.voyageone.web2.cms.bean;

import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;

import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 16-1-5.
 */
public class CmsProductInfoBean {

    private boolean productDataIsReady;

    private String channelId;

    private Long productId;

    private String categoryId;

    private String categoryFullPath;

    private List<Field> masterFields;

    private Field skuFields;

    private CustomAttributesBean customAttributes;

    private ProductStatus productStatus;

    protected String modified;

    private String productCode;

    //商品图片信息.
    private Map<String,List<CmsBtProductModel_Field_Image>> productImages;

    // feed方数据.
    private Map<String,String> feedInfoModel;

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryFullPath() {
        return categoryFullPath;
    }

    public void setCategoryFullPath(String categoryFullPath) {
        this.categoryFullPath = categoryFullPath;
    }

    public List<Field> getMasterFields() {
        return masterFields;
    }

    public void setMasterFields(List<Field> masterFields) {
        this.masterFields = masterFields;
    }

    public Field getSkuFields() {
        return skuFields;
    }

    public void setSkuFields(Field skuFields) {
        this.skuFields = skuFields;
    }

    public CustomAttributesBean getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(CustomAttributesBean customAttributes) {
        this.customAttributes = customAttributes;
    }

    public Map<String, List<CmsBtProductModel_Field_Image>> getProductImages() {
        return productImages;
    }

    public void setProductImages(Map<String, List<CmsBtProductModel_Field_Image>> productImages) {
        this.productImages = productImages;
    }

    public Map<String, String> getFeedInfoModel() {
        return feedInfoModel;
    }

    public void setFeedInfoModel(Map<String, String> feedInfoModel) {
        this.feedInfoModel = feedInfoModel;
    }

    public ProductStatus getProductStatusInstance(){
        return new ProductStatus();
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public boolean isProductDataIsReady() {
        return productDataIsReady;
    }

    public void setProductDataIsReady(boolean productDataIsReady) {
        this.productDataIsReady = productDataIsReady;
    }

    /**
     * 产品各种状态.
     */
    public class ProductStatus {

        //是否approved.
        private String approveStatus;

        //是否翻译完成.
        private Boolean translateStatus;

        //是否编辑完成.
        private Boolean editStatus;

        public String getApproveStatus() {
            return approveStatus;
        }

        public void setApproveStatus(String approveStatus) {
            this.approveStatus = approveStatus;
        }

        public Boolean getTranslateStatus() {
            return translateStatus;
        }

        public void setTranslateStatus(Boolean translateStatus) {
            this.translateStatus = translateStatus;
        }

        public Boolean getEditStatus() {
            return editStatus;
        }

        public void setEditStatus(Boolean editStatus) {
            this.editStatus = editStatus;
        }
    }

}
