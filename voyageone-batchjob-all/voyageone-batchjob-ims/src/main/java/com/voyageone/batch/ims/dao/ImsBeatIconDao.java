package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.bean.BeatIconBean;
import com.voyageone.batch.ims.enums.BeatFlg;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * ims 的 ActivityBeatIcon 和 CartActivityBeatIcon 的数据库操作类
 * <p>
 * Created by Jonas on 7/16/15.
 */
@Repository
public class ImsBeatIconDao extends BaseDao {
    @Override
    protected String namespace() {
        return "com.voyageone.ims.sql";
    }

    /**
     * 获取状态为 beatFlg 的，算上提前量后，活动正在进行的记录。
     *
     * @param limit   限定的行数
     * @param beatFlg 任务的执行阶段
     * @param advance 活动时间计算的提前量（单位为小时）
     * @return 价格披露的完整信息
     */
    public List<BeatIconBean> getLimitedBeatIcons(int limit, BeatFlg beatFlg, int advance) {

        return selectList("voyageone_ims_ims_bt_x_beat_icon_getLimitedBeatIcons", parameters(
                "limit", limit,
                "beat_flg", beatFlg.value(),
                "begin", DateTimeUtil.addMinutes(new Date(), -advance),
                "end", new Date()
        ));
    }

    /**
     * 获取已经 passed 并且活动时间已经结束的记录
     *
     * @param limit 限定的行数
     * @return 价格披露的完整信息
     */
    public List<BeatIconBean> getLimitedPassedBeatIcons(int limit) {

        return selectList("voyageone_ims_ims_bt_x_beat_icon_getLimitedPassedBeatIcons", parameters(
                "limit", limit,
                "beat_flg", BeatFlg.Passed.value(),
                "now", new Date()
        ));

    }

    /**
     * 更新任务的执行阶段标识
     *
     * @param beatIconBean 价格披露任务
     * @return 影响的行数
     */
    public int updateFlg(BeatIconBean beatIconBean) {

        return update("voyageone_ims_ims_bt_x_beat_icon_updateFlg", beatIconBean);
    }
}
