
package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VariationInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VariationInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IsInRelationship" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RelationshipName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsParent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ParentSku" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VariationInfo", propOrder = {
    "isInRelationship",
    "relationshipName",
    "isParent",
    "parentSku"
})
public class VariationInfo {

    @XmlElement(name = "IsInRelationship")
    protected boolean isInRelationship;
    @XmlElement(name = "RelationshipName")
    protected String relationshipName;
    @XmlElement(name = "IsParent")
    protected boolean isParent;
    @XmlElement(name = "ParentSku")
    protected String parentSku;

    /**
     * Gets the value of the isInRelationship property.
     * 
     */
    public boolean isIsInRelationship() {
        return isInRelationship;
    }

    /**
     * Sets the value of the isInRelationship property.
     * 
     */
    public void setIsInRelationship(boolean value) {
        this.isInRelationship = value;
    }

    /**
     * Gets the value of the relationshipName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelationshipName() {
        return relationshipName;
    }

    /**
     * Sets the value of the relationshipName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelationshipName(String value) {
        this.relationshipName = value;
    }

    /**
     * Gets the value of the isParent property.
     * 
     */
    public boolean isIsParent() {
        return isParent;
    }

    /**
     * Sets the value of the isParent property.
     * 
     */
    public void setIsParent(boolean value) {
        this.isParent = value;
    }

    /**
     * Gets the value of the parentSku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentSku() {
        return parentSku;
    }

    /**
     * Sets the value of the parentSku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentSku(String value) {
        this.parentSku = value;
    }

}
