package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementTaskDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private StockInfoService stockInfoService;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementTaskDao cmsBtStockSeparateIncrementTaskDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    /** 增量件数 */
    private int cntIncrement = 0;

    /** 推送件数 */
    private int cntSend = 0;

    /** 有增量接口的平台列表 */
    private List<Integer> cartList = new ArrayList<>();

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
        $info("取得 等待增量 处理件数：%d件", stockIncrementList.size());

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

        // 取得 有增量接口的平台
        List<TypeBean> typeBeanList = Types.getTypeList(69, "en");
        for(TypeBean typeBean : typeBeanList) {
            cartList.add(new Integer(typeBean.getValue()));
        }

        simpleTransaction.openTransaction();
            try {
            // 按渠道id更新等待增量的数据
            for (String channelId : stockIncrementMapByChannel.keySet()) {
                executeByChannel(channelId, stockIncrementMapByChannel.get(channelId));
            }
        }catch (Exception e) {
            $info("*****CmsStockIncrementSeparateJob任务异常终了*****");
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();

        $info("处理了增量件数：%d件", cntIncrement);
        $info("处理了推送件数：%d件", cntSend);
        $info("*****CmsStockIncrementSeparateJob任务结束*****");

    }

    /**
     * 按渠道id更新等待增量的数据
     *
     * @param channelId   渠道id
     * @param stockIncrementList 渠道id对应的增量数据列表
     */
    private void executeByChannel(String channelId, List<Map<String, Object>> stockIncrementList) {
//        // 取得渠道id 对应的所有销售平台
//        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
//        List<String> listCartId = new ArrayList<>();
//        cartList.forEach(cartInfo -> listCartId.add(cartInfo.getValue()));

        // 增量任务id列表
        Set<String> subTaskIdList = new HashSet<String>();
        for (Map<String, Object> stockIncrement : stockIncrementList) {
            String subTaskId = (String) stockIncrement.get("sub_task_id");
            subTaskIdList.add(subTaskId);
        }

        $info("开始取得可用隔离库存值");
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
        $info("隔离库存值取得完毕");

        // 取得可用库存
        $info("开始取得可用库存数据");
        Map<String,Integer> skuStockUsableAll = stockInfoService.getUsableStock(channelId);
        $info("可用库存数据取得完毕");

        // 取得该渠道下未被隔离的平台(共享平台)
        List<Integer> shareCartIdList = stockInfoService.getShareCartId(channelId, null, null);

        // 增量库存插入列表
        List<Map<String, Object>> imsBtLogSynInventoryList = new ArrayList<>();

        for (Map<String, Object> stockIncrement : stockIncrementList) {
            Integer seq = (Integer) stockIncrement.get("seq");
            Integer subTaskId = (Integer) stockIncrement.get("sub_task_id");
            String sku = (String) stockIncrement.get("sku");
            Integer cartId = (Integer) stockIncrement.get("cart_id");
            // 旧的可用库存
            Integer oldUsableQty = (Integer) stockIncrement.get("qty");
            // 旧的增量库存
            Integer oldIncrementQty = (Integer) stockIncrement.get("increment_qty");
            // 固定值隔离标志位
            String fixFlg = (String) stockIncrement.get("fix_flg");
            // 实际可用库存
            Integer usableQty = skuStockUsableAll.get(sku);


            // 计算实际增量库存
            Integer incrementQty = calculateIncrementQty(oldUsableQty, oldIncrementQty, usableQty, fixFlg);
            // 共享平台库存
            Integer shareQty = usableQty - incrementQty;
            if (shareQty < 0) {
                shareQty = 0;
            }

            // 将计算后的实际增量库存 更新到增量库存隔离数据表，同时更新状态为"1：等待增量"
            Integer updateCnt = updateStockIncrementInfo(subTaskId, sku, incrementQty);
            cntIncrement += updateCnt;

            // 更新成功的场合
            if (updateCnt > 0) {
                // 将增量库存数据插入ims_bt_log_syn_inventory表
                insertIncrementSeparateInventoryData(imsBtLogSynInventoryList, channelId, cartId, sku, incrementQty, seq);

                // 将共享平台的库存数据插入ims_bt_log_syn_inventory表
                $info("共享平台数量：" + shareCartIdList.size());
                for (Integer shareCartId : shareCartIdList) {
                    insertShareInventoryData(imsBtLogSynInventoryList, channelId, shareCartId, sku, shareQty, seq);
                }

                // 500件以上插入
                if (imsBtLogSynInventoryList.size() > 500) {
                    cntSend += stockInfoService.insertImsBtLogSynInventory(imsBtLogSynInventoryList);
                    imsBtLogSynInventoryList.clear();
                }
            }
        }
        if (imsBtLogSynInventoryList.size() > 0) {
            cntSend += stockInfoService.insertImsBtLogSynInventory(imsBtLogSynInventoryList);
        }
    }


    /**
     * 从增量库存隔离数据表（cms_bt_stock_separate_increment_item）取得状态为"1:等待增量"的数据
     *
     * @return 等待增量的数据
     */
    private List<Map<String, Object>> getStockSeparateIncrementData() {
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("status", StockInfoService.STATUS_WAITING_SEPARATE);
        List<Map<String, Object>> stockIncrementList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrement(sqlParam);
        return  stockIncrementList;
    }

    /**
     * 根据增量任务id列表，取得状态为"隔离成功"的隔离数据列表
     *
     * @param subTaskIdList 增量任务id列表
     * @return 状态为"隔离成功"的隔离数据列表
     */
    private List<Map<String, Object>> getStockSeparateData(Set<String> subTaskIdList) {

        // 任务id列表
        List<Integer> taskIdList = new ArrayList<>();
        // 取得增量任务id对应的任务id
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("subTaskIdList", subTaskIdList);
        List<Map<String, Object>> stockTaskList = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(sqlParam);
        for (Map<String, Object>stockTask : stockTaskList) {
            taskIdList.add((Integer)stockTask.get("sub_task_id"));
        }

        // 根据任务id列表，取得状态为"隔离成功"的隔离数据列表
        Map<String, Object> sqlParam1 = new HashMap<>();
        sqlParam1.put("taskIdList", taskIdList);
        List<Map<String, Object>> stockList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam1);
        return  stockList;
    }

    /**
     * 结算实际增量库存
     *
     * @param oldUsableQty 旧的可用库存
     * @param oldIncrementQty 旧的增量库存
     * @param usableQty 实际可用库存
     * @param fixFlg 固定值隔离标志位
     * @return 实际增量库存
     */
    private Integer calculateIncrementQty(Integer oldUsableQty,  Integer oldIncrementQty, Integer usableQty, String fixFlg) {
        // 旧的可用库存 = 实际可用库存的场合，实际增量库存 = 旧的增量库存
        if (oldUsableQty.intValue() == usableQty.intValue()) {
            return oldIncrementQty;
        }
        // 固定值隔离标志位 = 1：按固定值进行增量隔离的场合，实际增量库存 = 旧的增量库存
        if (StockInfoService.TYPE_FIX_VALUE.equals(fixFlg)) {
            return oldIncrementQty;
        }

        Integer incrementQty = null;
        return  incrementQty;
    }

    /**
     * 将计算后的实际增量库存 更新到增量库存隔离数据表，同时更新状态为"2：增量中"
     *
     * @param subTaskId 增量任务id
     * @param sku Sku
     * @param incrementQty 实际增量库存
     * @return 更新结果
     */
    private Integer updateStockIncrementInfo(Integer subTaskId, String sku, Integer incrementQty) {
        Map<String, Object> sqlParam = new HashMap<>();
        // 状态更新为"1：等待增量"
        sqlParam.put("status", StockInfoService.STATUS_INCREASING);
        // 实际增量库存
        sqlParam.put("incrementQty", incrementQty);
        sqlParam.put("modifier", getTaskName());
        // 更新条件
        sqlParam.put("subTaskId", subTaskId);
        sqlParam.put("sku", sku);
        // 状态为"0：未进行"
        sqlParam.put("statusWhere", StockInfoService.STATUS_WAITING_INCREMENT);
        Integer updateCnt = cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam);
        return  updateCnt;
    }

    /**
     * 将增量库存数据插入ims_bt_log_syn_inventory表
     *
     * @param imsBtLogSynInventoryList 增量库存插入列表
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param sku Sku
     * @param incrementQty 实际增量库存
     * @param seq Seq
     */
    private void insertIncrementSeparateInventoryData(List<Map<String, Object>> imsBtLogSynInventoryList,
                    String channelId, Integer cartId, String sku, Integer incrementQty, Integer seq) {
        imsBtLogSynInventoryList.add(
                stockInfoService.createMapImsBtLogSynInventory(
                        channelId,
                        cartId,
                        sku,
                        incrementQty,
                        StockInfoService.SYN_TYPE_ADD,
                        seq,
                        StockInfoService.STATUS_INCREASING,
                        getTaskName()));
    }

    /**
     * 将共享平台的库存数据插入ims_bt_log_syn_inventory表
     *
     * @param imsBtLogSynInventoryList 增量库存插入列表
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param sku Sku
     * @param shareQty 共享库存
     * @param seq Seq
     */
    private void insertShareInventoryData(List<Map<String, Object>> imsBtLogSynInventoryList, String channelId,
                                          Integer cartId, String sku, Integer shareQty, Integer seq) {
        imsBtLogSynInventoryList.add(
                stockInfoService.createMapImsBtLogSynInventory(
                        channelId,
                        cartId,
                        sku,
                        shareQty,
                        StockInfoService.SYN_TYPE_ADD,
                        null,
                        null,
                        getTaskName()));
    }
}
