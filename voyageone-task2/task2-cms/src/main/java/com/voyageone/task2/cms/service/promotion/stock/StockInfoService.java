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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存还原batch
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


    /**
     * 取得可用库存
     *
     * @param sku Sku
     * @param channelId 渠道id
     * @return 可用库存
     */
    public String getUsableStock(String sku, String channelId){
        // sku没有输入的情况
        if (StringUtils.isEmpty(sku)) {
            return "";
        }

        // 取得逻辑库存
        int logicStock = 0;
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sku", sku);
        sqlParam.put("channelId", channelId);
        Integer logicInventoryCnt = wmsBtLogicInventoryDao.selectLogicInventoryCnt(sqlParam);
        if (logicInventoryCnt != null) {
            logicStock = logicInventoryCnt;
        }

        // 取得隔离库存
        int stockSeparate = 0;
        Map<String,Object> sqlParam1 = new HashMap<String,Object>();
        sqlParam1.put("sku", sku);
        // 状态 = 3：隔离成功
        sqlParam1.put("status", STATUS_SEPARATE_SUCCESS);
        Integer stockSeparateSuccessQty =  cmsBtStockSeparateItemDao.selectStockSeparateSuccessQty(sqlParam1);
        if (stockSeparateSuccessQty != null) {
            stockSeparate =  stockSeparateSuccessQty;
        }


        // 取得增量隔离库存
        int stockIncrementSeparate = 0;
        Map<String,Object> sqlParam2 = new HashMap<String,Object>();
        sqlParam2.put("sku", sku);
        // 状态 = 3：增量成功
        sqlParam2.put("status", STATUS_INCREMENT_SUCCESS);
        Integer stockSeparateIncrementSuccessQty =  cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementSuccessQty(sqlParam2);
        if (stockSeparateIncrementSuccessQty != null) {
            stockIncrementSeparate =  stockSeparateIncrementSuccessQty;
        }

        // 取得平台的销售数量
        int stockSalesQuantity = 0;
        Map<String,Object> sqlParam3 = new HashMap<String,Object>();
        sqlParam3.put("channelId", channelId);
        sqlParam3.put("sku", sku);
        sqlParam3.put("endFlg", "0");
        Integer stockSalesQuantityQty =  cmsBtStockSalesQuantityDao.selectStockSalesQuantityQty(sqlParam3);
        if (stockSalesQuantityQty != null) {
            stockSalesQuantity =  stockSalesQuantityQty;
        }

        // 可用库存数 = 逻辑库存 - （隔离库存 + 增量隔离库 - 平台的销售数量)
        int usableStockInt = logicStock - (stockSeparate + stockIncrementSeparate - stockSalesQuantity);
        if (usableStockInt < 0 ) {
            usableStockInt = 0;
        }

        return String.valueOf(usableStockInt);
    }
}
