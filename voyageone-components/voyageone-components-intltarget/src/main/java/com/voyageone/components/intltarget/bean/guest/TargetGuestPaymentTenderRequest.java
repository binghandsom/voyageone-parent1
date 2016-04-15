package com.voyageone.components.intltarget.bean.guest;

import com.voyageone.components.intltarget.enums.TargetGuestPaymentType;

import java.util.List;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestPaymentTenderRequest {

    private TargetGuestPaymentType type;

    private String firstName;

    private String lastName;

    private String middleName;

    private List<String> addressLine;

    private String city;

    private String state;

    private String phone;

    private String phoneType;

    private String zipCode;

    private String defaultPayment;

    private String WCToken;

    private String WCTrustedToken;

    private String skipAddressValidation;

    private String cardNumber;

    private String cardName;

    private String cardType;

    private String expiryMonth;

    private String expiryYear;

    private String country;

    private String cardNickName;

    private String giftCardNumber;

    private String accessNumber;

    private String addressId;

    private String postalCode;

    /************    getter setter     ************/
    public TargetGuestPaymentType getType() {
        return type;
    }

    public void setType(TargetGuestPaymentType type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public List<String> getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(List<String> addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDefaultPayment() {
        return defaultPayment;
    }

    public void setDefaultPayment(String defaultPayment) {
        this.defaultPayment = defaultPayment;
    }

    public String getWCToken() {
        return WCToken;
    }

    public void setWCToken(String WCToken) {
        this.WCToken = WCToken;
    }

    public String getWCTrustedToken() {
        return WCTrustedToken;
    }

    public void setWCTrustedToken(String WCTrustedToken) {
        this.WCTrustedToken = WCTrustedToken;
    }

    public String getSkipAddressValidation() {
        return skipAddressValidation;
    }

    public void setSkipAddressValidation(String skipAddressValidation) {
        this.skipAddressValidation = skipAddressValidation;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCardNickName() {
        return cardNickName;
    }

    public void setCardNickName(String cardNickName) {
        this.cardNickName = cardNickName;
    }

    public String getGiftCardNumber() {
        return giftCardNumber;
    }

    public void setGiftCardNumber(String giftCardNumber) {
        this.giftCardNumber = giftCardNumber;
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
