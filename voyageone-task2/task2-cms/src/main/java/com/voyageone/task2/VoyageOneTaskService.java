/**
 *
 */
package com.voyageone.task2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author jacky.zhu
 */
public class VoyageOneTaskService {

    private static final Log log = LogFactory.getLog(VoyageOneTaskService.class);

    private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";

    public static void main(String[] args) {
        VoyageOneTaskService service = new VoyageOneTaskService();
        try {
            log.info(BATCH_JOB_NAME + "启动中......");
            service.startServer();
            log.info(BATCH_JOB_NAME + "启动完成......");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

    private void startServer() {
        // 上下文获得
        Context conext = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        conext.putAttribute("springContext", ctx);
    }
}
