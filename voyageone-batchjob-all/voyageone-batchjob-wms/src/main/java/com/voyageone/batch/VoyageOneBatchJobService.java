/**
 * 
 */
package com.voyageone.batch;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


/**
 * @author jacky.zhu
 *
 */
public class VoyageOneBatchJobService {

	private static final Logger log = Logger.getLogger(VoyageOneBatchJobService.class);
	
	private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";
	
	public static void main(String[] args) {
//		// 判断同名实例是否已经启动  TODO
//		String pid = CommonUtils.getPid();
//		boolean isExistSameNameJobRunning = CommonUtils.isExistJavaBatchJobRunning4Linux(BATCH_JOB_NAME, pid);
//		if (isExistSameNameJobRunning) {
//			log.info(BATCH_JOB_NAME + "另一个实例已经启动，本次实例不启动！");
//		} else {
			VoyageOneBatchJobService service = new VoyageOneBatchJobService();
			try {
				log.info(BATCH_JOB_NAME + "启动中......");
				service.startServer();
				log.info(BATCH_JOB_NAME + "启动完成......");
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex.getMessage());
			}
//		}
	}
	
	private void startServer() {
		// 数据库初始化
//		DBInitConfig.getInstance();

		// 上下文获得
		Context conext = Context.getContext();
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		conext.putAttribute("springContext", ctx);
		
		// 库存同步服务类初始化
//		SynInventoryFromUsaService.getInstance();
	}
	


}
