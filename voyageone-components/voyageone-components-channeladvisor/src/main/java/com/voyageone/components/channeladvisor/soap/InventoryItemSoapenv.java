package com.voyageone.components.channeladvisor.soap;//

import com.voyageone.components.channeladvisor.webservice.APICredentials;
import com.voyageone.components.channeladvisor.webservice.GetInventoryItemList;
import com.voyageone.components.channeladvisor.webservice.InventoryHeader;
import com.voyageone.components.channeladvisor.webservice.InventoryItemBody;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"header", "body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class InventoryItemSoapenv {
    @XmlElement(
            name = "Header"
    )
    private InventoryHeader header;
    @XmlElement(
            name = "Body"
    )
    private InventoryItemBody body;

    public InventoryItemSoapenv() {
    }

    public InventoryItemSoapenv(APICredentials api, GetInventoryItemList inventoryRequest) {
        this.body = new InventoryItemBody();
        this.body.setSubmitParam(inventoryRequest);
        this.header = new InventoryHeader();
        this.header.setaPICredentials(api);
    }

    public InventoryHeader getHeader() {
        return this.header;
    }

    public void setHeader(InventoryHeader header) {
        this.header = header;
    }

    public InventoryItemBody getBody() {
        return this.body;
    }

    public void setBody(InventoryItemBody body) {
        this.body = body;
    }
}
