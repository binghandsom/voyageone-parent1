package com.voyageone.components.intltarget.bean.cart;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartOrderReviewResponse {
    public String totalShippingCharge;
    public String orderId;
    public String giftWrapChargeCurrency;
    public String isFreeShipping;
    public String totalShippingChargeCurrency;
    public String grandTotalCurrency;
    public List<PaymentInstruction> paymentInstruction;

    /** PaymentInstruction is the inner class of TargetCartOrderReviewResponse */
    public class PaymentInstruction {
        public String piId;
        public String country;
        public String lastName;
        public String personTitle;
        public String city;
        public String postalCode;
        public String phone2;
        public String phone2Publish;
        public String phone1Publish;
        public String piDescription;
        public String payMethodId;
        public String phone1;
        public String email2;
        public String email1;
        public String fax2;
        public String fax1;
        public String billing_address_id;
        public String piAmount;
        public String nickName;
        public List<String> addressLine;
        public String piLanguage;
        public String phone1Type;
        public String piStatus;
        public String stateOrProvinceName;
        public String firstName;
        public String piCurrency;
        public List<ProtocolData> protocolData;


        /** ProtocolData is the inner class of PaymentInstruction */
        public class ProtocolData {
            public String name;
            public String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public String middleName;

        public String getPiId() {
            return piId;
        }

        public void setPiId(String piId) {
            this.piId = piId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPersonTitle() {
            return personTitle;
        }

        public void setPersonTitle(String personTitle) {
            this.personTitle = personTitle;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getPhone2() {
            return phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }

        public String getPhone2Publish() {
            return phone2Publish;
        }

        public void setPhone2Publish(String phone2Publish) {
            this.phone2Publish = phone2Publish;
        }

        public String getPhone1Publish() {
            return phone1Publish;
        }

        public void setPhone1Publish(String phone1Publish) {
            this.phone1Publish = phone1Publish;
        }

        public String getPiDescription() {
            return piDescription;
        }

        public void setPiDescription(String piDescription) {
            this.piDescription = piDescription;
        }

        public String getPayMethodId() {
            return payMethodId;
        }

        public void setPayMethodId(String payMethodId) {
            this.payMethodId = payMethodId;
        }

        public String getPhone1() {
            return phone1;
        }

        public void setPhone1(String phone1) {
            this.phone1 = phone1;
        }

        public String getEmail2() {
            return email2;
        }

        public void setEmail2(String email2) {
            this.email2 = email2;
        }

        public String getEmail1() {
            return email1;
        }

        public void setEmail1(String email1) {
            this.email1 = email1;
        }

        public String getFax2() {
            return fax2;
        }

        public void setFax2(String fax2) {
            this.fax2 = fax2;
        }

        public String getFax1() {
            return fax1;
        }

        public void setFax1(String fax1) {
            this.fax1 = fax1;
        }

        public String getBilling_address_id() {
            return billing_address_id;
        }

        public void setBilling_address_id(String billing_address_id) {
            this.billing_address_id = billing_address_id;
        }

        public String getPiAmount() {
            return piAmount;
        }

        public void setPiAmount(String piAmount) {
            this.piAmount = piAmount;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public List<String> getAddressLine() {
            return addressLine;
        }

        public void setAddressLine(List<String> addressLine) {
            this.addressLine = addressLine;
        }

        public String getPiLanguage() {
            return piLanguage;
        }

        public void setPiLanguage(String piLanguage) {
            this.piLanguage = piLanguage;
        }

        public String getPhone1Type() {
            return phone1Type;
        }

        public void setPhone1Type(String phone1Type) {
            this.phone1Type = phone1Type;
        }

        public String getPiStatus() {
            return piStatus;
        }

        public void setPiStatus(String piStatus) {
            this.piStatus = piStatus;
        }

        public String getStateOrProvinceName() {
            return stateOrProvinceName;
        }

        public void setStateOrProvinceName(String stateOrProvinceName) {
            this.stateOrProvinceName = stateOrProvinceName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getPiCurrency() {
            return piCurrency;
        }

        public void setPiCurrency(String piCurrency) {
            this.piCurrency = piCurrency;
        }

        public List<ProtocolData> getProtocolData() {
            return protocolData;
        }

        public void setProtocolData(List<ProtocolData> protocolData) {
            this.protocolData = protocolData;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }
    }

    public String estimatedTaxCurrency;
    public String totalProductPriceCurrency;
    public String totalProductPrice;
    public String savings;
    public String trackEmail;
    public String totalAdjustmentCurrency;
    public String totalSalesTaxCurrency;
    public List<Shipment> shipment;

    /** Shipment is the inner class of TargetCartOrderReviewResponse */
    public class Shipment {
        public String country;
        public String lastName;
        public String zipCode;
        public String shipmentQuantity;
        public String shipModeCode;
        public String city;
        public List<OrderItem> orderItem;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getShipmentQuantity() {
            return shipmentQuantity;
        }

        public void setShipmentQuantity(String shipmentQuantity) {
            this.shipmentQuantity = shipmentQuantity;
        }

        public String getShipModeCode() {
            return shipModeCode;
        }

        public void setShipModeCode(String shipModeCode) {
            this.shipModeCode = shipModeCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<OrderItem> getOrderItem() {
            return orderItem;
        }

        public void setOrderItem(List<OrderItem> orderItem) {
            this.orderItem = orderItem;
        }

        public String getShipModeDescription() {
            return shipModeDescription;
        }

        public void setShipModeDescription(String shipModeDescription) {
            this.shipModeDescription = shipModeDescription;
        }

        public String getExpectedDeliveryDate() {
            return expectedDeliveryDate;
        }

        public void setExpectedDeliveryDate(String expectedDeliveryDate) {
            this.expectedDeliveryDate = expectedDeliveryDate;
        }

        public String getShipModeId() {
            return shipModeId;
        }

        public void setShipModeId(String shipModeId) {
            this.shipModeId = shipModeId;
        }

        public List<String> getAddressLine() {
            return addressLine;
        }

        public void setAddressLine(List<String> addressLine) {
            this.addressLine = addressLine;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getStateOrProvinceName() {
            return stateOrProvinceName;
        }

        public void setStateOrProvinceName(String stateOrProvinceName) {
            this.stateOrProvinceName = stateOrProvinceName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getSignatureRequired() {
            return signatureRequired;
        }

        public void setSignatureRequired(String signatureRequired) {
            this.signatureRequired = signatureRequired;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getShipmentNumber() {
            return shipmentNumber;
        }

        public void setShipmentNumber(String shipmentNumber) {
            this.shipmentNumber = shipmentNumber;
        }

        /** OrderItem is the inner class of Shipment */
        public class OrderItem {
            public List<Attachments> attachments;

            /** Attachments is the inner class of OrderItem */
            public class Attachments {
                public String path;
                public String usage;

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }

                public String getUsage() {
                    return usage;
                }

                public void setUsage(String usage) {
                    this.usage = usage;
                }
            }

            public String quantity;
            public List<ItemAttributes> itemAttributes;

            /** ItemAttributes is the inner class of OrderItem */
            public class ItemAttributes {
                public String attrValue;
                public String attrName;

                public String getAttrValue() {
                    return attrValue;
                }

                public void setAttrValue(String attrValue) {
                    this.attrValue = attrValue;
                }

                public String getAttrName() {
                    return attrName;
                }

                public void setAttrName(String attrName) {
                    this.attrName = attrName;
                }
            }

            public String orderItemId;
            public List<Price> price;

            /** Price is the inner class of OrderItem */
            public class Price {
                public String priceUsage;
                public String priceValue;
                public String priceDescription;

                public String getPriceUsage() {
                    return priceUsage;
                }

                public void setPriceUsage(String priceUsage) {
                    this.priceUsage = priceUsage;
                }

                public String getPriceValue() {
                    return priceValue;
                }

                public void setPriceValue(String priceValue) {
                    this.priceValue = priceValue;
                }

                public String getPriceDescription() {
                    return priceDescription;
                }

                public void setPriceDescription(String priceDescription) {
                    this.priceDescription = priceDescription;
                }
            }

            public String inventoryStatus;
            public String name;
            public String catalogEntryId;
            public String isAO;
            public String currency;
            public String partNumber;
            public String orderItemPrice;

            public List<Attachments> getAttachments() {
                return attachments;
            }

            public void setAttachments(List<Attachments> attachments) {
                this.attachments = attachments;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public List<ItemAttributes> getItemAttributes() {
                return itemAttributes;
            }

            public void setItemAttributes(List<ItemAttributes> itemAttributes) {
                this.itemAttributes = itemAttributes;
            }

            public String getOrderItemId() {
                return orderItemId;
            }

            public void setOrderItemId(String orderItemId) {
                this.orderItemId = orderItemId;
            }

            public List<Price> getPrice() {
                return price;
            }

            public void setPrice(List<Price> price) {
                this.price = price;
            }

            public String getInventoryStatus() {
                return inventoryStatus;
            }

            public void setInventoryStatus(String inventoryStatus) {
                this.inventoryStatus = inventoryStatus;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCatalogEntryId() {
                return catalogEntryId;
            }

            public void setCatalogEntryId(String catalogEntryId) {
                this.catalogEntryId = catalogEntryId;
            }

            public String getIsAO() {
                return isAO;
            }

            public void setIsAO(String isAO) {
                this.isAO = isAO;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getPartNumber() {
                return partNumber;
            }

            public void setPartNumber(String partNumber) {
                this.partNumber = partNumber;
            }

            public String getOrderItemPrice() {
                return orderItemPrice;
            }

            public void setOrderItemPrice(String orderItemPrice) {
                this.orderItemPrice = orderItemPrice;
            }
        }

        public String shipModeDescription;
        public String expectedDeliveryDate;
        public String shipModeId;
        public List<String> addressLine;
        public String addressId;
        public String stateOrProvinceName;
        public String firstName;
        public String signatureRequired;
        public String middleName;
        public String shipmentNumber;

    }

    public String totalSalesTax;
    public String grandTotal;
    public String stateAndLocalFeeCurrency;
    public String totalShippingTax;
    public String stateAndLocalFee;
    public String totalShippingTaxCurrency;
    public String estimatedTax;
    public String giftWrapCharge;
    public String savingsPercent;
    public String savingsCurrency;
    public String totalAdjustment;

    public String getTotalShippingCharge() {
        return totalShippingCharge;
    }

    public void setTotalShippingCharge(String totalShippingCharge) {
        this.totalShippingCharge = totalShippingCharge;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGiftWrapChargeCurrency() {
        return giftWrapChargeCurrency;
    }

    public void setGiftWrapChargeCurrency(String giftWrapChargeCurrency) {
        this.giftWrapChargeCurrency = giftWrapChargeCurrency;
    }

    public String getIsFreeShipping() {
        return isFreeShipping;
    }

    public void setIsFreeShipping(String isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
    }

    public String getTotalShippingChargeCurrency() {
        return totalShippingChargeCurrency;
    }

    public void setTotalShippingChargeCurrency(String totalShippingChargeCurrency) {
        this.totalShippingChargeCurrency = totalShippingChargeCurrency;
    }

    public String getGrandTotalCurrency() {
        return grandTotalCurrency;
    }

    public void setGrandTotalCurrency(String grandTotalCurrency) {
        this.grandTotalCurrency = grandTotalCurrency;
    }

    public List<PaymentInstruction> getPaymentInstruction() {
        return paymentInstruction;
    }

    public void setPaymentInstruction(List<PaymentInstruction> paymentInstruction) {
        this.paymentInstruction = paymentInstruction;
    }

    public String getEstimatedTaxCurrency() {
        return estimatedTaxCurrency;
    }

    public void setEstimatedTaxCurrency(String estimatedTaxCurrency) {
        this.estimatedTaxCurrency = estimatedTaxCurrency;
    }

    public String getTotalProductPriceCurrency() {
        return totalProductPriceCurrency;
    }

    public void setTotalProductPriceCurrency(String totalProductPriceCurrency) {
        this.totalProductPriceCurrency = totalProductPriceCurrency;
    }

    public String getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(String totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }

    public String getTrackEmail() {
        return trackEmail;
    }

    public void setTrackEmail(String trackEmail) {
        this.trackEmail = trackEmail;
    }

    public String getTotalAdjustmentCurrency() {
        return totalAdjustmentCurrency;
    }

    public void setTotalAdjustmentCurrency(String totalAdjustmentCurrency) {
        this.totalAdjustmentCurrency = totalAdjustmentCurrency;
    }

    public String getTotalSalesTaxCurrency() {
        return totalSalesTaxCurrency;
    }

    public void setTotalSalesTaxCurrency(String totalSalesTaxCurrency) {
        this.totalSalesTaxCurrency = totalSalesTaxCurrency;
    }

    public List<Shipment> getShipment() {
        return shipment;
    }

    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
    }

    public String getTotalSalesTax() {
        return totalSalesTax;
    }

    public void setTotalSalesTax(String totalSalesTax) {
        this.totalSalesTax = totalSalesTax;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getStateAndLocalFeeCurrency() {
        return stateAndLocalFeeCurrency;
    }

    public void setStateAndLocalFeeCurrency(String stateAndLocalFeeCurrency) {
        this.stateAndLocalFeeCurrency = stateAndLocalFeeCurrency;
    }

    public String getTotalShippingTax() {
        return totalShippingTax;
    }

    public void setTotalShippingTax(String totalShippingTax) {
        this.totalShippingTax = totalShippingTax;
    }

    public String getStateAndLocalFee() {
        return stateAndLocalFee;
    }

    public void setStateAndLocalFee(String stateAndLocalFee) {
        this.stateAndLocalFee = stateAndLocalFee;
    }

    public String getTotalShippingTaxCurrency() {
        return totalShippingTaxCurrency;
    }

    public void setTotalShippingTaxCurrency(String totalShippingTaxCurrency) {
        this.totalShippingTaxCurrency = totalShippingTaxCurrency;
    }

    public String getEstimatedTax() {
        return estimatedTax;
    }

    public void setEstimatedTax(String estimatedTax) {
        this.estimatedTax = estimatedTax;
    }

    public String getGiftWrapCharge() {
        return giftWrapCharge;
    }

    public void setGiftWrapCharge(String giftWrapCharge) {
        this.giftWrapCharge = giftWrapCharge;
    }

    public String getSavingsPercent() {
        return savingsPercent;
    }

    public void setSavingsPercent(String savingsPercent) {
        this.savingsPercent = savingsPercent;
    }

    public String getSavingsCurrency() {
        return savingsCurrency;
    }

    public void setSavingsCurrency(String savingsCurrency) {
        this.savingsCurrency = savingsCurrency;
    }

    public String getTotalAdjustment() {
        return totalAdjustment;
    }

    public void setTotalAdjustment(String totalAdjustment) {
        this.totalAdjustment = totalAdjustment;
    }
}
