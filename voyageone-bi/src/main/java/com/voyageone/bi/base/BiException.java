package com.voyageone.bi.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BiException extends Exception {	

	private static final long serialVersionUID = -3143170356122464015L;
	private static Log logger = LogFactory.getLog(BiException.class);

	private String methodName = "";
	
	public BiException(Exception e, String method)  
    {  
		super(e);
//		e.printStackTrace();
		logger.error("BiException", e);
		logger.error(method + " ERROR--" + e.getMessage() + "  " + e.getStackTrace());
        
    }
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
