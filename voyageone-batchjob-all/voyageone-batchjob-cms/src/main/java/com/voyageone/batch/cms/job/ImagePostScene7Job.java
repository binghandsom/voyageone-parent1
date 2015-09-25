package com.voyageone.batch.cms.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.cms.service.ImagePostScene7Service;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public class ImagePostScene7Job {
	
	private static Log logger = LogFactory.getLog(ImagePostScene7Job.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	ImagePostScene7Service imagePostScene7Service;
	
	@Autowired
	IssueLog issueLog;
	
	private String taskCheck = "ImagePostScene7";
	
	public static boolean isRun = false;
	
	/**
	 * 
	 */
	public void run(String orderChannelId) {
		if (isRun) {
			logger.info("ImagePostScene7Job is running, continue");
			return;
		} else {
			isRun = true;
		}
		
		taskCheck = orderChannelId + ":" + taskCheck;
		
//		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);
//		// 是否可以运行的判断
//		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
//			return;
//		}
//		String taskID =  TaskControlUtils.getTaskId(taskControlList);
//		logger.info(taskCheck + "任务开始");
//		logger.info("oderChannelId:" + orderChannelId);
//		
//		// 任务监控历史记录添加:启动
//		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		ExecutorService es = null;
		
		List<Future<String>> resultList = new ArrayList<Future<String>>(); 
		
		try {
			// 获得该渠道要上传Scene7的图片url列表
			List<String> imageUrlList = imagePostScene7Service.getImageUrls(orderChannelId);
			
			if (imageUrlList != null && imageUrlList.size() > 0) {
				int count = 15;
				int totalSize = imageUrlList.size();
				int threadCount = 0;
				int subSize = 0;
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
					List<String> subImageUrlList = imageUrlList.subList(startIndex, endIndex);
					
					Future<String> future = es.submit(new ImageGetAndSendTask(orderChannelId, subImageUrlList, i + 1));
					resultList.add(future);
				}
				
			} else {
				logger.info(orderChannelId + "渠道本次没有要推送scene7的图片");
			}
			
		} catch (Exception ex) {
			isSuccess = false;
			
			logger.error(ex.getMessage(), ex);
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		for (Future<String> fs : resultList) {
			try {
				//打印各个线程（任务）执行的结果
				logger.info(fs.get());
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			} catch (ExecutionException e) {
				e.printStackTrace();
			} finally { 
				if (es != null) {
					es.shutdown();
				}
			}
		}
		
		// 任务监控历史记录添加:结束
//		String result = "";
//		if (isSuccess) {
//			result = TaskControlEnums.Status.SUCCESS.getIs();
//		} else {
//			result = TaskControlEnums.Status.ERROR.getIs();
//		}
//		taskDao.insertTaskHistory(taskID, result);
		
		isRun = false;

		logger.info(taskCheck + "任务结束");
	}
	
	class ImageGetAndSendTask implements Callable<String> {
		
		/**
		 * 渠道标志
		 */
		private String orderChannelId;
		
		/**
		 * 待上传图片url列表
		 */
		private List<String> subImageUrlList;
		
		/**
		 * 线程号
		 */
		private int threadNo;
		
		/**
		 * 构造函数
		 */
		public ImageGetAndSendTask(String orderChannelId, List<String> subImageUrlList, int threadNo) {
			this.orderChannelId = orderChannelId;
			this.subImageUrlList = subImageUrlList;
			this.threadNo = threadNo;
		}
		
		@Override
		public String call() throws Exception {
			logger.info("thread-" + threadNo + " start");
			
			//  成功处理的图片url列表
			List<String> subSuccessImageUrlList = new ArrayList<String>();
			boolean isSuccess = imagePostScene7Service.getAndSendImage(orderChannelId, subImageUrlList, subSuccessImageUrlList, threadNo);
			
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
			
			return "thread-" + threadNo + "上传scene7图片成功个数：" + subSuccessImageUrlList.size() + System.lineSeparator() + "已上传成功图片处理标志置位成功个数：" + returnValue;
		}
	}
	
	public static void main(String[] args) {
		int count = 20;
		int totalSize = 23;
		int threadCount = 5;
		int subSize = 0;
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
		
		for (int i = 0; i < threadCount; i++) {
			int startIndex = i * subSize;
			
			int endIndex = startIndex + subSize - 1;
			
			if (i == threadCount - 1) {
				endIndex = totalSize - 1;
			}
			System.out.println();
		}
	}
	
}

