package com.voyageone.components.intltarget.bean.guest;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestAccount {

    private String pluckToken;//	Token identified by Pluck for community activities

    private String cartQuantity;//	Count of Items in Cart

    private String logonId;//	Logon Id

    private String firstName;//	First name of the user

    private String lastName;//	Last name of the user

    private String userId;//	Unique Id for a user

    private String city;//	City

    private String state;//	State

    private String screenName;//	Screen name of the user

    private String personalizationId;//	Personalization Id of the user.

    private String cartAmount;//	Total Amount (Price)


    /*    getter setter      */

    public String getPluckToken() {
        return pluckToken;
    }

    public void setPluckToken(String pluckToken) {
        this.pluckToken = pluckToken;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public String getLogonId() {
        return logonId;
    }

    public void setLogonId(String logonId) {
        this.logonId = logonId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getPersonalizationId() {
        return personalizationId;
    }

    public void setPersonalizationId(String personalizationId) {
        this.personalizationId = personalizationId;
    }

    public String getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(String cartAmount) {
        this.cartAmount = cartAmount;
    }
}
