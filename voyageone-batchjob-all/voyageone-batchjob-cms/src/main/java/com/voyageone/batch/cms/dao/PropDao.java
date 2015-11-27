package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.batch.cms.model.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PropDao extends BaseDao {

    /**
     * 插入主数据（属性）
     * @return 新插入的属性id
     */
    public int insertProp(PropertyModel propertyModel, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("prop", propertyModel);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertProp", params);

        PropertyModel propertyModelResult = (PropertyModel)params.get("prop");

        return propertyModelResult.getPropId();
    }

    /**
     * 插入主数据（属性匹配）
     */
    public void insertPropMapping(PropMappingModel propMappingModel, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("mapping", propMappingModel);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertPropMapping", params);

    }

    /**
     * 插入主数据（属性可选项）
     * @return 新插入的属性可选项id
     */
    public int insertPropOption(PropOptionModel propOptionModel, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("option", propOptionModel);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertPropOption", params);

        PropOptionModel propOptionModelResult = (PropOptionModel)params.get("option");

        return propOptionModelResult.getPropOptionId();
    }

    /**
     * 插入主数据（属性可选项匹配）
     */
    public void insertPropOptionMapping( PropOptionMappingBean propOptionMappingBean, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("mapping", propOptionMappingBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertPropOptionMapping", params);

    }

    /**
     * 插入主数据（属性规则）
     */
    public void insertPropRule(PropRuleModel propRuleModel, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("rule", propRuleModel);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertPropRule", params);

    }

    /**
     * 根据主数据类目id， 获取该类目在主数据属性表中，已经保存过的那些属性的（第三方平台的）属性id列表
     */
    public List<HashMap> selectPlatformPropIdListByCategoryId(int category_id)
    {
        Map<String, Object> params = new HashMap<>();

        params.put("category_id", category_id);

        return super.selectList(Constants.DAO_NAME_SPACE_CMS + "ims_selectPlatformPropIdListByCategoryId", params);
    }

    /**
     * 根据主数据类目id， 获取该类目在主数据属性表中，已经保存过的那些属性的（第三方平台的）属性id列表
     */
    public List<HashMap> selectPropOptionListByCategoryId(int category_id)
    {
        Map<String, Object> params = new HashMap<>();

        params.put("category_id", category_id);

        return super.selectList(Constants.DAO_NAME_SPACE_CMS + "ims_selectPropOptionListByCategoryId", params);
    }

    /**
     * @Description 根据平台属性hash值获取属性mapping列表
     * @Author Leo
     * @param platformPropHashList
     * @return
     */
    public List<PropMappingModel> selectPropMappingByPlatformPropHashList(List<String> platformPropHashList, boolean isShop)
    {
        Map<String, Object> dataMap = new HashMap<>();
        if (platformPropHashList == null || platformPropHashList.isEmpty())
            return null;

        dataMap.put("propHashList", platformPropHashList);
        dataMap.put("is_shop", isShop);
        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_selectPropMappingByPropHashList", dataMap);
    }

    /**
     * @Description 根据平台属性hash值获取属性mapping列表
     * @Author Leo
     * @param propId
     * @return
     */
    public PropertyModel selectPropByPropId(int propId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_id", propId);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_selectPropByPropId", dataMap);
    }

    /**
     * @Description 根据平台属性hash值获取属性mapping列表
     * @Author Leo
     * @param propName
     * @param categoryId
     * @return
     */
    public PropertyModel selectPropByPropName(String categoryId, String propName)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_name", propName);
        dataMap.put("category_id", categoryId);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_selectPropByPropName", dataMap);
    }

    public PropOptionModel selectPropOptionByPropIdAndPropOptionValue(int propId, String propOptionValue)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_id", propId);
        dataMap.put("prop_option_value", propOptionValue);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_selectPropOptionByPropIdAndOptionValue", dataMap);
    }

    public String selectPlatformPropOptionHashByPropOptionId(int propOptionId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_option_id", propOptionId);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_selectPlatformPropOptionHashByPropOptionId", dataMap);
    }

    public String selectPlatformPropOptionValueByOptionHash(String platformPropOptionHash)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("platform_prop_option_hash", platformPropOptionHash);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_selectPlatformPropOptionValueByOptionHash", dataMap);
    }
}
