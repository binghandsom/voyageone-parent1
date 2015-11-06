package com.voyageone.cms.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.formbean.CategoryBean;
import com.voyageone.common.Constants;
@Repository
public class SearchDao extends CmsBaseDao {


   public List<Map<String ,Object>> doSearch(Map<String, Object> data){
	   List<Map<String ,Object>> ret = new ArrayList<Map<String,Object>>();
	   int type = (Integer)data.get("type");
	   if(type == CmsConstants.IDType.TYPE_CATEGORY){
		   ret = selectList(Constants.DAO_NAME_SPACE_CMS + "search_category",data);
	   }
	   if(type == CmsConstants.IDType.TYPE_MODEL ){
		   ret = selectList(Constants.DAO_NAME_SPACE_CMS + "search_model",data);
	   }
	   if(type == CmsConstants.IDType.TYPE_PRODUCT){
		   ret = selectList(Constants.DAO_NAME_SPACE_CMS + "search_product",data);
	   }
	   return ret;
   }
   
   public Integer doSearchCnt(Map<String, Object> data){
	   Integer ret = null;
	   int type = (Integer)data.get("type");
	   if(type == CmsConstants.IDType.TYPE_CATEGORY || type == CmsConstants.IDType.TYPE_ALL){
		   ret = selectOne(Constants.DAO_NAME_SPACE_CMS + "search_category_count", data);
	   }
	   if(type == CmsConstants.IDType.TYPE_MODEL || type == CmsConstants.IDType.TYPE_ALL){
		   ret = selectOne(Constants.DAO_NAME_SPACE_CMS + "search_model_count", data);
	   }
	   if(type == CmsConstants.IDType.TYPE_PRODUCT || type == CmsConstants.IDType.TYPE_ALL){
		   ret = selectOne(Constants.DAO_NAME_SPACE_CMS + "search_product_count", data);
	   }
	   return ret;
   }
	public List<CategoryBean> getAllParentCategory(String channelId) {
		List<CategoryBean> categoryList = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_all_category", channelId);
		for (CategoryBean each : categoryList) {
			for (CategoryBean inner : categoryList) {
				if (each.getCategoryId() == inner.getParentCategoryId()) {
					inner.setParent(each);
				}
			}
		}
		return categoryList;
	}
	
	public List<Map<String ,Object>> doAdvanceSearch(Map<String ,Object> data){
		// 发布状态是0：待发布
		if(	"0".equals(data.get("publishStatus"))){
			data.put("isPublished","0");
			// 发布状态是3：待更新
		}else if("3".equals(data.get("publishStatus"))){
			data.put("isPublished","1");
			data.put("publishStatus","0");
		}
		return selectList(Constants.DAO_NAME_SPACE_CMS + "advance_search", data);
	}
	public Integer doAdvanceSearchCnt(Map<String ,Object> data){
		// 发布状态是0：待发布
		if(	"0".equals(data.get("publishStatus"))){
			data.put("isPublished","0");
		// 发布状态是3：待更新
		}else if("3".equals(data.get("publishStatus"))){
			data.put("isPublished","1");
			data.put("publishStatus","0");
		}
		return (Integer)(selectOne(Constants.DAO_NAME_SPACE_CMS + "advance_search_count", data));
	}

	public Integer doCategoryUnMappingCnt(String channelId){
		return (Integer)(selectOne(Constants.DAO_NAME_SPACE_CMS + "category_unmapping_count", channelId));
	}
	public Integer doCategoryPropertyUnMappingCnt(String channelId){
		return (Integer)(selectOne(Constants.DAO_NAME_SPACE_CMS + "category_property_unmapping_count", channelId));
	}
}
