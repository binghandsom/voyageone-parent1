package com.voyageone.components.channeladvisor.body;

import com.voyageone.components.channeladvisor.webservice.GetOrderList;

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
