package com.voyageone.batch.cms.bean.platform;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;

import java.util.List;
import java.util.Map;

/**
 * Created by zhujiaye on 16/2/14.
 */
public class SxData {
	private String channelId;
	private Integer cartId;
	private Long groupId;
	private int promotionId;										// 活动id (目前只有聚美用到, 由cms进行管理)
	private int publishStatus;										// 上新状态

	private List<CmsBtProductModel> productList; // 单个group中, 包含的所有product列表, 里面虽然有platform和sku信息(但是里面的内容是包含其他group的数据的)
	private List<CmsBtProductModel_Group_Platform> platformList;	// 只包含当前group中, 允许使用的platform信息
	private List<CmsBtProductModel_Sku> skuList;					// 只包含当前group中, 允许使用的sku信息
	private Map<String, Integer> qtyList;							// 只包含当前group中, 允许使用的库存信息

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

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
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

	public List<CmsBtProductModel_Group_Platform> getPlatformList() {
		return platformList;
	}

	public void setPlatformList(List<CmsBtProductModel_Group_Platform> platformList) {
		this.platformList = platformList;
	}

	public List<CmsBtProductModel_Sku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<CmsBtProductModel_Sku> skuList) {
		this.skuList = skuList;
	}

	public Map<String, Integer> getQtyList() {
		return qtyList;
	}

	public void setQtyList(Map<String, Integer> qtyList) {
		this.qtyList = qtyList;
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
