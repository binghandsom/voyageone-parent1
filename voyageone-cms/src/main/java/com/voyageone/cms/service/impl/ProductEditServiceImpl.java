package com.voyageone.cms.service.impl;

import com.voyageone.cms.dao.CommonDao;
import com.voyageone.cms.dao.ProductDao;
import com.voyageone.cms.formbean.*;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.cms.service.ModelEditService;
import com.voyageone.cms.service.ProductEditService;
import com.voyageone.cms.utils.CommonUtils;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class ProductEditServiceImpl implements ProductEditService {

	private static Log logger = LogFactory.getLog(ProductEditServiceImpl.class);

	@Autowired
	private ModelEditService modelService;

	@Autowired
	private SimpleTransaction simpleTransactionCms;
	@Autowired
	private CommonServiceImpl commonServiceImpl;

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private CommonDao commonDao;
	@Override
	public ProductUSBean doGetUSProductInfo(String productId, String channelId, boolean isExtend) throws Exception {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		ProductUSBean productUSBean = productDao.doGetUSProductInfo(data);
		if (productUSBean != null) {
			if (isExtend) {
				ModelUSBean modelUSBean = modelService.doGetUSModelInfo(productUSBean.getModelId(), productUSBean.getChannelId(), isExtend);
				CommonUtils.merger(productUSBean, modelUSBean);
			}
		}
		return productUSBean;
	}

	@Override
	public ProductCNBean doGetCNProductInfo(String productId, String channelId, boolean isExtend) throws Exception {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		ProductCNBean productCNBean = productDao.doGetCNProductInfo(data);
		if (productCNBean != null) {
			if (isExtend) {
				ModelCNBean modelCNBean = modelService.doGetCNModelInfo(productCNBean.getModelId(), productCNBean.getChannelId(), isExtend);
				if (modelCNBean != null) {
					CommonUtils.merger(productCNBean.getCnBaseProductInfo(), modelCNBean.getCnBaseModelInfo());
					CommonUtils.merger(productCNBean.getTmProductInfo(), modelCNBean.getTmModelInfo());
					CommonUtils.merger(productCNBean.getJdProductInfo(), modelCNBean.getJdModelInfo());
				}
			}
		}
		return productCNBean;
	}

	@Override
	public ProductPriceSettingBean doGetProductCNPriceSettingInfo(String productId, String channelId, boolean isExtend) throws Exception {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		ProductPriceSettingBean productPriceSetting = productDao.doGetProductCNPriceSettingInfo(data);
		if (productPriceSetting != null) {
			if (isExtend) {
				ModelPriceSettingBean modelPriceSettingBean = modelService.doGetModelCNPriceSettingInfo(productPriceSetting.getModelId(), channelId, isExtend);
				CommonUtils.merger(productPriceSetting, modelPriceSettingBean);
			}
		}
		return productPriceSetting;
	}

	@Override
	public List<ProductUSPriceInfo> doGetProductUSPriceInfo(String productId, String channelId, String cartId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		data.put("cartId", cartId);
		List<ProductUSPriceInfo> ret = productDao.doGetProductUSPriceInfo(data);
		return ret;
	}

	// @Override
	// public ProductUSAmazonPriceInfo doGetProductUSAmazonPriceInfo (String
	// productId, String channelId) {
	// HashMap<String, Object> data = new HashMap<String, Object>();
	// data.put("productId", productId);
	// data.put("channelId", channelId);
	// ProductUSAmazonPriceInfo ret =
	// productDao.doGetProductUSAmazonPriceInfo(data);
	// return ret;
	// }
	@Override
	public List<ProductCNPriceInfo> doGetProductCNPriceInfo(String productId, String channelId, String cartId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		data.put("cartId", cartId);
		List<ProductCNPriceInfo> ret = productDao.doGetProductCNBasePriceInfo(data);
		return ret;
	}

	// @Override
	// public ProductCNCartPriceInfo doGetProductCNCartPriceInfo (String
	// productId, String channelId) {
	// HashMap<String, Object> data = new HashMap<String, Object>();
	// data.put("productId", productId);
	// data.put("channelId", channelId);
	// ProductCNCartPriceInfo ret =
	// productDao.doGetProductCNCartPriceInfo(data);
	// return ret;
	// }\
	@Override
	public List<ProductImage> doGetProductImage(String productId, String channelId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("productId", productId);
		data.put("channelId", channelId);
		List<ProductImage> ret = productDao.doGetProductImage(data);
		return ret;
	}

	/**
	 * 获取Inventory数据
	 * 
	 * @param channelId
	 * @param code
	 * @return
	 */
	@Override
	public List<Map<String, Object>> doGetInventory(String channelId, String code) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("channelId", channelId);
		data.put("code", code);
		List<Map<String, Object>> ret = productDao.doGetInventory(data);
		return ret;

	}

	@Override
	public boolean doSetCnProductProperty(Map<String, Object> paramMap) {
		simpleTransactionCms.openTransaction();
		try{
			String effective = String.valueOf(paramMap.get("effective"));
			if(!StringUtils.isNullOrBlank2(effective)) {
				paramMap.put("effective", effective);
				productDao.doUpdtProductEffective(paramMap);
			}
			String cnIsApprovedDescription = String.valueOf(paramMap.get("cnIsApprovedDescription"));
			if(!StringUtils.isNullOrBlank2(cnIsApprovedDescription)){
				paramMap.put("cnIsApprovedDescription", cnIsApprovedDescription);
				productDao.doUpdtProductIsApprovedDes(paramMap);
			}
			String hsCodeId = String.valueOf(paramMap.get("hsCodeId"));
			String hsCodePuId = String.valueOf(paramMap.get("hsCodePuId"));
			if(!StringUtils.isNullOrBlank2(hsCodeId) || !StringUtils.isNullOrBlank2(hsCodePuId)) {
				productDao.doUpdtProductHsCode(paramMap);
			}
		}catch (Exception e){
			simpleTransactionCms.rollback();
			throw e;
		}
		simpleTransactionCms.commit();
		return true;
	}

	@Override
	public boolean doSetCnProductShare(Map<String, Object> paramMap, UserSessionBean user) {
		simpleTransactionCms.openTransaction();
		try{
			String percent = String.valueOf(paramMap.get("percent"));
			// 如果percent填写了值，那么说明价格改变了，改变了价格需要更新表【cms_bt_history_product_price】
			if(!StringUtils.isNullOrBlank2(percent)) {
				paramMap.put("percent", (1 - Double.parseDouble(String.valueOf(paramMap.get("percent"))) / 100));
				setValueList(paramMap);
				productDao.doInstHisProductPrice(paramMap);
			}
			productDao.doUpdtProductShare(paramMap);
			// 修改或者更新ims_bt_Product表
			String isApproved = String.valueOf(paramMap.get("isApproved"));
			if ("1".equals(isApproved)) {
				List<ProductCNBaseProductInfo> productInfoList = productDao.getProductInfoById(paramMap);
				for (ProductCNBaseProductInfo productCNBaseProductInfo : productInfoList) {
					paramMap.put("productId", productCNBaseProductInfo.getProductId());
					paramMap.put("modelId", productCNBaseProductInfo.getModelId());
					paramMap.put("code", productCNBaseProductInfo.getCode());
					productDao.doInsertImsBtProduct(paramMap);
				}
			} else {
				productDao.doUpdateImsBtProduct(paramMap);
			}
		}catch (Exception e){
			simpleTransactionCms.rollback();
			throw e;
		}
		simpleTransactionCms.commit();
		return true;
	}

	@Override
	public DtResponse<List<RelationPromotionProduct>> doGetPromotionHistory(DtRequest<ProductUSPriceInfo> dtRequest) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("productId", dtRequest.getParam().getProductId());
		params.put("channelId", dtRequest.getParam().getChannelId());
		params.put("cartId", dtRequest.getParam().getCartId());
		params.put("offset", dtRequest.getStart());
		params.put("pageCount", dtRequest.getLength());
		List<RelationPromotionProduct> result = productDao.doGetPromotionHistory(params);
		DtResponse<List<RelationPromotionProduct>> dtResponse = new DtResponse<>();
		dtResponse.setData(result);
		int count = productDao.doGetPromotionHistoryCount(params);
		dtResponse.setRecordsFiltered(count);
		dtResponse.setRecordsTotal(count);
		return dtResponse;
	}


	@Override
	public DtResponse<List<ProductUSPriceInfo>> doGetPirceHistory(DtRequest<ProductUSPriceInfo> dtRequest) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("productId", dtRequest.getParam().getProductId());
		params.put("channelId", dtRequest.getParam().getChannelId());
		params.put("priceType", dtRequest.getParam().getPriceType());
		params.put("cartId", dtRequest.getParam().getCartId());
		params.put("offset", dtRequest.getStart());
		params.put("pageCount", dtRequest.getLength());
		List<ProductUSPriceInfo> result = productDao.doGetPriceHistory(params);
		DtResponse<List<ProductUSPriceInfo>> dtResponse = new DtResponse<>();
		dtResponse.setData(result);
		int count = productDao.doGetPriceHistoryCount(params);
		dtResponse.setRecordsFiltered(count);
		dtResponse.setRecordsTotal(count);
		return dtResponse;
	}

	private void setValueList(Map<String, Object> paramMap) {
		List<Integer> list = (List<Integer>) paramMap.get("productIdList");
		List<ProductCNPriceInfo> priceInfos = new ArrayList<>();
		for(int productId : list){
			paramMap.put("productId", productId);
			ProductCNPriceInfo priceInfo = productDao.doGetProductCNBasePriceInfo(paramMap).get(0);
			priceInfo.setCnPriceFinalRmb(String.valueOf(Double.parseDouble(priceInfo.getCnPriceFinalRmb()) * Double.parseDouble(String.valueOf(paramMap.get("percent")))));
			priceInfo.setComment(String.valueOf(paramMap.get("comment")));
			priceInfos.add(priceInfo);
		}
		paramMap.put("contentList", priceInfos);
	}

	@Override
	public boolean doUpdateProductCNPriceSettingInfo(ProductPriceSettingBean productPriceSettingBean) throws Exception {
		ProductPriceSettingBean oldNoExtendBean = doGetProductCNPriceSettingInfo(productPriceSettingBean.getProductId().toString(),
				productPriceSettingBean.getChannelId(), false);
		ProductPriceSettingBean oldYesExtendBean = doGetProductCNPriceSettingInfo(productPriceSettingBean.getProductId().toString(),
				productPriceSettingBean.getChannelId(), true);

		List<String> tables = CommonUtils.compareBean(productPriceSettingBean, oldYesExtendBean, oldNoExtendBean);
		// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
		List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
		simpleTransactionCms.openTransaction();
		try {
			if (tables.contains("cms_bt_cn_product_price_setting")) {
				if (notNullTables.contains("cms_bt_cn_product_price_setting")) {
					if (productDao.doUpdateProductCNPriceSettingInfo(oldNoExtendBean) == 0) {
						productDao.doInsertProductCNPriceSettingInfo(oldNoExtendBean);
					}
				} else {
					productDao.doDelProductCNPriceSettingInfo(oldNoExtendBean);
				}

				// 重新计算价格
				// 查看里面是否有需要从父model继承的
				if(productPriceSettingBean.isExistNull()){
					// 重新取一下继承的数据
					productPriceSettingBean = doGetProductCNPriceSettingInfo(productPriceSettingBean.getProductId().toString(),
							productPriceSettingBean.getChannelId(), true);
				}
				
				commonServiceImpl.doUpdateProductCNPriceInfo(productPriceSettingBean.getProductId(), productPriceSettingBean.getChannelId(), productPriceSettingBean, productPriceSettingBean.getModifier());
				
				// 插入履历表中
				productDao.doInsertProductHistoryPriceSettingInfo(productPriceSettingBean);
			}
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		simpleTransactionCms.commit();
		return true;
	}

	@Override
	public boolean doUpdateCNProductInfo(ProductCNBaseProductInfo productCNBean) throws Exception {
		// 获取更新前的model信息
		ProductCNBaseProductInfo oldNoExtendBean = doGetCNProductInfo(productCNBean.getProductId().toString(), productCNBean.getChannelId().toString(), false)
				.getCnBaseProductInfo();
		ProductCNBaseProductInfo oldYesExtendBean = doGetCNProductInfo(productCNBean.getProductId().toString(), productCNBean.getChannelId().toString(), true)
				.getCnBaseProductInfo();
		simpleTransactionCms.openTransaction();
		try {
			// 检查这条记录有没有已经被其他用户修改了 比较修改时间
			if (productDao.doUpdateCnProduct(productCNBean) > 0) {
				List<String> tables = CommonUtils.compareBean(productCNBean, oldYesExtendBean, oldNoExtendBean);
				// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
				List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
				// 检查需要跟新那几张表
				for (String table : tables) {
					if (table.equals("cms_bt_cn_product_extend")) {
						if (notNullTables.contains(table)) {
							if (productDao.doUpdateCnProductExtend(oldNoExtendBean) == 0) {
								productDao.doInsertCnProductExtend(oldNoExtendBean);
							}
						} else {
							productDao.doDelCnProductExtend(oldNoExtendBean);
						}
					}
				}
				productDao.doUpdatePublishStatus(productCNBean, "cn", null);
				simpleTransactionCms.commit();
			} else {
				simpleTransactionCms.rollback();
				return false;
			}
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;
	}

	@Override
	public boolean doUpdateCNProductTmallInfo(ProductCNTMProductInfo TMProductInfo) throws Exception {
		ProductCNTMProductInfo oldNoExtendBean = doGetCNProductInfo(TMProductInfo.getProductId().toString(), TMProductInfo.getChannelId().toString(), false)
				.getTmProductInfo();
		ProductCNTMProductInfo oldYesExtendBean = doGetCNProductInfo(TMProductInfo.getProductId().toString(), TMProductInfo.getChannelId().toString(), true)
				.getTmProductInfo();

		List<String> tables = CommonUtils.compareBean(TMProductInfo, oldYesExtendBean, oldNoExtendBean);
		// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
		List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
		try {
			simpleTransactionCms.openTransaction();
			if (tables.size() > 0) {
				String table = tables.get(0);
				if (notNullTables.contains(table)) {
					if (productDao.doUpdateCnTMProductExtend(oldNoExtendBean) == 0) {
						productDao.doInsertCnTMProductExtend(oldNoExtendBean);
					}
				} else {
					productDao.doDelCnTMProductExtend(oldNoExtendBean);
				}
				productDao.doUpdatePublishStatus(TMProductInfo, "cn", 1);
			}
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;
	}

	@Override
	public boolean doUpdateCNProductJingDongInfo(ProductCNJDProductInfo JDProduct) throws Exception {
		ProductCNJDProductInfo oldNoExtendBean = doGetCNProductInfo(JDProduct.getProductId().toString(), JDProduct.getChannelId().toString(), false)
				.getJdProductInfo();
		ProductCNJDProductInfo oldYesExtendBean = doGetCNProductInfo(JDProduct.getProductId().toString(), JDProduct.getChannelId().toString(), true)
				.getJdProductInfo();

		List<String> tables = CommonUtils.compareBean(JDProduct, oldYesExtendBean, oldNoExtendBean);
		// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
		List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
		try {
			simpleTransactionCms.openTransaction();
			if (tables.size() > 0) {
				String table = tables.get(0);
				if (notNullTables.contains(table)) {
					if (productDao.doUpdateCnJDProductExtend(oldNoExtendBean) == 0) {
						productDao.doInsertCnJDProductExtend(oldNoExtendBean);
					}
				} else {
					productDao.doDelCnJDProductExtend(oldNoExtendBean);
				}
				productDao.doUpdatePublishStatus(JDProduct, "cn", 2);
			}
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;
	}

	@Override
	public boolean doUpdateUSProductInfo(ProductUSBean productUSBean) throws Exception {
		// 获取更新前的model信息
		ProductUSBean oldNoExtendBean = doGetUSProductInfo(productUSBean.getProductId().toString(), productUSBean.getChannelId().toString(), false);
		ProductUSBean oldYesExtendBean = doGetUSProductInfo(productUSBean.getProductId().toString(), productUSBean.getChannelId().toString(), true);
		simpleTransactionCms.openTransaction();
		try {
			// 检查这条记录有没有已经被其他用户修改了 比较修改时间
			if (productDao.doUpdateUsProduct(productUSBean) > 0) {
				List<String> tables = CommonUtils.compareBean(productUSBean, oldYesExtendBean, oldNoExtendBean);
				// 检索出那些表里是有数据 不存在该list的表是需要做删除操作的
				List<String> notNullTables = CommonUtils.searchNotNullTable(oldNoExtendBean);
				// 检查需要跟新那几张表
				for (String table : tables) {
					if (table.equals("cms_bt_product_extend")) {
						if (notNullTables.contains(table)) {
							if (productDao.doUpdateUsProductExtend(oldNoExtendBean) == 0) {
								productDao.doInsertUsProductExtend(oldNoExtendBean);
							}
						} else {
							productDao.doDelUsProductExtend(oldNoExtendBean);
						}
					} else if (table.equals("cms_bt_google_product_extend")) {
						if (notNullTables.contains(table)) {
							if (productDao.doUpdateUsGoogleProductExtend(oldNoExtendBean) == 0) {
								productDao.doInsertUsGoogleProductExtend(oldNoExtendBean);
							}
						} else {
							productDao.doDelUsGoogleProductExtend(oldNoExtendBean);
						}
					} else if (table.equals("cms_bt_pricegrabber_product_extend")) {
						if (notNullTables.contains(table)) {
							if (productDao.doUpdateUsPricegrabberProductExtend(oldNoExtendBean) == 0) {
								productDao.doInsertUsPricegrabberProductExtend(oldNoExtendBean);
							}
						} else {
							productDao.doDelUsPricegrabberProductExtend(oldNoExtendBean);
						}
					} else if (table.equals("cms_bt_amazon_product_extend")) {
						if (notNullTables.contains(table)) {
							if (productDao.doUpdateUsAmazonProductExtend(oldNoExtendBean) == 0) {
								productDao.doInsertUsAmazonProductExtend(oldNoExtendBean);
							}
						} else {
							productDao.doDelUsAmazonProductExtend(oldNoExtendBean);
						}
					}
				}
				productDao.doUpdatePublishStatus(productUSBean, "us", null);
				
				// NewArrival发生变化的场合
				if(oldYesExtendBean.getIsNewArrival() != productUSBean.getIsNewArrival()){
					// 是新品的场合 
					if(productUSBean.getIsNewArrival()){
						productDao.doInsertNewArrivals(productUSBean);
					}else{
						productDao.doDelNewArrivals(productUSBean);
					}
				}

				simpleTransactionCms.commit();
			} else {
				simpleTransactionCms.rollback();
				return false;
			}
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;
	}

	@Override
	public boolean doUpdateProductUSPriceInfo(ProductUSPriceInfo productUSPriceInfo, boolean msrp) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modifier", productUSPriceInfo.getModifier());
		data.put("productId", productUSPriceInfo.getProductId());
		data.put("channelId", productUSPriceInfo.getChannelId());
		data.put("cartId", productUSPriceInfo.getCartId());

		List<ProductUSPriceInfo> oldlist = doGetProductUSPriceInfo(productUSPriceInfo.getProductId(), productUSPriceInfo.getChannelId(),
				productUSPriceInfo.getCartId());

		ProductUSPriceInfo old = null;
		if (oldlist.size() > 0) {
			old = oldlist.get(0);
		}
		simpleTransactionCms.openTransaction();
		try {
			// msrp update
			if (msrp) {
				ProductUSBean productUSBean = new ProductUSBean();
				productUSBean.setProductId(productUSPriceInfo.getProductId());
				productUSBean.setChannelId(productUSPriceInfo.getChannelId());
				productUSBean.setMsrp(productUSPriceInfo.getMsrp());
				productUSBean.setModifier(productUSPriceInfo.getModifier());
				productDao.doUpdateUsProduct(productUSBean);
			}

			if (productDao.doUpdateProductUSPriceInfo(productUSPriceInfo) == 0) {
				productDao.doInsertProductUSPriceInfo(productUSPriceInfo);
			}

			if (old == null || !old.getPrice().equals(productUSPriceInfo.getPrice())) {
				data.put("price", productUSPriceInfo.getPrice());
				data.put("comment", productUSPriceInfo.getComment());
				productDao.doInsertHistoryProductPrice(data);
			}
			if (productUSPriceInfo.getIsOutletsOnSale()) {
				// TODO
			} else {
				// TODO
			}

			if (productUSPriceInfo.getIsApproved()) {
				data.put("isPublished", false);
				if (productDao.doUpdatePublishStatus(data) == 0) {
					data.put("cnGroupId", 1);
					data.put("modelId", productUSPriceInfo.getModelId());
					data.put("code", productUSPriceInfo.getCode());
					productDao.doInsertPublishStatus(data);
				}
			}
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;

	}

	@Override
	public boolean doUpdateProductCNPriceInfo(ProductCNPriceInfo productCNPriceInfo) throws Exception {

		if (StringUtils.isEmpty(productCNPriceInfo.getCnPriceDiscount())) {
			productCNPriceInfo.setCnPriceDiscount("0.0");
		}

		simpleTransactionCms.openTransaction();
		try {
			productCNPriceInfo.setEffectivePrice("0.0");
			if(productDao.doUpdateProductCNPriceInfo(productCNPriceInfo) == 0){
				// 如果没有设过中国最终价格的 就和中国定价一样
				if(StringUtils.isEmpty(productCNPriceInfo.getCnPriceFinalRmb()) || "0.00".equals(productCNPriceInfo.getCnPriceFinalRmb())){
					// cnFinalPrice算出
					productCNPriceInfo.setCnPriceFinalRmb(productCNPriceInfo.getCnPriceRmb());
				}
				productDao.doInsertProductCNPriceInfo(productCNPriceInfo);
				updateHistoryAndPublish(productCNPriceInfo);
				
				// 中国售价追加的场合 把中国所有的渠道添加价格
				if("79".equalsIgnoreCase(productCNPriceInfo.getCartId())){
					String cartId=productDao.doGetTypeValue(productCNPriceInfo.getChannelId(),"4");
					String cartIds[] = cartId.split(",");
					for (String cart : cartIds) {
						if(!"79".equalsIgnoreCase(cart)){
							productCNPriceInfo.setCartId(cart);
							productDao.doInsertProductCNPriceInfo(productCNPriceInfo);
							updateHistoryAndPublish(productCNPriceInfo);
						}
					}
				}
			}else{
				updateHistoryAndPublish(productCNPriceInfo);
			}

			
			
			simpleTransactionCms.commit();
		} catch (Exception e) {
			simpleTransactionCms.rollback();
			throw e;
		}
		return true;

	}
	
	private void updateHistoryAndPublish(ProductCNPriceInfo productCNPriceInfo) {
		
		List<ProductCNPriceInfo> oldlist = doGetProductCNPriceInfo(productCNPriceInfo.getProductId(), productCNPriceInfo.getChannelId(),
				productCNPriceInfo.getCartId());

		ProductCNPriceInfo old = null;
		if (oldlist.size() > 0) {
			old = oldlist.get(0);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("modifier", productCNPriceInfo.getModifier());
		data.put("productId", productCNPriceInfo.getProductId());
		data.put("channelId", productCNPriceInfo.getChannelId());
		data.put("cartId", productCNPriceInfo.getCartId());
		data.put("cnGroupId", productCNPriceInfo.getCnGroupId());
		data.put("modelId", productCNPriceInfo.getModelId());
		if (old == null || !old.getCnPrice().equals(productCNPriceInfo.getCnPrice())
				|| !old.getCnPriceFinalRmb().equals(productCNPriceInfo.getCnPriceFinalRmb())) {
			data.put("price", productCNPriceInfo.getCnPrice());
			data.put("cnPriceRmb", productCNPriceInfo.getCnPriceRmb());
			data.put("cnPriceAdjustmentRmb", productCNPriceInfo.getCnPriceAdjustmentRmb());
			data.put("cnPriceFinalRmb", productCNPriceInfo.getCnPriceFinalRmb());
			data.put("effectivePrice", productCNPriceInfo.getEffectivePrice());
			data.put("comment", productCNPriceInfo.getComment());
			productDao.doInsertHistoryProductPrice(data);
		}

		if (productCNPriceInfo.getIsApproved()) {
			data.put("isPublished", false);
			if (productDao.doUpdatePublishStatus(data) == 0) {
				data.put("code", productCNPriceInfo.getCode());
				productDao.doInsertPublishStatus(data);
			}
		}
	}

	@Override
	public void doUpdateMainCategory(Map<String, Object> data) throws Exception {

		String productlId = (String) data.get("id");
		String channelId = (String) data.get("channelId");
		String mainCategoryId = (String) data.get("mainCategoryId");
		String tmCategoryId = (String) data.get("tmCategoryId");
		String jdCategoryId = (String) data.get("jdCategoryId");

		ProductCNBean productCNBean = doGetCNProductInfo(productlId, channelId, true);
		productCNBean.getCnBaseProductInfo().setMainCategoryId(mainCategoryId);
		productCNBean.getCnBaseProductInfo().setModifier((String)data.get("modifier"));
		
		productCNBean.getJdProductInfo().setJdCategoryId(jdCategoryId);
		productCNBean.getJdProductInfo().setModifier((String)data.get("modifier"));
		
		productCNBean.getTmProductInfo().setTmCategoryId(tmCategoryId);
		productCNBean.getTmProductInfo().setModifier((String)data.get("modifier"));

		doUpdateCNProductInfo(productCNBean.getCnBaseProductInfo());
		doUpdateCNProductJingDongInfo(productCNBean.getJdProductInfo());
		doUpdateCNProductTmallInfo(productCNBean.getTmProductInfo());

	}

	@Override
	public List<Map<String, Object>> doGetCustomInfo(Map<String, Object> requestMap) {
		return productDao.doGetCustomInfo(requestMap);
	}

	@Transactional("transactionManagerCms")
	@Override
	public boolean doUpdateProductImg(Map<String, Object> requestMap) throws Exception {
		FtpUtil ftpUtil = new FtpUtil();
		FTPClient ftpClient = new FTPClient();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		try {
			
			
			List<Map<String, Object>> delImgList = (List<Map<String, Object>>) requestMap.get("delImgList");
			for (Map<String, Object> productImage : delImgList) {
				productDao.doDelProductImg(productImage);
			}

			List<Map<String, Object>> addImgList = (List<Map<String, Object>>) requestMap.get("addImgList");
			if(addImgList != null && addImgList.size() > 0){
				FtpBean ftpBean;
				ftpBean = formatFtpBean(requestMap.get("channelId").toString());
				// 建立连接
				ftpClient = ftpUtil.linkFtp(ftpBean);
	
				String dirname = Properties.readValue("temp.img.path");
				
				for (Map<String, Object> item : addImgList) {
					java.util.Date date = new java.util.Date();
					String imageName = requestMap.get("productId").toString() + "-" + sdf.format(date) + "-" + item.get("filename").toString();
					if (imageName.lastIndexOf(".jpg") > -1) {
	
						// 把图片保存到本地临时目录
						CommonUtils.uploadImage(dirname, imageName, item.get("fileBase64").toString());
						// 再上传到ftp服务器上
						ftpBean.setUpload_filename(imageName);
						logger.info("ftp上传 :"+imageName);
						boolean isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);
						
						
						if(!isSuccess){
							throw new Exception("FTP上传失败");
						}
						logger.info("ftp上传成功 :"+imageName);
						// 上传完毕删除临时文件
						CommonUtils.deleteFile(dirname+imageName);
						

						// 更新数据库
						imageName = imageName.substring(0, imageName.lastIndexOf(".jpg"));
						Map<String,Object> data = new HashMap<String, Object>();
						data.put("imageTypeId", item.get("imageTypeId"));
						data.put("productId", requestMap.get("productId"));
						data.put("channelId", requestMap.get("channelId"));
						data.put("imageName", imageName);
						data.put("modifier", requestMap.get("modifier"));
	
						productDao.doUpdateProductImg(data);
					}
				}
			}
			if((addImgList != null && addImgList.size() > 0) || (delImgList  != null && delImgList.size() > 0)){
				productDao.doUpdateProductImgCnt(requestMap);
				// 更新PublishStatus
				productDao.doUpdatePublishStatus(requestMap);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// 断开连接
			if (ftpClient != null) {
				ftpUtil.disconnectFtp(ftpClient);
			}
		}
		return true;
	}
	
	/*@Transactional("transactionManagerCms")
	@Override
	public void doDelProductImg(ProductImage requestMap) {
		int count = productDao.doDelProductImg(requestMap);
		if(count > 0){
			Map<String,Object> par = new HashMap<String, Object>();
			par.put("productId", requestMap.getProductId());
			par.put("channelId", requestMap.getChannelId());
			par.put("modifier", requestMap.getModifier());
			// 更新Product信息中的图片数
			productDao.doUpdateProductImgCnt(par);
			// 更新PublishStatus
			productDao.doUpdatePublishStatus(par);
		}
	}*/
	
    private FtpBean formatFtpBean(String channelId){
        FtpBean ftpBean = new FtpBean();
        // ftp连接port
        ftpBean.setPort("21");
        // ftp连接url
        ftpBean.setUrl(Properties.readValue("FTP_S7_Url"));
        // ftp连接usernmae
        ftpBean.setUsername(Properties.readValue("FTP_S7_UserName"));
        // ftp连接password
        ftpBean.setPassword(Properties.readValue("FTP_S7_Password"));
        
        ftpBean.setUpload_localpath(Properties.readValue("temp.img.path"));
        //FTP服务器保存目录设定
        List<Map> master = commonDao.doMasterValue(channelId,"13");
        ftpBean.setUpload_path("/"+master.get(0).get("value").toString()+"/");
        
        ftpBean.setFile_coding("utf-8");
        return ftpBean;
    }


}
