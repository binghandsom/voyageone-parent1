package com.voyageone.components.channeladvisor.webservice;//

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.GetOrderListResponse;

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
    private com.voyageone.components.channeladvisor.webservice.GetOrderListResponse getOrderListResponse;

    public OrderListResBody() {
    }

    public com.voyageone.components.channeladvisor.webservice.GetOrderListResponse getOrderListResponse() {
        return this.getOrderListResponse;
    }

    public void setSubmitParam(GetOrderListResponse getOrderListResponse) {
        this.getOrderListResponse = getOrderListResponse;
    }
}
