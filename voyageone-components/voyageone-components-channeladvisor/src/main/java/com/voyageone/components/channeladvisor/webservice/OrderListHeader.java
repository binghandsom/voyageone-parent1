package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"aPICredentials"}
)
public class OrderListHeader {
    @XmlElement(
            name = "APICredentials"
    )
    private com.voyageone.components.channeladvisor.webservice.APICredentials aPICredentials;

    public OrderListHeader() {
    }

    public com.voyageone.components.channeladvisor.webservice.APICredentials getaPICredentials() {
        return this.aPICredentials;
    }

    public void setaPICredentials(com.voyageone.components.channeladvisor.webservice.APICredentials aPICredentials) {
        this.aPICredentials = aPICredentials;
    }
}
