
package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.ArrayOfImageThumbInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImageInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImageInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PlacementName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FolderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImageThumbList" type="{http://api.channeladvisor.com/webservices/}ArrayOfImageThumbInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageInfoResponse", propOrder = {
    "placementName",
    "folderName",
    "url",
    "imageThumbList"
})
public class ImageInfoResponse {

    @XmlElement(name = "PlacementName")
    protected String placementName;
    @XmlElement(name = "FolderName")
    protected String folderName;
    @XmlElement(name = "Url")
    protected String url;
    @XmlElement(name = "ImageThumbList")
    protected ArrayOfImageThumbInfo imageThumbList;

    /**
     * Gets the value of the placementName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlacementName() {
        return placementName;
    }

    /**
     * Sets the value of the placementName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlacementName(String value) {
        this.placementName = value;
    }

    /**
     * Gets the value of the folderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Sets the value of the folderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolderName(String value) {
        this.folderName = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the imageThumbList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfImageThumbInfo }
     *     
     */
    public ArrayOfImageThumbInfo getImageThumbList() {
        return imageThumbList;
    }

    /**
     * Sets the value of the imageThumbList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfImageThumbInfo }
     *     
     */
    public void setImageThumbList(ArrayOfImageThumbInfo value) {
        this.imageThumbList = value;
    }

}
