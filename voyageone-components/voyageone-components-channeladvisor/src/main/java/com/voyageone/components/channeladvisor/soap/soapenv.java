package com.voyageone.components.channeladvisor.soap;

import com.voyageone.components.channeladvisor.body.Body;
import com.voyageone.components.channeladvisor.webservice.APICredentials;
import com.voyageone.components.channeladvisor.webservice.Header;
import com.voyageone.components.channeladvisor.webservice.SubmitOrder;

import javax.xml.bind.annotation.*;

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
