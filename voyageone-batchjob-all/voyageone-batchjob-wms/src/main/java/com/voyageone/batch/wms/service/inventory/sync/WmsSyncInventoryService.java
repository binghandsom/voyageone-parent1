package com.voyageone.batch.wms.service.inventory.sync;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
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

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        List<ShopBean> shopBeans = ShopConfigs.getShopList();

        if (shopBeans == null || shopBeans.size() < 1)
            return;

        // 同步任务
        List<Runnable> threads = new ArrayList<>();

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.row_count);

        int intRowCount = Integer.valueOf(StringUtils.isNullOrBlank2(row_count)? "1":row_count);

        // 线程数
        String thread_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.thread_count);
        int THREAD_COUNT = Integer.valueOf(StringUtils.isNullOrBlank2(thread_count)? "1":thread_count);
        int limit = THREAD_COUNT * intRowCount;

        // 重试时间间隔
        String retry_time_interval =  TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.retry_time_interval);

        if (StringUtils.isNullOrBlank2(retry_time_interval)) {
            retry_time_interval = "4";
        }

        String process_time = DateTimeUtil.getLocalTime(DateTimeUtil.getDate(), Integer.valueOf(retry_time_interval) * -1);

        // 检查并同步销售商的前端库存
        for (final ShopBean shopBean : shopBeans) {

            final PlatFormEnums.PlatForm platForm = PlatFormEnums.PlatForm.getValueByID(shopBean.getPlatform_id());

            if (platForm == null) {
                logFailRecord("没有找到对应的 Cart", shopBean);
                continue;
            }

            if (!needSync(shopBean)) {
                $info(shopBean.getShop_name() + "（" + shopBean.getComment() + "） 不需要同步库存");
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
                                inventoryDao.getInventorySynLogForTM(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), limit);
                        break;
                    case JD:
                    case CN:
                        inventorySynLogBeans =
                                inventoryDao.getInventorySynLog(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), limit);
                        break;
                    case JM:
                        // 取得刚上新但没库存同步过的记录（由于为了能够自动审核，聚美上新时库存都是设置为1）
                        inventorySynLogBeans =
                                inventoryDao.getInventorySynLogForJMReFlush(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), WmsConstants.SYN_FLG.INITAL,process_time, limit);

                        // 库存刷新失败记录取得
                        List<InventorySynLogBean> inventorySynIgnores=
                                inventoryDao.getInventorySynLogForJMReFlush(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), WmsConstants.SYN_FLG.IGONRE, process_time, limit);
                        inventorySynLogBeans.addAll(inventorySynIgnores);

                        // 如果不存在需刷新的记录时，则取得有库存变化的记录
                        if (inventorySynLogBeans.size() == 0) {
                            inventorySynLogBeans =
                                    inventoryDao.getInventorySynLogForJM(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id(), limit);

                        }else {
                            updateFlg = WmsConstants.UPDATE_FLG.ReFlush;
                        }
                        break;
                }

                List<Runnable> runnable = new ArrayList<>();

                int total = inventorySynLogBeans.size();

                //按线程平分件数
                int split = total/THREAD_COUNT;
                split = split + THREAD_COUNT;

                $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")需要处理的总件数：" + total + "，线程数：" + THREAD_COUNT + "，平分件数：" + split);

                // 将集合拆分到线程
                for (int i = 0; i < THREAD_COUNT; i++) {
                    int start = i * split;
                    int end = start + split;

                    if (end >= total) {
                        end = total;
                        // 如果已经不足够了。说明就算后续还可以开线程，但是已经不需要了。
                        // 标记 i 为最大，结束 for 循环
                        i = THREAD_COUNT;
                    }

                    List<InventorySynLogBean> subList = inventorySynLogBeans.subList(start, end);

                    final String finalUpdateFlg = updateFlg;
                    runnable.add(() -> {

                        try {
                            // 根据平台，调用相应的 api 同步获取的内容
                            switch (platForm) {
                                case TM:
                                    wmsSyncToTaobaoSubService.syncTaobao(subList, shopBean);
                                    break;
                                case JD:
                                    wmsSyncToJingDongSubService.syncJingdong(subList, shopBean);
                                    break;
                                case CN:
                                    wmsSyncToCnSubService.syncSite(subList, shopBean);
                                    break;
                                case JM:
                                    wmsSyncToJuMeiSubService.syncJumei(subList, shopBean, finalUpdateFlg);
                                    break;
                            }
                        } catch (Exception e) {
                            logFailRecord(e, shopBean);
                        }
                    });
                }

                $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")生成的子线程任务数：" + runnable.size());

                try {
                    runWithThreadPool(runnable, taskControlList);
                } catch (Exception e) {
                    $info(shopBean.getShop_name() + "（" + shopBean.getComment() + "子线程错误：" + e);
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
