package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.dao.cms.CmsBtTaskJiagepiluDao;
import com.voyageone.service.daoext.cms.CmsBtBeatInfoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Tag Service
 * 价格披露用~
 * 因接口改造改动 by Jonas on 2016-05-24 17:52:01
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.1.0
 * @since 2.0.0
 */
@Service
public class BeatInfoService1 extends BaseService implements BeatInfoService {

    @Autowired
    private CmsBtTaskJiagepiluDao jiagepiluDao;

    @Autowired
    private CmsBtBeatInfoDaoExt beatInfoDaoExt;

    @Override
    public List<CmsBtBeatInfoBean> getBeatInfoListByTaskId(int taskId, BeatFlag flag, String searchKey, int offset, int size) {
        return beatInfoDaoExt.selectListByTaskWithPrice(taskId, flag, searchKey, offset, size);
    }

    @Override
    public int getBeatInfoCountByTaskId(int taskId, BeatFlag flag, String searchKey) {
        return beatInfoDaoExt.selectListByTaskCount(taskId, flag, searchKey);
    }

    @Override
    public CmsBtBeatInfoBean getBeatInfById(int beatId) {
        CmsBtTaskJiagepiluModel model = jiagepiluDao.select(beatId);
        return new CmsBtBeatInfoBean(model);
    }

    @Override
    public List<CmsBtBeatInfoBean> getBeatInfByTaskId(int taskId) {
        return beatInfoDaoExt.selectListByTask(taskId);
    }

    @Override
    public List<Map<String, Object>> getBeatSummary(int taskId) {
        return beatInfoDaoExt.selectSummary(taskId);
    }

    @Override
    public int getCountInFlags(int taskId, BeatFlag... flags) {
        return beatInfoDaoExt.selectCountInFlags(taskId, flags);
    }

    @Override
    public CmsBtBeatInfoBean getBeatInfByNumiid(int taskId, String numIid) {
        return beatInfoDaoExt.selectOneByNumiid(taskId, numIid);
    }

    @Override
    public List<CmsBtBeatInfoBean> getBeatInfByNumiidInOtherTask(int promotion_id, int taskId, String numIid) {
        return beatInfoDaoExt.selectListByNumiidInOtherTask(promotion_id, taskId, numIid);
    }

    @Override
    @VOTransactional
    public int addTasks(List<CmsBtBeatInfoBean> models) {
        return beatInfoDaoExt.insertList(models);
    }

    @Override
    @VOTransactional
    public int updateCode(CmsBtBeatInfoBean model) {
        return beatInfoDaoExt.updateCode(model);
    }

    @Override
    @VOTransactional
    public int importBeatInfo(int taskId, List<CmsBtBeatInfoBean> models) {

        int errorFlag = BeatFlag.CANT_BEAT.getFlag();

        int count1 = beatInfoDaoExt.deleteByTask(taskId);

        int count2 = beatInfoDaoExt.insertList(models);

        int count3 = beatInfoDaoExt.updateNoCodeMessage(taskId, errorFlag, "不能在 Promo 中找到 Code");

        int count4 = beatInfoDaoExt.updateNoNumiidMessage(taskId, errorFlag, "不能在 Promo 中找到 Numiid");

        int count5 = beatInfoDaoExt.updateCodeNotMatchNumiidMessage(taskId, errorFlag, "和 Promo 中的 Code 和 Numiid 不匹配");

        return count1 + count2 + count3 + count4 + count5;
    }

    @Override
    @VOTransactional
    public int updateBeatInfoFlag(CmsBtBeatInfoBean beatInfoModel) {
        return beatInfoDaoExt.updateFlag(beatInfoModel);
    }

    @Override
    @VOTransactional
    public int updateBeatInfoFlag(int taskId, BeatFlag flag, ImageStatus imageStatus, Boolean force, String userName) {
        return beatInfoDaoExt.updateFlags(taskId, flag.getFlag(), imageStatus.getId(), force, userName);
    }
}
