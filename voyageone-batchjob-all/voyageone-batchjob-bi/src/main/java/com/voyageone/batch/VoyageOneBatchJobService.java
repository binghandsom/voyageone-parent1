/**
 * 
 */
package com.voyageone.batch;

import java.io.IOException;

import com.voyageone.batch.bi.spider.job.BiSpiderFireFoxDriverInitialJob;
import com.voyageone.batch.bi.spider.job.BiSpiderProductDataJob;
import com.voyageone.batch.bi.spider.job.BiSpiderViewProductDataJob;
import com.voyageone.batch.bi.spider.jumei.BiGlobalDealUpdateJob;
import com.voyageone.batch.bi.spider.jumei.BiGlobalDealUploadJob;
import com.voyageone.batch.bi.spider.jumei.BiGlobalProductUpdateJob;
import com.voyageone.batch.bi.spider.jumei.BiGlobalProductUploadJob;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.voyageone.batch.bi.util.FileUtils;


/**
 * @author jacky.zhu
 *
 */
public class VoyageOneBatchJobService {

	private static final Logger log = Logger.getLogger(VoyageOneBatchJobService.class);
	
	private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";
	
	public static void main(String[] args) {
//		判断同名实例是否已经启动  TODO
//		String pid = CommonUtils.getPid();
//		boolean isExistSameNameJobRunning = CommonUtils.isExistJavaBatchJobRunning4Linux(BATCH_JOB_NAME, pid);
//		if (isExistSameNameJobRunning) {
//			log.info(BATCH_JOB_NAME + "另一个实例已经启动，本次实例不启动！");
//		} else {
			VoyageOneBatchJobService service = new VoyageOneBatchJobService();
			try {
				log.info(BATCH_JOB_NAME + "启动中......");
				//log.info(System.getProperty("user.dir"));
				service.startServer();
				log.info(BATCH_JOB_NAME + "启动完成......");
			} catch (Exception ex) {
				//ex.printStackTrace();
				log.error("", ex);
			}
//		}
	}
	
	private void startServer() throws IOException {
		log.info("Start Root Path:" + FileUtils.getRootPath());
		// 数据库初始化
//		DBInitConfig.getInstance();
		
		// 上下文获得
		//Context conext = Context.getContext();
		//ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		String path = "/conf/applicationContext.xml";
		//@SuppressWarnings({ "resource", "unused" })
		ApplicationContext ctx = new FileSystemXmlApplicationContext(path);
	     
		//conext.putAttribute("springContext", ctx);
		
		// Params Init 
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("start_date", "2015-07-01");
//		// Kettle Init
//		KettleExecuterService.init();
//		// brand
//		CleanCacheService jobService = ctx.getBean(CleanCacheService.class);
//		jobService.setParams(params);
//		jobService.startup();

		// BiVtOrderSyncJob
//		BiVtOrderSyncJob job = ctx.getBean(BiVtOrderSyncJob.class);
//		job.run();

//		BiSpiderFireFoxDriverInitialJob jobDriver = ctx.getBean(BiSpiderFireFoxDriverInitialJob.class);
//		jobDriver.run();

//		BiSpiderShopDataJob jobShop = ctx.getBean(BiSpiderShopDataJob.class);
//		jobShop.run();

//		BiSpiderProductDataJob jobProduct = ctx.getBean(BiSpiderProductDataJob.class);
//		jobProduct.run();

//		BiSpiderViewProductDataJob jobViewProduct = ctx.getBean(BiSpiderViewProductDataJob.class);
//		jobViewProduct.run();
		
//		BiVtStockSyncJob biVtStockSyncJob =  ctx.getBean(BiVtStockSyncJob.class);
//		biVtStockSyncJob.run();
		
//		BiGlobalProductListJob jobDriver = ctx.getBean(BiGlobalProductListJob.class);
//		jobDriver.run();

//		BiGlobalDealUploadJob jobDriver = ctx.getBean(BiGlobalDealUploadJob.class);
//		jobDriver.run();

//		BiGlobalProductUploadJob jobDriver = ctx.getBean(BiGlobalProductUploadJob.class);
//		jobDriver.run();
		
//		BiGlobalProductUpdateJob jobDriver = ctx.getBean(BiGlobalProductUpdateJob.class);
//		jobDriver.run();
		
		BiGlobalDealUpdateJob jobDriver = ctx.getBean(BiGlobalDealUpdateJob.class);
		jobDriver.run();
	}

}
