package com.voyageone.batch.oms.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "firstName", "lastName","creditCardNumber","expMonth","expYear","cvv","lastFourDigits"})
public class PaymentBean {
	private String firstName = "VoyageOne";
	private String lastName = "VoyageOne";
	private String creditCardNumber = "CREDIT_CARD_NUMBER";
	private String expMonth = "01";
	private String expYear = "2099";
	private String cvv = "CVV2";
	private String lastFourDigits = "9999";
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the creditCardNumber
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	/**
	 * @param creditCardNumber the creditCardNumber to set
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	/**
	 * @return the expMonth
	 */
	public String getExpMonth() {
		return expMonth;
	}
	/**
	 * @param expMonth the expMonth to set
	 */
	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}
	/**
	 * @return the expYear
	 */
	public String getExpYear() {
		return expYear;
	}
	/**
	 * @param expYear the expYear to set
	 */
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
	/**
	 * @return the cvv
	 */
	public String getCvv() {
		return cvv;
	}
	/**
	 * @param cvv the cvv to set
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	/**
	 * @return the lastFourDigits
	 */
	public String getLastFourDigits() {
		return lastFourDigits;
	}
	/**
	 * @param lastFourDigits the lastFourDigits to set
	 */
	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

}
