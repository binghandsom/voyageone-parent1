package com.voyageone.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.modelbean.MasterProperty;
import com.voyageone.cms.modelbean.PropertyOption;
import com.voyageone.cms.modelbean.PropertyRule;
import com.voyageone.cms.modelbean.PropertyValue;
import com.voyageone.common.Constants;
@Repository
public class MasterPropValueSettingDao extends BaseDao{
    

	public List<MasterProperty> getMasterProperties(int categoryId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("category_id", categoryId);

		return super.selectList(Constants.DAO_NAME_SPACE_CMS +"get_master_properties", parmMap);
	}

	public List<PropertyOption> getPropertyOptions(int categoryId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("category_id", categoryId);

		return super.selectList(Constants.DAO_NAME_SPACE_CMS +"get_property_options", parmMap);
	}

	public List<PropertyRule> getPropertyRules(int categoryId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("category_id", categoryId);

		return super.selectList(Constants.DAO_NAME_SPACE_CMS +"get_property_rules", parmMap);
	}

	public int save(List<PropertyValue> values) {

		Map<String, List<PropertyValue>> insertDataMap = new HashMap<String, List<PropertyValue>>();
		insertDataMap.put("list", values);

		int inCount = super.updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS +"insert_property_values", insertDataMap);

		return inCount;
	}

	public List<PropertyValue> getUpdatePropertyValues(String channel_id, int level, String level_value) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channel_id);
		parmMap.put("level", level);
		parmMap.put("level_value", level_value);

		return super.selectList(Constants.DAO_NAME_SPACE_CMS +"get_master_property_values", parmMap);
	}

	public int delete(String channel_id, int level, String level_value) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channel_id);
		parmMap.put("level", level);
		parmMap.put("level_value", level_value);

		return super.delete(Constants.DAO_NAME_SPACE_CMS +"delete_property_values", parmMap);
	}

	public int getPropValueCount(String channel_id, int level, String level_value) {

		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channel_id);
		parmMap.put("level", level);
		parmMap.put("level_value", level_value);

		return super.selectOne(Constants.DAO_NAME_SPACE_CMS +"get_property_values_count", parmMap);
	}
	
//	public List<MasterCategoryPropertyModel> getMasterCategoryProps(int categoryId,String channelId, int level, String levelValue) {
//		Map<String, Object> parmMap = new HashMap<String, Object>();
//		
//		parmMap.put("channelId", channelId);
//		parmMap.put("level", level);
//		parmMap.put("levelValue", levelValue);
//		parmMap.put("categoryId", categoryId);
//
//		return super.selectList(Constants.DAO_NAME_SPACE_CMS +"get_propertiesAndVaues", parmMap);
//	}
	
	public boolean updateDealFlag(String channelId, int level, String levelValue,String user) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("chanelId", channelId);
		parmMap.put("user", user);
		int updateCount=0;
		if (level==2) {
			parmMap.put("modelId", levelValue);
			updateCount = super.update(Constants.DAO_NAME_SPACE_CMS +"updateDealFlagByModelId", parmMap);
		}else if (level==3){
			parmMap.put("productId", levelValue);
			updateCount = super.update(Constants.DAO_NAME_SPACE_CMS +"updateDealFlagByProductId", parmMap);
		}
		
		if (updateCount>0) {
			return true;
		}
		
		return false;
	}

	/**
	 * 获取属性错误信息.
	 * @param channelId
	 * @param levelValue
	 * @return
	 */
	public List<String> getErrMsgs(String channelId,String levelValue){

		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channelId", channelId);
		parmMap.put("modelId", levelValue);

		List<String> msgs =  super.selectList(Constants.DAO_NAME_SPACE_CMS + "getErrorMessages", parmMap);

		return msgs;
	}

}
