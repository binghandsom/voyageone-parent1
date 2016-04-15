package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.service.dao.cms.CmsBtBeatInfoDao;
import com.voyageone.service.model.cms.CmsBtBeatInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jonasvlag on 16/3/4.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsBeatInfoService {

    @Autowired
    private CmsBtBeatInfoDao beatInfoDao;

    /**
     * 获取需要处理的价格披露数据
     *
     * @return CmsBtBeatInfoModel 集合
     */
    public List<CmsBtBeatInfoModel> getNeedBeatData(int limit) {
        return beatInfoDao.selectListNeedBeatFullData(limit);
    }

    public int saveFlagAndMessage(CmsBtBeatInfoModel beatInfoModel) {
        return beatInfoDao.updateFlagAndMessage(beatInfoModel);
    }
}
