package com.voyageone.task2.cms.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.task2.cms.service.ImagePostScene7Service;
import com.voyageone.common.components.issueLog.IssueLog;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public class ImagePostScene7Job {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	ImagePostScene7Service imagePostScene7Service;
	
	@Autowired
	IssueLog issueLog;
	
	private String taskCheck = "ImagePostScene7";
	
	public boolean isRun = false;
	
	/**
	 * 
	 */
	public void run(String orderChannelId) {

		String taskNameReal = orderChannelId + ":" + taskCheck;
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskNameReal);
		// 是否可以运行的判断
		if (!TaskControlUtils.isRunnable(taskControlList, taskNameReal)) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(taskNameReal + "任务开始");
		logger.info("oderChannelId:" + orderChannelId);

		if (isRun) {
			logger.info(taskNameReal + " is running, continue");
			return;
		} else {
			isRun = true;
		}

		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		ExecutorService es = null;
		
		List<Future<String>> resultList = new ArrayList<>();
		
		try {
			// 获得该渠道要上传Scene7的图片url列表
			List<Map<String, String>> imageUrlList = imagePostScene7Service.getImageUrls(orderChannelId);
			
			if (imageUrlList != null && imageUrlList.size() > 0) {
				int count = 25;
				int totalSize = imageUrlList.size();
				int threadCount;
				int subSize;
				if (totalSize > count) {
					if (totalSize % count != 0) {
						threadCount = totalSize / count + 1;
					} else {
						threadCount = totalSize / count;
					}
					subSize = totalSize / threadCount;
					
				} else {
					subSize = totalSize;
					threadCount = 1;
				}
				
				es = Executors.newFixedThreadPool(threadCount);
				for (int i = 0; i < threadCount; i++) {
					int startIndex = i * subSize;
					
					int endIndex = startIndex + subSize;
					
					if (i == threadCount - 1) {
						endIndex = totalSize;
					}
					List<Map<String, String>> subImageUrlList = imageUrlList.subList(startIndex, endIndex);
					
					Future<String> future = es.submit(new ImageGetAndSendTask(orderChannelId, subImageUrlList, i + 1));
					resultList.add(future);
				}
				
			} else {
				logger.info(orderChannelId + "渠道本次没有要推送scene7的图片");
			}
			
		} catch (Exception ex) {
			isSuccess = false;
			
			logger.error(ex.getMessage(), ex);
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);
		}
		
		for (Future<String> fs : resultList) {
			try {
				//打印各个线程（任务）执行的结果
				logger.info(fs.get());
			} catch (InterruptedException | ExecutionException e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (es != null) {
					es.shutdown();
				}
			}
		}
		
		// 任务监控历史记录添加:结束
		String result;
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);
		
		isRun = false;

		logger.info(taskNameReal + "任务结束");
	}
	
	class ImageGetAndSendTask implements Callable<String> {
		
		/**
		 * 渠道标志
		 */
		private String orderChannelId;
		
		/**
		 * 待上传图片url列表
		 */
		private List<Map<String, String>> subImageUrlList;
		
		/**
		 * 线程号
		 */
		private int threadNo;
		
		/**
		 * 构造函数
		 */
		public ImageGetAndSendTask(String orderChannelId, List<Map<String, String>> subImageUrlList, int threadNo) {
			this.orderChannelId = orderChannelId;
			this.subImageUrlList = subImageUrlList;
			this.threadNo = threadNo;
		}
		
		@Override
		public String call() throws Exception {
			logger.info("thread-" + threadNo + " start");
			
			//  成功处理的图片url列表
			List<String> subSuccessImageUrlList = new ArrayList<>();
			List<Map<String, String>> urlErrorList = new ArrayList<>();
			boolean isSuccess = imagePostScene7Service.getAndSendImage(orderChannelId, subImageUrlList, subSuccessImageUrlList, urlErrorList, threadNo);
			
			if (isSuccess) {
				logger.info(orderChannelId + "渠道本次推送scene7图片任务thread-" + threadNo + "成功");
			} else {
				logger.info(orderChannelId + "渠道本次推送scene7图片任务thread-" + threadNo + "有错误发生");
			}
			
			// 已上传成功图片处理标志置位
			int returnValue = 0;
			if (subSuccessImageUrlList.size() > 0) {
				
				returnValue = imagePostScene7Service.updateImageSendFlag(orderChannelId, subSuccessImageUrlList, taskCheck);
			}

			if (urlErrorList.size() > 0) {

				imagePostScene7Service.deleteUrlErrorImage(orderChannelId, urlErrorList);
			}
			
			return "thread-" + threadNo + "上传scene7图片成功个数：" + subSuccessImageUrlList.size() +
					System.lineSeparator() + "已上传成功图片处理标志置位成功个数：" + returnValue +
					System.lineSeparator() + "图片URL错误个数：" + urlErrorList.size();
		}
	}
	
	public static void main(String[] args) {
	}
	
}

