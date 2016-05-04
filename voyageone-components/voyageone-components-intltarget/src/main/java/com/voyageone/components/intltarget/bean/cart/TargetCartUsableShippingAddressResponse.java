package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartUsableShippingAddressResponse {

    private List<UsableShippingAddressInfo> usableShippingAddressInfo;

    public List<UsableShippingAddressInfo> getUsableShippingAddressInfo() {
        return this.usableShippingAddressInfo;
    }

    public void setUsableShippingAddressInfo(List<UsableShippingAddressInfo> usableShippingAddressInfo) {
        this.usableShippingAddressInfo = usableShippingAddressInfo;
    }


    /**
     * UsableShippingAddressInfo is the inner class of TargetCartUsableShippingAddressResponse
     */
    public static class UsableShippingAddressInfo {
        private String country;

        public String getCountry() {
            return this.country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

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

        private String officeAddressFlag;

        public String getOfficeAddressFlag() {
            return this.officeAddressFlag;
        }

        public void setOfficeAddressFlag(String officeAddressFlag) {
            this.officeAddressFlag = officeAddressFlag;
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

        private String isPrimary;

        public String getIsPrimary() {
            return this.isPrimary;
        }

        public void setIsPrimary(String isPrimary) {
            this.isPrimary = isPrimary;
        }

        private String middleName;

        public String getMiddleName() {
            return this.middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        private List<String> addressLine;

        public List<String> getAddressLine() {
            return this.addressLine;
        }

        public void setAddressLine(List<String> addressLine) {
            this.addressLine = addressLine;
        }

        private String addressId;

        public String getAddressId() {
            return this.addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        private String stateOrProvinceName;

        public String getStateOrProvinceName() {
            return this.stateOrProvinceName;
        }

        public void setStateOrProvinceName(String stateOrProvinceName) {
            this.stateOrProvinceName = stateOrProvinceName;
        }

    }

    private String isMultipleShipment;

    public String getIsMultipleShipment() {
        return this.isMultipleShipment;
    }

    public void setIsMultipleShipment(String isMultipleShipment) {
        this.isMultipleShipment = isMultipleShipment;
    }

    private String orderId;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private List<OrderItem> orderItem;

    public List<OrderItem> getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }


    /**
     * OrderItem is the inner class of TargetCartUsableShippingAddressResponse
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
            private List<ShippingModes> shippingModes;

            public List<ShippingModes> getShippingModes() {
                return this.shippingModes;
            }

            public void setShippingModes(List<ShippingModes> shippingModes) {
                this.shippingModes = shippingModes;
            }


            /**
             * ShippingModes is the inner class of UsableShippingMode
             */
            public static class ShippingModes {
                private String shipModeCode;

                public String getShipModeCode() {
                    return this.shipModeCode;
                }

                public void setShipModeCode(String shipModeCode) {
                    this.shipModeCode = shipModeCode;
                }

                private String EDDStartDate;

                public String getEDDStartDate() {
                    return this.EDDStartDate;
                }

                public void setEDDStartDate(String EDDStartDate) {
                    this.EDDStartDate = EDDStartDate;
                }

                private String EDDEndDate;

                public String getEDDEndDate() {
                    return this.EDDEndDate;
                }

                public void setEDDEndDate(String EDDEndDate) {
                    this.EDDEndDate = EDDEndDate;
                }

                private String shipModeId;

                public String getShipModeId() {
                    return this.shipModeId;
                }

                public void setShipModeId(String shipModeId) {
                    this.shipModeId = shipModeId;
                }

            }

            private String fullfillmentType;

            public String getFullfillmentType() {
                return this.fullfillmentType;
            }

            public void setFullfillmentType(String fullfillmentType) {
                this.fullfillmentType = fullfillmentType;
            }

        }

        private String itemType;

        public String getItemType() {
            return this.itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
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

        private String quantity;

        public String getQuantity() {
            return this.quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        private String giftWrappable;

        public String getGiftWrappable() {
            return this.giftWrappable;
        }

        public void setGiftWrappable(String giftWrappable) {
            this.giftWrappable = giftWrappable;
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

        private String DPCI;

        public String getDPCI() {
            return this.DPCI;
        }

        public void setDPCI(String DPCI) {
            this.DPCI = DPCI;
        }

        private String obgbFlag;

        public String getObgbFlag() {
            return this.obgbFlag;
        }

        public void setObgbFlag(String obgbFlag) {
            this.obgbFlag = obgbFlag;
        }

        private String isSTS;

        public String getIsSTS() {
            return this.isSTS;
        }

        public void setIsSTS(String isSTS) {
            this.isSTS = isSTS;
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

        private String defaultShipModeCode;

        public String getDefaultShipModeCode() {
            return this.defaultShipModeCode;
        }

        public void setDefaultShipModeCode(String defaultShipModeCode) {
            this.defaultShipModeCode = defaultShipModeCode;
        }

        private String isAOEligible;

        public String getIsAOEligible() {
            return this.isAOEligible;
        }

        public void setIsAOEligible(String isAOEligible) {
            this.isAOEligible = isAOEligible;
        }

        private String catalogEntryId;

        public String getCatalogEntryId() {
            return this.catalogEntryId;
        }

        public void setCatalogEntryId(String catalogEntryId) {
            this.catalogEntryId = catalogEntryId;
        }

        private String partNumber;

        public String getPartNumber() {
            return this.partNumber;
        }

        public void setPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        private String defaultAddressId;

        public String getDefaultAddressId() {
            return this.defaultAddressId;
        }

        public void setDefaultAddressId(String defaultAddressId) {
            this.defaultAddressId = defaultAddressId;
        }

        private String defaultFullfillmentType;

        public String getDefaultFullfillmentType() {
            return this.defaultFullfillmentType;
        }

        public void setDefaultFullfillmentType(String defaultFullfillmentType) {
            this.defaultFullfillmentType = defaultFullfillmentType;
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

        private String orderItemDescription;

        public String getOrderItemDescription() {
            return this.orderItemDescription;
        }

        public void setOrderItemDescription(String orderItemDescription) {
            this.orderItemDescription = orderItemDescription;
        }

        private String orderItemPrice;

        public String getOrderItemPrice() {
            return this.orderItemPrice;
        }

        public void setOrderItemPrice(String orderItemPrice) {
            this.orderItemPrice = orderItemPrice;
        }

        private String isRushDeliveryEligible;

        public String getIsRushDeliveryEligible() {
            return this.isRushDeliveryEligible;
        }

        public void setIsRushDeliveryEligible(String isRushDeliveryEligible) {
            this.isRushDeliveryEligible = isRushDeliveryEligible;
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
