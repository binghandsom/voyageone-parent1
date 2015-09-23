package com.voyageone.batch.oms.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.oms.job.logic.PostOrderLogic;
import com.voyageone.batch.oms.service.UpdateOrderStatusService;
import com.voyageone.batch.oms.service.OrderCheckService;
import com.voyageone.batch.oms.service.PostOrderService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;

/**
 * 给ChannelAdvisor推送订单状态
 * @author James
 *
 */
public class UpdateOrderStatusJob {

	private static Log logger = LogFactory.getLog(UpdateOrderStatusJob.class);

	@Autowired
	TaskDao taskDao;

	@Autowired
	UpdateOrderStatusService approveOrderImportService;
	
	@Autowired
	IssueLog issueLog;
	
	private String taskCheck;

	private static boolean running = false;

    public void run() throws MessagingException {

        if (running) {
            logger.info(taskCheck + "正在运行，忽略");
            return;
        }
        running = true;

        logger.info(taskCheck + "任务开始");

        try {

        	approveOrderImportService.setTaskName(taskCheck);
        	approveOrderImportService.startup();

        }catch (Exception e) {
            logger.error(taskCheck + "任务发生异常：", e);
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
        }

        logger.info(taskCheck + "任务结束");
        running = false;

    }

	/**
	 * @param taskCheck
	 *            the taskCheck to set
	 */
	public void setTaskCheck(String taskCheck) {
		this.taskCheck = taskCheck;
	}
}
