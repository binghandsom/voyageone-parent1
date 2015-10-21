package com.voyageone.batch.core.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CoreConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.HistroryDataTransferDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.SimpleTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eric on 2015/10/13.
 */
@Service
public class HistoryDataTansferService extends BaseTaskService {
    @Autowired
    private HistroryDataTransferDao histroryDataTansferDao;

    @Autowired
    private SimpleTransaction simpleTransactionCom;

    private static final String tableAlias = " a";
    private static final String fieldAlias = "a.";

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.COM;
    }

    @Override
    public String getTaskName() {
        return "HistoryDataTransferJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        //获取Table的配置项
        List<TaskControlBean> cfgValue = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.history_table);


        try {
            for (TaskControlBean taskControlBean : cfgValue) {
                simpleTransactionCom.openTransaction();

                //获取表配置属性值
                String cfgValue1 = taskControlBean.getCfg_val1();
                String cfgValue2 = taskControlBean.getCfg_val2();
                String[] tableName = cfgValue1.split(",");

                //获取表名
                String historyTableFrom = tableName[0];
                String historyTableTo = tableName[1];

                //获取要查询的时间字段
                String dataField = tableName[2];

                //插入SQL封装条件
                Map<String, Object> insertdata = new HashMap<String, Object>();

                //封装时间SQL
                String insertDate = String.format(CoreConstants.TIME_SQL, fieldAlias + dataField, cfgValue2);
                insertdata.put("historyTableFrom", historyTableFrom + tableAlias);
                insertdata.put("historyTableTo", historyTableTo);
                insertdata.put("dataTime", insertDate);

                //删除SQL封装条件
                String deleteDate = String.format(CoreConstants.TIME_SQL, dataField, cfgValue2);
                Map<String, Object> deleteDataMap = new HashMap<String, Object>();
                deleteDataMap.put("historyTableFrom", historyTableFrom);
                deleteDataMap.put("dataTime", deleteDate);

                //执行插入操作
                logger.info("准备插入" + historyTableTo);
                int insertCount = histroryDataTansferDao.insertHistoryData(insertdata);
                logger.info("插入成功了" + insertCount + "条数据");

                //执行删除操作
                logger.info("准备删除" + historyTableFrom);
                int deleteCount = histroryDataTansferDao.deleteHistoryData(deleteDataMap);
                logger.info("删除成功了" + deleteCount + "条数据");

                //没有异常出现提交
                simpleTransactionCom.commit();


            }
        } catch (Exception e) {
            //出现异常回滚
            simpleTransactionCom.rollback();
           //抛出异常
            throw e;
        }

    }
}


