package com.voyageone.common.components.channelAdvisor.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.voyageone.common.components.channelAdvisor.body.Body;
import com.voyageone.common.components.channelAdvisor.webservices.APICredentials;
import com.voyageone.common.components.channelAdvisor.webservices.Header;
import com.voyageone.common.components.channelAdvisor.webservices.SubmitOrder;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "header","body" })
@XmlRootElement(name = "Envelope")
public class soapenv {

	@XmlElement(name = "Header") 
	private Header header;
	@XmlElement(name = "Body") 
	private Body body;
	
	public soapenv(){
		
	}
	public soapenv(APICredentials api,SubmitOrder submitOrder){
		this.body=new Body();
		this.body.setSubmitOrder(submitOrder);
		this.header=new Header();
		this.header.setaPICredentials(api);
	}
	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(Header header) {
		this.header = header;
	}
	/**
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(Body body) {
		this.body = body;
	}
	
}
