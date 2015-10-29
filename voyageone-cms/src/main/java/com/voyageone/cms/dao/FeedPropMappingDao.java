package com.voyageone.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.formbean.FeedMappingProp;
import com.voyageone.cms.modelbean.*;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.ims.enums.MasterPropTypeEnum;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 为"第三方品牌数据，和主数据，进行属性和值的匹配"提供数据服务
 * <p>
 * Created by Jonas on 9/1/15.
 */
@Repository
public class FeedPropMappingDao extends BaseDao {

    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    /**
     * 获取需要进行处理的属性类型
     */
    private List<Integer> getMasterPropTypes() {

        // 只有下面这些类型,才需要被显示到画面上

        List<Integer> types = new ArrayList<>();

        types.add(MasterPropTypeEnum.INPUT.getValue());
        types.add(MasterPropTypeEnum.SINGLECHECK.getValue());
        types.add(MasterPropTypeEnum.MULTICHECK.getValue());
        types.add(MasterPropTypeEnum.LABEL.getValue());
        return types;
    }

    public String selectCategoryPath(int categoryId) {
        return selectOne("ims_mt_categories_selectCategoryPath", parameters("categoryId", categoryId));
    }

    public List<FeedMappingProp> selectProps(String prop_name, Integer categoryId, String channel_id, int start, int length, String is_ignored, String is_required) {

        List<Integer> types = getMasterPropTypes();

        return selectList("ims_mt_prop_selectProps", parameters(
                "prop_name", prop_name,
                "categoryId", categoryId,
                "channel_id", channel_id,
                "start", start,
                "length", length,
                "types", types,
                "is_ignored", is_ignored,
                "is_required", is_required));
    }

    public int selectPropsCount(String prop_name, Integer categoryId, String channel_id, String is_ignored, String is_required) {

        List<Integer> types = getMasterPropTypes();

        return selectOne("ims_mt_prop_selectPropsCount", parameters("prop_name", prop_name, "categoryId", categoryId, "channel_id",
                channel_id, "types", types, "is_ignored", is_ignored, "is_required", is_required));
    }

    public int insertMapping(FeedPropMapping mapping) {
        return insert("ims_bt_feed_prop_mapping_insertMapping", mapping);
    }

    public FeedPropMapping selectMappingByFinger(FeedPropMapping mapping) {
        return selectOne("ims_bt_feed_prop_mapping_selectMappingByFinger", mapping);
    }

    public FeedPropMapping selectMappingById(long id) {
        return selectOne("ims_bt_feed_prop_mapping_selectMappingById", parameters("id", id));
    }

    public List<FeedPropMapping> selectMappings(int prop_id, String channel_id) {
        return selectList("ims_bt_feed_prop_mapping_selectMapping_byProp", parameters("prop_id", prop_id, "channel_id", channel_id));
    }

    public List<FeedConfig> selectFeedProps(String channel_id) {
        return selectList("cms_mt_feed_config_selectFeedProps", parameters("channel_id", channel_id, "is_attribute", 1));
    }

    public List<FeedValue> selectFeedValues(String attr_name, String channel_id) {
        return selectList("cms_mt_feed_attribute_selectFeedValues", parameters("attr_name", attr_name, "channel_id", channel_id));
    }

    public List<PropertyOption> selectPropOptions(long prop_id) {
        return selectList("ims_mt_prop_option_selectPropOptions", parameters("prop_id", prop_id));
    }

    /**
     * 获取 props (prop_id) 属性的 is_ignore.
     * @return 返回的集合对象中,只包含 prop_id 和 is_ignore
     */
    public List<FeedMappingProp> selectIgnoreValue(List<FeedMappingProp> props, String channel_id) {
        return selectList("ims_bt_feed_prop_ignore_selectIgnoreValue", parameters("props", props, "channel_id", channel_id));
    }

    public int updateIgnoreValue(FeedMappingProp prop, String channel_id, String userName) {
        return update("ims_bt_feed_prop_ignore_updateIgnoreValue", parameters("prop", prop, "channel_id", channel_id, "userName", userName));
    }

    public int insertIgnoreValue(FeedMappingProp prop, String channel_id, String userName) {
        return insert("ims_bt_feed_prop_ignore_insertIgnoreValue", parameters("prop", prop, "channel_id", channel_id, "userName", userName));
    }

    public int updateIgnoreValues(List<FeedMappingProp> props, String channel_id, String userName) {
        return update("ims_bt_feed_prop_ignore_updateIgnoreValues", parameters("props", props, "channel_id", channel_id, "userName", userName, "is_ignore", 1));
    }

    public int insertIgnoreValues(List<FeedMappingProp> props, String channel_id, String userName) {
        return insert("ims_bt_feed_prop_ignore_insertIgnoreValues", parameters("props", props, "channel_id", channel_id, "userName", userName, "is_ignore", 1));
    }

    public int deleteMapping(int id) {
        return delete("ims_bt_feed_prop_mapping_deleteMapping", parameters("id", id));
    }

    public int updateMapping(FeedPropMapping mapping) {
        return update("ims_bt_feed_prop_mapping_updateMapping", mapping);
    }

    public String selectDefaultValue(FeedMappingProp prop, String channel_id) {
        return selectOne("ims_bt_feed_prop_mapping_default_selectDefaultValue", parameters(
                "prop", prop, "channel_id", channel_id
        ));
    }

    public CategoryPropCount selectCountsByCategory(String channel_id, int category_id) {

        List<Integer> types = getMasterPropTypes();

        return selectOne("ims_bt_feed_prop_mapping_selectCountsByCategory", parameters(
                "channel_id", channel_id,
                "category_id", category_id,
                "types", types));
    }

    public ImsCategoryExtend selectCategoryExtend(String channel_id, int category_id) {
        return selectOne("ims_bt_category_extend_selectCategoryExtend", parameters("category_id", category_id, "channel_id", channel_id));
    }

    public int insertCategoryExtend(ImsCategoryExtend imsCategoryExtend) {
        return insert("ims_bt_category_extend_insertCategoryExtend", imsCategoryExtend);
    }

    public int isSetAttr(ImsCategoryExtend imsCategoryExtend) {
        return update("ims_bt_category_extend_isSetAttr", imsCategoryExtend);
    }
}
