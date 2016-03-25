package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.*;
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