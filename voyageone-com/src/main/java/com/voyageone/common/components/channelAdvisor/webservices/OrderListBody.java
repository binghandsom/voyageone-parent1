package com.voyageone.common.components.channelAdvisor.webservices;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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
    private GetOrderList getOrderList;

    public OrderListBody() {
    }

    public GetOrderList getFilteredInventoryItemList() {
        return this.getOrderList;
    }

    public void setSubmitParam(GetOrderList getOrderList) {
        this.getOrderList = getOrderList;
    }
}
