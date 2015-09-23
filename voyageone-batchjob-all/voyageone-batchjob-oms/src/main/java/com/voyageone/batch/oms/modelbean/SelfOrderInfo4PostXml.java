/**
 * 
 */
package com.voyageone.batch.oms.modelbean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author jacky
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Orders")
@XmlType(name = "order", propOrder = {"orders"})
public class SelfOrderInfo4PostXml {
	
	
	@XmlElement(name = "Order")
	private List<SelfOrderInfo4Post> orders;

	public List<SelfOrderInfo4Post> getOrders() {
		return orders;
	}

	public void setOrders(List<SelfOrderInfo4Post> orders) {
		this.orders = orders;
	}
	
}
