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
        name = "",
        propOrder = {"inventoryItemList"}
)
public class InventoryItemBody {
    @XmlElement(
            name = "GetInventoryItemList"
    )
    private GetInventoryItemList inventoryItemList;

    public InventoryItemBody() {
    }

    public GetInventoryItemList getInventoryItemList() {
        return this.inventoryItemList;
    }

    public void setSubmitParam(GetInventoryItemList inventoryItemList) {
        this.inventoryItemList = inventoryItemList;
    }
}
