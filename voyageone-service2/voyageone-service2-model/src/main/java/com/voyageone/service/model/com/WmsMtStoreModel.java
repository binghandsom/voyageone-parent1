/*
 * WmsMtStoreModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

/**
 * 
 */
public class WmsMtStoreModel extends WmsMtStoreKey {
    protected String storeName;

    /**
     * 0:自营仓库 1:第三方合作仓库 2:菜鸟保税仓 3:聚美保税仓
     */
    protected String storeType;

    /**
     * 0:国外仓库 1:中国仓库
     */
    protected String storeLocation;

    /**
     * 0:真实仓库 1:超卖仓库
     */
    protected String storeKind;

    /**
     * 默认为自己 如果是子仓库，那么就设置主仓库的ID
     */
    protected Integer parentStoreId;

    /**
     * 0:详细拣货单 1:简单拣货单大 2:简单拣货单小 3:两联拣货单
     */
    protected String labelType;

    protected Integer rsvSort;

    /**
     * 该仓库的商品是否在售标志位（0:不在售；1:在售）
     */
    protected String isSale;

    /**
     * 库存管理（0：不管理；1：管理）
     */
    protected String inventoryManager;

    /**
     * 库存保留量（Val1：类型；Val2：保留量）
0：不做保留
1：按加减保留
2：按百分比保留
3：按销售计算（默认百分比）
     */
    protected String inventoryHold;

    /**
     * 库存同步类型（0：全量；1：增量；2：刷新）
     */
    protected String inventorySynType;

    protected String storeArea;

    /**
     * 仓库对应的拼单区域 0：不拼单，1：US拼单；2：HK拼单；3：CN拼单
     */
    protected Long areaId;

    protected String storeComment;

    protected Boolean active;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType == null ? null : storeType.trim();
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation == null ? null : storeLocation.trim();
    }

    public String getStoreKind() {
        return storeKind;
    }

    public void setStoreKind(String storeKind) {
        this.storeKind = storeKind == null ? null : storeKind.trim();
    }

    public Integer getParentStoreId() {
        return parentStoreId;
    }

    public void setParentStoreId(Integer parentStoreId) {
        this.parentStoreId = parentStoreId;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType == null ? null : labelType.trim();
    }

    public Integer getRsvSort() {
        return rsvSort;
    }

    public void setRsvSort(Integer rsvSort) {
        this.rsvSort = rsvSort;
    }

    public String getIsSale() {
        return isSale;
    }

    public void setIsSale(String isSale) {
        this.isSale = isSale == null ? null : isSale.trim();
    }

    public String getInventoryManager() {
        return inventoryManager;
    }

    public void setInventoryManager(String inventoryManager) {
        this.inventoryManager = inventoryManager == null ? null : inventoryManager.trim();
    }

    public String getInventoryHold() {
        return inventoryHold;
    }

    public void setInventoryHold(String inventoryHold) {
        this.inventoryHold = inventoryHold == null ? null : inventoryHold.trim();
    }

    public String getInventorySynType() {
        return inventorySynType;
    }

    public void setInventorySynType(String inventorySynType) {
        this.inventorySynType = inventorySynType == null ? null : inventorySynType.trim();
    }

    public String getStoreArea() {
        return storeArea;
    }

    public void setStoreArea(String storeArea) {
        this.storeArea = storeArea == null ? null : storeArea.trim();
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getStoreComment() {
        return storeComment;
    }

    public void setStoreComment(String storeComment) {
        this.storeComment = storeComment == null ? null : storeComment.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}