package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 增量库存隔离batch
 *
 * @author jeff.duan
 * @version 0.0.1, 16/3/31
 */
@Service
public class StockIncrementSeparateService extends BaseTaskService {


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
        return "CmsStockIncrementSeparateJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        $info("*****CmsStockIncrementSeparateJob任务开始*****");
        // 从增量库存隔离数据表（cms_bt_stock_separate_increment_item）取得状态为"1:等待增量"的数据
        List<Map<String, Object>> stockIncrementList = getStockSeparateIncrementData();

        // 按渠道id,整理Map<channelId, List<stockIncrementData>>
        Map<String, List<Map<String, Object>>> stockIncrementMapByChannel = new HashMap<>();

        for (Map<String, Object> stockIncrement : stockIncrementList) {
            String channelId = (String) stockIncrement.get("channel_id");

            if (stockIncrementMapByChannel.containsKey(channelId)) {
                stockIncrementMapByChannel.get(channelId).add(stockIncrement);
            } else {
                List<Map<String, Object>> stockIncrementListByChannel = new ArrayList<>();
                stockIncrementListByChannel.add(stockIncrement);
                stockIncrementMapByChannel.put(channelId, stockIncrementListByChannel);
            }
        }

        // 按渠道id更新等待增量的数据
        for (String channelId : stockIncrementMapByChannel.keySet()) {
            executeByChannel(channelId, stockIncrementMapByChannel.get(channelId));
        }




        $info("*****CmsStockIncrementSeparateJob任务结束*****");

    }

    /**
     * 按渠道id更新等待增量的数据
     *
     * @param channelId   渠道id
     * @param stockIncrementList 渠道id对应的增量数据列表
     */
    private void executeByChannel(String channelId, List<Map<String, Object>> stockIncrementList) {
        // 取得渠道id 对应的所有销售平台
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
        List<String> listCartId = new ArrayList<>();
        cartList.forEach(cartInfo -> listCartId.add(cartInfo.getValue()));

        // 增量任务id列表
        Set<String> subTaskIdList = new HashSet<String>();
        for (Map<String, Object> stockIncrement : stockIncrementList) {
            String subTaskId = (String) stockIncrement.get("sub_task_id");
            subTaskIdList.add(subTaskId);
        }

        // 根据增量任务id列表，取得状态为"隔离成功"的隔离数据的隔离库存值，生成Map<sku + cartId, 隔离库存值>
        // 部分平台的Api接口未提供增量的接口，刷增量库存的时候，需要加算隔离库存值
        Map<String, Integer> stockInfoMap = new HashMap<>();
        List<Map<String, Object>> stockList = getStockSeparateData(subTaskIdList);
        for (Map<String, Object> stock : stockList) {
            String sku = (String) stock.get("sku");
            String cartId = String.valueOf(stock.get("cart_id"));
            Integer separateQty = (Integer) stock.get("separate_qty");
            stockInfoMap.put(sku + cartId, separateQty);
        }
    }


    /**
     * 从增量库存隔离数据表（cms_bt_stock_separate_increment_item）取得状态为"1:等待增量"的数据
     *
     * @return 等待增量的数据
     */
    private List<Map<String, Object>> getStockSeparateIncrementData() {
        List<Map<String, Object>> stockIncrementList = null;
        return  stockIncrementList;
    }

    /**
     * 根据增量任务id列表，取得状态为"隔离成功"的隔离数据列表
     *
     * @param subTaskIdList 增量任务id列表
     * @return 状态为"隔离成功"的隔离数据列表
     */
    private List<Map<String, Object>> getStockSeparateData(Set<String> subTaskIdList) {
        List<Map<String, Object>> stockList = null;
        return  stockList;
    }
}
