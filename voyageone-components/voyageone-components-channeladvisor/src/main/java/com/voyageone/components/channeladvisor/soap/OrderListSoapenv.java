package com.voyageone.components.channeladvisor.soap;

import com.voyageone.components.channeladvisor.webservice.APICredentials;
import com.voyageone.components.channeladvisor.webservice.GetOrderList;
import com.voyageone.components.channeladvisor.webservice.OrderListBody;
import com.voyageone.components.channeladvisor.webservice.OrderListHeader;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
		name = "",
		propOrder = {"header", "body"}
)
@XmlRootElement(
		name = "Envelope"
)
public class OrderListSoapenv {
	@XmlElement(
			name = "Header"
	)
	private OrderListHeader header;
	@XmlElement(
			name = "Body"
	)
	private OrderListBody body;

	public OrderListSoapenv() {
	}

	public OrderListSoapenv(APICredentials api, GetOrderList getOrderList) {
		this.body = new OrderListBody();
		this.body.setSubmitParam(getOrderList);
		this.header = new OrderListHeader();
		this.header.setaPICredentials(api);
	}

	public OrderListHeader getHeader() {
		return this.header;
	}

	public void setHeader(OrderListHeader header) {
		this.header = header;
	}

	public OrderListBody getBody() {
		return this.body;
	}

	public void setBody(OrderListBody body) {
		this.body = body;
	}
}
