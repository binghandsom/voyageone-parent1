package com.voyageone.components.intltarget.bean.cart;


import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartAddProductResponse {

    public String totalQuantity;
    public List<OrderItem> orderItem;

    /** OrderItem is the inner class of TargetCartAddProductResponse */
    public static class OrderItem {
        public List<UsableShippingMode> usableShippingMode;

        /** UsableShippingMode is the inner class of OrderItem */
        public static class UsableShippingMode {
            public String fulfillmentType;
        }

        public List<Attachments> attachments;

        /** Attachments is the inner class of OrderItem */
        public static class Attachments {
            public String path;
            public String usage;
        }

        public String giftWrappable;
        public List<ItemAttributes> itemAttributes;

        /** ItemAttributes is the inner class of OrderItem */
        public static class ItemAttributes {
            public String attrValue;
            public String attrName;
        }

        public String orderItemPriceCurrency;
        public String giftWrapChargePerUnitCurrency;
        public String DPCI;
        public String isSTS;
        public String giftWrapChargePerUnit;
        public String orderItemPrice;
        public List<Offers> offers;

        /** Offers is the inner class of OrderItem */
        public static class Offers {
            public List<OfferPrice> offerPrice;

            /** OfferPrice is the inner class of Offers */
            public static class OfferPrice {
                public String priceValue;
                public String currencyCode;
            }

        }

        public String unitPrice;
        public String quantity;
        public String orderItemId;
        public String inventoryStatus;
        public String isAO;
        public String maxPurchaseLimit;
        public String frequencyInterval;
        public String isShipToHomeEligible;
        public String purchaseChannelId;
        public String isAOEligible;
        public String name;
        public String catalogEntryId;
        public List<Attributes> attributes;

        /** Attributes is the inner class of OrderItem */
        public static class Attributes {
            public String identifier;
            public String name;
            public String description;
        }

        public String partNumber;
        public String giftWrapStatus;
        public String isSFSEligible;
    }

    public String responseTime;
}
