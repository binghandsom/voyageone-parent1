package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartAddProductResponse {
    private String totalQuantity;

    public String getTotalQuantity() {
        return this.totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    private List<OrderItem> orderItem;

    public List<OrderItem> getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }


    /**
     * OrderItem is the inner class of TargetCartAddProductResponse
     */
    public static class OrderItem {
        private List<UsableShippingMode> usableShippingMode;

        public List<UsableShippingMode> getUsableShippingMode() {
            return this.usableShippingMode;
        }

        public void setUsableShippingMode(List<UsableShippingMode> usableShippingMode) {
            this.usableShippingMode = usableShippingMode;
        }


        /**
         * UsableShippingMode is the inner class of OrderItem
         */
        public static class UsableShippingMode {
            private String fulfillmentType;

            public String getFulfillmentType() {
                return this.fulfillmentType;
            }

            public void setFulfillmentType(String fulfillmentType) {
                this.fulfillmentType = fulfillmentType;
            }

        }

        private List<Attachments> attachments;

        public List<Attachments> getAttachments() {
            return this.attachments;
        }

        public void setAttachments(List<Attachments> attachments) {
            this.attachments = attachments;
        }


        /**
         * Attachments is the inner class of OrderItem
         */
        public static class Attachments {
            private String path;

            public String getPath() {
                return this.path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            private String usage;

            public String getUsage() {
                return this.usage;
            }

            public void setUsage(String usage) {
                this.usage = usage;
            }

        }

        private String giftWrappable;

        public String getGiftWrappable() {
            return this.giftWrappable;
        }

        public void setGiftWrappable(String giftWrappable) {
            this.giftWrappable = giftWrappable;
        }

        private List<ItemAttributes> itemAttributes;

        public List<ItemAttributes> getItemAttributes() {
            return this.itemAttributes;
        }

        public void setItemAttributes(List<ItemAttributes> itemAttributes) {
            this.itemAttributes = itemAttributes;
        }


        /**
         * ItemAttributes is the inner class of OrderItem
         */
        public static class ItemAttributes {
            private String attrValue;

            public String getAttrValue() {
                return this.attrValue;
            }

            public void setAttrValue(String attrValue) {
                this.attrValue = attrValue;
            }

            private String attrName;

            public String getAttrName() {
                return this.attrName;
            }

            public void setAttrName(String attrName) {
                this.attrName = attrName;
            }

        }

        private String orderItemPriceCurrency;

        public String getOrderItemPriceCurrency() {
            return this.orderItemPriceCurrency;
        }

        public void setOrderItemPriceCurrency(String orderItemPriceCurrency) {
            this.orderItemPriceCurrency = orderItemPriceCurrency;
        }

        private String giftWrapChargePerUnitCurrency;

        public String getGiftWrapChargePerUnitCurrency() {
            return this.giftWrapChargePerUnitCurrency;
        }

        public void setGiftWrapChargePerUnitCurrency(String giftWrapChargePerUnitCurrency) {
            this.giftWrapChargePerUnitCurrency = giftWrapChargePerUnitCurrency;
        }

        private String DPCI;

        public String getDPCI() {
            return this.DPCI;
        }

        public void setDPCI(String DPCI) {
            this.DPCI = DPCI;
        }

        private String isSTS;

        public String getIsSTS() {
            return this.isSTS;
        }

        public void setIsSTS(String isSTS) {
            this.isSTS = isSTS;
        }

        private String giftWrapChargePerUnit;

        public String getGiftWrapChargePerUnit() {
            return this.giftWrapChargePerUnit;
        }

        public void setGiftWrapChargePerUnit(String giftWrapChargePerUnit) {
            this.giftWrapChargePerUnit = giftWrapChargePerUnit;
        }

        private String orderItemPrice;

        public String getOrderItemPrice() {
            return this.orderItemPrice;
        }

        public void setOrderItemPrice(String orderItemPrice) {
            this.orderItemPrice = orderItemPrice;
        }

        private List<Offers> offers;

        public List<Offers> getOffers() {
            return this.offers;
        }

        public void setOffers(List<Offers> offers) {
            this.offers = offers;
        }


        /**
         * Offers is the inner class of OrderItem
         */
        public static class Offers {
            private List<OfferPrice> offerPrice;

            public List<OfferPrice> getOfferPrice() {
                return this.offerPrice;
            }

            public void setOfferPrice(List<OfferPrice> offerPrice) {
                this.offerPrice = offerPrice;
            }


            /**
             * OfferPrice is the inner class of Offers
             */
            public static class OfferPrice {
                private String priceValue;

                public String getPriceValue() {
                    return this.priceValue;
                }

                public void setPriceValue(String priceValue) {
                    this.priceValue = priceValue;
                }

                private String currencyCode;

                public String getCurrencyCode() {
                    return this.currencyCode;
                }

                public void setCurrencyCode(String currencyCode) {
                    this.currencyCode = currencyCode;
                }

            }

        }

        private String unitPrice;

        public String getUnitPrice() {
            return this.unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        private String quantity;

        public String getQuantity() {
            return this.quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        private String orderItemId;

        public String getOrderItemId() {
            return this.orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }

        private String inventoryStatus;

        public String getInventoryStatus() {
            return this.inventoryStatus;
        }

        public void setInventoryStatus(String inventoryStatus) {
            this.inventoryStatus = inventoryStatus;
        }

        private String isAO;

        public String getIsAO() {
            return this.isAO;
        }

        public void setIsAO(String isAO) {
            this.isAO = isAO;
        }

        private String maxPurchaseLimit;

        public String getMaxPurchaseLimit() {
            return this.maxPurchaseLimit;
        }

        public void setMaxPurchaseLimit(String maxPurchaseLimit) {
            this.maxPurchaseLimit = maxPurchaseLimit;
        }

        private String frequencyInterval;

        public String getFrequencyInterval() {
            return this.frequencyInterval;
        }

        public void setFrequencyInterval(String frequencyInterval) {
            this.frequencyInterval = frequencyInterval;
        }

        private String isShipToHomeEligible;

        public String getIsShipToHomeEligible() {
            return this.isShipToHomeEligible;
        }

        public void setIsShipToHomeEligible(String isShipToHomeEligible) {
            this.isShipToHomeEligible = isShipToHomeEligible;
        }

        private String purchaseChannelId;

        public String getPurchaseChannelId() {
            return this.purchaseChannelId;
        }

        public void setPurchaseChannelId(String purchaseChannelId) {
            this.purchaseChannelId = purchaseChannelId;
        }

        private String isAOEligible;

        public String getIsAOEligible() {
            return this.isAOEligible;
        }

        public void setIsAOEligible(String isAOEligible) {
            this.isAOEligible = isAOEligible;
        }

        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String catalogEntryId;

        public String getCatalogEntryId() {
            return this.catalogEntryId;
        }

        public void setCatalogEntryId(String catalogEntryId) {
            this.catalogEntryId = catalogEntryId;
        }

        private List<Attributes> attributes;

        public List<Attributes> getAttributes() {
            return this.attributes;
        }

        public void setAttributes(List<Attributes> attributes) {
            this.attributes = attributes;
        }


        /**
         * Attributes is the inner class of OrderItem
         */
        public static class Attributes {
            private String identifier;

            public String getIdentifier() {
                return this.identifier;
            }

            public void setIdentifier(String identifier) {
                this.identifier = identifier;
            }

            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String description;

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        private String partNumber;

        public String getPartNumber() {
            return this.partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        private String giftWrapStatus;

        public String getGiftWrapStatus() {
            return this.giftWrapStatus;
        }

        public void setGiftWrapStatus(String giftWrapStatus) {
            this.giftWrapStatus = giftWrapStatus;
        }

        private String isSFSEligible;

        public String getIsSFSEligible() {
            return this.isSFSEligible;
        }

        public void setIsSFSEligible(String isSFSEligible) {
            this.isSFSEligible = isSFSEligible;
        }

    }

    private String responseTime;

    public String getResponseTime() {
        return this.responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

}
