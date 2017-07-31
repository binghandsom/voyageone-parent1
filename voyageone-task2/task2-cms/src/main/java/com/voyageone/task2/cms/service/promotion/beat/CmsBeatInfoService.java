package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.daoext.cms.CmsBtBeatInfoDaoExt;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
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
     * 获取需要处理的价格披露数据。
     *
     * @return CmsBtBeatInfoModel 集合
     */
    List<CmsBtBeatInfoBean> getNeedBeatData(int limit, List<Integer> cartIds) {
        // 逻辑内指定了三个 flag。表示三种需要处理的场景。
        // BEATING 时, 当现在的时间正处于任务有效时间内。则需要上传图片。
        // REVERT 时, 表示强制或手动还原。
        // SUCCESS 时, 表示当现在时间已超过任务有效时间。则需要还原图片。
        return beatInfoDao.selectListNeedBeatFullData(
                limit,
                BeatFlag.BEATING.getFlag(),
                BeatFlag.REVERT.getFlag(),
                BeatFlag.SUCCESS.getFlag(),
                DateTimeUtilBeijing.getCurrentBeiJingDate(),
                cartIds);
    }

    int saveFlagAndMessage(CmsBtBeatInfoBean beatInfoModel) {
        return beatInfoDao.updateFlagAndMessage(beatInfoModel);
    }
}
