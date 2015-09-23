package com.voyageone.batch.synship.service.tracking.sync;

import com.google.gson.Gson;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.internal.JSON.JSON;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import com.voyageone.common.components.cn.CnInventoryService;
import com.voyageone.common.components.cn.beans.InventoryUpdateBean;
import com.voyageone.common.components.cn.enums.InventorySynType;
import com.voyageone.common.components.jumei.Bean.SetShippingReq;
import com.voyageone.common.components.jumei.JumeiService;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 运单同步的子服务，用于提供同步到独立域名的功能
 * <br />
 * Created by Jack on 15/8/1.
 */
@Service
public class SynShipSyncToJuMeiSubService extends SynShipSyncTrackingBaseService {

    @Autowired
    private JumeiService jumeiService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JM 的内容。但暂时搁置
    }

    public void syncJuMei(List<TrackingSyncBean> trackingSyncBeans, ShopBean shopBean) {

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步开始");
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单需要同步件数："+ trackingSyncBeans.size());

        String  tracking_type = ShopConfigs.getVal1(shopBean, ShopConfigEnums.Name.tracking_type);

        for (TrackingSyncBean trackingSyncBean : trackingSyncBeans) {

            syncJuMei(trackingSyncBean, shopBean, tracking_type);
        }

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步结束");

    }

    private void syncJuMei(TrackingSyncBean trackingSyncBean, ShopBean shopBean, String tracking_type) {

         String res = null;

        try {
            SetShippingReq req = new SetShippingReq();
            // 订单号
            req.setOrder_id(trackingSyncBean.getSource_order_id());

            // 快递公司(Synship时，同步Synship对应的快递公司ID，否则同步实际快递公司的ID)
            if (tracking_type.equals(ShopConfigEnums.Type.Synship.getIs())) {
                req.setLogistic_id(ShopConfigs.getVal2(shopBean, ShopConfigEnums.Name.logistics, tracking_type));
                // 快递单号
                req.setLogistic_track_no(trackingSyncBean.getSyn_ship_no());
            } else {
                req.setLogistic_id(ShopConfigs.getVal2(shopBean, ShopConfigEnums.Name.logistics, trackingSyncBean.getTracking_type()));
                // 快递单号
                req.setLogistic_track_no(trackingSyncBean.getTracking_no());
            }

            logger.info("----------" + shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步记录：" + new Gson().toJson(req));

            res = jumeiService.setShipping(shopBean, req);

            logger.info("----------" + shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步结果：" + res);
        } catch (Exception e) {
            logFailRecord(e, trackingSyncBean);
            return;
        }

        // 有返回内容，但没有错误代码，就认为是成功调用
        if (res != null) {

            Map<String, Object> resultMap = JsonUtil.jsonToMap(res);

            //  0 处理正确(已发货的订单再次调用此接口, 更改快递信息,更改成功后也会返回 0)
            if ("0".equals(resultMap.get("error"))) {
                // 成功后，更新标志位
                UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.SENDED);
                return;
            }
            // 2 message:no exchange 说明订单已经发货再次调用，订单单发货的快递信息无变化
            else if ("2".equals(resultMap.get("error"))) {
                // 成功后，更新标志位
                UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.SENDED);
                return;
            }else {
                // 失败时，更新标志位
                UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.IGNORE);
                // 失败的话，记录失败的信息
                logFailRecord(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步失败，错误：" + resultMap.get("result"), trackingSyncBean);
                return;
            }
        }

        logFailRecord(shopBean.getShop_name() + "（" + shopBean.getComment() + ")运单同步失败，错误未知", trackingSyncBean);
    }
}
