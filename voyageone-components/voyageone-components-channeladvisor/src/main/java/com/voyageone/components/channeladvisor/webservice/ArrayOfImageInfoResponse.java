
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.ImageInfoResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfImageInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfImageInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ImageInfoResponse" type="{http://api.channeladvisor.com/webservices/}ImageInfoResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfImageInfoResponse", propOrder = {
    "imageInfoResponse"
})
public class ArrayOfImageInfoResponse {

    @XmlElement(name = "ImageInfoResponse", nillable = true)
    protected List<com.voyageone.components.channeladvisor.webservice.ImageInfoResponse> imageInfoResponse;

    /**
     * Gets the value of the imageInfoResponse property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageInfoResponse property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageInfoResponse().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link com.voyageone.components.channeladvisor.webservice.ImageInfoResponse }
     *
     *
     */
    public List<com.voyageone.components.channeladvisor.webservice.ImageInfoResponse> getImageInfoResponse() {
        if (imageInfoResponse == null) {
            imageInfoResponse = new ArrayList<com.voyageone.components.channeladvisor.webservice.ImageInfoResponse>();
        }
        return this.imageInfoResponse;
    }

}
