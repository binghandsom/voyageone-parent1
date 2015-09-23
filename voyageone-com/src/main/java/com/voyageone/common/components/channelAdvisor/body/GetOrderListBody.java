package com.voyageone.common.components.channelAdvisor.body;

import com.voyageone.common.components.channelAdvisor.webservices.GetOrderList;
import com.voyageone.common.components.channelAdvisor.webservices.UpdateOrderList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getOrderList" })
public class GetOrderListBody {
	
	@XmlElement(name = "GetOrderList")
	private GetOrderList getOrderList;

	/**
	 * @return the updateOrderList
	 */
	public GetOrderList getUpdateOrderList() {
		return getOrderList;
	}

	/**
	 * @param getOrderList the updateOrderList to set
	 */
	public void setOrderListBody(GetOrderList getOrderList) {
		this.getOrderList = getOrderList;
	}


	
}
