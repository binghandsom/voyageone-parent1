package com.voyageone.web2.cms.views.promotion.task;
;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jeff.duan on 2016/03/04.
 */
@Service
public class CmsTaskStockService extends BaseAppService {

    @Autowired
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

    @Autowired
    private WmsBtLogicInventoryDao wmsBtLogicInventoryDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    /**
     * 取得库存隔离数据各种状态的数量
     *
     * @param param 客户端参数
     * @return 某种状态的数量
     */
    public List<Map<String,Object>>  getStockStatusCount(Map param){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getStockStatusCountSql(param));
        List<Map<String,Object>> statusCountList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);
        return statusCountList;
    }

    /**
     * 库存隔离明细sku总数
     *
     * @param param 客户端参数
     * @return 库存隔离明细sku总数
     */
    public int getStockSkuCount(Map param){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getStockSkuCountSql(param));
        List<Object> countInfo = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlObject(sqlParam);
        return  Integer.parseInt(String.valueOf(countInfo.get(0)));
    }


    /**
     * 取得属性列表
     * 属性列表数据结构
     *    {"value":"property1", "name":"品牌", "logic":"", "type":"", "show":false},
     *    {"value":"property2", "name":"英文短描述", "logic":"Like", "type":"", "show":false},
     *    {"value":"property3", "name":"性别", "logic":"", "type":"", "show":false}，
     *    {"value":"property4", "name":"Size", "logic":"", "type":"int", "show":false}...
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPropertyList(Map param){
        List<Map<String,Object>> propertyList = new ArrayList<Map<String,Object>>();
        // 取得动态属性
        List<TypeChannelBean> dynamicPropertyList = TypeChannel.getTypeList("dynamicProperty", (String) param.get("channelId"));
        for (TypeChannelBean dynamicProperty : dynamicPropertyList) {
            if (((String) param.get("lang")).equals(dynamicProperty.getLang_id())) {
                Map<String, Object> propertyItem = new HashMap<String, Object>();
                propertyItem.put("property", dynamicProperty.getValue());
                propertyItem.put("name", dynamicProperty.getName());
                propertyItem.put("logic", dynamicProperty.getAdd_name1());
                propertyItem.put("type", dynamicProperty.getAdd_name2());
                propertyItem.put("show", false);
                propertyItem.put("value", "");
                propertyList.add(propertyItem);
            }
        }
        return propertyList;
    }

    /**
     * 取得任务对应平台信息列表
     * 平台信息列表数据结构
     *    {"cartId":"23", "cartName":"天猫国际"},
*         {"cartId":"27", "cartName":"聚美优品"}...
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPlatformList(Map param){
        return cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(param);
    }

    /**
     * 取得库存隔离明细列表
     * 例：
     * "stockList": [ {"model":"35265", "code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50",
     *                                                         "platformStock":[{"cartId":"23", "separationQty":"30", "status":"隔离成功"},
     *                                                                          {"cartId":"27", "separationQty":"10", "status":"隔离失败"}]},
     *                   {"model":"35265", "code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"80",
     *                                                         "platformStock":[{"cartId":"23", "separationQty":"-1", "status":""},
     *                                                                          {"cartId":"27", "separationQty":"10", "status":"还原成功"}]},
     *
     *
     * @param param 客户端参数
     * @return 库存隔离明细列表
     */
    public List<Map<String,Object>> getCommonStockList(Map param){
        // 库存隔离明细列表
        List<Map<String,Object>> stockList = new ArrayList<Map<String,Object>>();
        // 平台列表
        List<Map<String,Object>> platformList = (List<Map<String,Object>>) param.get("platformList");

        // 获取当页表示的库存隔离数据
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getStockPageSkuSql(param, "1"));
        List<Map<String,Object>> stockAllList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);

        // 画面上一行的数据
        Map<String, Object> lineInfo = null;
        // 画面上一行里平台相关的数据
        List<Map<String, Object>> linePlatformInfoList = null;
        for (Map<String,Object> stockInfo : stockAllList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String sku = (String) stockInfo.get("sku");
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");
            String qty = String.valueOf(stockInfo.get("qty"));
            lineInfo = new HashMap<String, Object>();
            linePlatformInfoList = new ArrayList<Map<String, Object>>();
            stockList.add(lineInfo);
            lineInfo.put("model", model);
            lineInfo.put("code", code);
            lineInfo.put("sku", sku);
            lineInfo.put("property1", property1);
            lineInfo.put("property2", property2);
            lineInfo.put("property3", property3);
            lineInfo.put("property4", property4);
            lineInfo.put("qty", qty);
            lineInfo.put("platformStock", linePlatformInfoList);
            int index = 1;
            for (Map<String,Object> platformInfo : platformList) {
                String separateQty = String.valueOf(stockInfo.get("separate_qty" + String.valueOf(index)));
                String statusName = (String) stockInfo.get("status_name" + String.valueOf(index));
                Map<String, Object> linePlatformInfo = new HashMap<String, Object>();
                linePlatformInfo.put("cartId", platformInfo.get("cartId"));
                linePlatformInfo.put("separationQty", separateQty);
                linePlatformInfo.put("status", statusName);
                linePlatformInfoList.add(linePlatformInfo);
                index++;
            }
        }
        return stockList;
    }

    /**
     * 获取实时库存状态一页表示的Sku
     *
     * @param param 客户端参数
     * @return 实时库存状态一页表示的Sku
     */
//    public List<Object> geRealStockPageSkuList(Map param) {
//        Map<String,Object> sqlParam = new HashMap<String,Object>();
//        sqlParam.put("sql", getRealStockPageSkuSql(param));
//        List<Object> skuList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlObject(sqlParam);
//        return  skuList;
//    }

    /**
     * 库存隔离数据是否移到history表
     *
     * @param param 客户端参数
     * @return 库存隔离数据是否移到history表
     */
    public boolean isHistoryExist(Map param){
        return (cmsBtStockSeparateItemDao.selectStockSeparateItemHistoryCnt(param) !=  0) ? true : false;
    }

    /**
     * 取得实时库存表示状态(0:活动期间表示,1:活动结束后表示）
     *
     * @param param 客户端参数
     * @return 实时库存表示状态
     */
    public String getRealStockStatus(Map param){
        String status = "1";
        // 历史表存在数据时，判定为活动结束
        if ("_history".equals(param.get("tableNameSuffix"))) {
            return status;
        }

        // 取得任务下的平台平台信息
        Date now = DateTimeUtil.parse(DateTimeUtil.getNow());
        List<Map<String, Object>> platformList = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(param);
        for (Map<String, Object> platformInfo : platformList) {
            Date restoreSeparateTime = DateTimeUtil.parse((String) platformInfo.get("restore_separate_time"));
            if (restoreSeparateTime != null && now.getTime() - restoreSeparateTime.getTime() <= 0) {
                status = "0";
                break;
            }
        }
        return status;
    }



    /**
     * 取得实时库存状态列表
     *
     * @param param 客户端参数
     * @return 实时库存状态列表
     */
    public List<Map<String,Object>> getRealStockList(Map param){
        // 实时库存状态列表
        List<Map<String,Object>> realStockList = new ArrayList<Map<String,Object>>();
        // 平台列表
        List<Map<String,Object>> platformList = (List<Map<String,Object>>) param.get("platformList");

        // 取得一页中的sku基本信息（含逻辑库存）
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getStockPageSkuSql(param, "2"));
        List<Map<String,Object>> stockRealList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);
        if (stockRealList == null || stockRealList.size() == 0) {
            return realStockList;
        }
        // 一页中的sku列表（之后的检索用）
        List<String> skuList = new ArrayList<String>();
        for (Map<String,Object> stockInfo : stockRealList) {
            skuList.add((String) stockInfo.get("sku"));
        }

        // 取得一页中的sku所有平台的隔离库存（含非本任务的）
        Map<String,Object> sqlParam1 = new HashMap<String,Object>();
        sqlParam1.put("skuList", skuList);
        sqlParam1.put("status", "2");
        sqlParam1.put("tableNameSuffix", param.get("tableNameSuffix"));
        List<Map<String,Object>> stockSeparateList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam1);

        // sku库存隔离信息（所有任务所有平台的数据）
        Map<String,Integer> skuStockAllTask = new HashMap<String,Integer>();
        // sku库存隔离信息（该任务下的各个平台的数据）
        Map<String,Integer> skuStockByPlatform = new HashMap<String,Integer>();
        String currentTaskId = (String) param.get("taskId");
        if (stockSeparateList != null && stockSeparateList.size() > 0) {
            for (Map<String, Object> stockInfo : stockSeparateList) {
                String sku = (String) stockInfo.get("sku");
                String cartId = String.valueOf(stockInfo.get("cart_id"));
                Integer separateQty = (Integer) stockInfo.get("separate_qty");
                String taskId = String.valueOf(stockInfo.get("task_id"));
                // 加入到sku库存隔离信息（所有任务所有平台的数据）
                if (skuStockAllTask.containsKey(sku)) {
                    skuStockAllTask.put(sku, skuStockAllTask.get(sku) + separateQty);
                } else {
                    skuStockAllTask.put(sku, separateQty);
                }

                // 加入到sku库存隔离信息（该任务下的各个平台的数据）
                if (currentTaskId.equals(taskId)) {
                    skuStockByPlatform.put(sku + cartId, separateQty);
                }
            }
        }

        // 取得一页中的sku所有平台的增量隔离库存（含非本任务的）
        Map<String,Object> sqlParam2 = new HashMap<String,Object>();
        sqlParam2.put("skuList", skuList);
        sqlParam2.put("status", "2");
        sqlParam2.put("tableNameSuffix", param.get("tableNameSuffix"));
        List<Map<String,Object>> stockIncrementList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementSuccessAll(sqlParam2);

        // sku库存增量隔离信息（所有任务所有平台的数据）
        Map<String,Integer> skuStockIncrementAllTask = new HashMap<String,Integer>();
        // sku库存增量隔离信息（该任务下的各个平台的数据）
        Map<String,Integer> skuStockIncrementByPlatform = new HashMap<String,Integer>();
        if (stockIncrementList != null && stockIncrementList.size() > 0) {
            for (Map<String, Object> stockIncrementInfo : stockIncrementList) {
                String sku = (String) stockIncrementInfo.get("sku");
                String cartId = String.valueOf(stockIncrementInfo.get("cart_id"));
                Integer incrementQty = (Integer) stockIncrementInfo.get("increment_qty");
                String taskId = String.valueOf(stockIncrementInfo.get("task_id"));
                // 加入到sku库存增量隔离信息（所有任务所有平台的数据）
                if (skuStockIncrementAllTask.containsKey(sku)) {
                    skuStockIncrementAllTask.put(sku, skuStockIncrementAllTask.get(sku) + incrementQty);
                } else {
                    skuStockIncrementAllTask.put(sku, incrementQty);
                }

                // 加入到sku库存增量隔离信息（该任务下的各个平台的数据）
                if (currentTaskId.equals(taskId)) {
                    skuStockIncrementByPlatform.put(sku + cartId, incrementQty);
                }
            }
        }

        // 取得一页中的sku所有平台的销售数量
        Map<String,Object> sqlParam3 = new HashMap<String,Object>();
        sqlParam3.put("channelId", param.get("channelId"));
        sqlParam3.put("skuList", skuList);
        sqlParam3.put("endFlg", "0");
        List<Map<String,Object>> stockSalesQuantityList = cmsBtStockSalesQuantityDao.selectStockSalesQuantity(sqlParam3);

        // sku所有平台的销售数量信息（所有所有平台的数据）
        Map<String,Integer> skuStockSalesAll = new HashMap<String,Integer>();
        // sku各个平台的销售数量信息
        Map<String,Integer> skuStockSalesByPlatform = new HashMap<String,Integer>();
        if (stockSalesQuantityList != null && stockSalesQuantityList.size() > 0) {
            for (Map<String, Object> stockSalesQuantityInfo : stockSalesQuantityList) {
                String sku = (String) stockSalesQuantityInfo.get("sku");
                String cartId = String.valueOf(stockSalesQuantityInfo.get("cart_id"));
                Integer qty = (Integer) stockSalesQuantityInfo.get("qty");
                // 加入到sku所有平台的销售数量信息（所有所有平台的数据）
                if (skuStockSalesAll.containsKey(sku)) {
                    skuStockSalesAll.put(sku, skuStockSalesAll.get(sku) + qty);
                } else {
                    skuStockSalesAll.put(sku, qty);
                }

                // 加入到sku各个平台的销售数量信息
                skuStockSalesByPlatform.put(sku + cartId, qty);
            }
        }


        // 画面上一行的数据
        Map<String, Object> lineInfo = null;
        // 画面上一行里平台相关的数据
        List<Map<String, Object>> linePlatformInfoList = null;
        // 生成实时库存状态画面数据
        for (Map<String,Object> stockInfo : stockRealList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String sku = (String) stockInfo.get("sku");
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");

            lineInfo = new HashMap<String, Object>();
            linePlatformInfoList = new ArrayList<Map<String, Object>>();
            realStockList.add(lineInfo);
            lineInfo.put("model", model);
            lineInfo.put("code", code);
            lineInfo.put("sku", sku);
            lineInfo.put("property1", property1);
            lineInfo.put("property2", property2);
            lineInfo.put("property3", property3);
            lineInfo.put("property4", property4);
            // 某个sku的逻辑库存
            int logicStock = 0;
            if (stockInfo.get("qty_china") != null) {
                logicStock = (Integer) stockInfo.get("qty_china");
            }
            // 某个sku的隔离库存
            int stockSeparateAll = 0;
            if (skuStockAllTask.get(sku) != null) {
                stockSeparateAll =  skuStockAllTask.get(sku);
            }

            // 某个sku的增量隔离库存
            int stockSeparateIncrementAll = 0;
            if (skuStockIncrementAllTask.get(sku) != null) {
                stockSeparateIncrementAll =  skuStockIncrementAllTask.get(sku);
            }

            // 某个sku的各个隔离平台的销售数量
            int stockSalesQuantityAll = 0;
            if (skuStockSalesAll.get(sku) != null) {
                stockSalesQuantityAll =  skuStockSalesAll.get(sku);
            }
            int qty = 0;
            if (logicStock - (stockSeparateAll + stockSeparateIncrementAll - stockSalesQuantityAll) > 0 ) {
                qty = logicStock - (stockSeparateAll + stockSeparateIncrementAll - stockSalesQuantityAll);
            }
            lineInfo.put("qty", String.valueOf(qty));
            lineInfo.put("platformStock", linePlatformInfoList);

            int index = 1;
            for (Map<String,Object> platformInfo : platformList) {
                String cartId = (String) platformInfo.get("cartId");
                String separateQty = String.valueOf(stockInfo.get("separate_qty" + String.valueOf(index)));
                // 某个sku某个平台的隔离库存
                int stockSeparate = 0;
                if (skuStockByPlatform.get(sku + cartId) != null) {
                    stockSeparate =  skuStockByPlatform.get(sku + cartId);
                }
                // 某个sku某个平台的销售数量
                int stockSalesQuantity = 0;
                if (skuStockIncrementByPlatform.get(sku + cartId) != null) {
                    stockSalesQuantity =  skuStockIncrementByPlatform.get(sku + cartId);
                }

                // 某个sku某个平台的增量隔离库存
                int stockIncrementSeparate = 0;
                if (skuStockSalesByPlatform.get(sku + cartId) != null) {
                    stockIncrementSeparate =  skuStockSalesByPlatform.get(sku + cartId);
                }
                Map<String, Object> linePlatformInfo = new HashMap<String, Object>();
                linePlatformInfo.put("cartId", cartId);
                if ("-1".equals(separateQty)) {
                    linePlatformInfo.put("separationQty", "-1");
                } else {
                    linePlatformInfo.put("separationQty", String.valueOf(stockSeparate + stockSalesQuantity));
                    linePlatformInfo.put("salesQty", String.valueOf(stockIncrementSeparate));
                }
                linePlatformInfoList.add(linePlatformInfo);
                index++;
            }
        }

        return realStockList;
    }

    /**
     * 保存隔离库存明细
     *
     * @param param 客户端参数
     */
    public void saveRecord(Map param){
        // 取得任务id在history表中是否有数据
        boolean historyFlg = isHistoryExist(param);
        if (historyFlg) {
            throw new BusinessException("隔离任务已经结束，不能修改数据！");
        }

        List<Map<String,Object>> stockList = new ArrayList<Map<String,Object>>();
        if(param.get("index") == null) {
            stockList = (List<Map<String, Object>>) param.get("stockList");
        } else {
            stockList.add(((List<Map<String, Object>>) param.get("stockList")).get((Integer)param.get("index")));
        }
        checksSparationQty(stockList);
        String taskId = (String) param.get("taskId");
        simpleTransaction.openTransaction();
        try {
            List<String> skuList = new ArrayList<String>();
            for (Map<String, Object> stockInfo : stockList) {
                skuList.add((String) stockInfo.get("sku"));
            }
            // 取得这个页面所有sku的库存隔离信息
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            sqlParam.put("taskId", taskId);
            sqlParam.put("skuList", skuList);
//            sqlParam.put("cartId", cartId);
            sqlParam.put("tableNameSuffix", "");
            sqlParam.put("lang", param.get("lang"));
            List<Map<String, Object>> stockSeparateItemList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
            if (stockSeparateItemList == null || stockSeparateItemList.size() == 0) {
                simpleTransaction.rollback();
                throw new BusinessException("选择的明细不存在！");
            }
            // 这个页面所有sku的库存隔离信息Map
            Map<String, Object> skuInfo = new HashMap<String, Object>();
            // 生成这个页面所有sku的库存隔离信息Map
            for (Map<String, Object> stockSeparateItem : stockSeparateItemList) {
                String skuDB = String.valueOf(stockSeparateItem.get("sku"));
                String cartIdDB = String.valueOf(stockSeparateItem.get("cart_id"));
                skuInfo.put(skuDB + cartIdDB, stockSeparateItem);
            }
            for (Map<String, Object> stockInfo : stockList) {
                String sku = (String) stockInfo.get("sku");
                List<Map<String, Object>> platformStockList = (List<Map<String, Object>>) stockInfo.get("platformStock");
                for (Map<String, Object> platformInfo : platformStockList) {
                    String separationQty = (String) platformInfo.get("separationQty");
                    String status = (String) platformInfo.get("status");
                    String cartId = (String) platformInfo.get("cartId");
                    // sku + cartId在DB中存在的场合（隔离信息存在）
                    if (skuInfo.containsKey(sku + cartId)) {
                        Map<String, Object> recordDB = (Map<String, Object>) skuInfo.get(sku + cartId);
                        String separateQtyDB = String.valueOf(recordDB.get("separate_qty"));
                        String statusDB = String.valueOf(recordDB.get("status"));

                        // 画面的隔离库存 != DB的隔离库存是进行更新
                        if (!separationQty.equals(separateQtyDB) && !StringUtils.isEmpty(status)) {
                            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
                            sqlParam1.put("taskId", taskId);
                            sqlParam1.put("sku", sku);
                            sqlParam1.put("cartId", cartId);
                            sqlParam1.put("separateQty", separationQty);
                            // 导入前状态为"7：再修正"和"2 隔离成功"以外，则导入后状态为"0：未进行"，导入前状态为"7：再修正"或"2 隔离成功"，则导入后状态为"7：再修正"
                            String changedStatus = "";
                            if ("2".equals(statusDB) || "7".equals(statusDB)) {
                                sqlParam1.put("status", "7");
                                changedStatus = "7";
                            } else {
                                sqlParam1.put("status", "0");
                                changedStatus = "0";
                            }
                            int updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam1);
                            if (updateCnt == 1) {
                                String typeName = Type.getTypeName(63, changedStatus, (String) param.get("lang"));
                                platformInfo.put("status", typeName);
                            } else {
                                simpleTransaction.rollback();
                                throw new BusinessException("选择的明细不存在！");
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 删除隔离库存明细
     *
     * @param param 客户端参数
     */
    public void delRecord(Map param){
        // 取得任务id在history表中是否有数据
        boolean historyFlg = isHistoryExist(param);
        if (historyFlg) {
            throw new BusinessException("隔离任务已经结束，不能删除数据！");
        }
        simpleTransaction.openTransaction();
        // 取得这条sku明细对应的库存隔离信息
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("taskId", param.get("taskId"));
        sqlParam.put("sku", ((Map) param.get("stockInfo")).get("sku"));
        sqlParam.put("tableNameSuffix", "");
        sqlParam.put("lang", param.get("lang"));
        List<Map<String, Object>> stockSeparateItemList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
        if (stockSeparateItemList == null || stockSeparateItemList.size() == 0) {
            simpleTransaction.rollback();
            throw new BusinessException("选择的明细不存在！");
        }
        for (Map<String, Object> stockSeparateItem : stockSeparateItemList) {
            if (!"0".equals(stockSeparateItem.get("status"))) {
                throw new BusinessException("选择的明细不能删除！");
            }
        }

        Map<String, Object> sqlParam1 = new HashMap<String, Object>();
        sqlParam1.put("taskId", param.get("taskId"));
        sqlParam1.put("sku", ((Map) param.get("stockInfo")).get("sku"));
        int delCount = cmsBtStockSeparateItemDao.deleteStockSeparateItem(sqlParam1);
        if (delCount <= 0) {
            simpleTransaction.rollback();
            throw new BusinessException("选择的明细不存在！");
        }

        simpleTransaction.commit();
    }

    /**
     * 库存隔离数据输入Check
     *
     * @param stockList 库存隔离数据
     */
    private void checksSparationQty(List<Map<String,Object>> stockList) throws BusinessException{
        for (Map<String, Object> stockInfo : stockList) {
            List<Map<String, Object>> platformStockList = (List<Map<String, Object>>) stockInfo.get("platformStock");
            for (Map<String, Object> platformInfo : platformStockList) {
                String separationQty = (String) platformInfo.get("separationQty");
                String status = (String) platformInfo.get("status");
                boolean checkResult = true;
                // 动态以外的场合
                if (!StringUtils.isEmpty(status)) {
                    try {
                        int value = Integer.parseInt(separationQty);
                        if (value < 0)  checkResult = false;
                    } catch (NumberFormatException ex) {
                        checkResult = false;
                    }
                    if (!checkResult) {
                        throw new BusinessException("隔离库存只能是0以上的整数");
                    }
                }
            }
        }
    }


    /**
     * 取得库存隔离明细一页表示的Sku的Sql
     *
     * @param param 客户端参数
     * @param flg 1:库存隔离明细; 2:实时库存状态
     * @return 库存隔离明细一页表示的Sku的Sql
     */
    private String getStockPageSkuSql(Map param, String flg){
        List<Map<String,Object>> platformList = (List<Map<String,Object>>) param.get("platformList");
        String sql = "select t1.product_model, t1.product_code, t1.sku, t1.cart_id, t1.property1, t1.property2, t1.property3, t1.property4, ";
        int index = 1;
        for (Map<String,Object> platformInfo : platformList) {
            sql += "t" + String.valueOf(index) + ".separate_qty as separate_qty" + String.valueOf(index) + ", ";
            sql += "z" + String.valueOf(index) + ".name as status_name" + String.valueOf(index) + ", ";
            index++;
        }
        if ("2".equals(flg)) {
            sql += " (select qty_china from wms_bt_inventory_center_logic t50 where order_channel_id = '" + param.get("channelId") + "' and t50.sku = t1.sku) qty_china,";
        }
        sql += " t1.qty";
        sql += " from (select * from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
        sql += getWhereSql(param, true) ;
        sql += " and cart_id = " + ((List<Map<String, Object>>)param.get("platformList")).get(0).get("cartId");
        sql += " order by sku";
        if ("1".equals(flg)) {
            String start = String.valueOf(param.get("start1"));
            String length = String.valueOf(param.get("length1"));
            sql += " limit " + start + "," + length + ") t1 ";
        } else {
            String start = String.valueOf(param.get("start2"));
            String length = String.valueOf(param.get("length2"));
            sql += " limit " + start + "," + length + ") t1 ";
        }
        index = 1;
        for (Map<String,Object> platformInfo : platformList) {
            if (index == 1) {
                sql +=" left join (select value,name from com_mt_value where type_id= '63' and lang_id = '" + param.get("lang") + "') z" + String.valueOf(index)
                        + " on t1.status = z" + String.valueOf(index) + ".value";
                index++;
                continue;
            }
            sql += " left join voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix") + " t" + String.valueOf(index) + " on "
                    + " t" + String.valueOf(index) + ".task_id = " +  String.valueOf(param.get("taskId")) + ""
                    + " and t" + String.valueOf(index) + ".cart_id = " +  platformInfo.get("cartId")
                    + " and t1.sku = t" +  String.valueOf(index) + ".sku";
            sql +=" left join (select value,name from com_mt_value where type_id= '63' and lang_id = '" + param.get("lang") + "') z" + String.valueOf(index)
                    + " on t" + String.valueOf(index) + ".status = z" + String.valueOf(index) + ".value";
            index++;
        }
        return sql;
    }


    /**
     * 取得实时库存状态一页表示的Sku的Sql
     *
     * @param param 客户端参数
     * @return 一页表示的Sku的Sql
     */
    private String getRealStockPageSkuSql(Map param){
        String sql = "select sku from ( select distinct sku as sku from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
        sql += getWhereSql(param, true) + " order by product_code,sku) t1 ";
        String start = String.valueOf(param.get("start2"));
        String length = String.valueOf(param.get("length2"));
        sql += " limit " + start + "," + length;
        return sql;
    }

    /**
     * 取得各种状态数量的Sql
     *
     * @param param 客户端参数
     * @return 各种状态数量的Sql
     */
    private String getStockStatusCountSql(Map param){
        String sql = "select status,count(*) as count from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
        sql += getWhereSql(param, false);
        sql += " group by status";
        return sql;
    }

    /**
     * 取得Sku的数量
     *
     * @param param 客户端参数
     * @return 一页表示的Sku的Sql
     */
    private String getStockSkuCountSql(Map param){
        String sql = "select count(sku) as count from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
        sql += getWhereSql(param, true);
        sql += " and cart_id = " + ((List<Map<String, Object>>)param.get("platformList")).get(0).get("cartId");
        return sql;
    }

    /**
     * 取得where条件的Sql文
     *
     * @param param
     * @param statusFlg 使用状态条件Flg
     *
     * @return where条件的Sql文
     */
    private String getWhereSql(Map param, boolean statusFlg){
        String whereSql = " where 1=1 ";
        // 任务Id
        whereSql += " and task_id = " + String.valueOf(param.get("taskId")) + " ";

        // 商品code
        if (!StringUtils.isEmpty((String) param.get("code"))) {
            whereSql += " and product_code = '" + escapeSpecialChar((String) param.get("code")) + "'";
        }
        // sku
        if (!StringUtils.isEmpty((String) param.get("sku"))) {
            whereSql += " and sku = '" + escapeSpecialChar((String) param.get("sku")) + "'";
        }
        // 可用库存（下限）
        if (!StringUtils.isEmpty((String) param.get("qtyFrom"))) {
            whereSql += " and qty >= " + escapeSpecialChar((String) param.get("qtyFrom"));
        }
        // 可用库存（上限）
        if (!StringUtils.isEmpty((String) param.get("qtyTo"))) {
            whereSql += " and qty <= " + escapeSpecialChar((String) param.get("qtyTo"));
        }
        //状态
        if (statusFlg && !StringUtils.isEmpty((String) param.get("status"))) {
            whereSql += " and status = '" + (String) param.get("status") + "'";
        }

        for (Map<String,Object> property : (List<Map<String,Object>>)param.get("propertyList")) {
            // 动态属性名
            String propertyName = (String) property.get("property");
            // 动态属性值
            String propertyValue = (String) property.get("value");
            // 逻辑，支持：Like，默认为空白
            String logic = (String) property.get("logic");
            // 类型，支持：int，默认为空白
            String type = (String) property.get("type");
            if (StringUtils.isEmpty(propertyValue)) {
                continue;
            }
            // 存在~则加上大于等于和小于等于的条件(如果是数值型则转换为数值做比较)
            if (propertyValue.contains("~")) {
                String values[] = propertyValue.split("~");
                if ("int".equals(type)) {
                    // propertyName = "convert(" + propertyName + ",signed)";
                    propertyName = propertyName + "*1";
                }
                int end = 2;
                if (values.length < 2) {
                    end = values.length;
                }
                for (int i = 0;i <  end; i++) {
                    if (i == 0) {
                        if (!StringUtils.isEmpty(values[i])) {
                            whereSql += " and " + propertyName + " >= '" + escapeSpecialChar(values[i]) + "' ";
                        }
                    } else {
                        if (!StringUtils.isEmpty(values[i])) {
                            whereSql += " and " + propertyName + " <= '" + escapeSpecialChar(values[i]) + "' ";
                        }
                    }
                }
                continue;
            }

            // 按逗号分割则生成多个用 or 分割的条件
            String values[] = propertyValue.split(",");
            String propertySql = "";
            for (String value : values) {
                propertySql += propertyName;
                if (logic.toLowerCase().equals("like")) {
                    propertySql += " like '%" + escapeSpecialChar(value) + "%' or ";
                } else {
                    propertySql += " = '" + escapeSpecialChar(value) + "' or ";
                }
            }
            if(values != null && values.length > 0 ) {
                whereSql += " and ( " + propertySql.substring(0,propertySql.length()-3) + " ) ";
            }
        }

        return whereSql;
    }

    /**
     * Sql转义字符转换
     *
     * @param value 转义前字符串
     * @return 转义后字符串
     */
    private String escapeSpecialChar(String value){
        return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("'","\\\\'").replaceAll("\"","\\\\\"")
                .replaceAll("%","\\\\%").replaceAll("_","\\\\_");
    }

//    /**
//     * 状态文字转换
//     *
//     * @param status 转义前状态
//     * @return 转换后的状态文字
//     */
//    private String getStatus(String status) {
//        String changedStatus = "";
//        if ("0".equals(status)) {
//            changedStatus = "未进行";
//        } else if ("1".equals(status)) {
//            changedStatus = "等待隔离";
//        } else if ("2".equals(status)) {
//            changedStatus = "隔离成功";
//        } else if ("3".equals(status)) {
//            changedStatus = "隔离失败";
//        } else if ("4".equals(status)) {
//            changedStatus = "等待还原";
//        } else if ("5".equals(status)) {
//            changedStatus = "还原成功";
//        } else if ("6".equals(status)) {
//            changedStatus = "还原失败";
//        } else if ("7".equals(status)) {
//            changedStatus = "再修正";
//        }
//
//        return changedStatus;
//    }

}