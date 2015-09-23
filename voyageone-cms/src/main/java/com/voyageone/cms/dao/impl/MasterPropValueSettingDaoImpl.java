package com.voyageone.cms.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.dao.MasterPropValueSettingDao;
import com.voyageone.cms.modelbean.MasterProperty;
import com.voyageone.cms.modelbean.PropertyOption;
import com.voyageone.cms.modelbean.PropertyRule;
import com.voyageone.cms.modelbean.PropertyValue;

@Repository
public class MasterPropValueSettingDaoImpl extends BaseDao implements MasterPropValueSettingDao {

	@Override
	public List<MasterProperty> getMasterProperties(int categoryId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("category_id", categoryId);

		return super.selectList("get_master_properties", parmMap);
	}

	@Override
	public List<PropertyOption> getPropertyOptions(int categoryId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("category_id", categoryId);

		return super.selectList("get_property_options", parmMap);
	}

	@Override
	public List<PropertyRule> getPropertyRules(int categoryId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("category_id", categoryId);

		return super.selectList("get_property_rules", parmMap);
	}

	@Override
	public int save(List<PropertyValue> values) {

		Map<String, List<PropertyValue>> insertDataMap = new HashMap<String, List<PropertyValue>>();
		insertDataMap.put("list", values);

		int inCount = super.updateTemplate.insert("insert_property_values", insertDataMap);

		return inCount;
	}

	@Override
	public List<PropertyValue> getUpdatePropertyValues(String channel_id, int level, String level_value) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channel_id);
		parmMap.put("level", level);
		parmMap.put("level_value", level_value);

		return super.selectList("get_master_property_values", parmMap);
	}

	@Override
	public int delete(String channel_id, int level, String level_value) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channel_id);
		parmMap.put("level", level);
		parmMap.put("level_value", level_value);

		return super.delete("delete_property_values", parmMap);
	}

	@Override
	public int getPropValueCount(String channel_id, int level, String level_value) {

		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channel_id", channel_id);
		parmMap.put("level", level);
		parmMap.put("level_value", level_value);

		return super.selectOne("get_property_values_count", parmMap);
	}

}
