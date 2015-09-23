package com.voyageone.cms.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.common.Constants;
@Repository
public class CommonDao extends CmsBaseDao {

	public List<CategoryBean> getCategoryMenuList(String channelId) {
//		List<CategoryBean> categoryList = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_all_category", channelId);
//		List<CategoryBean> ret = new ArrayList<CategoryBean>();
//		for (CategoryBean each : categoryList) {
//			if (each.getParentCategoryId() == 2) {
//				ret.add(each);
//			}
//			for (CategoryBean inner : categoryList) {
//				if (each.getId() == inner.getParentCategoryId()) {
//					each.getChildren().add(inner);
//				}
//			}
//		}
//		return ret;
		return getSubCategoryMenuList(channelId, "2");
	}
	
	public List<CategoryBean> getSubCategoryMenuList(String channelId, String categoryId ) {
		List<CategoryBean> categoryList = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_all_category", channelId);
		List<CategoryBean> ret = new ArrayList<CategoryBean>();
		for (CategoryBean each : categoryList) {
			if (each.getParentCategoryId() == Integer.parseInt(categoryId)) {
				ret.add(each);
			}
			for (CategoryBean inner : categoryList) {
				if (each.getId() == inner.getParentCategoryId()) {
					each.getChildren().add(inner);
				}
			}
		}
		return ret;
	}
	
   public List<Map<String,String>> doCategoryList(String channelId){
		List<Map<String,String>> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "categoryList", channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map<String,String>>();
		}
		return info;
	}	  
   public List<Map> doAmazonCategoryList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_amazon_category",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doGoogleCategoryList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_google_attribute",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doPriceGrabberCategoryList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_pricegrabber_category",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doHsCodeShList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_hs_code_sh",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doHsCodeGzList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_hs_code_gz",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doPercentList(String channelId,Integer attributeId){
	   	Map<String,Object>data = new HashMap<String, Object>();
	   	data.put("channelId", channelId);
	   	data.put("attributeId", attributeId);
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_attribute_value", data);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doProductTypeList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_product_type",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}	
   public List<Map> doBrandList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_brand",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}
   public List<Map> doSizeTypeList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_size_type",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}
   public List<Map> doColorMapList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_color_map",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}
   public List<Map> doCountryList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_countries",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}
   public List<Map> doMaterialFabricList(String channelId){
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_material_fabric",channelId);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}
   public List<Map> doMasterValue(String channelId){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("channelId", channelId);
		List<Map> info=(List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_value",data);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}

	public List<Map> doMasterValue(String channelId, String typeId) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("channelId", channelId);
		data.put("typeId", typeId);
		List<Map> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_value", data);
		if (info == null || info.size() == 0) {
			info = new ArrayList<Map>();
		}
		return info;
	}
}
