package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartOrderReviewResponse {
    private String totalShippingCharge;

    public String getTotalShippingCharge() {
        return this.totalShippingCharge;
    }

    public void setTotalShippingCharge(String totalShippingCharge) {
        this.totalShippingCharge = totalShippingCharge;
    }

    private String orderId;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String giftWrapChargeCurrency;

    public String getGiftWrapChargeCurrency() {
        return this.giftWrapChargeCurrency;
    }

    public void setGiftWrapChargeCurrency(String giftWrapChargeCurrency) {
        this.giftWrapChargeCurrency = giftWrapChargeCurrency;
    }

    private String isFreeShipping;

    public String getIsFreeShipping() {
        return this.isFreeShipping;
    }

    public void setIsFreeShipping(String isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
    }

    private String totalShippingChargeCurrency;

    public String getTotalShippingChargeCurrency() {
        return this.totalShippingChargeCurrency;
    }

    public void setTotalShippingChargeCurrency(String totalShippingChargeCurrency) {
        this.totalShippingChargeCurrency = totalShippingChargeCurrency;
    }

    private String grandTotalCurrency;

    public String getGrandTotalCurrency() {
        return this.grandTotalCurrency;
    }

    public void setGrandTotalCurrency(String grandTotalCurrency) {
        this.grandTotalCurrency = grandTotalCurrency;
    }

    private List<PaymentInstruction> paymentInstruction;

    public List<PaymentInstruction> getPaymentInstruction() {
        return this.paymentInstruction;
    }

    public void setPaymentInstruction(List<PaymentInstruction> paymentInstruction) {
        this.paymentInstruction = paymentInstruction;
    }


    /**
     * PaymentInstruction is the inner class of TargetCartOrderReviewResponse
     */
    public static class PaymentInstruction {
        private String piId;

        public String getPiId() {
            return this.piId;
        }

        public void setPiId(String piId) {
            this.piId = piId;
        }

        private String country;

        public String getCountry() {
            return this.country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        private String lastName;

        public String getLastName() {
            return this.lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        private String personTitle;

        public String getPersonTitle() {
            return this.personTitle;
        }

        public void setPersonTitle(String personTitle) {
            this.personTitle = personTitle;
        }

        private String city;

        public String getCity() {
            return this.city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        private String postalCode;

        public String getPostalCode() {
            return this.postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        private String phone2;

        public String getPhone2() {
            return this.phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }

        private String phone2Publish;

        public String getPhone2Publish() {
            return this.phone2Publish;
        }

        public void setPhone2Publish(String phone2Publish) {
            this.phone2Publish = phone2Publish;
        }

        private String phone1Publish;

        public String getPhone1Publish() {
            return this.phone1Publish;
        }

        public void setPhone1Publish(String phone1Publish) {
            this.phone1Publish = phone1Publish;
        }

        private String piDescription;

        public String getPiDescription() {
            return this.piDescription;
        }

        public void setPiDescription(String piDescription) {
            this.piDescription = piDescription;
        }

        private String payMethodId;

        public String getPayMethodId() {
            return this.payMethodId;
        }

        public void setPayMethodId(String payMethodId) {
            this.payMethodId = payMethodId;
        }

        private String phone1;

        public String getPhone1() {
            return this.phone1;
        }

        public void setPhone1(String phone1) {
            this.phone1 = phone1;
        }

        private String email2;

        public String getEmail2() {
            return this.email2;
        }

        public void setEmail2(String email2) {
            this.email2 = email2;
        }

        private String email1;

        public String getEmail1() {
            return this.email1;
        }

        public void setEmail1(String email1) {
            this.email1 = email1;
        }

        private String fax2;

        public String getFax2() {
            return this.fax2;
        }

        public void setFax2(String fax2) {
            this.fax2 = fax2;
        }

        private String fax1;

        public String getFax1() {
            return this.fax1;
        }

        public void setFax1(String fax1) {
            this.fax1 = fax1;
        }

        private String billing_address_id;

        public String getBilling_address_id() {
            return this.billing_address_id;
        }

        public void setBilling_address_id(String billing_address_id) {
            this.billing_address_id = billing_address_id;
        }

        private String piAmount;

        public String getPiAmount() {
            return this.piAmount;
        }

        public void setPiAmount(String piAmount) {
            this.piAmount = piAmount;
        }

        private String nickName;

        public String getNickName() {
            return this.nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        private List<String> addressLine;

        public List<String> getAddressLine() {
            return this.addressLine;
        }

        public void setAddressLine(List<String> addressLine) {
            this.addressLine = addressLine;
        }

        private String piLanguage;

        public String getPiLanguage() {
            return this.piLanguage;
        }

        public void setPiLanguage(String piLanguage) {
            this.piLanguage = piLanguage;
        }

        private String phone1Type;

        public String getPhone1Type() {
            return this.phone1Type;
        }

        public void setPhone1Type(String phone1Type) {
            this.phone1Type = phone1Type;
        }

        private String piStatus;

        public String getPiStatus() {
            return this.piStatus;
        }

        public void setPiStatus(String piStatus) {
            this.piStatus = piStatus;
        }

        private String stateOrProvinceName;

        public String getStateOrProvinceName() {
            return this.stateOrProvinceName;
        }

        public void setStateOrProvinceName(String stateOrProvinceName) {
            this.stateOrProvinceName = stateOrProvinceName;
        }

        private String firstName;

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        private String piCurrency;

        public String getPiCurrency() {
            return this.piCurrency;
        }

        public void setPiCurrency(String piCurrency) {
            this.piCurrency = piCurrency;
        }

        private List<ProtocolData> protocolData;

        public List<ProtocolData> getProtocolData() {
            return this.protocolData;
        }

        public void setProtocolData(List<ProtocolData> protocolData) {
            this.protocolData = protocolData;
        }


        /**
         * ProtocolData is the inner class of PaymentInstruction
         */
        public static class ProtocolData {
            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String value;

            public String getValue() {
                return this.value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

        private String middleName;

        public String getMiddleName() {
            return this.middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

    }

    private String estimatedTaxCurrency;

    public String getEstimatedTaxCurrency() {
        return this.estimatedTaxCurrency;
    }

    public void setEstimatedTaxCurrency(String estimatedTaxCurrency) {
        this.estimatedTaxCurrency = estimatedTaxCurrency;
    }

    private String totalProductPriceCurrency;

    public String getTotalProductPriceCurrency() {
        return this.totalProductPriceCurrency;
    }

    public void setTotalProductPriceCurrency(String totalProductPriceCurrency) {
        this.totalProductPriceCurrency = totalProductPriceCurrency;
    }

    private String totalProductPrice;

    public String getTotalProductPrice() {
        return this.totalProductPrice;
    }

    public void setTotalProductPrice(String totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    private String savings;

    public String getSavings() {
        return this.savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }

    private String trackEmail;

    public String getTrackEmail() {
        return this.trackEmail;
    }

    public void setTrackEmail(String trackEmail) {
        this.trackEmail = trackEmail;
    }

    private String totalAdjustmentCurrency;

    public String getTotalAdjustmentCurrency() {
        return this.totalAdjustmentCurrency;
    }

    public void setTotalAdjustmentCurrency(String totalAdjustmentCurrency) {
        this.totalAdjustmentCurrency = totalAdjustmentCurrency;
    }

    private String totalSalesTaxCurrency;

    public String getTotalSalesTaxCurrency() {
        return this.totalSalesTaxCurrency;
    }

    public void setTotalSalesTaxCurrency(String totalSalesTaxCurrency) {
        this.totalSalesTaxCurrency = totalSalesTaxCurrency;
    }

    private List<Shipment> shipment;

    public List<Shipment> getShipment() {
        return this.shipment;
    }

    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
    }


    /**
     * Shipment is the inner class of TargetCartOrderReviewResponse
     */
    public static class Shipment {
        private String country;

        public String getCountry() {
            return this.country;
        }

        public void setCountry(String country) {
            this.country = country;
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

        private String shipmentQuantity;

        public String getShipmentQuantity() {
            return this.shipmentQuantity;
        }

        public void setShipmentQuantity(String shipmentQuantity) {
            this.shipmentQuantity = shipmentQuantity;
        }

        private String shipModeCode;

        public String getShipModeCode() {
            return this.shipModeCode;
        }

        public void setShipModeCode(String shipModeCode) {
            this.shipModeCode = shipModeCode;
        }

        private String city;

        public String getCity() {
            return this.city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        private List<OrderItem> orderItem;

        public List<OrderItem> getOrderItem() {
            return this.orderItem;
        }

        public void setOrderItem(List<OrderItem> orderItem) {
            this.orderItem = orderItem;
        }


        /**
         * OrderItem is the inner class of Shipment
         */
        public static class OrderItem {
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

            private String orderItemId;

            public String getOrderItemId() {
                return this.orderItemId;
            }

            public void setOrderItemId(String orderItemId) {
                this.orderItemId = orderItemId;
            }

            private List<Price> price;

            public List<Price> getPrice() {
                return this.price;
            }

            public void setPrice(List<Price> price) {
                this.price = price;
            }


            /**
             * Price is the inner class of OrderItem
             */
            public static class Price {
                private String priceUsage;

                public String getPriceUsage() {
                    return this.priceUsage;
                }

                public void setPriceUsage(String priceUsage) {
                    this.priceUsage = priceUsage;
                }

                private String priceValue;

                public String getPriceValue() {
                    return this.priceValue;
                }

                public void setPriceValue(String priceValue) {
                    this.priceValue = priceValue;
                }

                private String priceDescription;

                public String getPriceDescription() {
                    return this.priceDescription;
                }

                public void setPriceDescription(String priceDescription) {
                    this.priceDescription = priceDescription;
                }

            }

            private String inventoryStatus;

            public String getInventoryStatus() {
                return this.inventoryStatus;
            }

            public void setInventoryStatus(String inventoryStatus) {
                this.inventoryStatus = inventoryStatus;
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

            private String isAO;

            public String getIsAO() {
                return this.isAO;
            }

            public void setIsAO(String isAO) {
                this.isAO = isAO;
            }

            private String currency;

            public String getCurrency() {
                return this.currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            private String partNumber;

            public String getPartNumber() {
                return this.partNumber;
            }

            public void setPartNumber(String partNumber) {
                this.partNumber = partNumber;
            }

            private String orderItemPrice;

            public String getOrderItemPrice() {
                return this.orderItemPrice;
            }

            public void setOrderItemPrice(String orderItemPrice) {
                this.orderItemPrice = orderItemPrice;
            }

        }

        private String shipModeDescription;

        public String getShipModeDescription() {
            return this.shipModeDescription;
        }

        public void setShipModeDescription(String shipModeDescription) {
            this.shipModeDescription = shipModeDescription;
        }

        private String expectedDeliveryDate;

        public String getExpectedDeliveryDate() {
            return this.expectedDeliveryDate;
        }

        public void setExpectedDeliveryDate(String expectedDeliveryDate) {
            this.expectedDeliveryDate = expectedDeliveryDate;
        }

        private String shipModeId;

        public String getShipModeId() {
            return this.shipModeId;
        }

        public void setShipModeId(String shipModeId) {
            this.shipModeId = shipModeId;
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

        private String firstName;

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        private String signatureRequired;

        public String getSignatureRequired() {
            return this.signatureRequired;
        }

        public void setSignatureRequired(String signatureRequired) {
            this.signatureRequired = signatureRequired;
        }

        private String middleName;

        public String getMiddleName() {
            return this.middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        private String shipmentNumber;

        public String getShipmentNumber() {
            return this.shipmentNumber;
        }

        public void setShipmentNumber(String shipmentNumber) {
            this.shipmentNumber = shipmentNumber;
        }

    }

    private String totalSalesTax;

    public String getTotalSalesTax() {
        return this.totalSalesTax;
    }

    public void setTotalSalesTax(String totalSalesTax) {
        this.totalSalesTax = totalSalesTax;
    }

    private String grandTotal;

    public String getGrandTotal() {
        return this.grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    private String stateAndLocalFeeCurrency;

    public String getStateAndLocalFeeCurrency() {
        return this.stateAndLocalFeeCurrency;
    }

    public void setStateAndLocalFeeCurrency(String stateAndLocalFeeCurrency) {
        this.stateAndLocalFeeCurrency = stateAndLocalFeeCurrency;
    }

    private String totalShippingTax;

    public String getTotalShippingTax() {
        return this.totalShippingTax;
    }

    public void setTotalShippingTax(String totalShippingTax) {
        this.totalShippingTax = totalShippingTax;
    }

    private String stateAndLocalFee;

    public String getStateAndLocalFee() {
        return this.stateAndLocalFee;
    }

    public void setStateAndLocalFee(String stateAndLocalFee) {
        this.stateAndLocalFee = stateAndLocalFee;
    }

    private String totalShippingTaxCurrency;

    public String getTotalShippingTaxCurrency() {
        return this.totalShippingTaxCurrency;
    }

    public void setTotalShippingTaxCurrency(String totalShippingTaxCurrency) {
        this.totalShippingTaxCurrency = totalShippingTaxCurrency;
    }

    private String estimatedTax;

    public String getEstimatedTax() {
        return this.estimatedTax;
    }

    public void setEstimatedTax(String estimatedTax) {
        this.estimatedTax = estimatedTax;
    }

    private String giftWrapCharge;

    public String getGiftWrapCharge() {
        return this.giftWrapCharge;
    }

    public void setGiftWrapCharge(String giftWrapCharge) {
        this.giftWrapCharge = giftWrapCharge;
    }

    private String savingsPercent;

    public String getSavingsPercent() {
        return this.savingsPercent;
    }

    public void setSavingsPercent(String savingsPercent) {
        this.savingsPercent = savingsPercent;
    }

    private String savingsCurrency;

    public String getSavingsCurrency() {
        return this.savingsCurrency;
    }

    public void setSavingsCurrency(String savingsCurrency) {
        this.savingsCurrency = savingsCurrency;
    }

    private String totalAdjustment;

    public String getTotalAdjustment() {
        return this.totalAdjustment;
    }

    public void setTotalAdjustment(String totalAdjustment) {
        this.totalAdjustment = totalAdjustment;
    }

}
