package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.promotion.task.StockExcelBean;
import com.voyageone.web2.cms.dao.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

;

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
    private CmsBtStockSeparateIncrementTaskDao cmsBtStockSeparateIncrementTaskDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

    @Autowired
    private WmsBtLogicInventoryDao wmsBtLogicInventoryDao;

    @Autowired
    private CmsBtTasksDao cmsBtTasksDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private TransactionRunner transactionRunner;

    /** 增量/库存隔离状态 0：未进行 */
    private static final String STATUS_READY = "0";
    /** 库存隔离状态 1：等待隔离 */
    private static final String STATUS_WAITING_SEPARATE = "1";
    /** 库存隔离状态 2：隔离成功 */
    private static final String STATUS_SEPARATE_SUCCESS = "2";
    /** 库存隔离状态 3：隔离失败 */
    private static final String STATUS_SEPARATE_FAIL = "3";
    /** 库存隔离状态 4：等待还原 */
    private static final String STATUS_WAITING_REVERT = "4";
    /** 库存隔离状态 5：还原成功 */
    private static final String STATUS_REVERT_SUCCESS = "5";
    /** 库存隔离状态 6：还原失败 */
    private static final String STATUS_REVERT_FAIL = "6";
    /** 库存隔离状态 7：再修正 */
    private static final String STATUS_CHANGED = "7";

    /** 增量库存隔离状态 1：等待增量 */
    private static final String STATUS_WAITING_INCREMENT = "1";
    /** 增量库存隔离状态 2：增量成功 */
    private static final String STATUS_INCREMENT_SUCCESS = "2";
    /** 增量库存隔离状态 3：增量失败 */
    private static final String STATUS_INCREMENT_FAIL = "3";
    /** 增量库存隔离状态 4：还原 */
    private static final String STATUS_REVERT = "4";

    /** Excel增量方式导入 */
    private static final String EXCEL_IMPORT_ADD = "1";
    /** Excel变更方式导入 */
    private static final String EXCEL_IMPORT_UPDATE = "2";
    /** Excel的Title部可用库存显示文字 */
    private static final String USABLESTOCK = "Usable Stock";
    /** Excel的Title部其他平台显示文字 */
    private static final String OTHER = "Other";
    /** Excel动态时显示文字 */
    private static final String DYNAMIC = "Dynamic";

    /**
     * 根据活动ID取得CartId
     *
     * @param param 活动ID
     * @return CartId数量
     */
    public Map<Object, String> findByCartId(Map param){
        Map<Object,String> sqlParam = new HashMap<Object,String>();
        //循环取得活动ID
        for(Object entry : param.entrySet()){
        }
        return null;
    }


    /**
     * 任务id/渠道id权限check
     *
     * @param channelId 渠道id
     * @param platformList 隔离平台列表
     * @return 任务id/渠道id权限check结果（false:没有权限,true:有权限）
     */
    public boolean hasAuthority(String channelId, List<Map<String, Object>> platformList){
        for (Map<String, Object> platform : platformList) {
            // 任务对应的渠道中存在和当前渠道不一致的情况，视为没有权限
            if (!channelId.equals(platform.get("channelId"))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除库存隔离任务
     *
     * @param param 客户端参数
     */
    public void delTask(Map param){
        // 取得库存隔离数据中是否存在状态为"0:未进行"以外的数据
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("taskId", param.get("taskId"));
        // "0:未进行"以外的状态
        sqlParam.put("statusList", Arrays.asList( STATUS_WAITING_SEPARATE,
                                                    STATUS_SEPARATE_SUCCESS,
                                                    STATUS_SEPARATE_FAIL,
                                                    STATUS_WAITING_REVERT,
                                                    STATUS_REVERT_SUCCESS,
                                                    STATUS_REVERT_FAIL,
                                                    STATUS_CHANGED));
         List<Object> stockSeparateItem = cmsBtStockSeparateItemDao.selectStockSeparateItemByStatus(sqlParam);
        // 库存隔离数据中是否存在状态为"0:未进行"以外的数据,不允许删除任务
        if (stockSeparateItem != null && stockSeparateItem.size() > 0) {
            throw new BusinessException("已经开始库存隔离，不能删除任务！");
        }
        simpleTransaction.openTransaction();
        try {
            // 删除库存隔离表中的数据
            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
            sqlParam1.put("taskId", param.get("taskId"));
            cmsBtStockSeparateItemDao.deleteStockSeparateItem(sqlParam1);

            // 删除隔离任务/平台基本信息表中的数据
            Map<String, Object> sqlParam2 = new HashMap<String, Object>();
            sqlParam2.put("taskId", param.get("taskId"));
            cmsBtStockSeparatePlatformInfoDao.deleteStockSeparatePlatform(sqlParam2);

            // 删除任务表中的数据
            CmsBtTasksModel cmsBtTasksModel = new CmsBtTasksModel();
            cmsBtTasksModel.setTask_id((int)param.get("taskId"));
            cmsBtTasksDao.delete(cmsBtTasksModel);

        }catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 取得库存隔离数据各种状态的数量
     *
     * @param param 客户端参数
     * @return 某种状态的数量
     */
    public List<Map<String,Object>>  getStockStatusCount(Map param){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        // 各种状态统计数量的Sql
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
        // 一页表示的Sku数量的Sql
        sqlParam.put("sql", getStockSkuCountSql(param));
        List<Object> countInfo = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlObject(sqlParam);
        return  Integer.parseInt(String.valueOf(countInfo.get(0)));
    }


    /**
     * 取得属性列表
     * 属性列表数据结构
     *     {"property":"property1", "name":"品牌", "logic":"", "type":"", "show":false, "value"=""},
     *     {"property":"property2", "name":"英文短描述", "logic":"Like", "type":"", "show":false, "value"=""},
     *     {"property":"property3", "name":"性别", "logic":"", "type":"", "show":false, "value"=""}，
     *     {"property":"property4", "name":"Size", "logic":"", "type":"int", "show":false, "value"=""}...
     *
     * @param channelId 渠道id
     * @param lang 语言
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPropertyList(String channelId, String lang){
        List<Map<String,Object>> propertyList = new ArrayList<Map<String,Object>>();
        // 取得动态属性
        List<TypeChannelBean> dynamicPropertyList = TypeChannel.getTypeList("dynamicProperty", (String) channelId);
        for (TypeChannelBean dynamicProperty : dynamicPropertyList) {
            if (lang.equals(dynamicProperty.getLang_id())) {
                Map<String, Object> propertyItem = new HashMap<String, Object>();
                propertyItem.put("property", dynamicProperty.getValue());
                propertyItem.put("name", dynamicProperty.getName());
                propertyItem.put("logic", dynamicProperty.getAdd_name1());
                propertyItem.put("type", dynamicProperty.getAdd_name2());
                propertyItem.put("show", true);
                propertyItem.put("value", "");
                propertyList.add(propertyItem);
            }
        }
        return propertyList;
    }

    /**
     * 取得任务对应平台信息列表
     * 平台信息列表数据结构
     *    {"cartId":"23", "cartName":"天猫国际", "channelId":010},
*         {"cartId":"27", "cartName":"聚美优品", "channelId":010}...
     *
     * @param taskId 任务id
     * @param channelId 渠道id
     * @param lang 语言
     *
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPlatformList(String taskId, String channelId, String lang){
        // 取得任务对应平台信息列表
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        // 任务id
        sqlParam.put("taskId", taskId);
        // 渠道id
        sqlParam.put("channelId", channelId);
        // 语言
        sqlParam.put("lang", lang);
        List<Map<String,Object>> stockSeparatePlatformListDB = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        List<Map<String,Object>> stockSeparatePlatformList = new ArrayList<Map<String,Object>>();
        // 生成只有cartId和cartName和channelId的平台信息列表
        for (Map<String,Object> stockSeparatePlatformDB : stockSeparatePlatformListDB) {
            Map<String,Object> stockSeparatePlatform = new HashMap<String,Object>();
            stockSeparatePlatform.put("cartId", String.valueOf(stockSeparatePlatformDB.get("cart_id")));
            stockSeparatePlatform.put("cartName", (String) stockSeparatePlatformDB.get("name"));
            stockSeparatePlatform.put("channelId", (String) stockSeparatePlatformDB.get("channel_id"));
            stockSeparatePlatformList.add(stockSeparatePlatform);
        }
        return stockSeparatePlatformList;
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
        // 库存隔离明细一页表示的Sku的Sql
        sqlParam.put("sql", getStockPageSkuSql(param, "1"));
        List<Map<String,Object>> stockAllList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);

        for (Map<String,Object> stockInfo : stockAllList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String sku = (String) stockInfo.get("sku");
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");
            String qty = String.valueOf(stockInfo.get("qty"));
            // 画面上一行的数据
            Map<String, Object> lineInfo = new HashMap<String, Object>();
            // 画面上一行里平台相关的数据
            List<Map<String, Object>> linePlatformInfoList = new ArrayList<Map<String, Object>>();
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
                // 某个平台的库存隔离值
                String separateQty = String.valueOf(stockInfo.get("separate_qty" + String.valueOf(index)));
                // 某个平台的库存数据的状态名
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
     * 库存隔离数据是否移到history表
     *
     * @param taskId 任务id
     * @return 库存隔离数据是否移到history表
     */
    public boolean isHistoryExist(String taskId){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", taskId);
        return (cmsBtStockSeparateItemDao.selectStockSeparateItemHistoryCnt(sqlParam) !=  0) ? true : false;
    }

    /**
     * 取得实时库存表示状态(0:活动期间表示,1:活动结束后表示）
     *
     * @param param 客户端参数
     * @return 实时库存表示状态
     */
    public String getRealStockStatus(Map param){
        // 实时库存表示状态
        String status = "1";
        // 历史表存在数据时，判定为活动结束
        if ("_history".equals(param.get("tableNameSuffix"))) {
            return status;
        }

        // 取得任务下的平台平台信息
        Date now = DateTimeUtil.parse(DateTimeUtil.getNow());
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", (String) param.get("taskId"));
        List<Map<String, Object>> platformList = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        for (Map<String, Object> platformInfo : platformList) {
            // 库存隔离还原时间
            String restoreSeparateTime = (String) platformInfo.get("restore_separate_time");
            // 系统时间小于这个任务中任意一个隔离平台的还原时间，实时库存表示状态=0:活动期间表示
            if (!StringUtils.isEmpty(restoreSeparateTime)) {
                if (restoreSeparateTime.length() == 10) {
                    restoreSeparateTime = restoreSeparateTime + " 00:00:00";
                }
                Date restoreSeparateTimeDate = DateTimeUtil.parse(restoreSeparateTime);
                if (now.getTime() - restoreSeparateTimeDate.getTime() <= 0) {
                    status = "0";
                    break;
                }
            }
        }
        return status;
    }



    /**
     * 取得实时库存状态列表
     *      {"model":"35265", "code":"35265465", "Sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50",
     *                                                         "platformStock":[{"cartId":"23", "separationQty":"30", "salesQty":"10"},
     *                                                                          {"cartId":"27", "separationQty":"20", "salesQty":"0"}]},
     *      {"model":"35265", "code":"35265465", "Sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"80",
     *                                                         "platformStock":[{"cartId":"23", "separationQty":"30", "salesQty":"10"},
     *                                                                          {"cartId":"27", "separationQty":"20", "salesQty":"0"}]},
     *      {"model":"35265", "code":"35265465", "Sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"80",
     *                                                         "platformStock":[{"cartId":"23", "separationQty":"-1", },
     *                                                                          {"cartId":"27", "separationQty":"20", "salesQty":"0"}
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
        // 库存隔离明细一页表示的Sku的Sql
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
        // 状态 = 2：隔离成功,4：等待还原, 5：还原成功 ,6：还原失败
        sqlParam1.put("statusList", Arrays.asList( STATUS_SEPARATE_SUCCESS,
                                                    STATUS_WAITING_REVERT,
                                                    STATUS_REVERT_SUCCESS,
                                                    STATUS_REVERT_FAIL));
        sqlParam1.put("tableNameSuffix", param.get("tableNameSuffix"));
        List<Map<String,Object>> stockSeparateList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam1);

        // sku库存隔离信息（所有任务所有平台的数据）
        Map<String,Integer> skuStockAllTask = new HashMap<String,Integer>();
        // sku库存隔离信息（该任务下的各个平台的数据）
        Map<String,Integer> skuStockByPlatform = new HashMap<String,Integer>();
        // 当前任务id
        String currentTaskId = (String) param.get("taskId");
        if (stockSeparateList != null && stockSeparateList.size() > 0) {
            for (Map<String, Object> stockInfo : stockSeparateList) {
                String sku = (String) stockInfo.get("sku");
                String cartId = String.valueOf(stockInfo.get("cart_id"));
                Integer separateQty = (Integer) stockInfo.get("separate_qty");
                String taskId = String.valueOf(stockInfo.get("task_id"));
                String status = (String) stockInfo.get("status");
                // 如果状态为2：隔离成功 那么加入到sku库存隔离信息（所有任务所有平台的数据）
                if (STATUS_SEPARATE_SUCCESS.equals(status)) {
                    if (skuStockAllTask.containsKey(sku)) {
                        skuStockAllTask.put(sku, skuStockAllTask.get(sku) + separateQty);
                    } else {
                        skuStockAllTask.put(sku, separateQty);
                    }
                }

                // 加入到sku库存隔离信息（该任务下的各个平台的数据），状态为4：等待还原, 5：还原成功 ,6：还原失败也认为是隔离成功
                if (currentTaskId.equals(taskId)) {
                    skuStockByPlatform.put(sku + cartId, separateQty);
                }
            }
        }

        // 取得一页中的sku所有平台的增量隔离库存（含非本任务的）
        Map<String,Object> sqlParam2 = new HashMap<String,Object>();
        sqlParam2.put("skuList", skuList);
        // 状态 = 2：增量成功,4：还原,
        sqlParam2.put("statusList", Arrays.asList(STATUS_INCREMENT_SUCCESS, STATUS_REVERT));
        sqlParam2.put("tableNameSuffix", param.get("tableNameSuffix"));
        List<Map<String,Object>> stockIncrementList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrement(sqlParam2);

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
                String status = (String) stockIncrementInfo.get("status");
                // 如果状态为2：隔离成功 那么加入到sku库存增量隔离信息（所有任务所有平台的数据）
                if (STATUS_INCREMENT_SUCCESS.equals(status)) {
                    if (skuStockIncrementAllTask.containsKey(sku)) {
                        skuStockIncrementAllTask.put(sku, skuStockIncrementAllTask.get(sku) + incrementQty);
                    } else {
                        skuStockIncrementAllTask.put(sku, incrementQty);
                    }
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

        // 生成实时库存状态画面数据
        for (Map<String,Object> stockInfo : stockRealList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String sku = (String) stockInfo.get("sku");
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");
            // 画面上一行的数据
            Map<String, Object> lineInfo = new HashMap<String, Object>();
            // 画面上一行里平台相关的数据
            List<Map<String, Object>> linePlatformInfoList = new ArrayList<Map<String, Object>>();
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
            // 可用库存数 = 逻辑库存 - （隔离库存数 + 增量隔离库存数 - 隔离平台的销售数量）
            if (logicStock - (stockSeparateAll + stockSeparateIncrementAll - stockSalesQuantityAll) > 0 ) {
                qty = logicStock - (stockSeparateAll + stockSeparateIncrementAll - stockSalesQuantityAll);
            }
            lineInfo.put("qty", String.valueOf(qty));
            lineInfo.put("platformStock", linePlatformInfoList);

            int index = 1;
            for (Map<String,Object> platformInfo : platformList) {
                String cartId = (String) platformInfo.get("cartId");
                // 库存隔离值
                String separateQty = String.valueOf(stockInfo.get("separate_qty" + String.valueOf(index)));
                // 某个sku某个平台的隔离库存
                int stockSeparate = 0;
                if (skuStockByPlatform.get(sku + cartId) != null) {
                    stockSeparate =  skuStockByPlatform.get(sku + cartId);
                }
                // 某个sku某个平台的增量隔离库存
                int stockIncrementSeparate = 0;
                if (skuStockIncrementByPlatform.get(sku + cartId) != null) {
                    stockIncrementSeparate =  skuStockIncrementByPlatform.get(sku + cartId);
                }

                // 某个sku某个平台的销售数量
                int stockSalesQuantity = 0;
                if (skuStockSalesByPlatform.get(sku + cartId) != null) {
                    stockSalesQuantity =  skuStockSalesByPlatform.get(sku + cartId);
                }
                Map<String, Object> linePlatformInfo = new HashMap<String, Object>();
                linePlatformInfo.put("cartId", cartId);
                if ("-1".equals(separateQty)) {
                    linePlatformInfo.put("separationQty", "-1");
                } else {
                    linePlatformInfo.put("separationQty", String.valueOf(stockSeparate + stockIncrementSeparate));
                    linePlatformInfo.put("salesQty", String.valueOf(stockSalesQuantity));
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
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能修改数据！");
        }

        List<Map<String,Object>> stockList = new ArrayList<Map<String,Object>>();
        // 多件明细保存的场合
        if(param.get("index") == null) {
            stockList = (List<Map<String, Object>>) param.get("stockList");
        } else {
            // 一件明细保存的场合
            stockList.add(((List<Map<String, Object>>) param.get("stockList")).get((Integer)param.get("index")));
        }

        // 库存隔离数据输入Check
        checksSparationQty(stockList);

        String taskId = (String) param.get("taskId");
        simpleTransaction.openTransaction();
        try {
            // 更新的sku对象列表
            List<String> skuList = new ArrayList<String>();
            for (Map<String, Object> stockInfo : stockList) {
                skuList.add((String) stockInfo.get("sku"));
            }
            // 取得这个页面所有sku的库存隔离信息
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            // 任务id
            sqlParam.put("taskId", taskId);
            // 更新的sku对象列表
            sqlParam.put("skuList", skuList);
            // 非history表
            sqlParam.put("tableNameSuffix", "");
            List<Map<String, Object>> stockSeparateItemList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
            if (stockSeparateItemList == null || stockSeparateItemList.size() == 0) {
                throw new BusinessException("更新对象不存在！");
            }
            // 这个页面所有sku的库存隔离信息Map
            Map<String, Object> skuDBInfo = new HashMap<String, Object>();
            // 生成这个页面所有sku的库存隔离信息Map
            for (Map<String, Object> stockSeparateItem : stockSeparateItemList) {
                String skuDB = (String) stockSeparateItem.get("sku");
                String cartIdDB = String.valueOf(stockSeparateItem.get("cart_id"));
                skuDBInfo.put(skuDB + cartIdDB, stockSeparateItem);
            }
            for (Map<String, Object> stockInfo : stockList) {
                String sku = (String) stockInfo.get("sku");
                // 平台列表信息
                List<Map<String, Object>> platformStockList = (List<Map<String, Object>>) stockInfo.get("platformStock");
                for (Map<String, Object> platformInfo : platformStockList) {
                    // 库存隔离值
                    String separationQty = (String) platformInfo.get("separationQty");
                    // 状态
                    String status = (String) platformInfo.get("status");
                    // 平台id
                    String cartId = (String) platformInfo.get("cartId");
                    // sku + cartId在DB中存在的场合（隔离信息存在）
                    if (skuDBInfo.containsKey(sku + cartId)) {
                        Map<String, Object> recordDB = (Map<String, Object>) skuDBInfo.get(sku + cartId);
                        // 库存隔离值（DB值）
                        String separateQtyDB = String.valueOf(recordDB.get("separate_qty"));
                        // 状态（DB值）
                        String statusDB = (String) recordDB.get("status");

                        // 画面的隔离库存 != DB的隔离库存是进行更新 并且 不是动态的场合（状态不是空白）
                        if (!separationQty.equals(separateQtyDB) && !StringUtils.isEmpty(status)) {
                            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
                            sqlParam1.put("taskId", taskId);
                            sqlParam1.put("sku", sku);
                            sqlParam1.put("cartId", cartId);
                            sqlParam1.put("separateQty", separationQty);
                            sqlParam1.put("modifier", param.get("userName"));
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
                                throw new BusinessException("更新对象不存在！");
                            }
                        }
                    }
                }
            }
        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 删除隔离库存明细
     *
     * @param taskId 任务id
     * @param sku Sku
     */
    public void delRecord(String taskId, String sku){
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart(taskId);
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能删除数据！");
        }
        simpleTransaction.openTransaction();
        try {
            // 取得这条sku明细对应的库存隔离信息
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            sqlParam.put("taskId", taskId);
            sqlParam.put("sku", sku);
            sqlParam.put("tableNameSuffix", "");
            List<Map<String, Object>> stockSeparateItemList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
            if (stockSeparateItemList == null || stockSeparateItemList.size() == 0) {
                throw new BusinessException("选择的明细不存在！");
            }
            for (Map<String, Object> stockSeparateItem : stockSeparateItemList) {
                String status = (String) stockSeparateItem.get("status");
                // 只有状态为 0：未进行的数据可以删除
                if (!StringUtils.isEmpty(status) && !STATUS_READY.equals(status)) {
                    throw new BusinessException("选择的明细不能删除！");
                }
            }

            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
            sqlParam.put("taskId", taskId);
            sqlParam.put("sku", sku);
            int delCount = cmsBtStockSeparateItemDao.deleteStockSeparateItem(sqlParam);
            if (delCount <= 0) {
                throw new BusinessException("选择的明细不存在！");
            }
        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

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
        // 状态 = 2：隔离成功
        sqlParam1.put("status", STATUS_SEPARATE_SUCCESS);
        Integer stockSeparateSuccessQty =  cmsBtStockSeparateItemDao.selectStockSeparateSuccessQty(sqlParam1);
        if (stockSeparateSuccessQty != null) {
            stockSeparate =  stockSeparateSuccessQty;
        }


        // 取得增量隔离库存
        int stockIncrementSeparate = 0;
        Map<String,Object> sqlParam2 = new HashMap<String,Object>();
        sqlParam2.put("sku", sku);
        // 状态 = 2：增量成功
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

    /**
     * 新增库存隔离明细
     *
     * @param param 客户端参数
     */
    public void saveNewRecord(Map param){
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能新增库存隔离明细！");
        }

        // 输入验证
        checkInputNewRecord(param);

        // 插入库存隔离明细
        simpleTransaction.openTransaction();
        try {
            insertNewRecord(param);
        }catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 新增库存隔离明细 输入验证
     *
     * @param param 客户端参数
     */
    private void checkInputNewRecord(Map param) throws BusinessException{
        // Model
        String model = (String) param.get("model");
        // Code
        String code = (String) param.get("code");
        // Sku
        String sku = (String) param.get("sku");
        // 可用库存
        String usableStock = (String) param.get("usableStock");
        // 属性列表
        List<Map<String,Object>> propertyStockList = (List<Map<String,Object>>) param.get("propertyStockList");
        // 平台隔离库存信息
        List<Map<String,Object>> platformStockList = (List<Map<String,Object>>) param.get("platformStockList");

        // Model输入check
        if (StringUtils.isEmpty(model) || model.getBytes().length > 50) {
            throw new BusinessException("Model必须输入且长度小于50！");
        }

        // Code输入check
        if (StringUtils.isEmpty(code) || code.getBytes().length > 50) {
            throw new BusinessException("Code必须输入且长度小于50！");
        }

        // Sku输入check
        if (StringUtils.isEmpty(sku) || sku.getBytes().length > 50) {
            throw new BusinessException("Sku必须输入且长度小于50！");
        }

        // 可用库存输入check
        if (StringUtils.isEmpty(usableStock) || !StringUtils.isDigit(usableStock) || usableStock.getBytes().length > 9) {
            throw new BusinessException("可用库存必须输入小于10位的整数！");
        }

        // 属性列表
        for (Map<String,Object> property : propertyStockList) {
            // 属性名
            String name = (String) property.get("name");
            // 属性值
            String value = (String) property.get("value");
            // 属性输入check
            if (StringUtils.isEmpty(value) || value.getBytes().length > 500) {
                throw new BusinessException(name + "必须输入且长度小于500！");
            }
        }

        // 隔离库存的平台数
        int stockCnt = 0;
        for (Map<String,Object> platformInfo : platformStockList) {
            // 平台隔离库存
            String qty = (String) platformInfo.get("qty");
            // 是否动态
            boolean dynamic = (boolean) platformInfo.get("dynamic");
            // 平台隔离库存与是否动态 必须且只能选择一项
            if (StringUtils.isEmpty(qty) && !dynamic) {
                throw new BusinessException("请输入隔离库存的值或勾选动态！");
            }
            if (!StringUtils.isEmpty(qty) && dynamic) {
                throw new BusinessException("隔离库存的值或动态,只能选择其一！");
            }
            if (!StringUtils.isDigit(qty) || qty.getBytes().length > 9) {
                throw new BusinessException("隔离库存的值必须输入小于10位的整数！");
            }
            if (!StringUtils.isEmpty(qty)) {
                stockCnt++;
            }
        }

        // 隔离库存的平台数 = 0
        if (stockCnt == 0) {
            throw new BusinessException("至少隔离一个平台的库存值！");
        }

        // 验证taskId + sku是否在库存隔离表中存在
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", param.get("taskId"));
        sqlParam.put("tableNameSuffix", "");
        sqlParam.put("sku", sku);
        List<Map<String, Object>> stockSeparateItem = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
        if (stockSeparateItem.size() > 0) {
            throw new BusinessException("Sku已经存在！");
        }
    }

    /**
     * 插入库存隔离明细
     *
     * @param param 客户端参数
     */
    private void insertNewRecord(Map param) {
        // Model
        String model = (String) param.get("model");
        // Code
        String code = (String) param.get("code");
        // Sku
        String sku = (String) param.get("sku");
        // 可用库存
        String usableStock = (String) param.get("usableStock");
        // 属性列表
        List<Map<String,Object>> propertyStockList = (List<Map<String,Object>>) param.get("propertyStockList");
        // 平台隔的离库存
        List<Map<String,Object>> platformStockList = (List<Map<String,Object>>) param.get("platformStockList");

        // 插入库存隔离数据库
        for (Map<String, Object> platformInfo : platformStockList) {
            // 平台id
            String cartId = (String) platformInfo.get("cartId");
            // 平台隔离库存
            String qty = (String) platformInfo.get("qty");
            // 是否动态
            boolean dynamic = (boolean) platformInfo.get("dynamic");

            // 插入库存隔离数据库
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            // 任务id
            sqlParam.put("taskId", param.get("taskId"));
            // Model
            sqlParam.put("productModel", model);
            // Code
            sqlParam.put("productCode", code);
            // Sku
            sqlParam.put("sku", sku);
            // 平台id
            sqlParam.put("cartId", cartId);
            // 属性列表
            for (Map<String, Object> property : propertyStockList) {
                // DB字段名
                String propertyDB = (String) property.get("property");
                // 属性值
                String value = (String) property.get("value");
                // 属性1
                sqlParam.put(propertyDB, value);
            }
            // 可用库存
            sqlParam.put("qty", usableStock);
            // 该平台动态库存的场合
            if (dynamic) {
                sqlParam.put("separateQty", -1);
            } else {
                sqlParam.put("separateQty", qty);
                sqlParam.put("status", STATUS_READY);
            }
            sqlParam.put("creater", param.get("userName"));
            cmsBtStockSeparateItemDao.insertStockSeparateItem(sqlParam);
        }
    }

    /**
     * 取得某个Sku的所有隔离详细
     * 所有隔离详细列表数据结构
     *              {"type":"一般库存隔离", "taskName":"天猫双11任务", "qty":"10" },
     *              {"type":"增量库存隔离", "taskName":"天猫双11-增量1", "qty":"1" },
     *              {"type":"增量库存隔离", "taskName":"天猫双11-增量2", "qty":"2" }...
     *
     * @param param 客户端参数
     * @return 某个Sku的所有隔离详细
     */
    public List<Map<String,Object>> getSkuSeparationDetail(Map param) {

        // 某个Sku的所有隔离详细
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        // 任务id
        sqlParam.put("taskId", param.get("taskId"));
        // 平台id
        sqlParam.put("cartId", param.get("cartId"));
        // sku
        sqlParam.put("sku", param.get("sku"));
        // 一般库存隔离状态 状态 = 2：隔离成功,4：等待还原, 5：还原成功 ,6：还原失败
        sqlParam.put("statusStockList", Arrays.asList(STATUS_SEPARATE_SUCCESS,
                                                        STATUS_WAITING_REVERT,
                                                        STATUS_REVERT_SUCCESS,
                                                        STATUS_REVERT_FAIL));
        // 增量库存隔离状态 状态 = 2：增量成功,4：还原
        sqlParam.put("statusStockIncrementList", Arrays.asList(STATUS_INCREMENT_SUCCESS, STATUS_REVERT));
        // 是否从历史表中取得数据
        sqlParam.put("tableNameSuffix", param.get("tableNameSuffix"));
        List<Map<String, Object>> stockHistoryList = cmsBtStockSeparateItemDao.selectStockSeparateDetailAll(sqlParam);

        return stockHistoryList;

    }


    /**
     * 启动/重刷库存隔离
     *
     * @param param 客户端参数
     */
    public void executeStockSeparation(Map param){
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能进行库存隔离！");
        }
        simpleTransaction.openTransaction();
        try {
            // 画面选择的sku
            String selSku = (String) param.get("selSku");
            // 更新结果
            int updateCnt = 0;

            Map<String, Object> sqlParam = new HashMap<String, Object>();
            // 选择一件sku进行库存隔离的的场合,加入sku的条件
            if (!StringUtils.isEmpty(selSku)) {
                sqlParam.put("sku", selSku);
            }
            // 更新状态为"1:等待隔离"
            sqlParam.put("status", STATUS_WAITING_SEPARATE);
            sqlParam.put("modifier", param.get("userName"));
            // 更新条件
            sqlParam.put("taskId", param.get("taskId"));
            // 只有状态为"0:未进行"，"3:隔离失败"，"7:再修正"的数据可以进行隔离库存操作
            sqlParam.put("statusList", Arrays.asList(STATUS_READY, STATUS_SEPARATE_FAIL, STATUS_CHANGED));
            updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam);

            if (updateCnt == 0) {
                throw new BusinessException("没有可以隔离的数据！");
            }
        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 还原库存隔离
     *
     * @param param 客户端参数
     */
    public void executeStockRevert(Map param){
        simpleTransaction.openTransaction();
        try {
            // 画面选择的sku
            String selSku = (String) param.get("selSku");
            // 更新结果
            int updateCnt = 0;

            // 对库存隔离数据表进行数据还原
            Map<String, Object> sqlParam2 = new HashMap<String, Object>();
            // 选择一件sku进行库存还原的的场合,加入sku的条件
            if (!StringUtils.isEmpty(selSku)) {
                sqlParam2.put("sku", selSku);
            }
            // 更新状态为"4:等待还原"
            sqlParam2.put("status", STATUS_WAITING_REVERT);
            sqlParam2.put("modifier", param.get("userName"));
            // 更新条件
            sqlParam2.put("taskId", param.get("taskId"));
            // 只有状态为"2:隔离成功"，"6:还原失败"的数据可以进行还原库存隔离操作。
            sqlParam2.put("statusList", Arrays.asList(STATUS_SEPARATE_SUCCESS, STATUS_REVERT_FAIL));
            updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam2);
            if (updateCnt == 0) {
                throw new BusinessException("没有可以还原的数据！");
            }

            // 对增量库存隔离数据表进行数据还原
            // 取得隔离对象对应的子任务id
            List<Integer> subTaskIdList = new ArrayList<Integer>();
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            sqlParam.put("taskId", param.get("taskId"));
            List<Map<String, Object>> incrementTaskList = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(sqlParam);
            for (Map<String, Object> incrementTask : incrementTaskList) {
                subTaskIdList.add((Integer)incrementTask.get("sub_task_id"));
            }

            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
            // 选择一件sku进行库存还原的的场合,加入sku的条件
            if (!StringUtils.isEmpty(selSku)) {
                sqlParam1.put("sku", selSku);
            }
            // 更新状态为"4:还原"
            sqlParam1.put("status", STATUS_REVERT);
            sqlParam1.put("modifier", param.get("userName"));
            //更新条件
            sqlParam1.put("subTaskIdList", subTaskIdList);
            // 只对状态为"2:增量成功"的数据改变状态
            sqlParam1.put("statusWhere", EXCEL_IMPORT_UPDATE);
            cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam1);

        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 库存隔离Excel文档做成，数据流返回
     *
     * @param param 客户端参数
     * @return byte[] 数据流
     * @throws IOException
     * @throws InvalidFormatException
     */
    public byte[] getExcelFileStockInfo(Map param) throws IOException, InvalidFormatException {

        String templatePath = Properties.readValue(CmsConstants.Props.STOCK_EXPORT_TEMPLATE);

        param.put("whereSql", getWhereSql(param, true));
        List<StockExcelBean> resultData = cmsBtStockSeparateItemDao.selectExcelStockInfo(param);

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
            SXSSFWorkbook book = new SXSSFWorkbook(new XSSFWorkbook(inputStream))) {
            // Titel行
            Map<String, Integer> mapCartCol = writeExcelStockInfoHead(book.getXSSFWorkbook(), param);
            // 数据行
            writeExcelStockInfoRecord(book.getXSSFWorkbook(), param, resultData, mapCartCol);

            // 自适应列宽
            List<Map> propertyList = (List<Map>) param.get("propertyList");
            List<Map> platformList = (List<Map>) param.get("platformList");
            int cntCol = 3 + propertyList.size() + platformList.size() + 2;
            for (int i = 0; i < cntCol; i++) {
                book.getXSSFWorkbook().getSheetAt(0).autoSizeColumn(i);
            }

            // 格式copy用sheet删除
            book.getXSSFWorkbook().removeSheetAt(1);

            $info("文档写入完成");

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * 库存隔离Excel的第一行Title部写入
     *
     * @return Map<cart_id, colIndex列号>
     */
    private Map<String, Integer> writeExcelStockInfoHead(Workbook book, Map param) {
        Map<String, Integer> mapCartCol = new HashMap<String, Integer>();

        Sheet sheet = book.getSheetAt(0);
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = book.getCreationHelper();

        Row row = FileUtils.row(sheet, 0);
        CellStyle cellStyleProperty = book.getSheetAt(1).getRow(0).getCell(4).getCellStyle(); // 属性的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");
        List<Map> platformList = (List<Map>) param.get("platformList");

        // 内容输出
        int index = 3;

        // 属性
        for (Map property : propertyList) {
            FileUtils.cell(row, index++, cellStyleProperty).setCellValue((String) property.get("name"));

            Comment comment = drawing.createCellComment(helper.createClientAnchor());
            comment.setString(helper.createRichTextString((String) property.get("property")));
            row.getCell(index - 1).setCellComment(comment);
        }

        // 可用库存
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(USABLESTOCK);

        // 平台
        CellStyle cellStylePlatform = book.getSheetAt(1).getRow(0).getCell(0).getCellStyle(); // 平台的cellStyle
        for (Map platform : platformList) {
            mapCartCol.put((String) platform.get("cartId"), Integer.valueOf(index));
            FileUtils.cell(row, index++, cellStylePlatform).setCellValue((String) platform.get("cartName"));

            Comment comment = drawing.createCellComment(helper.createClientAnchor());
            comment.setString(helper.createRichTextString((String) platform.get("cartId")));
            row.getCell(index - 1).setCellComment(comment);
        }
        mapCartCol.put("-1", index);
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue(OTHER);

        // 筛选
        CellRangeAddress filter = new CellRangeAddress(0, 0, 0, index - 1);
        sheet.setAutoFilter(filter);

        return mapCartCol;
    }

    /**
     * 库存隔离Excel的数据写入
     */
    private void writeExcelStockInfoRecord(Workbook book, Map param, List<StockExcelBean> resultData, Map<String, Integer> mapCartCol) {

        Sheet sheet = book.getSheetAt(0);
        String preSku = "";
        String cart_id = "";
        int lineIndex = 1; // 行号
        int colIndex = 0; // 列号
        Row row = null;

        CellStyle cellStyleDynamic = book.getSheetAt(1).getRow(0).getCell(1).getCellStyle(); // 动态的CellStyle
        CellStyle cellStyleDataLock = book.getSheetAt(1).getRow(0).getCell(2).getCellStyle(); // 数据（锁定）的cellStyle
        CellStyle cellStyleData = book.getSheetAt(1).getRow(0).getCell(3).getCellStyle(); // 数据（不锁定）的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");

        for (StockExcelBean rowData : resultData) {
            cart_id = rowData.getCart_id();
            if (!mapCartCol.containsKey(cart_id)) {
                continue;
            }

            if (!preSku.equals(rowData.getSku())) {
                // 新sku
                preSku = rowData.getSku();
                row = FileUtils.row(sheet, lineIndex++);
                colIndex = 0;

                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getProduct_model()); // Model
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getProduct_code()); // Code
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getSku()); // Sku

                // 属性
                for (Map property : propertyList) {
                    FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getProperty((String) property.get("property")));
                }

                // 可用库存
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getQty().toPlainString());

                // 平台
                if (StringUtils.isEmpty(rowData.getStatus())) {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleDynamic).setCellValue(DYNAMIC);
                } else {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleData).setCellValue(rowData.getSeparate_qty().toPlainString());
                }

                CellStyle cellStyle = book.createCellStyle();
                cellStyle.cloneStyleFrom(cellStyleDynamic);
                cellStyle.setLocked(true);
                FileUtils.cell(row, mapCartCol.get("-1"), cellStyle).setCellValue(DYNAMIC);
            } else {
                // 同一个sku，不同平台
                // 平台
                if (StringUtils.isEmpty(rowData.getStatus())) {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleDynamic).setCellValue(DYNAMIC);
                } else {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleData).setCellValue(rowData.getSeparate_qty().toPlainString());
                }
            }
        }
    }

    /**
     * excel 导入
     *
     * @param param 客户端参数
     */
    public void importExcelFileStockInfo(Map param, MultipartFile file) {
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能修改数据！");
        }

        String task_id = (String) param.get("task_id");
        String import_mode = (String) param.get("import_mode");
        List<Map> paramPropertyList = JacksonUtil.json2Bean((String) param.get("propertyList"), List.class);
        List<Map> paramPlatformInfoList = JacksonUtil.json2Bean((String) param.get("platformList"), List.class);

        // 库存隔离数据取得
        logger.info("库存隔离数据取得开始, task_id=" + task_id);
        Map searchParam = new HashMap();
        searchParam.put("tableName", "voyageone_cms2.cms_bt_stock_separate_item");
        searchParam.put("whereSql", " where task_id= '" + task_id + "'");
        List<StockExcelBean> resultData = cmsBtStockSeparateItemDao.selectExcelStockInfo(searchParam);
        // Map<sku, Map<cart_id, StockExcelBean>>
        Map<String, Map<String, StockExcelBean>> mapSkuInDB = new HashMap<String, Map<String, StockExcelBean>>();
        for (StockExcelBean rowData : resultData) {
            if (mapSkuInDB.containsKey(rowData.getSku())) {
                mapSkuInDB.get(rowData.getSku()).put(rowData.getCart_id(), rowData);
            } else {
                mapSkuInDB.put(rowData.getSku(), new HashMap<String, StockExcelBean>() {{
                    put(rowData.getCart_id(), rowData);
                }});
            }
        }
        logger.info("库存隔离数据取得结束");

        logger.info("导入Excel取得并check的处理开始");
        List<StockExcelBean> saveData = readExcel(file, import_mode, paramPropertyList, paramPlatformInfoList, mapSkuInDB);
        logger.info("导入Excel取得并check的处理结束");

        if (saveData.size() > 0) {
            logger.info("更新开始");
            saveImportData(saveData, import_mode, task_id, (String) param.get("userName"));
            logger.info(String.format("更新结束,更新了%d件", saveData.size()));
        } else {
            logger.info("没有更新对象");
            throw new BusinessException("没有更新对象");
        }
    }

    /**
     * 读取导入文件，并做check
     *
     * @param file                  导入文件
     * @param import_mode           导入mode
     * @param paramPropertyList     属性list
     * @param paramPlatformInfoList 平台list
     * @param mapSkuInDB            cms_bt_stock_separate_item的数据
     * @return
     */
    private List<StockExcelBean> readExcel(MultipartFile file, String import_mode, List<Map> paramPropertyList, List<Map> paramPlatformInfoList, Map<String, Map<String, StockExcelBean>> mapSkuInDB) {
        List<StockExcelBean> saveData = new ArrayList<StockExcelBean>();

        Workbook wb;
        int[] colPlatform = null;
        try {
            wb = WorkbookFactory.create(file.getInputStream());
        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("7000005");
        } catch (Exception e) {
            throw new BusinessException("7000005");
        }

        Sheet sheet = wb.getSheetAt(0);
        boolean isHeader = true;
        for (Row row : sheet) {
            if (isHeader) {
                // 第一行Title行
                isHeader = false;
                // Title行check
                colPlatform = checkHeader(row, paramPropertyList, paramPlatformInfoList);
            } else {
                // 数据行
                checkRecord(row, sheet.getRow(0), import_mode, colPlatform, mapSkuInDB, saveData);
            }
        }

        return saveData;
    }

    /**
     * Title行check
     *
     * @param row                   行
     * @param paramPropertyList     设定的属性list
     * @param paramPlatformInfoList 设定的平台list
     * @return colPlatform colPlatform[0]平台对应起始列号,colPlatform[1]平台对应结束列号
     */
    private int[] checkHeader(Row row, List<Map> paramPropertyList, List<Map> paramPlatformInfoList) {
        int[] colPlatform = new int[2];
        String messageModelErr = "请使用正确格式的excel导入文件";
        if (!"Model".equals(row.getCell(0).getStringCellValue())
                || !"Code".equals(row.getCell(1).getStringCellValue())
                || !"Sku".equals(row.getCell(2).getStringCellValue())) {
            throw new BusinessException(messageModelErr);
        }

        // 属性列check
        List<String> listPropertyKey = new ArrayList<String>();
        paramPropertyList.forEach(paramProperty -> listPropertyKey.add((String) paramProperty.get("property")));

        List<String> propertyList = new ArrayList<String>();
        int index = 3;
        for (; index <= 6; index++) {
            Comment comment = row.getCell(index).getCellComment();
            if (comment == null) {
                // 注解为空
                if (propertyList.size() > 0) {
                    // 已经有属性列，属性列结束
                    break;
                } else {
                    // 没有属性列，此列也不是属性列，错误Excel
                    throw new BusinessException(messageModelErr);
                }
            }
            if (!listPropertyKey.contains(comment.getString().getString())) {
                // 注解不是设定属性
                throw new BusinessException(messageModelErr);
            } else {
                // 注解是设定属性
                if (propertyList.contains(comment.getString().getString())) {
                    // 已经存在相同属性列
                    throw new BusinessException(messageModelErr);
                } else {
                    propertyList.add(comment.getString().getString());
                }
            }
        }

        if (propertyList.size() != listPropertyKey.size()) {
            // Excel属性列少于设定属性列
            throw new BusinessException(messageModelErr);
        }

        if (!USABLESTOCK.equals(row.getCell(index++).getStringCellValue())) {
            throw new BusinessException(messageModelErr);
        }

        // 平台对应起始列号
        colPlatform[0] = index;

        // 平台列check
        List<String> listPlatformKey = new ArrayList<String>();
        paramPlatformInfoList.forEach(paramPlatform -> listPlatformKey.add((String) paramPlatform.get("cartId")));

        List<String> platformList = new ArrayList<String>();
        while (true) {
            Comment comment = row.getCell(index).getCellComment();
            if (comment == null) {
                // 注解为空
                if (platformList.size() > 0) {
                    // 已经有平台列，平台列结束
                    break;
                } else {
                    // 没有平台列，此列也不是平台列，错误Excel
                    throw new BusinessException(messageModelErr);
                }
            }
            if (!listPlatformKey.contains(comment.getString().getString())) {
                // 注解不是设定平台
                throw new BusinessException(messageModelErr);
            } else {
                // 注解是设定平台
                if (platformList.contains(comment.getString().getString())) {
                    // 已经存在相同平台列
                    throw new BusinessException(messageModelErr);
                } else {
                    platformList.add(comment.getString().getString());
                }
            }

            index++;
        }

        if (platformList.size() != listPlatformKey.size()) {
            // Excel平台列少于设定平台列
            throw new BusinessException(messageModelErr);
        }

        // 平台对应结束列号
        colPlatform[1] = index;

        if (!OTHER.equals(row.getCell(index).getStringCellValue())) {
            throw new BusinessException(messageModelErr);
        }

        return colPlatform;
    }

    /**
     * check数据，并返回保存对象
     *
     * @param row         行
     * @param import_mode 导入mode
     * @param colPlatform colPlatform[0]平台对应起始列号,colPlatform[1]平台对应结束列号
     * @param mapSkuInDB  cms_bt_stock_separate_item的数据
     * @param saveData    保存对象
     */
    private void checkRecord(Row row, Row rowHeader, String import_mode, int[] colPlatform, Map<String, Map<String, StockExcelBean>> mapSkuInDB, List<StockExcelBean> saveData) {

        String model = row.getCell(0).getStringCellValue(); // Model
        String code = row.getCell(1).getStringCellValue(); // Code
        String sku = row.getCell(2).getStringCellValue(); // Sku

        // Model输入check
        if (StringUtils.isEmpty(model) || model.getBytes().length > 50) {
            throw new BusinessException("Model必须输入且长度小于50！" + "Sku=" + sku);
        }
        // Code输入check
        if (StringUtils.isEmpty(code) || code.getBytes().length > 50) {
            throw new BusinessException("Code必须输入且长度小于50！" + "Sku=" + sku);
        }
        // Sku输入check
        if (StringUtils.isEmpty(sku) || sku.getBytes().length > 50) {
            throw new BusinessException("Sku必须输入且长度小于50！" + "Sku=" + sku);
        }

        for (int index = 3; index <= colPlatform[0] - 2; index++) {
            // 属性
            String property = row.getCell(index).getStringCellValue();
            if (StringUtils.isEmpty(property) || property.getBytes().length > 500) {
                throw new BusinessException(rowHeader.getCell(index).getStringCellValue() + "必须输入且长度小于500！" + "Sku=" + sku);
            }
        }

        // 可用库存输入check
        String usableStock = row.getCell(colPlatform[0] - 1).getStringCellValue();
        if (StringUtils.isEmpty(usableStock) || !StringUtils.isDigit(usableStock) || usableStock.getBytes().length > 9) {
            throw new BusinessException("可用库存必须输入小于10位的整数！" + "Sku=" + sku);
        }

        // 隔离库存的平台数
        int stockCnt = 0;
        for (int index = colPlatform[0]; index < colPlatform[1]; index++) {
            // 平台隔离库存
            String separate_qty = row.getCell(index).getStringCellValue();
            // 平台号
            String cartId = rowHeader.getCell(index).getCellComment().getString().getString();

            boolean isDYNAMIC = false;
            if (DYNAMIC.equals(separate_qty)) {
                // 动态
                isDYNAMIC = true;
            } else {
                if (StringUtils.isEmpty(separate_qty) || !StringUtils.isDigit(separate_qty) || separate_qty.getBytes().length > 9) {
                    throw new BusinessException("隔离库存必须输入小于10位的整数,或者输入'" + DYNAMIC + "'！" + "Sku=" + sku);
                }
                stockCnt++;
            }

            // DB里本条sku对应的平台的数据Map<平台id，数据>
            Map<String, StockExcelBean> mapCartIdInDB = mapSkuInDB.get(sku);

            if (EXCEL_IMPORT_ADD.equals(import_mode)) {
                // 增量方式
                if (mapCartIdInDB != null) {
                    throw new BusinessException("Sku=" + sku + "的数据在DB里已经存在,不能增量！");
                }

                StockExcelBean bean = new StockExcelBean();
                bean.setProduct_model(model);
                bean.setProduct_code(code);
                bean.setSku(sku);
                bean.setCart_id(cartId);
                for (int c = 3; c <= colPlatform[0] - 2; c++) {
                    // 属性
                    bean.setProperty(rowHeader.getCell(c).getCellComment().getString().getString(), row.getCell(c).getStringCellValue());
                }
                bean.setQty(new BigDecimal(usableStock));
                if (isDYNAMIC) {
                    // 动态
                    bean.setSeparate_qty(new BigDecimal("-1"));
                } else {
                    bean.setSeparate_qty(new BigDecimal(separate_qty));
                    bean.setStatus("0");
                }

                saveData.add(bean);
            } else {
                // 变更方式
                if (mapCartIdInDB == null) {
                    throw new BusinessException("Sku=" + sku + "的数据在DB里不存在,不能变更！");
                }

                StockExcelBean beanInDB = mapCartIdInDB.get(cartId);
                if (beanInDB == null) {
                    throw new BusinessException("Sku=" + sku + "的DB数据的平台信息错误！");
                }
                if (!model.equals(beanInDB.getProduct_model())) {
                    throw new BusinessException("变更方式导入时,Model不能变更！" + "Sku=" + sku);
                }
                if (!code.equals(beanInDB.getProduct_code())) {
                    throw new BusinessException("变更方式导入时,Code不能变更！" + "Sku=" + sku);
                }
                if (!sku.equals(beanInDB.getSku())) {
                    throw new BusinessException("变更方式导入时,Sku不能变更！" + "Sku=" + sku);
                }

                for (int c = 3; c <= colPlatform[0] - 2; c++) {
                    // 属性
                    String propertyNa = rowHeader.getCell(c).getCellComment().getString().getString();
                    String property = row.getCell(c).getStringCellValue();
                    if (!property.equals(beanInDB.getProperty(propertyNa))) {
                        throw new BusinessException("变更方式导入时," + rowHeader.getCell(c).getStringCellValue() + "不能变更！" + "Sku=" + sku);
                    }
                }

                if (!usableStock.equals(beanInDB.getQty().toPlainString())) {
                    throw new BusinessException("变更方式导入时,可用库存不能变更！" + "Sku=" + sku);
                }

                boolean isUpdate = false; // 更新对象
                if (isDYNAMIC) {
                    // 动态
                    if (!StringUtils.isEmpty(beanInDB.getStatus())) {
                        // DB非动态
                        isUpdate = true;
                    }
                } else {
                    // 非动态
                    if (StringUtils.isEmpty(beanInDB.getStatus()) || !separate_qty.equals(beanInDB.getSeparate_qty().toPlainString())) {
                        // DB动态或数量不一致
                        isUpdate = true;
                    }
                }

                if (isUpdate) {
                    StockExcelBean bean = new StockExcelBean();
                    bean.setProduct_model(model);
                    bean.setProduct_code(code);
                    bean.setSku(sku);
                    bean.setCart_id(cartId);
                    for (int c = 3; c <= colPlatform[0] - 2; c++) {
                        // 属性
                        bean.setProperty(rowHeader.getCell(c).getCellComment().getString().getString(), row.getCell(c).getStringCellValue());
                    }
                    bean.setQty(new BigDecimal(usableStock));
                    if (isDYNAMIC) {
                        // 动态
                        bean.setSeparate_qty(new BigDecimal("-1"));
                    } else {
                        bean.setSeparate_qty(new BigDecimal(separate_qty));
                        if ("2".equals(beanInDB.getStatus()) || "7".equals(beanInDB.getStatus())) {
                            bean.setStatus("7");
                        } else {
                            bean.setStatus("0");
                        }
                    }

                    saveData.add(bean);
                }
            }
        }
        // 隔离库存的平台数 = 0
        if (stockCnt == 0) {
            throw new BusinessException("Sku=" + sku + "的数据至少隔离一个平台的库存值！");
        }

        if (!DYNAMIC.equals(row.getCell(colPlatform[1]).getStringCellValue())) {
            throw new BusinessException("Sku=" + sku + "的其它平台栏不是动态！");
        }
    }

    /**
     * 导入文件数据更新
     *
     * @param saveData    保存对象
     * @param import_mode 导入方式
     * @param task_id     任务id
     * @param creater     创建者/更新者
     */
    private void saveImportData(List<StockExcelBean> saveData, String import_mode, String task_id, String creater) {
        try {
            transactionRunner.runWithTran(() -> {
                if (EXCEL_IMPORT_UPDATE.equals(import_mode)) {
                    // 变更方式
                    for (StockExcelBean bean : saveData) {
                        Map<String, Object> mapSaveData =  new HashMap<String, Object>();
                        mapSaveData.put("taskId", task_id);
                        mapSaveData.put("sku", bean.getSku());
                        mapSaveData.put("cartId", bean.getCart_id());
                        mapSaveData.put("separateQty", bean.getSeparate_qty());
                        mapSaveData.put("status", StringUtils.null2Space(bean.getStatus()));
                        mapSaveData.put("modifier", creater);

                        updateImportData(mapSaveData);
                    }
                } else {
                    // 增量方式
                    List<Map<String, Object>> listSaveData = new ArrayList<Map<String, Object>>();
                    for (StockExcelBean bean : saveData) {
                        Map<String, Object> mapSaveData;
                        try {
                            mapSaveData = PropertyUtils.describe(bean);
                        } catch (Exception e) {
                            throw new BusinessException("导入文件有数据异常");
                        }

                        mapSaveData.put("task_id", task_id);
                        mapSaveData.put("creater", creater);

                        listSaveData.add(mapSaveData);
                        if (listSaveData.size() == 200) {
                            insertImportData(listSaveData);
                            listSaveData.clear();
                        }
                    }

                    if (listSaveData.size() > 0) {
                        insertImportData(listSaveData);
                        listSaveData.clear();
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusinessException("更新异常,请重新尝试！");
        }
    }

    /**
     * 导入文件数据插入更新
     *
     * @param saveData 保存对象
     */
    private void insertImportData(List<Map<String, Object>> saveData) {
        cmsBtStockSeparateItemDao.insertStockSeparateItemFromExcel(saveData);
    }

    /**
     * 导入文件数据变更更新
     *
     * @param saveData 保存对象
     */
    private void updateImportData(Map<String, Object> saveData) {
        cmsBtStockSeparateItemDao.updateStockSeparateItem(saveData);
    }

    /**
     * 库存隔离数据输入Check
     *
     * @param stockList 库存隔离数据
     */
    private void checksSparationQty(List<Map<String,Object>> stockList) throws BusinessException{
        for (Map<String, Object> stockInfo : stockList) {
            // 平台隔离数据
            List<Map<String, Object>> platformStockList = (List<Map<String, Object>>) stockInfo.get("platformStock");
            for (Map<String, Object> platformInfo : platformStockList) {
                // 库存隔离值
                String separationQty = (String) platformInfo.get("separationQty");
                // 状态
                String status = (String) platformInfo.get("status");
                // 动态以外的场合
                if (!StringUtils.isEmpty(status)) {
                    if (StringUtils.isEmpty(separationQty) || !StringUtils.isDigit(separationQty) || separationQty.getBytes().length > 9 ) {
                        throw new BusinessException("隔离库存必须输入小于10位的整数！");
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
        sql += getWhereSql(param, false);
        if (!StringUtils.isEmpty((String) param.get("status"))) {
            sql += " and sku in (select sku from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix") + getWhereSql(param, false) +
                    " and status = '" + (String) param.get("status") + "')";        }
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


//    /**
//     * 取得实时库存状态一页表示的Sku的Sql
//     *
//     * @param param 客户端参数
//     * @return 一页表示的Sku的Sql
//     */
//    private String getRealStockPageSkuSql(Map param){
//        String sql = "select sku from ( select distinct sku as sku from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
//        sql += getWhereSql(param, true) + " order by product_code,sku) t1 ";
//        String start = String.valueOf(param.get("start2"));
//        String length = String.valueOf(param.get("length2"));
//        sql += " limit " + start + "," + length;
//        return sql;
//    }

    /**
     * 取得各种状态统计数量的Sql
     *
     * @param param 客户端参数
     * @return 各种状态统计数量的Sql
     */
    private String getStockStatusCountSql(Map param){
        String sql = "select status,count(*) as count from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
        sql += getWhereSql(param, false);
        sql += " group by status";
        return sql;
    }

    /**
     * 取得一页表示的Sku数量的Sql
     *
     * @param param 客户端参数
     * @return 一页表示的Sku的Sql
     */
    private String getStockSkuCountSql(Map param){
        String sql = "select count(distinct sku) as count from voyageone_cms2.cms_bt_stock_separate_item" + (String) param.get("tableNameSuffix");
        sql += getWhereSql(param, true);
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

        // 商品model
        if (!StringUtils.isEmpty((String) param.get("model"))) {
            whereSql += " and product_model = '" + escapeSpecialChar((String) param.get("model")) + "'";
        }

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
     * 某个任务对应的Promotion是否已经开始（只要有一个Promotion开始就认为已经开始）
     *
     * @param taskId 任务id
     * @return 实时库存表示状态
     */
    private boolean isPromotionStart(String taskId){

        // 取得任务下的平台平台信息
        Date now = DateTimeUtil.parse(DateTimeUtil.getNow());
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", taskId);
        List<Map<String, Object>> platformList = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        for (Map<String, Object> platformInfo : platformList) {
            // Promotion开始时间
            String activityStart = (String) platformInfo.get("activity_start");
            if (!StringUtils.isEmpty(activityStart)) {
                if (activityStart.length() == 10) {
                    activityStart = activityStart + " 00:00:00";
                }
                Date activityStartDate = DateTimeUtil.parse(activityStart);
                if (now.getTime() - activityStartDate.getTime() > 0) {
                    return true;
                }
            }
        }
        return false;
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