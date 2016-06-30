package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        propOrder = {"getOrderList"}
)
public class OrderListBody {
    @XmlElement(
            name = "GetOrderList"
    )
    private com.voyageone.components.channeladvisor.webservice.GetOrderList getOrderList;

    public OrderListBody() {
    }

    public com.voyageone.components.channeladvisor.webservice.GetOrderList getFilteredInventoryItemList() {
        return this.getOrderList;
    }

    public void setSubmitParam(GetOrderList getOrderList) {
        this.getOrderList = getOrderList;
    }
}
