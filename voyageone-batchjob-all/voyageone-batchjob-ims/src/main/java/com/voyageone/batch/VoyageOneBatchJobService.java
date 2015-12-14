package com.voyageone.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author jacky.zhu
 */
public class VoyageOneBatchJobService {

    private static final Log log = LogFactory.getLog(VoyageOneBatchJobService.class.getName());

    private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";

    public static void main(String[] args) {
        VoyageOneBatchJobService service = new VoyageOneBatchJobService();
        try {
            log.info(BATCH_JOB_NAME + "启动中......");
            service.startServer();
            log.info(BATCH_JOB_NAME + "启动完成......");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info(ex.getMessage());
        }
    }

    private void startServer() {
        // 上下文获得
        Context context = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        context.putAttribute("springContext", ctx);
    }
}
