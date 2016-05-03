package com.voyageone.components.intltarget.bean.cart;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartApplyShippingDetailRequest {

    public List<Address> address;

    /** Address is the inner class of TargetCartApplyShippingDetailRequest */
    public static class Address {
        public String firstName;
        public String lastName;
        public String zipCode;
        public String phoneType;
        public String city;
        public String phone;
        public String skipAddressValidation;
        public String middleName;
        public String state;
        public List<String> addressLine;
    }

    public List<OrderItem> orderItem;

    /** OrderItem is the inner class of TargetCartApplyShippingDetailRequest */
    public static class OrderItem {
        public String orderItemId;
        public String shipModeId;
    }
}
