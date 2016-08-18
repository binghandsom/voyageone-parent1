package com.voyageone.web2.admin.bean.store;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/12
 */
public class StoreFormBean extends AdminFormBean {
	
	private String orderChannelId;
	
	private Long storeId;
	
	private String storeName;
	
	private String storeType;
	
	private String storeLocation;
	
	private String storeKind;
	
	private Integer parentStoreId;
	
	private String labelType;
	
	private Integer rsvSort;
	
	private String isSale;
	
	private String inventoryManager;
	
	private String inventoryHold;
	
	private String inventorySynType;
	
	private String storeArea;
	
	private Integer areaId;
	
	private String storeComment;
	
	private Boolean active;

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	public String getStoreKind() {
		return storeKind;
	}

	public void setStoreKind(String storeKind) {
		this.storeKind = storeKind;
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
		this.labelType = labelType;
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
		this.isSale = isSale;
	}

	public String getInventoryManager() {
		return inventoryManager;
	}

	public void setInventoryManager(String inventoryManager) {
		this.inventoryManager = inventoryManager;
	}

	public String getInventoryHold() {
		return inventoryHold;
	}

	public void setInventoryHold(String inventoryHold) {
		this.inventoryHold = inventoryHold;
	}

	public String getInventorySynType() {
		return inventorySynType;
	}

	public void setInventorySynType(String inventorySynType) {
		this.inventorySynType = inventorySynType;
	}

	public String getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(String storeArea) {
		this.storeArea = storeArea;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getStoreComment() {
		return storeComment;
	}

	public void setStoreComment(String storeComment) {
		this.storeComment = storeComment;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
