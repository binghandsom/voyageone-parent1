package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository("web2.cms.wsdl.CmsBtTagDao")
public class CmsBtTagDao extends BaseDao{

    public int insertCmsBtTag(CmsBtTagModel cmsBtTagModel){
        return updateTemplate.insert("insert_cms_bt_tag1",cmsBtTagModel);
    }

    public int updateCmsBtTag(CmsBtTagModel cmsBtTagModel){
        return updateTemplate.update("update_cms_bt_tag1", cmsBtTagModel);
    }

    public CmsBtTagModel getCmsBtTagByTagId(int tagId) {
        HashMap<String, Object> paraIn = new HashMap<String, Object>();
        paraIn.put("tagId", tagId);
        return updateTemplate.selectOne("select_one_by_tag_id1", paraIn);
    }
//
//    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
//        return updateTemplate.selectList("select_list_by_parent_tag_id", parentTagId);
//    }
}
