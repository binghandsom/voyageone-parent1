package com.voyageone.components.channeladvisor.soap;

import com.voyageone.components.channeladvisor.webservice.OrderListResBody;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        propOrder = {"body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class OrderListResSoapenv {

    @XmlElement(
            name = "Body"
    )
    private OrderListResBody body;

    public OrderListResSoapenv() {
    }

    public OrderListResBody getBody() {
        return this.body;
    }

    public void setBody(OrderListResBody body) {
        this.body = body;
    }
}
