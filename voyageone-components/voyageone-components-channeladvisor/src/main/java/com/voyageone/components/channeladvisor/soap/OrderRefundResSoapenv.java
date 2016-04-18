package com.voyageone.components.channeladvisor.soap;//

import com.voyageone.components.channeladvisor.webservice.OrderRefundResBody;

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
