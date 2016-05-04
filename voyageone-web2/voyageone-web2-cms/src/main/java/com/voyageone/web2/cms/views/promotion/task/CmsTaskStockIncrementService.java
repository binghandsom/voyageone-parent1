package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.StockSeparateService;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by jeff.duan on 2016/03/04.
 */
@Service
public class CmsTaskStockIncrementService extends BaseAppService {

    @Autowired
    private CmsTaskStockIncrementDetailService cmsTaskStockIncrementDetailService;

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private StockSeparateService stockSeparateService;

    @Autowired
    private CmsTaskStockService cmsTaskStockService;

    /**
     * 判断隔离任务:1新规的场合
     */
    private static final String TYPE_INCREMENT_INSERT = "1";
    /**
     * 判断隔离任务:2更新的场合
     */
    private static final String TYPE_INCREMENT_UPDATE = "2";
    /**
     * 1：按比例增量隔离
     */
    private static final String TYPE_INCREMENT_PERCENT = "1";
    /**
     * 2：按数量增量隔离
     */
    private static final String TYPE_INCREMENT_COUNT = "2";

    /**
     * 检索增量库存隔离任务
     *
     * @param param 客户端参数
     * @return 增量库存隔离任务列表
     * {"subTaskId":"1", "subTaskName":"天猫国际双11-增量任务1", "cartName":"天猫"},
     * {"subTaskId":"2", "subTaskName":"天猫国际双11-增量任务2", "cartName":"京东"}
     */
    public List<Map<String, Object>> getStockIncrementTaskList(Map<String, Object> param) {

        // 检索增量库存隔离任务
        List<Map<String, Object>> taskListDB = stockSeparateService.getStockSeparateIncrementTask(param);
        // 生成只有taskName和cartName的平台信息列表
        List<Map<String, Object>> taskList = new ArrayList<>();
        for (Map<String, Object> taskDB : taskListDB) {
            Map<String, Object> task = new HashMap<>();
            task.put("subTaskId", String.valueOf(taskDB.get("id")));
            task.put("subTaskName", taskDB.get("sub_task_name"));
            task.put("cartName", taskDB.get("name"));
            taskList.add(task);
        }
        return taskList;
    }

    /**
     * 根据SubTaskID取得增量隔离信息
     */
    public Map<String, Object> getIncrementInfoBySubTaskID(Map<String, Object> param) {
        //判断增量任务:更新的场合
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("subTaskId", param.get("incrementSubTaskId"));
        List<Map<String, Object>> stockIncrementInfo = stockSeparateService.getStockSeparateIncrementTask(sqlParam);
        Map<String, Object> incrementInfoMap = new HashMap<>();
        for (Map<String, Object> aStockIncrementInfo : stockIncrementInfo) {
            //增量任务名称
            incrementInfoMap.put("incrementTaskName", aStockIncrementInfo.get("sub_task_name"));
            //增量类型
            incrementInfoMap.put("incrementType", aStockIncrementInfo.get("type"));
            //增量值
            incrementInfoMap.put("incrementValue", aStockIncrementInfo.get("value"));
            //Cart_ID
            incrementInfoMap.put("incrementCartId", aStockIncrementInfo.get("cart_id"));

        }
        return incrementInfoMap;
    }

    /**
     * 新建/修改增量库存隔离任务
     */
    public void saveIncrementInfoByTaskID(Map<String, Object> param) {
        //判断增量任务:新规场合
        String promotionType = (String) param.get("promotionType");
        //画面入力信息的CHECK
        checkIncrementInfo(param);
        //判断增量任务:新规场合
        if (promotionType.equals(TYPE_INCREMENT_INSERT)) {
            simpleTransaction.openTransaction();
            try {
                //更新的场合
                //根据增量任务id，将增量任务信息插入到cms_bt_stock_separate_increment_task表
                Map<String, Object> stockSeparateIncrementTaskMap = getAddIncrementTaskByTaskID(param);
                //根据增量任务id，抽出cms_bt_stock_separate_item表中的所有sku后，计算出各sku的可用库存数并且根据增量任务的设定。
                // 计算出增量库存隔离数，插入到cms_bt_stock_separate_increment_item表中。（固定值隔离标志位=0：按动态值进行增量隔离）
                insertStockSeparateIncrementByTaskID(param, stockSeparateIncrementTaskMap);
            } catch (Exception e) {
                simpleTransaction.rollback();
                throw e;
            }
            simpleTransaction.commit();

        }
        //判断增量任务:更新场合
        if (promotionType.equals(TYPE_INCREMENT_UPDATE)) {
            //如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表
            stockSeparateService.updateIncrementTaskByTaskID(param);
        }
    }

    /**
     * 画面入力信息的CHECK
     */
    private void checkIncrementInfo(Map<String, Object> param) {
        //增量类型
        String incrementType = String.valueOf(param.get("incrementType"));
        //增量数值
        String incrementValues = String.valueOf(param.get("incrementValue"));
        //任务名
        String incrementTaskName = String.valueOf(param.get("incrementTaskName"));
        //incrementCartId
        String incrementCartId = String.valueOf(param.get("incrementCartId"));
        //incrementTaskId
        String incrementTaskId = String.valueOf(param.get("incrementTaskId"));

        //渠道的判断
        if (null == param.get("incrementCartId")) {
            //请选择增量的隔离渠道
            throw new BusinessException("7000071");
        }
        //增量类型:增量数量(incrementPercent) 增量百分比(incrementCount)
        if (null == param.get("incrementType")) {
            //请选择增量的增量类型
            throw new BusinessException("7000072");
        }
        //增量类的判断
        if (incrementType.equals(TYPE_INCREMENT_PERCENT)) {
            //百分比增量
            if (incrementValues.contains("%")) {
                if (incrementValues.startsWith("%")) {
                    //隔离平台的隔离比例必须填且为大于0小于100整数
                    throw new BusinessException("7000056");
                } else {
                    //隔离平台的隔离比例
                    String separate = incrementValues.substring(0, incrementValues.lastIndexOf("%"));
                    if (separate.contains("%") || !StringUtils.isDigit(separate)
                            || Integer.parseInt(separate)>100 ) {
                        throw new BusinessException("7000056");
                    }
                }
            } else {
                if (StringUtils.isEmpty(incrementValues) || !StringUtils.isDigit(incrementValues)
                        || Integer.parseInt(incrementValues)>100) {
                    //隔离平台的隔离比例必须填且为大于0小于100整数
                    throw new BusinessException("7000056");
                }
            }
        }
        if (incrementType.equals(TYPE_INCREMENT_COUNT)) {
            //数量增量
            if (StringUtils.isEmpty(incrementValues) || !StringUtils.isDigit(incrementValues)
                    || incrementValues.getBytes().length > 9) {
                // 增量必须为大于0的整数
                throw new BusinessException("7000055");
            }
        }
        //任务名称
        if (StringUtils.isEmpty(incrementTaskName) || incrementTaskName.getBytes().length > 1000) {
            // 任务名必须输入且长度小于1000
            throw new BusinessException("7000012");
        }
        //增量时间check

        // 取得任务id对应的Promotion是否未开始或者已经结束
        boolean promotionDuringFlg = cmsTaskStockIncrementDetailService.isPromotionDuring(incrementTaskId, incrementCartId);
        if (!promotionDuringFlg) {
            // 活动未开始或者已经结束，不能增量
            throw new BusinessException("7000062");
        }
    }

    /**
     * 如果是新建增量任务时（参数.增量任务id没有内容），将增量任务信息插入到cms_bt_stock_separate_increment_task表
     */
    private Map<String, Object> getAddIncrementTaskByTaskID(Map<String, Object> param) {
        Map<String, Object> mapSaveData = new HashMap<>();
        //一般任务ID
        mapSaveData.put("task_id", param.get("incrementTaskId"));
        //任务名
        mapSaveData.put("sub_task_name", param.get("incrementTaskName"));
        //平台ID
        mapSaveData.put("cart_id", param.get("incrementCartId"));
        //类型
        mapSaveData.put("type", param.get("incrementType"));
        //隔离比例/隔离值
        mapSaveData.put("value", param.get("incrementValue").toString().replace("%", ""));
        //创建者
        mapSaveData.put("creater", param.get("userName"));
        //更新者
        mapSaveData.put("modifier", param.get("userName"));
        //cmsBtStockSeparateIncrementTaskDao.insertStockSeparateIncrementTask(mapSaveData);
        //任务ID
        //return String.valueOf(mapSaveData.get("sub_task_id"));
        return mapSaveData;
    }

    /**
     * insertStockSeparateIncrementByTaskID
     */
    private void insertStockSeparateIncrementByTaskID(Map<String, Object> param, Map<String, Object> stockSeparateIncrementTaskMap) {
        //channelID
        String channelID = param.get("channel_id").toString();
        //根据channelID取得可用的库存
        Map<String, Integer> skuStockUsableAll = cmsTaskStockService.getUsableStock(channelID);
        //根据任务ID和CartId取得该平台下的SKU
        Map<String, Object> allSkuHash = getStockSeparateItemSkuByIncrementInfo(param, skuStockUsableAll);
        //根据allSkuProHash和SeparateHashMaps将Sku基本情报信息到和可用库存插入到cms_bt_stock_separate_increment_item表
        stockSeparateService.saveStockSeparateIncrementTaskAndItemList(stockSeparateIncrementTaskMap, allSkuHash);
    }

    /**
     * 根据任务ID和CartId取得该平台下的SKU
     */
    private Map<String, Object> getStockSeparateItemSkuByIncrementInfo(Map<String, Object> param, Map<String, Integer> skuStockUsableAll) {
        Map<String, Object> sqlParam = new HashMap<>();
        //任务ID
        sqlParam.put("taskId", param.get("incrementTaskId"));
        //平台id
        sqlParam.put("cartId", param.get("incrementCartId"));
        //状态
        sqlParam.put("status", CmsTaskStockService.STATUS_SEPARATE_SUCCESS);
        //增量类型
        String incrementType = param.get("incrementType").toString();
        //增量值
        String incrementValue = param.get("incrementValue").toString().replace("%", "");
        //隔离比例
        int separate_percent;
        //可用库存
        String usableStockInt;
        //增量库存
        int incrementQty;
        //Sku结果集
        Map<String, Object> skuMap = new HashMap<>();
        //取得隔离渠道SKU
        List<Map<String, Object>> skuList = stockSeparateService.getStockSeparateItem(sqlParam);
        //循环取得的SKU结果集
        for (Map<String, Object> aSkuList : skuList) {
            //取得对应的活动product_sku
            Map<String, Object> skuPro = new HashMap<>();
            String product_sku = aSkuList.get("sku").toString();
//            //增量任务ID
//            skuPro.put("subTaskId", subTaskId);
            //销售渠道
            skuPro.put("channelId", aSkuList.get("channel_id").toString());
            //产品model
            skuPro.put("productModel", aSkuList.get("product_model").toString());
            //产品code
            skuPro.put("productCode", aSkuList.get("product_code").toString());
            //产品Sku
            skuPro.put("sku", product_sku);
            if (skuStockUsableAll.keySet().contains(product_sku)) {
                //可用库存(取得可用库存)
                usableStockInt = String.valueOf(skuStockUsableAll.get(product_sku));
                skuPro.put("qty", usableStockInt);
            } else {
                // SKU(%s)在逻辑库存表里不存在
                throw new BusinessException("7000019", product_sku);
            }
            //数值增量
            if (incrementType.equals(TYPE_INCREMENT_COUNT)) {
                skuPro.put("incrementQty", Integer.parseInt(incrementValue));
            }
            //百分比增量
            if (incrementType.equals(TYPE_INCREMENT_PERCENT)) {
                //隔离库存比例
                separate_percent = Integer.parseInt(incrementValue);
                //隔离库存
                incrementQty = Math.round((Long.parseLong(usableStockInt) * separate_percent) / 100);
                skuPro.put("incrementQty", incrementQty);
            }
            //属性1（品牌）
            skuPro.put("property1", aSkuList.get("property1").toString());
            //属性2（英文短描述）
            skuPro.put("property2", aSkuList.get("property2").toString());
            //属性3（性别）
            skuPro.put("property3", aSkuList.get("property3").toString());
            //属性4（SIZE）
            skuPro.put("property4", aSkuList.get("property4").toString());
            //状态(0：未进行； 1：等待增量； 2：增量成功； 3：增量失败； 4：还原)
            skuPro.put("status", "0");
            //0：按动态值进行增量隔离； 1：按固定值进行增量隔离
            skuPro.put("fixFlg", "0");
            //创建者
            skuPro.put("creater", param.get("userName").toString());
            skuMap.put(product_sku, skuPro);
        }

        //返回Sku结果集合
        return skuMap;
    }


    /**
     * 删除增量库存隔离任务
     *
     * @param taskId    任务id
     * @param subTaskId 子任务id
     */
    public void delTask(String taskId, String subTaskId) {
        // 取得增量库存隔离数据中是否存在状态为"0:未进行"以外的数据
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("subTaskId", subTaskId);
        // 状态为"0:未进行"以外
        sqlParam.put("statusList", Arrays.asList(CmsTaskStockService.STATUS_WAITING_INCREMENT,
                CmsTaskStockService.STATUS_INCREASING,
                CmsTaskStockService.STATUS_INCREMENT_SUCCESS,
                CmsTaskStockService.STATUS_INCREMENT_FAIL,
                CmsTaskStockService.STATUS_REVERT));

        Integer seq = stockSeparateService.getStockSeparateIncrementItemByStatus(sqlParam);
        if (seq != null) {
            // 已经开始增量库存隔离，不能删除增量任务
            throw new BusinessException("7000057");
        }

        // 子任务id对应的增量库存隔离数据是否移到history表
        boolean historyFlg = cmsTaskStockIncrementDetailService.isHistoryExist(subTaskId);
        if (historyFlg) {
            // 已经开始增量库存隔离，不能删除增量任务
            throw new BusinessException("7000057");
        }

        stockSeparateService.delStockSeparateIncrementTask(subTaskId);
    }

}