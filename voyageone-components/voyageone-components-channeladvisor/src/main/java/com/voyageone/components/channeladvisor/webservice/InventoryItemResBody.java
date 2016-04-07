package com.voyageone.components.channeladvisor.webservice;//

import com.voyageone.components.channeladvisor.webservice.GetInventoryItemListResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"getInventoryItemListResponse"}
)
public class InventoryItemResBody {
    @XmlElement(
            name = "GetInventoryItemListResponse"
    )
    private com.voyageone.components.channeladvisor.webservice.GetInventoryItemListResponse getInventoryItemListResponse;

    public InventoryItemResBody() {
    }

    public com.voyageone.components.channeladvisor.webservice.GetInventoryItemListResponse getInventoryItemListResponse() {
        return this.getInventoryItemListResponse;
    }

    public void setSubmitParam(com.voyageone.components.channeladvisor.webservice.GetInventoryItemListResponse getInventoryItemListResponse) {
        this.getInventoryItemListResponse = getInventoryItemListResponse;
    }
}
