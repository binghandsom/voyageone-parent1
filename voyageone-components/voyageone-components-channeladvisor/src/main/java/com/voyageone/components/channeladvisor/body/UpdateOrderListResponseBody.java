package com.voyageone.components.channeladvisor.body;

import com.voyageone.components.channeladvisor.webservice.UpdateOrderListResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateOrderListResponse" })
public class UpdateOrderListResponseBody {
	
	@XmlElement(name = "UpdateOrderListResponse") 
	private UpdateOrderListResponse updateOrderListResponse;

	/**
	 * @return the updateOrderList
	 */
	public UpdateOrderListResponse getUpdateOrderListResponse() {
		return updateOrderListResponse;
	}

	/**
	 * @param updateOrderList the updateOrderList to set
	 */
	public void setUpdateOrderListResponse(UpdateOrderListResponse updateOrderListResponse) {
		this.updateOrderListResponse = updateOrderListResponse;
	}


	
}
