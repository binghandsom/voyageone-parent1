package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MainPropDao extends BaseDao {

    /**
     * 查找未处理的商品
     * @param channel_id 店铺
     * @return List<PlatformCategories>
     */
    public List<MainPropTodoListBean> getPlatformSubCatsWithoutShop( String channel_id ) {

        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_product_attribute_selectTodoList", params);
    }

    /**
     * 查找未处理的商品
     * @param channel_id channel_id
     * @param product_id product_id
     */
    public void doFinishProduct( String channel_id, String product_id ) {

        Map<String, Object> params = new HashMap<>();

        params.put("channel_id", channel_id);
        params.put("product_id", product_id);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_mt_product_attribute_setFinished", params);
    }

    /**
     * 看看主数据的值是否已经被设置过了
     * @param channelId channelId
     * @param level level
     * @param level_value level_value
     * @return true/false
     */
    public boolean selectMainValue(String channelId, String level, String level_value) {

        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channelId);
        params.put("level", level);
        params.put("level_value", level_value);

        Integer cnt = selectOne(Constants.DAO_NAME_SPACE_CMS + "ims_mt_prop_value_selectMainValue", params);

        if (cnt > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 根据类目id，获取主数据的属性
     * @param categoryId 主数据类目id
     * @return 属性列表
     */
    public LinkedHashMap<String, ImsPropBean> selectImsPropByCategoryId(String categoryId) {

        Map<String, Object> params = new HashMap<>();
        params.put("category_id", categoryId);

        List<ImsPropBean> imsPropBeanList = selectList(Constants.DAO_NAME_SPACE_CMS + "ims_mt_prop_selectMainProp", params);

        LinkedHashMap<String, ImsPropBean> result = new LinkedHashMap<>();
        for (ImsPropBean imsPropBean : imsPropBeanList) {
            result.put(String.valueOf(imsPropBean.getPropId()), imsPropBean);
        }

        return result;

    }

    /**
     * 根据channel_id和product_id，获取feed的值
     * @param channel_id channel_id
     * @param product_id product_id
     * @return 属性列表
     */
    public List<MainPropTodoItemListBean> selectMainPropTodoItemListBean(String channel_id, String product_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);
        params.put("product_id", product_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_product_attribute_selectFeed", params);
    }

    /**
     * 根据channel_id，获取feed mapping default的值
     * @param channel_id channel_id
     * @return 属性列表
     */
    public List<FeedMappingDefaultBean> selectFeedMappingDefaultList(String channel_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_bt_feed_prop_mapping_default_select", params);
    }

    /**
     * 根据channel_id，获取feed mapping的值
     * @param channel_id channel_id
     * @param main_category_id main_category_id
     * @return 属性列表
     */
    public List<FeedMappingBean> selectFeedMappingList(String channel_id, String main_category_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);
        params.put("main_category_id", main_category_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_bt_feed_prop_mapping_select", params);
    }

    /**
     * 插入主数据的值表
     *
     */
    public void doInsertMainValue(List<ImsPropValueBean> imsPropValueBeanList, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("imsPropValueBeanList", imsPropValueBeanList);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertPropValue", params);
    }

}