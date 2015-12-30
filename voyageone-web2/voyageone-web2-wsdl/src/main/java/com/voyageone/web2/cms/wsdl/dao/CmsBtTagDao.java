package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository("web2.cms.wsdl.CmsBtTagDao")
public class CmsBtTagDao extends WebBaseDao{

    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.WSDL;
    }

    public int insertCmsBtTag(CmsBtTagModel cmsBtTagModel){
        return insert("insert_cms_bt_tag", cmsBtTagModel);
    }

    public int updateCmsBtTag(CmsBtTagModel cmsBtTagModel){
        return update("update_cms_bt_tag", cmsBtTagModel);
    }

    public CmsBtTagModel getCmsBtTagByTagId(int tagId) {
        HashMap<String, Object> paraIn = new HashMap<String, Object>();
        paraIn.put("tagId", tagId);
        return selectOne("select_one_by_tag_id", paraIn);
    }

    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        return selectList("select_list_by_parent_tag_id", parentTagId);
    }

    public List<CmsBtTagModel> selectListByParentTagId(String channelId, int parentTagId, String tagName) {
        HashMap<String, Object> paraIn = new HashMap<String, Object>();
        paraIn.put("channelId", channelId);
        paraIn.put("parentTagId", parentTagId);
        paraIn.put("tagName", tagName);

        return selectList("select_one_by_tag_name", paraIn);
    }
}
