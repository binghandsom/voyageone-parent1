package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementTaskDao;
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
        String incrementTaskID = (String) param.get("incrementTaskID");
        //画面入力信息的CHECK
        checkIncrementInfo(param,incrementTaskID);

        if(promotionType.equals(TYPE_INCREMENT_INSERT)){
            //根据增量任务id，将增量任务信息插入到cms_bt_stock_separate_increment_task表
            insertIncrementByTaskID(param,incrementTaskID);
            //根据增量任务id，抽出cms_bt_stock_separate_item表中的所有sku后，计算出各sku的可用库存数并且根据增量任务的设定。
            // 计算出增量库存隔离数，插入到cms_bt_stock_separate_increment_item表中。（固定值隔离标志位=0：按动态值进行增量隔离）
            insertStockSeparateByTaskID(param,incrementTaskID);
        }
        //判断增量任务:更新场合
        if(promotionType.equals(TYPE_INCREMENT_UPDATE)){
            //如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表
            updateIncrementByTaskID(param,incrementTaskID);
        }
    }

    /**
     * 画面入力信息的CHECK
     * @param param
     */
    private void checkIncrementInfo(Map param,String incrementTaskID) {
        //增量类型:增量数量
        String incrementCount = (String) param.get("incrementCount");
        //增量类型:增量百分比
        String incrementPercent = (String) param.get("incrementPercent");
        //任务名称
        if (StringUtils.isEmpty(incrementTaskID)||incrementTaskID.getBytes().length>=1000) {
            throw new BusinessException("必须输入且长度小于1000！");
        }
        //数量增量
        if (StringUtils.isEmpty(incrementCount) || !StringUtils.isDigit(incrementCount)
                ||incrementCount.getBytes().length>1||Long.parseLong(incrementCount)>0) {
            throw new BusinessException("增量必须为大于0的整数！");
        }
        //百分比增量
        if(incrementPercent.equals("%")){
            throw new BusinessException("增量比例必须填且为大于0小于100整数！");
        }else{
            String[] IncrementValue = incrementPercent.split("%");
            if (StringUtils.isEmpty(IncrementValue[0])|| !StringUtils.isDigit(IncrementValue[0])
                    ||IncrementValue[0].getBytes().length>2||IncrementValue.length>1||Long.parseLong(IncrementValue[0])>0) {
                throw new BusinessException("增量比例必须填且为大于0小于100整数！");
            }
        }
    }

    /**
     *
     * @param param
     */
    private void insertStockSeparateByTaskID(Map param,String incrementTaskID) {

    }

    /**
     * 如果是新建增量任务时（参数.增量任务id没有内容），将增量任务信息插入到cms_bt_stock_separate_increment_task表
     * @param param
     */
    private void insertIncrementByTaskID(Map param,String incrementTaskID) {

    }

    /**
     * 如果是修改增量任务时（参数.增量任务id有内容），只能修改任务名，将任务名更新到cms_bt_stock_separate_increment_task表
     * @param param
     */
    private void updateIncrementByTaskID(Map param,String incrementTaskID) {

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
            throw new BusinessException("已经开始增量库存隔离，不能删除增量任务！");
        }

        // 子任务id对应的增量库存隔离数据是否移到history表
        boolean historyFlg = cmsTaskStockIncrementDetailService.isHistoryExist(subTaskId);
        if (historyFlg) {
            throw new BusinessException("已经开始增量库存隔离，不能删除增量任务！");
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