/**
 *
 */
package com.voyageone.task2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author jeff.duan
 * @version 2.0.0
 */
public class VOVms2Startup {

    private static final Log log = LogFactory.getLog(VOVms2Startup.class);

    private static final String NAME = "VoyageOne Task2 -> VMS";

    public static void main(String[] args) {
        try {
            log.info(NAME + " -> 启动中......");
            Context context = Context.getContext();
            ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
            context.putAttribute("springContext", ctx);
            log.info(NAME + " -> 启动完成......");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }
}
