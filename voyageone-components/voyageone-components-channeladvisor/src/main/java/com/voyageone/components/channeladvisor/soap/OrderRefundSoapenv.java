package com.voyageone.components.channeladvisor.soap;//

import com.voyageone.components.channeladvisor.body.OrderRefundBody;
import com.voyageone.components.channeladvisor.webservice.APICredentials;
import com.voyageone.components.channeladvisor.webservice.Header;
import com.voyageone.components.channeladvisor.webservice.SubmitOrderRefund;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"header", "body"}
)
@XmlRootElement(
        name = "Envelope"
)
public class OrderRefundSoapenv {
    @XmlElement(
            name = "Header"
    )
    private Header header;
    @XmlElement(
            name = "Body"
    )
    private OrderRefundBody body;

    public OrderRefundSoapenv() {
    }

    public OrderRefundSoapenv(APICredentials api, SubmitOrderRefund submitOrderRefund) {
        this.body = new OrderRefundBody();
        this.body.setSubmitOrderRefund(submitOrderRefund);
        this.header = new Header();
        this.header.setaPICredentials(api);
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public OrderRefundBody getBody() {
        return this.body;
    }

    public void setBody(OrderRefundBody body) {
        this.body = body;
    }
}
