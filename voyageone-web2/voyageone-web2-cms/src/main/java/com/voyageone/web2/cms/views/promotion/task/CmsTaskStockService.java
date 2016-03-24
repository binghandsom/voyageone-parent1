package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.dao.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSalesQuantityDao cmsBtStockSalesQuantityDao;

    @Autowired
    private WmsBtLogicInventoryDao wmsBtLogicInventoryDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    /**
     *
     * @param selFlag
     * @param language
     * @return
     */
    public Map<String, Object> getSeparateInfoByPromotionID(Map<String, Boolean> selFlag,String language){

        //判断cms_bt_stock_separate_platform_info表中取得是否有对应的任务ID
        checkSeparatePromotionID(selFlag);
        //根据活动ID取得cms_bt_promotion隔离的CartID
        Map<String,Object>  separateCartIdMap =getPromotionIdByCartID(selFlag);
        //共同方法TypeChannel.getTypeListSkuCarts取得该公司的所有销售渠道
        List<TypeChannelBean> typeChannelBeanList =getCartNameByChannelId(selFlag, language);
        //取得隔离和未隔离的数据
        List<Map> allSeparateCartIdMap =getSeparateCartByTypeChannel(separateCartIdMap, typeChannelBeanList);
        //设定默认的增优先顺和设定默认的还原时间
        Map<String, Object> data = setSeparateDataMap(allSeparateCartIdMap);
        return data;
    }
    /**
     *
     * @param selFlag
     */
    private void checkSeparatePromotionID(Map<String, Boolean> selFlag){
        for (Map.Entry<String, Boolean> entry : selFlag.entrySet()) {
            //取得活动ID 渠道ID 还原时间
            HashMap<String, String> promotionInfo= cmsBtPromotionDao.selectPromotionIDByCartId(entry.getKey());
            // Promotion存在的存在时间小于还原时间返回错误画面
            for (Map.Entry<String, String> promotionInfoEntry : promotionInfo.entrySet()) {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //取得还原时间
                String activityStart=promotionInfo.get("activity_start").toString()+" 00:00:00";
                //取得当前系统时间
                Date date=new Date();
                String sysTime=fmt.format(date);
                Calendar activityStartCalendar = Calendar.getInstance();
                Calendar sysTimeCalendar = Calendar.getInstance();
                //还原时间检查
                if("activity_start".equals(promotionInfoEntry.getKey())){
                    try {
                        //取得还原时间
                        activityStartCalendar.setTime(fmt.parse(activityStart));
                        //当前时间
                        sysTimeCalendar.setTime(fmt.parse(sysTime));
                        int result=activityStartCalendar.compareTo(sysTimeCalendar);
                        if(result==0)
                            throw new BusinessException("隔离任务已存在请重新确认");
                        else if(result<0){
                            throw new BusinessException("隔离任务已存在请重新确认");
                        }
                    } catch (ParseException e) {
                        throw new BusinessException("时间格式不正确,请重新确认");
                    }
                }
                //系统任务的检查
                if("cart_id".equals(promotionInfoEntry.getKey())){
                    String cart_id=promotionInfoEntry.getKey();
                    String separateInfo= cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatFormInfoById(cart_id,sysTime);
                    // Code输入check
                    if (!StringUtils.isEmpty(separateInfo)) {
                        throw new BusinessException("隔离已任务已存在，请重新确认");
                    }
                }
            }
        }
    }
    /**
     *
     * @param selFlag
     * @return
     */
    private Map<String, Object> getPromotionIdByCartID(Map<String, Boolean> selFlag) {
        Map<String,Object> platformList  = new HashMap<>();
        Map<String,String> cartNameMap = new HashMap<>();
        HashMap<String,String> channelIdMap = new HashMap<>();
        String channelId="";
        for (Map.Entry<String, Boolean> entry : selFlag.entrySet()) {
            if(entry.getValue()){
                //根据活动ID取得隔离的CartID
                cartNameMap=cmsBtPromotionDao.selectPromotionIDByCartId(entry.getKey());
                platformList.put(entry.getKey(),cartNameMap);
            }
        }
        return platformList;
    }
    /**
     *
     * @param selFlag
     * @param language
     * @return
     */
    private List<TypeChannelBean> getCartNameByChannelId(Map<String, Boolean> selFlag,String language) {
        // 获取type channel bean
        List<TypeChannelBean> typeChannelBeanList = new ArrayList<>();
        String channelId="";
        for (Map.Entry<String, Boolean> entry : selFlag.entrySet()) {
            // 根据活动ID取得对应的ChannelId
            channelId= cmsBtPromotionDao.selectPromotionIDByChannelId(entry.getKey());
        }
        // 根据ChannelId获取cart list
        typeChannelBeanList = TypeChannel.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A,language);
        return typeChannelBeanList;
    }
    /**
     *
     * @param separateCartIdMap
     * @param typeChannelBeanList
     * @return
     */
    private List<Map> getSeparateCartByTypeChannel(Map<String, Object> separateCartIdMap, List<TypeChannelBean> typeChannelBeanList) {
        String activityEnd="";
        List<Map> separatePlatformList = new ArrayList<>();
        HashMap<String,String> sq=new HashMap();
        HashMap<String,String> isAllSq=new HashMap();
        for(TypeChannelBean cartId :typeChannelBeanList){
            for(Map.Entry<String, Object> entry : separateCartIdMap.entrySet()){
                Map<String,String> separatePlatformMap  = new HashMap<>();
                HashMap<String,Object> separateMap =(HashMap<String, Object>) entry.getValue();
                String sepCarId=separateMap.get("cart_id").toString();
                activityEnd=separateMap.get("activity_end").toString();
                if(sepCarId.equals(cartId.getValue().toString())){
                    // 平台id
                    separatePlatformMap.put("carId", cartId.getValue());
                    // 平台名
                    separatePlatformMap.put("cartName",cartId.getName());
                    // 隔离比例
                    separatePlatformMap.put("value","");
                    // 类型（1：隔离，2：共享）
                    separatePlatformMap.put("type","1");
                    // 还原时间
                    separatePlatformMap.put("revertTime", activityEnd);
                    sq.put(cartId.getValue(),cartId.getName());

                    separatePlatformList.add(separatePlatformMap);
                }
            }
            isAllSq.put(cartId.getValue(),cartId.getName());
        }
        // 获取未隔离的名称
        StringBuffer cartName= new StringBuffer();
        for(Map.Entry<String, String> sqEntry : sq.entrySet()){
            for(Map.Entry<String, String> isAllSqEntry : isAllSq.entrySet()){
                if(!sq.keySet().contains(isAllSqEntry.getKey())){
                    cartName.append(isAllSqEntry.getValue()+"|");
                }
            }
        }
        // 获取隔离和未隔离的数据
        Map<String,String> isNotSeparatePlatformMap  = new HashMap<>();
        // 平台id
        isNotSeparatePlatformMap.put("carId", "-1");
        // 平台名
        isNotSeparatePlatformMap.put("cartName",cartName.toString());
        // 隔离比例
        isNotSeparatePlatformMap.put("value","");
        // 类型（1：隔离，2：共享）
        isNotSeparatePlatformMap.put("type", "2");
        // 还原时间
        isNotSeparatePlatformMap.put("revertTime",activityEnd);
        separatePlatformList.add(isNotSeparatePlatformMap);
        return separatePlatformList;
    }
    /**
     *
     * @param allSeparateCartIdMap
     * @return
     */
    private Map<String,Object> setSeparateDataMap(List<Map> allSeparateCartIdMap) {
        // 获取隔离数据的属性
        Map<String, Object> data = new HashMap<>();
        List<Map> separatePlatformList = new ArrayList<>();
        int addPriority=0;
        int subtractPriority=allSeparateCartIdMap.size();
        for(int i=0;i<allSeparateCartIdMap.size();i++){
            Map<String,String> separatePlatformMap  = new HashMap<>();
            String separateType = allSeparateCartIdMap.get(i).get("type").toString();
            // 平台id
            separatePlatformMap.put("carId", allSeparateCartIdMap.get(i).get("carId").toString());
            // 平台名
            separatePlatformMap.put("cartName",allSeparateCartIdMap.get(i).get("cartName").toString());
            // 隔离比例
            separatePlatformMap.put("value",allSeparateCartIdMap.get(i).get("value").toString());
            // 类型（1：隔离，2：共享）
            separatePlatformMap.put("type",allSeparateCartIdMap.get(i).get("type").toString());
            // 还原时间
            separatePlatformMap.put("revertTime", allSeparateCartIdMap.get(i).get("revertTime").toString());
            //增优先顺
            addPriority++;
            separatePlatformMap.put("addPriority", String.valueOf(addPriority));
            //减优先顺
            separatePlatformMap.put("subtractPriority", String.valueOf(subtractPriority));
            subtractPriority--;
            separatePlatformList.add(separatePlatformMap);
        }
        data.put("platformList", separatePlatformList);
        data.put("taskName", "");
        data.put("onlySku",false);
        return data;
    }
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
     *    {"value":"property1", "name":"品牌", "logic":"", "type":"", "show":false, "value":""},
     *    {"value":"property2", "name":"英文短描述", "logic":"Like", "type":"", "show":false, "value":""},
     *    {"value":"property3", "name":"性别", "logic":"", "type":"", "show":false, "value":""}，
     *    {"value":"property4", "name":"Size", "logic":"", "type":"int", "show":false, "value":""}...
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
//        sqlParam1.put("status", "2");
        sqlParam1.put("statusList", Arrays.asList("2","4","5","6"));
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
                String status = (String) stockInfo.get("status");
                // 加入到sku库存隔离信息（所有任务所有平台的数据）
                if ("2".equals(status)) {
                    if (skuStockAllTask.containsKey(sku)) {
                        skuStockAllTask.put(sku, skuStockAllTask.get(sku) + separateQty);
                    } else {
                        skuStockAllTask.put(sku, separateQty);
                    }
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
//        sqlParam2.put("status", "2");
        sqlParam1.put("statusList", Arrays.asList("2","4"));
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
                String status = (String) stockIncrementInfo.get("status");
                // 加入到sku库存增量隔离信息（所有任务所有平台的数据）
                if ("2".equals(status)) {
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
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能删除数据！");
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
            String status = (String) stockSeparateItem.get("status");
            if (!StringUtils.isEmpty(status) && !"0".equals(status)) {
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
     * 取得可用库存
     *
     * @param param 客户端参数
     */
    public String getUsableStock(Map param){
        // sku没有输入的情况
        if (StringUtils.isEmpty((String) param.get("sku"))) {
            return "";
        }

        // 取得逻辑库存
        int logicStock = 0;
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sku", param.get("sku"));
        sqlParam.put("channelId", param.get("channelId"));
        Integer logicInventoryCnt = wmsBtLogicInventoryDao.selectLogicInventoryCnt(sqlParam);
        if (logicInventoryCnt != null) {
            logicStock = logicInventoryCnt;
        }

        // 取得隔离库存
        int stockSeparate = 0;
        Map<String,Object> sqlParam1 = new HashMap<String,Object>();
        sqlParam1.put("sku", param.get("sku"));
        sqlParam1.put("status", "2");
        Integer stockSeparateSuccessQty =  cmsBtStockSeparateItemDao.selectStockSeparateSuccessQty(sqlParam1);
        if (stockSeparateSuccessQty != null) {
            stockSeparate =  stockSeparateSuccessQty;
        }


        // 取得增量隔离库存
        int stockIncrementSeparate = 0;
        Map<String,Object> sqlParam2 = new HashMap<String,Object>();
        sqlParam2.put("sku", param.get("sku"));
        sqlParam2.put("status", "2");
        Integer stockSeparateIncrementSuccessQty =  cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementSuccessQty(sqlParam2);
        if (stockSeparateIncrementSuccessQty != null) {
            stockIncrementSeparate =  stockSeparateIncrementSuccessQty;
        }

        // 取得平台的销售数量
        int stockSalesQuantity = 0;
        Map<String,Object> sqlParam3 = new HashMap<String,Object>();
        sqlParam3.put("channelId", param.get("channelId"));
        sqlParam3.put("sku", param.get("sku"));
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
        // 平台隔的离库存
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
                sqlParam.put("status", "0");
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
        // 平台id
        sqlParam.put("sku", param.get("sku"));
        // 一般库存隔离状态
        sqlParam.put("statusStockList", Arrays.asList("2","4","5","6"));
        // 增量库存隔离状态
        sqlParam.put("statusStockIncrementList", Arrays.asList("2","4"));
        List<Map<String, Object>> stockHistoryList = cmsBtStockSeparateItemDao.selectStockSeparateDetailAll(sqlParam);

        return stockHistoryList;

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
        List<Map<String, Object>> resultData = cmsBtStockSeparateItemDao.selectExcelStockInfo(param);

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             XSSFWorkbook book = new XSSFWorkbook(inputStream)) {
            // Titel行
            Map<String, Integer> mapCartCol = writeExcelStockInfoHead(book, param);
            // 数据行
            writeExcelStockInfoRecord(book, param, resultData, mapCartCol);

            // 自适应列宽
            List<Map> propertyList = (List<Map>) param.get("propertyList");
            List<Map> platformList = (List<Map>) param.get("platformList");
            int cntCol = 3 + propertyList.size() + platformList.size() + 1;
            for (int i = 0; i < cntCol; i++) {
                book.getSheetAt(0).autoSizeColumn(i);
            }

            // 格式copy用sheet删除
            book.removeSheetAt(1);

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
        }

        // 可用库存
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue("Usable Stock");

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
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue("Other");

        // 筛选
        CellRangeAddress filter = new CellRangeAddress(0, 0, 0, index - 1);
        sheet.setAutoFilter(filter);

        return mapCartCol;
    }

    /**
     * 库存隔离Excel的数据写入
     */
    private void writeExcelStockInfoRecord(Workbook book, Map param, List<Map<String, Object>> resultData, Map<String, Integer> mapCartCol) {

        Sheet sheet = book.getSheetAt(0);
        String preSku = "";
        String cart_id = "";
        int lineIndex = 1; // 行号
        int colIndex = 0; // 列号
        Row row = null;

        Cell cellDynamic = book.getSheetAt(1).getRow(0).getCell(1); // 动态的cell
        CellStyle cellStyleDataLock = book.getSheetAt(1).getRow(0).getCell(2).getCellStyle(); // 数据（锁定）的cellStyle
        CellStyle cellStyleData = book.getSheetAt(1).getRow(0).getCell(3).getCellStyle(); // 数据（不锁定）的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");

        for (Map<String, Object> rowData : resultData) {
            cart_id = rowData.get("cart_id").toString();
            if (!mapCartCol.containsKey(cart_id)) {
                continue;
            }

            if (!preSku.equals((String) rowData.get("sku"))) {
                // 新sku
                preSku = (String) rowData.get("sku");
                row = FileUtils.row(sheet, lineIndex++);
                colIndex = 0;

                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue((String) rowData.get("product_model")); // Model
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue((String) rowData.get("product_code")); // Code
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue((String) rowData.get("sku")); // Sku

                // 属性
                for (Map property : propertyList) {
                    FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue((String) rowData.get(property.get("property")));
                }

                // 可用库存
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.get("qty").toString());

                // 平台
                if (StringUtils.isEmpty((String) rowData.get("status"))) {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellDynamic.getCellStyle()).setCellValue(cellDynamic.getStringCellValue());
                } else {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleData).setCellValue(rowData.get("separate_qty").toString());
                }

                CellStyle cellStyle = book.createCellStyle();
                cellStyle.cloneStyleFrom(cellDynamic.getCellStyle());
                cellStyle.setLocked(true);
                FileUtils.cell(row, mapCartCol.get("-1"), cellStyle).setCellValue(cellDynamic.getStringCellValue());
            } else {
                // 同一个sku，不同平台
                // 平台
                if (StringUtils.isEmpty((String) rowData.get("status"))) {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellDynamic.getCellStyle()).setCellValue(cellDynamic.getStringCellValue());
                } else {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleData).setCellValue(rowData.get("separate_qty").toString());
                }
            }
        }
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
        sql += getWhereSql(param, false) ;
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