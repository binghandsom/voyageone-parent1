package com.voyageone.components.intltarget.bean.guest;

import java.util.List;

/**
 * @author aooer 2016/4/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestShippingAddressRequest {

    private String firstName;
    private String lastName;
    private String middleName;
    private String addressType;
    private List<String> addressLine;
    private String city;
    private String state;//length 2
    private String zipCode;
    private String phoneType;
    private String phone;
    private String saveAsDefault;
    private String skipAddressValidation;

    /******   getter setter   ******/

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

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSaveAsDefault() {
        return saveAsDefault;
    }

    public void setSaveAsDefault(String saveAsDefault) {
        this.saveAsDefault = saveAsDefault;
    }

    public String getSkipAddressValidation() {
        return skipAddressValidation;
    }

    public void setSkipAddressValidation(String skipAddressValidation) {
        this.skipAddressValidation = skipAddressValidation;
    }
}
