package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
import com.voyageone.service.bean.cms.task.stock.StockIncrementExcelBean;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tag Service
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class StockSeparateService extends BaseService {

    @Autowired
    private CmsBtTasksDao tasksDao;

    @Autowired
    private CmsBtTasksIncrementStockDaoExt cmsBtTasksIncrementStockDaoExt;

    @Autowired
    private CmsBtStockSeparateItemDaoExt cmsBtStockSeparateItemDaoExt;

    @Autowired
    private CmsBtTasksStockDaoExt cmsBtTasksStockDaoExt;

    @Autowired
    private CmsBtStockSeparateIncrementItemDaoExt cmsBtStockSeparateIncrementItemDaoExt;

    @Autowired
    private CmsBtStockSalesQuantityDaoExt cmsBtStockSalesQuantityDaoExt;

    public List<Map<String, Object>> getStockSeparateIncrementTask(Map<String, Object> param) {
        return cmsBtTasksIncrementStockDaoExt.selectStockSeparateIncrementTask(param);
    }

    public List<Map<String, Object>> getStockSeparatePlatFormInfoById(int cartId, String revertTime, String channelId) {
        return cmsBtTasksStockDaoExt.selectStockSeparatePlatFormInfoById(cartId, revertTime, channelId);
    }

    public List<Map<String, Object>> getPlatformStockSeparateList(Map<String, Object> param) {
        return cmsBtTasksStockDaoExt.selectStockSeparatePlatform(param);
    }

    public List<Map<String, Object>> getStockSeparatePlatFormInfoMapByTaskID(String taskId) {
        return cmsBtTasksStockDaoExt.selectStockSeparatePlatFormInfoMapByTaskID(taskId);
    }

    public List<Map<String, Object>> getStockSeparateItem(Map<String, Object> sqlParam) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateItem(sqlParam);
    }

    @VOTransactional
    public void removeStockSeparateTask(int taskId) {
        // 删除库存隔离表中的数据
        Map<String, Object> sqlParam1 = new HashMap<>();
        sqlParam1.put("taskId", taskId);
        cmsBtStockSeparateItemDaoExt.deleteStockSeparateItem(sqlParam1);

        // 删除隔离任务/平台基本信息表中的数据
        Map<String, Object> sqlParam2 = new HashMap<>();
        sqlParam2.put("taskId", taskId);
        cmsBtTasksStockDaoExt.deleteStockSeparatePlatform(sqlParam2);

        // 删除任务表中的数据
        tasksDao.delete(taskId);
    }

    // TODO 因为梁兄帮promotion stock修改了将dao和service的访问,不知道这个方法对应的原始方法是哪个,我暂时注释掉-edward
//    public List<String>  getStockSeparateItemPageSku(Map<String, Object> sqlParam){
//        return cmsBtStockSeparateItemDao.selectStockSeparateItemPageSku(sqlParam);
//    }

    public int getStockSeparateItemHistoryCnt(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateItemHistoryCnt(param);
    }

    public Integer getStockSeparateItemByStatus(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateItemByStatus(param);
    }

    public List<Map<String, Object>> getStockSeparateItemBySqlMap(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateItemBySqlMap(param);
    }

    public List<Object> getStockSeparateItemBySqlObject(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateItemBySqlObject(param);
    }

    public List<Map<String, Object>> getStockSeparateDetailAll(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateDetailAll(param);
    }

    public List<StockExcelBean> getExcelStockInfo(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectExcelStockInfo(param);
    }

    public List<Map<String, Object>> getExcelStockErrorInfo(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectExcelStockErrorInfo(param);
    }

    public int addStockSeparateItem(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.insertStockSeparateItem(param);
    }

    @VOTransactional
    public int updateStockSeparateItem(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.updateStockSeparateItem(param);
    }

    @VOTransactional
    public int removeStockSeparateItem(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.deleteStockSeparateItem(param);
    }

    public List<Map<String, Object>> getStockSeparateIncrementItemBySql(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.selectStockSeparateIncrementItemBySql(param);
    }

    public int getStockSeparateIncrementItemHistoryCnt(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.selectStockSeparateIncrementItemHistoryCnt(param);
    }

    public List<Map<String, Object>> getStockSeparateIncrement(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.selectStockSeparateIncrement(param);
    }

    public List<StockIncrementExcelBean> getExcelStockIncrementInfo(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.selectExcelStockIncrementInfo(param);
    }

    public Integer getStockSeparateIncrementItemByStatus(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.selectStockSeparateIncrementItemByStatus(param);
    }

    public Integer getStockSeparateIncrementSuccessQty(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.selectStockSeparateIncrementSuccessQty(param);
    }

    @VOTransactional
    public int saveStockSeparateIncrementItem(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.updateStockSeparateIncrementItem(param);
    }

    @VOTransactional
    public int removeStockSeparateIncrementItem(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.deleteStockSeparateIncrementItem(param);
    }

    @VOTransactional
    public int updateStockSeparateIncrementItem(Map<String, Object> param) {
        return cmsBtStockSeparateIncrementItemDaoExt.updateStockSeparateIncrementItem(param);
    }

    /**
     * 导入文件数据更新变更方式
     *
     * @param saveData  保存对象
     * @param mapSku    原隔离成功的sku(Map<cartId,List<sku>>)，用于更新cms_bt_stock_sales_quantity（隔离平台实际销售数据表）的end_flg为1：结束
     * @param taskId    任务id
     * @param creater   创建者/更新者
     * @param channelId 渠道id
     */
    @VOTransactional
    public void importExcelFileStockUpdate(List<StockExcelBean> saveData, Map<String, List<String>> mapSku, String taskId, String creater, String channelId) {
        // 变更方式
        Map<String, Object> mapSaveData = new HashMap<>();
        mapSaveData.put("taskId", taskId);
        mapSaveData.put("modifier", creater);
        for (StockExcelBean bean : saveData) {
            mapSaveData.put("sku", bean.getSku());
            mapSaveData.put("cartId", bean.getCartId());
            mapSaveData.put("separateQty", bean.getSeparateQty());
            mapSaveData.put("status", StringUtils.null2Space(bean.getStatus()));

            cmsBtStockSeparateItemDaoExt.updateStockSeparateItem(mapSaveData);
        }

        mapSaveData.clear();
        mapSaveData.put("endFlg", "1");
        mapSaveData.put("modifier", creater);
        mapSaveData.put("channelId", channelId);
        for (String cartId : mapSku.keySet()) {
            mapSaveData.put("cartId", cartId);
            List<String> listSku = mapSku.get(cartId);
            int index = 0;
            for (; index + 500 < listSku.size(); index = index + 500) {
                mapSaveData.put("skuList", mapSku.get(cartId).subList(index, index + 500));
                cmsBtStockSalesQuantityDaoExt.updateStockSalesQuantity(mapSaveData);
            }
            mapSaveData.put("skuList", mapSku.get(cartId).subList(index, listSku.size()));
            cmsBtStockSalesQuantityDaoExt.updateStockSalesQuantity(mapSaveData);
        }
    }

    /**
     * 导入文件数据更新(增量方式)
     *
     * @param saveData  保存对象
     * @param taskId    ?
     * @param creater   创建者/更新者
     * @param channelId 渠道id
     */
    @VOTransactional
    public void importExcelFileStockAdd(List<StockExcelBean> saveData, String taskId, String creater, String channelId) {
        // 增量方式
        List<Map<String, Object>> listSaveData = new ArrayList<>();
        for (StockExcelBean bean : saveData) {
            Map<String, Object> mapSaveData;
            try {
                mapSaveData = PropertyUtils.describe(bean);
            } catch (Exception e) {
                // 导入文件有数据异常
                throw new BusinessException("7000053");
            }

            mapSaveData.put("taskId", taskId);
            mapSaveData.put("creater", creater);
            mapSaveData.put("channelId", channelId);

            listSaveData.add(mapSaveData);
            if (listSaveData.size() == 200) {
                cmsBtStockSeparateItemDaoExt.insertStockSeparateItemByList(listSaveData);
                listSaveData.clear();
            }
        }

        if (listSaveData.size() > 0) {
            cmsBtStockSeparateItemDaoExt.insertStockSeparateItemByList(listSaveData);
            listSaveData.clear();
        }
    }

    /**
     * 导入文件数据更新(重置方式)
     *
     * @param insertData insert对象
     * @param updateData update对象
     * @param subTaskId  任务id
     * @param creater    创建者/更新者
     * @param channelId  渠道id
     */
    @VOTransactional
    public void saveDeleteImportData(List<StockIncrementExcelBean> insertData, List<StockIncrementExcelBean> updateData, String subTaskId, String creater, String channelId) {
        // 重置方式
        // 此任务所有数据删除
        cmsBtStockSeparateIncrementItemDaoExt.deleteStockSeparateIncrementItem(new HashMap<String, Object>() {{
            this.put("subTaskId", subTaskId);
        }});
        // 插入数据
        insertImportData(insertData, subTaskId, creater, channelId);
    }

    /**
     * 导入文件数据更新（变更方式）
     *
     * @param insertData insert对象
     * @param updateData update对象
     * @param subTaskId  任务id
     * @param creater    创建者/更新者
     * @param channelId  渠道id
     */
    @VOTransactional
    public void saveUpdateImportData(List<StockIncrementExcelBean> insertData, List<StockIncrementExcelBean> updateData, String subTaskId, String creater, String channelId) {
        // 变更方式
        // 更新数据
        updateImportData(updateData, subTaskId, creater);
        // 插入数据
        insertImportData(insertData, subTaskId, creater, channelId);
    }

    /**
     * 导入文件数据插入更新
     *
     * @param insertData insert对象
     * @param subTaskId  任务id
     * @param creater    创建者/更新者
     * @param channelId  渠道id
     */
    private void insertImportData(List<StockIncrementExcelBean> insertData, String subTaskId, String creater, String channelId) {
        List<Map<String, Object>> listSaveData = new ArrayList<>();
        for (StockIncrementExcelBean bean : insertData) {
            Map<String, Object> mapSaveData;
            try {
                mapSaveData = PropertyUtils.describe(bean);
            } catch (Exception e) {
                // 导入文件有数据异常
                throw new BusinessException("7000053");
            }

            mapSaveData.put("subTaskId", subTaskId);
            mapSaveData.put("creater", creater);
            mapSaveData.put("channelId", channelId);

            listSaveData.add(mapSaveData);
            if (listSaveData.size() == 200) {
                cmsBtStockSeparateIncrementItemDaoExt.insertStockSeparateIncrementItemByList(listSaveData);
                listSaveData.clear();
            }
        }

        if (listSaveData.size() > 0) {
            cmsBtStockSeparateIncrementItemDaoExt.insertStockSeparateIncrementItemByList(listSaveData);
            listSaveData.clear();
        }
    }

    /**
     * 导入文件数据变更更新
     *
     * @param updateData update对象
     * @param subTaskId  任务id
     * @param creater    创建者/更新者
     */
    private void updateImportData(List<StockIncrementExcelBean> updateData, String subTaskId, String creater) {
        for (StockIncrementExcelBean bean : updateData) {
            Map<String, Object> mapSaveData = new HashMap<>();
            mapSaveData.put("subTaskId", subTaskId);
            mapSaveData.put("sku", bean.getSku());
            mapSaveData.put("incrementQty", bean.getIncrementQty());
            mapSaveData.put("fixFlg", bean.getFixFlg());
            mapSaveData.put("modifier", creater);
            cmsBtStockSeparateIncrementItemDaoExt.updateStockSeparateIncrementItem(mapSaveData);
        }
    }

    /**
     * 根据SKU取得库存隔离数将隔离数据插入表中
     */
    @VOTransactional
    public void saveStockSeparateItem(Map<String, Object> separateHashMaps, Map<String, Object> allSkuProHash, Boolean onlySku, CmsBtTasksBean cmsBtTasksBean, List<Map<String, String>> separatePlatformMapList) {

        /**
         * insert cmsBtTasks
         */
        tasksDao.insert(cmsBtTasksBean);

        String taskID = String.valueOf(cmsBtTasksBean.getId());

        /**
         * insert cmsBtStockSeparatePlatformInfo
         */
        for (Map<String, String> separatePlatformMap : separatePlatformMapList) {
            separatePlatformMap.put("task_id", taskID);
            cmsBtTasksStockDaoExt.insert(separatePlatformMap);
        }

        /**
         * insert cmsBtStockSeparateItem
         */
        List<Map<String, Object>> allSku = new ArrayList<>();
        //循环所有的SKU
        for (Map.Entry<String, Object> allSkuProHashEntry : allSkuProHash.entrySet()) {
            //判断是否在隔离
            Map<String, Object> aSkuProHash = new HashMap<>();
            Map<String, Object> proMapValue = (Map<String, Object>) allSkuProHashEntry.getValue();
            if (separateHashMaps.keySet().contains(allSkuProHashEntry.getKey())) {
                //任务ID
                aSkuProHash.put("taskId", taskID);
                //渠道ID
                aSkuProHash.put("channelId", proMapValue.get("channel_id").toString());
                //model
                aSkuProHash.put("productModel", proMapValue.get("product_model").toString());
                //code
                aSkuProHash.put("productCode", proMapValue.get("product_code").toString());
                //sku
                aSkuProHash.put("sku", proMapValue.get("sku").toString());
                //cart_id
                aSkuProHash.put("cartId", proMapValue.get("cart_id").toString());
                //属性1（品牌）
                aSkuProHash.put("property1", proMapValue.get("property1").toString());
                //属性2（英文短描述）
                aSkuProHash.put("property2", proMapValue.get("property2").toString());
                //属性3（性别）
                aSkuProHash.put("property3", proMapValue.get("property3").toString());
                //属性4（SIZE）
                aSkuProHash.put("property4", proMapValue.get("property4").toString());
                //可用库存(取得可用库存)
                aSkuProHash.put("qty", proMapValue.get("qty").toString());
                //隔离库存比例
                //判断是否只导入SKU
                if (onlySku) {
                    aSkuProHash.put("separateQty", 0);
                } else {
                    aSkuProHash.put("separateQty", proMapValue.get("separate_qty").toString());
                }
                //状态(0：未进行； 1：等待增量； 2：增量成功； 3：增量失败； 4：还原)
                aSkuProHash.put("status", "0");
                //创建者
                aSkuProHash.put("creater", proMapValue.get("creater").toString());
                //更新者
                aSkuProHash.put("modifier", proMapValue.get("modifier").toString());
                allSku.add(aSkuProHash);
            } else {
                //任务ID
                aSkuProHash.put("taskId", taskID);
                //model
                aSkuProHash.put("productModel", proMapValue.get("product_model").toString());
                //code
                aSkuProHash.put("productCode", proMapValue.get("product_code").toString());
                //渠道ID
                aSkuProHash.put("channelId", proMapValue.get("channel_id").toString());
                //sku
                aSkuProHash.put("sku", proMapValue.get("sku").toString());
                //cart_id
                aSkuProHash.put("cartId", proMapValue.get("cart_id").toString());
                //属性1（品牌）
                aSkuProHash.put("property1", proMapValue.get("property1").toString());
                //属性2（英文短描述）
                aSkuProHash.put("property2", proMapValue.get("property2").toString());
                //属性3（性别）
                aSkuProHash.put("property3", proMapValue.get("property3").toString());
                //属性4（SIZE）
                aSkuProHash.put("property4", proMapValue.get("property4").toString());
                //可用库存
                aSkuProHash.put("qty", proMapValue.get("qty").toString());
                //隔离库存
                //隔离库存
                aSkuProHash.put("separateQty", "-1");
                //状态(0：未进行； 1：等待增量； 2：增量成功； 3：增量失败； 4：还原)
                aSkuProHash.put("status", "");
                //创建者
                aSkuProHash.put("creater", proMapValue.get("creater").toString());
                //更新者
                aSkuProHash.put("modifier", proMapValue.get("modifier").toString());
                allSku.add(aSkuProHash);
            }
            //批量更新
            if (allSku.size() > 500) {
                //将数据插入cms_bt_stock_separate_item表中
                cmsBtStockSeparateItemDaoExt.insertStockSeparateItemByList(allSku);
                allSku.clear();
            }
        }
        if (allSku.size() > 0) {
            //将数据插入cms_bt_stock_separate_item表中
            cmsBtStockSeparateItemDaoExt.insertStockSeparateItemByList(allSku);
            allSku.clear();
        }
    }

    /**
     * 更新数据库cms_bt_stock_separate_platform_info
     */
    @VOTransactional
    public void updateStockSeparatePlatformList(List<Map<String, Object>> updateStockSeparatePlatformList) {
        for (Map<String, Object> separatePlatformMap : updateStockSeparatePlatformList) {
            cmsBtTasksStockDaoExt.updateStockSeparatePlatform(separatePlatformMap);
        }
    }

    /**
     * 将增量任务信息插入到cms_bt_stock_separate_increment_task表
     * 根据allSkuProHash和SeparateHashMaps将Sku基本情报信息到和可用库存插入到cms_bt_stock_separate_increment_item表
     */
    @VOTransactional
    public void saveStockSeparateIncrementTaskAndItemList(Map<String, Object> stockSeparateIncrementTaskMap, Map<String, Object> allSkuHash) {

        //将增量任务信息插入到cms_bt_stock_separate_increment_task表
        cmsBtTasksIncrementStockDaoExt.insertStockSeparateIncrementTask(stockSeparateIncrementTaskMap);
        //任务ID
        String subTaskId = String.valueOf(stockSeparateIncrementTaskMap.get("id"));

        //所有要增量的Sku属性值
        List<Map<String, Object>> allSku = new ArrayList<>();
        for (Map.Entry<String, Object> skuMap : allSkuHash.entrySet()) {
            //单条Sku的属性值
            Map<String, Object> skuProHash = new HashMap<>();
            //Sku对应的属性值
            Map<String, Object> skuHash = (Map<String, Object>) skuMap.getValue();
            //增量任务ID
            skuProHash.put("subTaskId", subTaskId);
            //销售渠道
            skuProHash.put("channelId", skuHash.get("channelId"));
            //产品model
            skuProHash.put("productModel", skuHash.get("productModel"));
            //产品code
            skuProHash.put("productCode", skuHash.get("productCode"));
            //产品Sku
            skuProHash.put("sku", skuMap.getKey());
            //可用库存
            skuProHash.put("qty", skuHash.get("qty"));
            //增量库存
            skuProHash.put("incrementQty", skuHash.get("incrementQty"));
            //属性1（品牌）
            skuProHash.put("property1", skuHash.get("property1"));
            //属性2（英文短描述）
            skuProHash.put("property2", skuHash.get("property2"));
            //属性3（性别）
            skuProHash.put("property3", skuHash.get("property3"));
            //属性4（SIZE）
            skuProHash.put("property4", skuHash.get("property4"));
            //状态 0：未进行； 1：等待增量； 2：增量成功； 3：增量失败； 4：还原
            skuProHash.put("status", skuHash.get("status"));
            //固定值隔离标志位 0：按动态值进行增量隔离； 1：按固定值进行增量隔离
            skuProHash.put("fixFlg", "0");
            //更新者
            skuProHash.put("creater", skuHash.get("creater"));
            //所有增量的Sku属性值
            allSku.add(skuProHash);
            //批量更新
            if (allSku.size() > 500) {
                cmsBtStockSeparateIncrementItemDaoExt.insertStockSeparateIncrementItemByList(allSku);
                allSku.clear();
            }
        }
        if (allSku.size() > 0) {
            cmsBtStockSeparateIncrementItemDaoExt.insertStockSeparateIncrementItemByList(allSku);
            allSku.clear();
        }
    }

    /**
     * 如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表
     */
    @VOTransactional
    public void updateIncrementTaskByTaskID(Map<String, Object> param) {
        Map<String, Object> mapSaveData = new HashMap<>();
        //任务名
        mapSaveData.put("subTaskName", param.get("incrementTaskName"));
        //增量任务ID
        mapSaveData.put("subTaskId", param.get("incrementSubTaskId"));
        //创建者
        mapSaveData.put("created", param.get("userName"));
        //更新者
        mapSaveData.put("modified", param.get("userName"));
        cmsBtTasksIncrementStockDaoExt.updateStockSeparateIncrementTask(mapSaveData);
    }

    /**
     * 删除增量库存隔离任务
     *
     * @param subTaskId 子任务id
     */
    @VOTransactional
    public void delStockSeparateIncrementTask(String subTaskId) {
        // 删除增量库存隔离表中的数据
        Map<String, Object> sqlParam1 = new HashMap<>();
        sqlParam1.put("subTaskId", subTaskId);
        cmsBtStockSeparateIncrementItemDaoExt.deleteStockSeparateIncrementItem(sqlParam1);

        // 删除增量库存隔离任务表中的数据
        Map<String, Object> sqlParam2 = new HashMap<>();
        sqlParam2.put("subTaskId", subTaskId);
        cmsBtTasksIncrementStockDaoExt.deleteStockSeparateIncrementTask(sqlParam2);
    }

    public List<Map<String, Object>> getStockSalesQuantity(Map<String, Object> param) {
        return cmsBtStockSalesQuantityDaoExt.selectStockSalesQuantity(param);
    }

    public Integer getStockSeparateSuccessQty(Map<String, Object> param) {
        return cmsBtStockSeparateItemDaoExt.selectStockSeparateSuccessQty(param);
    }

    public Integer getStockSalesQuantityQty(Map<String, Object> param) {
        return cmsBtStockSalesQuantityDaoExt.selectStockSalesQuantityQty(param);
    }

    @VOTransactional
    public void updateStockSalesQuantityList(List<Map<String, Object>> updateStockSeparateItemList, List<Map<String, Object>> updateStockSalesQuantityList) {
        for (Map<String, Object> updateMap : updateStockSeparateItemList) {
            int updateCnt = cmsBtStockSeparateItemDaoExt.updateStockSeparateItem(updateMap);
            if (updateCnt != 1) {
                // 明细对象不存在
                throw new BusinessException("7000023");
            }
        }

        for (Map<String, Object> updateMap : updateStockSalesQuantityList) {
            cmsBtStockSalesQuantityDaoExt.updateStockSalesQuantity(updateMap);
        }
    }

    /**
     * 还原库存隔离
     */
    @VOTransactional
    public void stockSeparateRevert(Map<String, Object> updateStockSeparateItem, List<Map<String, Object>> updateStockSalesQuantityList) {
        if (updateStockSeparateItem != null) {
            int updateCnt = cmsBtStockSeparateItemDaoExt.updateStockSeparateItem(updateStockSeparateItem);
            if (updateCnt == 0) {
                // 没有可以还原的数据
                throw new BusinessException("7000038");
            }
        }

        for (Map<String, Object> sqlParam1 : updateStockSalesQuantityList) {
            cmsBtStockSalesQuantityDaoExt.updateStockSalesQuantity(sqlParam1);
        }
    }
}
