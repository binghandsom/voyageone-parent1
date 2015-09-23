package com.voyageone.cms.dao;

import com.voyageone.cms.formbean.*;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDao extends CmsBaseDao{

	public ProductUSBean doGetUSProductInfo(Map<String, Object> data){
		ProductUSBean ret = new ProductUSBean();
		List<ProductUSBean> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_USProductInfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	
	public ProductCNBean doGetCNProductInfo(Map<String, Object> data){
		ProductCNBean ret = new ProductCNBean();
		List<ProductCNBean> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNProductInfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	
	public ProductPriceSettingBean doGetProductCNPriceSettingInfo(Map<String, Object> data){
		ProductPriceSettingBean ret = new ProductPriceSettingBean();
		List<ProductPriceSettingBean> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNProductPriceSettingInfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	public List<ProductUSPriceInfo> doGetProductUSPriceInfo (Map<String, Object> data) {
		ProductUSPriceInfo ret = null;
		List<ProductUSPriceInfo> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_USProductOfficalPriceInfo", data);
		return info;
	}
	public ProductUSAmazonPriceInfo doGetProductUSAmazonPriceInfo (Map<String, Object> data) {
		ProductUSAmazonPriceInfo ret = null;
		List<ProductUSAmazonPriceInfo> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_USProductAmzonePriceInfo", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	public List<ProductCNPriceInfo> doGetProductCNBasePriceInfo (Map<String, Object> data) {
		List<ProductCNPriceInfo> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNProductBasePriceInfo", data);
		return info;
	}
	public ProductCNCartPriceInfo doGetProductCNCartPriceInfo (Map<String, Object> data) {
		ProductCNCartPriceInfo ret = null;
		List<ProductCNCartPriceInfo> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "CNProductCartPricetMap", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	public List<ProductImage> doGetProductImage (Map<String, Object> data) {
		
		List<ProductImage> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_ProductImage", data);
		return info;
	}
	public List<Map<String, Object>> doGetInventory(Map<String, Object> data) {
		List<Map<String, Object>> result = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_inventory", data);
		
		return result;
	}
	public int doUpdateProductCNPriceSettingInfo(ProductPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_product_price_setting", data);
		return ret;
	}
	public int doInsertProductCNPriceSettingInfo(ProductPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_product_price_setting", data);
		return ret;
	}
	public int doDelProductCNPriceSettingInfo(ProductPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_product_price_setting", data);
		return ret;
	}
	public int doInsertProductHistoryPriceSettingInfo(ProductPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_product_cms_bt_history_price_setting", data);
		return ret;
	}
	public List<Map<String, Object>> doGetProductRelation(Map<String, Object> data){
		List<Map<String, Object>> ret = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_relation_category_model", data);
		if(ret == null){
			ret = new ArrayList<Map<String,Object>>();
		}
		return ret;
	}
	
	// 更新cms_bt_cn_product
	public int doUpdateCnProduct(ProductCNBaseProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_product",data);
	}
	// 更新cms_bt_cn_product
	public int doUpdateCnProductExtend(ProductCNBaseProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_product_extend",data);
	}
	// 更新cms_bt_cn_product
	public int doUpdateCnJDProductExtend(ProductCNJDProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_jd_product_extend",data);
	}
	// 更新cms_bt_cn_product
	public int doUpdateCnTMProductExtend(ProductCNTMProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_tm_product_extend",data);
	}
	
	// 更新cms_bt_product
	public int doUpdateUsProduct(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_product",data);
	}
	// 更新cms_bt_product_extend
	public int doUpdateUsProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_product_extend",data);
	}
	// 更新cms_bt_google_product_extend
	public int doUpdateUsGoogleProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_google_product_extend",data);
	}
	// 更新cms_bt_pricegrabber_product_extend
	public int doUpdateUsPricegrabberProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_pricegrabber_product_extend",data);
	}
	// 更新cms_bt_amazon_product_extend
	public int doUpdateUsAmazonProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_amazon_product_extend",data);
	}
	
	
	// 插入cms_bt_cn_product
	public int doInsertCnProduct(ProductCNBaseProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_product",data);
	}
	// 插入cms_bt_cn_product_extend
	public int doInsertCnProductExtend(ProductCNBaseProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_product_extend",data);
	}
	// 插入cms_bt_cn_jd_product_extend
	public int doInsertCnJDProductExtend(ProductCNJDProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_jd_product_extend",data);
	}
	// 插入cms_bt_cn_tm_product_extend
	public int doInsertCnTMProductExtend(ProductCNTMProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_tm_product_extend",data);
	}
	
	// 插入cms_bt_product
	public int doInsertUsProduct(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_product",data);
	}
	// 插入cms_bt_product_extend
	public int doInsertUsProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_product_extend",data);
	}
	// 插入cms_bt_google_product_extend
	public int doInsertUsGoogleProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_google_product_extend",data);
	}
	// 插入cms_bt_pricegrabber_product_extend
	public int doInsertUsPricegrabberProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_pricegrabber_product_extend",data);
	}
	// 插入cms_bt_amazon_product_extend
	public int doInsertUsAmazonProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_amazon_product_extend",data);
	}
	
	// 删除cms_bt_cn_product_extend
	public int doDelCnProductExtend(ProductCNBaseProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_product_extend",data);
	}
	// 删除cms_bt_cn_jd_product_extend
	public int doDelCnJDProductExtend(ProductCNJDProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_jd_product_extend",data);
	}
	// 删除cms_bt_cn_tm_product_extend
	public int doDelCnTMProductExtend(ProductCNTMProductInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_tm_product_extend",data);
	}
	
	// 删除cms_bt_product_extend
	public int doDelUsProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_product_extend",data);
	}
	// 删除cms_bt_google_product_extend
	public int doDelUsGoogleProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_google_product_extend",data);
	}
	// 删除cms_bt_pricegrabber_product_extend
	public int doDelUsPricegrabberProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_pricegrabber_product_extend",data);
	}
	// 删除cms_bt_amazon_product_extend
	public int doDelUsAmazonProductExtend(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_amazon_product_extend",data);
	}
	
	/**
	 * 更新PublishStatus
	 * @param product
	 * @param cart cn/us
	 * @param platformId 1：阿里 2：京东 null： 全部
	 * @return
	 */
	public int doUpdatePublishStatus(ProductBaseBean product,String cart,Integer platformId){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("modifier", product.getModifier());
		data.put("productId", product.getProductId());
		data.put("channelId", product.getChannelId());
		data.put("platformId", platformId);
		data.put("cart", cart);
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_publish_product_status",data);
	}
	public int doUpdatePublishStatus(Map<String,Object> data){
			int i= updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_publish_product_status",data);
		return i;
	}
	
	public int doInsertPublishStatus(Map<String,Object> data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_publish_product_status",data);
	}
	public int doUpdateProductUSPriceInfo(ProductUSPriceInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_product_share",data);
	}
	public int doInsertProductUSPriceInfo(ProductUSPriceInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_product_share",data);
	}
	public int doUpdateProductCNPriceInfo(ProductCNPriceInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_product_share",data);
	}
	public int doInsertProductCNPriceInfo(ProductCNPriceInfo data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_product_share",data);
	}	
	public int doInsertHistoryProductPrice(Map<String,Object> data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_history_product_price",data);
	}
	public int doInsertNewArrivals(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_new_arrivals",data);
	}
	public int doDelNewArrivals(ProductUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_new_arrivals",data);
	}
	
	public String doGetTypeValue(String channelId, String typeId){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("channelId",channelId);
		data.put("typeId", typeId);
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_mt_type_value", data);
	}

	public List<Map<String, Object>> doGetCustomInfo(Map<String, Object> paramMap){
		List<Map<String, Object>> ret;
		ret = selectList(Constants.DAO_NAME_SPACE_CMS + "get_custom_info", paramMap);
		if(ret == null){
			ret = new ArrayList<Map<String,Object>>();
		}
		return ret;
	}
	public int doUpdtProductEffective(Map<String, Object> paramMap) {
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_product_effective", paramMap);
	}

	public int doUpdtProductHsCode(Map<String, Object> paramMap) {
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_product_extend_hscode", paramMap);
	}

	public int doInstHisProductPrice(Map<String, Object> paramMap) {
		return updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_product_his_price", paramMap);
	}

	public int doUpdtProductShare(Map<String, Object> paramMap) {
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_product_share_batch", paramMap);
	}

	public List<RelationPromotionProduct> doGetPromotionHistory(Map<String, Object> paramMap) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "select_product_promotion_history", paramMap);
	}

	public List<ProductUSPriceInfo> doGetPriceHistory(HashMap<String, Object> params) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "select_product_price_history", params);
	}

	public int doGetPriceHistoryCount(HashMap<String, Object> params) {
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "select_product_price_history_count", params);
	}

	public int doGetPromotionHistoryCount(HashMap<String, Object> params) {
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "select_product_promotion_history_count", params);
	}
	
	public int doUpdateProductImg(Map<String, Object> params){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_product_image", params);
	}
	
	public int doUpdateProductImgCnt(Map<String, Object> params){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_product_image_item_count", params);
	}

	public int doUpdtProductIsApprovedDes(Map<String, Object> paramMap) {
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_product_isappproveddes", paramMap);
	}

	public int doInsertImsBtProduct(Map<String, Object> paramMap) {
		return updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "insert_ims_bt_product", paramMap);
	}

	public int doUpdateImsBtProduct(Map<String, Object> paramMap) {
		return updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "update_Ims_Bt_Product", paramMap);
	}

	public List<ProductCNBaseProductInfo> getProductInfoById(Map<String, Object> paramMap) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "select_product_info_by_id", paramMap);
	}
	public int doDelProductImg(Map<String, Object> params){
		return updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "del_cms_bt_product_image_item", params);
	}
	
	
}
