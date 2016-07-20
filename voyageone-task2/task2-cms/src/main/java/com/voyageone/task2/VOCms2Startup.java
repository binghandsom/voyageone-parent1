/**
 *
 */
package com.voyageone.task2;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.task2.cms.job.AttributeTranslateJob;
import com.voyageone.task2.cms.job.CmsBuildMasterCategoryJob;
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
            log.info(NAME + " -> 启动中......");
//            Context context = Context.getContext();
            ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
//            context.putAttribute("springContext", ctx);
            AttributeTranslateJob job = ctx.getBean(AttributeTranslateJob.class);

            job.run();

            CmsBuildMasterCategoryJob job2 = ctx.getBean(CmsBuildMasterCategoryJob.class);
            job2.run();

            log.info(NAME + " -> 启动完成......");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }
}
