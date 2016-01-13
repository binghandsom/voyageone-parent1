package com.voyageone.cms.service.impl;

import java.util.*;

import com.voyageone.common.configs.ImsCategoryConfigs;
import com.voyageone.common.configs.beans.ImsCategoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.dao.CategoryDao;
import com.voyageone.cms.dao.CommonDao;
import com.voyageone.cms.dao.ProductDao;
import com.voyageone.cms.dao.PromotionDao;
import com.voyageone.cms.formbean.IPriceSetting;
import com.voyageone.cms.formbean.ProductCNPriceInfo;
import com.voyageone.cms.service.CommonService;
import com.voyageone.cms.utils.CommonUtils;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.util.StringUtils;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private CategoryDao catrgoryDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private PromotionDao promotion;
	
	private static String MSRP = "1";
	

	@Override
	public Map doGetMasterDataList(String channelId) {

		Map<String, Object> ret = new HashMap<String, Object>();

		ret.put("categoryMenuList", commonDao.getCategoryMenuList(channelId));
		ret.put("categoryList", commonDao.doCategoryList(channelId));
		ret.put("amazonCategoryList", commonDao.doAmazonCategoryList(channelId));
		ret.put("googleCategoryList", commonDao.doGoogleCategoryList(channelId));
		ret.put("priceGrabberCategoryList", commonDao.doPriceGrabberCategoryList(channelId));
		ret.put("hsCodeShList", commonDao.doHsCodeShList(channelId));
		ret.put("hsCodeGzList", commonDao.doHsCodeGzList(channelId));
		ret.put("basePriceList", commonDao.doPercentList(channelId, CmsConstants.AttributeType.BasePrice));
		ret.put("percentList", commonDao.doPercentList(channelId, CmsConstants.AttributeType.PercentList));
		ret.put("shippingTypeList", commonDao.doPercentList(channelId,CmsConstants.AttributeType.ShippingList));
		ret.put("USProductAttributes", commonDao.doPercentList(channelId,CmsConstants.AttributeType.USProductAttributes));
		ret.put("CNProductAttributes", commonDao.doPercentList(channelId,CmsConstants.AttributeType.CNProductAttributes));
		ret.put("productTypeList", commonDao.doProductTypeList(channelId));
		ret.put("brandList", commonDao.doBrandList(channelId));
		ret.put("sizeTypeList", commonDao.doSizeTypeList(channelId));
		ret.put("colorMapList", commonDao.doColorMapList(channelId));
		ret.put("countryList", commonDao.doCountryList(channelId));
		ret.put("materialFabricList", commonDao.doMaterialFabricList(channelId));
		ret.put("masterValue", commonDao.doMasterValue(channelId));
		ret.put("cartList", ShopConfigs.getChannelShopList(channelId));
		ret.put("promotionMenuList", promotion.getPromotionList(channelId));
		ret.put("promotionList", promotion.getPromotionInfoList(channelId));
		return ret;
	}
	
	public List<ProductCNPriceInfo> doGetProductCNPriceInfo(String productId, String channelId, String cartId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		data.put("cartId", cartId);
		List<ProductCNPriceInfo> ret = productDao.doGetProductCNBasePriceInfo(data);
		return ret;
	}
	
	/**
	 * 价格系数发生变化时批量更新product的价格
	 * @param productCNPriceInfo
	 * @throws Exception
	 */
	@Override
	public void doUpdateProductCNPriceInfo(String productId, String channelId, IPriceSetting priceSetting, String modifier) throws Exception {
		
		List<ProductCNPriceInfo> productCNPriceList = doGetProductCNPriceInfo(productId, channelId, null);
		for (ProductCNPriceInfo productCNPriceInfo : productCNPriceList) {
			productCNPriceInfo.setModifier(modifier);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("modifier", modifier);
			data.put("productId", productCNPriceInfo.getProductId());
			data.put("channelId", productCNPriceInfo.getChannelId());
			data.put("cartId", productCNPriceInfo.getCartId());
			data.put("modelId", productCNPriceInfo.getModelId());
			data.put("cnGroupId",1);

			if (StringUtils.isEmpty(productCNPriceInfo.getCnPriceDiscount())) {
				productCNPriceInfo.setCnPriceDiscount("0.0");
			}
			
			// CnpriceRmb 算出
			double basePrice;
			double pricingFactor;
			double cnPriceRmb;
			if(MSRP.equals(priceSetting.getBasePriceId())){
				basePrice = Double.parseDouble(productCNPriceInfo.getMsrp());
			}else{
				basePrice = Double.parseDouble(productCNPriceInfo.getCnPrice());
			}
			pricingFactor = Double.parseDouble(priceSetting.getPricingFactor());
			
			cnPriceRmb=CommonUtils.round(basePrice*pricingFactor);
			productCNPriceInfo.setCnPriceRmb(String.valueOf(cnPriceRmb));
			
			// 如果没有设过中国最终价格的 就和中国定价一样
			if(StringUtils.isEmpty(productCNPriceInfo.getCnPriceFinalRmb()) || "0.00".equals(productCNPriceInfo.getCnPriceFinalRmb())){
				// cnFinalPrice算出
				productCNPriceInfo.setCnPriceFinalRmb(String.valueOf(cnPriceRmb));
			}
			

			
//			double overheard1 = priceSetting.getOverHeard1() == null?0.0: Double.parseDouble(priceSetting.getOverHeard1());
//			double overheard2 = priceSetting.getOverHeard2() == null?0.0: Double.parseDouble(priceSetting.getOverHeard2());
//			double rate = priceSetting.getExchangeRate() == null ?1.0:Double.parseDouble(priceSetting.getExchangeRate());
//			double shippingoffset = priceSetting.getShippingCompensation() == null?0.0:Double.parseDouble(priceSetting.getShippingCompensation());
//			double effective = cnFinalPrice * (1 - overheard1 - overheard2)/rate-shippingoffset;
//			
//			productCNPriceInfo.setEffectivePrice(String.valueOf(effective));
			
			
			// 更新ProductCNPrice
			productDao.doUpdateProductCNPriceInfo(productCNPriceInfo);

			// 插入HistoryProductPrice
			data.put("price", productCNPriceInfo.getCnPrice());
			data.put("cnPriceRmb", productCNPriceInfo.getCnPriceRmb());
			data.put("cnPriceAdjustmentRmb", productCNPriceInfo.getCnPriceAdjustmentRmb());
			data.put("cnPriceFinalRmb", productCNPriceInfo.getCnPriceFinalRmb());
			data.put("effectivePrice", productCNPriceInfo.getEffectivePrice());
			productDao.doInsertHistoryProductPrice(data);

			// 更新PublishStatus
			if (productCNPriceInfo.getIsApproved()) {
				data.put("isPublished", false);
				if (productDao.doUpdatePublishStatus(data) == 0) {
					data.put("code", productCNPriceInfo.getCode());
					data.put("cnGroupId", 1);
					productDao.doInsertPublishStatus(data);
				}
			}
		}
	}

	/**
	 * 获取当前channel下的类目.
	 * @param channelId
	 * @return
	 */
	@Override
	public List<ImsCategoryBean> getChannelCategories(String channelId) {

		List<ImsCategoryBean> categories = new ArrayList<>();

		List<String> displayCategories = commonDao.getChannelCategories(channelId);

		List<String> noDisplayCategories = commonDao.getChannelNoDisplayCategories(channelId);

		for(String categoryId:displayCategories){
			ImsCategoryBean channelCategory = null;
			try {
				channelCategory = (ImsCategoryBean)ImsCategoryConfigs.getMtCategoryBeanById(Integer.valueOf(categoryId)).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			for (Iterator<String> iterator = noDisplayCategories.iterator();iterator.hasNext();){
				String noDisplayCategoryId = iterator.next();
				if(removeNoDisplayCategories(channelCategory,noDisplayCategoryId)) {
					iterator.remove();
				}
			}
			categories.add(channelCategory);
		}

		return categories;
	}

	/**
	 *
	 * @param category
	 * @param noDisplayCategoryId
	 */
	private boolean removeNoDisplayCategories(ImsCategoryBean category,String noDisplayCategoryId){

		for (Iterator<ImsCategoryBean> iter = category.getSubCategories().iterator();iter.hasNext();){
			ImsCategoryBean subCategory = iter.next();
			if (noDisplayCategoryId.equals(String.valueOf(subCategory.getCategoryId()))){
				iter.remove();
				return true;
			} else {
				if (removeNoDisplayCategories(subCategory,noDisplayCategoryId))
					return true;
			}
		}
		return false;
	}
}
