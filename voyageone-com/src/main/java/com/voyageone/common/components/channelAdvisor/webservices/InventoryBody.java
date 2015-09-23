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
        propOrder = {"filteredInventoryItemList"}
)
public class InventoryBody {
    @XmlElement(
            name = "GetFilteredInventoryItemList"
    )
    private GetFilteredInventoryItemList filteredInventoryItemList;

    public InventoryBody() {
    }

    public GetFilteredInventoryItemList getFilteredInventoryItemList() {
        return this.filteredInventoryItemList;
    }

    public void setSubmitParam(GetFilteredInventoryItemList filteredInventoryItemList) {
        this.filteredInventoryItemList = filteredInventoryItemList;
    }
}
