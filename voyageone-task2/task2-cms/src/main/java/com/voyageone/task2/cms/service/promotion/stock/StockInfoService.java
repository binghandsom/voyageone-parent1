package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.ims.ImsBtLogSynInventoryDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.daoext.cms.*;
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
    /** syn_type 0: 全量 */
    public static final String SYN_TYPE_ALL  = "0";
    /** syn_type 2: 增量 */
    public static final String SYN_TYPE_ADD = "2";
    /** 1：按比例增量隔离 */
    public static final String TYPE_INCREMENT_BY_RATION = "1";
    /** 2：按数量增量隔离 */
    public static final String TYPE_INCREMENT_BY_QUANTITY = "2";
    /** 0：按动态值进行增量隔离 */
    public static final String TYPE_DYNAMIC = "0";
    /** 1：按固定值进行增量隔离 */
    public static final String TYPE_FIX_VALUE = "1";
    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;
    @Autowired
    private CmsBtStockSeparateItemDaoExt cmsBtStockSeparateItemDaoExt;
    @Autowired
    private CmsBtStockSeparateIncrementItemDaoExt cmsBtStockSeparateIncrementItemDaoExt;
    @Autowired
    private CmsBtStockSalesQuantityDaoExt cmsBtStockSalesQuantityDaoExt;
    @Autowired
    private CmsBtTasksStockDaoExt cmsBtTasksStockDaoExt;
    @Autowired
    private CmsBtTasksIncrementStockDaoExt cmsBtTasksIncrementStockDaoExt;
    @Autowired
    private ImsBtLogSynInventoryDao imsBtLogSynInventoryDao;

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

        // 取得该渠道下隔离中的task
        List<Integer> listSeparateTaskId = new ArrayList<>();
        sqlParam.clear();
        sqlParam.put("channelIdWhere", channelId);
        sqlParam.put("revertTimeGt", DateTimeUtil.getNow());
        List<Map<String, Object>> listPlatform = cmsBtTasksStockDaoExt.selectStockSeparatePlatform(sqlParam);
        listPlatform.forEach(data -> {
            Integer taskId = (Integer) data.get("task_id");
            if (!listSeparateTaskId.contains(taskId)) {
                listSeparateTaskId.add(taskId);
            }
        });

        // 取得该渠道下所有隔离库存
        Map<String,Integer> skuStockSeparateAll = new HashMap<>();
        if (listSeparateTaskId.size() > 0) {
            sqlParam.clear();
            // 状态 = 3：隔离成功
            sqlParam.put("status", STATUS_SEPARATE_SUCCESS);
            sqlParam.put("taskIdList", listSeparateTaskId);
            List<Map<String, Object>> listStockSeparate = cmsBtStockSeparateItemDaoExt.selectStockSeparateItem(sqlParam);
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
        }

        // 取得该渠道下隔离中的task的增量隔离任务
        List<Integer> listIncrementTaskId = new ArrayList<>();
        sqlParam.clear();
        sqlParam.put("taskIdList", listSeparateTaskId);
        List<Map<String, Object>> listIncTask = cmsBtTasksIncrementStockDaoExt.selectStockSeparateIncrementTask(sqlParam);
        listIncTask.forEach(data -> {
            Integer subTaskId = (Integer) data.get("id");
            if (!listIncrementTaskId.contains(subTaskId)) {
                listIncrementTaskId.add(subTaskId);
            }
        });

        // 取得增量隔离库存
        Map<String,Integer> skuStockIncrementAll = new HashMap<>();
        if (listIncrementTaskId.size() > 0) {
            sqlParam.clear();
            // 状态 = 3：增量成功
            sqlParam.put("status", STATUS_INCREMENT_SUCCESS);
            sqlParam.put("subTaskIdList", listIncrementTaskId);
            List<Map<String, Object>> listStockIncrement = cmsBtStockSeparateIncrementItemDaoExt.selectStockSeparateIncrement(sqlParam);
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
        }

        // 取得销售数量
        Map<String,Integer> skuStockSalesAll = new HashMap<>();
        sqlParam.clear();
        sqlParam.put("channelId", channelId);
        sqlParam.put("endFlg", NOT_END);
        List<Map<String, Object>> listStockSalesQuantity = cmsBtStockSalesQuantityDaoExt.selectStockSalesQuantity(sqlParam);
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

        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("channelIdWhere", channelId);
        sqlParam.put("revertTimeGt", DateTimeUtil.getNow());
        List<Map<String, Object>> resultData = cmsBtTasksStockDaoExt.selectStockSeparatePlatform(sqlParam);
        resultData.forEach(data -> {
            Integer cartId = (Integer) data.get("cart_id");
            if (!listSeparateCartId.contains(cartId)) {
                listSeparateCartId.add(cartId);
            }
        });

        return listSeparateCartId;
    }

    /**
     * 取得该渠道下未被隔离的平台(共享平台)
     *
     * @param channelId 渠道id
     * @param listCartIdAll 全部平台
     * @param listCartIdExcept 除外的平台
     * @return
     */
    public List<Integer> getShareCartId(String channelId, List<Integer> listCartIdAll, List<Integer> listCartIdExcept) {
        List<Integer> listShareCartId = new ArrayList<>();

        if (listCartIdAll == null || listCartIdAll.size() == 0) {
            // 渠道对应的所有销售平台取得
            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList.forEach(cartInfo -> listShareCartId.add(Integer.parseInt(cartInfo.getValue())));
        } else {
            listShareCartId.addAll(listCartIdAll);
        }

        // 取得该渠道下已经隔离的平台,并去除
        List<Integer> listSeparateCartId = getSeparateCartId(channelId);
        if (listSeparateCartId != null && listSeparateCartId.size() > 0) {
            removeListValue(listShareCartId, listSeparateCartId);
        }

        // 去除用户设定一定要去除的平台
        if (listCartIdExcept != null && listCartIdExcept.size() > 0) {
            removeListValue(listShareCartId, listCartIdExcept);
        }

        return listShareCartId;
    }

    /**
     * 去除list值
     *
     * @param listSource 去除的list对象
     * @param listRemove 要去除的值
     */
    public void removeListValue(List<Integer> listSource, List<Integer> listRemove) {
        listRemove.forEach(val -> listSource.remove(val));
    }

    /**
     * ims_bt_log_syn_inventory插入Map生成
     *
     * @param channelId 渠道id
     * @param cartId 平台
     * @param sku sku
     * @param qty 数量
     * @param synType 0、全量；2、增量;
     * @param separateSeq 隔离的数据的seq(null(默认值0):隔离对象外)
     * @param separateStatus 隔离状态(2：隔离中, 6：还原中， null：隔离对象外或增量时)
     * @param creater 创建者
     */
    public Map<String, Object> createMapImsBtLogSynInventory(String channelId, Integer cartId, String sku, Integer qty, String synType, Integer separateSeq, String separateStatus, String creater) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("channelId",channelId);
        retMap.put("cartId",cartId);
        retMap.put("sku",sku);
        retMap.put("qty",qty);
        retMap.put("synType",synType);
        retMap.put("separateSeq",separateSeq);
        retMap.put("separateStatus",separateStatus);
        retMap.put("creater",creater);
        return retMap;
    }

    /**
     * ims_bt_log_syn_inventory插入处理
     *
     * @param listData 更新对象
     * @return 更新件数
     */
    public int insertImsBtLogSynInventory(List<Map<String, Object>> listData) {
        return imsBtLogSynInventoryDao.insertByList(listData);
    }
}
