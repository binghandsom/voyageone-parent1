package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AddressInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AddressInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Region" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegionDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressInfo", propOrder = { "addressLine1", "addressLine2", "city", "region", "regionDescription",
		"postalCode", "countryCode" })
public class AddressInfo {

	@XmlElement(name = "AddressLine1")
	protected String addressLine1;
	@XmlElement(name = "AddressLine2")
	protected String addressLine2;
	@XmlElement(name = "City")
	protected String city;
	@XmlElement(name = "Region")
	protected String region;
	@XmlElement(name = "RegionDescription")
	protected String regionDescription;
	@XmlElement(name = "PostalCode")
	protected String postalCode;
	@XmlElement(name = "CountryCode")
	protected String countryCode;

	/**
	 * Gets the value of the addressLine1 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * Sets the value of the addressLine1 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAddressLine1(String value) {
		this.addressLine1 = value;
	}

	/**
	 * Gets the value of the addressLine2 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * Sets the value of the addressLine2 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAddressLine2(String value) {
		this.addressLine2 = value;
	}

	/**
	 * Gets the value of the city property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the value of the city property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCity(String value) {
		this.city = value;
	}

	/**
	 * Gets the value of the region property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the value of the region property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegion(String value) {
		this.region = value;
	}

	/**
	 * Gets the value of the regionDescription property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegionDescription() {
		return regionDescription;
	}

	/**
	 * Sets the value of the regionDescription property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegionDescription(String value) {
		this.regionDescription = value;
	}

	/**
	 * Gets the value of the postalCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Sets the value of the postalCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPostalCode(String value) {
		this.postalCode = value;
	}

	/**
	 * Gets the value of the countryCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Sets the value of the countryCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCountryCode(String value) {
		this.countryCode = value;
	}

}
