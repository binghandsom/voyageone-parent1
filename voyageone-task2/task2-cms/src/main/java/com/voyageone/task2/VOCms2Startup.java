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
 * @version 2.0.0
 */
public class VOCms2Startup {

    private static final Log log = LogFactory.getLog(VOCms2Startup.class);

    private static final String NAME = "VoyageOne Task2 -> CMS";

    public static void main(String[] args) {
        try {
            log.info(NAME + " -> 启动中111111111111......");
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
