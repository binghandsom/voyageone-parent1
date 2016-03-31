package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtStockSalesQuantityDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.dao.wms.WmsBtLogicInventoryDao;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 库存batch共用逻辑Service
 *
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Service
public class StockInfoService {

    @Autowired
    private WmsBtLogicInventoryDao wmsBtLogicInventoryDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

    /** 增量/库存隔离状态 0：未进行 */
    public static final String STATUS_READY = "0";
    /** 库存隔离状态 1：等待隔离 */
    public static final String STATUS_WAITING_SEPARATE = "1";
    /** 库存隔离状态 2：隔离中 */
    public static final String STATUS_SEPARATING = "2";
    /** 库存隔离状态 3：隔离成功 */
    public static final String STATUS_SEPARATE_SUCCESS = "3";
    /** 库存隔离状态 4：隔离失败 */
    public static final String STATUS_SEPARATE_FAIL = "4";
    /** 库存隔离状态 5：等待还原 */
    public static final String STATUS_WAITING_REVERT = "5";
    /** 库存隔离状态 6：还原中 */
    public static final String STATUS_REVERTING = "6";
    /** 库存隔离状态 7：还原成功 */
    public static final String STATUS_REVERT_SUCCESS = "7";
    /** 库存隔离状态 8：还原失败 */
    public static final String STATUS_REVERT_FAIL = "8";
    /** 库存隔离状态 9：再修正 */
    public static final String STATUS_CHANGED = "9";

    /** 增量库存隔离状态 1：等待增量 */
    public static final String STATUS_WAITING_INCREMENT = "1";
    /** 增量库存隔离状态 2：增量中 */
    public static final String STATUS_INCREASING = "2";
    /** 增量库存隔离状态 3：增量成功 */
    public static final String STATUS_INCREMENT_SUCCESS = "3";
    /** 增量库存隔离状态 5：增量失败 */
    public static final String STATUS_INCREMENT_FAIL = "4";
    /** 增量库存隔离状态 5：还原 */
    public static final String STATUS_REVERT = "5";

    /** 结束状态  0: 未结束 */
    public static final String NOT_END = "0";
    /** 结束状态  1: 结束 */
    public static final String END = "1";

    /** 自动还原标志  0: 未执行自动还原 */
    public static final String AUTO_REVERTED  = "1";
    /** 自动还原标志  1: 已经执行自动还原 */
    public static final String NOT_REVERT = "0";

    /**
     * 取得可用库存里list
     *
     * @param channelId 渠道id
     * @return 可用库存Map<sku,usableStock>
     */
    public Map<String,Integer> getUsableStock(String channelId) {
        // 检索用param
        Map<String,Object> sqlParam = new HashMap<>();
        // skuList
        List<String> listSku = new ArrayList<>();

        // 取得逻辑库存
        Map<String,Integer> skuLogicStockAll = new HashMap<>();
        sqlParam.put("channelId", channelId);
        List<Map<String, Object>> listLogicInventory = wmsBtLogicInventoryDao.selectLogicInventoryList(sqlParam);
        if (listLogicInventory != null && listLogicInventory.size() > 0) {
            for (Map<String, Object> mapLogicInventory : listLogicInventory) {
                String sku = (String) mapLogicInventory.get("sku");
                if (!listSku.contains(sku)) {
                    listSku.add(sku);
                }
                Integer logicStock = (Integer) mapLogicInventory.get("qty_china");
                if (skuLogicStockAll.containsKey(sku)) {
                    skuLogicStockAll.put(sku, skuLogicStockAll.get(sku) + logicStock);
                } else {
                    skuLogicStockAll.put(sku, logicStock);
                }
            }
        }

        // 隔离库存
        Map<String,Integer> skuStockSeparateAll = new HashMap<>();
        sqlParam.clear();
        sqlParam.put("channelId", channelId);
        // 状态 = 2：隔离成功
        sqlParam.put("status", STATUS_SEPARATE_SUCCESS);
        sqlParam.put("tableNameSuffix", "");
        List<Map<String,Object>> listStockSeparate = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
        // sku库存隔离信息（所有任务所有平台的数据）
        if (listStockSeparate != null && listStockSeparate.size() > 0) {
            for (Map<String, Object> stockInfo : listStockSeparate) {
                String sku = (String) stockInfo.get("sku");
                if (!listSku.contains(sku)) {
                    listSku.add(sku);
                }
                Integer separateQty = (Integer) stockInfo.get("separate_qty");
                if (skuStockSeparateAll.containsKey(sku)) {
                    skuStockSeparateAll.put(sku, skuStockSeparateAll.get(sku) + separateQty);
                } else {
                    skuStockSeparateAll.put(sku, separateQty);
                }
            }
        }

        // 取得增量隔离库存
        Map<String,Integer> skuStockIncrementAll = new HashMap<>();
        sqlParam.clear();
        sqlParam.put("channelId", channelId);
        // 状态 = 3：增量成功
        sqlParam.put("status", STATUS_INCREMENT_SUCCESS);
        sqlParam.put("tableNameSuffix", "");
        List<Map<String,Object>> listStockIncrement = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrement(sqlParam);
        if (listStockIncrement != null && listStockIncrement.size() > 0) {
            for (Map<String, Object> stockIncrementInfo : listStockIncrement) {
                String sku = (String) stockIncrementInfo.get("sku");
                if (!listSku.contains(sku)) {
                    listSku.add(sku);
                }
                Integer incrementQty = (Integer) stockIncrementInfo.get("increment_qty");
                if (skuStockIncrementAll.containsKey(sku)) {
                    skuStockIncrementAll.put(sku, skuStockIncrementAll.get(sku) + incrementQty);
                } else {
                    skuStockIncrementAll.put(sku, incrementQty);
                }
            }
        }

        // 取得销售数量
        Map<String,Integer> skuStockSalesAll = new HashMap<>();
        sqlParam.clear();
        sqlParam.put("channelId", channelId);
        sqlParam.put("endFlg", "0");
        List<Map<String,Object>> listStockSalesQuantity = cmsBtStockSalesQuantityDao.selectStockSalesQuantity(sqlParam);
        if (listStockSalesQuantity != null && listStockSalesQuantity.size() > 0) {
            for (Map<String, Object> stockSaleInfo : listStockSalesQuantity) {
                String sku = (String) stockSaleInfo.get("sku");
                if (!listSku.contains(sku)) {
                    listSku.add(sku);
                }
                Integer qty = (Integer) stockSaleInfo.get("qty");
                if (skuStockSalesAll.containsKey(sku)) {
                    skuStockSalesAll.put(sku, skuStockSalesAll.get(sku) + qty);
                } else {
                    skuStockSalesAll.put(sku, qty);
                }
            }
        }

        // 可用库存数 = 逻辑库存 - （隔离库存 + 增量隔离库 - 销售数量)
        Map<String, Integer> skuStockUsableAll = new HashMap<>();
        for (String sku : listSku) {
            Integer usable = getMapValue(skuLogicStockAll, sku) - (getMapValue(skuStockSeparateAll, sku) + getMapValue(skuStockIncrementAll, sku) - getMapValue(skuStockSalesAll, sku));
            if (usable < 0) {
                usable = 0;
            }
            skuStockUsableAll.put(sku, usable);
        }

        return skuStockUsableAll;
    }

    private Integer getMapValue(Map<String, Integer> map, String key) {
        Integer ret = map.get(key);
        if (ret == null) {
            ret = 0;
        }
        return ret;
    }
}
