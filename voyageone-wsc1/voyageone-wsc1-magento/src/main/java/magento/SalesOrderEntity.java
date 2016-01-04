/**
 * SalesOrderEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  SalesOrderEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class SalesOrderEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = salesOrderEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Increment_id
     */
    protected String localIncrement_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIncrement_idTracker = false;

    /**
     * field for Parent_id
     */
    protected String localParent_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localParent_idTracker = false;

    /**
     * field for Store_id
     */
    protected String localStore_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_idTracker = false;

    /**
     * field for Created_at
     */
    protected String localCreated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCreated_atTracker = false;

    /**
     * field for Updated_at
     */
    protected String localUpdated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUpdated_atTracker = false;

    /**
     * field for Is_active
     */
    protected String localIs_active;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_activeTracker = false;

    /**
     * field for Customer_id
     */
    protected String localCustomer_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_idTracker = false;

    /**
     * field for Tax_amount
     */
    protected String localTax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_amountTracker = false;

    /**
     * field for Shipping_amount
     */
    protected String localShipping_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_amountTracker = false;

    /**
     * field for Discount_amount
     */
    protected String localDiscount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_amountTracker = false;

    /**
     * field for Subtotal
     */
    protected String localSubtotal;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubtotalTracker = false;

    /**
     * field for Grand_total
     */
    protected String localGrand_total;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGrand_totalTracker = false;

    /**
     * field for Total_paid
     */
    protected String localTotal_paid;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_paidTracker = false;

    /**
     * field for Total_refunded
     */
    protected String localTotal_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_refundedTracker = false;

    /**
     * field for Total_qty_ordered
     */
    protected String localTotal_qty_ordered;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_qty_orderedTracker = false;

    /**
     * field for Total_canceled
     */
    protected String localTotal_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_canceledTracker = false;

    /**
     * field for Total_invoiced
     */
    protected String localTotal_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_invoicedTracker = false;

    /**
     * field for Total_online_refunded
     */
    protected String localTotal_online_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_online_refundedTracker = false;

    /**
     * field for Total_offline_refunded
     */
    protected String localTotal_offline_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_offline_refundedTracker = false;

    /**
     * field for Base_tax_amount
     */
    protected String localBase_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_amountTracker = false;

    /**
     * field for Base_shipping_amount
     */
    protected String localBase_shipping_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_amountTracker = false;

    /**
     * field for Base_discount_amount
     */
    protected String localBase_discount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_discount_amountTracker = false;

    /**
     * field for Base_subtotal
     */
    protected String localBase_subtotal;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_subtotalTracker = false;

    /**
     * field for Base_grand_total
     */
    protected String localBase_grand_total;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_grand_totalTracker = false;

    /**
     * field for Base_total_paid
     */
    protected String localBase_total_paid;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_paidTracker = false;

    /**
     * field for Base_total_refunded
     */
    protected String localBase_total_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_refundedTracker = false;

    /**
     * field for Base_total_qty_ordered
     */
    protected String localBase_total_qty_ordered;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_qty_orderedTracker = false;

    /**
     * field for Base_total_canceled
     */
    protected String localBase_total_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_canceledTracker = false;

    /**
     * field for Base_total_invoiced
     */
    protected String localBase_total_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_invoicedTracker = false;

    /**
     * field for Base_total_online_refunded
     */
    protected String localBase_total_online_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_online_refundedTracker = false;

    /**
     * field for Base_total_offline_refunded
     */
    protected String localBase_total_offline_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_offline_refundedTracker = false;

    /**
     * field for Billing_address_id
     */
    protected String localBilling_address_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_address_idTracker = false;

    /**
     * field for Billing_firstname
     */
    protected String localBilling_firstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_firstnameTracker = false;

    /**
     * field for Billing_lastname
     */
    protected String localBilling_lastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_lastnameTracker = false;

    /**
     * field for Shipping_address_id
     */
    protected String localShipping_address_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_address_idTracker = false;

    /**
     * field for Shipping_firstname
     */
    protected String localShipping_firstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_firstnameTracker = false;

    /**
     * field for Shipping_lastname
     */
    protected String localShipping_lastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_lastnameTracker = false;

    /**
     * field for Billing_name
     */
    protected String localBilling_name;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_nameTracker = false;

    /**
     * field for Shipping_name
     */
    protected String localShipping_name;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_nameTracker = false;

    /**
     * field for Store_to_base_rate
     */
    protected String localStore_to_base_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_to_base_rateTracker = false;

    /**
     * field for Store_to_order_rate
     */
    protected String localStore_to_order_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_to_order_rateTracker = false;

    /**
     * field for Base_to_global_rate
     */
    protected String localBase_to_global_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_to_global_rateTracker = false;

    /**
     * field for Base_to_order_rate
     */
    protected String localBase_to_order_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_to_order_rateTracker = false;

    /**
     * field for Weight
     */
    protected String localWeight;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeightTracker = false;

    /**
     * field for Store_name
     */
    protected String localStore_name;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_nameTracker = false;

    /**
     * field for Remote_ip
     */
    protected String localRemote_ip;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRemote_ipTracker = false;

    /**
     * field for Status
     */
    protected String localStatus;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStatusTracker = false;

    /**
     * field for State
     */
    protected String localState;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStateTracker = false;

    /**
     * field for Applied_rule_ids
     */
    protected String localApplied_rule_ids;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localApplied_rule_idsTracker = false;

    /**
     * field for Global_currency_code
     */
    protected String localGlobal_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGlobal_currency_codeTracker = false;

    /**
     * field for Base_currency_code
     */
    protected String localBase_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_currency_codeTracker = false;

    /**
     * field for Store_currency_code
     */
    protected String localStore_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_currency_codeTracker = false;

    /**
     * field for Order_currency_code
     */
    protected String localOrder_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_currency_codeTracker = false;

    /**
     * field for Shipping_method
     */
    protected String localShipping_method;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_methodTracker = false;

    /**
     * field for Shipping_description
     */
    protected String localShipping_description;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_descriptionTracker = false;

    /**
     * field for Customer_email
     */
    protected String localCustomer_email;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_emailTracker = false;

    /**
     * field for Customer_firstname
     */
    protected String localCustomer_firstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_firstnameTracker = false;

    /**
     * field for Customer_lastname
     */
    protected String localCustomer_lastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_lastnameTracker = false;

    /**
     * field for Quote_id
     */
    protected String localQuote_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localQuote_idTracker = false;

    /**
     * field for Is_virtual
     */
    protected String localIs_virtual;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_virtualTracker = false;

    /**
     * field for Customer_group_id
     */
    protected String localCustomer_group_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_group_idTracker = false;

    /**
     * field for Customer_note_notify
     */
    protected String localCustomer_note_notify;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_note_notifyTracker = false;

    /**
     * field for Customer_is_guest
     */
    protected String localCustomer_is_guest;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_is_guestTracker = false;

    /**
     * field for Email_sent
     */
    protected String localEmail_sent;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localEmail_sentTracker = false;

    /**
     * field for Order_id
     */
    protected String localOrder_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_idTracker = false;

    /**
     * field for Gift_message_id
     */
    protected String localGift_message_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_message_idTracker = false;

    /**
     * field for Gift_message
     */
    protected String localGift_message;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_messageTracker = false;

    /**
     * field for Shipping_address
     */
    protected magento.SalesOrderAddressEntity localShipping_address;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_addressTracker = false;

    /**
     * field for Billing_address
     */
    protected magento.SalesOrderAddressEntity localBilling_address;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_addressTracker = false;

    /**
     * field for Items
     */
    protected magento.SalesOrderItemEntityArray localItems;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localItemsTracker = false;

    /**
     * field for Payment
     */
    protected magento.SalesOrderPaymentEntity localPayment;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPaymentTracker = false;

    /**
     * field for Status_history
     */
    protected magento.SalesOrderStatusHistoryEntityArray localStatus_history;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStatus_historyTracker = false;

    public boolean isIncrement_idSpecified() {
        return localIncrement_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getIncrement_id() {
        return localIncrement_id;
    }

    /**
     * Auto generated setter method
     * @param param Increment_id
     */
    public void setIncrement_id(String param) {
        localIncrement_idTracker = param != null;

        this.localIncrement_id = param;
    }

    public boolean isParent_idSpecified() {
        return localParent_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getParent_id() {
        return localParent_id;
    }

    /**
     * Auto generated setter method
     * @param param Parent_id
     */
    public void setParent_id(String param) {
        localParent_idTracker = param != null;

        this.localParent_id = param;
    }

    public boolean isStore_idSpecified() {
        return localStore_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStore_id() {
        return localStore_id;
    }

    /**
     * Auto generated setter method
     * @param param Store_id
     */
    public void setStore_id(String param) {
        localStore_idTracker = param != null;

        this.localStore_id = param;
    }

    public boolean isCreated_atSpecified() {
        return localCreated_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCreated_at() {
        return localCreated_at;
    }

    /**
     * Auto generated setter method
     * @param param Created_at
     */
    public void setCreated_at(String param) {
        localCreated_atTracker = param != null;

        this.localCreated_at = param;
    }

    public boolean isUpdated_atSpecified() {
        return localUpdated_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getUpdated_at() {
        return localUpdated_at;
    }

    /**
     * Auto generated setter method
     * @param param Updated_at
     */
    public void setUpdated_at(String param) {
        localUpdated_atTracker = param != null;

        this.localUpdated_at = param;
    }

    public boolean isIs_activeSpecified() {
        return localIs_activeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getIs_active() {
        return localIs_active;
    }

    /**
     * Auto generated setter method
     * @param param Is_active
     */
    public void setIs_active(String param) {
        localIs_activeTracker = param != null;

        this.localIs_active = param;
    }

    public boolean isCustomer_idSpecified() {
        return localCustomer_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_id() {
        return localCustomer_id;
    }

    /**
     * Auto generated setter method
     * @param param Customer_id
     */
    public void setCustomer_id(String param) {
        localCustomer_idTracker = param != null;

        this.localCustomer_id = param;
    }

    public boolean isTax_amountSpecified() {
        return localTax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTax_amount() {
        return localTax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Tax_amount
     */
    public void setTax_amount(String param) {
        localTax_amountTracker = param != null;

        this.localTax_amount = param;
    }

    public boolean isShipping_amountSpecified() {
        return localShipping_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_amount() {
        return localShipping_amount;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_amount
     */
    public void setShipping_amount(String param) {
        localShipping_amountTracker = param != null;

        this.localShipping_amount = param;
    }

    public boolean isDiscount_amountSpecified() {
        return localDiscount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getDiscount_amount() {
        return localDiscount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Discount_amount
     */
    public void setDiscount_amount(String param) {
        localDiscount_amountTracker = param != null;

        this.localDiscount_amount = param;
    }

    public boolean isSubtotalSpecified() {
        return localSubtotalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSubtotal() {
        return localSubtotal;
    }

    /**
     * Auto generated setter method
     * @param param Subtotal
     */
    public void setSubtotal(String param) {
        localSubtotalTracker = param != null;

        this.localSubtotal = param;
    }

    public boolean isGrand_totalSpecified() {
        return localGrand_totalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGrand_total() {
        return localGrand_total;
    }

    /**
     * Auto generated setter method
     * @param param Grand_total
     */
    public void setGrand_total(String param) {
        localGrand_totalTracker = param != null;

        this.localGrand_total = param;
    }

    public boolean isTotal_paidSpecified() {
        return localTotal_paidTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_paid() {
        return localTotal_paid;
    }

    /**
     * Auto generated setter method
     * @param param Total_paid
     */
    public void setTotal_paid(String param) {
        localTotal_paidTracker = param != null;

        this.localTotal_paid = param;
    }

    public boolean isTotal_refundedSpecified() {
        return localTotal_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_refunded() {
        return localTotal_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Total_refunded
     */
    public void setTotal_refunded(String param) {
        localTotal_refundedTracker = param != null;

        this.localTotal_refunded = param;
    }

    public boolean isTotal_qty_orderedSpecified() {
        return localTotal_qty_orderedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_qty_ordered() {
        return localTotal_qty_ordered;
    }

    /**
     * Auto generated setter method
     * @param param Total_qty_ordered
     */
    public void setTotal_qty_ordered(String param) {
        localTotal_qty_orderedTracker = param != null;

        this.localTotal_qty_ordered = param;
    }

    public boolean isTotal_canceledSpecified() {
        return localTotal_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_canceled() {
        return localTotal_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Total_canceled
     */
    public void setTotal_canceled(String param) {
        localTotal_canceledTracker = param != null;

        this.localTotal_canceled = param;
    }

    public boolean isTotal_invoicedSpecified() {
        return localTotal_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_invoiced() {
        return localTotal_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Total_invoiced
     */
    public void setTotal_invoiced(String param) {
        localTotal_invoicedTracker = param != null;

        this.localTotal_invoiced = param;
    }

    public boolean isTotal_online_refundedSpecified() {
        return localTotal_online_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_online_refunded() {
        return localTotal_online_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Total_online_refunded
     */
    public void setTotal_online_refunded(String param) {
        localTotal_online_refundedTracker = param != null;

        this.localTotal_online_refunded = param;
    }

    public boolean isTotal_offline_refundedSpecified() {
        return localTotal_offline_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_offline_refunded() {
        return localTotal_offline_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Total_offline_refunded
     */
    public void setTotal_offline_refunded(String param) {
        localTotal_offline_refundedTracker = param != null;

        this.localTotal_offline_refunded = param;
    }

    public boolean isBase_tax_amountSpecified() {
        return localBase_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_tax_amount() {
        return localBase_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_amount
     */
    public void setBase_tax_amount(String param) {
        localBase_tax_amountTracker = param != null;

        this.localBase_tax_amount = param;
    }

    public boolean isBase_shipping_amountSpecified() {
        return localBase_shipping_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_amount() {
        return localBase_shipping_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_amount
     */
    public void setBase_shipping_amount(String param) {
        localBase_shipping_amountTracker = param != null;

        this.localBase_shipping_amount = param;
    }

    public boolean isBase_discount_amountSpecified() {
        return localBase_discount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_discount_amount() {
        return localBase_discount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_discount_amount
     */
    public void setBase_discount_amount(String param) {
        localBase_discount_amountTracker = param != null;

        this.localBase_discount_amount = param;
    }

    public boolean isBase_subtotalSpecified() {
        return localBase_subtotalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_subtotal() {
        return localBase_subtotal;
    }

    /**
     * Auto generated setter method
     * @param param Base_subtotal
     */
    public void setBase_subtotal(String param) {
        localBase_subtotalTracker = param != null;

        this.localBase_subtotal = param;
    }

    public boolean isBase_grand_totalSpecified() {
        return localBase_grand_totalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_grand_total() {
        return localBase_grand_total;
    }

    /**
     * Auto generated setter method
     * @param param Base_grand_total
     */
    public void setBase_grand_total(String param) {
        localBase_grand_totalTracker = param != null;

        this.localBase_grand_total = param;
    }

    public boolean isBase_total_paidSpecified() {
        return localBase_total_paidTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_paid() {
        return localBase_total_paid;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_paid
     */
    public void setBase_total_paid(String param) {
        localBase_total_paidTracker = param != null;

        this.localBase_total_paid = param;
    }

    public boolean isBase_total_refundedSpecified() {
        return localBase_total_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_refunded() {
        return localBase_total_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_refunded
     */
    public void setBase_total_refunded(String param) {
        localBase_total_refundedTracker = param != null;

        this.localBase_total_refunded = param;
    }

    public boolean isBase_total_qty_orderedSpecified() {
        return localBase_total_qty_orderedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_qty_ordered() {
        return localBase_total_qty_ordered;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_qty_ordered
     */
    public void setBase_total_qty_ordered(String param) {
        localBase_total_qty_orderedTracker = param != null;

        this.localBase_total_qty_ordered = param;
    }

    public boolean isBase_total_canceledSpecified() {
        return localBase_total_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_canceled() {
        return localBase_total_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_canceled
     */
    public void setBase_total_canceled(String param) {
        localBase_total_canceledTracker = param != null;

        this.localBase_total_canceled = param;
    }

    public boolean isBase_total_invoicedSpecified() {
        return localBase_total_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_invoiced() {
        return localBase_total_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_invoiced
     */
    public void setBase_total_invoiced(String param) {
        localBase_total_invoicedTracker = param != null;

        this.localBase_total_invoiced = param;
    }

    public boolean isBase_total_online_refundedSpecified() {
        return localBase_total_online_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_online_refunded() {
        return localBase_total_online_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_online_refunded
     */
    public void setBase_total_online_refunded(String param) {
        localBase_total_online_refundedTracker = param != null;

        this.localBase_total_online_refunded = param;
    }

    public boolean isBase_total_offline_refundedSpecified() {
        return localBase_total_offline_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_offline_refunded() {
        return localBase_total_offline_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_offline_refunded
     */
    public void setBase_total_offline_refunded(String param) {
        localBase_total_offline_refundedTracker = param != null;

        this.localBase_total_offline_refunded = param;
    }

    public boolean isBilling_address_idSpecified() {
        return localBilling_address_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBilling_address_id() {
        return localBilling_address_id;
    }

    /**
     * Auto generated setter method
     * @param param Billing_address_id
     */
    public void setBilling_address_id(String param) {
        localBilling_address_idTracker = param != null;

        this.localBilling_address_id = param;
    }

    public boolean isBilling_firstnameSpecified() {
        return localBilling_firstnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBilling_firstname() {
        return localBilling_firstname;
    }

    /**
     * Auto generated setter method
     * @param param Billing_firstname
     */
    public void setBilling_firstname(String param) {
        localBilling_firstnameTracker = param != null;

        this.localBilling_firstname = param;
    }

    public boolean isBilling_lastnameSpecified() {
        return localBilling_lastnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBilling_lastname() {
        return localBilling_lastname;
    }

    /**
     * Auto generated setter method
     * @param param Billing_lastname
     */
    public void setBilling_lastname(String param) {
        localBilling_lastnameTracker = param != null;

        this.localBilling_lastname = param;
    }

    public boolean isShipping_address_idSpecified() {
        return localShipping_address_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_address_id() {
        return localShipping_address_id;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_address_id
     */
    public void setShipping_address_id(String param) {
        localShipping_address_idTracker = param != null;

        this.localShipping_address_id = param;
    }

    public boolean isShipping_firstnameSpecified() {
        return localShipping_firstnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_firstname() {
        return localShipping_firstname;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_firstname
     */
    public void setShipping_firstname(String param) {
        localShipping_firstnameTracker = param != null;

        this.localShipping_firstname = param;
    }

    public boolean isShipping_lastnameSpecified() {
        return localShipping_lastnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_lastname() {
        return localShipping_lastname;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_lastname
     */
    public void setShipping_lastname(String param) {
        localShipping_lastnameTracker = param != null;

        this.localShipping_lastname = param;
    }

    public boolean isBilling_nameSpecified() {
        return localBilling_nameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBilling_name() {
        return localBilling_name;
    }

    /**
     * Auto generated setter method
     * @param param Billing_name
     */
    public void setBilling_name(String param) {
        localBilling_nameTracker = param != null;

        this.localBilling_name = param;
    }

    public boolean isShipping_nameSpecified() {
        return localShipping_nameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_name() {
        return localShipping_name;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_name
     */
    public void setShipping_name(String param) {
        localShipping_nameTracker = param != null;

        this.localShipping_name = param;
    }

    public boolean isStore_to_base_rateSpecified() {
        return localStore_to_base_rateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStore_to_base_rate() {
        return localStore_to_base_rate;
    }

    /**
     * Auto generated setter method
     * @param param Store_to_base_rate
     */
    public void setStore_to_base_rate(String param) {
        localStore_to_base_rateTracker = param != null;

        this.localStore_to_base_rate = param;
    }

    public boolean isStore_to_order_rateSpecified() {
        return localStore_to_order_rateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStore_to_order_rate() {
        return localStore_to_order_rate;
    }

    /**
     * Auto generated setter method
     * @param param Store_to_order_rate
     */
    public void setStore_to_order_rate(String param) {
        localStore_to_order_rateTracker = param != null;

        this.localStore_to_order_rate = param;
    }

    public boolean isBase_to_global_rateSpecified() {
        return localBase_to_global_rateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_to_global_rate() {
        return localBase_to_global_rate;
    }

    /**
     * Auto generated setter method
     * @param param Base_to_global_rate
     */
    public void setBase_to_global_rate(String param) {
        localBase_to_global_rateTracker = param != null;

        this.localBase_to_global_rate = param;
    }

    public boolean isBase_to_order_rateSpecified() {
        return localBase_to_order_rateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_to_order_rate() {
        return localBase_to_order_rate;
    }

    /**
     * Auto generated setter method
     * @param param Base_to_order_rate
     */
    public void setBase_to_order_rate(String param) {
        localBase_to_order_rateTracker = param != null;

        this.localBase_to_order_rate = param;
    }

    public boolean isWeightSpecified() {
        return localWeightTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getWeight() {
        return localWeight;
    }

    /**
     * Auto generated setter method
     * @param param Weight
     */
    public void setWeight(String param) {
        localWeightTracker = param != null;

        this.localWeight = param;
    }

    public boolean isStore_nameSpecified() {
        return localStore_nameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStore_name() {
        return localStore_name;
    }

    /**
     * Auto generated setter method
     * @param param Store_name
     */
    public void setStore_name(String param) {
        localStore_nameTracker = param != null;

        this.localStore_name = param;
    }

    public boolean isRemote_ipSpecified() {
        return localRemote_ipTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getRemote_ip() {
        return localRemote_ip;
    }

    /**
     * Auto generated setter method
     * @param param Remote_ip
     */
    public void setRemote_ip(String param) {
        localRemote_ipTracker = param != null;

        this.localRemote_ip = param;
    }

    public boolean isStatusSpecified() {
        return localStatusTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStatus() {
        return localStatus;
    }

    /**
     * Auto generated setter method
     * @param param Status
     */
    public void setStatus(String param) {
        localStatusTracker = param != null;

        this.localStatus = param;
    }

    public boolean isStateSpecified() {
        return localStateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getState() {
        return localState;
    }

    /**
     * Auto generated setter method
     * @param param State
     */
    public void setState(String param) {
        localStateTracker = param != null;

        this.localState = param;
    }

    public boolean isApplied_rule_idsSpecified() {
        return localApplied_rule_idsTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getApplied_rule_ids() {
        return localApplied_rule_ids;
    }

    /**
     * Auto generated setter method
     * @param param Applied_rule_ids
     */
    public void setApplied_rule_ids(String param) {
        localApplied_rule_idsTracker = param != null;

        this.localApplied_rule_ids = param;
    }

    public boolean isGlobal_currency_codeSpecified() {
        return localGlobal_currency_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGlobal_currency_code() {
        return localGlobal_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Global_currency_code
     */
    public void setGlobal_currency_code(String param) {
        localGlobal_currency_codeTracker = param != null;

        this.localGlobal_currency_code = param;
    }

    public boolean isBase_currency_codeSpecified() {
        return localBase_currency_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_currency_code() {
        return localBase_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Base_currency_code
     */
    public void setBase_currency_code(String param) {
        localBase_currency_codeTracker = param != null;

        this.localBase_currency_code = param;
    }

    public boolean isStore_currency_codeSpecified() {
        return localStore_currency_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStore_currency_code() {
        return localStore_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Store_currency_code
     */
    public void setStore_currency_code(String param) {
        localStore_currency_codeTracker = param != null;

        this.localStore_currency_code = param;
    }

    public boolean isOrder_currency_codeSpecified() {
        return localOrder_currency_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getOrder_currency_code() {
        return localOrder_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Order_currency_code
     */
    public void setOrder_currency_code(String param) {
        localOrder_currency_codeTracker = param != null;

        this.localOrder_currency_code = param;
    }

    public boolean isShipping_methodSpecified() {
        return localShipping_methodTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_method() {
        return localShipping_method;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_method
     */
    public void setShipping_method(String param) {
        localShipping_methodTracker = param != null;

        this.localShipping_method = param;
    }

    public boolean isShipping_descriptionSpecified() {
        return localShipping_descriptionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_description() {
        return localShipping_description;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_description
     */
    public void setShipping_description(String param) {
        localShipping_descriptionTracker = param != null;

        this.localShipping_description = param;
    }

    public boolean isCustomer_emailSpecified() {
        return localCustomer_emailTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_email() {
        return localCustomer_email;
    }

    /**
     * Auto generated setter method
     * @param param Customer_email
     */
    public void setCustomer_email(String param) {
        localCustomer_emailTracker = param != null;

        this.localCustomer_email = param;
    }

    public boolean isCustomer_firstnameSpecified() {
        return localCustomer_firstnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_firstname() {
        return localCustomer_firstname;
    }

    /**
     * Auto generated setter method
     * @param param Customer_firstname
     */
    public void setCustomer_firstname(String param) {
        localCustomer_firstnameTracker = param != null;

        this.localCustomer_firstname = param;
    }

    public boolean isCustomer_lastnameSpecified() {
        return localCustomer_lastnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_lastname() {
        return localCustomer_lastname;
    }

    /**
     * Auto generated setter method
     * @param param Customer_lastname
     */
    public void setCustomer_lastname(String param) {
        localCustomer_lastnameTracker = param != null;

        this.localCustomer_lastname = param;
    }

    public boolean isQuote_idSpecified() {
        return localQuote_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getQuote_id() {
        return localQuote_id;
    }

    /**
     * Auto generated setter method
     * @param param Quote_id
     */
    public void setQuote_id(String param) {
        localQuote_idTracker = param != null;

        this.localQuote_id = param;
    }

    public boolean isIs_virtualSpecified() {
        return localIs_virtualTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getIs_virtual() {
        return localIs_virtual;
    }

    /**
     * Auto generated setter method
     * @param param Is_virtual
     */
    public void setIs_virtual(String param) {
        localIs_virtualTracker = param != null;

        this.localIs_virtual = param;
    }

    public boolean isCustomer_group_idSpecified() {
        return localCustomer_group_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_group_id() {
        return localCustomer_group_id;
    }

    /**
     * Auto generated setter method
     * @param param Customer_group_id
     */
    public void setCustomer_group_id(String param) {
        localCustomer_group_idTracker = param != null;

        this.localCustomer_group_id = param;
    }

    public boolean isCustomer_note_notifySpecified() {
        return localCustomer_note_notifyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_note_notify() {
        return localCustomer_note_notify;
    }

    /**
     * Auto generated setter method
     * @param param Customer_note_notify
     */
    public void setCustomer_note_notify(String param) {
        localCustomer_note_notifyTracker = param != null;

        this.localCustomer_note_notify = param;
    }

    public boolean isCustomer_is_guestSpecified() {
        return localCustomer_is_guestTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_is_guest() {
        return localCustomer_is_guest;
    }

    /**
     * Auto generated setter method
     * @param param Customer_is_guest
     */
    public void setCustomer_is_guest(String param) {
        localCustomer_is_guestTracker = param != null;

        this.localCustomer_is_guest = param;
    }

    public boolean isEmail_sentSpecified() {
        return localEmail_sentTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getEmail_sent() {
        return localEmail_sent;
    }

    /**
     * Auto generated setter method
     * @param param Email_sent
     */
    public void setEmail_sent(String param) {
        localEmail_sentTracker = param != null;

        this.localEmail_sent = param;
    }

    public boolean isOrder_idSpecified() {
        return localOrder_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getOrder_id() {
        return localOrder_id;
    }

    /**
     * Auto generated setter method
     * @param param Order_id
     */
    public void setOrder_id(String param) {
        localOrder_idTracker = param != null;

        this.localOrder_id = param;
    }

    public boolean isGift_message_idSpecified() {
        return localGift_message_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGift_message_id() {
        return localGift_message_id;
    }

    /**
     * Auto generated setter method
     * @param param Gift_message_id
     */
    public void setGift_message_id(String param) {
        localGift_message_idTracker = param != null;

        this.localGift_message_id = param;
    }

    public boolean isGift_messageSpecified() {
        return localGift_messageTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGift_message() {
        return localGift_message;
    }

    /**
     * Auto generated setter method
     * @param param Gift_message
     */
    public void setGift_message(String param) {
        localGift_messageTracker = param != null;

        this.localGift_message = param;
    }

    public boolean isShipping_addressSpecified() {
        return localShipping_addressTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderAddressEntity
     */
    public magento.SalesOrderAddressEntity getShipping_address() {
        return localShipping_address;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_address
     */
    public void setShipping_address(magento.SalesOrderAddressEntity param) {
        localShipping_addressTracker = param != null;

        this.localShipping_address = param;
    }

    public boolean isBilling_addressSpecified() {
        return localBilling_addressTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderAddressEntity
     */
    public magento.SalesOrderAddressEntity getBilling_address() {
        return localBilling_address;
    }

    /**
     * Auto generated setter method
     * @param param Billing_address
     */
    public void setBilling_address(magento.SalesOrderAddressEntity param) {
        localBilling_addressTracker = param != null;

        this.localBilling_address = param;
    }

    public boolean isItemsSpecified() {
        return localItemsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderItemEntityArray
     */
    public magento.SalesOrderItemEntityArray getItems() {
        return localItems;
    }

    /**
     * Auto generated setter method
     * @param param Items
     */
    public void setItems(magento.SalesOrderItemEntityArray param) {
        localItemsTracker = param != null;

        this.localItems = param;
    }

    public boolean isPaymentSpecified() {
        return localPaymentTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderPaymentEntity
     */
    public magento.SalesOrderPaymentEntity getPayment() {
        return localPayment;
    }

    /**
     * Auto generated setter method
     * @param param Payment
     */
    public void setPayment(magento.SalesOrderPaymentEntity param) {
        localPaymentTracker = param != null;

        this.localPayment = param;
    }

    public boolean isStatus_historySpecified() {
        return localStatus_historyTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderStatusHistoryEntityArray
     */
    public magento.SalesOrderStatusHistoryEntityArray getStatus_history() {
        return localStatus_history;
    }

    /**
     * Auto generated setter method
     * @param param Status_history
     */
    public void setStatus_history(
        magento.SalesOrderStatusHistoryEntityArray param) {
        localStatus_historyTracker = param != null;

        this.localStatus_history = param;
    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
        final javax.xml.namespace.QName parentQName,
        final org.apache.axiom.om.OMFactory factory)
        throws org.apache.axis2.databinding.ADBException {
        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,
                parentQName);

        return factory.createOMElement(dataSource, parentQName);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter, boolean serializeType)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        String prefix = null;
        String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            String namespacePrefix = registerPrefix(xmlWriter,
                    "urn:Magento");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":salesOrderEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "salesOrderEntity", xmlWriter);
            }
        }

        if (localIncrement_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "increment_id", xmlWriter);

            if (localIncrement_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "increment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localIncrement_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localParent_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "parent_id", xmlWriter);

            if (localParent_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "parent_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localParent_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_id", xmlWriter);

            if (localStore_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "store_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStore_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localCreated_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "created_at", xmlWriter);

            if (localCreated_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "created_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCreated_at);
            }

            xmlWriter.writeEndElement();
        }

        if (localUpdated_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "updated_at", xmlWriter);

            if (localUpdated_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "updated_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localUpdated_at);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_activeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_active", xmlWriter);

            if (localIs_active == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "is_active cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localIs_active);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_id", xmlWriter);

            if (localCustomer_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_amount", xmlWriter);

            if (localTax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_amount", xmlWriter);

            if (localShipping_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_amount", xmlWriter);

            if (localDiscount_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDiscount_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localSubtotalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "subtotal", xmlWriter);

            if (localSubtotal == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSubtotal);
            }

            xmlWriter.writeEndElement();
        }

        if (localGrand_totalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "grand_total", xmlWriter);

            if (localGrand_total == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "grand_total cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGrand_total);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_paidTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_paid", xmlWriter);

            if (localTotal_paid == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_paid cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_paid);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_refunded", xmlWriter);

            if (localTotal_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_qty_orderedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_qty_ordered", xmlWriter);

            if (localTotal_qty_ordered == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_qty_ordered cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_qty_ordered);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_canceled", xmlWriter);

            if (localTotal_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_invoiced", xmlWriter);

            if (localTotal_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_online_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_online_refunded",
                xmlWriter);

            if (localTotal_online_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_online_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_online_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_offline_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_offline_refunded",
                xmlWriter);

            if (localTotal_offline_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_offline_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_offline_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_tax_amount", xmlWriter);

            if (localBase_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_amount", xmlWriter);

            if (localBase_shipping_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_discount_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_discount_amount", xmlWriter);

            if (localBase_discount_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_discount_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_subtotalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_subtotal", xmlWriter);

            if (localBase_subtotal == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_subtotal);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_grand_totalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_grand_total", xmlWriter);

            if (localBase_grand_total == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_grand_total cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_grand_total);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_paidTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_paid", xmlWriter);

            if (localBase_total_paid == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_paid cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_paid);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_refunded", xmlWriter);

            if (localBase_total_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_qty_orderedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_qty_ordered",
                xmlWriter);

            if (localBase_total_qty_ordered == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_qty_ordered cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_qty_ordered);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_canceled", xmlWriter);

            if (localBase_total_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_invoiced", xmlWriter);

            if (localBase_total_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_online_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_online_refunded",
                xmlWriter);

            if (localBase_total_online_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_online_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_online_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_offline_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_offline_refunded",
                xmlWriter);

            if (localBase_total_offline_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_offline_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_offline_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBilling_address_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "billing_address_id", xmlWriter);

            if (localBilling_address_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_address_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBilling_address_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localBilling_firstnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "billing_firstname", xmlWriter);

            if (localBilling_firstname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_firstname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBilling_firstname);
            }

            xmlWriter.writeEndElement();
        }

        if (localBilling_lastnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "billing_lastname", xmlWriter);

            if (localBilling_lastname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_lastname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBilling_lastname);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_address_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_address_id", xmlWriter);

            if (localShipping_address_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_address_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_address_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_firstnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_firstname", xmlWriter);

            if (localShipping_firstname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_firstname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_firstname);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_lastnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_lastname", xmlWriter);

            if (localShipping_lastname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_lastname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_lastname);
            }

            xmlWriter.writeEndElement();
        }

        if (localBilling_nameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "billing_name", xmlWriter);

            if (localBilling_name == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_name cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBilling_name);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_nameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_name", xmlWriter);

            if (localShipping_name == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_name cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_name);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_to_base_rateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_to_base_rate", xmlWriter);

            if (localStore_to_base_rate == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "store_to_base_rate cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStore_to_base_rate);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_to_order_rateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_to_order_rate", xmlWriter);

            if (localStore_to_order_rate == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "store_to_order_rate cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStore_to_order_rate);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_to_global_rateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_to_global_rate", xmlWriter);

            if (localBase_to_global_rate == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_to_global_rate cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_to_global_rate);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_to_order_rateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_to_order_rate", xmlWriter);

            if (localBase_to_order_rate == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_to_order_rate cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_to_order_rate);
            }

            xmlWriter.writeEndElement();
        }

        if (localWeightTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weight", xmlWriter);

            if (localWeight == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "weight cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localWeight);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_nameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_name", xmlWriter);

            if (localStore_name == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "store_name cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStore_name);
            }

            xmlWriter.writeEndElement();
        }

        if (localRemote_ipTracker) {
            namespace = "";
            writeStartElement(null, namespace, "remote_ip", xmlWriter);

            if (localRemote_ip == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "remote_ip cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRemote_ip);
            }

            xmlWriter.writeEndElement();
        }

        if (localStatusTracker) {
            namespace = "";
            writeStartElement(null, namespace, "status", xmlWriter);

            if (localStatus == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "status cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStatus);
            }

            xmlWriter.writeEndElement();
        }

        if (localStateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "state", xmlWriter);

            if (localState == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "state cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localState);
            }

            xmlWriter.writeEndElement();
        }

        if (localApplied_rule_idsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "applied_rule_ids", xmlWriter);

            if (localApplied_rule_ids == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "applied_rule_ids cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localApplied_rule_ids);
            }

            xmlWriter.writeEndElement();
        }

        if (localGlobal_currency_codeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "global_currency_code", xmlWriter);

            if (localGlobal_currency_code == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "global_currency_code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGlobal_currency_code);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_currency_codeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_currency_code", xmlWriter);

            if (localBase_currency_code == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_currency_code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_currency_code);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_currency_codeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_currency_code", xmlWriter);

            if (localStore_currency_code == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "store_currency_code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStore_currency_code);
            }

            xmlWriter.writeEndElement();
        }

        if (localOrder_currency_codeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_currency_code", xmlWriter);

            if (localOrder_currency_code == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_currency_code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_currency_code);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_methodTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_method", xmlWriter);

            if (localShipping_method == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_method cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_method);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_descriptionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_description", xmlWriter);

            if (localShipping_description == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_description cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_description);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_emailTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_email", xmlWriter);

            if (localCustomer_email == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_email cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_email);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_firstnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_firstname", xmlWriter);

            if (localCustomer_firstname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_firstname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_firstname);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_lastnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_lastname", xmlWriter);

            if (localCustomer_lastname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_lastname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_lastname);
            }

            xmlWriter.writeEndElement();
        }

        if (localQuote_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "quote_id", xmlWriter);

            if (localQuote_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "quote_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localQuote_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_virtualTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_virtual", xmlWriter);

            if (localIs_virtual == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "is_virtual cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localIs_virtual);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_group_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_group_id", xmlWriter);

            if (localCustomer_group_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_group_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_group_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_note_notifyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_note_notify", xmlWriter);

            if (localCustomer_note_notify == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_note_notify cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_note_notify);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_is_guestTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_is_guest", xmlWriter);

            if (localCustomer_is_guest == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_is_guest cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_is_guest);
            }

            xmlWriter.writeEndElement();
        }

        if (localEmail_sentTracker) {
            namespace = "";
            writeStartElement(null, namespace, "email_sent", xmlWriter);

            if (localEmail_sent == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "email_sent cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localEmail_sent);
            }

            xmlWriter.writeEndElement();
        }

        if (localOrder_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_id", xmlWriter);

            if (localOrder_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localGift_message_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_message_id", xmlWriter);

            if (localGift_message_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_message_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_message_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localGift_messageTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_message", xmlWriter);

            if (localGift_message == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_message cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_message);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_addressTracker) {
            if (localShipping_address == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_address cannot be null!!");
            }

            localShipping_address.serialize(new javax.xml.namespace.QName("",
                    "shipping_address"), xmlWriter);
        }

        if (localBilling_addressTracker) {
            if (localBilling_address == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_address cannot be null!!");
            }

            localBilling_address.serialize(new javax.xml.namespace.QName("",
                    "billing_address"), xmlWriter);
        }

        if (localItemsTracker) {
            if (localItems == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "items cannot be null!!");
            }

            localItems.serialize(new javax.xml.namespace.QName("", "items"),
                xmlWriter);
        }

        if (localPaymentTracker) {
            if (localPayment == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "payment cannot be null!!");
            }

            localPayment.serialize(new javax.xml.namespace.QName("", "payment"),
                xmlWriter);
        }

        if (localStatus_historyTracker) {
            if (localStatus_history == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "status_history cannot be null!!");
            }

            localStatus_history.serialize(new javax.xml.namespace.QName("",
                    "status_history"), xmlWriter);
        }

        xmlWriter.writeEndElement();
    }

    private static String generatePrefix(String namespace) {
        if (namespace.equals("urn:Magento")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(String prefix,
        String namespace, String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(String prefix,
        String namespace, String attName,
        String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(String namespace,
        String attName, String attValue,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(String namespace,
        String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String attributeNamespace = qname.getNamespaceURI();
        String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        String attributeValue;

        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     *  method to handle Qnames
     */
    private void writeQName(javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            String prefix = xmlWriter.getPrefix(namespaceURI);

            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" +
                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            }
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            StringBuffer stringToWrite = new StringBuffer();
            String namespaceURI = null;
            String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }

                namespaceURI = qnames[i].getNamespaceURI();

                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);

                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                     .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            qnames[i]));
                }
            }

            xmlWriter.writeCharacters(stringToWrite.toString());
        }
    }

    /**
     * Register a namespace prefix
     */
    private String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
        throws javax.xml.stream.XMLStreamException {
        String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                String uri = nsContext.getNamespaceURI(prefix);

                if ((uri == null) || (uri.length() == 0)) {
                    break;
                }

                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     *
     */
    public javax.xml.stream.XMLStreamReader getPullParser(
        javax.xml.namespace.QName qName)
        throws org.apache.axis2.databinding.ADBException {
        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        if (localIncrement_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "increment_id"));

            if (localIncrement_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIncrement_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "increment_id cannot be null!!");
            }
        }

        if (localParent_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "parent_id"));

            if (localParent_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localParent_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "parent_id cannot be null!!");
            }
        }

        if (localStore_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "store_id"));

            if (localStore_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_id cannot be null!!");
            }
        }

        if (localCreated_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "created_at"));

            if (localCreated_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCreated_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "created_at cannot be null!!");
            }
        }

        if (localUpdated_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "updated_at"));

            if (localUpdated_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUpdated_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "updated_at cannot be null!!");
            }
        }

        if (localIs_activeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_active"));

            if (localIs_active != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_active));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_active cannot be null!!");
            }
        }

        if (localCustomer_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_id"));

            if (localCustomer_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_id cannot be null!!");
            }
        }

        if (localTax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_amount"));

            if (localTax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_amount cannot be null!!");
            }
        }

        if (localShipping_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "shipping_amount"));

            if (localShipping_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_amount cannot be null!!");
            }
        }

        if (localDiscount_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "discount_amount"));

            if (localDiscount_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_amount cannot be null!!");
            }
        }

        if (localSubtotalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "subtotal"));

            if (localSubtotal != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSubtotal));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal cannot be null!!");
            }
        }

        if (localGrand_totalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "grand_total"));

            if (localGrand_total != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGrand_total));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "grand_total cannot be null!!");
            }
        }

        if (localTotal_paidTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_paid"));

            if (localTotal_paid != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_paid));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_paid cannot be null!!");
            }
        }

        if (localTotal_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_refunded"));

            if (localTotal_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_refunded cannot be null!!");
            }
        }

        if (localTotal_qty_orderedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "total_qty_ordered"));

            if (localTotal_qty_ordered != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_qty_ordered));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_qty_ordered cannot be null!!");
            }
        }

        if (localTotal_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_canceled"));

            if (localTotal_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_canceled cannot be null!!");
            }
        }

        if (localTotal_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_invoiced"));

            if (localTotal_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_invoiced cannot be null!!");
            }
        }

        if (localTotal_online_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "total_online_refunded"));

            if (localTotal_online_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_online_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_online_refunded cannot be null!!");
            }
        }

        if (localTotal_offline_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "total_offline_refunded"));

            if (localTotal_offline_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_offline_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_offline_refunded cannot be null!!");
            }
        }

        if (localBase_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_tax_amount"));

            if (localBase_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_amount cannot be null!!");
            }
        }

        if (localBase_shipping_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_amount"));

            if (localBase_shipping_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_amount cannot be null!!");
            }
        }

        if (localBase_discount_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_discount_amount"));

            if (localBase_discount_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_discount_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_amount cannot be null!!");
            }
        }

        if (localBase_subtotalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_subtotal"));

            if (localBase_subtotal != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_subtotal));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal cannot be null!!");
            }
        }

        if (localBase_grand_totalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_grand_total"));

            if (localBase_grand_total != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_grand_total));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_grand_total cannot be null!!");
            }
        }

        if (localBase_total_paidTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_total_paid"));

            if (localBase_total_paid != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_paid));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_paid cannot be null!!");
            }
        }

        if (localBase_total_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_refunded"));

            if (localBase_total_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_refunded cannot be null!!");
            }
        }

        if (localBase_total_qty_orderedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_qty_ordered"));

            if (localBase_total_qty_ordered != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_qty_ordered));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_qty_ordered cannot be null!!");
            }
        }

        if (localBase_total_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_canceled"));

            if (localBase_total_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_canceled cannot be null!!");
            }
        }

        if (localBase_total_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_invoiced"));

            if (localBase_total_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_invoiced cannot be null!!");
            }
        }

        if (localBase_total_online_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_online_refunded"));

            if (localBase_total_online_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_online_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_online_refunded cannot be null!!");
            }
        }

        if (localBase_total_offline_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_offline_refunded"));

            if (localBase_total_offline_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_offline_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_offline_refunded cannot be null!!");
            }
        }

        if (localBilling_address_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "billing_address_id"));

            if (localBilling_address_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBilling_address_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_address_id cannot be null!!");
            }
        }

        if (localBilling_firstnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "billing_firstname"));

            if (localBilling_firstname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBilling_firstname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_firstname cannot be null!!");
            }
        }

        if (localBilling_lastnameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "billing_lastname"));

            if (localBilling_lastname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBilling_lastname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_lastname cannot be null!!");
            }
        }

        if (localShipping_address_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_address_id"));

            if (localShipping_address_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_address_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_address_id cannot be null!!");
            }
        }

        if (localShipping_firstnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_firstname"));

            if (localShipping_firstname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_firstname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_firstname cannot be null!!");
            }
        }

        if (localShipping_lastnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_lastname"));

            if (localShipping_lastname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_lastname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_lastname cannot be null!!");
            }
        }

        if (localBilling_nameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "billing_name"));

            if (localBilling_name != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBilling_name));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_name cannot be null!!");
            }
        }

        if (localShipping_nameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "shipping_name"));

            if (localShipping_name != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_name));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_name cannot be null!!");
            }
        }

        if (localStore_to_base_rateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "store_to_base_rate"));

            if (localStore_to_base_rate != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_to_base_rate));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_to_base_rate cannot be null!!");
            }
        }

        if (localStore_to_order_rateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "store_to_order_rate"));

            if (localStore_to_order_rate != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_to_order_rate));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_to_order_rate cannot be null!!");
            }
        }

        if (localBase_to_global_rateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_to_global_rate"));

            if (localBase_to_global_rate != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_to_global_rate));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_to_global_rate cannot be null!!");
            }
        }

        if (localBase_to_order_rateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_to_order_rate"));

            if (localBase_to_order_rate != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_to_order_rate));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_to_order_rate cannot be null!!");
            }
        }

        if (localWeightTracker) {
            elementList.add(new javax.xml.namespace.QName("", "weight"));

            if (localWeight != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeight));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "weight cannot be null!!");
            }
        }

        if (localStore_nameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "store_name"));

            if (localStore_name != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_name));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_name cannot be null!!");
            }
        }

        if (localRemote_ipTracker) {
            elementList.add(new javax.xml.namespace.QName("", "remote_ip"));

            if (localRemote_ip != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRemote_ip));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "remote_ip cannot be null!!");
            }
        }

        if (localStatusTracker) {
            elementList.add(new javax.xml.namespace.QName("", "status"));

            if (localStatus != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStatus));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "status cannot be null!!");
            }
        }

        if (localStateTracker) {
            elementList.add(new javax.xml.namespace.QName("", "state"));

            if (localState != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localState));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "state cannot be null!!");
            }
        }

        if (localApplied_rule_idsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "applied_rule_ids"));

            if (localApplied_rule_ids != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localApplied_rule_ids));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "applied_rule_ids cannot be null!!");
            }
        }

        if (localGlobal_currency_codeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "global_currency_code"));

            if (localGlobal_currency_code != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGlobal_currency_code));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "global_currency_code cannot be null!!");
            }
        }

        if (localBase_currency_codeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_currency_code"));

            if (localBase_currency_code != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_currency_code));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_currency_code cannot be null!!");
            }
        }

        if (localStore_currency_codeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "store_currency_code"));

            if (localStore_currency_code != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_currency_code));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_currency_code cannot be null!!");
            }
        }

        if (localOrder_currency_codeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "order_currency_code"));

            if (localOrder_currency_code != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_currency_code));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_currency_code cannot be null!!");
            }
        }

        if (localShipping_methodTracker) {
            elementList.add(new javax.xml.namespace.QName("", "shipping_method"));

            if (localShipping_method != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_method));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_method cannot be null!!");
            }
        }

        if (localShipping_descriptionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_description"));

            if (localShipping_description != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_description));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_description cannot be null!!");
            }
        }

        if (localCustomer_emailTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_email"));

            if (localCustomer_email != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_email));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_email cannot be null!!");
            }
        }

        if (localCustomer_firstnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_firstname"));

            if (localCustomer_firstname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_firstname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_firstname cannot be null!!");
            }
        }

        if (localCustomer_lastnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_lastname"));

            if (localCustomer_lastname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_lastname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_lastname cannot be null!!");
            }
        }

        if (localQuote_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "quote_id"));

            if (localQuote_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localQuote_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "quote_id cannot be null!!");
            }
        }

        if (localIs_virtualTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_virtual"));

            if (localIs_virtual != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_virtual));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_virtual cannot be null!!");
            }
        }

        if (localCustomer_group_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_group_id"));

            if (localCustomer_group_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_group_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_group_id cannot be null!!");
            }
        }

        if (localCustomer_note_notifyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_note_notify"));

            if (localCustomer_note_notify != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_note_notify));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_note_notify cannot be null!!");
            }
        }

        if (localCustomer_is_guestTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_is_guest"));

            if (localCustomer_is_guest != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_is_guest));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_is_guest cannot be null!!");
            }
        }

        if (localEmail_sentTracker) {
            elementList.add(new javax.xml.namespace.QName("", "email_sent"));

            if (localEmail_sent != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localEmail_sent));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "email_sent cannot be null!!");
            }
        }

        if (localOrder_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "order_id"));

            if (localOrder_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_id cannot be null!!");
            }
        }

        if (localGift_message_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "gift_message_id"));

            if (localGift_message_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_message_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_message_id cannot be null!!");
            }
        }

        if (localGift_messageTracker) {
            elementList.add(new javax.xml.namespace.QName("", "gift_message"));

            if (localGift_message != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_message));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_message cannot be null!!");
            }
        }

        if (localShipping_addressTracker) {
            elementList.add(new javax.xml.namespace.QName("", "shipping_address"));

            if (localShipping_address == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_address cannot be null!!");
            }

            elementList.add(localShipping_address);
        }

        if (localBilling_addressTracker) {
            elementList.add(new javax.xml.namespace.QName("", "billing_address"));

            if (localBilling_address == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "billing_address cannot be null!!");
            }

            elementList.add(localBilling_address);
        }

        if (localItemsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "items"));

            if (localItems == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "items cannot be null!!");
            }

            elementList.add(localItems);
        }

        if (localPaymentTracker) {
            elementList.add(new javax.xml.namespace.QName("", "payment"));

            if (localPayment == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "payment cannot be null!!");
            }

            elementList.add(localPayment);
        }

        if (localStatus_historyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "status_history"));

            if (localStatus_history == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "status_history cannot be null!!");
            }

            elementList.add(localStatus_history);
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName,
            elementList.toArray(), attribList.toArray());
    }

    /**
     *  Factory class that keeps the parse method
     */
    public static class Factory {
        /**
         * static method to create the object
         * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
         *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         *                If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static SalesOrderEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            SalesOrderEntity object = new SalesOrderEntity();

            int event;
            String nillableValue = null;
            String prefix = "";
            String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"salesOrderEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (SalesOrderEntity) magento.ExtensionMapper.getTypeObject(nsUri,
                                type, reader);
                        }
                    }
                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "increment_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "increment_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIncrement_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "parent_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "parent_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setParent_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "store_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStore_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "created_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "created_at" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCreated_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "updated_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "updated_at" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUpdated_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_active").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_active" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_active(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "discount_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "discount_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setDiscount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "subtotal").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "subtotal" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setSubtotal(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "grand_total").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "grand_total" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGrand_total(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_paid").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_paid" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_paid(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_qty_ordered").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_qty_ordered" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_qty_ordered(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "total_online_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_online_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_online_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "total_offline_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_offline_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_offline_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_tax_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_shipping_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_discount_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_discount_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_discount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_subtotal").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_subtotal" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_subtotal(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_grand_total").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_grand_total" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_grand_total(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_total_paid").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_paid" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_paid(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_total_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_total_qty_ordered").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_qty_ordered" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_qty_ordered(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_total_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_total_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_total_online_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_online_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_online_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_total_offline_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_offline_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_offline_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "billing_address_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "billing_address_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBilling_address_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "billing_firstname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "billing_firstname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBilling_firstname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "billing_lastname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "billing_lastname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBilling_lastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_address_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_address_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_address_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_firstname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_firstname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_firstname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_lastname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_lastname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_lastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "billing_name").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "billing_name" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBilling_name(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_name").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_name" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_name(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "store_to_base_rate").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_to_base_rate" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStore_to_base_rate(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "store_to_order_rate").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_to_order_rate" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStore_to_order_rate(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_to_global_rate").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_to_global_rate" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_to_global_rate(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_to_order_rate").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_to_order_rate" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_to_order_rate(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "weight").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "weight" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setWeight(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "store_name").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_name" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStore_name(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "remote_ip").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "remote_ip" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setRemote_ip(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "status").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "status" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStatus(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "state").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "state" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setState(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "applied_rule_ids").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "applied_rule_ids" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setApplied_rule_ids(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "global_currency_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "global_currency_code" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGlobal_currency_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_currency_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_currency_code" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_currency_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "store_currency_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_currency_code" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStore_currency_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_currency_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_currency_code" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setOrder_currency_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_method").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_method" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_method(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_description").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_description" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_description(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_email").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_email" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_email(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_firstname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_firstname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_firstname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_lastname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_lastname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_lastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "quote_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "quote_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setQuote_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_virtual").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_virtual" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_virtual(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_group_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_group_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_group_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_note_notify").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_note_notify" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_note_notify(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_is_guest").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_is_guest" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_is_guest(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "email_sent").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "email_sent" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setEmail_sent(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setOrder_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gift_message_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_message_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGift_message_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gift_message").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_message" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGift_message(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_address").equals(
                            reader.getName())) {
                    object.setShipping_address(magento.SalesOrderAddressEntity.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "billing_address").equals(
                            reader.getName())) {
                    object.setBilling_address(magento.SalesOrderAddressEntity.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "items").equals(
                            reader.getName())) {
                    object.setItems(magento.SalesOrderItemEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "payment").equals(
                            reader.getName())) {
                    object.setPayment(magento.SalesOrderPaymentEntity.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "status_history").equals(
                            reader.getName())) {
                    object.setStatus_history(magento.SalesOrderStatusHistoryEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }
    } //end of factory class
}
