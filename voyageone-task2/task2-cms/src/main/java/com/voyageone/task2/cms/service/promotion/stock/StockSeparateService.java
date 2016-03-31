package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.service.dao.cms.CmsBtBeatInfoDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.model.cms.CmsBtBeatInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存隔离batch
 *
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Service
public class StockSeparateService extends BaseTaskService {

    @Autowired
    private StockInfoService stockInfoService;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private TransactionRunner transactionRunner;

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
        return "CmsStockSeparateJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("status", stockInfoService.STATUS_WAITING_SEPARATE);
        $info("开始取得等待隔离数据");
        List<Map<String, Object>> resultData = cmsBtStockSeparateItemDao.selectStockSeparateItem(param);
        $info("等待隔离数据取得完毕");
        // 按渠道整理Map<channelId, resultData>
        Map<String, List<Map<String, Object>>> resultDataByChannel = new HashMap<>();
        for (Map<String, Object> rowData : resultData) {
            String channelId = (String) rowData.get("channel_id");
            if (resultDataByChannel.containsKey(channelId)) {
                resultDataByChannel.get(channelId).add(rowData);
            } else {
                List<Map<String, Object>> listData = new ArrayList<>();
                listData.add(rowData);
                resultDataByChannel.put(channelId, listData);
            }
        }

        for (String channelId : resultDataByChannel.keySet()) {
            executeByChannel(channelId, resultDataByChannel.get(channelId));
        }

    }

    /**
     * 按渠道把等待隔离数据进行更新
     *
     * @param channelId   渠道id
     * @param listSkuData 该渠道下的sku数据
     */
    private void executeByChannel(String channelId, List<Map<String, Object>> listSkuData) {
        // 渠道对应的所有销售平台取得


        // 取得可用库存
        $info("开始取得可用库存数据");
        Map<String,Integer> skuStockUsableAll = stockInfoService.getUsableStock(channelId);
        $info("可用库存数据取得完毕");

        $info("更新处理开始,渠道是%s", channelId);
        try {
            transactionRunner.runWithTran(() -> {

            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            logIssue("cms 库存隔离batch", "渠道是"+ channelId +"的等待隔离数据更新失败. " + e.getMessage());
        }
        $info("更新处理结束,渠道是%s", channelId);
    }
}
