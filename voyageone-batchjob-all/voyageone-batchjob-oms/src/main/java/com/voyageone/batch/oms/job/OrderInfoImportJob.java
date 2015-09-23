package com.voyageone.batch.oms.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.OrderInfoImportService;

public class OrderInfoImportJob {
	
	private static Log logger = LogFactory.getLog(OrderInfoImportJob.class);

    @Autowired
    TaskDao taskDao;

	@Autowired
	OrderInfoImportService orderInfoImportService;

    private final static String taskCheck = "OrderInfoImportJob";

	public void importNewOrder() {

        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);

        // 是否可以运行的判断
        if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
            return;
        }

        String taskID =  TaskControlUtils.getTaskId(taskControlList);

        logger.info(taskCheck + "任务开始");

        //任务监控历史记录添加:启动
        taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

//        String result = orderInfoImportService.importOrders();
		System.out.println();

        //任务监控历史记录添加:结束
//        taskDao.insertTaskHistory(taskID, result);

        logger.info(taskCheck + "任务结束");

	}
}

