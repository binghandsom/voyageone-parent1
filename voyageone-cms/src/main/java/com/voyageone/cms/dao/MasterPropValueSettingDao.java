package com.voyageone.cms.dao;

import java.util.List;

import com.voyageone.cms.modelbean.MasterProperty;
import com.voyageone.cms.modelbean.PropertyOption;
import com.voyageone.cms.modelbean.PropertyRule;
import com.voyageone.cms.modelbean.PropertyValue;

public interface MasterPropValueSettingDao {
    
    public List<MasterProperty> getMasterProperties(int categoryId);
    
    public List<PropertyOption> getPropertyOptions(int categoryId);
    
    public List<PropertyRule> getPropertyRules(int categoryId);
    
    public List<PropertyValue> getUpdatePropertyValues(String channel_id,int level,String level_value);
    
    public int save(List<PropertyValue> values);
    
    public int delete(String channel_id,int level,String level_value);
    
    public int getPropValueCount(String channel_id,int level,String level_value);

}
