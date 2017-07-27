package com.voyageone.service.model.cms;

import java.util.List;

/**
 * Created by Charis on 2017/7/25.
 */
public class TransferUsCategoryModel {

    private String channelId;
    private String storeId;
    private String id;
    private String parentId;
    private String categoryPath;
    private Integer displayOrder;
    private String name;
    private boolean isPublished;
    private String urlKey;
    private String headerTitle;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
    private String seoCanonical;
    private boolean isVisibleOnMenu;
    private boolean isEnableFilter;
    private List<String> productCodes;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoCanonical() {
        return seoCanonical;
    }

    public void setSeoCanonical(String seoCanonical) {
        this.seoCanonical = seoCanonical;
    }

    public boolean isVisibleOnMenu() {
        return isVisibleOnMenu;
    }

    public void setVisibleOnMenu(boolean visibleOnMenu) {
        isVisibleOnMenu = visibleOnMenu;
    }

    public boolean isEnableFilter() {
        return isEnableFilter;
    }

    public void setEnableFilter(boolean enableFilter) {
        isEnableFilter = enableFilter;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }
}
