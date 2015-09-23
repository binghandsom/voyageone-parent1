package com.voyageone.common.components.channelAdvisor.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SetOrdersExportStatusResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfSetExportStatusResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "setOrdersExportStatusResult" })
@XmlRootElement(name = "SetOrdersExportStatusResponse")
public class SetOrdersExportStatusResponse {

	@XmlElement(name = "SetOrdersExportStatusResult")
	protected APIResultOfArrayOfSetExportStatusResponse setOrdersExportStatusResult;

	/**
	 * Gets the value of the setOrdersExportStatusResult property.
	 * 
	 * @return possible object is
	 *         {@link APIResultOfArrayOfSetExportStatusResponse }
	 * 
	 */
	public APIResultOfArrayOfSetExportStatusResponse getSetOrdersExportStatusResult() {
		return setOrdersExportStatusResult;
	}

	/**
	 * Sets the value of the setOrdersExportStatusResult property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link APIResultOfArrayOfSetExportStatusResponse }
	 * 
	 */
	public void setSetOrdersExportStatusResult(APIResultOfArrayOfSetExportStatusResponse value) {
		this.setOrdersExportStatusResult = value;
	}

}
