package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.dao.wms.WmsBtLogicInventoryDao;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final NumberFormat numberFormatter = new DecimalFormat("#");

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
    private static final String NOT_END_FLG = "0";
    /** 结束状态  1: 结束 */
    private static final String END_FLG = "1";

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

    /** Excel的Title部平台显示文字 */
    private static final String PLATFORM = "Platform";
    /** Excel的Title部隔离类型显示文字 */
    private static final String SEPARATETYPE = "Separate Type";
    /** Excel的Title部错误信息显示文字 */
    private static final String ERRORMESSAGE = "Error Message";
    /** Excel的Title部发生时间显示文字 */
    private static final String ERRORTIME = "Error Time";

    /** Excel一般隔离类型显示文字 */
    private static final String TYPE_SEPARATE = "一般";
    /** Excel增量隔离类型显示文字 */
    private static final String TYPE_INCREMENT = "增量";

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
            //根据活动ID取得隔离的CartID
            cartNameMap=cmsBtPromotionDao.selectPromotionIDByCartId(entry.getKey());
            platformList.put(entry.getKey(),cartNameMap);
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
        typeChannelBeanList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A,language);
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
        StringBuffer sbCartName= new StringBuffer();
        String cartName = "";
        for(Map.Entry<String, String> isAllSqEntry : isAllSq.entrySet()){
            if(sq.keySet().contains(isAllSqEntry.getKey())){
                continue;
            }else{
                sbCartName.append(isAllSqEntry.getValue()+"|");
            }
        }
        // 去掉最后一个|
        cartName=sbCartName.substring(0,sbCartName.length()-1);
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
        data.put("onlySku", false);
        return data;
    }

    /**
     *
     *
     * @param param
     * @param lang
     * @return
     */
    public Map<String, Object> saveSeparateInfoByPromotionInfo(Map param, String lang) {
        //画面入力信息的CHECK
        checkPromotionInfo(param, lang);
        //将隔离任务信息（任务名，对应平台隔离比例，还原时间，优先顺等）反应到cms_bt_tasks
        saveTasksByPromotionInfo(param, lang);
        //表和cms_bt_stock_separate_platform_info表
        saveStockSeparatePlatFormByPromotionInfo(param, lang);
        //将隔离任务信息（任务名，对应平台隔离比例，还原时间，优先顺等）反应到cms_bt_stock_separate_platform_info表
        setPromotionInfo(param);
        //抽出隔离平台下面的所有Sku，取得商品基本情报，计算出可用库存数和各隔离平台的隔离数
        return null;
    }

    /**
     *
     * @param param
     * @param lang
     */
    private void saveTasksByPromotionInfo(Map param, String lang) {

    }

    /**
     *
     * @param param
     * @param lang
     */
    private void saveStockSeparatePlatFormByPromotionInfo(Map param, String lang) {

    }


    /**
     *
     * @param param
     * @param lang
     */
    private void checkPromotionInfo(Map param, String lang) {
        List<Map> separatePlatformList = new ArrayList<>();

        Map<String, Object> promotionList = (Map) param.get("promotionList");
        separatePlatformList= (List<Map>) promotionList.get("platformList");

        for(int i=0;i<separatePlatformList.size();i++){
            //平台id
            String carId=separatePlatformList.get(i).get("carId").toString();
            //平台名
            String cartName=separatePlatformList.get(i).get("cartName").toString();
            //隔离比例
            String value=separatePlatformList.get(i).get("value").toString();
            //类型（1：隔离，2：共享）
            String type= separatePlatformList.get(i).get("type").toString();
            // 还原时间
            String revertTime=separatePlatformList.get(i).get("revertTime").toString();
            //增优先顺
            String addPriority=separatePlatformList.get(i).get("addPriority").toString();
            //减优先顺
            String subtractPriority=separatePlatformList.get(i).get("subtractPriority").toString();

            if(type.equals("1")){
                //隔离平台的隔离比例
                if (StringUtils.isEmpty(value) || !StringUtils.isDigit(value)) {
                    throw new BusinessException("隔离平台的隔离比例为大于0的整数！");
                }
                //增优先顺
                if (StringUtils.isEmpty(addPriority) || !StringUtils.isDigit(addPriority)) {
                    throw new BusinessException("增优先顺为大于0的整数！");
                }
                //减优先顺
                if (StringUtils.isEmpty(subtractPriority) || !StringUtils.isDigit(subtractPriority)) {
                    throw new BusinessException("减优先顺为大于0的整数！");
                }
                //优先顺必须是1开始的连续整数

                //隔离结束时间必须是时间格式
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                try {
                    format.setLenient(false);
                    format.parse(revertTime);
                } catch (ParseException e){
                    throw new BusinessException("时间格式不正确,请填写正确的时间格式！");
                }
            }
        }

    }

    /**
     *
     * @param param
     */
    private void setPromotionInfo(Map param) {

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
                                                    STATUS_SEPARATING,
                                                    STATUS_SEPARATE_SUCCESS,
                                                    STATUS_SEPARATE_FAIL,
                                                    STATUS_WAITING_REVERT,
                                                    STATUS_REVERTING,
                                                    STATUS_REVERT_SUCCESS,
                                                    STATUS_REVERT_FAIL,
                                                    STATUS_CHANGED));
        Integer seq = cmsBtStockSeparateItemDao.selectStockSeparateItemByStatus(sqlParam);
        // 库存隔离数据中是否存在状态为"0:未进行"以外的数据,不允许删除任务
        if (seq != null) {
            throw new BusinessException("已经开始库存隔离，不能删除任务！");
        }

        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (promotionStartFlg) {
            throw new BusinessException("活动已经开始，不能删除任务！");
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
        List<TypeChannelBean> dynamicPropertyList = TypeChannels.getTypeList("dynamicProperty", (String) channelId);
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
        List<Map<String,Object>> stockCommonList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);
        // 实时库存状态查询时用
        param.put("stockCommonList", stockCommonList);

        for (Map<String,Object> stockInfo : stockCommonList) {
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
            String restoreSeparateTime = (String) platformInfo.get("revert_time");
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
        List<Map<String, Object>> stockRealList = (List<Map<String, Object>> ) param.get("stockCommonList");
        if (stockRealList == null) {
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            // 库存隔离明细一页表示的Sku的Sql
            sqlParam.put("sql", getStockPageSkuSql(param, "2"));
            stockRealList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);
            if (stockRealList == null || stockRealList.size() == 0) {
                return realStockList;
            }
        }
        // 一页中的sku列表（之后的检索用）
        List<String> skuList = new ArrayList<String>();
        for (Map<String,Object> stockInfo : stockRealList) {
            skuList.add((String) stockInfo.get("sku"));
        }

        // 取得一页中的sku所有平台的隔离库存（含非本任务的）
        Map<String,Object> sqlParam1 = new HashMap<String,Object>();
        sqlParam1.put("skuList", skuList);
        sqlParam1.put("channelId", param.get("channelId"));
        // 状态 = 2：隔离成功,5：等待还原, 6：还原中, 7：还原成功 ,8：还原失败
        sqlParam1.put("statusList", Arrays.asList( STATUS_SEPARATE_SUCCESS,
                                                    STATUS_WAITING_REVERT,
                                                    STATUS_REVERTING,
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
                // 如果状态为2：隔离成功那么加入到sku库存隔离信息（所有任务所有平台的数据）
                if (STATUS_SEPARATE_SUCCESS.equals(status)) {
                    if (skuStockAllTask.containsKey(sku)) {
                        skuStockAllTask.put(sku, skuStockAllTask.get(sku) + separateQty);
                    } else {
                        skuStockAllTask.put(sku, separateQty);
                    }
                }

                // 加入到sku库存隔离信息（该任务下的各个平台的数据），状态为5：等待还原, 6：还原中, 7：还原成功 ,8：还原失败也认为是隔离成功
                if (currentTaskId.equals(taskId)) {
                    skuStockByPlatform.put(sku + cartId, separateQty);
                }
            }
        }

        // 取得一页中的sku所有平台的增量隔离库存（含非本任务的）
        Map<String,Object> sqlParam2 = new HashMap<String,Object>();
        sqlParam2.put("skuList", skuList);
        sqlParam1.put("channelId", param.get("channelId"));
        // 状态 = 3：增量成功,5：还原,
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
                // 如果状态为3：隔离成功 那么加入到sku库存增量隔离信息（所有任务所有平台的数据）
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
            throw new BusinessException("活动已经开始，不能编辑数据！");
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
                throw new BusinessException("明细对象不存在！");
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
                            // 状态为"2:隔离中"或者"6:还原中"的数据不能进行变更
                            if(STATUS_SEPARATING.equals(statusDB) || STATUS_REVERTING.equals(statusDB)) {
                                throw new BusinessException("状态为 还原中 或者 隔离中 的明细不能进行编辑！");
                            }
                            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
                            sqlParam1.put("taskId", taskId);
                            sqlParam1.put("sku", sku);
                            sqlParam1.put("cartId", cartId);
                            sqlParam1.put("separateQty", separationQty);
                            sqlParam1.put("modifier", param.get("userName"));
                            // 导入前状态为"9：再修正"和"3: 隔离成功"以外，则导入后状态为"0：未进行"，导入前状态为"9：再修正"或"3: 隔离成功"，则导入后状态为"9：再修正"
                            String changedStatus = "";
                            if (STATUS_SEPARATE_SUCCESS.equals(statusDB) || STATUS_CHANGED.equals(statusDB)) {
                                sqlParam1.put("status", STATUS_CHANGED);
                                changedStatus = STATUS_CHANGED;
                            } else {
                                sqlParam1.put("status", STATUS_READY);
                                changedStatus = STATUS_READY;
                            }
                            int updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam1);
                            if (updateCnt == 1) {
                                String typeName = Types.getTypeName(63, (String) param.get("lang"), changedStatus);
                                platformInfo.put("status", typeName);
                            } else {
                                throw new BusinessException("明细对象不存在！");
                            }

                            // 导入前状态为"3: 隔离成功"的场合，更新隔离平台实际销售数据表(cms_bt_stock_sales_quantity)的end_flg为"1：结束"
                            if (STATUS_SEPARATE_SUCCESS.equals(statusDB)) {
                                Map<String, Object> sqlParam2 = new HashMap<String, Object>();
                                sqlParam2.put("channelId", param.get("channelId"));
                                sqlParam2.put("cartId", cartId);
                                sqlParam2.put("sku", sku);
                                sqlParam2.put("modifier", param.get("userName"));
                                sqlParam2.put("endFlg", END_FLG);
                                cmsBtStockSalesQuantityDao.updateStockSalesQuantity(sqlParam2);
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
                throw new BusinessException("明细对象不存在");
            }
            for (Map<String, Object> stockSeparateItem : stockSeparateItemList) {
                String status = (String) stockSeparateItem.get("status");
                // 只有状态为 0：未进行的数据可以删除
                if (!StringUtils.isEmpty(status) && !STATUS_READY.equals(status)) {
                    throw new BusinessException("只有状态为未进行的明细才能进行删除！");
                }
            }

            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
            sqlParam.put("taskId", taskId);
            sqlParam.put("sku", sku);
            int delCount = cmsBtStockSeparateItemDao.deleteStockSeparateItem(sqlParam);
            if (delCount == 0) {
                throw new BusinessException("明细对象不存在！");
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
        sqlParam1.put("channelId", channelId);
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
        sqlParam2.put("channelId", channelId);
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
        // 渠道id
        String channelId = (String) param.get("channelId");
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
            // 渠道id
            sqlParam.put("channelId", channelId);
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
        // 一般库存隔离状态 状态 = 3：隔离成功,5：等待还原, 6：等待中, 7：还原成功 ,8：还原失败
        sqlParam.put("statusStockList", Arrays.asList(STATUS_SEPARATE_SUCCESS,
                                                        STATUS_WAITING_REVERT,
                                                        STATUS_REVERTING,
                                                        STATUS_REVERT_SUCCESS,
                                                        STATUS_REVERT_FAIL));
        // 增量库存隔离状态 状态 = 3：增量成功,5：还原
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
            // 只有状态为"0:未进行"，"4:隔离失败"，"9:再修正"的数据可以进行隔离库存操作
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

        // 还原状态 1:所有活动未开始前的状态；2:至少有一个活动已经自动还原的状态
        String revertStatus = "";

        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("taskId"));
        if (!promotionStartFlg) {
            revertStatus = "1";
        }
        // 取得任务id对应的Promotion是否结束
        boolean promotionRevertFlg = isPromotionRevert((String) param.get("taskId"));
        if (promotionRevertFlg) {
            revertStatus = "2";
        }

        // 活动期间
        if ("".equals(revertStatus)) {
            throw new BusinessException("活动期间不能进行还原！");
        }

        simpleTransaction.openTransaction();
        try {
            // 画面选择的sku
            String selSku = (String) param.get("selSku");
            // 更新结果
            int updateCnt = 0;

            // 还原状态 1:所有活动未开始前的状态 的情况下，对状态为"3:隔离成功"，"8:还原失败"的数据可以进行还原
            // 状态为"3:隔离成功"的场合，并且更新隔离平台实际销售数据表(cms_bt_stock_sales_quantity)的end_flg为"1：结束"
            if ("1".equals(revertStatus)) {
                // 取得状态为状态为"3:隔离成功"的sku和平台id
                Map<String, Object> sqlParam = new HashMap<String, Object>();
                sqlParam.put("taskId", param.get("taskId"));
                // 选择一件sku进行库存还原的的场合,加入sku的条件
                if (!StringUtils.isEmpty(selSku)) {
                    sqlParam.put("sku", selSku);
                }
                sqlParam.put("status", STATUS_SEPARATE_SUCCESS);
                List<Map<String, Object>> stockList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);

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
                // 只有状态为"3:隔离成功"，"8:还原失败"的数据可以进行还原库存隔离操作。
                sqlParam2.put("statusList", Arrays.asList(STATUS_SEPARATE_SUCCESS, STATUS_REVERT_FAIL));
                updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam2);
                if (updateCnt == 0) {
                    throw new BusinessException("没有可以还原的数据！");
                }

                // 做一个以cartId为单位的包含sku的Map，Map<cartId, List<sku>>
                Map<String, List> cartSkuMap = new HashMap<String, List>();
                for (Map<String, Object> stock : stockList) {
                    String cartId =  String.valueOf(stock.get("cart_id"));
                    String sku = (String) stock.get("sku");
                    if (cartSkuMap.containsKey(cartId)) {
                        ArrayList<String> skuList = (ArrayList<String>) cartSkuMap.get(cartId);
                        skuList.add(sku);
                    } else {
                        ArrayList<String> skuList = new ArrayList<String>();
                        skuList.add(sku);
                        cartSkuMap.put(cartId, skuList);
                    }
                }

                // 更新隔离平台实际销售数据表(cms_bt_stock_sales_quantity)的end_flg为"1：结束"
                for (Map.Entry<String, List> cartSku : cartSkuMap.entrySet()) {
                    String cartId = cartSku.getKey();
                    List skuList = cartSku.getValue();
                    for(int i = 0; i < skuList.size(); i+= 500) {
                        int toIndex = i + 500;
                        if (toIndex > skuList.size()) {
                            toIndex = skuList.size();
                        }
                        List newList = skuList.subList(i, toIndex);
                        Map<String, Object> sqlParam1 = new HashMap<String, Object>();
                        sqlParam1.put("channelId", param.get("channelId"));
                        sqlParam1.put("cartId", cartId);
                        sqlParam1.put("skuList", newList);
                        sqlParam1.put("modifier", param.get("userName"));
                        sqlParam1.put("endFlg", END_FLG);
                        cmsBtStockSalesQuantityDao.updateStockSalesQuantity(sqlParam1);
                    }
                }

            } else {

                // 还原状态 2:至少有一个活动已经自动还原的状态 的情况下，对状态为 "8:还原失败"的数据可以进行还原
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
                // 只有状态为"3:隔离成功"，"8:还原失败"的数据可以进行还原库存隔离操作。
                sqlParam2.put("statusList", Arrays.asList(STATUS_REVERT_FAIL));
                updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam2);
                if (updateCnt == 0) {
                    throw new BusinessException("没有可以还原的数据！");
                }
            }

//            // 对库存隔离数据表进行数据还原
//            Map<String, Object> sqlParam2 = new HashMap<String, Object>();
//            // 选择一件sku进行库存还原的的场合,加入sku的条件
//            if (!StringUtils.isEmpty(selSku)) {
//                sqlParam2.put("sku", selSku);
//            }
//            // 更新状态为"4:等待还原"
//            sqlParam2.put("status", STATUS_WAITING_REVERT);
//            sqlParam2.put("modifier", param.get("userName"));
//            // 更新条件
//            sqlParam2.put("taskId", param.get("taskId"));
//            // 只有状态为"3:隔离成功"，"8:还原失败"的数据可以进行还原库存隔离操作。
//            sqlParam2.put("statusList", Arrays.asList(STATUS_SEPARATE_SUCCESS, STATUS_REVERT_FAIL));
//            updateCnt = cmsBtStockSeparateItemDao.updateStockSeparateItem(sqlParam2);
//            if (updateCnt == 0) {
//                throw new BusinessException("没有可以还原的数据！");
//            }

//            // 对增量库存隔离数据表进行数据还原
//            // 取得隔离对象对应的子任务id
//            List<Integer> subTaskIdList = new ArrayList<Integer>();
//            Map<String, Object> sqlParam = new HashMap<String, Object>();
//            sqlParam.put("taskId", param.get("taskId"));
//            List<Map<String, Object>> incrementTaskList = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(sqlParam);
//            for (Map<String, Object> incrementTask : incrementTaskList) {
//                subTaskIdList.add((Integer)incrementTask.get("sub_task_id"));
//            }
//
//            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
//            // 选择一件sku进行库存还原的的场合,加入sku的条件
//            if (!StringUtils.isEmpty(selSku)) {
//                sqlParam1.put("sku", selSku);
//            }
//            // 更新状态为"5:还原"
//            sqlParam1.put("status", STATUS_REVERT);
//            sqlParam1.put("modifier", param.get("userName"));
//            //更新条件
//            sqlParam1.put("subTaskIdList", subTaskIdList);
//            // 只对状态为"2:增量成功"的数据改变状态
//            sqlParam1.put("statusWhere", STATUS_INCREMENT_SUCCESS);
//            cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam1);

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
        CellStyle cellStyleNum = book.getSheetAt(1).getRow(0).getCell(3).getCellStyle(); // 数值（不锁定）的cellStyle
        CellStyle cellStyleNumLock = book.getSheetAt(1).getRow(0).getCell(6).getCellStyle(); // 数值（锁定）的cellStyle

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
                FileUtils.cell(row, colIndex++, cellStyleNumLock).setCellValue(Double.valueOf(rowData.getQty().toPlainString()));

                // 平台
                if (StringUtils.isEmpty(rowData.getStatus())) {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleDynamic).setCellValue(DYNAMIC);
                } else {
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleNum).setCellValue(Double.valueOf(rowData.getSeparate_qty().toPlainString()));
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
                    FileUtils.cell(row, mapCartCol.get(cart_id), cellStyleNum).setCellValue(Double.valueOf(rowData.getSeparate_qty().toPlainString()));
                }
            }
        }
    }

    /**
     * excel 导入
     *
     * @param param      客户端参数
     * @param file       导入文件
     * @param resultBean 返回内容
     */
    public void importExcelFileStockInfo(Map param, MultipartFile file, Map<String, Object> resultBean) {
        // 取得任务id对应的Promotion是否开始
        boolean promotionStartFlg = isPromotionStart((String) param.get("task_id"));
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
        Map<String, List<String>> mapSku = new HashMap<String, List<String>>();
        List<StockExcelBean> saveData = readExcel(file, import_mode, paramPropertyList, paramPlatformInfoList, mapSkuInDB, mapSku, resultBean);
        logger.info("导入Excel取得并check的处理结束");

        if (saveData.size() > 0) {
            logger.info("更新开始");
            saveImportData(saveData, mapSku, import_mode, task_id, (String) param.get("userName"), (String) param.get("channelId"));
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
     * @param mapSku               原隔离成功的sku(Map<cartId,List<sku>>)，用于更新cms_bt_stock_sales_quantity（隔离平台实际销售数据表）的end_flg为1：结束
     * @param resultBean            返回内容
     * @return 更新对象
     */
    private List<StockExcelBean> readExcel(MultipartFile file, String import_mode, List<Map> paramPropertyList, List<Map> paramPlatformInfoList, Map<String, Map<String, StockExcelBean>> mapSkuInDB, Map<String, List<String>> mapSku, Map<String, Object> resultBean) {
        List<StockExcelBean> saveData = new ArrayList<StockExcelBean>();
        List<String> listExcelSku = new ArrayList<String>();

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
        boolean hasExecutingData = false;
        for (Row row : sheet) {
            if (isHeader) {
                // 第一行Title行
                isHeader = false;
                // Title行check
                colPlatform = checkHeader(row, paramPropertyList, paramPlatformInfoList);
            } else {
                // 数据行
                boolean isExecutingData = checkRecord(row, sheet.getRow(0), import_mode, colPlatform, mapSkuInDB, saveData, listExcelSku, mapSku);
                if (isExecutingData && !hasExecutingData) {
                    hasExecutingData = true;
                }
            }
        }

        resultBean.put("hasExecutingData", hasExecutingData);

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
        if (!"Model".equals(getCellValue(row, 0))
                || !"Code".equals(getCellValue(row, 1))
                || !"Sku".equals(getCellValue(row, 2))) {
            throw new BusinessException(messageModelErr);
        }

        // 属性列check
        List<String> listPropertyKey = new ArrayList<String>();
        paramPropertyList.forEach(paramProperty -> listPropertyKey.add((String) paramProperty.get("property")));

        List<String> propertyList = new ArrayList<String>();
        int index = 3;
        for (; index <= 6; index++) {
            String comment = getCellCommentValue(row, index);
            if (StringUtils.isEmpty(comment)) {
                // 注解为空
                if (propertyList.size() > 0) {
                    // 已经有属性列，属性列结束
                    break;
                } else {
                    // 没有属性列，此列也不是属性列，错误Excel
                    throw new BusinessException(messageModelErr);
                }
            }
            if (!listPropertyKey.contains(comment)) {
                // 注解不是设定属性
                throw new BusinessException(messageModelErr);
            } else {
                // 注解是设定属性
                if (propertyList.contains(comment)) {
                    // 已经存在相同属性列
                    throw new BusinessException(messageModelErr);
                } else {
                    propertyList.add(comment);
                }
            }
        }

        if (propertyList.size() != listPropertyKey.size()) {
            // Excel属性列少于设定属性列
            throw new BusinessException(messageModelErr);
        }

        if (!USABLESTOCK.equals(getCellValue(row, index++))) {
            throw new BusinessException(messageModelErr);
        }

        // 平台对应起始列号
        colPlatform[0] = index;

        // 平台列check
        List<String> listPlatformKey = new ArrayList<String>();
        paramPlatformInfoList.forEach(paramPlatform -> listPlatformKey.add((String) paramPlatform.get("cartId")));

        List<String> platformList = new ArrayList<String>();
        while (true) {
            String comment = getCellCommentValue(row, index);
            if (StringUtils.isEmpty(comment)) {
                // 注解为空
                if (platformList.size() > 0) {
                    // 已经有平台列，平台列结束
                    break;
                } else {
                    // 没有平台列，此列也不是平台列，错误Excel
                    throw new BusinessException(messageModelErr);
                }
            }
            if (!listPlatformKey.contains(comment)) {
                // 注解不是设定平台
                throw new BusinessException(messageModelErr);
            } else {
                // 注解是设定平台
                if (platformList.contains(comment)) {
                    // 已经存在相同平台列
                    throw new BusinessException(messageModelErr);
                } else {
                    platformList.add(comment);
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

        if (!OTHER.equals(getCellValue(row, index))) {
            throw new BusinessException(messageModelErr);
        }

        return colPlatform;
    }

    /**
     * check数据，并返回保存对象
     *
     * @param row          数据行
     * @param rowHeader    Title行
     * @param import_mode  导入mode
     * @param colPlatform  colPlatform[0]平台对应起始列号,colPlatform[1]平台对应结束列号
     * @param mapSkuInDB   cms_bt_stock_separate_item的数据
     * @param saveData     保存对象
     * @param listExcelSku Excel输入的sku
     * @param mapSku      原隔离成功的sku(Map<cartId,List<sku>>)，用于更新cms_bt_stock_sales_quantity（隔离平台实际销售数据表）的end_flg为1：结束
     */
    private boolean checkRecord(Row row, Row rowHeader, String import_mode, int[] colPlatform, Map<String, Map<String, StockExcelBean>> mapSkuInDB, List<StockExcelBean> saveData, List<String> listExcelSku, Map<String, List<String>> mapSku) {
        boolean isExecutingData = false; // 是否是隔离中或者还原中数据

        String model = getCellValue(row, 0); // Model
        String code = getCellValue(row, 1); // Code
        String sku = getCellValue(row, 2); // Sku

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

        if (listExcelSku.contains(sku)) {
            throw new BusinessException("Sku重复输入！" + "Sku=" + sku);
        } else {
            listExcelSku.add(sku);
        }

        for (int index = 3; index <= colPlatform[0] - 2; index++) {
            // 属性
            String property = getCellValue(row, index);
            if (StringUtils.isEmpty(property) || property.getBytes().length > 500) {
                throw new BusinessException(getCellValue(rowHeader, index) + "必须输入且长度小于500！" + "Sku=" + sku);
            }
        }

        // 可用库存输入check
        String usableStock = getCellValue(row, colPlatform[0] - 1);
        if (StringUtils.isEmpty(usableStock) || !StringUtils.isDigit(usableStock) || usableStock.getBytes().length > 9) {
            throw new BusinessException("可用库存必须输入小于10位的整数！" + "Sku=" + sku);
        }

        // 隔离库存的平台数
        int stockCnt = 0;
        for (int index = colPlatform[0]; index < colPlatform[1]; index++) {
            // 平台隔离库存
            String separate_qty = getCellValue(row, index);
            // 平台号
            String cartId = getCellCommentValue(rowHeader, index);

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
                    bean.setProperty(getCellCommentValue(rowHeader, c), getCellValue(row, c));
                }
                bean.setQty(new BigDecimal(usableStock));
                if (isDYNAMIC) {
                    // 动态
                    bean.setSeparate_qty(new BigDecimal("-1"));
                } else {
                    bean.setSeparate_qty(new BigDecimal(separate_qty));
                    bean.setStatus(STATUS_READY);
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
                    String propertyNa = getCellCommentValue(rowHeader, c);
                    String property = getCellValue(row, c);
                    if (!property.equals(beanInDB.getProperty(propertyNa))) {
                        throw new BusinessException("变更方式导入时," + getCellValue(rowHeader, c) + "不能变更！" + "Sku=" + sku);
                    }
                }

                if (!usableStock.equals(beanInDB.getQty().toPlainString())) {
                    throw new BusinessException("变更方式导入时,可用库存不能变更！" + "Sku=" + sku);
                }

                boolean isUpdate = false; // 更新对象
                String dbStatus = beanInDB.getStatus();
                if (STATUS_SEPARATING.equals(dbStatus) || STATUS_REVERTING.equals(dbStatus)) {
                    // 隔离中或者还原中
                    isExecutingData = true;
                } else {
                    if (isDYNAMIC) {
                        // 动态
                        if (!StringUtils.isEmpty(dbStatus)) {
                            // DB非动态
                            isUpdate = true;
                        }
                    } else {
                        // 非动态
                        if (StringUtils.isEmpty(dbStatus) || !separate_qty.equals(beanInDB.getSeparate_qty().toPlainString())) {
                            // DB动态或数量不一致
                            isUpdate = true;
                        }
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
                        bean.setProperty(getCellCommentValue(rowHeader, c), getCellValue(row, c));
                    }
                    bean.setQty(new BigDecimal(usableStock));
                    if (isDYNAMIC) {
                        // 动态
                        bean.setSeparate_qty(new BigDecimal("-1"));
                    } else {
                        bean.setSeparate_qty(new BigDecimal(separate_qty));
                        if (STATUS_SEPARATE_SUCCESS.equals(dbStatus)) {
                            bean.setStatus(STATUS_CHANGED);
                            if(mapSku.containsKey(cartId)) {
                                mapSku.get(cartId).add(sku);
                            } else {
                                mapSku.put(cartId, new ArrayList<String>(){{this.add(sku);}});
                            }
                        } else if (STATUS_CHANGED.equals(dbStatus)) {
                            bean.setStatus(STATUS_CHANGED);
                        } else {
                            bean.setStatus(STATUS_READY);
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

        if (!DYNAMIC.equals(getCellValue(row, colPlatform[1]))) {
            throw new BusinessException("Sku=" + sku + "的其它平台栏不是动态！");
        }

        return isExecutingData;
    }

    /**
     * 导入文件数据更新
     *
     * @param saveData    保存对象
     * @param mapSku     原隔离成功的sku(Map<cartId,List<sku>>)，用于更新cms_bt_stock_sales_quantity（隔离平台实际销售数据表）的end_flg为1：结束
     * @param import_mode 导入方式
     * @param task_id     任务id
     * @param creater     创建者/更新者
     * @param channelId   渠道id
     */
    private void saveImportData(List<StockExcelBean> saveData, Map<String, List<String>> mapSku, String import_mode, String task_id, String creater, String channelId) {
        try {
            transactionRunner.runWithTran(() -> {
                if (EXCEL_IMPORT_UPDATE.equals(import_mode)) {
                    // 变更方式
                    Map<String, Object> mapSaveData =  new HashMap<String, Object>();
                    mapSaveData.put("taskId", task_id);
                    mapSaveData.put("modifier", creater);
                    for (StockExcelBean bean : saveData) {
                        mapSaveData.put("sku", bean.getSku());
                        mapSaveData.put("cartId", bean.getCart_id());
                        mapSaveData.put("separateQty", bean.getSeparate_qty());
                        mapSaveData.put("status", StringUtils.null2Space(bean.getStatus()));

                        updateImportData(mapSaveData);
                    }

                    mapSaveData.clear();
                    mapSaveData.put("endFlg", "1");
                    mapSaveData.put("modifier", creater);
                    mapSaveData.put("channelId", channelId);
                    for(String cartId : mapSku.keySet()) {
                        mapSaveData.put("cartId", cartId);
                        List<String> listSku = mapSku.get(cartId);
                        int index = 0;
                        for (; index + 500 < listSku.size(); index = index + 500) {
                            mapSaveData.put("skuList", mapSku.get(cartId).subList(index, index + 500));
                            cmsBtStockSalesQuantityDao.updateStockSalesQuantity(mapSaveData);
                        }
                        mapSaveData.put("skuList", mapSku.get(cartId).subList(index, listSku.size()));
                        cmsBtStockSalesQuantityDao.updateStockSalesQuantity(mapSaveData);
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
                        mapSaveData.put("channelId", channelId);

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
     * 返回单元格注解值
     *
     * @param row 行
     * @param col 列
     * @return 单元格注解值
     */
    private String getCellCommentValue(Row row, int col) {
        if (row == null) return null;
        if (row.getCell(col) == null) return null;
        if (row.getCell(col).getCellComment() == null) return null;
        return row.getCell(col).getCellComment().getString().getString();
    }

    /**
     * 返回单元格值
     *
     * @param row 行
     * @param col 列
     * @return 单元格值
     */
    public String getCellValue(Row row, int col) {
        if (row == null) return null;
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return getCellValue(cell);
    }

    public String getCellValue(Cell cell) {
        String ret;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                ret = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                ret = null;
                break;
            case Cell.CELL_TYPE_FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = getCellValue(evaluator.evaluateInCell(cell));
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    ret = simpleDateFormat.format(cell.getDateCellValue());
                } else {
                    ret = numberFormatter.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            default:
                ret = null;
        }

        return ret;
    }

    /**
     * 导出Error日志Excel文档做成，数据流返回
     *
     * @param param 客户端参数
     * @return byte[] 数据流
     * @throws IOException
     * @throws InvalidFormatException
     */
    public byte[] getExcelFileStockErrorInfo(Map<String, Object> param) throws IOException, InvalidFormatException {
        String taskId = (String) param.get("taskId");

        String templatePath = Properties.readValue(CmsConstants.Props.STOCK_EXPORT_TEMPLATE);

        Map<String, Object> searchParam = new HashMap<String, Object>();
        searchParam.put("taskId", taskId);
        // 任务id对应的库存隔离数据是否移到history表
        boolean historyFlg = this.isHistoryExist(taskId);
        if (historyFlg) {
            searchParam.put("tableNameSuffix", "_history");
        } else {
            searchParam.put("tableNameSuffix", "");
        }
        $info("Error数据取得开始");
        List<Map<String, Object>> resultData = cmsBtStockSeparateItemDao.selectExcelStockErrorInfo(searchParam);
        $info("Error数据取得结束,共%d件", resultData.size());

        List<Map<String, Object>> platformList = this.getPlatformList(taskId, (String) param.get("channelId"), (String) param.get("lang"));
        Map<String, String> mapPlatform = new HashMap<String, String>();
        platformList.forEach(platform -> mapPlatform.put((String) platform.get("cartId"), (String) platform.get("cartName")));

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             SXSSFWorkbook book = new SXSSFWorkbook(new XSSFWorkbook(inputStream))) {
            // Titel行
            writeExcelStockErrorInfoHead(book.getXSSFWorkbook());
            // 数据行
            writeExcelStockErrorInfoRecord(book.getXSSFWorkbook(), resultData, mapPlatform);

            // 自适应列宽
            for (int i = 0; i < 7; i++) {
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
     * Error日志Excel的第一行Title部写入
     */
    private void writeExcelStockErrorInfoHead(Workbook book) {
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);
        CellStyle cellStyleProperty = book.getSheetAt(1).getRow(0).getCell(4).getCellStyle(); // 属性的cellStyle

        // 内容输出
        int index = 3;
        // 平台
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(PLATFORM);
        // 隔离类型
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(SEPARATETYPE);
        // 错误信息
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(ERRORMESSAGE);
        // 发生时间
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(ERRORTIME);

        // 筛选
        CellRangeAddress filter = new CellRangeAddress(0, 0, 0, index - 1);
        sheet.setAutoFilter(filter);
    }

    /**
     * Error日志Excel的数据写入
     */
    private void writeExcelStockErrorInfoRecord(Workbook book, List<Map<String, Object>> resultData, Map<String, String> mapPlatform) {
        Sheet sheet = book.getSheetAt(0);
        int lineIndex = 1; // 行号
        int colIndex; // 列号
        Row row;

        CellStyle cellStyleData = book.getSheetAt(1).getRow(0).getCell(5).getCellStyle(); // 数据（不锁定）的cellStyle

        for (Map<String, Object> rowData : resultData) {
            row = FileUtils.row(sheet, lineIndex++);
            colIndex = 0;

            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue((String) rowData.get("model")); // Model
            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue((String) rowData.get("code")); // Code
            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue((String) rowData.get("sku")); // Sku
            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(mapPlatform.get((String) rowData.get("cartId"))); // 平台

            // 隔离类型
            if ("1".equals((String) rowData.get("type"))) {
                // 一般隔离类型
                FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(TYPE_SEPARATE);
            } else {
                // 增量隔离类型
                FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(TYPE_INCREMENT);
            }

            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue((String) rowData.get("errorMsg")); // 错误信息
            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue((String) rowData.get("errorTime")); // 发生时间
        }
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
        sql += " (select qty_china from wms_bt_inventory_center_logic t50 where order_channel_id = '" + param.get("channelId") + "' and t50.sku = t1.sku) qty_china,";
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
    public String getWhereSql(Map param, boolean statusFlg){
        String whereSql = " where 1=1 ";

        // 子任务Id
        if (!StringUtils.isEmpty((String) param.get("subTaskId"))) {
            whereSql += " and sub_task_id = " + String.valueOf(param.get("subTaskId")) + " ";
        } else {
            // 任务Id
            if (!StringUtils.isEmpty((String) param.get("taskId"))) {
                whereSql += " and task_id = " + String.valueOf(param.get("taskId")) + " ";
            }
        }

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
     * @return Promotion是否开始
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
     * 某个任务对应的Promotion是否已经还原（只要有一个Promotion还原就认为已经还原）
     *
     * @param taskId 任务id
     * @return Promotion是否结束
     */
    private boolean isPromotionRevert(String taskId){
        // 取得任务下的平台平台信息
        Date now = DateTimeUtil.parse(DateTimeUtil.getNow());
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", taskId);
        List<Map<String, Object>> platformList = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        for (Map<String, Object> platformInfo : platformList) {
            // 库存隔离还原时间
            String restoreSeparateTime = (String) platformInfo.get("revert_time");
            // 系统时间大于这个任务中任意一个隔离平台的还原时间，认为已经还原
            if (!StringUtils.isEmpty(restoreSeparateTime)) {
                if (restoreSeparateTime.length() == 10) {
                    restoreSeparateTime = restoreSeparateTime + " 00:00:00";
                }
                Date restoreSeparateTimeDate = DateTimeUtil.parse(restoreSeparateTime);
                if (now.getTime() - restoreSeparateTimeDate.getTime() > 0) {
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