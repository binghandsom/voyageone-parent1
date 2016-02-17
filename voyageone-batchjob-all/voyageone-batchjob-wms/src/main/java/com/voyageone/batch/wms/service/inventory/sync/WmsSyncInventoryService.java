package com.voyageone.batch.wms.service.inventory.sync;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存同步任务，同步库存变动到独立域名，和其他第三方
 * <br />
 * Created by jonas on 15/6/1.
 */
@Service
public class WmsSyncInventoryService extends WmsSyncInventoryBaseService {

    @Autowired
    private WmsSyncToTaobaoSubService wmsSyncToTaobaoSubService;

    @Autowired
    private WmsSyncToJingDongSubService wmsSyncToJingDongSubService;

    @Autowired
    private WmsSyncToCnSubService wmsSyncToCnSubService;

    @Autowired
    private WmsSyncToJuMeiSubService wmsSyncToJuMeiSubService;

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

                // 过滤不需要更新的SKU（如赠品SKU等\增量更新时例外）
                List<InventorySynLogBean> inventoryExceptSynLogBeans =
                        inventoryDao.getInventoryExceptSynLog(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id());

                for (InventorySynLogBean inventorySynLogBean : inventoryExceptSynLogBeans) {
                    moveIgnore(inventorySynLogBean, "该 SKU 不需要更新");
                }

                List<InventorySynLogBean> inventorySynLogBeans = new ArrayList<>();

                // 更新标志位
                String updateFlg = WmsConstants.UPDATE_FLG.Update;

                // 根据平台，调用相应的 抽出方法 （淘宝需要NumID更新；聚美需要存在上新记录）
                switch (platForm) {
                    case TM:
                        inventorySynLogBeans =
                                inventoryDao.getInventorySynLogForTM(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), intRowCount);
                        break;
                    case JD:
                    case CN:
                        inventorySynLogBeans =
                                inventoryDao.getInventorySynLog(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), intRowCount);
                        break;
                    case JM:
                        // 取得刚上新但没库存同步过的记录（由于为了能够自动审核，聚美上新时库存都是设置为1）
                        inventorySynLogBeans =
                                inventoryDao.getInventorySynLogForJMReFlush(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), intRowCount);
                        // 如果不存在需刷新的记录时，则取得有库存变化的记录
                        if (inventorySynLogBeans.size() == 0) {
                            inventorySynLogBeans =
                                    inventoryDao.getInventorySynLogForJM(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), intRowCount);
                        }else {
                            updateFlg = WmsConstants.UPDATE_FLG.ReFlush;
                        }
                        break;
                }

                if (!needSync(shopBean) && updateFlg.equals(WmsConstants.UPDATE_FLG.Update)) {

                    // 不需要同步的，则直接转为忽略
                    for (InventorySynLogBean inventorySynLogBean : inventorySynLogBeans)

                        moveIgnore(inventorySynLogBean, "该 Cart 不需要更新");

                    return;
                }

                try {
                    // 根据平台，调用相应的 api 同步获取的内容
                    switch (platForm) {
                        case TM:
                            wmsSyncToTaobaoSubService.syncTaobao(inventorySynLogBeans, shopBean);
                            break;
                        case JD:
                            wmsSyncToJingDongSubService.syncJingdong(inventorySynLogBeans, shopBean);
                            break;
                        case CN:
                            wmsSyncToCnSubService.syncSite(inventorySynLogBeans, shopBean);
                            break;
                        case JM:
                            wmsSyncToJuMeiSubService.syncJumei(inventorySynLogBeans, shopBean, updateFlg);
                            break;
                    }
                } catch (Exception e) {
                    logFailRecord(e, shopBean);
                }

                // 清除历史库存记录
                List<InventorySynLogBean> inventoryHistorySynLogBeans =
                        inventoryDao.getInventoryHistorySynLog(shopBean.getOrder_channel_id(),shopBean.getCart_id(), intRowCount);

                logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")清除历史库存记录件数："+inventoryHistorySynLogBeans.size());

                for (InventorySynLogBean inventorySynLogBean : inventoryHistorySynLogBeans) {
                    inventoryDao.deleteInventorySynLog(inventorySynLogBean);
                }

            });
        }

        runWithThreadPool(threads, taskControlList);
    }
}
