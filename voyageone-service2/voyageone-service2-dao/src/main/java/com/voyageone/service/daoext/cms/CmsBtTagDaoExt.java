package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtTagModel;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository
public class CmsBtTagDaoExt extends ServiceBaseDao {

    public int deleteCmsBtTagByTagId(CmsBtTagModel cmsBtTagModel) {
        return delete("delete_cms_bt_tag_by_tag_id", cmsBtTagModel);
    }

    public int deleteCmsBtTagByParentTagId(CmsBtTagModel cmsBtTagModel) {
        return delete("delete_cms_bt_tag_by_parent_tag_id", cmsBtTagModel);
    }

    public CmsBtTagModel selectCmsBtTagByTagId(int tagId) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("tagId", tagId);
        return selectOne("select_one_by_tag_id", paraIn);
    }

    public CmsBtTagModel selectCmsBtTagByParentTagId(int tagId) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("tagId", tagId);
        return selectOne("select_one_by_parent_tag_id", paraIn);
    }

    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        return selectList("select_list_by_parent_tag_id", parentTagId);
    }

    public List<CmsBtTagModel> selectListByParentTagId(String channelId, int parentTagId, String tagName) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("channelId", channelId);
        paraIn.put("parentTagId", parentTagId);
        paraIn.put("tagName", tagName);

        return selectList("select_one_by_tag_name", paraIn);
    }

    public List<CmsBtTagBean> selectListByChannelIdAndTagType(Map<String, Object> params) {
        return selectList("select_list_by_channel_id_and_tag_type", params);
    }

    public List<CmsBtTagBean> selectListByChannelId4AdvSearch(Map<String, Object> params) {
        return selectList("select_list_by_channel_id_4AdvSearch", params);
    }

    public List<CmsBtTagBean> selectListByChannelIdAndTagType2(Map<String, Object> params) {
        return selectList("select_list_by_channel_id_and_tag_type2", params);
    }

    public List<CmsBtTagBean> selectListByChannelIdAndParentTag(Map<String, Object> params) {
        return selectList("select_list_by_channel_id_and_parent_tag", params);
    }

    public List<CmsBtTagModel> selectCmsBtTagByTagInfo(String channelId, String parentTagId, String tagType) {
        return selectList("select_one_by_tag_info", parameters("channelId", channelId, "parentTagId", parentTagId, "tagType", tagType));
    }

    // 查询同级别的tag信息
    public List<CmsBtTagModel> selectListBySameLevel(String channelId, int parentTagId, int tagId) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("channelId", channelId);
        paraIn.put("parentTagId", parentTagId);
        paraIn.put("tagId", tagId);

        return selectList("select_list_by_same_lvl", paraIn);
    }

    // 根据tag path查询tag path name
    public List<CmsBtTagBean> selectTagPathNameByTagPath(String channelId, List<String> tagPathList) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("channelId", channelId);
        paraIn.put("tagPathList", tagPathList);

        return selectList("select_tagpathname_list_by_tagpath", paraIn);
    }

    /**
     * 根据Tag 类型查询channel Tag
     *
     * @param channelId 渠道ID
     * @param type      Tag类型
     */
    public List<CmsBtTagBean> selectListByType(String channelId, Integer type) {
        return selectList("select_list_by_type", parameters("channelId", channelId, "type", type));
    }

}
