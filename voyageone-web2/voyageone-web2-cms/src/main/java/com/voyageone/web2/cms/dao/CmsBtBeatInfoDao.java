package com.voyageone.web2.cms.dao;

import com.voyageone.cms.enums.BeatFlag;
import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.cms.model.CmsBtBeatInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 */
@Repository
public class CmsBtBeatInfoDao extends WebBaseDao {

    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CMS;
    }

    public int insertList(List<CmsBtBeatInfoModel> modelList) {
        return insert("cms_bt_beat_info_insertList", parameters("modelList", modelList));
    }

    /**
     * 更新 BeatInfo 的 Message, 为那些在 Beat 中但不在 Promotion 中的 Code
     */
    public int updateNoCodeMessage(int task_id, String message) {
        return update("cms_bt_beat_info_updateNoCodeMessage", parameters(
                "task_id", task_id,
                "message", message,
                "syn_flag", BeatFlag.CANT_BEAT.getFlag()));
    }

    public List<CmsBtBeatInfoModel> selectListByTask(int task_id) {
        return selectList("cms_bt_beat_info_selectListByTask", parameters(
                "task_id", task_id));
    }

    public List<CmsBtBeatInfoModel> selectListByTask(int task_id, int offset, int size) {
        return selectList("cms_bt_beat_info_selectListByTask_page", parameters(
                "task_id", task_id,
                "offset", offset,
                "size", size));
    }

    public int selectListByTaskCount(int task_id) {
        return selectOne("cms_bt_beat_info_selectListByTask_count", parameters(
                "task_id", task_id));
    }

    public int deleteByTask(int task_id) {
        return delete("cms_bt_beat_info_deleteByTask", parameters("task_id", task_id));
    }

    public int selectCountInFlags(int task_id, BeatFlag... flags) {
        return selectOne("cms_bt_beat_info_selectCountInFlags", parameters("task_id", task_id, "flags", flags));
    }
}
