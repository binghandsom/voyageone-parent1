package com.voyageone.components.channeladvisor.soap;

import com.voyageone.components.channeladvisor.webservice.InventoryItemResBody;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        propOrder = {"body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class InventoryItemResSoapenv {

    @XmlElement(
            name = "Body"
    )
    private InventoryItemResBody body;

    public InventoryItemResSoapenv() {
    }

    public InventoryItemResBody getBody() {
        return this.body;
    }

    public void setBody(InventoryItemResBody body) {
        this.body = body;
    }
}
