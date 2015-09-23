package com.voyageone.common.components.channelAdvisor.webservices;//

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        propOrder = {"getOrderListResponse"}
)
public class OrderListResBody {
    @XmlElement(
            name = "GetOrderListResponse"
    )
    private GetOrderListResponse getOrderListResponse;

    public OrderListResBody() {
    }

    public GetOrderListResponse getOrderListResponse() {
        return this.getOrderListResponse;
    }

    public void setSubmitParam(GetOrderListResponse getOrderListResponse) {
        this.getOrderListResponse = getOrderListResponse;
    }
}
