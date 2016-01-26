package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.ims.modelbean.DictWordBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * for voyageone_ims.ims_mt_dict
 * <p>
 * Created by Jonas on 9/11/15.
 */
@Repository
public class DictDao extends BaseDao {

    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public List<DictWordBean> selectAll(String channel_id, int start, int length) {
        return selectList("cms_mt_dict_selectAll", parameters("channel_id", channel_id, "start", start, "length", length));
    }

    public int selectAllCount(String channel_id) {
        return selectOne("cms_mt_dict_selectAllCount", parameters("channel_id", channel_id));
    }

    public int insertDict(DictWordBean dictWordBean) {
        return insert("cms_mt_dict_insertDict", dictWordBean);
    }

    public int insertDictLog(DictWordBean dictWordBean){
        return insert("cms_mt_dict_insertDict_log",dictWordBean);
    }

    public int updateDict(DictWordBean dictWordBean) {
        return update("cms_mt_dict_updateDict", dictWordBean);
    }

    public int deleteDict(DictWordBean dictWordBean) {
        return delete("cms_mt_dict_deleteDict", dictWordBean);
    }

    public List<DictWordBean> selectByName(DictWordBean dictWordBean) {
        return selectList("cms_mt_dict_selectByName", dictWordBean);
    }

    public List<DictWordBean> selectSimpleDict(String channel_id) {
        return selectList("cms_mt_dict_selectSimpleDict", parameters("channel_id", channel_id));
    }
}
