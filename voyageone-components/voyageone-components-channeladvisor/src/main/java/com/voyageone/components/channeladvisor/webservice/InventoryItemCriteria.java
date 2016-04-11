
package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for InventoryItemCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InventoryItemCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DateRangeField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DateRangeStartGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DateRangeEndGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="PartialSku" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SkuStartsWith" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SkuEndsWith" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClassificationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LabelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QuantityCheckField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QuantityCheckType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QuantityCheckValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InventoryItemCriteria", propOrder = {
    "dateRangeField",
    "dateRangeStartGMT",
    "dateRangeEndGMT",
    "partialSku",
    "skuStartsWith",
    "skuEndsWith",
    "classificationName",
    "labelName",
    "quantityCheckField",
    "quantityCheckType",
    "quantityCheckValue",
    "pageNumber",
    "pageSize"
})
public class InventoryItemCriteria {

    @XmlElement(name = "DateRangeField")
    protected String dateRangeField;
    @XmlElement(name = "DateRangeStartGMT", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateRangeStartGMT;
    @XmlElement(name = "DateRangeEndGMT", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateRangeEndGMT;
    @XmlElement(name = "PartialSku")
    protected String partialSku;
    @XmlElement(name = "SkuStartsWith")
    protected String skuStartsWith;
    @XmlElement(name = "SkuEndsWith")
    protected String skuEndsWith;
    @XmlElement(name = "ClassificationName")
    protected String classificationName;
    @XmlElement(name = "LabelName")
    protected String labelName;
    @XmlElement(name = "QuantityCheckField")
    protected String quantityCheckField;
    @XmlElement(name = "QuantityCheckType")
    protected String quantityCheckType;
    @XmlElement(name = "QuantityCheckValue", required = true, type = Integer.class, nillable = true)
    protected Integer quantityCheckValue;
    @XmlElement(name = "PageNumber")
    protected int pageNumber;
    @XmlElement(name = "PageSize")
    protected int pageSize;

    /**
     * Gets the value of the dateRangeField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateRangeField() {
        return dateRangeField;
    }

    /**
     * Sets the value of the dateRangeField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRangeField(String value) {
        this.dateRangeField = value;
    }

    /**
     * Gets the value of the dateRangeStartGMT property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateRangeStartGMT() {
        return dateRangeStartGMT;
    }

    /**
     * Sets the value of the dateRangeStartGMT property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateRangeStartGMT(XMLGregorianCalendar value) {
        this.dateRangeStartGMT = value;
    }

    /**
     * Gets the value of the dateRangeEndGMT property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateRangeEndGMT() {
        return dateRangeEndGMT;
    }

    /**
     * Sets the value of the dateRangeEndGMT property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateRangeEndGMT(XMLGregorianCalendar value) {
        this.dateRangeEndGMT = value;
    }

    /**
     * Gets the value of the partialSku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartialSku() {
        return partialSku;
    }

    /**
     * Sets the value of the partialSku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartialSku(String value) {
        this.partialSku = value;
    }

    /**
     * Gets the value of the skuStartsWith property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkuStartsWith() {
        return skuStartsWith;
    }

    /**
     * Sets the value of the skuStartsWith property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkuStartsWith(String value) {
        this.skuStartsWith = value;
    }

    /**
     * Gets the value of the skuEndsWith property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkuEndsWith() {
        return skuEndsWith;
    }

    /**
     * Sets the value of the skuEndsWith property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkuEndsWith(String value) {
        this.skuEndsWith = value;
    }

    /**
     * Gets the value of the classificationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassificationName() {
        return classificationName;
    }

    /**
     * Sets the value of the classificationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassificationName(String value) {
        this.classificationName = value;
    }

    /**
     * Gets the value of the labelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * Sets the value of the labelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelName(String value) {
        this.labelName = value;
    }

    /**
     * Gets the value of the quantityCheckField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantityCheckField() {
        return quantityCheckField;
    }

    /**
     * Sets the value of the quantityCheckField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantityCheckField(String value) {
        this.quantityCheckField = value;
    }

    /**
     * Gets the value of the quantityCheckType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantityCheckType() {
        return quantityCheckType;
    }

    /**
     * Sets the value of the quantityCheckType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantityCheckType(String value) {
        this.quantityCheckType = value;
    }

    /**
     * Gets the value of the quantityCheckValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQuantityCheckValue() {
        return quantityCheckValue;
    }

    /**
     * Sets the value of the quantityCheckValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQuantityCheckValue(Integer value) {
        this.quantityCheckValue = value;
    }

    /**
     * Gets the value of the pageNumber property.
     * 
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the value of the pageNumber property.
     * 
     */
    public void setPageNumber(int value) {
        this.pageNumber = value;
    }

    /**
     * Gets the value of the pageSize property.
     * 
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

}
