package com.voyageone.service.bean.cms.imagetemplate;

import java.util.List;

public  class  ImageTempateParameter {
    List<Integer> cartIdList;
    int imageTemplateType;
    int viewType;
    String imageTemplateName;
    String beginModified;
    String endModified;
    List<String> brandName;
    List<String> productType;
    List<String> sizeType;
    int     pageIndex;
    int     pageSize;


    public int getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public List<Integer> getCartIdList() {
        return cartIdList;
    }
    public void setCartIdList(List<Integer> cartIdList) {
        this.cartIdList = cartIdList;
    }
    public int getImageTemplateType() {
        return imageTemplateType;
    }
    public void setImageTemplateType(int imageTemplateType) {
        this.imageTemplateType = imageTemplateType;
    }
    public int getViewType() {
        return viewType;
    }
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
    public String getImageTemplateName() {
        return imageTemplateName;
    }
    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }
    public String getBeginModified() {
        return beginModified;
    }
    public void setBeginModified(String beginModified) {
        this.beginModified = beginModified;
    }
    public String getEndModified() {
        return endModified;
    }
    public void setEndModified(String endModified) {
        this.endModified = endModified;
    }
    public List<String> getBrandName() {
        return brandName;
    }
    public void setBrandName(List<String> brandName) {
        this.brandName = brandName;
    }
    public List<String> getProductType() {
        return productType;
    }
    public void setProductType(List<String> productType) {
        this.productType = productType;
    }
    public List<String> getSizeType() {
        return sizeType;
    }
    public void setSizeType(List<String> sizeType) {
        this.sizeType = sizeType;
    }
}