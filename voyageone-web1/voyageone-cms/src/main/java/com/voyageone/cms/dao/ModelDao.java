package com.voyageone.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.formbean.*;
import com.voyageone.common.Constants;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ModelDao extends CmsBaseDao{

	public ModelUSBean doGetUSModelInfo(Map<String, Object> data){
		ModelUSBean ret = null;
		List<ModelUSBean> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_us_model_info", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	
	public ModelCNBean doGetCNModelInfo(Map<String, Object> data){
		ModelCNBean ret = null;
		List<ModelCNBean> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_cn_model_info", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	
	public ModelPriceSettingBean doGetModelCNPriceSettingInfo(Map<String, Object> data){
		ModelPriceSettingBean ret = null;
		List<ModelPriceSettingBean> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_model_price_setting", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}
	public int doUpdateModelCNPriceSettingInfo(ModelPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_model_price_setting", data);
		return ret;
	}
	public int doInsertModelCNPriceSettingInfo(ModelPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_model_price_setting", data);
		return ret;
	}
	public int doDelModelCNPriceSettingInfo(ModelPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_model_price_setting", data);
		return ret;
	}
	public int doInsertModelHistoryPriceSettingInfo(ModelPriceSettingBean data){
		int ret=updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_model_cms_bt_history_price_setting", data);
		return ret;
	}
	public List<Map<String, Object>> doGetModelRelation(Map<String, Object> data){
		List<Map<String, Object>> ret = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_relation_category_model", data);
		if(ret == null){
			ret = new ArrayList<Map<String,Object>>();
		}
		return ret;
	}
	
	public List<ModelProductUSBean> doGetUSProductList(Map<String, Object> data){
		List<ModelProductUSBean> ret = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_USModelProductList", data);
		if(ret == null){
			ret = new ArrayList<ModelProductUSBean>();
		}
		return ret;
	}
	public Integer doGetUSProductListCount(Map<String, Object> data){
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_USModelProductList_count", data);
	}
	
	public List<ModelProductCNBean> doGetCNProductList(Map<String, Object> data){
		List<ModelProductCNBean> ret = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNModelProductList", data);
		if(ret == null){
			ret = new ArrayList<ModelProductCNBean>();
		}
		return ret;
	}
	
	public Integer doGetCNProductListCount(Map<String, Object> data){
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_CNModelProductList_count", data);
	}
	
	public int doUpdateUSModelPrimaryCategory(Map<String, Object> data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_relation_category_model",data);
	}
	
	// 更新cms_bt_cn_model
	public int doUpdateCnModel(ModelCNBaseBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_model",data);
	}
	// 更新cms_bt_cn_model
	public int doUpdateCnModelExtend(ModelCNBaseBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_model_extend",data);
	}
	// 更新cms_bt_cn_model
	public int doUpdateCnJDModelExtend(ModelJDBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_jd_model_extend",data);
	}
	// 更新cms_bt_cn_model
	public int doUpdateCnTMModelExtend(ModelTMBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_cn_tm_model_extend",data);
	}
	
	// 更新cms_bt_model
	public int doUpdateUsModel(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_model",data);
	}
	// 更新cms_bt_model_extend
	public int doUpdateUsModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_model_extend",data);
	}
	// 更新cms_bt_google_model_extend
	public int doUpdateUsGoogleModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_google_model_extend",data);
	}
	// 更新cms_bt_pricegrabber_model_extend
	public int doUpdateUsPricegrabberModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_pricegrabber_model_extend",data);
	}
	// 更新cms_bt_amazon_model_extend
	public int doUpdateUsAmazonModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_amazon_model_extend",data);
	}
	
	
	// 插入cms_bt_cn_model
	public int doInsertCnModel(ModelCNBaseBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_model",data);
	}
	// 插入cms_bt_cn_model_extend
	public int doInsertCnModelExtend(ModelCNBaseBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_model_extend",data);
	}
	// 插入cms_bt_cn_jd_model_extend
	public int doInsertCnJDModelExtend(ModelJDBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_jd_model_extend",data);
	}
	// 插入cms_bt_cn_tm_model_extend
	public int doInsertCnTMModelExtend(ModelTMBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_cn_tm_model_extend",data);
	}
	
	// 插入cms_bt_model
	public int doInsertUsModel(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_model",data);
	}
	// 插入cms_bt_model_extend
	public int doInsertUsModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_model_extend",data);
	}
	// 插入cms_bt_google_model_extend
	public int doInsertUsGoogleModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_google_model_extend",data);
	}
	// 插入cms_bt_pricegrabber_model_extend
	public int doInsertUsPricegrabberModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_pricegrabber_model_extend",data);
	}
	// 插入cms_bt_amazon_model_extend
	public int doInsertUsAmazonModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "insert_cms_bt_amazon_model_extend",data);
	}
	
	// 删除cms_bt_cn_model_extend
	public int doDelCnModelExtend(ModelCNBaseBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_model_extend",data);
	}
	// 删除cms_bt_cn_jd_model_extend
	public int doDelCnJDModelExtend(ModelJDBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_jd_model_extend",data);
	}
	// 删除cms_bt_cn_tm_model_extend
	public int doDelCnTMModelExtend(ModelTMBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_cn_tm_model_extend",data);
	}
	
	// 删除cms_bt_model_extend
	public int doDelUsModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_model_extend",data);
	}
	// 删除cms_bt_google_model_extend
	public int doDelUsGoogleModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_google_model_extend",data);
	}
	// 删除cms_bt_pricegrabber_model_extend
	public int doDelUsPricegrabberModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_pricegrabber_model_extend",data);
	}
	// 删除cms_bt_amazon_model_extend
	public int doDelUsAmazonModelExtend(ModelUSBean data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_amazon_model_extend",data);
	}
	
	public int doUpdatePublishStatus(ModelBaseBean model,String cart,Integer platformId){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("modifier", model.getModifier());
		data.put("modelId", model.getModelId());
		data.put("channelId", model.getChannelId());
		data.put("platformId", platformId);
		data.put("cart", cart);
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "update_cms_bt_publish_product_status",data);
	}
	
	public int doUpdateRemoveCategoryModel(Map<String,Object> data){
		return updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "delete_cms_bt_relation_category_model",data);
	}
	public int getModelProductCount (Map<String, Object> data) {
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_modelproductcount", data);
		
	}
	public List<ProductPriceSettingBean>getModelProductPriceSetting(String modelId, String channelId){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("modelId", modelId);
		data.put("channelId", channelId);
		List<ProductPriceSettingBean> ret = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "get_model_product_price_setting", data);
		return ret;
	}

	/**
	 * @description 根据给的model找出所有的category（包含父category）
	 * @param requestMap 参数Map
	 * @return List
	 */
	public List<Map<String, Object>> doSearchCategoryByModel(Map<String, Object> requestMap) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "search_model", requestMap);
	}

	/**
	 * @description 解除product和model关系；解除product和category关系
	 * @param requestMap 参数Map
	 * @return boolean 是否删除成功
	 */
	public boolean doDeleteRelation(Map<String, Object> requestMap) {
		updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_movetomodel_delete_pm_relation", requestMap);
		updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_movetomodel_delete_pc_relation", requestMap);
		return true;
	}

	/**
	 * @description 建立product和model;建立product和requestMap的关系
	 * @param requestMap 参数map
	 * @return 是否插入成功
	 */
	public boolean doCreateRelation(Map<String, Object> requestMap) {
		int insPMCount = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_movetomodel_create_pm_relation", requestMap);
		int insPCCount = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_movetomodel_create_pc_relation", requestMap);
		return insPMCount > 0 && insPCCount > 0;
	}

}
