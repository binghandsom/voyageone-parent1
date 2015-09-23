package com.voyageone.bi.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.bi.etl.service.KettleExecuterService;
import com.voyageone.batch.bi.util.FileUtils;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.commonutils.PropertiesUtils;
import com.voyageone.bi.task.TaskInit;

public class InitApplication {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	// 页面初期化
	@Autowired
	private TaskInit taskInit;	

	@Autowired
	ServletContext servletContext;

    public void init() {
	  
	    logger.info("APPLICATION init start");

		try {
			
			PropertiesUtils prop = new PropertiesUtils();		
			BiApplication.setMsgEnMap(prop.readConfigFile("conf/i18n/messages_en.properties"));
			logger.info("conf/i18n/messages_en.properties load success");
			BiApplication.setMsgCnMap(prop.readConfigFile("conf/i18n/messages_zh.properties"));
			logger.info("conf/i18n/messages_zh.properties load success");
			BiApplication.setKeyValueMap(prop.readConfigFile("keyvalue.properties"));
			logger.info("keyvalue.properties load success");
			
			// task init
			taskInit.init();
			String rootPath = servletContext.getRealPath("/WEB-INF/");
			if (rootPath != null && rootPath.length()>0) {
				if (rootPath.endsWith("/") || rootPath.endsWith("\\")) {
					rootPath = rootPath.substring(0, rootPath.length()-1);
				}
				FileUtils.setRootPath(rootPath);
			}
			KettleExecuterService.init();
		} catch (Exception ex) {
			logger.error(ex.getStackTrace(), ex);
		} finally{

	    }	
		logger.info("APPLICATION init end");
    }

    public void contextDestroyed(ServletContextEvent event) {		  
	}	  
}
