package com.voyageone.batch.synship.service.tracking.sync;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.response.order.OverseasOrderSopDeliveryResponse;
import com.jd.open.api.sdk.response.order.OverseasOrderSopOutstorageResponse;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import com.voyageone.common.components.jd.JdInventoryService;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运单同步的子服务，用于提供同步到京东的功能
 * <br />
 * Created by Jack on 15/8/1.
 */
@Service
public class SynShipSyncToJingDongSubService extends SynShipSyncTrackingBaseService {

    @Autowired
    private JdInventoryService jdInventoryService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    public void syncJingdong(List<TrackingSyncBean> trackingSyncBeans, ShopBean shopBean) {

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步开始");
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单需要同步件数："+ trackingSyncBeans.size());
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步暂时在旧synShip任务中做成，这里不做处理");

        for (TrackingSyncBean trackingSyncBean : trackingSyncBeans) {

//            syncJingdong(trackingSyncBean, shopBean);
        }

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步结束");
    }

    private void syncJingdong(TrackingSyncBean trackingSyncBean, ShopBean shopBean) {

        OverseasOrderSopOutstorageResponse res = null;
//        OverseasOrderSopDeliveryResponse res = null;

//        try {
//            res = jdInventoryService
//        } catch (JdException e) {
//            logFailRecord(e, trackingSyncBean);
//        }

        // 有返回内容，但没有错误代码，就认为是成功调用
        if (res != null) {

            if ("0".equals(res.getCode())) {
                // 成功后，更新标志位
                UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.SENDED);
                return;
            }

            String failMessage = String.format("msg: %s; desc: %s;",
                    res.getMsg(),
                    res.getZhDesc());

            // 失败的话，记录失败的信息
            logFailRecord(failMessage, trackingSyncBean);
            return;
        }

        logFailRecord(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步失败，错误未知", trackingSyncBean);
    }
}
