package com.voyageone.components.channeladvisor.soap;

import com.voyageone.components.channeladvisor.body.UpdateOrderStatusBody;
import com.voyageone.components.channeladvisor.webservice.APICredentials;
import com.voyageone.components.channeladvisor.webservice.Header;
import com.voyageone.components.channeladvisor.webservice.UpdateOrderList;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "header","body" })
@XmlRootElement(name = "Envelope")
public class UpdateOrderStatusSoap {

	@XmlElement(name = "Header") 
	private Header header;
	@XmlElement(name = "Body") 
	private UpdateOrderStatusBody body;
	
	public UpdateOrderStatusSoap(){
		
	}
	public UpdateOrderStatusSoap(APICredentials api,UpdateOrderList updateOrderList){
		this.body=new UpdateOrderStatusBody();
		this.body.setUpdateOrderList(updateOrderList);
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
	public UpdateOrderStatusBody getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(UpdateOrderStatusBody body) {
		this.body = body;
	}
	
}
