package com.voyageone.batch.cms.bean.platform;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;

import java.util.List;

/**
 * Created by zhujiaye on 16/2/14.
 */
public class SxData {
	private String channelId;
	private Integer cartId;
	private Long groupId;
	private int publishStatus;

	private List<CmsBtProductModel> productList;
	private List<CmsBtProductModel_Sku> skuList;

	private String platformProductId;
	private String platformNumIId;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public int getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}

	public List<CmsBtProductModel> getProductList() {
		return productList;
	}

	public void setProductList(List<CmsBtProductModel> productList) {
		this.productList = productList;
	}

	public List<CmsBtProductModel_Sku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<CmsBtProductModel_Sku> skuList) {
		this.skuList = skuList;
	}

	public String getPlatformProductId() {
		return platformProductId;
	}

	public void setPlatformProductId(String platformProductId) {
		this.platformProductId = platformProductId;
	}

	public String getPlatformNumIId() {
		return platformNumIId;
	}

	public void setPlatformNumIId(String platformNumIId) {
		this.platformNumIId = platformNumIId;
	}
}
