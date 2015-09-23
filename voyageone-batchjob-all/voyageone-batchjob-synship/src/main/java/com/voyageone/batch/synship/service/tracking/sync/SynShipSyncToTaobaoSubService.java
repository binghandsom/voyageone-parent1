package com.voyageone.batch.synship.service.tracking.sync;

import com.taobao.api.response.LogisticsOnlineSendResponse;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import com.voyageone.common.components.tmall.TbInventoryService;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *运单同步的子服务，用于提供同步到淘宝或天猫的功能
 * <br />
 * Created by Jack on 15/8/1.
 */
@Service
public class SynShipSyncToTaobaoSubService extends SynShipSyncTrackingBaseService {

    @Autowired
    private TbInventoryService tbInventoryService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 TM 的内容。但暂时搁置
    }

    public void syncTaobao(List<TrackingSyncBean> trackingSyncBeans, ShopBean shopBean) {

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步开始");
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单需要同步件数："+ trackingSyncBeans.size());
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步暂时在旧synShip任务中做成，这里不做处理");

        for (TrackingSyncBean trackingSyncBean : trackingSyncBeans) {

//            syncTaobao(trackingSyncBean, shopBean);

        }
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步结束");
    }

    private void syncTaobao(TrackingSyncBean trackingSyncBean, ShopBean shopBean) {


        LogisticsOnlineSendResponse res = null;
//        LogisticsOfflineSendResponse res = null;

//        try {
//            res = tbInventoryService
//        } catch (ApiException ae) {
//            logFailRecord(ae, trackingSyncBean);
//        } catch (Exception e) {
//            logFailRecord(e, trackingSyncBean);
//            return;
//        }

        if (res == null) return;

        // 有返回内容，但没有错误代码，就认为是成功调用
        if (StringUtils.isEmpty(res.getErrorCode())) {

            // 成功后，更新标志位
            UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.SENDED);
            return;
        }

        switch (res.getSubCode()) {
            // 商品id对应的商品已经被删除
            case "isv.item-is-delete:invalid-numIid-or-iid":
            case "isv.item-is-delete:invalid-numIid-or-iid-tmall":
            // 商品id对应的商品不存在
            case "isv.item-not-exist:invalid-numIid-or-iid":
            case "isv.item-not-exist:invalid-numIid-or-iid-tmall":
            // 传入的sku的属性找不到对应的sku记录
            case "isv.invalid-parameter:sku-properties":
            case "isv.invalid-parameter:sku-properties-tmall":

                UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.IGNORE);

                logFailRecord(res.getSubMsg(), trackingSyncBean);
                break;

            default:
                String failMessage = String.format("code: %s; sub_code: %s; msg: %s; sub_msg: %s;",
                        res.getErrorCode(),
                        res.getSubCode(),
                        res.getMsg(),
                        res.getSubMsg());

                logFailRecord(failMessage, trackingSyncBean);
                break;
        }
    }

}
