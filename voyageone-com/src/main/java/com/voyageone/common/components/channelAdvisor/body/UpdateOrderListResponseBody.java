package com.voyageone.common.components.channelAdvisor.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.voyageone.common.components.channelAdvisor.webservices.UpdateOrderListResponse;


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
