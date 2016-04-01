package com.voyageone.task2.base.job;

import java.util.List;

import javax.mail.MessagingException;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractJobExecuter extends VOAbsLoggable implements JobExecuter {
	
	protected boolean isRunning = false;
	
    @Autowired
    protected TaskDao taskDao;
	
	@Override
	public void execute() {
		$info("task start at:" + System.currentTimeMillis());
		
		String taskName = getTaskName();
		
		JobContext jobContext = new JobContext();
		jobContext.put(JobContext.JOB_CONTEXT_TASK_NAME, taskName);

		$info("preExecute for task:" + taskName + " start...");
		preExecute(jobContext);
		$info("preExecute for task:" + taskName + " end...");
		
		boolean canRun = checkRunable(jobContext);
		$info("taskName:" + taskName + ",canRun:" + canRun);
		if (!canRun) {
			return;
		}
		
		long start = System.currentTimeMillis();

		try {
			$info("doExecute for task:" + taskName + " start...");
			doExecute(jobContext);
			$info("doExecute for task:"+taskName+" end...");
		} catch (Exception e) {
			$error("execute task fail,taskName:" + taskName);
			$error(e);
		}
		
		long end = System.currentTimeMillis();
		$info("execute task use " + (end - start) + " milliseconds");

		$info("postExecute for task:" + taskName + " start...");
		postExecute(jobContext);
		$info("postExecute for task:" + taskName + " end...");

		$info("execute task end,taskName:" + taskName);
        isRunning = false;
        
	}

	private boolean checkRunable(JobContext jobContext) {
		String taskName = jobContext.getTaskName();
		
        //　取得相关任务的配置属性
        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskName);

        // 是否可以运行的判断
        if (!TaskControlUtils.isRunnable(taskControlList, taskName)) {
        	isRunning = Boolean.FALSE;
            return Boolean.FALSE;
        }
		
		
		return Boolean.TRUE;
	}

	/**
	 * Job前置逻辑,默认为空,可由之子类复写
	 * @param jobContext JobContext
	 */
	protected void preExecute(JobContext jobContext) {
		
	}
	
	
	/**
	 *	执行Job
	 * @param jobContext JobContext
	 * @throws MessagingException 
	 */
	protected abstract void doExecute(JobContext jobContext) throws Exception;
	
	/**
	 * Job后置逻辑,默认为空,可由之子类复写
	 * @param jobContext JobContext
	 */
	protected void postExecute(JobContext jobContext) {
		
	}

	/**
	 * 获取任务名称
	 * 
	 * @return String
	 */
	protected abstract String getTaskName();
	
	

}
