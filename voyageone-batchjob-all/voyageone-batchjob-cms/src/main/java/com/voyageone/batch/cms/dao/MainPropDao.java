package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.common.Constants;
import com.voyageone.common.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

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
     * 设置为已处理
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
     * 根据product id，获取Sku列表
     * @param channel_id channel_id
     * @param product_id product_id
     * @return sku列表
     */
    public List<String> getSkuListFromProductId( String channel_id, String product_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);
        params.put("product_id", product_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "wms_bt_item_details_selectSkuListFromProductId", params);
    }

    /**
     * 一次性获取所有的类目匹配关系（根据channel_id）
     * @param channel_id 店铺
     * @return List<PlatformCategories>
     */
    public Map<String, String> selectMainCategoryIdList( String channel_id ) {

        // 返回值
        Map<String, String> result = new HashMap<>();

        // 输入参数
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);

        List<Map<String, String>> lstRelation = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_relation_category_selectMainCategoryIdList", params);

        // 换一种便于读取的格式
        Map<String, List<String>> mapRelation = new HashMap<>();
        for (Map<String, String> relation : lstRelation) {
            String strCategoryId = String.valueOf(relation.get("category_id"));
            String strParent = String.valueOf(relation.get("parent"));
            String strMainCategoryId;
            if (relation.get("main_category_id") == null) {
                strMainCategoryId = null;
            } else {
                strMainCategoryId = String.valueOf(relation.get("main_category_id"));
            }

            List<String> lstTemp = new ArrayList<>();

            lstTemp.add(strParent);
            lstTemp.add(strMainCategoryId);

            mapRelation.put(strCategoryId, lstTemp);
        }

        // 正式开始遍历
        for (Map<String, String> relation : lstRelation) {
            String strCategoryId = String.valueOf(relation.get("category_id"));
            String strMainCategoryId = selectMainCategoryIdListSub(strCategoryId, mapRelation);

            if (!StringUtils.isEmpty(strMainCategoryId)) {
                result.put(strCategoryId, strMainCategoryId);
            }

        }

        return result;
    }
    private String selectMainCategoryIdListSub(String categoryId, Map<String, List<String>> mapRelation) {
        // 看看自己有没有设定过MainCategoryId
        if (mapRelation.containsKey(categoryId)) {

            List<String> lstTemp;
            lstTemp = mapRelation.get(categoryId);

            String parent = lstTemp.get(0);
            String mainCategoryId = lstTemp.get(1);

            // 如果到顶层了，那就不用再看下去了
            if ("2".equals(categoryId)) {
                return null;
            }

            if (StringUtils.isEmpty(mainCategoryId)) {
                // 看看父亲有没有设定过
                return selectMainCategoryIdListSub(parent, mapRelation);
            } else {
                // 自己有设定过，那就直接返回
                return mainCategoryId;
            }

        } else {
            return null;
        }

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
     * 根据channel_id，获取ims_bt_sku_prop_mapping的值
     * @param channel_id channel_id
     * @param main_category_id main_category_id
     * @return 属性列表
     */
    public List<FeedMappingSkuBean> selectFeedMappingSkuList(String channel_id, String main_category_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channel_id);
        params.put("main_category_id", main_category_id);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_bt_sku_prop_mapping_select", params);
    }

    /**
     * 获取ims_bt_prop_value_sku_template的值
     * @return ims_bt_prop_value_sku_template列表
     */
    public List<PropValueSkuTemplateBean> selectPropValueSkuTemplateList() {

        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_bt_prop_value_sku_template_select");
    }

    /**
     * 获取ims_bt_prop_value_sku的值
     * @return ims_bt_prop_value_sku列表
     */
    public List<ImsPropValueSkuBean> selectPropValueSku(String channel_id, String sku, String prop_name) {
        Map<String, Object> params = new HashMap<>();
        params.put("order_channel_id", channel_id);
        params.put("sku", sku);
        params.put("prop_name", prop_name);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_bt_prop_value_sku_select", params);
    }

    /**
     * 插入sku的值表
     *
     */
    public void doInsertSkuValue(List<ImsPropValueSkuBean> imsPropValueSkuBeanList, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("imsPropValueSkuBeanList", imsPropValueSkuBeanList);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertPropValueSku", params);
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

    /**
     * 获取model id 和 主类目
     * @param channelId 渠道
     * @param modelId model id
     * @return 第一个项目是model id， 第二个项目是主类目， 第三个项目是主类目（cn表）
     */
    public List<String> selectModelIdByModelName(String channelId, String modelId) {

        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channelId);
        params.put("model_id", modelId);

        List<HashMap<String, String>> lstResult = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_model_select_main_category", params);

        if (lstResult == null || lstResult.size() == 0) {
            return null;
        } else {
            List<String> result = new ArrayList<>();

            // model id
            result.add(getString(lstResult.get(0).get("model_id")));

            // 主数据
            result.add(getString(lstResult.get(0).get("main_category_id")));
            result.add(getString(lstResult.get(0).get("main_category_id_cn")));
            result.add(getString(lstResult.get(0).get("ext_model_id")));
            result.add(getString(lstResult.get(0).get("ext_cn_model_id")));

            // 平台系
            result.add(getString(lstResult.get(0).get("tm_category_id")));
            result.add(getString(lstResult.get(0).get("ext_cn_tm_model_id")));
            result.add(getString(lstResult.get(0).get("jd_category_id")));
            result.add(getString(lstResult.get(0).get("ext_cn_jd_model_id")));

            return result;
        }

    }

    /**
     * 设置主类目
     * @param model_id_value model_id_value
     * @param channel_id channel_id
     * @param main_category_id main_category_id
     * @param jobName jobName
     */
    public void doSetMainCategoryId(List<String> model_id_value, String channel_id, String main_category_id, String jobName) {

        String model_id = model_id_value.get(0);

        String cms_bt_model_extend_data = model_id_value.get(1);
        String cms_bt_cn_model_extend_data = model_id_value.get(2);
        String cms_bt_model_extend_data_exist = model_id_value.get(3);
        String cms_bt_cn_model_extend_data_exist = model_id_value.get(4);

        String cms_bt_cn_tm_model_data = model_id_value.get(5);
        String cms_bt_cn_tm_model_exist = model_id_value.get(6);
        String cms_bt_cn_jd_model_data = model_id_value.get(7);
        String cms_bt_cn_jd_model_exist = model_id_value.get(8);

        // 获取对应的天猫类目和京东类目
        Map<String, String> platformCid = getPlatformCategoryByMainCategoryId(main_category_id);
        String platformCidTm = "";
        String platformCidJd = "";
        if (platformCid.containsKey("1")) {
            platformCidTm = platformCid.get("1");
        }
        if (platformCid.containsKey("2")) {
            platformCidJd = platformCid.get("2");
        }

        // 设定主类目
        if (StringUtils.isEmpty(cms_bt_model_extend_data_exist)) {
            // insert
            doInsertMainCategoryId_1(model_id, channel_id, main_category_id, jobName);
        } else {
            // update 与数据库里的不一致的场合，更新
            if (StringUtils.isEmpty(cms_bt_model_extend_data)) {
                cms_bt_model_extend_data = "";
            }
            if (!main_category_id.equals(cms_bt_model_extend_data)) {
                doUpdateMainCategoryId_1(model_id, channel_id, main_category_id, jobName);
            }
        }

        if (StringUtils.isEmpty(cms_bt_cn_model_extend_data_exist)) {
            // insert
            doInsertMainCategoryId_2(model_id, channel_id, main_category_id, jobName);
        } else {
            // update 与数据库里的不一致的场合，更新
            if (StringUtils.isEmpty(cms_bt_cn_model_extend_data)) {
                cms_bt_cn_model_extend_data = "";
            }
            if (!main_category_id.equals(cms_bt_cn_model_extend_data)) {
                doUpdateMainCategoryId_2(model_id, channel_id, main_category_id, jobName);
            }
        }

        if (StringUtils.isEmpty(cms_bt_cn_tm_model_exist)) {
            // insert
            doInsertPlatformCategoryId_Tm(model_id, channel_id, platformCidTm, jobName);
        } else {
            if (StringUtils.isEmpty(cms_bt_cn_tm_model_data)) {
                cms_bt_cn_tm_model_data = "";
            }
            if (!platformCidTm.equals(cms_bt_cn_tm_model_data)) {
                // 更新
                doUpdatePlatformCategoryId_Tm(model_id, channel_id, platformCidTm, jobName);
            }
        }

        if (StringUtils.isEmpty(cms_bt_cn_jd_model_exist)) {
            // insert
            doInsertPlatformCategoryId_Jd(model_id, channel_id, platformCidJd, jobName);
        } else {
            if (StringUtils.isEmpty(cms_bt_cn_jd_model_data)) {
                cms_bt_cn_jd_model_data = "";
            }
            if (!platformCidJd.equals(cms_bt_cn_jd_model_data)) {
                // 更新
                doUpdatePlatformCategoryId_Jd(model_id, channel_id, platformCidJd, jobName);
            }
        }


    }

    /**
     * 插入model的主类目
     *   插入表：
     *     cms_bt_model_extend
     *   设定字段：
     *     model_id
     *     channel_id
     *     main_category_id
     *     created
     *     creater
     * @param model_id model id
     * @param channel_id channel_id
     * @param main_category_id main category id
     * @param jobName jobName
     */
    private void doInsertMainCategoryId_1(String model_id, String channel_id, String main_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("main_category_id", main_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_model_extend_insert", params);

    }

    /**
     * 插入model的主类目
     *   插入表：
     *     cms_bt_cn_model_extend
     *   设定字段：
     *     model_id
     *     channel_id
     *     main_category_id
     *     created
     *     creater
     * @param model_id model id
     * @param channel_id channel_id
     * @param main_category_id main category id
     * @param jobName jobName
     */
    private void doInsertMainCategoryId_2(String model_id, String channel_id, String main_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("main_category_id", main_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_model_extend_insert", params);

    }

    /**
     * 更新model的主类目
     *   更新两张表：
     *     cms_bt_model_extend
     *   更新字段：
     *     main_category_id
     * @param model_id model id
     * @param channel_id channel_id
     * @param main_category_id main category id
     * @param jobName jobName
     */
    private void doUpdateMainCategoryId_1(String model_id, String channel_id, String main_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("main_category_id", main_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_model_extend_update", params);

    }

    /**
     * 更新model的主类目
     *   更新两张表：
     *     cms_bt_cn_model_extend
     *   更新字段：
     *     main_category_id
     * @param model_id model id
     * @param channel_id channel_id
     * @param main_category_id main category id
     * @param jobName jobName
     */
    private void doUpdateMainCategoryId_2(String model_id, String channel_id, String main_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("main_category_id", main_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_model_extend_update", params);

    }

    /**
     * 插入model的平台类目
     *   插入表：
     *     voyageone_cms.cms_bt_cn_tm_model_extend
     *   设定字段：
     *     model_id
     *     channel_id
     *     created
     *     creater
     * @param model_id model id
     * @param channel_id channel_id
     * @param platform_category_id tm_category_id 或者 jd_category_id
     * @param jobName jobName
     */
    private void doInsertPlatformCategoryId_Tm(String model_id, String channel_id, String platform_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("platform_category_id", platform_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_tm_model_extend_insert", params);

    }

    /**
     * 插入model的平台类目
     *   插入表：
     *     voyageone_cms.cms_bt_cn_jd_model_extend
     *   设定字段：
     *     model_id
     *     channel_id
     *     created
     *     creater
     * @param model_id model id
     * @param channel_id channel_id
     * @param platform_category_id tm_category_id 或者 jd_category_id
     * @param jobName jobName
     */
    private void doInsertPlatformCategoryId_Jd(String model_id, String channel_id, String platform_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("platform_category_id", platform_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_jd_model_extend_insert", params);

    }

    /**
     * 更新model的平台类目
     *   更新两张表：
     *     voyageone_cms.cms_bt_cn_tm_model_extend
     *   更新字段：
     *     tm_category_id
     * @param model_id model id
     * @param channel_id channel_id
     * @param platform_category_id tm_category_id
     * @param jobName jobName
     */
    private void doUpdatePlatformCategoryId_Tm(String model_id, String channel_id, String platform_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("platform_category_id", platform_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_tm_model_extend_update", params);

    }

    /**
     * 更新model的平台类目
     *   更新两张表：
     *     voyageone_cms.cms_bt_cn_jd_model_extend
     *   更新字段：
     *     jd_category_id
     * @param model_id model id
     * @param channel_id channel_id
     * @param platform_category_id jd_category_id
     * @param jobName jobName
     */
    private void doUpdatePlatformCategoryId_Jd(String model_id, String channel_id, String platform_category_id, String jobName) {

        Map<String, Object> params = new HashMap<>();

        params.put("model_id", model_id);
        params.put("channel_id", channel_id);
        params.put("platform_category_id", platform_category_id);
        params.put("jobName", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_cn_jd_model_extend_update", params);

    }

    /**
     * 根据主类目id，获取各个平台的类目id
     * @param categoryId 主类目的id
     * @return 各个平台的类目id
     */
    private Map<String, String> getPlatformCategoryByMainCategoryId(String categoryId) {
        Map<String, String> result = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);

        List<HashMap<String, String>> lstResult = selectList(Constants.DAO_NAME_SPACE_CMS + "ims_mt_category_mapping_select_by_categoryid", params);

        for (Map<String, String> map : lstResult) {
            result.put(getString(map.get("platform_id")), getString(map.get("platform_cid")));
        }

        return result;
    }

    public String getString(Object value) {
        if (value == null) {
            return "";
        } else {
            return String.valueOf(value);
        }
    }

}