package com.voyageone.common.bussiness.platformInfo.model;
/**
 * 
 * @author lewis
 *
 */
public class PlatformInfoModel {
	/**
	 * 主类目id.
	 */
	private int categoryId;
	/**
	 * 平台分类id.
	 */
	private int platformCid;
	/**
	 * 平台id
	 */
	private int platformId;
	/**
	 * 子平台id.
	 */
	private int cartId;
	/**
	 * 平台代码.
	 */
	private String platformCode;
	/**
	 * 平台中文名字.
	 */
	private String platformName;
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getPlatformCid() {
		return platformCid;
	}
	public void setPlatformCid(int platformCid) {
		this.platformCid = platformCid;
	}
	public int getPlatformId() {
		return platformId;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public String getPlatformCode() {
		return platformCode;
	}
	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	
}
