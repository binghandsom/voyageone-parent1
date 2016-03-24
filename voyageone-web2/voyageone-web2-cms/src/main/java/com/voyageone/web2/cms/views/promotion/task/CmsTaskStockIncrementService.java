package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

;

/**
 * Created by jeff.duan on 2016/03/04.
 */
@Service
public class CmsTaskStockIncrementService extends BaseAppService {

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
    private CmsTaskStockIncrementDetailService cmsTaskStockIncrementDetailService;

    @Autowired
    private SimpleTransaction simpleTransaction;

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
        sqlParam.put("statusList", Arrays.asList( STATUS_WAITING_INCREMENT,
                                                    STATUS_INCREASING,
                                                    STATUS_INCREMENT_SUCCESS,
                                                    STATUS_INCREMENT_FAIL,
                                                    STATUS_REVERT));
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