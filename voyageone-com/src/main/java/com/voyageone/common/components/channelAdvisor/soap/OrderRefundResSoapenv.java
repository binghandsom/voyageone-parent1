package com.voyageone.common.components.channelAdvisor.soap;//

import com.voyageone.common.components.channelAdvisor.webservices.InventoryResBody;
import com.voyageone.common.components.channelAdvisor.webservices.OrderRefundResBody;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class OrderRefundResSoapenv {

    @XmlElement(
            name = "Body"
    )
    private OrderRefundResBody body;

    public OrderRefundResSoapenv() {
    }

    public OrderRefundResBody getBody() {
        return this.body;
    }

    public void setBody(OrderRefundResBody body) {
        this.body = body;
    }
}
