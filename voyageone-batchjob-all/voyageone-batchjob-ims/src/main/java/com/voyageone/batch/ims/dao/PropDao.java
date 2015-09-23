package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.*;
import com.voyageone.common.Constants;
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
    public int insertProp( PropBean propBean, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("prop", propBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertProp", params);

        PropBean propBeanResult = (PropBean)params.get("prop");

        return propBeanResult.getPropId();
    }

    /**
     * 插入主数据（属性匹配）
     */
    public void insertPropMapping( PropMappingBean propMappingBean, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("mapping", propMappingBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPropMapping", params);

    }

    /**
     * 插入主数据（属性可选项）
     * @return 新插入的属性可选项id
     */
    public int insertPropOption( PropOptionBean propOptionBean, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("option", propOptionBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPropOption", params);

        PropOptionBean propOptionBeanResult = (PropOptionBean)params.get("option");

        return propOptionBeanResult.getPropOptionId();
    }

    /**
     * 插入主数据（属性可选项匹配）
     */
    public void insertPropOptionMapping( PropOptionMappingBean propOptionMappingBean, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("mapping", propOptionMappingBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPropOptionMapping", params);

    }

    /**
     * 插入主数据（属性规则）
     */
    public void insertPropRule( PropRuleBean propRuleBean, String jobName ) {
        Map<String, Object> params = new HashMap<>();

        params.put("rule", propRuleBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPropRule", params);

    }

    /**
     * 根据主数据类目id， 获取该类目在主数据属性表中，已经保存过的那些属性的（第三方平台的）属性id列表
     */
    public List<HashMap> selectPlatformPropIdListByCategoryId(int category_id)
    {
        Map<String, Object> params = new HashMap<>();

        params.put("category_id", category_id);

        return super.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformPropIdListByCategoryId", params);
    }

    /**
     * 根据主数据类目id， 获取该类目在主数据属性表中，已经保存过的那些属性的（第三方平台的）属性id列表
     */
    public List<HashMap> selectPropOptionListByCategoryId(int category_id)
    {
        Map<String, Object> params = new HashMap<>();

        params.put("category_id", category_id);

        return super.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropOptionListByCategoryId", params);
    }

    /**
     * @Description 根据平台属性hash值获取属性mapping列表
     * @Author Leo
     * @param platformPropHashList
     * @return
     */
    public List<PropMappingBean> selectPropMappingByPlatformPropHashList(List<String> platformPropHashList, boolean isShop)
    {
        Map<String, Object> dataMap = new HashMap<>();
        if (platformPropHashList == null || platformPropHashList.isEmpty())
            return null;

        dataMap.put("propHashList", platformPropHashList);
        dataMap.put("is_shop", isShop);
        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropMappingByPropHashList", dataMap);
    }

    /**
     * @Description 根据平台属性hash值获取属性mapping列表
     * @Author Leo
     * @param propId
     * @return
     */
    public PropBean selectPropByPropId(int propId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_id", propId);
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropByPropId", dataMap);
    }

    /**
     * @Description 根据平台属性hash值获取属性mapping列表
     * @Author Leo
     * @param propName
     * @param categoryId
     * @return
     */
    public PropBean selectPropByPropName(String categoryId, String propName)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_name", propName);
        dataMap.put("category_id", categoryId);
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropByPropName", dataMap);
    }

    public PropOptionBean selectPropOptionByPropIdAndPropOptionValue(int propId, String propOptionValue)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_id", propId);
        dataMap.put("prop_option_value", propOptionValue);
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropOptionByPropIdAndOptionValue", dataMap);
    }

    public String selectPlatformPropOptionHashByPropOptionId(int propOptionId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("prop_option_id", propOptionId);
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformPropOptionHashByPropOptionId", dataMap);
    }

    public String selectPlatformPropOptionValueByOptionHash(String platformPropOptionHash)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("platform_prop_option_hash", platformPropOptionHash);
        return selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformPropOptionValueByOptionHash", dataMap);
    }
}
