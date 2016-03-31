package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.*;
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
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementTaskDao cmsBtStockSeparateIncrementTaskDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

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

        // 取得系统时间
        String sysTime = DateTimeUtil.getNow();

        // 从隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）取得还原时间已到的隔离任务列表（自动还原标志位为0：未执行自动还原）
        List<Map<String, Object>> tasksList = getWaitingRevertTasks(sysTime);

        simpleTransaction.openTransaction();
        try {
            // 更新隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）自动还原标志位为1：已经执行自动还原
            updateStockSeparatePlatformInfo(sysTime);

            for (Map<String, Object> taskInfo : tasksList) {
                // 渠道id
                String channelId = (String) taskInfo.get("channel_id");
                // 任务id
                String taskId = String.valueOf(taskInfo.get("task_id"));
                // 平台id
                String cartId = String.valueOf(taskInfo.get("cart_id"));

                $info("渠道id：" + channelId + "任务id：" + taskId + "平台id：" + cartId);

                // 取得库存隔离数据表（cms_bt_stock_separate_item）中 需要设定"等待还原"的sku
                List<String> skuList = getSkuList(taskId, cartId);

                // 更新库存隔离数据表（cms_bt_stock_separate_item）中 状态为"3：隔离成功"的状态为"5：等待还原"
                updateStockSeparateItem(taskId, cartId);

                // 取得任务id对应的增量任务id
                String subTaskId = getSubTaskId(taskId, cartId);
                if (!StringUtils.isEmpty(subTaskId)) {
                    // 更新增量库存隔离数据表（cms_bt_stock_separate_increment_item）中 状态为"3：增量成功"的状态为"5：还原"
                    updateStockSeparateIncrementItem(subTaskId);
                }
                // 根据渠道id和平台id，批量更新隔离平台实际销售数据表（cms_bt_stock_sales_quantity）的结束状态为"5：还原"
            }


        }catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 更新隔离任务/平台基本信息表（cms_bt_stock_separate_platform_info）自动还原标志位为1：已经执行自动还原
     *
     * @param sysTime 系统时间
     */
    private void updateStockSeparatePlatformInfo(String sysTime) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 更新为 1：已经执行自动还原
        sqlParam.put("revertFlg", StockInfoService.AUTO_REVERTED);
         // 更新条件
        sqlParam.put("revertTime", sysTime);
        // 0: 未执行自动还原
        sqlParam.put("revertFlgWhere", StockInfoService.NOT_REVERT);
        int cnt = cmsBtStockSeparatePlatformInfoDao.updateStockSeparatePlatform(sqlParam);
        $info("隔离任务/平台基本信息表 更新件数：" + cnt);
        return;
    }

    /**
     * 更新库存隔离数据表（cms_bt_stock_separate_item）中 状态为"3：隔离成功"的状态为"5：等待还原"
     *
     * @param taskId 任务id
     * @param cartId 平台id
     */
    private void updateStockSeparateItem(String taskId, String cartId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 状态更新为"5：等待还原"
        sqlParam.put("status", StockInfoService.STATUS_WAITING_REVERT);
        // 更新条件
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        // 状态为"3：隔离成功"
        sqlParam.put("statusWhere", StockInfoService.STATUS_SEPARATE_SUCCESS);
        int cnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam);
        $info("库存隔离数据表 更新件数：" + cnt);
        return;
    }

    /**
     * 更新增量库存隔离数据表（cms_bt_stock_separate_increment_item）中 状态为"3：增量成功"的状态为"5：还原"
     *
     * @param subTaskId 增量任务id
     */
    private void updateStockSeparateIncrementItem(String subTaskId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 状态更新为"5：还原"
        sqlParam.put("status", StockInfoService.STATUS_REVERT);
        // 更新条件
        sqlParam.put("subTaskId", subTaskId);
        // 状态为"3：增量成功"
        sqlParam.put("statusWhere", StockInfoService.STATUS_INCREMENT_SUCCESS);
        int cnt = cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam);
        $info("增量库存隔离数据表 更新件数：" + cnt);
        return;
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
        List<Map<String, Object>> tasksList = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
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
        List<Map<String, Object>> stockList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
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
    private String getSubTaskId(String taskId, String cartId) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        List<Map<String, Object>> stockList = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(sqlParam);
        if (stockList.size() > 0) {
            return String.valueOf(stockList.get(0).get("sub_task_id"));
        } else {
            return "";
        }
    }


}
