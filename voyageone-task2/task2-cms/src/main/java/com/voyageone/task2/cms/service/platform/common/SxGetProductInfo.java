package com.voyageone.task2.cms.service.platform.common;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.bean.platform.SxData;
import com.voyageone.task2.cms.dao.SkuInventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class SxGetProductInfo {
	@Autowired
	private ProductService productService;
	@Autowired
	private CmsBtProductGroupDao cmsBtProductGroupDao;
	@Autowired
	private SkuInventoryDao skuInventoryDao;
	@Autowired
	private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;

	/**
	 * 获取group的所有商品的信息
	 */
	public SxData getProductInfoByGroupId(String channelId, Long groupId) {
		SxData sxData = new SxData();
		List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

		// 获取group的所有商品的主数据信息 (取出一堆的product)
		List<CmsBtProductBean> cmsBtProductModelList = getProductInfo(channelId, groupId);
		sxData.setProductList(cmsBtProductModelList);

		// 分析出需要的platform (只取出当前的group的platform信息, 存放的是多个product的当前platform)
		CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
		List<CmsBtProductGroupModel> platformList = new ArrayList<>();
		for (CmsBtProductModel productModel : cmsBtProductModelList) {
			platformList.add(grpObj);
			skuList.addAll(productModel.getCommon().getSkus());
		}
		sxData.setPlatformList(platformList);

		// 设置一些基本信息
		sxData.setChannelId(channelId);
		sxData.setCartId(platformList.get(0).getCartId());
		sxData.setGroupId(groupId);
		// ShopBean
		ShopBean shopBean = Shops.getShop(channelId, sxData.getCartId());
		sxData.setShopBean(shopBean);
		// 平台类目
		CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId(
				channelId, sxData.getCartId(), sxData.getProductList().get(0).getCommon().getCatId()
		);
		if (cmsMtPlatformMappingModel == null)  {
			// TODO: 主数据与平台之间没有做过类目mapping, 无法上新
			return null;
		}
//		if (cmsMtPlatformMappingModel.getMatchOver() == 0) {
//			// TODO: 主数据与平台之间的属性mapping没有完成, 无法上新
//			return null;
//		}
		sxData.setPlatformCategoryId(cmsMtPlatformMappingModel.getPlatformCategoryId());
		// 平台上的productId
		String platformProductId = "";
		for (CmsBtProductGroupModel platform : platformList) {
			if (!StringUtils.isEmpty(platform.getPlatformPid())) {
				platformProductId = platform.getPlatformPid();
				break;
			}
		}
		sxData.setPlatformProductId(platformProductId);
		// 平台上的numIId
		String platformNumIId = "";
		for (CmsBtProductGroupModel platform : platformList) {
			if (!StringUtils.isEmpty(platform.getNumIId())) {
				platformNumIId = platform.getNumIId();
				break;
			}
		}
		sxData.setPlatformNumIId(platformNumIId);

		// 获取group的所有商品的sku信息 (剔除不需要在当前cart中上新的sku, 内容是所有product在当前cart中需要上新的sku)
		List<CmsBtProductModel_Sku> cmsBtProductModelSkuList = getSkuInfo(sxData.getCartId(), skuList);
		sxData.setSkuList(cmsBtProductModelSkuList);

		// 获取库存信息
		List<String> skus = new ArrayList<>();
		for (CmsBtProductModel_Sku sku : cmsBtProductModelSkuList) {
			skus.add(sku.getSkuCode());
		}
		Map<String, Integer> qtyList = skuInventoryDao.getSkuInventory(channelId, skus);
		// 如果找不到的话, 库存直接设置为0
		for (CmsBtProductModel_Sku sku : cmsBtProductModelSkuList) {
			if (!qtyList.containsKey(sku.getSkuCode())) {
				qtyList.put(sku.getSkuCode(), 0);
			}
		}
		sxData.setQtyList(qtyList);

		return sxData;
	}

	/**
	 * 获取group的所有商品的主数据信息
	 */
	private List<CmsBtProductBean> getProductInfo(String channelId, Long groupId) {
		return productService.getProductByGroupId(channelId, groupId, false);
	}

	/**
	 * 获取group的所有商品的sku信息 (剔除不需要在当前cart中上新的sku)
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

		// 排序一下
		skuList = sortSkuInfo(skuList);

		return skuList;
	}

	/**
	 * 将sku排序一下, 用于显示在平台上面好看一点
	 * TODO: 排序的代码先临时这样写一下, 等最后有空了再改好看一点 tom
	 * TODO: 另外, 排序的算法这样写其实速度也是很一般, 如果以后数据量很大的话, 这里可以优化一下的 tom
	 * @param skuSourceList 等待排序的sku列表
	 * @return 排序之后的sku列表
	 */
	private List<CmsBtProductModel_Sku> sortSkuInfo(List<CmsBtProductModel_Sku> skuSourceList) {

		// 准备排序用的顺序
		List<String> lstSortRuleList = new ArrayList<>();
		// 纯数字系列
		for (int i = 0; i < 100; i++) {
			lstSortRuleList.add(String.valueOf(i));
			lstSortRuleList.add(String.valueOf(i) + ".5");
		}
		// 纯数字系列(cm)
		for (int i = 0; i < 100; i++) {
			lstSortRuleList.add(String.valueOf(i) + "cm");
			lstSortRuleList.add(String.valueOf(i) + ".5cm");
		}
		// SM系列
		lstSortRuleList.add("XXX");
		lstSortRuleList.add("XXS");
		lstSortRuleList.add("XS");
		lstSortRuleList.add("XS/S");
		lstSortRuleList.add("XSS");
		lstSortRuleList.add("S");
		lstSortRuleList.add("S/M");
		lstSortRuleList.add("M");
		lstSortRuleList.add("M/L");
		lstSortRuleList.add("L");
		lstSortRuleList.add("XL");
		lstSortRuleList.add("XXL");

		// 包的尺码不参与排序(放最后)
		lstSortRuleList.add("N/S");
		// OneSize尺码不参与排序(放最后)
		lstSortRuleList.add("O/S");
		lstSortRuleList.add("OneSize");

		// 排序
		List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

		for (String strValue : lstSortRuleList) {
			for (CmsBtProductModel_Sku value : skuSourceList) {
				String valueCheck = value.getSize();

				if (strValue.equals(valueCheck)) {
					skuList.add(value);
				}
			}
		}

		// 把剩下不在排序表里的例外的情况,放到最后面
		for (CmsBtProductModel_Sku value : skuSourceList) {
			String valueCheck = value.getSize();

			if (!lstSortRuleList.contains(valueCheck)) {
				skuList.add(value);
			}
		}

		return skuList;
	}

}
