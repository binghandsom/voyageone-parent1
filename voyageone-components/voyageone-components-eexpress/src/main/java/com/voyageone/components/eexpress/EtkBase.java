package com.voyageone.components.eexpress;

import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.beans.PostResponse;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.components.ComponentBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EtkBase extends ComponentBase {
	
	/*
	 * 保持Session调用E特快的API
	 */
	protected PostResponse reqETKFull(String postAction, String postData,CarrierBean carrierBean, String session) throws Exception {
		
		//生成SOAP用XML
		StringBuilder postXml = new StringBuilder();
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
