package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.enums.BeatFlag;
import com.voyageone.web2.cms.wsdl.models.CmsBtBeatInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 */
@Repository
public class CmsBtBeatInfoDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.web2.cms.sql";
    }

    public int insertList(List<CmsBtBeatInfoModel> modelList) {
        return insert("cms_bt_beat_info_insertList", parameters("modelList", modelList));
    }

    /**
     * 更新 BeatInfo 的 Message, 为那些在 Beat 中但不在 Promotion 中的 Code/Numiid
     */
    public int updateDiffPromotionMessage(int task_id, String message) {
        Map params = parameters(
                "task_id", task_id,
                "message", message,
                "syn_flag", BeatFlag.CANT_BEAT.getFlag());
        int count0 = update("cms_bt_beat_info_updateNoCodeMessage", params);
        int count1 = update("cms_bt_beat_info_updateNoNumiidMessage", params);
        return count0 + count1;
    }

    public List<CmsBtBeatInfoModel> selectListByTask(int task_id) {
        return selectList("cms_bt_beat_info_selectListByTask", parameters(
                "task_id", task_id));
    }

    public List<CmsBtBeatInfoModel> selectListByTask(int task_id, BeatFlag flag, int offset, int size) {
        return selectList("cms_bt_beat_info_selectListByTask_page", parameters(
                "task_id", task_id,
                "flag", flag,
                "offset", offset,
                "size", size));
    }

    public int selectListByTaskCount(int task_id, BeatFlag flag) {
        return selectOne("cms_bt_beat_info_selectListByTask_count", parameters(
                "task_id", task_id,
                "flag", flag));
    }

    public int deleteByTask(int task_id) {
        return delete("cms_bt_beat_info_deleteByTask", parameters("task_id", task_id));
    }

    public int selectCountInFlags(int task_id, BeatFlag... flags) {
        return selectOne("cms_bt_beat_info_selectCountInFlags", parameters("task_id", task_id, "flags", flags));
    }

    public CmsBtBeatInfoModel selectOneById(int beat_id) {
        return selectOne("cms_bt_beat_info_selectOneById", parameters("beat_id", beat_id));
    }

    public int updateFlag(CmsBtBeatInfoModel beatInfoModel) {
        return update("cms_bt_beat_info_updateFlag", beatInfoModel);
    }

    public int updateFlags(Integer task_id, BeatFlag flag, String userName) {
        return update("cms_bt_beat_info_updateFlags", parameters(
                "task_id", task_id,
                "modifier", userName,
                "syn_flag", flag.getFlag()));
    }

    public List<Map> selectSummary(int task_id) {
        return selectList("cms_bt_beat_info_selectSummary", parameters("task_id", task_id));
    }

    public List<CmsBtBeatInfoModel> selectListByNumiidInOtherTask(int promotion_id, int task_id, String num_iid) {
        return selectList("cms_bt_beat_info_selectListByNumiidInOtherTask", parameters(
                "promotion_id", promotion_id,
                "task_id", task_id,
                "num_iid", num_iid
        ));
    }

    public CmsBtBeatInfoModel selectOneByNumiid(int task_id, String num_iid) {
        return selectOne("cms_bt_beat_info_selectOneByNumiid", parameters(
                "task_id", task_id,
                "num_iid", num_iid
        ));
    }

    public Integer updateCode(CmsBtBeatInfoModel model) {
        return update("cms_bt_beat_info_updateCode", model);
    }
}
