package com.voyageone.common.components.channelAdvisor.soap;

import com.voyageone.common.components.channelAdvisor.webservices.InventoryResBody;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        propOrder = {"body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class InventoryResSoapenv {

    @XmlElement(
            name = "Body"
    )
    private InventoryResBody body;

    public InventoryResSoapenv() {
    }

    public InventoryResBody getBody() {
        return this.body;
    }

    public void setBody(InventoryResBody body) {
        this.body = body;
    }
}
