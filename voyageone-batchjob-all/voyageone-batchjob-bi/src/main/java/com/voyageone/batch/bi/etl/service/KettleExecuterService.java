package com.voyageone.batch.bi.etl.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.stereotype.Service;

@Service
public class KettleExecuterService {

	private static Log logger = LogFactory.getLog(KettleExecuterService.class);
	
	/**
	 * init
	 */
	public static  void init()  {
		logger.debug("KettleExecuterService init start");
		try {
			KettleEnvironment.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * transExecute
	 * @param fileName
	 * @param params
	 * @return
	 */
	public boolean transExecute(String fileName, Map<String, String> params) {
		Trans trans = null;
		try {
			TransMeta metaData = new TransMeta(fileName);
			trans = new Trans(metaData);
			Iterator<Entry<String, String>> it = params.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				trans.setVariable(entry.getKey(), entry.getValue());
			}
			trans.execute(null);
			trans.waitUntilFinished();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		boolean result = false;
		if (trans.getErrors() == 0) {
			result = true;
		}
		return result;
	}
	
	/**
	 * jobExecute
	 * @param fileName
	 * @param params
	 * @return
	 */
	public boolean jobExecute(String fileName, Map<String, String> params) {
		Job job = null;
		try {
			JobMeta jobMeta = new JobMeta(fileName, null);
			job = new Job(null, jobMeta);
			Iterator<Entry<String, String>> it = params.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				job.setVariable(entry.getKey(), entry.getValue());
			}
			job.start();
			job.waitUntilFinished();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		boolean result = false;
		if (job.getErrors() == 0) {
			result = true;
		}
		return result;
	}	
	
}
