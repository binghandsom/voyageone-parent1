package com.voyageone.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.logging.Logger;

/**
 * @author jacky.zhu
 */
public class VoyageOneBatchJobService {

    private static final Logger log = Logger.getLogger(VoyageOneBatchJobService.class.getName());

    private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";

    public static void main(String[] args) {
        VoyageOneBatchJobService service = new VoyageOneBatchJobService();
        try {
            log.info(BATCH_JOB_NAME + "启动中......");
            service.startServer();
            log.info(BATCH_JOB_NAME + "启动完成......");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.severe(ex.getMessage());
        }
    }

    private void startServer() {
        // 上下文获得
        Context context = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        context.putAttribute("springContext", ctx);
    }
}
