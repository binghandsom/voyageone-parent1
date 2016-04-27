package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设定活动结束的隔离数据状态为等待还原batch
 *
 * @author jeff.duan
 * @version 0.0.1, 16/3/31
 */
@Service
public class StockWaitingRevertService extends BaseTaskService {

    @Autowired
    private CmsBtStockSeparatePlatformInfoDaoExt cmsBtStockSeparatePlatformInfoDaoExt;

    @Autowired
    private CmsBtStockSeparateItemDaoExt cmsBtStockSeparateItemDaoExt;

    @Autowired
    private CmsBtTaskKucungeliDaoExt cmsBtTaskKucungeliDaoExt;

    @Autowired
    private CmsBtStockSeparateIncrementItemDaoExt cmsBtStockSeparateIncrementItemDaoExt;

    @Autowired
    private CmsBtStockSalesQuantityDaoExt cmsBtStockSalesQuantityDaoExt;

    @Autowired
    private SimpleTransaction simpleTransaction;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "CmsStockWaitingRevertJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        $info("*****CmsStockWaitingRevertJob任务开始*****");

        // 取得系统时间
        String sysTime = DateTimeUtil.getNow();

        // 从隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）取得还原时间已到的隔离任务列表（自动还原标志位为0：未执行自动还原）
        List<Map<String, Object>> tasksList = getWaitingRevertTasks(sysTime);

        simpleTransaction.openTransaction();
        try {
            // 更新隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）自动还原标志位为1：已经执行自动还原
            int cntStockPlatform = updateStockSeparatePlatformInfo(sysTime);
            $info("隔离任务/平台基本信息表 更新件数：" + cntStockPlatform);

            for (Map<String, Object> taskInfo : tasksList) {
                // 渠道id
                String channelId = (String) taskInfo.get("channel_id");
                // 任务id
                String taskId = String.valueOf(taskInfo.get("task_id"));
                // 平台id
                String cartId = String.valueOf(taskInfo.get("cart_id"));

                $info("**********渠道id：" + channelId + "; 任务id：" + taskId + "; 平台id：" + cartId);

                // 取得库存隔离数据表（cms_bt_stock_separate_item）中 需要设定"等待还原"的sku
                List<String> skuList = getSkuList(taskId, cartId);
                $info("取得sku件数:" + skuList.size());

                // 更新库存隔离数据表（cms_bt_stock_separate_item）中 状态为"3：隔离成功"的状态为"5：等待还原"
                int cntStock = updateStockSeparateItem(taskId, cartId);
                $info("库存隔离数据表 更新件数：" + cntStock);

                // 取得任务id对应的增量任务信息
                int cntStockIncrement = 0;
                List<Map<String, Object>> stockIncrementList = getSubTaskInfo(taskId, cartId);
                for (Map<String, Object> stockIncrement : stockIncrementList) {
                    // 更新增量库存隔离数据表（cms_bt_stock_separate_increment_item）中 状态为"3：增量成功"的状态为"5：还原"
                    cntStockIncrement += updateStockSeparateIncrementItem(String.valueOf(stockIncrement.get("sub_task_id")));
                }
                $info("增量库存隔离数据表 更新件数：" + cntStockIncrement);

                // 根据渠道id和平台id，批量更新隔离平台实际销售数据表（cms_bt_stock_sales_quantity）的结束状态为"1：结束"
                int cntStockSales = updateStockSalesQuantity(channelId, cartId, skuList);
                $info("隔离平台实际销售数据表 更新件数：" + cntStockSales);
            }
        }catch (Exception e) {
            $info("*****CmsStockWaitingRevertJob任务异常终了*****");
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();

        $info("*****CmsStockWaitingRevertJob任务结束*****");
    }

    /**
     * 更新隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）自动还原标志位为1：已经执行自动还原
     *
     * @param sysTime 系统时间
     * @return 更新隔离任务/平台基本信息表件数
     */
    private int updateStockSeparatePlatformInfo(String sysTime) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 更新为 1：已经执行自动还原
        sqlParam.put("revertFlg", StockInfoService.AUTO_REVERTED);
        sqlParam.put("modifier", getTaskName());
         // 更新条件
        sqlParam.put("revertTimeWhere", sysTime);
        // 0: 未执行自动还原
        sqlParam.put("revertFlgWhere", StockInfoService.NOT_REVERT);
        return cmsBtStockSeparatePlatformInfoDaoExt.updateStockSeparatePlatform(sqlParam);
    }

    /**
     * 更新库存隔离数据表（cms_bt_stock_separate_item）中 状态为"3：隔离成功"的状态为"5：等待还原"
     *
     * @param taskId 任务id
     * @param cartId 平台id
     * @return  更新库存隔离数据表件数
     */
    private int updateStockSeparateItem(String taskId, String cartId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 状态更新为"5：等待还原"
        sqlParam.put("status", StockInfoService.STATUS_WAITING_REVERT);
        sqlParam.put("modifier", getTaskName());
        // 更新条件
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        // 状态为"3：隔离成功"
        sqlParam.put("statusWhere", StockInfoService.STATUS_SEPARATE_SUCCESS);
        return cmsBtStockSeparateItemDaoExt.updateStockSeparateItem(sqlParam);
    }

    /**
     * 更新增量库存隔离数据表（cms_bt_stock_separate_increment_item）中 状态为"3：增量成功"的状态为"5：还原"
     *
     * @param subTaskId 增量任务id
     * @return  更新增量库存隔离数据表件数
     */
    private int updateStockSeparateIncrementItem(String subTaskId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 状态更新为"5：还原"
        sqlParam.put("status", StockInfoService.STATUS_REVERT);
        sqlParam.put("modifier", getTaskName());
        // 更新条件
        sqlParam.put("subTaskId", subTaskId);
        // 状态为"3：增量成功"
        sqlParam.put("statusWhere", StockInfoService.STATUS_INCREMENT_SUCCESS);
        return cmsBtStockSeparateIncrementItemDaoExt.updateStockSeparateIncrementItem(sqlParam);
    }

    /**
     * 根据渠道id和平台id，批量更新隔离平台实际销售数据表（cms_bt_stock_sales_quantity）的结束状态为"1：结束"
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param skuList sku列表
     * @return 更新隔离平台实际销售数据表件数
     */
    private int updateStockSalesQuantity(String channelId, String cartId, List<String> skuList) {

        int updateCnt = 0;
        for(int i = 0; i < skuList.size(); i+= 500) {
            int toIndex = i + 500;
            if (toIndex > skuList.size()) {
                toIndex = skuList.size();
            }
            // 分割执行sku列表
            List newList = skuList.subList(i, toIndex);
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            // 结束状态更新为"1：结束"
            sqlParam.put("endFlg", StockInfoService.END);
            sqlParam.put("modifier", getTaskName());
            // 更新条件
            sqlParam.put("channelId", channelId);
            sqlParam.put("cartId", cartId);
            sqlParam.put("skuList", newList);
            int cnt = cmsBtStockSalesQuantityDaoExt.updateStockSalesQuantity(sqlParam);
            updateCnt += cnt;
        }
        return updateCnt;
    }

    /**
     * 从隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）取得还原时间已到的隔离任务列表
     *
     * @param sysTime 系统时间
     * @return 还原时间已到的隔离任务列表
     */
    private List<Map<String, Object>> getWaitingRevertTasks(String sysTime) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("revertTime", sysTime);
        // 0: 未执行自动还原
        sqlParam.put("revertFlg", StockInfoService.NOT_REVERT);
        List<Map<String, Object>> tasksList = cmsBtStockSeparatePlatformInfoDaoExt.selectStockSeparatePlatform(sqlParam);
        return  tasksList;
    }

    /**
     * 取得库存隔离数据表（cms_bt_stock_separate_item）中 需要设定"等待还原"的sku
     *
     * @param taskId 任务id
     * @param cartId 平台id
     * @return sku列表
     */
    private List<String> getSkuList(String taskId, String cartId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        sqlParam.put("statusNotEmpty", true);
        List<Map<String, Object>> stockList = cmsBtStockSeparateItemDaoExt.selectStockSeparateItem(sqlParam);
        List<String> skuList = new ArrayList<String>();
        for (Map<String, Object>  stock : stockList) {
            skuList.add((String) stock.get("sku"));
        }

        return  skuList;
    }

    /**
     * 取得任务id对应的增量任务id
     *
     * @param taskId 任务id
     * @param cartId 平台id
     * @return 增量任务id
     */
    private List<Map<String, Object>> getSubTaskInfo(String taskId, String cartId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        List<Map<String, Object>> stockIncrementList = cmsBtTaskKucungeliDaoExt.selectStockSeparateIncrementTask(sqlParam);
        return stockIncrementList;
    }


}
