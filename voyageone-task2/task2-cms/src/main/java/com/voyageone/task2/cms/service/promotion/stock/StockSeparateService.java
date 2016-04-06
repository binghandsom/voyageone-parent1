package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtStockSalesQuantityDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparatePlatformInfoDao;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

    @Autowired
    private TransactionRunner transactionRunner;

    private static final String ERROR_MSG = "此sku在别的隔离平台有暂时不能隔离的数据";

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
        $info("等待隔离数据取得完毕. %d件", resultData.size());

        // 按渠道,sku整理Map<channelId, Map<taskId, Map<sku,resultData>>>
        Map<String, Map<Integer, Map<String, List<Map<String, Object>>>>> resultDataByChannel = new HashMap<>();

        for (Map<String, Object> rowData : resultData) {
            String channelId = (String) rowData.get("channel_id");
            String sku = (String) rowData.get("sku");
            Integer taskId = (Integer) rowData.get("task_id");

            // 隔离数据Map put
            Map<Integer, Map<String, List<Map<String, Object>>>> mapDataChannel = resultDataByChannel.get(channelId);
            if (mapDataChannel == null) {
                mapDataChannel = new HashMap<>();
                Map<String, List<Map<String, Object>>> mapDataTask = new HashMap<>();
                List<Map<String, Object>> listData = new ArrayList<>();
                listData.add(rowData);
                mapDataTask.put(sku, listData);
                mapDataChannel.put(taskId, mapDataTask);
                resultDataByChannel.put(channelId, mapDataChannel);
            } else {
                Map<String, List<Map<String, Object>>> mapDataTask = mapDataChannel.get(taskId);
                if (mapDataTask == null) {
                    mapDataTask = new HashMap<>();
                    List<Map<String, Object>> listData = new ArrayList<>();
                    listData.add(rowData);
                    mapDataTask.put(sku, listData);
                    mapDataChannel.put(taskId, mapDataTask);
                } else {
                    if (mapDataTask.containsKey(sku)) {
                        mapDataTask.get(sku).add(rowData);
                    } else {
                        List<Map<String, Object>> listData = new ArrayList<>();
                        listData.add(rowData);
                        mapDataTask.put(sku, listData);
                    }
                }
            }
        }

        // 把隔离成功的数据进行更新（因为sku所有平台必须一起隔离）
        // if（该sku下所有平台只要有状态不是隔离成功和等待隔离的）
        //    此sku等待隔离数据变成隔离失败，其余状态的数据不更新，保持原样(此sku从更新对象resultDataByChannel里面remove)
        // else
        //    此sku隔离成功数据变成等待隔离，即也作为对象重新隔离(此sku对应平台从更新对象resultDataByChannel里面add),并把cms_bt_stock_sales_quantity销售表end_flg更新成"1：结束"
        List<String> errorChannel = new ArrayList<>();
        for (String channelId : resultDataByChannel.keySet()) {
            try {
                updateSeparateSuccessData(resultDataByChannel.get(channelId), channelId);
            } catch (Exception e) {
                errorChannel.add(channelId);
                logger.error(e.getMessage());
                logIssue("cms 库存隔离batch", "渠道是" + channelId + "的等待隔离数据整理失败. " + e.getMessage());
            }
        }

        // error渠道从更新对象中删除
        errorChannel.forEach(channelId-> resultDataByChannel.remove(channelId));

        // 按渠道把等待隔离数据进行更新
        for (String channelId : resultDataByChannel.keySet()) {
            executeByChannel(channelId, resultDataByChannel.get(channelId));
        }

    }

    /**
     * 隔离数据整理
     *
     * @param mapSkuTaskData 等待隔离数据
     * @param channelId      平台
     */
    private void updateSeparateSuccessData(Map<Integer, Map<String, List<Map<String, Object>>>> mapSkuTaskData, String channelId) {
        Map<String, Object> sqlParamItem = new HashMap<>();
        Map<String, Object> sqlParamTask = new HashMap<>();
        List<String> listErrorSku = new ArrayList<>();
        List<String> listSuccessStatusSku = new ArrayList<>();
        List<Map<String, Object>> listAddData = new ArrayList<>();
        Map<Integer, List<String>> mapCartSku = new HashMap<>(); // Map<平台, List<Sku>> 用于更新sku隔离销售表

        $info("隔离数据整理开始");
        transactionRunner.runWithTran(() -> {
            for (Integer taskId : mapSkuTaskData.keySet()) {
                listErrorSku.clear();
                listSuccessStatusSku.clear();
                mapCartSku.clear();

                // taskId对应所有隔离平台
                sqlParamItem.put("taskId", taskId);
                sqlParamTask.put("taskId", taskId);
                List<Map<String, Object>> listTaskInfo = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParamTask);

                Map<String, List<Map<String, Object>>> mapSkuData = mapSkuTaskData.get(taskId);
                for (Map.Entry<String, List<Map<String, Object>>> entry : mapSkuData.entrySet()) {
                    String sku = entry.getKey();
                    List<Map<String, Object>> listData = entry.getValue();
                    if (listData.size() != listTaskInfo.size()) {
                        //  该sku等待隔离状态的数量 <> taskId对应所有隔离平台数量
                        sqlParamItem.put("sku", sku);
                        List<Map<String, Object>> resultData = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParamItem);
                        boolean isErr = false;
                        listAddData.clear();
                        for (Map<String, Object> data : resultData) {
                            String status = (String) data.get("status");
                            if (!StringUtils.isEmpty(status)
                                    && !stockInfoService.STATUS_WAITING_SEPARATE.equals(status)
                                    && !stockInfoService.STATUS_SEPARATE_SUCCESS.equals(status)) {
                                // 状态不是隔离成功和等待隔离,此sku不是更新对象
                                isErr = true;
                                listErrorSku.add(sku);
                                break;
                            }
                            if (stockInfoService.STATUS_SEPARATE_SUCCESS.equals(status)) {
                                listAddData.add(data);
                            }
                        }
                        if (!isErr && listAddData.size() > 0) {
                            listData.addAll(listAddData);
                            listSuccessStatusSku.add(sku);
                            listAddData.forEach(data -> {
                                Integer cartId = (Integer) data.get("cart_id");
                                if (mapCartSku.containsKey(cartId)) {
                                    mapCartSku.get(cartId).add(sku);
                                } else {
                                    mapCartSku.put(cartId, new ArrayList<String>() {{
                                        this.add(sku);
                                    }});
                                }
                            });
                        }
                    }
                }

                if (listErrorSku.size() > 0) {
                    listErrorSku.forEach(sku -> {
                        // errorSku从更新对象中删除
                        mapSkuData.remove(sku);
                    });
                    // errorSku等待隔离状态变更成隔离失败
                    Map<String, Object> updateParam = new HashMap<>();
                    updateParam.put("status", stockInfoService.STATUS_SEPARATE_FAIL);
                    updateParam.put("errorMsg", ERROR_MSG);
                    updateParam.put("errorTime", DateTimeUtil.getNow());
                    updateParam.put("modifier", getTaskName());
                    updateParam.put("taskId", taskId);
                    updateParam.put("statusWhere", stockInfoService.STATUS_WAITING_SEPARATE);
                    for (int index = 0; index < listErrorSku.size(); index = index + 500) {
                        if (index + 500 < listErrorSku.size()) {
                            updateParam.put("skuList", listErrorSku.subList(index, index + 500));
                        } else {
                            updateParam.put("skuList", listErrorSku.subList(index, listErrorSku.size()));
                        }
                        cmsBtStockSeparateItemDao.updateStockSeparateItem(updateParam);
                    }
                }

                if (listSuccessStatusSku.size() > 0) {
                    // 隔离成功状态变更成等待隔离状态
                    Map<String, Object> updateParam = new HashMap<>();
                    updateParam.put("status", stockInfoService.STATUS_WAITING_SEPARATE);
                    updateParam.put("modifier", getTaskName());
                    updateParam.put("taskId", taskId);
                    updateParam.put("statusWhere", stockInfoService.STATUS_SEPARATE_SUCCESS);

                    for (int index = 0; index < listSuccessStatusSku.size(); index += 500) {
                        if (index + 500 < listSuccessStatusSku.size()) {
                            updateParam.put("skuList", listSuccessStatusSku.subList(index, index + 500));
                        } else {
                            updateParam.put("skuList", listSuccessStatusSku.subList(index, listSuccessStatusSku.size()));
                        }
                        cmsBtStockSeparateItemDao.updateStockSeparateItem(updateParam);
                    }
                }

                if (mapCartSku.size() > 0) {
                    // 隔离成功的销售表end_flg更新成"1：结束"
                    Map<String, Object> updateParam = new HashMap<>();
                    updateParam.put("channelId", channelId);
                    updateParam.put("endFlg", stockInfoService.END);
                    updateParam.put("modifier", getTaskName());

                    for (Map.Entry<Integer, List<String>> entry : mapCartSku.entrySet()) {
                        updateParam.put("cartId", entry.getKey());
                        List<String> lisSku = entry.getValue();
                        for (int index = 0; index < lisSku.size(); index += 500) {
                            if (index + 500 < lisSku.size()) {
                                updateParam.put("skuList", lisSku.subList(index, index + 500));
                            } else {
                                updateParam.put("skuList", lisSku.subList(index, lisSku.size()));
                            }
                            cmsBtStockSalesQuantityDao.updateStockSalesQuantity(updateParam);
                        }
                    }
                }
            }
        });
        $info("隔离数据整理结束");
    }

    /**
     * 按渠道把等待隔离数据进行更新
     *
     * @param channelId   渠道id
     * @param mapSkuTaskData Map<taskId, Map<sku,resultData>>该渠道下的sku数据
     */
    private void executeByChannel(String channelId, Map<Integer, Map<String, List<Map<String, Object>>>> mapSkuTaskData) {
        // 取得可用库存
        $info("开始取得可用库存数据");
        Map<String,Integer> skuStockUsableAll = stockInfoService.getUsableStock(channelId);
        $info("可用库存数据取得完毕");

        // 取得该渠道下未被隔离的平台(共享平台)
        List<Integer> listShareCartId = stockInfoService.getShareCartId(channelId, null, null);

        $info("更新处理开始,渠道是%s", channelId);
        try {
            transactionRunner.runWithTran(() -> {
                // 计算隔离库存,更新
                for (Integer taskId : mapSkuTaskData.keySet()) {
                    // taskId对应平台的增减顺
                    Map<String, Object> param = new HashMap<>();
                    param.put("taskId", taskId);
                    param.put("commonPlatform", true);
                    List<Map<String, Object>> listTaskInfo = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(param);
                    Map<Integer, Integer> mapPlatAdd = new TreeMap<>(); // 增顺
                    Map<Integer, Integer> mapPlatSub = new TreeMap<>(); // 减顺
                    for(Map<String, Object> platformInfo : listTaskInfo) {
                        Integer cartId = (Integer) platformInfo.get("cart_id");
                        Integer add = (Integer) platformInfo.get("add_priority");
                        Integer sub = (Integer) platformInfo.get("subtract_priority");
                        mapPlatAdd.put(add, cartId);
                        mapPlatSub.put(sub, cartId);
                    }

                    Map<String, List<Map<String, Object>>> mapSkuData = mapSkuTaskData.get(taskId);
                    Integer skuUsableOld = 0; // 可用库存
                    Map<Integer, Integer> cartSeparateQtyOld = new HashMap<>(); // Map<平台, 隔离库存>
                    Map<Integer, Integer> cartSeq = new HashMap<>(); // Map<平台, seq>
                    for (Map.Entry<String, List<Map<String, Object>>> entry : mapSkuData.entrySet()) {
                        String sku = entry.getKey();
                        List<Map<String, Object>> listData = entry.getValue();
                        skuUsableOld = (Integer) listData.get(0).get("qty");
                        cartSeparateQtyOld.clear();
                        cartSeq.clear();
                        Integer separateQtyAll = 0;
                        for (Map<String, Object> data : listData) {
                            Integer cartId = (Integer) data.get("cart_id");
                            Integer separateQty = (Integer) data.get("separate_qty");
                            separateQtyAll += separateQty;
                            cartSeparateQtyOld.put(cartId, separateQty);
                            cartSeq.put(cartId, (Integer) data.get("seq"));
                        }

                        // 各平台最终显示库存（包含隔离平台，共享平台（-1），不包含动态平台）
                        Map<Integer, Integer> cartDisplayQty = calQty(cartSeparateQtyOld, separateQtyAll, skuUsableOld, skuStockUsableAll.get(sku), mapPlatAdd, mapPlatSub);

                        // 更新
                        for (Integer cartId : mapPlatAdd.values()) {
                           // 此任务设定的隔离平台
                            if (cartId != -1) {
                                // 更新cms_bt_stock_separate_item库存隔离数据表
                                Map<String, Object> updateParam = new HashMap<>();
                                updateParam.put("qty", skuStockUsableAll.get(sku));
                                updateParam.put("status", stockInfoService.STATUS_SEPARATING);
                                updateParam.put("modifier", getTaskName());
                                updateParam.put("taskId", taskId);
                                updateParam.put("sku", sku);
                                updateParam.put("cartId", cartId);

                                Integer separateQty = cartDisplayQty.get(cartId);
                                if (separateQty == null) {
                                    // 动态
                                    separateQty = cartDisplayQty.get(-1);
                                    cmsBtStockSeparateItemDao.updateStockSeparateItem(updateParam);

                                    // 更新ims_bt_log_syn_inventory
                                    stockInfoService.insertImsBtLogSynInventory(
                                            channelId,
                                            cartId,
                                            sku,
                                            separateQty,
                                            stockInfoService.SYN_TYPE_ALL,
                                            null,
                                            null,
                                            getTaskName());
                                } else {
                                    updateParam.put("separateQty", separateQty);
                                    cmsBtStockSeparateItemDao.updateStockSeparateItem(updateParam);

                                    // 更新ims_bt_log_syn_inventory
                                    stockInfoService.insertImsBtLogSynInventory(
                                            channelId,
                                            cartId,
                                            sku,
                                            separateQty,
                                            stockInfoService.SYN_TYPE_ALL,
                                            cartSeq.get(cartId),
                                            stockInfoService.STATUS_SEPARATING,
                                            getTaskName());
                                }
                            }
                        }

                        for (Integer cartId : listShareCartId) {
                            // 共享平台
                            Integer separateQty = cartDisplayQty.get(-1);
                            // 更新ims_bt_log_syn_inventory
                            stockInfoService.insertImsBtLogSynInventory(
                                    channelId,
                                    cartId,
                                    sku,
                                    separateQty,
                                    stockInfoService.SYN_TYPE_ALL,
                                    null,
                                    null,
                                    getTaskName());
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            logIssue("cms 库存隔离batch", "渠道是"+ channelId +"的等待隔离数据更新失败. " + e.getMessage());
        }
        $info("更新处理结束,渠道是%s", channelId);
    }

    /**
     * @param cartSeparateQty Map<平台, 隔离库存>
     * @param separateQtyAll  各平台设置隔离数量之和
     * @param skuUsableOld    元可用库存
     * @param skuUsable       现可用库存
     * @param mapPlatAdd      平台增顺
     * @param mapPlatSub      平台减顺
     * @return Map<平台, 隔离库存>
     */
    private Map<Integer, Integer> calQty(Map<Integer, Integer> cartSeparateQty, Integer separateQtyAll, Integer skuUsableOld, Integer skuUsable, Map<Integer, Integer> mapPlatAdd, Map<Integer, Integer> mapPlatSub) {
        Map<Integer, Integer> ret = new HashMap<>();
        ret.putAll(cartSeparateQty);
        // 共享平台
        Integer share = skuUsableOld - separateQtyAll;
        if (share < 0) share = 0;
        ret.put(-1, share);

        if (skuUsableOld == skuUsable) {
            return ret;
        }

        if (separateQtyAll <= skuUsableOld) {
            // 各平台设置之和 <= 可用库存
            // 根据增减顺修正隔离库存
            if (skuUsableOld < skuUsable) {
                // 增顺
                for (Integer cartId : mapPlatAdd.values()) {
                    Integer separateQty = ret.get(cartId);
                    if (separateQty != null) {
                        // 非动态平台，即隔离平台或共享平台
                        ret.put(cartId, separateQty + (skuUsable - skuUsableOld));
                        break;
                    }
                }
            } else {
                // 减顺
                Integer minus = skuUsableOld - skuUsable; // 需要减少的数量
                for (Integer cartId : mapPlatSub.values()) {
                    Integer separateQty = ret.get(cartId);
                    if (separateQty != null) {
                        // 非动态平台，即隔离平台或共享平台
                        if (separateQty - minus >= 0) {
                            // 够减
                            ret.put(cartId, separateQty - minus);
                            break;
                        } else {
                            // 不够减，下个平台继续减
                            ret.put(cartId, 0);
                            minus = minus - separateQty;
                        }
                    }
                }
            }
        } else {
            // 按比例修正隔离库存
            if (skuUsableOld ==0) {
                // 原可用库存为0
                if (skuUsable > separateQtyAll) {
                    // 现可用库存 > 各平台设置比例之和
                    // 按各平台所占比例重新计算
                    Integer all = 0;
                    for (Map.Entry<Integer, Integer> entry : cartSeparateQty.entrySet()) {
                        Integer separateQtyOld = entry.getValue();
                        Integer separateQty = skuUsable * separateQtyOld / separateQtyAll;
                        all += separateQty;
                        ret.put(entry.getKey(), separateQty);
                    }
                    ret.put(-1, skuUsable - all); // 共享平台
                }
                // 现可用库存 <= 各平台设置比例之和,隔离数量不变
            } else {
                // 原可用库存不为0
                for (Map.Entry<Integer, Integer> entry : cartSeparateQty.entrySet()) {
                    Integer separateQtyOld = entry.getValue();
                    Integer separateQty = skuUsable * separateQtyOld / skuUsableOld;
                    ret.put(entry.getKey(), separateQty);
                }
            }
        }

        return ret;
    }

}
