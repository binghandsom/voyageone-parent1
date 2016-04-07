package com.voyageone.components.channeladvisor.body;

import com.voyageone.components.channeladvisor.webservice.SubmitOrder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "submitOrder" })
public class Body {
	
	@XmlElement(name = "SubmitOrder") 
	private SubmitOrder submitOrder;

	/**
	 * @return the submitOrder
	 */
	public SubmitOrder getSubmitOrder() {
		return submitOrder;
	}

	/**
	 * @param submitOrder the submitOrder to set
	 */
	public void setSubmitOrder(SubmitOrder submitOrder) {
		this.submitOrder = submitOrder;
	}
	
}
