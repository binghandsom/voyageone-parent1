package com.voyageone.common.components.channelAdvisor.webservices;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.voyageone.common.components.channelAdvisor.bean.*;

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
    private APICredentials aPICredentials;

    public InventoryHeader() {
    }

    public APICredentials getaPICredentials() {
        return this.aPICredentials;
    }

    public void setaPICredentials(APICredentials aPICredentials) {
        this.aPICredentials = aPICredentials;
    }
}
