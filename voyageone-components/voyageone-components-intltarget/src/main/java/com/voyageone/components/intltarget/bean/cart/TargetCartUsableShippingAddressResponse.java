package com.voyageone.components.intltarget.bean.cart;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartUsableShippingAddressResponse {

    public List<UsableShippingAddressInfo> usableShippingAddressInfo;

    /** UsableShippingAddressInfo is the inner class of TargetCartUsableShippingAddressResponse */
    public static class UsableShippingAddressInfo {
        public String country;
        public String firstName;
        public String lastName;
        public String zipCode;
        public String officeAddressFlag;
        public String city;
        public String phone;
        public String isPrimary;
        public String middleName;
        public List<String> addressLine;
        public String addressId;
        public String stateOrProvinceName;
    }

    public String isMultipleShipment;
    public String orderId;
    public List<OrderItem> orderItem;

    /** OrderItem is the inner class of TargetCartUsableShippingAddressResponse */
    public static class OrderItem {
        public List<UsableShippingMode> usableShippingMode;

        /** UsableShippingMode is the inner class of OrderItem */
        public static class UsableShippingMode {
            public List<ShippingModes> shippingModes;

            /** ShippingModes is the inner class of UsableShippingMode */
            public static class ShippingModes {
                public String shipModeCode;
                public String EDDStartDate;
                public String EDDEndDate;
                public String shipModeId;
            }

            public String fullfillmentType;
        }

        public String itemType;
        public List<Attachments> attachments;

        /** Attachments is the inner class of OrderItem */
        public static class Attachments {
            public String path;
            public String usage;
        }

        public String quantity;
        public String giftWrappable;
        public String orderItemId;
        public String inventoryStatus;
        public String isAO;
        public String DPCI;
        public String obgbFlag;
        public String isSTS;
        public String isShipToHomeEligible;
        public String purchaseChannelId;
        public String defaultShipModeCode;
        public String isAOEligible;
        public String catalogEntryId;
        public String partNumber;
        public String defaultAddressId;
        public String defaultFullfillmentType;
        public String giftWrapStatus;
        public String isSFSEligible;
        public String orderItemDescription;
        public String orderItemPrice;
        public String isRushDeliveryEligible;
    }

    public String responseTime;
}
