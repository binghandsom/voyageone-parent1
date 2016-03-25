package com.voyageone.common.components.eExpress.base;

import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.beans.PostResponse;
import com.voyageone.common.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EtkBase {
	
	protected Log logger = LogFactory.getLog(getClass());
	
//	public final static Map map = new HashMap() {{
//	    put("key1", "value1");
//	    put("key2", "value2");
//	}};
	
	/*
	 * 保持Session调用E特快的API
	 */
	protected PostResponse reqETKFull(String postAction, String postData,CarrierBean carrierBean, String session) throws Exception {
		
		//生成SOAP用XML
		StringBuffer postXml = new StringBuffer();
		postXml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		postXml.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">");
		postXml.append("<soap:Body>");
		//拼接报文内容
		postXml.append(postData);
		
		postXml.append("</soap:Body>");
		postXml.append("</soap:Envelope>");
		
		//logger.info("E特快请求报文：" + postXml.toString());
		PostResponse result = HttpUtils.PostSoapFull(carrierBean.getApi_url(), postAction, postXml.toString(),session);
		//logger.info("E特快返回报文：" + result.getResult());
		
		return result;
	}

}
