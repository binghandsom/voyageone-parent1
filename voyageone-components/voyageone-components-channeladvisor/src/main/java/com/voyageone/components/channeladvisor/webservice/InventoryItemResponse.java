package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for InventoryItemResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InventoryItemResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Sku" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Subtitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShortDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="SupplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WarehouseLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxProductCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlagStyle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlagDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsBlocked" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="BlockComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASIN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ISBN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UPC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MPN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Brand" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Condition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Warranty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProductMargin" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="SupplierPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HarmonizedCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Height" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Width" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="Classification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DistributionCenterList" type="{http://api.channeladvisor.com/webservices/}ArrayOfDistributionCenterInfoResponse" minOccurs="0"/>
 *         &lt;element name="Quantity" type="{http://api.channeladvisor.com/webservices/}QuantityInfoResponse" minOccurs="0"/>
 *         &lt;element name="PriceInfo" type="{http://api.channeladvisor.com/webservices/}PriceInfo" minOccurs="0"/>
 *         &lt;element name="AttributeList" type="{http://api.channeladvisor.com/webservices/}ArrayOfAttributeInfo" minOccurs="0"/>
 *         &lt;element name="VariationInfo" type="{http://api.channeladvisor.com/webservices/}VariationInfo" minOccurs="0"/>
 *         &lt;element name="StoreInfo" type="{http://api.channeladvisor.com/webservices/}StoreInfo" minOccurs="0"/>
 *         &lt;element name="ImageList" type="{http://api.channeladvisor.com/webservices/}ArrayOfImageInfoResponse" minOccurs="0"/>
 *         &lt;element name="MetaDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InventoryItemResponse", propOrder = {
    "sku",
    "title",
    "subtitle",
    "shortDescription",
    "description",
    "weight",
    "supplierCode",
    "warehouseLocation",
    "taxProductCode",
    "flagStyle",
    "flagDescription",
    "isBlocked",
    "blockComment",
    "asin",
    "isbn",
    "upc",
    "mpn",
    "ean",
    "manufacturer",
    "brand",
    "condition",
    "warranty",
    "productMargin",
    "supplierPO",
    "harmonizedCode",
    "height",
    "length",
    "width",
    "classification",
    "distributionCenterList",
    "quantity",
    "priceInfo",
    "attributeList",
    "variationInfo",
    "storeInfo",
    "imageList",
    "metaDescription"
})
public class InventoryItemResponse {

    @XmlElement(name = "Sku")
    protected String sku;
    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "Subtitle")
    protected String subtitle;
    @XmlElement(name = "ShortDescription")
    protected String shortDescription;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Weight", required = true, nillable = true)
    protected BigDecimal weight;
    @XmlElement(name = "SupplierCode")
    protected String supplierCode;
    @XmlElement(name = "WarehouseLocation")
    protected String warehouseLocation;
    @XmlElement(name = "TaxProductCode")
    protected String taxProductCode;
    @XmlElement(name = "FlagStyle")
    protected String flagStyle;
    @XmlElement(name = "FlagDescription")
    protected String flagDescription;
    @XmlElement(name = "IsBlocked", required = true, type = Boolean.class, nillable = true)
    protected Boolean isBlocked;
    @XmlElement(name = "BlockComment")
    protected String blockComment;
    @XmlElement(name = "ASIN")
    protected String asin;
    @XmlElement(name = "ISBN")
    protected String isbn;
    @XmlElement(name = "UPC")
    protected String upc;
    @XmlElement(name = "MPN")
    protected String mpn;
    @XmlElement(name = "EAN")
    protected String ean;
    @XmlElement(name = "Manufacturer")
    protected String manufacturer;
    @XmlElement(name = "Brand")
    protected String brand;
    @XmlElement(name = "Condition")
    protected String condition;
    @XmlElement(name = "Warranty")
    protected String warranty;
    @XmlElement(name = "ProductMargin", required = true, nillable = true)
    protected BigDecimal productMargin;
    @XmlElement(name = "SupplierPO")
    protected String supplierPO;
    @XmlElement(name = "HarmonizedCode")
    protected String harmonizedCode;
    @XmlElement(name = "Height", required = true, nillable = true)
    protected BigDecimal height;
    @XmlElement(name = "Length", required = true, nillable = true)
    protected BigDecimal length;
    @XmlElement(name = "Width", required = true, nillable = true)
    protected BigDecimal width;
    @XmlElement(name = "Classification")
    protected String classification;
    @XmlElement(name = "DistributionCenterList")
    protected ArrayOfDistributionCenterInfoResponse distributionCenterList;
    @XmlElement(name = "Quantity")
    protected QuantityInfoResponse quantity;
    @XmlElement(name = "PriceInfo")
    protected PriceInfo priceInfo;
    @XmlElement(name = "AttributeList")
    protected ArrayOfAttributeInfo attributeList;
    @XmlElement(name = "VariationInfo")
    protected VariationInfo variationInfo;
    @XmlElement(name = "StoreInfo")
    protected StoreInfo storeInfo;
    @XmlElement(name = "ImageList")
    protected ArrayOfImageInfoResponse imageList;
    @XmlElement(name = "MetaDescription")
    protected String metaDescription;

    /**
     * Gets the value of the sku property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSku() {
        return sku;
    }

    /**
     * Sets the value of the sku property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSku(String value) {
        this.sku = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the subtitle property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the value of the subtitle property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSubtitle(String value) {
        this.subtitle = value;
    }

    /**
     * Gets the value of the shortDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the weight property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setWeight(BigDecimal value) {
        this.weight = value;
    }

    /**
     * Gets the value of the supplierCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * Sets the value of the supplierCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSupplierCode(String value) {
        this.supplierCode = value;
    }

    /**
     * Gets the value of the warehouseLocation property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    /**
     * Sets the value of the warehouseLocation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWarehouseLocation(String value) {
        this.warehouseLocation = value;
    }

    /**
     * Gets the value of the taxProductCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTaxProductCode() {
        return taxProductCode;
    }

    /**
     * Sets the value of the taxProductCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTaxProductCode(String value) {
        this.taxProductCode = value;
    }

    /**
     * Gets the value of the flagStyle property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFlagStyle() {
        return flagStyle;
    }

    /**
     * Sets the value of the flagStyle property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFlagStyle(String value) {
        this.flagStyle = value;
    }

    /**
     * Gets the value of the flagDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFlagDescription() {
        return flagDescription;
    }

    /**
     * Sets the value of the flagDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFlagDescription(String value) {
        this.flagDescription = value;
    }

    /**
     * Gets the value of the isBlocked property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIsBlocked() {
        return isBlocked;
    }

    /**
     * Sets the value of the isBlocked property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIsBlocked(Boolean value) {
        this.isBlocked = value;
    }

    /**
     * Gets the value of the blockComment property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBlockComment() {
        return blockComment;
    }

    /**
     * Sets the value of the blockComment property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBlockComment(String value) {
        this.blockComment = value;
    }

    /**
     * Gets the value of the asin property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getASIN() {
        return asin;
    }

    /**
     * Sets the value of the asin property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setASIN(String value) {
        this.asin = value;
    }

    /**
     * Gets the value of the isbn property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getISBN() {
        return isbn;
    }

    /**
     * Sets the value of the isbn property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setISBN(String value) {
        this.isbn = value;
    }

    /**
     * Gets the value of the upc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUPC() {
        return upc;
    }

    /**
     * Sets the value of the upc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUPC(String value) {
        this.upc = value;
    }

    /**
     * Gets the value of the mpn property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMPN() {
        return mpn;
    }

    /**
     * Sets the value of the mpn property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMPN(String value) {
        this.mpn = value;
    }

    /**
     * Gets the value of the ean property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEAN() {
        return ean;
    }

    /**
     * Sets the value of the ean property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEAN(String value) {
        this.ean = value;
    }

    /**
     * Gets the value of the manufacturer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the brand property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the value of the brand property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBrand(String value) {
        this.brand = value;
    }

    /**
     * Gets the value of the condition property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCondition(String value) {
        this.condition = value;
    }

    /**
     * Gets the value of the warranty property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWarranty() {
        return warranty;
    }

    /**
     * Sets the value of the warranty property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWarranty(String value) {
        this.warranty = value;
    }

    /**
     * Gets the value of the productMargin property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getProductMargin() {
        return productMargin;
    }

    /**
     * Sets the value of the productMargin property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setProductMargin(BigDecimal value) {
        this.productMargin = value;
    }

    /**
     * Gets the value of the supplierPO property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSupplierPO() {
        return supplierPO;
    }

    /**
     * Sets the value of the supplierPO property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSupplierPO(String value) {
        this.supplierPO = value;
    }

    /**
     * Gets the value of the harmonizedCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHarmonizedCode() {
        return harmonizedCode;
    }

    /**
     * Sets the value of the harmonizedCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHarmonizedCode(String value) {
        this.harmonizedCode = value;
    }

    /**
     * Gets the value of the height property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setHeight(BigDecimal value) {
        this.height = value;
    }

    /**
     * Gets the value of the length property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setLength(BigDecimal value) {
        this.length = value;
    }

    /**
     * Gets the value of the width property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setWidth(BigDecimal value) {
        this.width = value;
    }

    /**
     * Gets the value of the classification property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClassification(String value) {
        this.classification = value;
    }

    /**
     * Gets the value of the distributionCenterList property.
     *
     * @return
     *     possible object is
     *     {@link ArrayOfDistributionCenterInfoResponse }
     *
     */
    public ArrayOfDistributionCenterInfoResponse getDistributionCenterList() {
        return distributionCenterList;
    }

    /**
     * Sets the value of the distributionCenterList property.
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfDistributionCenterInfoResponse }
     *
     */
    public void setDistributionCenterList(ArrayOfDistributionCenterInfoResponse value) {
        this.distributionCenterList = value;
    }

    /**
     * Gets the value of the quantity property.
     *
     * @return
     *     possible object is
     *     {@link QuantityInfoResponse }
     *
     */
    public QuantityInfoResponse getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityInfoResponse }
     *
     */
    public void setQuantity(QuantityInfoResponse value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the priceInfo property.
     *
     * @return
     *     possible object is
     *     {@link PriceInfo }
     *
     */
    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    /**
     * Sets the value of the priceInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link PriceInfo }
     *
     */
    public void setPriceInfo(PriceInfo value) {
        this.priceInfo = value;
    }

    /**
     * Gets the value of the attributeList property.
     *
     * @return
     *     possible object is
     *     {@link ArrayOfAttributeInfo }
     *
     */
    public ArrayOfAttributeInfo getAttributeList() {
        return attributeList;
    }

    /**
     * Sets the value of the attributeList property.
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttributeInfo }
     *
     */
    public void setAttributeList(ArrayOfAttributeInfo value) {
        this.attributeList = value;
    }

    /**
     * Gets the value of the variationInfo property.
     *
     * @return
     *     possible object is
     *     {@link VariationInfo }
     *
     */
    public VariationInfo getVariationInfo() {
        return variationInfo;
    }

    /**
     * Sets the value of the variationInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link VariationInfo }
     *
     */
    public void setVariationInfo(VariationInfo value) {
        this.variationInfo = value;
    }

    /**
     * Gets the value of the storeInfo property.
     *
     * @return
     *     possible object is
     *     {@link StoreInfo }
     *
     */
    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    /**
     * Sets the value of the storeInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link StoreInfo }
     *     
     */
    public void setStoreInfo(StoreInfo value) {
        this.storeInfo = value;
    }

    /**
     * Gets the value of the imageList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfImageInfoResponse }
     *     
     */
    public ArrayOfImageInfoResponse getImageList() {
        return imageList;
    }

    /**
     * Sets the value of the imageList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfImageInfoResponse }
     *     
     */
    public void setImageList(ArrayOfImageInfoResponse value) {
        this.imageList = value;
    }

    /**
     * Gets the value of the metaDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetaDescription() {
        return metaDescription;
    }

    /**
     * Sets the value of the metaDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetaDescription(String value) {
        this.metaDescription = value;
    }

}
