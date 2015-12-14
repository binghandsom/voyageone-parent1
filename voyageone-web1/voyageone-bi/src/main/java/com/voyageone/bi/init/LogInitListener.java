package com.voyageone.bi.init;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class LogInitListener implements ServletContextListener  {
	
	protected final Log logger = LogFactory.getLog(getClass());

	private static final String LOG4J_CONFIG_FILE = "log4j.properties";	
	
	/** Our internal configuration object */
//	public static PropertiesConfiguration commonConfig = null;

    public void contextInitialized(ServletContextEvent event) {
	  
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream logIn = null;
		try {					
		    logIn = cl.getResourceAsStream(LOG4J_CONFIG_FILE);
			Properties p = new Properties();
			p.load(logIn);
			PropertyConfigurator.configure(p);
			logger.info("log4j init success------------" );				
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    public void contextDestroyed(ServletContextEvent event) {		  
	}	  
}
