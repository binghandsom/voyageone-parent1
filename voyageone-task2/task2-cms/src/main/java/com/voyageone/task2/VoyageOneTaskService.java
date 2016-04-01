/**
 *
 */
package com.voyageone.task2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author jacky.zhu
 */
public class VoyageOneTaskService {

    private static final Logger logger = LoggerFactory.getLogger(VoyageOneTaskService.class);

    private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";

    public static void main(String[] args) {
        VoyageOneTaskService service = new VoyageOneTaskService();
        try {
            logger.info(BATCH_JOB_NAME + "启动中......");
            service.startServer();
            logger.info(BATCH_JOB_NAME + "启动完成......");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void startServer() {
        // 上下文获得
        Context conext = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        conext.putAttribute("springContext", ctx);

        logger.info("startServer fininsh");
    }
}
