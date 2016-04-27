package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.daoext.cms.CmsBtBeatInfoDaoExt;
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
    private CmsBtBeatInfoDaoExt beatInfoDao;

    /**
     * 获取需要处理的价格披露数据
     *
     * @return CmsBtBeatInfoModel 集合
     */
    public List<CmsBtBeatInfoBean> getNeedBeatData(int limit) {
        return beatInfoDao.selectListNeedBeatFullData(limit);
    }

    public int saveFlagAndMessage(CmsBtBeatInfoBean beatInfoModel) {
        return beatInfoDao.updateFlagAndMessage(beatInfoModel);
    }
}
