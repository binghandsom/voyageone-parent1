
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.ImageThumbInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfImageThumbInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfImageThumbInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ImageThumbInfo" type="{http://api.channeladvisor.com/webservices/}ImageThumbInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfImageThumbInfo", propOrder = {
    "imageThumbInfo"
})
public class ArrayOfImageThumbInfo {

    @XmlElement(name = "ImageThumbInfo", nillable = true)
    protected List<ImageThumbInfo> imageThumbInfo;

    /**
     * Gets the value of the imageThumbInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageThumbInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageThumbInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImageThumbInfo }
     * 
     * 
     */
    public List<ImageThumbInfo> getImageThumbInfo() {
        if (imageThumbInfo == null) {
            imageThumbInfo = new ArrayList<ImageThumbInfo>();
        }
        return this.imageThumbInfo;
    }

}
