package com.voyageone.components.channeladvisor.webservice;//

import com.voyageone.components.channeladvisor.webservice.GetFilteredInventoryItemListResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"getFilteredInventoryItemListResponse"}
)
public class InventoryResBody {
    @XmlElement(
            name = "GetFilteredInventoryItemListResponse"
    )
    private GetFilteredInventoryItemListResponse getFilteredInventoryItemListResponse;

    public InventoryResBody() {
    }

    public GetFilteredInventoryItemListResponse getFilteredInventoryItemListResponse() {
        return this.getFilteredInventoryItemListResponse;
    }

    public void setSubmitParam(GetFilteredInventoryItemListResponse getFilteredInventoryItemListResponse) {
        this.getFilteredInventoryItemListResponse = getFilteredInventoryItemListResponse;
    }
}
