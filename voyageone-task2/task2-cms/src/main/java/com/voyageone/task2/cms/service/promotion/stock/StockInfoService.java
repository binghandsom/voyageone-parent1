package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsBtStockSalesQuantityDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparatePlatformInfoDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
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

//    @Autowired
//    private WmsBtLogicInventoryDao wmsBtLogicInventoryDao;
    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

    @Autowired
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

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
     * 取得可用库存
     *
     * @param channelId 渠道id
     * @return 可用库存Map<sku,usableStock>
     */
    public Map<String,Integer> getUsableStock(String channelId) {
        // 检索用param
        Map<String,Object> sqlParam = new HashMap<>();
        // skuList
        Set<String> setSku = new HashSet<>();

        // 取得逻辑库存
        Map<String,Integer> skuLogicStockAll = new HashMap<>();
        sqlParam.put("channelId", channelId);
        List<WmsBtInventoryCenterLogicModel> listLogicInventory = wmsBtInventoryCenterLogicDao.selectItemDetail(sqlParam);
        if (listLogicInventory != null && listLogicInventory.size() > 0) {
            for (WmsBtInventoryCenterLogicModel logicInventory : listLogicInventory) {
                String sku = logicInventory.getSku();
                setSku.add(sku);
                Integer logicStock = logicInventory.getQtyChina();
                Integer logicStockAll = skuLogicStockAll.get(sku);
                if (logicStockAll != null) {
                    skuLogicStockAll.put(sku, logicStockAll + logicStock);
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
                setSku.add(sku);
                Integer separateQty = (Integer) stockInfo.get("separate_qty");
                Integer separateQtyAll = skuStockSeparateAll.get(sku);
                if (separateQtyAll != null) {
                    skuStockSeparateAll.put(sku, separateQtyAll + separateQty);
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
                setSku.add(sku);
                Integer incrementQty = (Integer) stockIncrementInfo.get("increment_qty");
                Integer incrementQtyAll = skuStockIncrementAll.get(sku);
                if (incrementQtyAll != null) {
                    skuStockIncrementAll.put(sku, incrementQtyAll + incrementQty);
                } else {
                    skuStockIncrementAll.put(sku, incrementQty);
                }
            }
        }

        // 取得销售数量
        Map<String,Integer> skuStockSalesAll = new HashMap<>();
        sqlParam.clear();
        sqlParam.put("channelId", channelId);
        sqlParam.put("endFlg", NOT_END);
        List<Map<String, Object>> listStockSalesQuantity = cmsBtStockSalesQuantityDao.selectStockSalesQuantity(sqlParam);
        if (listStockSalesQuantity != null && listStockSalesQuantity.size() > 0) {
            for (Map<String, Object> stockSaleInfo : listStockSalesQuantity) {
                String sku = (String) stockSaleInfo.get("sku");
                setSku.add(sku);
                Integer qty = (Integer) stockSaleInfo.get("qty");
                Integer qtyAll = skuStockSalesAll.get(sku);
                if (qtyAll != null) {
                    skuStockSalesAll.put(sku, qtyAll + qty);
                } else {
                    skuStockSalesAll.put(sku, qty);
                }
            }
        }

        // 可用库存数 = 逻辑库存 - （隔离库存 + 增量隔离库 - 销售数量)
        Map<String, Integer> skuStockUsableAll = new HashMap<>();
        for (String sku : setSku) {
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

    /**
     * 取得该渠道下已经隔离的平台
     *
     * @param channelId 渠道id
     * @return List<渠道id>
     */
    public List<Integer> getSeparateCartId(String channelId) {
        List<Integer> listSeparateCartId = new ArrayList<>();

        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("channelIdWhere", channelId);
        sqlParam.put("revertTimeGt", DateTimeUtil.getNow());
        List<Map<String, Object>> resultData = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        resultData.forEach(data -> {
            Integer cartId = (Integer) data.get("cart_id");
            if (!listSeparateCartId.contains(cartId)) {
                listSeparateCartId.add(cartId);
            }
        });

        return listSeparateCartId;
    }
}
