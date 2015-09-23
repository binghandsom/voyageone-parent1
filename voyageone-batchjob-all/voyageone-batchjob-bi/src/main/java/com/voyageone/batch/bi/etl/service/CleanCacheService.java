package com.voyageone.batch.bi.etl.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.voyageone.batch.configs.FileProperties;

@Service
public class CleanCacheService extends BaseBiKettleService {
	
	private static Log logger = LogFactory.getLog(CleanCacheService.class);
	
	private final static String TASK_NAME = "clearCache";
	private final static String KBJ_FILE_NAME = "clearCache";
	
	public static final String GET_URL = "http://localhost:8080/voyageone-bi/clearCache.html";
	
	public CleanCacheService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}
	
	@Override 
	public boolean execute() throws Exception {
		
		String getURL = GET_URL;
		String url = FileProperties.readValue("bi.clear.cache.path");
		if (url != null && !"".equals(url.trim())) {
			getURL = url;
		}
		logger.info("CleanCacheService request url:"+getURL); 
				
		URL getUrl = new URL(getURL); 
		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection(); 
		connection.connect(); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
       
		logger.info("CleanCacheService connect "); 

        String lines; 
        while ((lines = reader.readLine()) != null) { 
        	logger.info("CleanCacheService respones str:="+lines); 
        } 
        reader.close(); 
        connection.disconnect();
        return true;
	}

}
