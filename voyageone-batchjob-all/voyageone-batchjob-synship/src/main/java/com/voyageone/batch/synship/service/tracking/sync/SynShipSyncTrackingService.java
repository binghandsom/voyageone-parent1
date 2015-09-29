package com.voyageone.batch.synship.service.tracking.sync;

import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 运单同步任务，同步运单到平台
 * <br />
 * Created by Jack on 15/8/1.
 */
@Service
public class SynShipSyncTrackingService extends SynShipSyncTrackingBaseService {

    @Autowired
    private SynShipSyncToTaobaoSubService synShipSyncToTaobaoSubService;

    @Autowired
    private SynShipSyncToJingDongSubService synShipSyncTJingDongSubService;

    @Autowired
    private SynShipSyncToJuMeiSubService synShipSyncTJuMeiSubService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        List<ShopBean> shopBeans = ShopConfigs.getShopList();

        if (shopBeans == null || shopBeans.size() < 1)
            return;

        // 同步任务
        List<Runnable> threads = new ArrayList<>();

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.row_count);

        final int intRowCount = Integer.valueOf(com.voyageone.common.util.StringUtils.isNullOrBlank2(row_count)? "1":row_count);

        // 检查并同步销售商的前端库存
        for (final ShopBean shopBean : shopBeans) {

            final PlatFormEnums.PlatForm platForm = PlatFormEnums.PlatForm.getValueByID(shopBean.getPlatform_id());

            if (platForm == null) {
                logFailRecord("没有找到对应的 Cart", shopBean);
                continue;
            }

            // 为需要的店铺，创建同步任务
            threads.add(() -> {

                List<TrackingSyncBean> trackingSyncBeans = new ArrayList<>();

                String  sim_shipping = ShopConfigs.getVal1(shopBean, ShopConfigEnums.Name.sim_shipping);
                $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")模拟发货标志位：" + sim_shipping);

                // 根据平台，调用相应的 抽出方法 （淘宝需要NumID更新）
                switch (platForm) {
                    case TM:
                    case JD:
                    case CN:
                        break;
                    case JM:

                        // 需要模拟时，全件取得，不需要，按实际取得模拟
                        if (sim_shipping.equals(ShopConfigEnums.Sim.YES.getIs())) {
                            trackingSyncBeans =
                                    trackingDao.getSimSyncTracking(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), intRowCount);
                        } else {
                            trackingSyncBeans =
                                    trackingDao.getSyncTracking(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), intRowCount);
                        }

                        break;
                }


                if (!needSync(shopBean)) {

                    $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")不需要同步运单");

                    // 不需要同步的，则直接转为忽略
                    for (TrackingSyncBean trackingSyncBean : trackingSyncBeans)

                        UpdateSyncTracking(trackingSyncBean, SynshipConstants.SEND_TRACKING_FLG.IGNORE);

                    return;
                }

                try {
                    // 根据平台，调用相应的 api 同步获取的内容
                    switch (platForm) {
                        case TM:
//                            synShipSyncToTaobaoSubService.syncTaobao(trackingSyncBeans, shopBean);
                            break;
                        case JD:
//                            synShipSyncTJingDongSubService.syncJingdong(trackingSyncBeans, shopBean);
                            break;
                        case CN:
                            break;
                        case JM:
                            synShipSyncTJuMeiSubService.syncJuMei(trackingSyncBeans, shopBean);
                            break;
                    }
                } catch (Exception e) {
                    logFailRecord(e, shopBean);
                }

            });
        }

        runWithThreadPool(threads, taskControlList);
    }
}
