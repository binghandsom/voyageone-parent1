package com.voyageone.service.dao.cms;

import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtDictModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jonas, 1/19/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtDictDao extends ServiceBaseDao {

    public List<CmsMtDictModel> selectByChannel(CmsDictionaryIndexBean params) {
        return selectList("cms_mt_dict_selectByCondition", params);
    }

    public int selectAllCount(CmsDictionaryIndexBean params) {
        return selectOne("cms_mt_dict_selectAllCount", params);
    }

    public int insertDict(CmsMtDictModel cmsMtDictModel) {
        return insert("cms_mt_dict_insertDict", cmsMtDictModel);
    }

    public int insertDictLog(CmsMtDictModel cmsMtDictModel){
        return insert("cms_mt_dict_insertDict_log",cmsMtDictModel);
    }

    public int updateDict(CmsMtDictModel cmsMtDictModel) {
        return update("cms_mt_dict_updateDict", cmsMtDictModel);
    }

    public int deleteDict(CmsMtDictModel cmsMtDictModel) {
        return delete("cms_mt_dict_deleteDict", cmsMtDictModel);
    }

    public List<CmsMtDictModel> selectByName(CmsMtDictModel cmsMtDictModel) {
        return selectList("cms_mt_dict_selectByName", cmsMtDictModel);
    }

    public CmsMtDictModel selectById(CmsMtDictModel cmsMtDictModel) {
        return selectOne("cms_mt_dict_selectById", cmsMtDictModel);
    }

    public List<CmsMtDictModel> selectSimpleDict(String channel_id) {
        return selectList("cms_mt_dict_selectSimpleDict", parameters("channel_id", channel_id));
    }
}
