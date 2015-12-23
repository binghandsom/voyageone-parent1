package com.voyageone.common.components.channelAdvisor.webservices;//

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
    private GetInventoryItemListResponse getInventoryItemListResponse;

    public InventoryItemResBody() {
    }

    public GetInventoryItemListResponse getInventoryItemListResponse() {
        return this.getInventoryItemListResponse;
    }

    public void setSubmitParam(GetInventoryItemListResponse getInventoryItemListResponse) {
        this.getInventoryItemListResponse = getInventoryItemListResponse;
    }
}
