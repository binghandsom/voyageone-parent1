package com.voyageone.batch.oms.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.oms.job.logic.PostOrderLogic;
import com.voyageone.batch.oms.service.PostOrderService;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;

/**
 * 把订单信息推送给第三方（JC BHFO）
 * @author James
 *
 */
public class PostOrderToClientJob {

	private static Log logger = LogFactory.getLog(PostOrderToClientJob.class);

	@Autowired
	TaskDao taskDao;

	@Autowired
	PostOrderService postOrderService;

	private String taskCheck;

	private PostOrderLogic postOrderLogic;
	
	private ChannelConfigEnums.Channel channel;
	
	public void postOrderJob() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);

        // 是否可以运行的判断
        if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
            return;
        }

		String taskID = TaskControlUtils.getTaskId(taskControlList);
		logger.info(taskCheck + "任务开始");

		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		try {
			List<Map<String, Object>> dt = new ArrayList<Map<String,Object>>();
			if(channel == ChannelConfigEnums.Channel.JC){
				dt = postOrderService.getOrdersToOneShop(channel.getId());
			}else{
				dt = postOrderService.getOrdersToChanneladvisor(channel.getId());
			}
			if (dt.size() > 0) {
				postOrderLogic.init(taskCheck,channel.getId());
				for (Map<String, Object> rv : dt) {
					postOrderLogic.run(rv);
				}
			}
		} catch (Exception e) {
			logger.info(e.toString());
			// 任务监控历史记录添加:结束
			taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.ERROR.getIs());
			logger.info(taskCheck + "任务结束");
			return;
		}
		// 任务监控历史记录添加:结束
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.SUCCESS.getIs());
		logger.info(taskCheck + "任务结束");
	}

	/**
	 * @return the postOrderLogic
	 */
	public PostOrderLogic getPostOrderLogic() {
		return postOrderLogic;
	}

	/**
	 * @param postOrderLogic the postOrderLogic to set
	 */
	public void setPostOrderLogic(PostOrderLogic postOrderLogic) {
		this.postOrderLogic = postOrderLogic;
	}


	/**
	 * @return the channel
	 */
	public ChannelConfigEnums.Channel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(ChannelConfigEnums.Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the taskCheck
	 */
	public String getTaskCheck() {
		return taskCheck;
	}

	/**
	 * @param taskCheck the taskCheck to set
	 */
	public void setTaskCheck(String taskCheck) {
		this.taskCheck = taskCheck;
	}
	
	
}
