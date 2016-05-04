package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartApplyShippingDetailRequest {
    private List<Address> address;

    public List<Address> getAddress() {
        return this.address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }


    /**
     * Address is the inner class of TargetCartApplyShippingDetailRequest
     */
    public static class Address {
        private String firstName;

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        private String lastName;

        public String getLastName() {
            return this.lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        private String zipCode;

        public String getZipCode() {
            return this.zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        private String phoneType;

        public String getPhoneType() {
            return this.phoneType;
        }

        public void setPhoneType(String phoneType) {
            this.phoneType = phoneType;
        }

        private String city;

        public String getCity() {
            return this.city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        private String phone;

        public String getPhone() {
            return this.phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        private String skipAddressValidation;

        public String getSkipAddressValidation() {
            return this.skipAddressValidation;
        }

        public void setSkipAddressValidation(String skipAddressValidation) {
            this.skipAddressValidation = skipAddressValidation;
        }

        private String middleName;

        public String getMiddleName() {
            return this.middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        private String state;

        public String getState() {
            return this.state;
        }

        public void setState(String state) {
            this.state = state;
        }

        private List<String> addressLine;

        public List<String> getAddressLine() {
            return this.addressLine;
        }

        public void setAddressLine(List<String> addressLine) {
            this.addressLine = addressLine;
        }

    }

    private List<OrderItem> orderItem;

    public List<OrderItem> getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }


    /**
     * OrderItem is the inner class of TargetCartApplyShippingDetailRequest
     */
    public static class OrderItem {
        private String orderItemId;

        public String getOrderItemId() {
            return this.orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }

        private String shipModeId;

        public String getShipModeId() {
            return this.shipModeId;
        }

        public void setShipModeId(String shipModeId) {
            this.shipModeId = shipModeId;
        }

    }

}
