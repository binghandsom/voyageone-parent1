package com.voyageone.common.components.channelAdvisor.soap;//
import com.voyageone.common.components.channelAdvisor.webservices.APICredentials;
import com.voyageone.common.components.channelAdvisor.webservices.GetFilteredInventoryItemList;
import com.voyageone.common.components.channelAdvisor.webservices.InventoryBody;
import com.voyageone.common.components.channelAdvisor.webservices.InventoryHeader;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"header", "body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class InventorySoapenv {
    @XmlElement(
            name = "Header"
    )
    private InventoryHeader header;
    @XmlElement(
            name = "Body"
    )
    private InventoryBody body;

    public InventorySoapenv() {
    }

    public InventorySoapenv(APICredentials api, GetFilteredInventoryItemList inventoryRequest) {
        this.body = new InventoryBody();
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

    public InventoryBody getBody() {
        return this.body;
    }

    public void setBody(InventoryBody body) {
        this.body = body;
    }
}
