package com.voyageone.cms.service;


import com.voyageone.cms.formbean.*;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.common.Constants;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public interface ProductEditService {

	public ProductUSBean doGetUSProductInfo(String productId, String channelId, boolean isExtend) throws Exception;
	
	public ProductCNBean doGetCNProductInfo(String productId, String channelId, boolean isExtend) throws Exception;
	
	public ProductPriceSettingBean doGetProductCNPriceSettingInfo(String productId, String channelId, boolean isExtend) throws Exception;
	
	public boolean doUpdateProductCNPriceSettingInfo(ProductPriceSettingBean productPriceSettingBean) throws Exception;
	
	public boolean doUpdateCNProductInfo(ProductCNBaseProductInfo productCNBean) throws Exception;
	
	public boolean doUpdateUSProductInfo(ProductUSBean productUSBean) throws Exception;
	
	public boolean doUpdateProductUSPriceInfo(ProductUSPriceInfo productUSPriceInfo, boolean msrp) throws Exception;
	
	public boolean doUpdateProductCNPriceInfo(ProductCNPriceInfo productCNPriceInfo) throws Exception;

//	public ProductCNCartPriceInfo doGetProductCNCartPriceInfo(String productId,String channelId);

	public List<ProductCNPriceInfo> doGetProductCNPriceInfo(String productId,String channelId, String cartId);

//	public ProductUSAmazonPriceInfo doGetProductUSAmazonPriceInfo(String productId,String channelId);

	public List<ProductUSPriceInfo> doGetProductUSPriceInfo(String productId, String channelId, String cartId);

	public boolean doUpdateCNProductJingDongInfo(ProductCNJDProductInfo JDProduct) throws Exception;

	public boolean doUpdateCNProductTmallInfo(ProductCNTMProductInfo TMProductInfo) throws Exception;

	public List<ProductImage> doGetProductImage(String productId, String channelId);

	public void doUpdateMainCategory(Map<String, Object> data) throws Exception;

	public List<Map<String, Object>> doGetInventory(String channelId, String code);

	public boolean doSetCnProductProperty(Map<String, Object> paramMap);

	public boolean doSetCnProductShare(Map<String, Object> paramMap, UserSessionBean user);

	DtResponse<List<RelationPromotionProduct>> doGetPromotionHistory(DtRequest<ProductUSPriceInfo> dtRequest);

	public List<Map<String, Object>> doGetCustomInfo(Map<String, Object> requestMap);

	DtResponse<List<ProductUSPriceInfo>> doGetPirceHistory(DtRequest<ProductUSPriceInfo> dtRequest);

	public boolean doUpdateProductImg(Map<String, Object> requestMap) throws Exception;

//	public void doDelProductImg(ProductImage requestMap);
}
