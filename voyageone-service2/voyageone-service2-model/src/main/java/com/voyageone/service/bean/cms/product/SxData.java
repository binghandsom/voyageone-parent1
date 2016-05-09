package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.List;

/**
 * Created by morse.lu on 16/4/20.
 */
public class SxData {
	private String channelId;
	private Integer cartId;
	private Long groupId;

	private CmsBtProductModel mainProduct; // 主商品
	private CmsBtFeedInfoModel cmsBtFeedInfoModel; // 主商品的feed信息
	private CmsBtProductGroupModel platform; // 平台信息(也是当前group信息)
	private List<CmsBtProductModel> productList; // 单个group中, 包含的所有product列表(product下所有sku都没有当前cartId，则去除)
	private List<CmsBtProductModel_Sku> skuList;	// 只包含当前group，cart中, 允许使用的sku信息

    private Double maxPrice; // 当前productList的所有sku中，最大的那个价格（销售的价格）

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

	public CmsBtProductModel getMainProduct() {
		return mainProduct;
	}

	public void setMainProduct(CmsBtProductModel mainProduct) {
		this.mainProduct = mainProduct;
	}

	public CmsBtFeedInfoModel getCmsBtFeedInfoModel() {
		return cmsBtFeedInfoModel;
	}

	public void setCmsBtFeedInfoModel(CmsBtFeedInfoModel cmsBtFeedInfoModel) {
		this.cmsBtFeedInfoModel = cmsBtFeedInfoModel;
	}

	public CmsBtProductGroupModel getPlatform() {
		return platform;
	}

	public void setPlatform(CmsBtProductGroupModel platform) {
		this.platform = platform;
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

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
