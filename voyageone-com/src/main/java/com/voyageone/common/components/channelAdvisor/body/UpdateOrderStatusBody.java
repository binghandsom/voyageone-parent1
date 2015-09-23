package com.voyageone.common.components.channelAdvisor.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.voyageone.common.components.channelAdvisor.webservices.UpdateOrderList;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateOrderList" })
public class UpdateOrderStatusBody {
	
	@XmlElement(name = "UpdateOrderList") 
	private UpdateOrderList updateOrderList;

	/**
	 * @return the updateOrderList
	 */
	public UpdateOrderList getUpdateOrderList() {
		return updateOrderList;
	}

	/**
	 * @param updateOrderList the updateOrderList to set
	 */
	public void setUpdateOrderList(UpdateOrderList updateOrderList) {
		this.updateOrderList = updateOrderList;
	}


	
}
