package com.voyageone.batch.oms.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.api.domain.Trade;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.OrderCheckService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbOrderService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;


/**
 * 检查漏单的定时任务
 * @author James
 *
 */
public class CheckOmitSecResultJob {

	private static Log logger = LogFactory.getLog(CheckOmitSecResultJob.class);

	@Autowired
	TaskDao taskDao;

	@Autowired
	OrderCheckService orderCheckService;
	
	@Autowired
	IssueLog issueLog;
	
	private String taskCheck;

	private String channelId;
	
	private static boolean running = false;

    public void run() throws MessagingException {

        if (running) {
            logger.info(taskCheck + "正在运行，忽略");
            return;
        }
        running = true;

        logger.info(taskCheck + "任务开始");

        try {

        	orderCheckService.setTaskName(taskCheck);
        	orderCheckService.setChannelId(channelId);
        	orderCheckService.startup();

        }catch (Exception e) {
            logger.error(taskCheck + "任务发生异常：", e);
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
        }

        logger.info(taskCheck + "任务结束");
        running = false;

    }


	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	/**
	 * @param taskCheck
	 *            the taskCheck to set
	 */
	public void setTaskCheck(String taskCheck) {
		this.taskCheck = taskCheck;
	}

}
