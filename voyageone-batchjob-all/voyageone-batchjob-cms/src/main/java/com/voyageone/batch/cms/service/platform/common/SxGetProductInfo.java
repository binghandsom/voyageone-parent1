package com.voyageone.batch.cms.service.platform.common;

import com.voyageone.batch.cms.bean.platform.SxData;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class SxGetProductInfo {
	@Autowired
	CmsProductService cmsProductService;

	/**
	 * 获取group的所有商品的信息
	 */
	public SxData getProductInfoByGroupId(String channelId, Long groupId) {
		SxData sxData = new SxData();

		// 获取group的所有商品的主数据信息
		List<CmsBtProductModel> cmsBtProductModelList = getProductInfo(channelId, groupId);
		sxData.setProductList(cmsBtProductModelList);

		// 分析出需要的platform
		List<CmsBtProductModel_Group_Platform> platformList = new ArrayList<>();
		for (CmsBtProductModel productModel : cmsBtProductModelList) {
			platformList.add(productModel.getGroups().getPlatformByGroupId(groupId));
		}
		sxData.setPlatformList(platformList);

		// 设置一些基本信息
		sxData.setChannelId(channelId);
		sxData.setCartId(platformList.get(0).getCartId());
		sxData.setGroupId(groupId);
		// 平台上的productId
		String platformProductId = "";
		for (CmsBtProductModel_Group_Platform platform : platformList) {
			if (!StringUtils.isEmpty(platform.getProductId())) {
				platformProductId = platform.getProductId();
				break;
			}
		}
		sxData.setPlatformProductId(platformProductId);
		// 平台上的numIId
		String platformNumIId = "";
		for (CmsBtProductModel_Group_Platform platform : platformList) {
			if (!StringUtils.isEmpty(platform.getNumIId())) {
				platformNumIId = platform.getNumIId();
				break;
			}
		}
		sxData.setPlatformNumIId(platformNumIId);

		// 获取group的所有商品的sku信息
		List<CmsBtProductModel_Sku> cmsBtProductModelSkuList = getSkuInfo(sxData.getCartId(), sxData.getSkuList());
		sxData.setSkuList(cmsBtProductModelSkuList);

		return sxData;
	}

	/**
	 * 获取group的所有商品的主数据信息
	 */
	private List<CmsBtProductModel> getProductInfo(String channelId, Long groupId) {
		return cmsProductService.getProductByGroupId(channelId, groupId);
	}

	/**
	 * 获取group的所有商品的sku信息
	 */
	private List<CmsBtProductModel_Sku> getSkuInfo(int cartId, List<CmsBtProductModel_Sku> skuSourceList) {
		List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

		for (CmsBtProductModel_Sku sku : skuSourceList) {
			if (sku.getSkuCarts() != null) {
				if (sku.getSkuCarts().contains(cartId)) {
					skuList.add(sku);
				}
			}
		}

		return skuList;
	}

	/**
	 * 获取group的所有商品的库存信息
	 */
	public void getQtyInfo(SxData sxData) {

	}

}
