package com.voyageone.common.components.channelAdvisor.soap;

import com.voyageone.common.components.channelAdvisor.webservices.InventoryItemResBody;
import com.voyageone.common.components.channelAdvisor.webservices.InventoryResBody;

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
