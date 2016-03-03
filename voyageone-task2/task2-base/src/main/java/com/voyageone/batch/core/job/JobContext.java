package com.voyageone.batch.core.job;

import java.util.HashMap;

public class JobContext {

	private HashMap<String, Object> context = new HashMap<String, Object>();
	
	public static final String JOB_CONTEXT_TASK_NAME="JOB_CONTEXT_TASK_NAME";
	
	public void put(String key, Object value) {
		context.put(key, value);
	}
	
	public void remove(String key) {
		context.remove(key);
	}
	
	public void get(String key) {
		context.get(key);
	}
	
	public String getTaskName() {
		return (String) context.get(JOB_CONTEXT_TASK_NAME);
	}
}
