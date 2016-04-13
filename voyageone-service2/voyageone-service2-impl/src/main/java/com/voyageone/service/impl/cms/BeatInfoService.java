package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtBeatInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtBeatInfoModel;
import com.voyageone.service.model.cms.enums.BeatFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Tag Service
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class BeatInfoService extends BaseService {

    @Autowired
    private CmsBtBeatInfoDao beatInfoDao;


    public List<CmsBtBeatInfoModel> getBeatInfoListByTaskId(int taskId, BeatFlag flag, int offset, int size) {
        return beatInfoDao.selectListByTask(taskId, flag, offset, size);
    }

    public int getBeatInfoCountByTaskId(int taskId, BeatFlag flag) {
        return beatInfoDao.selectListByTaskCount(taskId, flag);
    }

    public CmsBtBeatInfoModel getBeatInfById(int beatId) {
        return beatInfoDao.selectOneById(beatId);
    }

    public List<CmsBtBeatInfoModel> getBeatInfByTaskId(int taskId) {
        return beatInfoDao.selectListByTask(taskId);
    }

    public List<Map> getBeatSummary(int taskId) {
        return beatInfoDao.selectSummary(taskId);
    }

    public int getCountInFlags(int taskId, BeatFlag... flags) {
        return beatInfoDao.selectCountInFlags(taskId, flags);
    }

    public CmsBtBeatInfoModel getBeatInfByNumiid(int taskId, String numIid) {
        return beatInfoDao.selectOneByNumiid(taskId, numIid);
    }

    public List<CmsBtBeatInfoModel> getBeatInfByNumiidInOtherTask(int promotion_id, int taskId, String numIid) {
        return beatInfoDao.selectListByNumiidInOtherTask(promotion_id, taskId, numIid);
    }

    public int addTasks(List<CmsBtBeatInfoModel> models) {
        return beatInfoDao.insertList(models);
    }

    public int updateCode(CmsBtBeatInfoModel model) {
        return beatInfoDao.updateCode(model);
    }

    public int updateDiffPromotionMessage(int taskId, String message) {
        return beatInfoDao.updateDiffPromotionMessage(taskId, message);
    }

    public int removeTask(int taskId) {
        return beatInfoDao.deleteByTask(taskId);
    }

    public void importBeatInfo(int taskId, List<CmsBtBeatInfoModel> models) {
        removeTask(taskId);

        addTasks(models);

        updateDiffPromotionMessage(taskId, "与 Promotion 信息不符");
    }

    public int updateBeatInfoFlag(CmsBtBeatInfoModel beatInfoModel) {
        return beatInfoDao.updateFlag(beatInfoModel);
    }

    public int updateBeatInfoFlag(int taskId, BeatFlag flag, String userName) {
        return beatInfoDao.updateFlags(taskId, flag, userName);
    }


}
