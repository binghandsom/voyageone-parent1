package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.modelbean.PlatformPropOptionBean;
import com.voyageone.batch.ims.modelbean.PlatformPropRuleBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlatformPropDao extends BaseDao {

    /**
     * 删除（第三方平台）类目下的属性
     */
    public void deletePlatformProp( List<PlatformPropBean> props) {
        Map<String, Object> params = new HashMap<>();

        params.put("props", props);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_deletePlatformProp", params);

    }

    /**
     * 插入（第三方平台）类目下的属性
     */
    public void insertPlatformProp( List<PlatformPropBean> props, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("props", props);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPlatformProp", params);

    }

    /**
     * 删除（第三方平台）类目下的属性的可选项目
     */
    public void deletePlatformPropOption( List<PlatformPropOptionBean> options) {
        Map<String, Object> params = new HashMap<>();

        params.put("options", options);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_deletePlatformPropOption", params);

    }

    /**
     * 插入（第三方平台）类目下的属性的可选项目
     */
    public void insertPlatformPropOption( List<PlatformPropOptionBean> options, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("options", options);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPlatformPropOption", params);

    }

    /**
     * 删除（第三方平台）类目下的属性规则
     */
    public void deletePlatformPropRule( List<PlatformPropRuleBean> rules) {
        Map<String, Object> params = new HashMap<>();

        params.put("rules", rules);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_deletePlatformPropRule", params);

    }

    /**
     * 插入（第三方平台）类目下的属性规则
     */
    public void insertPlatformPropRule( List<PlatformPropRuleBean> rules, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("rules", rules);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPlatformPropRule", params);

    }

    /**
     * @Author: Leo
     * @Description: 查找指定平台分类的所有的产品属性
     */
    public List<PlatformPropBean> selectProductPropsByCid(int cartId, String platformCId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        dataMap.put("platform_cid", platformCId);
        dataMap.put("is_product", 1);
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropsByCId", dataMap);
    }

    /**
     * @Author: Leo
     * @Description: 查找指定平台分类的所有的商品属性
     */
    public List<PlatformPropBean> selectItemPropsByCid(int cartId, String platformCId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        dataMap.put("platform_cid", platformCId);
        dataMap.put("is_product", 0);
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPropsByCId", dataMap);
    }

     /**
     * @Author: Leo
     * @Description: 根据platform_prop_hash查找属性
     */
    public PlatformPropBean selectPlatformPropByPropHash(String platformPropHash) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("platform_prop_hash", platformPropHash);
        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformPropByPropHash", dataMap);
    }

    public List<PlatformPropOptionBean> selectPlatformOptionsByPropHash(String platformPropHash) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("platform_prop_hash", platformPropHash);
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformOptionsByPropHash", dataMap);
    }

    public PlatformPropBean selectPlatformPropByPropId(int cartId, String categoryId, String propId, String parentPropHash)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        dataMap.put("platform_cid", categoryId);
        dataMap.put("platform_prop_id", propId);
        dataMap.put("parent_prop_hash", parentPropHash);
        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformPropByPropId", dataMap);
    }


}
