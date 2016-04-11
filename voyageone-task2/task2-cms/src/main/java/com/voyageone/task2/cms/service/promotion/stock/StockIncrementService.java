package com.voyageone.task2.cms.service.promotion.stock;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsBtStockSalesQuantityDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementTaskDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.ibatis.type.IntegerTypeHandler;
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
public class StockIncrementService extends BaseTaskService {

    @Autowired
    private StockInfoService stockInfoService;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementTaskDao cmsBtStockSeparateIncrementTaskDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

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
        return "CmsStockIncrementJob";
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

        if (stockIncrementList.size() > 0) {
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
            if (typeBeanList.size() == 0) {
                $error("请配置有增量接口的平台");
                throw new BusinessException("请配置有增量接口的平台");
            }
            for (TypeBean typeBean : typeBeanList) {
                cartList.add(new Integer(typeBean.getValue()));
            }

            simpleTransaction.openTransaction();
            try {
                // 按渠道id更新等待增量的数据
                for (String channelId : stockIncrementMapByChannel.keySet()) {
                    executeByChannel(channelId, stockIncrementMapByChannel.get(channelId));
                }
            } catch (Exception e) {
                $info("*****CmsStockIncrementSeparateJob任务异常终了*****");
                simpleTransaction.rollback();
                throw e;
            }
            simpleTransaction.commit();
        }

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

        // 增量任务id列表
        Set<Integer> subTaskIdList = new HashSet<>();
        for (Map<String, Object> stockIncrement : stockIncrementList) {
            Integer subTaskId = (Integer) stockIncrement.get("sub_task_id");
            subTaskIdList.add(subTaskId);
        }

        $info("开始取得一般隔离库存值");
        // 部分平台的Api接口未提供增量的接口，刷增量库存的时候，需要加算(隔离库存值 + 已增量的值 - 平台销售量)
        // 根据增量任务id列表，取得状态为"隔离成功"的隔离数据的隔离库存值，生成Map<sku + cartId, 隔离库存值>
        Map<String, Integer> stockInfoMap = new HashMap<>();
        List<Map<String, Object>> stockList = getStockSeparateData(subTaskIdList);
        for (Map<String, Object> stock : stockList) {
            String sku = (String) stock.get("sku");
            String cartId = String.valueOf(stock.get("cart_id"));
            Integer separateQty = (Integer) stock.get("separate_qty");
            stockInfoMap.put(sku + cartId, separateQty);
        }

        // 根据渠道id，取得已增量数据，生成Map<sku + cartId, 已增量的值>
        Map<String, Integer> stockIncreasedInfoMap = new HashMap<>();
        List<Map<String, Object>> stockIncreasedList = getStockIncrementData(channelId);
        for (Map<String, Object> stockIncreased : stockIncreasedList) {
            String sku = (String) stockIncreased.get("sku");
            String cartId = String.valueOf(stockIncreased.get("cart_id"));
            Integer incrementQty = (Integer) stockIncreased.get("increment_qty");
            if (stockIncreasedInfoMap.get(sku + cartId) != null) {
                stockIncreasedInfoMap.put(sku + cartId, incrementQty + stockInfoMap.get(sku + cartId));
            } else {
                stockIncreasedInfoMap.put(sku + cartId, incrementQty);
            }
        }

        // 根据渠道id，取得平台销售量，生成Map<sku + cartId, 平台销售量>
        Map<String, Integer> salesInfoMap = new HashMap<>();
        List<Map<String, Object>> salesList = getSalesQuantityData(channelId);
        for (Map<String, Object> sales : salesList) {
            String sku = (String) sales.get("sku");
            String cartId = String.valueOf(sales.get("cart_id"));
            Integer qty = (Integer) sales.get("qty");
            salesInfoMap.put(sku + cartId, qty);
        }

        $info("一般隔离库存值取得完毕");

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
            // 类型
            String type = (String) stockIncrement.get("type");
            // 实际可用库存
            Integer usableQty = skuStockUsableAll.get(sku);
            if (usableQty == null) {
                usableQty = 0;
            }

            // 计算实际增量库存
            Integer incrementQty = calculateIncrementQty(oldUsableQty, oldIncrementQty, usableQty, fixFlg, type);
            // 共享平台库存
            Integer shareQty = usableQty - incrementQty;
            if (shareQty < 0) {
                shareQty = 0;
            }
            // 如果增量为0件，更新到增量库存隔离数据表，同时更新状态为"3：增量成功"
            if (incrementQty == 0) {
                cntIncrement += updateStockIncrementInfo(subTaskId, sku, usableQty, incrementQty);
            } else {
                // 将计算后的实际增量库存 更新到增量库存隔离数据表，同时更新状态为"2：增量中"
                Integer updateCnt = updateStockIncrementInfo(subTaskId, sku, usableQty, incrementQty);
                cntIncrement += updateCnt;

                // 更新成功的场合
                if (updateCnt > 0) {
                    // 将增量库存数据插入ims_bt_log_syn_inventory表
                    insertIncrementSeparateInventoryData(imsBtLogSynInventoryList, stockInfoMap, stockIncreasedInfoMap, salesInfoMap, channelId, cartId, sku, incrementQty, seq);

                    // 将共享平台的库存数据插入ims_bt_log_syn_inventory表
                    for (Integer shareCartId : shareCartIdList) {
                        insertShareInventoryData(imsBtLogSynInventoryList, channelId, shareCartId, sku, shareQty);
                    }

                    // 500件以上插入
                    if (imsBtLogSynInventoryList.size() > 500) {
                        cntSend += stockInfoService.insertImsBtLogSynInventory(imsBtLogSynInventoryList);
                        imsBtLogSynInventoryList.clear();
                    }
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
    private List<Map<String, Object>> getStockSeparateData(Set<Integer> subTaskIdList) {

        // 任务id列表
        Set<Integer> taskIdList = new HashSet<>();
        // 取得增量任务id对应的任务id
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("subTaskIdList", subTaskIdList);
        List<Map<String, Object>> stockTaskList = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(sqlParam);
        for (Map<String, Object>stockTask : stockTaskList) {
            taskIdList.add((Integer)stockTask.get("task_id"));
        }

        // 根据任务id列表，取得状态为"隔离成功"的隔离数据列表
        Map<String, Object> sqlParam1 = new HashMap<>();
        sqlParam1.put("taskIdList", taskIdList);
        sqlParam1.put("status", StockInfoService.STATUS_SEPARATE_SUCCESS);
        List<Map<String, Object>> stockList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam1);
        return  stockList;
    }

    /**
     * 根据渠道id，取得已增量数据列表
     *
     * @param channelId 渠道id
     * @return 已增量数据列表
     */
    private List<Map<String, Object>> getStockIncrementData(String channelId) {
        // 根据渠道id，取得平台销售量列表
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelId", channelId);
        sqlParam.put("status", StockInfoService.STATUS_INCREMENT_SUCCESS);
        List<Map<String, Object>> stockIncreasedList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrement(sqlParam);
        return  stockIncreasedList;
    }


    /**
     * 根据渠道id，取得平台销售量列表
     *
     * @param channelId 渠道id
     * @return 平台销售量列表
     */
    private List<Map<String, Object>> getSalesQuantityData(String channelId) {
        // 根据渠道id，取得平台销售量列表
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelId", channelId);
        sqlParam.put("endFlg", StockInfoService.NOT_END);
        List<Map<String, Object>> salesList = cmsBtStockSalesQuantityDao.selectStockSalesQuantity(sqlParam);
        return  salesList;
    }

    /**
     * 结算实际增量库存
     *
     * @param oldUsableQty 旧的可用库存
     * @param oldIncrementQty 旧的增量库存
     * @param usableQty 实际可用库存
     * @param fixFlg 固定值隔离标志位
     * @param type 类型(1：按比例增量隔离； 2：按数量增量隔离)
     * @return 实际增量库存
     */
    private Integer calculateIncrementQty(Integer oldUsableQty,  Integer oldIncrementQty, Integer usableQty, String fixFlg, String type) {
        // 实际增量库存
        Integer incrementQty = null;

        // 固定值隔离标志位 = 1：按固定值进行增量隔离的场合，实际增量库存 = 旧的增量库存
        if (StockInfoService.TYPE_FIX_VALUE.equals(fixFlg)) {
            return oldIncrementQty;
        }

        // 旧的可用库存 = 实际可用库存的场合，实际增量库存 = 旧的增量库存
        if (oldUsableQty - usableQty == 0 && oldUsableQty != 0) {
            return oldIncrementQty;
        }

        // 若是按比例增量，无论“实时可用库存” 大于或者小于 旧的可用库存, 都按比例进行再计算
        if (StockInfoService.TYPE_INCREMENT_BY_RATION.equals(type)) {
            // 原可用库存为0
            if (oldUsableQty == 0) {
                // TODO 要确认
                //  实际增量库存 = 实际可用库存
                incrementQty = usableQty;
            } else {
                // 旧的增量库存 <= 旧的可用库存,则按比例计算
                if (oldIncrementQty <= oldUsableQty) {
                    incrementQty = usableQty * oldIncrementQty / oldUsableQty;
                } else {
                    // 旧的增量库存 > 旧的可用库存, 则实际增量库存 = 实时可用库存
                    incrementQty = usableQty;
                }
            }
        } else {
            // 若是按数值增量
            // 旧的增量库存 <= 旧的可用库存
            if (oldIncrementQty <= oldUsableQty) {
                // 实时可用库存 大于 旧的增量库存, 实际增量库存 = 旧的增量库存
                if (usableQty > oldIncrementQty) {
                    incrementQty = oldIncrementQty;
                } else {
                    // 实时可用库存 小于等于 旧的增量库存, 实际增量库存 = 实时可用库存
                    incrementQty = usableQty;
                }
            } else {
                // 旧的增量库存 > 旧的可用库存, 则实际增量库存 = 实时可用库存
                incrementQty = usableQty;
            }
        }

        return  incrementQty;
    }

    /**
     * 将计算后的实际增量库存 更新到增量库存隔离数据表，同时更新状态为"2：增量中"或者"3：增量成功"
     *
     * @param subTaskId 增量任务id
     * @param sku Sku
     * @param usableQty  实际可用库存
     * @param incrementQty 实际增量库存
     * @return 更新结果
     */
    private Integer updateStockIncrementInfo(Integer subTaskId, String sku, Integer usableQty, Integer incrementQty) {
        Map<String, Object> sqlParam = new HashMap<>();
        if (incrementQty == 0) {
            // 状态更新为"3：增量成功"
            sqlParam.put("status", StockInfoService.STATUS_INCREMENT_SUCCESS);
            // 隔离时间
            sqlParam.put("separateTime", DateTimeUtil.getNow());

        } else {
            // 状态更新为"2：增量中"
            sqlParam.put("status", StockInfoService.STATUS_INCREASING);
        }
        // 实际可用库存
        sqlParam.put("qty", usableQty);
        // 实际增量库存
        sqlParam.put("incrementQty", incrementQty);
        sqlParam.put("modifier", getTaskName());
        // 更新条件
        sqlParam.put("subTaskId", subTaskId);
        sqlParam.put("sku", sku);
        // 状态为"1：等待增量"
        sqlParam.put("statusWhere", StockInfoService.STATUS_WAITING_INCREMENT);
        Integer updateCnt = cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam);
        return  updateCnt;
    }

    /**
     * 将增量库存数据插入ims_bt_log_syn_inventory表
     *
     * @param imsBtLogSynInventoryList 增量库存插入列表
     * @param stockInfoMap 一般隔离库存值Map<sku + cartId, 隔离库存值>
     * @param stockIncreasedInfoMap 已增量值Map<sku + cartId, 已增量值>
     * @param salesInfoMap 平台销售数量Map<sku + cartId, 平台销售数量>
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param sku Sku
     * @param incrementQty 实际增量库存
     * @param seq Seq
     */
    private void insertIncrementSeparateInventoryData(List<Map<String, Object>> imsBtLogSynInventoryList,
                 Map<String, Integer> stockInfoMap, Map<String, Integer> stockIncreasedInfoMap, Map<String, Integer> salesInfoMap,
                 String channelId, Integer cartId, String sku, Integer incrementQty, Integer seq) {

        // 该渠道不提供增量接口
        if (!cartList.contains(cartId)) {
            // 一般隔离库存值
            Integer stockValue = 0;
            if (stockInfoMap.containsKey(sku + cartId)) {
                stockValue = stockInfoMap.get(sku + cartId);
            }

            // 已增量值
            Integer stockIncreasedValue = 0;
            if (stockIncreasedInfoMap.containsKey(sku + cartId)) {
                stockIncreasedValue = stockIncreasedInfoMap.get(sku + cartId);
            }

            // 平台销售数量
            Integer salesValue = 0;
            if (salesInfoMap.containsKey(sku + cartId)) {
                salesValue = salesInfoMap.get(sku + cartId);
            }

            // 出现数值不整合，不能将负值刷到平台上
            if (stockValue + stockIncreasedValue - salesValue < 0) {
                $error("一般隔离库存值 + 已增量值 - 平台销售数量 < 0, 数据不整合。渠道id:" + channelId + ",平台id:" + cartId + ",SKU:" + sku);
                throw new BusinessException("一般隔离库存值 + 已增量值 - 平台销售数量 < 0, 数据不整合。渠道id:" + channelId + ",平台id:" + cartId + ",SKU:" + sku);
            }

            // 增量库存 = 一般隔离库存值 - 平台销售数量 + DB中的增量库存值
            incrementQty += stockValue + stockIncreasedValue - salesValue;
        }

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
     */
    private void insertShareInventoryData(List<Map<String, Object>> imsBtLogSynInventoryList,
                  String channelId, Integer cartId, String sku, Integer shareQty) {
        imsBtLogSynInventoryList.add(
                stockInfoService.createMapImsBtLogSynInventory(
                        channelId,
                        cartId,
                        sku,
                        shareQty,
                        StockInfoService.SYN_TYPE_ALL,
                        null,
                        null,
                        getTaskName()));
    }
}
