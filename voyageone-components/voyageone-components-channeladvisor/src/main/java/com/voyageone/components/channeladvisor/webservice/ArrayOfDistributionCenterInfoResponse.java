package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfDistributionCenterInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfDistributionCenterInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DistributionCenterInfoResponse" type="{http://api.channeladvisor.com/webservices/}DistributionCenterInfoResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDistributionCenterInfoResponse", propOrder = {
    "distributionCenterInfoResponse"
})
public class ArrayOfDistributionCenterInfoResponse {

    @XmlElement(name = "DistributionCenterInfoResponse", nillable = true)
    protected List<com.voyageone.components.channeladvisor.webservice.DistributionCenterInfoResponse> distributionCenterInfoResponse;

    /**
     * Gets the value of the distributionCenterInfoResponse property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the distributionCenterInfoResponse property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDistributionCenterInfoResponse().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link com.voyageone.components.channeladvisor.webservice.DistributionCenterInfoResponse }
     *
     *
     */
    public List<com.voyageone.components.channeladvisor.webservice.DistributionCenterInfoResponse> getDistributionCenterInfoResponse() {
        if (distributionCenterInfoResponse == null) {
            distributionCenterInfoResponse = new ArrayList<DistributionCenterInfoResponse>();
        }
        return this.distributionCenterInfoResponse;
    }

}
