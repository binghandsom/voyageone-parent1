package com.voyageone.components.channeladvisor.soap;

import com.voyageone.components.channeladvisor.body.UpdateOrderListResponseBody;

import javax.xml.bind.annotation.*;

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
