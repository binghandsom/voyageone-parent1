package com.voyageone.components.channeladvisor.webservice;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.voyageone.components.channeladvisor.webservice.GetFilteredInventoryItemList;

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
    private com.voyageone.components.channeladvisor.webservice.GetFilteredInventoryItemList filteredInventoryItemList;

    public InventoryBody() {
    }

    public com.voyageone.components.channeladvisor.webservice.GetFilteredInventoryItemList getFilteredInventoryItemList() {
        return this.filteredInventoryItemList;
    }

    public void setSubmitParam(com.voyageone.components.channeladvisor.webservice.GetFilteredInventoryItemList filteredInventoryItemList) {
        this.filteredInventoryItemList = filteredInventoryItemList;
    }
}
