package com.voyageone.components.intltarget.bean;

import java.util.List;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestAddress {

    private String addressId;//	Unique Id for an address.

    private List<String> addressLine;//

    private String addressType;//	Address type.The valid values are ‘S’, ‘B’ or ‘SB’.

    private String city;//	City name in address entered by the user.

    private String firstName;//	First name of the user.

    private String lastName;//	Last name of the user.

    private String middleName;//	Middle name of the user.

    private String phone1;//	Personal contact number.

    private String phone1Type;//	Type of contact number.

    private String primary;//	The default Shipping address will have primary set as true, else false.

    private String state;//	The state corresponding to the address.

    private String zipCode;//	The postal/zip code corresponding to the address.

    private String markForDelete;//	Indicator whether address can be deleted or not. ‘Y ‘means address can be deleted. ‘N’ means address can’t be deleted


    /********    getter setter      *******/
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<String> getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(List<String> addressLine) {
        this.addressLine = addressLine;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone1Type() {
        return phone1Type;
    }

    public void setPhone1Type(String phone1Type) {
        this.phone1Type = phone1Type;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
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

    public String getMarkForDelete() {
        return markForDelete;
    }

    public void setMarkForDelete(String markForDelete) {
        this.markForDelete = markForDelete;
    }
}
