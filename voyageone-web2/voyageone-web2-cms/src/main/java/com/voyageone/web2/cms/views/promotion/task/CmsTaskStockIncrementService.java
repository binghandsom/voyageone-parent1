package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementTaskDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

;

/**
 * Created by jeff.duan on 2016/03/04.
 */
@Service
public class CmsTaskStockIncrementService extends BaseAppService {
    @Autowired
    private CmsBtStockSeparateIncrementTaskDao cmsBtStockSeparateIncrementTaskDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsTaskStockIncrementDetailService cmsTaskStockIncrementDetailService;

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsTaskStockService cmsTaskStockService;
    /** 判断隔离任务:1新规的场合 */
    private  static final String TYPE_INCREMENT_INSERT="1";
    /** 判断隔离任务:2更新的场合 */
    private  static final String TYPE_INCREMENT_UPDATE="2";

    /**
     * 检索增量库存隔离任务
     *
     * @param param 客户端参数
     * @return 增量库存隔离任务列表
     *    {"subTaskId":"1", "subTaskName":"天猫国际双11-增量任务1", "cartName":"天猫"},
     *    {"subTaskId":"2", "subTaskName":"天猫国际双11-增量任务2", "cartName":"京东"}
     *
     */
    public List<Map<String,Object>> getStockIncrementTaskList(Map param) {

        // 检索增量库存隔离任务
        List<Map<String, Object>> taskListDB = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(param);
        // 生成只有taskName和cartName的平台信息列表
        List<Map<String, Object>> taskList = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> taskDB : taskListDB) {
            Map<String,Object> task = new HashMap<String,Object>();
            task.put("subTaskId", String.valueOf(taskDB.get("sub_task_id")));
            task.put("subTaskName", (String) taskDB.get("sub_task_name"));
            task.put("cartName", (String) taskDB.get("name"));
            taskList.add(task);
        }
        return taskList;
    }

    /**
     * 新建/修改增量库存隔离任务
     * @param param
     */
    public void saveIncrementInfoByTaskID(Map param){
        //判断增量任务:新规场合
        String promotionType = (String) param.get("promotionType");
        //判断增量任务:增量任务ID
        String incrementTaskID = (String) param.get("incrementTaskId");
        //画面入力信息的CHECK
        checkIncrementInfo(param, incrementTaskID);
        //判断增量任务:新规场合
        if(promotionType.equals(TYPE_INCREMENT_INSERT)){
            //根据增量任务id，将增量任务信息插入到cms_bt_stock_separate_increment_task表
            insertIncrementTaskByTaskID(param);
            //根据增量任务id，抽出cms_bt_stock_separate_item表中的所有sku后，计算出各sku的可用库存数并且根据增量任务的设定。
            // 计算出增量库存隔离数，插入到cms_bt_stock_separate_increment_item表中。（固定值隔离标志位=0：按动态值进行增量隔离）
            insertStockSeparateIncrementByTaskID(param);
        }
        //判断增量任务:更新场合
        if(promotionType.equals(TYPE_INCREMENT_UPDATE)){
            //如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表
            updateIncrementTaskByTaskID(param);
        }
    }

    /**
     * 画面入力信息的CHECK
     * @param param
     * @param incrementTaskID
     */
    private void checkIncrementInfo(Map param,String incrementTaskID) {
        //增量类型
        String incrementType = (String) param.get("incrementType");
        //增量数值
        String incrementValues = (String) param.get("incrementValue");
        //任务名
        String incrementTaskName = (String) param.get("incrementTaskName");
        //渠道的判断
        if (null==param.get("incrementCartId")){
            //请选择增量的隔离渠道
            throw new BusinessException("请选择增量的隔离渠道");
        }
        //增量类型:增量数量(incrementPercent) 增量百分比(incrementCount)
        if(null==param.get("incrementType")){
            //请选择增量的增量类型
            throw new BusinessException("请选择增量的增量类型");
        }
        //增量类的判断
        if(incrementType.equals("incrementPercent")){
            //百分比增量
            if(incrementType.equals("%")){
                // 增量比例必须输入且为大于0小于100整数
                throw new BusinessException("7000056");
            }else{
                String[] incrementValue = incrementValues.split("%");
                if (StringUtils.isEmpty(incrementValue[0])|| !StringUtils.isDigit(incrementValue[0])
                        ||incrementValue[0].getBytes().length>2||incrementValue.length>1) {
                    // 增量比例必须输入且为大于0小于100整数
                    throw new BusinessException("7000056");
                }
            }
        }
        if(incrementType.equals("incrementCount")){
            //数量增量
            if (StringUtils.isEmpty(incrementValues) || !StringUtils.isDigit(incrementValues)
                    ||incrementValues.getBytes().length>1) {
                // 增量必须为大于0的整数
                throw new BusinessException("7000055");
            }
        }
        //任务名称
        if (StringUtils.isEmpty(incrementTaskName)||incrementTaskName.getBytes().length>=1000) {
            // 任务名必须输入且长度小于1000
            throw new BusinessException("7000012");
        }
    }
    /**
     * 如果是新建增量任务时（参数.增量任务id没有内容），将增量任务信息插入到cms_bt_stock_separate_increment_task表
     * @param param
     */
    private void insertIncrementTaskByTaskID(Map param) {
        Map<String, Object> mapSaveData =new HashMap<>();
        //增量任务ID
        mapSaveData.put("sub_task_id", param.get(""));
        //一般任务ID
        mapSaveData.put("task_id", param.get(""));
        //任务名
        mapSaveData.put("sub_task_name", param.get(""));
        //平台ID
        mapSaveData.put("cart_id", param.get(""));
        //类型
        mapSaveData.put("type", param.get(""));
        //隔离比例/隔离值
        mapSaveData.put("value", param.get(""));
        //创建者
        mapSaveData.put("created", param.get(""));
        //更新者
        mapSaveData.put("modified", param.get(""));
        Integer seq = cmsBtStockSeparateIncrementTaskDao.insertStockSeparateIncrementTask(mapSaveData);
    }
    /**
     * 如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表
     * @param param
     */
    private void updateIncrementTaskByTaskID(Map param) {
        Map<String, Object> mapSaveData =new HashMap<>();
        //任务名
        mapSaveData.put("sub_task_name", param.get(""));
        //增量任务ID
        mapSaveData.put("sub_task_id", param.get(""));
        //创建者
        mapSaveData.put("created", param.get(""));
        //更新者
        mapSaveData.put("modified", param.get(""));
        Integer seq = cmsBtStockSeparateIncrementTaskDao.insertStockSeparateIncrementTask(mapSaveData);

    }
    /**
     *
     * @param param
     */
    private void insertStockSeparateIncrementByTaskID(Map param) {

        //channelID
        String channelID =param.get("").toString();
        //根据channelID取得可用的库存
        Map<String, Integer> skuStockUsableAll =cmsTaskStockService.getUsableStock(channelID);
        //根据任务ID和CartId取得该平台下的SKU
        HashMap<String,Object> allSkuHash=getStockSeparateItemSkuByIncrementInfo(param, skuStockUsableAll);
        //根据allSkuProHash和SeparateHashMaps将Sku基本情报信息到和可用库存插入到cms_bt_stock_separate_increment_item表
        saveStockSeparateIncrementItem(allSkuHash);

    }

    /**
     * 根据任务ID和CartId取得该平台下的SKU
     * @param param
     * @return skuList
     */
    private  HashMap<String,Object>  getStockSeparateItemSkuByIncrementInfo(Map param,Map<String, Integer> skuStockUsableAll){
        Map<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("taskId", param.get(""));
        sqlParam.put("cartId", param.get(""));
        String incrementType=param.get("incrementType").toString();
        String incrementValue=param.get("incrementValue").toString();
        List<Map<String, Object>> skuList= cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
        HashMap<String,Object> skuMap=  new HashMap();
        HashMap<String,Object> skuPro=  new HashMap();
        //隔离比例
        int separate_percent;
        //可用库存
        String usableStockInt;
        //增量库存
        int incrementQty;
        for(int i=0;i<skuList.size();i++){
            //取得对应的活动product_sku
            String product_sku =skuList.get(i).get("product_sku").toString();
            //增量任务ID
            skuPro.put("sub_task_id","");
            //销售渠道
            skuPro.put("channel_id","");
            //产品model
            skuPro.put("product_model","");
            //产品code
            skuPro.put("product_code","");
            //产品Sku
            skuPro.put("sku","");
            if(skuStockUsableAll.keySet().contains(product_sku)){
                //可用库存(取得可用库存)
                usableStockInt=String.valueOf(skuStockUsableAll.get(product_sku));
                skuPro.put("qty", usableStockInt);
            }else{
                // SKU(%s)在逻辑库存表里不存在
                throw new BusinessException("7000019", product_sku);
            }
            //数值增量
            if(incrementType.equals("incrementCount")){
                skuPro.put("separate_qty", Integer.parseInt(incrementValue));
            }
            //百分比增量
            if(incrementType.equals("incrementPercent")){
                //隔离库存比例
                separate_percent=Integer.parseInt(incrementValue);
                //隔离库存
                incrementQty=Math.round((Long.parseLong(usableStockInt)*separate_percent)/100);
                skuPro.put("separate_qty", incrementQty);
            }
            //属性1（品牌）
            skuPro.put("property1","");
            //属性2（英文短描述）
            skuPro.put("property2","");
            //属性3（性别）
            skuPro.put("property3","");
            //属性4（SIZE）
            skuPro.put("property4","");
            //状态(0：未进行； 1：等待增量； 2：增量成功； 3：增量失败； 4：还原)
            skuPro.put("status","");
            //0：按动态值进行增量隔离； 1：按固定值进行增量隔离
            skuPro.put("fix_flg","");
            //创建者
            skuPro.put("creater","");
            //更新者
            skuPro.put("modifier","");

            skuMap.put(product_sku,skuPro);
        }
        return skuMap;
    }
    /**
     * 根据allSkuProHash和SeparateHashMaps将Sku基本情报信息到和可用库存插入到cms_bt_stock_separate_increment_item表
     * @param allSkuHash
     */
    private void saveStockSeparateIncrementItem(HashMap<String, Object> allSkuHash) {
        for(int i=0;i<allSkuHash.size();i++){
            List<Map<String, Object>> allSku =new ArrayList<>();
            HashMap<String,Object> aSkuProHash = new HashMap();
            aSkuProHash.put("seq",allSkuHash.get(""));
            aSkuProHash.put("sub_task_id","");
            aSkuProHash.put("channel_id","");
            aSkuProHash.put("product_model","");
            aSkuProHash.put("product_code","");
            aSkuProHash.put("sku","");
            aSkuProHash.put("qty1","");
            aSkuProHash.put("qty2","");
            aSkuProHash.put("qty","");
            aSkuProHash.put("increment_qty","");
            aSkuProHash.put("property1","");
            aSkuProHash.put("property2","");
            aSkuProHash.put("property3","");
            aSkuProHash.put("property4","");
            aSkuProHash.put("error_msg","");
            aSkuProHash.put("error_time","");
            aSkuProHash.put("separate_time","");
            aSkuProHash.put("status","");
            aSkuProHash.put("fix_flg","");
            aSkuProHash.put("created","");
            aSkuProHash.put("creater","");
            aSkuProHash.put("modified","");
            aSkuProHash.put("modifier","");
            allSku.add(aSkuProHash);
            //批量更新
            if (allSku.size() > 500) {
                cmsBtStockSeparateIncrementItemDao.insertStockSeparateIncrementItemByList(allSku);
                allSku.clear();
            }
        }

    }
    /**
     * 删除增量库存隔离任务
     *
     * @param taskId 任务id
     * @param subTaskId 子任务id
     */
    public void delTask(String taskId, String subTaskId){
        // 取得增量库存隔离数据中是否存在状态为"0:未进行"以外的数据
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("subTaskId", subTaskId);
        // 状态为"0:未进行"以外
        sqlParam.put("statusList", Arrays.asList( CmsTaskStockService.STATUS_WAITING_INCREMENT,
                                                    CmsTaskStockService.STATUS_INCREASING,
                                                    CmsTaskStockService.STATUS_INCREMENT_SUCCESS,
                                                    CmsTaskStockService.STATUS_INCREMENT_FAIL,
                                                    CmsTaskStockService.STATUS_REVERT));
        Integer seq = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemByStatus(sqlParam);
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

        simpleTransaction.openTransaction();
        try {
            // 删除增量库存隔离表中的数据
            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
            sqlParam1.put("subTaskId", subTaskId);
            cmsBtStockSeparateIncrementItemDao.deleteStockSeparateIncrementItem(sqlParam1);

            // 删除增量库存隔离任务表中的数据
            Map<String, Object> sqlParam2 = new HashMap<String, Object>();
            sqlParam2.put("subTaskId", subTaskId);
            cmsBtStockSeparateIncrementTaskDao.deleteStockSeparateIncrementTask(sqlParam2);

        }catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

}