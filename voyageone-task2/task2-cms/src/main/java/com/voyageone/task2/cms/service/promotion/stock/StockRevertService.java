package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.daoext.cms.CmsBtStockSeparateItemDaoExt;
import com.voyageone.service.daoext.cms.CmsBtTasksStockDaoExt;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * 库存还原batch
 *
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Service
public class StockRevertService extends BaseTaskService {

    @Autowired
    private StockInfoService stockInfoService;

    @Autowired
    private CmsBtStockSeparateItemDaoExt cmsBtStockSeparateItemDaoExt;

    @Autowired
    private CmsBtTasksStockDaoExt cmsBtTasksStockDaoExt;

    @Autowired
    private TransactionRunner transactionRunner;

    /** 还原件数 */
    private int cntRevert;
    /** 推送件数 */
    private int cntSend;

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
        return "CmsStockRevertJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        cntRevert = 0;
        cntSend = 0;

        Map<String, Object> param = new HashMap<>();
        param.put("status", stockInfoService.STATUS_WAITING_REVERT);
//        param.put("taskId", 10);
//        param.put("channelId", "066");

        $info("开始取得等待还原数据");
        List<Map<String, Object>> resultData = cmsBtStockSeparateItemDaoExt.selectStockSeparateItem(param);
        $info("等待还原数据取得完毕. %d件", resultData.size());

        // 按渠道,sku整理Map<channelId, Map<sku,resultData>>
        Map<String, Map<String, List<Map<String, Object>>>> resultDataByChannel = resultData.stream().collect(
                groupingBy(rowData -> (String) rowData.get("channel_id"), groupingBy(rowData -> (String) rowData.get("sku")))
        );
        // 按渠道整理Map<channelId, Set<隔离平台>>
        Map<String, Set<Integer>> setCartIdByChannel = resultData.stream().collect(
                groupingBy(rowData -> (String) rowData.get("channel_id"), mapping(rowData ->
                        (Integer) rowData.get("cart_id"), toSet())
                )
        );
        // 按渠道整理Map<channelId, Set<隔离任务>>
        Map<String, Set<Integer>> setTaskIdByChannel = resultData.stream().collect(
                groupingBy(rowData -> (String) rowData.get("channel_id"), mapping(rowData ->
                        (Integer) rowData.get("task_id"), toSet())
                )
        );

//        Map<String, Map<String, List<Map<String, Object>>>> resultDataByChannel = new HashMap<>();
//        Map<String, Set<Integer>> setCartIdByChannel = new HashMap<>();
//        Map<String, Set<Integer>> setTaskIdByChannel = new HashMap<>();
//
//        for (Map<String, Object> rowData : resultData) {
//            String channelId = (String) rowData.get("channel_id");
//            String sku = (String) rowData.get("sku");
//            Integer taskId = (Integer) rowData.get("task_id");
//            Integer cartId = (Integer) rowData.get("cart_id");
//
//            // 隔离数据Map put
//            Map<String, List<Map<String, Object>>> mapDataChannel = resultDataByChannel.get(channelId);
//            if (mapDataChannel == null) {
//                mapDataChannel = new HashMap<>();
//                List<Map<String, Object>> listData = new ArrayList<>();
//                listData.add(rowData);
//                mapDataChannel.put(sku, listData);
//                resultDataByChannel.put(channelId, mapDataChannel);
//                setTaskIdByChannel.put(channelId, new HashSet<Integer>(){{this.add(taskId);}});
//                setCartIdByChannel.put(channelId, new HashSet<Integer>(){{this.add(cartId);}});
//            } else {
//                if (mapDataChannel.containsKey(sku)) {
//                    mapDataChannel.get(sku).add(rowData);
//                } else {
//                    List<Map<String, Object>> listData = new ArrayList<>();
//                    listData.add(rowData);
//                    mapDataChannel.put(sku, listData);
//                }
//                setTaskIdByChannel.get(channelId).add(taskId);
//                setCartIdByChannel.get(channelId).add(cartId);
//            }
//        }

        // 按渠道把等待还原数据进行更新
        for (String channelId : resultDataByChannel.keySet()) {
            executeByChannel(channelId, resultDataByChannel.get(channelId), new ArrayList<>(setTaskIdByChannel.get(channelId)), new ArrayList<>(setCartIdByChannel.get(channelId)));
        }

        $info("处理了等待还原状态件数为%d件", cntRevert);
        $info("处理了推送件数为%d件", cntSend);

    }

    /**
     * 按渠道把等待还原数据进行更新
     *
     * @param channelId  渠道id
     * @param mapSkuData Map<sku,resultData>该渠道下的sku数据
     * @param listTaskId 待还原的任务id
     * @param listRevertCartId 待还原的平台
     */
    private void executeByChannel(String channelId, Map<String, List<Map<String, Object>>> mapSkuData, List<Integer> listTaskId, List<Integer> listRevertCartId) {
        // 取得可用库存
        $info("开始取得可用库存数据");
        Map<String,Integer> skuStockUsableAll = stockInfoService.getUsableStock(channelId);
        $info("可用库存数据取得完毕");

        // 渠道对应的所有销售平台取得
        List<Integer> listCartIdAll = new ArrayList<>();
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
        cartList.forEach(cartInfo -> listCartIdAll.add(Integer.parseInt(cartInfo.getValue())));
        // ★注意★：还原数据可能是隔离成功变成还原（活动未开始），也可能是活动结束变成的还原，所以sql根据task的还原时间来抽出的隔离平台是不正确的
        // ★★★    因此以下逻辑把所有待还原的task，平台，都归类到隔离数据下面
        // 取得该渠道下未被隔离的平台(即共享平台)(去除待还原平台)
        List<Integer> listShareCartId = stockInfoService.getShareCartId(channelId, listCartIdAll, listRevertCartId);
        // 取得该渠道下未被还原的平台(即还原平台和共享平台以外的隔离平台)
        List<Integer> listSeparateCartId = new ArrayList<>();
        listSeparateCartId.addAll(listCartIdAll); // 所有平台
        stockInfoService.removeListValue(listSeparateCartId, listRevertCartId); // 去除待还原平台
        stockInfoService.removeListValue(listSeparateCartId, listShareCartId); // 去除共享平台

        // 取得该渠道下隔离中的task
        List<Integer> listSeparateTaskId = new ArrayList<>();
        listSeparateTaskId.addAll(listTaskId);
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelIdWhere", channelId);
        sqlParam.put("revertTimeGt", DateTimeUtil.getNow());
        List<Map<String, Object>> listPlatform = cmsBtTasksStockDaoExt.selectStockSeparatePlatform(sqlParam);
        listPlatform.forEach(data -> {
            Integer taskId = (Integer) data.get("task_id");
            if (!listSeparateTaskId.contains(taskId)) {
                listSeparateTaskId.add(taskId);
            }
        });

        // 取得该渠道下所有动态隔离的sku对应的平台(隔离中还原中任务)
        Map<String, Set<Integer>> mapSkuDynamic = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("status", "");
        param.put("taskIdList", listSeparateTaskId);
        List<Map<String, Object>> listSkuDynamic = cmsBtStockSeparateItemDaoExt.selectStockSeparateItem(param);
        listSkuDynamic.forEach(data -> {
            String sku = (String) data.get("sku");
            Integer cartId = (Integer) data.get("cart_id");
            if (!listShareCartId.contains(cartId)) {
                // 不是共享平台(即已经被还原的平台)
                Set<Integer> setCartDynamic = mapSkuDynamic.get(sku);
                if (setCartDynamic == null) {
                    setCartDynamic = new HashSet<>();
                    setCartDynamic.add(cartId);
                    mapSkuDynamic.put(sku, setCartDynamic);
                } else {
                    setCartDynamic.add(cartId);
                }
            }
        });

        $info("更新处理开始,渠道是%s", channelId);
        List<Map<String, Object>> listImsBtLogSynInventory = new ArrayList<>();

        try {
            transactionRunner.runWithTran(() -> {
                // cms_bt_stock_separate_item状态更新成6：还原中
                Map<String, Object> updateParam = new HashMap<>();
                updateParam.put("status", stockInfoService.STATUS_REVERTING);
                updateParam.put("modifier", getTaskName());
                updateParam.put("taskList", listTaskId);
                updateParam.put("statusWhere", stockInfoService.STATUS_WAITING_REVERT);
                cntRevert += cmsBtStockSeparateItemDaoExt.updateStockSeparateItem(updateParam);

                Map<Integer, Map<String, Object>> mapDataByCart = new HashMap<>(); // Map<平台, data>
                for (Map.Entry<String, List<Map<String, Object>>> entry : mapSkuData.entrySet()) {
                    String sku = entry.getKey();
                    List<Map<String, Object>> listData = entry.getValue();
                    Integer qty = skuStockUsableAll.get(sku); // 可用库存

                    mapDataByCart.clear();
                    for (Map<String, Object> data : listData) {
                        Integer cartId = (Integer) data.get("cart_id");
                        mapDataByCart.put(cartId, data);
                    }

                    // 还原平台
                    for (Integer revertCartId : listRevertCartId) {
                        Map<String, Object> data = mapDataByCart.get(revertCartId);
                        if (data != null) {
                            // 还原
                            // 更新ims_bt_log_syn_inventory
                            listImsBtLogSynInventory.add(
                                    stockInfoService.createMapImsBtLogSynInventory(
                                            channelId,
                                            revertCartId,
                                            sku,
                                            qty,
                                            stockInfoService.SYN_TYPE_ALL,
                                            (Integer) data.get("id"),
                                            stockInfoService.STATUS_REVERTING,
                                            getTaskName()));
                        }
                    }

                    // 隔离中task的动态隔离数据
                    Set<Integer> setCartDynamic = mapSkuDynamic.get(sku);
                    if (setCartDynamic != null) {
                        // 动态隔离数据
                        for (Integer cartDynamic : setCartDynamic) {
                            // 更新ims_bt_log_syn_inventory
                            listImsBtLogSynInventory.add(
                                    stockInfoService.createMapImsBtLogSynInventory(
                                            channelId,
                                            cartDynamic,
                                            sku,
                                            qty,
                                            stockInfoService.SYN_TYPE_ALL,
                                            null,
                                            null,
                                            getTaskName()));
                        }
                    }

                    // 共享平台
                    for (Integer cartShare : listShareCartId) {
                        // 更新ims_bt_log_syn_inventory
                        listImsBtLogSynInventory.add(
                                stockInfoService.createMapImsBtLogSynInventory(
                                        channelId,
                                        cartShare,
                                        sku,
                                        qty,
                                        stockInfoService.SYN_TYPE_ALL,
                                        null,
                                        null,
                                        getTaskName()));
                    }

                    // 一个sku结束
                    if (listImsBtLogSynInventory.size() > 500) {
                        cntSend += stockInfoService.insertImsBtLogSynInventory(listImsBtLogSynInventory);
                        listImsBtLogSynInventory.clear();
                    }
                }

                // end
                if (listImsBtLogSynInventory.size() > 0) {
                    cntSend += stockInfoService.insertImsBtLogSynInventory(listImsBtLogSynInventory);
                    listImsBtLogSynInventory.clear();
                }
            });
        } catch (Exception e) {
            $error(e.getMessage());
            logIssue("cms 库存隔离batch", "渠道是"+ channelId +"的等待还原数据更新失败. " + e.getMessage());
        }
        $info("更新处理结束,渠道是%s", channelId);
    }
}
