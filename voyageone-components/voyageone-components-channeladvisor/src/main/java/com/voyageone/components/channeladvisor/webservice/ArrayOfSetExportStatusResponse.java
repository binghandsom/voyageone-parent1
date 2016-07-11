package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Java class for ArrayOfSetExportStatusResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSetExportStatusResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SetExportStatusResponse" type="{http://api.channeladvisor.com/webservices/}SetExportStatusResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSetExportStatusResponse", propOrder = { "setExportStatusResponse" })
public class ArrayOfSetExportStatusResponse {

	@XmlElement(name = "SetExportStatusResponse", nillable = true)
	protected List<com.voyageone.components.channeladvisor.webservice.SetExportStatusResponse> setExportStatusResponse;

	/**
	 * Gets the value of the setExportStatusResponse property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the setExportStatusResponse property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getSetExportStatusResponse().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link com.voyageone.components.channeladvisor.webservice.SetExportStatusResponse }
	 *
	 *
	 */
	public List<com.voyageone.components.channeladvisor.webservice.SetExportStatusResponse> getSetExportStatusResponse() {
		if (setExportStatusResponse == null) {
			setExportStatusResponse = new ArrayList<SetExportStatusResponse>();
		}
		return this.setExportStatusResponse;
	}

}
