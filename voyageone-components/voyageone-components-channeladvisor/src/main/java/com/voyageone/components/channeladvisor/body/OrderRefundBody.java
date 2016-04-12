package com.voyageone.components.channeladvisor.body;

import com.voyageone.components.channeladvisor.webservice.SubmitOrderRefund;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "submitOrderRefund" })
public class OrderRefundBody {
	
	@XmlElement(name = "SubmitOrderRefund")
	private SubmitOrderRefund submitOrderRefund;

	/**
	 * @return the submitOrderRefund
	 */
	public SubmitOrderRefund getSubmitOrderRefund() {
		return submitOrderRefund;
	}

	/**
	 * @param submitOrderRefund the submitOrderRefund to set
	 */
	public void setSubmitOrderRefund(SubmitOrderRefund submitOrderRefund) {
		this.submitOrderRefund = submitOrderRefund;
	}
	
}
