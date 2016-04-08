package com.voyageone.components.channeladvisor.webservice;//

import com.voyageone.components.channeladvisor.webservice.SubmitOrderRefundResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"submitOrderRefundResponse"}
)
public class OrderRefundResBody {
    @XmlElement(
            name = "SubmitOrderRefundResponse"
    )
    private SubmitOrderRefundResponse submitOrderRefundResponse;

    public OrderRefundResBody() {
    }

    public SubmitOrderRefundResponse getSubmitOrderRefundResponse() {
        return this.submitOrderRefundResponse;
    }

    public void setSubmitParam(SubmitOrderRefundResponse submitOrderRefundResponse) {
        this.submitOrderRefundResponse = submitOrderRefundResponse;
    }
}
