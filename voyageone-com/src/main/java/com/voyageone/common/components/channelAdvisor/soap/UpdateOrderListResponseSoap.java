package com.voyageone.common.components.channelAdvisor.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.voyageone.common.components.channelAdvisor.body.UpdateOrderListResponseBody;
import com.voyageone.common.components.channelAdvisor.body.UpdateOrderStatusBody;
import com.voyageone.common.components.channelAdvisor.webservices.APICredentials;
import com.voyageone.common.components.channelAdvisor.webservices.Header;
import com.voyageone.common.components.channelAdvisor.webservices.UpdateOrderList;
import com.voyageone.common.components.channelAdvisor.webservices.UpdateOrderListResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "body" })
@XmlRootElement(name = "Envelope")
public class UpdateOrderListResponseSoap {

	@XmlElement(name = "Body") 
	private UpdateOrderListResponseBody body;

	/**
	 * @return the body
	 */
	public UpdateOrderListResponseBody getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(UpdateOrderListResponseBody body) {
		this.body = body;
	}
	
}
