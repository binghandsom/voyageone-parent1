package com.voyageone.common.components.channelAdvisor.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "aPICredentials" })
public class Header {
	@XmlElement(name = "APICredentials") 
	private APICredentials aPICredentials;

	/**
	 * @return the aPICredentials
	 */
	public APICredentials getaPICredentials() {
		return aPICredentials;
	}

	/**
	 * @param aPICredentials the aPICredentials to set
	 */
	public void setaPICredentials(APICredentials aPICredentials) {
		this.aPICredentials = aPICredentials;
	}
}
