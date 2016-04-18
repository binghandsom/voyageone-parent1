package com.voyageone.components.channeladvisor.webservice;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.voyageone.components.channeladvisor.bean.*;
import com.voyageone.components.channeladvisor.webservice.APICredentials;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"aPICredentials"}
)
public class InventoryHeader {
    @XmlElement(
            name = "APICredentials"
    )
    private com.voyageone.components.channeladvisor.webservice.APICredentials aPICredentials;

    public InventoryHeader() {
    }

    public com.voyageone.components.channeladvisor.webservice.APICredentials getaPICredentials() {
        return this.aPICredentials;
    }

    public void setaPICredentials(com.voyageone.components.channeladvisor.webservice.APICredentials aPICredentials) {
        this.aPICredentials = aPICredentials;
    }
}
