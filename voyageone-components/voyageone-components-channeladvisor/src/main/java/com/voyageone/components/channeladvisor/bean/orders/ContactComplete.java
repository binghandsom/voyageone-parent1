package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ContactComplete complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ContactComplete">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}AddressInfo">
 *       &lt;sequence>
 *         &lt;element name="CompanyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="JobTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Suffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneNumberDay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneNumberEvening" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactComplete", propOrder = { "companyName", "jobTitle", "title", "firstName", "lastName", "suffix",
		"phoneNumberDay", "phoneNumberEvening" })
public class ContactComplete extends AddressInfo {

	@XmlElement(name = "CompanyName")
	protected String companyName;
	@XmlElement(name = "JobTitle")
	protected String jobTitle;
	@XmlElement(name = "Title")
	protected String title;
	@XmlElement(name = "FirstName")
	protected String firstName;
	@XmlElement(name = "LastName")
	protected String lastName;
	@XmlElement(name = "Suffix")
	protected String suffix;
	@XmlElement(name = "PhoneNumberDay")
	protected String phoneNumberDay;
	@XmlElement(name = "PhoneNumberEvening")
	protected String phoneNumberEvening;

	/**
	 * Gets the value of the companyName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Sets the value of the companyName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCompanyName(String value) {
		this.companyName = value;
	}

	/**
	 * Gets the value of the jobTitle property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * Sets the value of the jobTitle property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setJobTitle(String value) {
		this.jobTitle = value;
	}

	/**
	 * Gets the value of the title property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Gets the value of the firstName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the value of the firstName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFirstName(String value) {
		this.firstName = value;
	}

	/**
	 * Gets the value of the lastName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the value of the lastName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLastName(String value) {
		this.lastName = value;
	}

	/**
	 * Gets the value of the suffix property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the value of the suffix property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSuffix(String value) {
		this.suffix = value;
	}

	/**
	 * Gets the value of the phoneNumberDay property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPhoneNumberDay() {
		return phoneNumberDay;
	}

	/**
	 * Sets the value of the phoneNumberDay property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPhoneNumberDay(String value) {
		this.phoneNumberDay = value;
	}

	/**
	 * Gets the value of the phoneNumberEvening property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPhoneNumberEvening() {
		return phoneNumberEvening;
	}

	/**
	 * Sets the value of the phoneNumberEvening property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPhoneNumberEvening(String value) {
		this.phoneNumberEvening = value;
	}

}
