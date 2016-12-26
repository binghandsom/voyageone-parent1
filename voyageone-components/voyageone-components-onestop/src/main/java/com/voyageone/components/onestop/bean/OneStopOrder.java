package com.voyageone.components.onestop.bean;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: zhen.wang
 * @date: 2016/11/22 13:06
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopOrder {

    /*
     *orderId: 0,
     *PartnerOrderId: string
     *Customer: {"FirstName": "string","LastName": "string","EmailAddress": "string"}
     *OrderOrigin: string
     *Referrer: string
     *AssociateId: "string
     *ShipChoice: string
     *OrderDate": 2016-11-21T01:50:44.461Z
     *EstimatedShipDate: 2016-11-21T01:50:44.461Z
     *Addresses: [{"AddressLine1": "string","AddressLine2": "string","City": "string","CountryIsoCode": "string","CountryIsoCode3": "string","CountryCode": "string","CountryName": "string","Email": "string","FirstName": "string","LastName": "string","PhoneNumber": "string","PostalCode": "string","State": "string","AddressType": "string"}]
     *Totals: {"GrandTotal": 0,"SubTotal": 0,"TaxTotal": 0,"DiscountTotal": 0,"ShippingTotal": 0,"ShippingTax": 0,"ShippingDiscount": 0,"ProductTax": 0,"ProductDiscount": 0}
     *OrderItems: [{"Upc": "string","ModelName": "string","ColorName": "string","ColorSku": "string","SizeName": "string","SizeSku": "string","ItemCost": 0,"ItemDiscount": 0,"ItemTax": [{"TaxAmount": 0,"Jurisdiction": "string","TaxCode": "string"}],"ItemTotal": 0,"IsReturnable": true,"ShipmentId": 0,"FulfillmentType": "string","FacilityId": 0,"ProductId": 0,"ItemStatus": "string","DisplayStatus": "string","IsShipping": true,"IsRefunded": true,"RefundId": 0,"CustomData": {},"IsReturned": true,"PendingRmaNumber": 0,"RefundIds": [0]}]
     *PaymentInfo: {"FullName": "string","CreditCardNumber": "string","CreditCardCin": "string","ExpirationMonth": "string","ExpirationYear": "string","PaymentType": "string","PaymentToken": "string","PaymentId": "string","TransactionId": "string","CreditCardLast4": "string"}
     *GiftMessage: {"FromName": "string","ToName": "string","GiftMessage": "string","GiftBox": true}
     *GiftCards: [{"GiftCardNumber": "string","AppliedAmount": 0}]
     *GiftCertificates: [{"GiftCertificateNumber": "string","AppliedAmount": 0}]
     *IpAddress: string,
     *OrderStatus: string
     *DisplayStatus: string
     *OrderSource: string
     *Shipments: [{"ShipmentId": 0,"ShipDate": "2016-11-21T01:50:44.462Z","CarrierName": "string","ServiceClass": "string","TrackingInfo": {"TrackingNumber": "string","ReturnTrackingNumber": "string"},"Transactions": [{"Amount": 0,"TransactionId": "string","ReferenceNumber": "string","PaymentType": "string","LastFour": "string","TransactionType": "string"}]}]
     *Refunds: [{"RefundId": 0,"RmaNumber": 0,"RefundReason": "string","RefundDate": "2016-11-21T01:50:44.462Z","Transactions": [{"Amount": 0,"TransactionId": "string","ReferenceNumber": "string","PaymentType": "string","LastFour": "string","TransactionType": "string"}]}]
     *PromoCode: string
     *Transactions: [{"Amount": 0,"TransactionId": "string","ReferenceNumber": "string","PaymentType": "string","LastFour": "string","TransactionType": "string"}]
     *LastModified: 2016-11-21T01:50:44.462Z
     *FulfillmentType: string
     *IsSignatureRequired": true
     *IsInternationalExchange: true
     *Prefix: string
     *ReturnWindow: 0
     *CartRecordId: 0
     * */


    private long OrderId;

    private String PartnerOrderId;

    private Customer Customer;

    private String OrderOrigin;

    private String Referrer;

    private String AssociateId;

    private String ShipChoice;

    private Date OrderDate;

    private Date EstimatedShipDate;

    private List<Addresses> Addresses;

    private Totals Totals;

    private List<OrderItems> OrderItems;

    private PaymentInfo PaymentInfo;

    private GiftMessage GiftMessage;

    private List<GiftCards> GiftCards;

    private List<GiftCertificates> GiftCertificates;

    private String IpAddress;

    private String OrderStatus;

    private String DisplayStatus;

    private String OrderSource;

    private List<Shipments> Shipments;

    private List<Refunds> Refunds;

    private String PromoCode;

    private List<Transactions> Transactions;

    private Date LastModified;

    private String FulfillmentType;

    private boolean IsSignatureRequired;

    private boolean IsInternationalExchange;

    private String Prefix;

    private int ReturnWindow;

    private int CartRecordId;

    public void setOrderId(long OrderId) {
        this.OrderId = OrderId;
    }

    public long getOrderId() {
        return this.OrderId;
    }

    public void setPartnerOrderId(String PartnerOrderId) {
        this.PartnerOrderId = PartnerOrderId;
    }

    public String getPartnerOrderId() {
        return this.PartnerOrderId;
    }

    public void setCustomer(Customer Customer) {
        this.Customer = Customer;
    }

    public Customer getCustomer() {
        return this.Customer;
    }

    public void setOrderOrigin(String OrderOrigin) {
        this.OrderOrigin = OrderOrigin;
    }

    public String getOrderOrigin() {
        return this.OrderOrigin;
    }

    public void setReferrer(String Referrer) {
        this.Referrer = Referrer;
    }

    public String getReferrer() {
        return this.Referrer;
    }

    public void setAssociateId(String AssociateId) {
        this.AssociateId = AssociateId;
    }

    public String getAssociateId() {
        return this.AssociateId;
    }

    public void setShipChoice(String ShipChoice) {
        this.ShipChoice = ShipChoice;
    }

    public String getShipChoice() {
        return this.ShipChoice;
    }

    public void setOrderDate(Date OrderDate) {
        this.OrderDate = OrderDate;
    }

    public Date getOrderDate() {
        return this.OrderDate;
    }

    public void setEstimatedShipDate(Date EstimatedShipDate) {
        this.EstimatedShipDate = EstimatedShipDate;
    }

    public Date getEstimatedShipDate() {
        return this.EstimatedShipDate;
    }

    public void setAddresses(List<Addresses> Addresses) {
        this.Addresses = Addresses;
    }

    public List<Addresses> getAddresses() {
        return this.Addresses;
    }

    public void setTotals(Totals Totals) {
        this.Totals = Totals;
    }

    public Totals getTotals() {
        return this.Totals;
    }

    public void setOrderItems(List<OrderItems> OrderItems) {
        this.OrderItems = OrderItems;
    }

    public List<OrderItems> getOrderItems() {
        return this.OrderItems;
    }

    public void setPaymentInfo(PaymentInfo PaymentInfo) {
        this.PaymentInfo = PaymentInfo;
    }

    public PaymentInfo getPaymentInfo() {
        return this.PaymentInfo;
    }

    public void setGiftMessage(GiftMessage GiftMessage) {
        this.GiftMessage = GiftMessage;
    }

    public GiftMessage getGiftMessage() {
        return this.GiftMessage;
    }

    public void setGiftCards(List<GiftCards> GiftCards) {
        this.GiftCards = GiftCards;
    }

    public List<GiftCards> getGiftCards() {
        return this.GiftCards;
    }

    public void setGiftCertificates(List<GiftCertificates> GiftCertificates) {
        this.GiftCertificates = GiftCertificates;
    }

    public List<GiftCertificates> getGiftCertificates() {
        return this.GiftCertificates;
    }

    public void setIpAddress(String IpAddress) {
        this.IpAddress = IpAddress;
    }

    public String getIpAddress() {
        return this.IpAddress;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public String getOrderStatus() {
        return this.OrderStatus;
    }

    public void setDisplayStatus(String DisplayStatus) {
        this.DisplayStatus = DisplayStatus;
    }

    public String getDisplayStatus() {
        return this.DisplayStatus;
    }

    public void setOrderSource(String OrderSource) {
        this.OrderSource = OrderSource;
    }

    public String getOrderSource() {
        return this.OrderSource;
    }

    public void setShipments(List<Shipments> Shipments) {
        this.Shipments = Shipments;
    }

    public List<Shipments> getShipments() {
        return this.Shipments;
    }

    public void setRefunds(List<Refunds> Refunds) {
        this.Refunds = Refunds;
    }

    public List<Refunds> getRefunds() {
        return this.Refunds;
    }

    public void setPromoCode(String PromoCode) {
        this.PromoCode = PromoCode;
    }

    public String getPromoCode() {
        return this.PromoCode;
    }

    public void setTransactions(List<Transactions> Transactions) {
        this.Transactions = Transactions;
    }

    public List<Transactions> getTransactions() {
        return this.Transactions;
    }

    public void setLastModified(Date LastModified) {
        this.LastModified = LastModified;
    }

    public Date getLastModified() {
        return this.LastModified;
    }

    public void setFulfillmentType(String FulfillmentType) {
        this.FulfillmentType = FulfillmentType;
    }

    public String getFulfillmentType() {
        return this.FulfillmentType;
    }

    public void setIsSignatureRequired(boolean IsSignatureRequired) {
        this.IsSignatureRequired = IsSignatureRequired;
    }

    public boolean getIsSignatureRequired() {
        return this.IsSignatureRequired;
    }

    public void setIsInternationalExchange(boolean IsInternationalExchange) {
        this.IsInternationalExchange = IsInternationalExchange;
    }

    public boolean getIsInternationalExchange() {
        return this.IsInternationalExchange;
    }

    public void setPrefix(String Prefix) {
        this.Prefix = Prefix;
    }

    public String getPrefix() {
        return this.Prefix;
    }

    public void setReturnWindow(int ReturnWindow) {
        this.ReturnWindow = ReturnWindow;
    }

    public int getReturnWindow() {
        return this.ReturnWindow;
    }

    public void setCartRecordId(int CartRecordId) {
        this.CartRecordId = CartRecordId;
    }

    public int getCartRecordId() {
        return this.CartRecordId;
    }

    public static class Customer {
        /*
        * FirstName: string
        * LastName: string
        * EmailAddress: string
        * */

        private String FirstName;

        private String LastName;

        private String EmailAddress;

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public String getFirstName() {
            return this.FirstName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public String getLastName() {
            return this.LastName;
        }

        public void setEmailAddress(String EmailAddress) {
            this.EmailAddress = EmailAddress;
        }

        public String getEmailAddress() {
            return this.EmailAddress;
        }

    }

    public static class Addresses {
        /*
        * AddressLine1: string
        * AddressLine2: string
        * City: string
        * CountryIsoCode: string
        * CountryIsoCode3: string
        * CountryCode: string
        * CountryName: string
        * Email: string
        * FirstName: string
        * LastName: string
        * PhoneNumber: string
        * PostalCode: string
        * State: string
        * AddressType: string
        *
        * */
        private String AddressLine1;

        private String AddressLine2;

        private String City;

        private String CountryIsoCode;

        private String CountryIsoCode3;

        private String CountryCode;

        private String CountryName;

        private String Email;

        private String FirstName;

        private String LastName;

        private String PhoneNumber;

        private String PostalCode;

        private String State;

        private String AddressType;

        public void setAddressLine1(String AddressLine1) {
            this.AddressLine1 = AddressLine1;
        }

        public String getAddressLine1() {
            return this.AddressLine1;
        }

        public void setAddressLine2(String AddressLine2) {
            this.AddressLine2 = AddressLine2;
        }

        public String getAddressLine2() {
            return this.AddressLine2;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getCity() {
            return this.City;
        }

        public void setCountryIsoCode(String CountryIsoCode) {
            this.CountryIsoCode = CountryIsoCode;
        }

        public String getCountryIsoCode() {
            return this.CountryIsoCode;
        }

        public void setCountryIsoCode3(String CountryIsoCode3) {
            this.CountryIsoCode3 = CountryIsoCode3;
        }

        public String getCountryIsoCode3() {
            return this.CountryIsoCode3;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }

        public String getCountryCode() {
            return this.CountryCode;
        }

        public void setCountryName(String CountryName) {
            this.CountryName = CountryName;
        }

        public String getCountryName() {
            return this.CountryName;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getEmail() {
            return this.Email;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public String getFirstName() {
            return this.FirstName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public String getLastName() {
            return this.LastName;
        }

        public void setPhoneNumber(String PhoneNumber) {
            this.PhoneNumber = PhoneNumber;
        }

        public String getPhoneNumber() {
            return this.PhoneNumber;
        }

        public void setPostalCode(String PostalCode) {
            this.PostalCode = PostalCode;
        }

        public String getPostalCode() {
            return this.PostalCode;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getState() {
            return this.State;
        }

        public void setAddressType(String AddressType) {
            this.AddressType = AddressType;
        }

        public String getAddressType() {
            return this.AddressType;
        }

    }

    public class Totals {
        /*
        * GrandTotal: 0
        * SubTotal: 0
        * TaxTotal: 0
        * DiscountTotal: 0
        * ShippingTotal: 0
        * ShippingTax: 0
        * ShippingDiscount: 0
        * ProductTax: 0
        * ProductDiscount: 0
        *
        * */
        private double GrandTotal;

        private double SubTotal;

        private double TaxTotal;

        private int DiscountTotal;

        private double ShippingTotal;

        private double ShippingTax;

        private int ShippingDiscount;

        private double ProductTax;

        private int ProductDiscount;

        public void setSubTotal(double SubTotal) {
            this.SubTotal = SubTotal;
        }

        public double getGrandTotal() {
            return GrandTotal;
        }

        public double getTaxTotal() {
            return TaxTotal;
        }

        public double getShippingTotal() {
            return ShippingTotal;
        }

        public double getProductTax() {
            return ProductTax;
        }

        public double getShippingTax() {
            return ShippingTax;
        }

        public double getSubTotal() {
            return this.SubTotal;

        }

        public void setGrandTotal(double grandTotal) {
            GrandTotal = grandTotal;
        }

        public void setTaxTotal(double taxTotal) {
            TaxTotal = taxTotal;
        }

        public void setShippingTotal(double shippingTotal) {
            ShippingTotal = shippingTotal;
        }

        public void setShippingTax(double shippingTax) {
            ShippingTax = shippingTax;
        }

        public void setProductTax(double productTax) {
            ProductTax = productTax;
        }

        public void setDiscountTotal(int DiscountTotal) {
            this.DiscountTotal = DiscountTotal;
        }

        public int getDiscountTotal() {
            return this.DiscountTotal;
        }



        public void setShippingDiscount(int ShippingDiscount) {
            this.ShippingDiscount = ShippingDiscount;
        }

        public int getShippingDiscount() {
            return this.ShippingDiscount;
        }


        public void setProductDiscount(int ProductDiscount) {
            this.ProductDiscount = ProductDiscount;
        }

        public int getProductDiscount() {
            return this.ProductDiscount;
        }

    }

    public static class OrderItems {

        /*
        * Upc: string
        * ModelName: string
        * ColorName: string
        * ColorSku: string
        * SizeName: string
        * SizeSku: string
        * ItemCost: 0
        * ItemDiscount: 0
        * ItemTax: [{"TaxAmount": 0,"Jurisdiction": "string","TaxCode": "string"}]
        * ItemTotal: 0
        * IsReturnable: true
        * ShipmentId: 0
        * FulfillmentType: string
        * FacilityId: 0
        * ProductId: 0
        * ItemStatus: string
        * DisplayStatus: string
        * IsShipping: true
        * IsRefunded: true
        * RefundId: 0
        * CustomData: {}
        * IsReturned: true
        * PendingRmaNumber: 0
        * RefundIds: [0]
        * */
        private String Upc;

        private String ModelName;

        private String ColorName;

        private String ColorSku;

        private String SizeName;

        private String SizeSku;

        private double ItemCost;

        private int ItemDiscount;

        private List<ItemTax> ItemTax;

        private double ItemTotal;

        private boolean IsReturnable;

        private int ShipmentId;

        private String FulfillmentType;

        private int FacilityId;

        private int ProductId;

        private String ItemStatus;

        private String DisplayStatus;

        private boolean IsShipping;

        private boolean IsRefunded;

        private int RefundId;

        private Map<String,String> CustomData;

        private boolean IsReturned;

        private int PendingRmaNumber;

        private List<Integer> RefundIds;

        public void setUpc(String Upc) {
            this.Upc = Upc;
        }

        public String getUpc() {
            return this.Upc;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
        }

        public String getModelName() {
            return this.ModelName;
        }

        public void setColorName(String ColorName) {
            this.ColorName = ColorName;
        }

        public String getColorName() {
            return this.ColorName;
        }

        public void setColorSku(String ColorSku) {
            this.ColorSku = ColorSku;
        }

        public String getColorSku() {
            return this.ColorSku;
        }

        public void setSizeName(String SizeName) {
            this.SizeName = SizeName;
        }

        public String getSizeName() {
            return this.SizeName;
        }

        public void setSizeSku(String SizeSku) {
            this.SizeSku = SizeSku;
        }

        public String getSizeSku() {
            return this.SizeSku;
        }

        public void setItemCost(double ItemCost) {
            this.ItemCost = ItemCost;
        }

        public double getItemCost() {
            return this.ItemCost;
        }

        public void setItemDiscount(int ItemDiscount) {
            this.ItemDiscount = ItemDiscount;
        }

        public int getItemDiscount() {
            return this.ItemDiscount;
        }

        public void setItemTax(List<ItemTax> ItemTax) {
            this.ItemTax = ItemTax;
        }

        public List<ItemTax> getItemTax() {
            return this.ItemTax;
        }

        public void setItemTotal(double ItemTotal) {
            this.ItemTotal = ItemTotal;
        }

        public double getItemTotal() {
            return this.ItemTotal;
        }

        public void setIsReturnable(boolean IsReturnable) {
            this.IsReturnable = IsReturnable;
        }

        public boolean getIsReturnable() {
            return this.IsReturnable;
        }

        public void setShipmentId(int ShipmentId) {
            this.ShipmentId = ShipmentId;
        }

        public int getShipmentId() {
            return this.ShipmentId;
        }

        public void setFulfillmentType(String FulfillmentType) {
            this.FulfillmentType = FulfillmentType;
        }

        public String getFulfillmentType() {
            return this.FulfillmentType;
        }

        public void setFacilityId(int FacilityId) {
            this.FacilityId = FacilityId;
        }

        public int getFacilityId() {
            return this.FacilityId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public int getProductId() {
            return this.ProductId;
        }

        public void setItemStatus(String ItemStatus) {
            this.ItemStatus = ItemStatus;
        }

        public String getItemStatus() {
            return this.ItemStatus;
        }

        public void setDisplayStatus(String DisplayStatus) {
            this.DisplayStatus = DisplayStatus;
        }

        public String getDisplayStatus() {
            return this.DisplayStatus;
        }

        public void setIsShipping(boolean IsShipping) {
            this.IsShipping = IsShipping;
        }

        public boolean getIsShipping() {
            return this.IsShipping;
        }

        public void setIsRefunded(boolean IsRefunded) {
            this.IsRefunded = IsRefunded;
        }

        public boolean getIsRefunded() {
            return this.IsRefunded;
        }

        public void setRefundId(int RefundId) {
            this.RefundId = RefundId;
        }

        public int getRefundId() {
            return this.RefundId;
        }

        public void setCustomData(Map<String,String> CustomData) {
            this.CustomData = CustomData;
        }

        public Map<String,String> getCustomData() {
            return this.CustomData;
        }

        public void setIsReturned(boolean IsReturned) {
            this.IsReturned = IsReturned;
        }

        public boolean getIsReturned() {
            return this.IsReturned;
        }

        public void setPendingRmaNumber(int PendingRmaNumber) {
            this.PendingRmaNumber = PendingRmaNumber;
        }

        public int getPendingRmaNumber() {
            return this.PendingRmaNumber;
        }

        public void setRefundIds(List<Integer> RefundIds) {
            this.RefundIds = RefundIds;
        }

        public List<Integer> getRefundIds() {
            return this.RefundIds;
        }

        public class ItemTax {
            /*
            * TaxAmount: 0
            * Jurisdiction: string
            * TaxCode: string
            * */
            private double TaxAmount;

            private String Jurisdiction;

            private String TaxCode;

            public double getTaxAmount() {
                return TaxAmount;
            }

            public void setTaxAmount(double taxAmount) {
                TaxAmount = taxAmount;
            }


            public void setJurisdiction(String Jurisdiction) {
                this.Jurisdiction = Jurisdiction;
            }

            public String getJurisdiction() {
                return this.Jurisdiction;
            }

            public void setTaxCode(String TaxCode) {
                this.TaxCode = TaxCode;
            }

            public String getTaxCode() {
                return this.TaxCode;
            }

        }


    }

    public static class PaymentInfo {
        /*
        *FullName: string
        *CreditCardNumber: string
        *CreditCardCin: string
        *ExpirationMonth: string
        *ExpirationYear: string
        *PaymentType: string
        *PaymentToken: string
        *PaymentId: string
        *TransactionId: string
        *CreditCardLast4: string
        * */
        private String FullName;

        private String CreditCardNumber;

        private String CreditCardCin;

        private String ExpirationMonth;

        private String ExpirationYear;

        private String PaymentType;

        private String PaymentToken;

        private String PaymentId;

        private String TransactionId;

        private String CreditCardLast4;

        public void setFullName(String FullName) {
            this.FullName = FullName;
        }

        public String getFullName() {
            return this.FullName;
        }

        public void setCreditCardNumber(String CreditCardNumber) {
            this.CreditCardNumber = CreditCardNumber;
        }

        public String getCreditCardNumber() {
            return this.CreditCardNumber;
        }

        public void setCreditCardCin(String CreditCardCin) {
            this.CreditCardCin = CreditCardCin;
        }

        public String getCreditCardCin() {
            return this.CreditCardCin;
        }

        public void setExpirationMonth(String ExpirationMonth) {
            this.ExpirationMonth = ExpirationMonth;
        }

        public String getExpirationMonth() {
            return this.ExpirationMonth;
        }

        public void setExpirationYear(String ExpirationYear) {
            this.ExpirationYear = ExpirationYear;
        }

        public String getExpirationYear() {
            return this.ExpirationYear;
        }

        public void setPaymentType(String PaymentType) {
            this.PaymentType = PaymentType;
        }

        public String getPaymentType() {
            return this.PaymentType;
        }

        public void setPaymentToken(String PaymentToken) {
            this.PaymentToken = PaymentToken;
        }

        public String getPaymentToken() {
            return this.PaymentToken;
        }

        public void setPaymentId(String PaymentId) {
            this.PaymentId = PaymentId;
        }

        public String getPaymentId() {
            return this.PaymentId;
        }

        public void setTransactionId(String TransactionId) {
            this.TransactionId = TransactionId;
        }

        public String getTransactionId() {
            return this.TransactionId;
        }

        public void setCreditCardLast4(String CreditCardLast4) {
            this.CreditCardLast4 = CreditCardLast4;
        }

        public String getCreditCardLast4() {
            return this.CreditCardLast4;
        }

    }

    public class GiftMessage {

        /*
        *FromName: string
        *ToName: string
        *GiftMessage: string
        *GiftBox: true
        * */
        private String FromName;

        private String ToName;

        private String GiftMessage;

        private boolean GiftBox;

        public void setFromName(String FromName) {
            this.FromName = FromName;
        }

        public String getFromName() {
            return this.FromName;
        }

        public void setToName(String ToName) {
            this.ToName = ToName;
        }

        public String getToName() {
            return this.ToName;
        }

        public void setGiftMessage(String GiftMessage) {
            this.GiftMessage = GiftMessage;
        }

        public String getGiftMessage() {
            return this.GiftMessage;
        }

        public void setGiftBox(boolean GiftBox) {
            this.GiftBox = GiftBox;
        }

        public boolean getGiftBox() {
            return this.GiftBox;
        }

    }

    public class GiftCards {

        /*
        * GiftCardNumber: string
        * AppliedAmount: 0
        * */
        private String GiftCardNumber;

        private int AppliedAmount;

        public void setGiftCardNumber(String GiftCardNumber) {
            this.GiftCardNumber = GiftCardNumber;
        }

        public String getGiftCardNumber() {
            return this.GiftCardNumber;
        }

        public void setAppliedAmount(int AppliedAmount) {
            this.AppliedAmount = AppliedAmount;
        }

        public int getAppliedAmount() {
            return this.AppliedAmount;
        }

    }

    public class GiftCertificates {
        /*
        * GiftCertificateNumber: string
        * AppliedAmount: 0
        * */
        private String GiftCertificateNumber;

        private int AppliedAmount;

        public void setGiftCertificateNumber(String GiftCertificateNumber) {
            this.GiftCertificateNumber = GiftCertificateNumber;
        }

        public String getGiftCertificateNumber() {
            return this.GiftCertificateNumber;
        }

        public void setAppliedAmount(int AppliedAmount) {
            this.AppliedAmount = AppliedAmount;
        }

        public int getAppliedAmount() {
            return this.AppliedAmount;
        }

    }

    public class Shipments {

        /*
        * ShipmentId: 0
        * ShipDate: 2016-11-21T01:50:44.462Z
        * CarrierName: string
        * ServiceClass: string
        * TrackingInfo: {"TrackingNumber": "string","ReturnTrackingNumber": "string"}
        * Transactions: [{"Amount": 0,"TransactionId": "string","ReferenceNumber": "string","PaymentType": "string","LastFour": "string","TransactionType": "string"}]
        * */
        private int ShipmentId;

        private Date ShipDate;

        private String CarrierName;

        private String ServiceClass;

        private TrackingInfo TrackingInfo;

        private List<Transactions> Transactions;

        public void setShipmentId(int ShipmentId) {
            this.ShipmentId = ShipmentId;
        }

        public int getShipmentId() {
            return this.ShipmentId;
        }

        public void setShipDate(Date ShipDate) {
            this.ShipDate = ShipDate;
        }

        public Date getShipDate() {
            return this.ShipDate;
        }

        public void setCarrierName(String CarrierName) {
            this.CarrierName = CarrierName;
        }

        public String getCarrierName() {
            return this.CarrierName;
        }

        public void setServiceClass(String ServiceClass) {
            this.ServiceClass = ServiceClass;
        }

        public String getServiceClass() {
            return this.ServiceClass;
        }

        public void setTrackingInfo(TrackingInfo TrackingInfo) {
            this.TrackingInfo = TrackingInfo;
        }

        public TrackingInfo getTrackingInfo() {
            return this.TrackingInfo;
        }

        public void setTransactions(List<Transactions> Transactions) {
            this.Transactions = Transactions;
        }

        public List<Transactions> getTransactions() {
            return this.Transactions;
        }

        public class TrackingInfo {
            /*
            * TrackingNumber: string
            * ReturnTrackingNumber: string
            * */
            private String TrackingNumber;

            private String ReturnTrackingNumber;

            public void setTrackingNumber(String TrackingNumber) {
                this.TrackingNumber = TrackingNumber;
            }

            public String getTrackingNumber() {
                return this.TrackingNumber;
            }

            public void setReturnTrackingNumber(String ReturnTrackingNumber) {
                this.ReturnTrackingNumber = ReturnTrackingNumber;
            }

            public String getReturnTrackingNumber() {
                return this.ReturnTrackingNumber;
            }

        }

        public class Transactions {

            /*
            *Amount: 0
            *TransactionId: string
            *ReferenceNumber: string
            *PaymentType: string
            *LastFour: string
            *TransactionType: string
            * */
            private double Amount;

            private String TransactionId;

            private String ReferenceNumber;

            private String PaymentType;

            private String LastFour;

            private String TransactionType;

            public double getAmount() {
                return Amount;
            }

            public void setAmount(double amount) {
                Amount = amount;
            }

            public void setTransactionId(String TransactionId) {
                this.TransactionId = TransactionId;
            }

            public String getTransactionId() {
                return this.TransactionId;
            }

            public void setReferenceNumber(String ReferenceNumber) {
                this.ReferenceNumber = ReferenceNumber;
            }

            public String getReferenceNumber() {
                return this.ReferenceNumber;
            }

            public void setPaymentType(String PaymentType) {
                this.PaymentType = PaymentType;
            }

            public String getPaymentType() {
                return this.PaymentType;
            }

            public void setLastFour(String LastFour) {
                this.LastFour = LastFour;
            }

            public String getLastFour() {
                return this.LastFour;
            }

            public void setTransactionType(String TransactionType) {
                this.TransactionType = TransactionType;
            }

            public String getTransactionType() {
                return this.TransactionType;
            }

        }
    }

    public class Refunds {
        /*
        * RefundId: 0
        * RmaNumber: 0
        * RefundReason: string
        * RefundDate: 2016-11-21T01:50:44.462Z
        * Transactions: [{"Amount": 0,"TransactionId": "string","ReferenceNumber": "string","PaymentType": "string","LastFour": "string","TransactionType": "string"}]
        * */
        private int RefundId;

        private int RmaNumber;

        private String RefundReason;

        private Date RefundDate;

        private List<Transactions> Transactions;

        public void setRefundId(int RefundId) {
            this.RefundId = RefundId;
        }

        public int getRefundId() {
            return this.RefundId;
        }

        public void setRmaNumber(int RmaNumber) {
            this.RmaNumber = RmaNumber;
        }

        public int getRmaNumber() {
            return this.RmaNumber;
        }

        public void setRefundReason(String RefundReason) {
            this.RefundReason = RefundReason;
        }

        public String getRefundReason() {
            return this.RefundReason;
        }

        public void setRefundDate(Date RefundDate) {
            this.RefundDate = RefundDate;
        }

        public Date getRefundDate() {
            return this.RefundDate;
        }

        public void setTransactions(List<Transactions> Transactions) {
            this.Transactions = Transactions;
        }

        public List<Transactions> getTransactions() {
            return this.Transactions;
        }

        public class Transactions {

            /*
            *Amount: 0
            *TransactionId: string
            *ReferenceNumber: string
            *PaymentType: string
            *LastFour: string
            *TransactionType: string
            * */
            private double Amount;

            private String TransactionId;

            private String ReferenceNumber;

            private String PaymentType;

            private String LastFour;

            private String TransactionType;

            public double getAmount() {
                return Amount;
            }

            public void setAmount(double amount) {
                Amount = amount;
            }

            public void setTransactionId(String TransactionId) {
                this.TransactionId = TransactionId;
            }

            public String getTransactionId() {
                return this.TransactionId;
            }

            public void setReferenceNumber(String ReferenceNumber) {
                this.ReferenceNumber = ReferenceNumber;
            }

            public String getReferenceNumber() {
                return this.ReferenceNumber;
            }

            public void setPaymentType(String PaymentType) {
                this.PaymentType = PaymentType;
            }

            public String getPaymentType() {
                return this.PaymentType;
            }

            public void setLastFour(String LastFour) {
                this.LastFour = LastFour;
            }

            public String getLastFour() {
                return this.LastFour;
            }

            public void setTransactionType(String TransactionType) {
                this.TransactionType = TransactionType;
            }

            public String getTransactionType() {
                return this.TransactionType;
            }

        }
    }

    public class Transactions {
        /*
        *Amount: 0
        *TransactionId: string
        *ReferenceNumber: string
        *PaymentType: string
        *LastFour: string
        *TransactionType: string
        * */


        private double Amount;

        private String TransactionId;

        private String ReferenceNumber;

        private String PaymentType;

        private String LastFour;

        private String TransactionType;

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }

        public void setTransactionId(String TransactionId) {
            this.TransactionId = TransactionId;
        }

        public String getTransactionId() {
            return this.TransactionId;
        }

        public void setReferenceNumber(String ReferenceNumber) {
            this.ReferenceNumber = ReferenceNumber;
        }

        public String getReferenceNumber() {
            return this.ReferenceNumber;
        }

        public void setPaymentType(String PaymentType) {
            this.PaymentType = PaymentType;
        }

        public String getPaymentType() {
            return this.PaymentType;
        }

        public void setLastFour(String LastFour) {
            this.LastFour = LastFour;
        }

        public String getLastFour() {
            return this.LastFour;
        }

        public void setTransactionType(String TransactionType) {
            this.TransactionType = TransactionType;
        }

        public String getTransactionType() {
            return this.TransactionType;
        }

    }
    @Override
    public String toString() {
        return "OneStopOrder{" +
                "orderId=" + OrderId +
                ", PartnerOrderId='" + PartnerOrderId + '\'' +
                ", Customer=" + Customer +
                ", OrderOrigin='" + OrderOrigin + '\'' +
                ", Referrer='" + Referrer + '\'' +
                ", AssociateId='" + AssociateId + '\'' +
                ", ShipChoice='" + ShipChoice + '\'' +
                ", OrderDate='" + OrderDate + '\'' +
                ", EstimatedShipDate='" + EstimatedShipDate + '\'' +
                ", Addresses=" + Addresses +
                ", Totals=" + Totals +
                ", OrderItems=" + OrderItems +
                ", PaymentInfo=" + PaymentInfo +
                ", GiftMessage=" + GiftMessage +
                ", GiftCards=" + GiftCards +
                ", GiftCertificates=" + GiftCertificates +
                ", IpAddress='" + IpAddress + '\'' +
                ", OrderStatus='" + OrderStatus + '\'' +
                ", DisplayStatus='" + DisplayStatus + '\'' +
                ", OrderSource='" + OrderSource + '\'' +
                ", Shipments=" + Shipments +
                ", Refunds=" + Refunds +
                ", PromoCode='" + PromoCode + '\'' +
                ", Transactions=" + Transactions +
                ", LastModified='" + LastModified + '\'' +
                ", FulfillmentType='" + FulfillmentType + '\'' +
                ", IsSignatureRequired=" + IsSignatureRequired +
                ", IsInternationalExchange=" + IsInternationalExchange +
                ", Prefix='" + Prefix + '\'' +
                ", ReturnWindow=" + ReturnWindow +
                ", CartRecordId=" + CartRecordId +
                '}';
    }
}
