package com.voyageone.batch.synship.service.tracking.sync;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.dao.TrackingDao;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 为同步运单的 主服务 和 子服务，提供统一的辅助功能
 * <br />
 * Created by Jack on 15/8/1.
 */
public abstract class SynShipSyncTrackingBaseService extends BaseTaskService {

    @Autowired
    protected TrackingDao trackingDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynShipSyncTrackingJob";
    }

    @Override
    protected abstract void onStartup(List<TaskControlBean> taskControlList) throws Exception;

    /**
     * 如果 send_tracking 配置正是字符串 0。则说明需要同步，否则都不同步
     *
     * @param shopBean 店铺
     * @return boolean
     */
    protected boolean needSync(ShopBean shopBean) {
        String value = ShopConfigs.getVal1(shopBean, ShopConfigEnums.Name.send_tracking);

        return value.equals(ShopConfigEnums.Sent.YES.getIs());
    }

    /**
     * 更新成功记录
     */
    protected void UpdateSyncTracking(TrackingSyncBean trackingSyncBean, String sendTrackingFlg) {
        trackingSyncBean.setSend_tracking_flg(sendTrackingFlg);
        trackingSyncBean.setUpdate_time(DateTimeUtil.getNow());
        trackingSyncBean.setUpdate_person(getTaskName());

        trackingDao.UpdateSyncTracking(trackingSyncBean);
    }

    /**
     * 记录失败的记录
     *
     * @param e       失败的异常
     * @param attJson 附加的信息
     */
    protected void logFailRecord(Exception e, Object attJson) {
        String json = attJson == null ? "" : new Gson().toJson(attJson);
        issueLog.log(e, ErrorType.BatchJob, getSubSystem(), json);
    }

    /**
     * 记录失败的行记录
     */
    protected void logFailRecord(String msg, Object attJson) {
        String json = attJson == null ? "" : new Gson().toJson(attJson);
        issueLog.log(getTaskName(), msg, ErrorType.BatchJob, getSubSystem(), json);
    }
}
