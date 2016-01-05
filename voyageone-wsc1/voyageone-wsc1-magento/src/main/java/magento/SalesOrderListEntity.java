/**
 * SalesOrderListEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  SalesOrderListEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class SalesOrderListEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = salesOrderListEntity
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
     * field for Coupon_code
     */
    protected String localCoupon_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCoupon_codeTracker = false;

    /**
     * field for Protect_code
     */
    protected String localProtect_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localProtect_codeTracker = false;

    /**
     * field for Base_discount_canceled
     */
    protected String localBase_discount_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_discount_canceledTracker = false;

    /**
     * field for Base_discount_invoiced
     */
    protected String localBase_discount_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_discount_invoicedTracker = false;

    /**
     * field for Base_discount_refunded
     */
    protected String localBase_discount_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_discount_refundedTracker = false;

    /**
     * field for Base_shipping_canceled
     */
    protected String localBase_shipping_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_canceledTracker = false;

    /**
     * field for Base_shipping_invoiced
     */
    protected String localBase_shipping_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_invoicedTracker = false;

    /**
     * field for Base_shipping_refunded
     */
    protected String localBase_shipping_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_refundedTracker = false;

    /**
     * field for Base_shipping_tax_amount
     */
    protected String localBase_shipping_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_tax_amountTracker = false;

    /**
     * field for Base_shipping_tax_refunded
     */
    protected String localBase_shipping_tax_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_tax_refundedTracker = false;

    /**
     * field for Base_subtotal_canceled
     */
    protected String localBase_subtotal_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_subtotal_canceledTracker = false;

    /**
     * field for Base_subtotal_invoiced
     */
    protected String localBase_subtotal_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_subtotal_invoicedTracker = false;

    /**
     * field for Base_subtotal_refunded
     */
    protected String localBase_subtotal_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_subtotal_refundedTracker = false;

    /**
     * field for Base_tax_canceled
     */
    protected String localBase_tax_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_canceledTracker = false;

    /**
     * field for Base_tax_invoiced
     */
    protected String localBase_tax_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_invoicedTracker = false;

    /**
     * field for Base_tax_refunded
     */
    protected String localBase_tax_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_refundedTracker = false;

    /**
     * field for Base_total_invoiced_cost
     */
    protected String localBase_total_invoiced_cost;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_invoiced_costTracker = false;

    /**
     * field for Discount_canceled
     */
    protected String localDiscount_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_canceledTracker = false;

    /**
     * field for Discount_invoiced
     */
    protected String localDiscount_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_invoicedTracker = false;

    /**
     * field for Discount_refunded
     */
    protected String localDiscount_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_refundedTracker = false;

    /**
     * field for Shipping_canceled
     */
    protected String localShipping_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_canceledTracker = false;

    /**
     * field for Shipping_invoiced
     */
    protected String localShipping_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_invoicedTracker = false;

    /**
     * field for Shipping_refunded
     */
    protected String localShipping_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_refundedTracker = false;

    /**
     * field for Shipping_tax_amount
     */
    protected String localShipping_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_tax_amountTracker = false;

    /**
     * field for Shipping_tax_refunded
     */
    protected String localShipping_tax_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_tax_refundedTracker = false;

    /**
     * field for Subtotal_canceled
     */
    protected String localSubtotal_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubtotal_canceledTracker = false;

    /**
     * field for Subtotal_invoiced
     */
    protected String localSubtotal_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubtotal_invoicedTracker = false;

    /**
     * field for Subtotal_refunded
     */
    protected String localSubtotal_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubtotal_refundedTracker = false;

    /**
     * field for Tax_canceled
     */
    protected String localTax_canceled;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_canceledTracker = false;

    /**
     * field for Tax_invoiced
     */
    protected String localTax_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_invoicedTracker = false;

    /**
     * field for Tax_refunded
     */
    protected String localTax_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_refundedTracker = false;

    /**
     * field for Can_ship_partially
     */
    protected String localCan_ship_partially;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCan_ship_partiallyTracker = false;

    /**
     * field for Can_ship_partially_item
     */
    protected String localCan_ship_partially_item;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCan_ship_partially_itemTracker = false;

    /**
     * field for Edit_increment
     */
    protected String localEdit_increment;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localEdit_incrementTracker = false;

    /**
     * field for Forced_do_shipment_with_invoice
     */
    protected String localForced_do_shipment_with_invoice;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localForced_do_shipment_with_invoiceTracker = false;

    /**
     * field for Payment_authorization_expiration
     */
    protected String localPayment_authorization_expiration;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPayment_authorization_expirationTracker = false;

    /**
     * field for Paypal_ipn_customer_notified
     */
    protected String localPaypal_ipn_customer_notified;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPaypal_ipn_customer_notifiedTracker = false;

    /**
     * field for Quote_address_id
     */
    protected String localQuote_address_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localQuote_address_idTracker = false;

    /**
     * field for Adjustment_negative
     */
    protected String localAdjustment_negative;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdjustment_negativeTracker = false;

    /**
     * field for Adjustment_positive
     */
    protected String localAdjustment_positive;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdjustment_positiveTracker = false;

    /**
     * field for Base_adjustment_negative
     */
    protected String localBase_adjustment_negative;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_adjustment_negativeTracker = false;

    /**
     * field for Base_adjustment_positive
     */
    protected String localBase_adjustment_positive;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_adjustment_positiveTracker = false;

    /**
     * field for Base_shipping_discount_amount
     */
    protected String localBase_shipping_discount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_discount_amountTracker = false;

    /**
     * field for Base_subtotal_incl_tax
     */
    protected String localBase_subtotal_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_subtotal_incl_taxTracker = false;

    /**
     * field for Base_total_due
     */
    protected String localBase_total_due;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_total_dueTracker = false;

    /**
     * field for Payment_authorization_amount
     */
    protected String localPayment_authorization_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPayment_authorization_amountTracker = false;

    /**
     * field for Shipping_discount_amount
     */
    protected String localShipping_discount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_discount_amountTracker = false;

    /**
     * field for Subtotal_incl_tax
     */
    protected String localSubtotal_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubtotal_incl_taxTracker = false;

    /**
     * field for Total_due
     */
    protected String localTotal_due;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_dueTracker = false;

    /**
     * field for Customer_dob
     */
    protected String localCustomer_dob;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_dobTracker = false;

    /**
     * field for Customer_middlename
     */
    protected String localCustomer_middlename;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_middlenameTracker = false;

    /**
     * field for Customer_prefix
     */
    protected String localCustomer_prefix;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_prefixTracker = false;

    /**
     * field for Customer_suffix
     */
    protected String localCustomer_suffix;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_suffixTracker = false;

    /**
     * field for Customer_taxvat
     */
    protected String localCustomer_taxvat;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_taxvatTracker = false;

    /**
     * field for Discount_description
     */
    protected String localDiscount_description;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_descriptionTracker = false;

    /**
     * field for Ext_customer_id
     */
    protected String localExt_customer_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localExt_customer_idTracker = false;

    /**
     * field for Ext_order_id
     */
    protected String localExt_order_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localExt_order_idTracker = false;

    /**
     * field for Hold_before_state
     */
    protected String localHold_before_state;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHold_before_stateTracker = false;

    /**
     * field for Hold_before_status
     */
    protected String localHold_before_status;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHold_before_statusTracker = false;

    /**
     * field for Original_increment_id
     */
    protected String localOriginal_increment_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOriginal_increment_idTracker = false;

    /**
     * field for Relation_child_id
     */
    protected String localRelation_child_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRelation_child_idTracker = false;

    /**
     * field for Relation_child_real_id
     */
    protected String localRelation_child_real_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRelation_child_real_idTracker = false;

    /**
     * field for Relation_parent_id
     */
    protected String localRelation_parent_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRelation_parent_idTracker = false;

    /**
     * field for Relation_parent_real_id
     */
    protected String localRelation_parent_real_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRelation_parent_real_idTracker = false;

    /**
     * field for X_forwarded_for
     */
    protected String localX_forwarded_for;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localX_forwarded_forTracker = false;

    /**
     * field for Customer_note
     */
    protected String localCustomer_note;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_noteTracker = false;

    /**
     * field for Total_item_count
     */
    protected String localTotal_item_count;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_item_countTracker = false;

    /**
     * field for Customer_gender
     */
    protected String localCustomer_gender;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_genderTracker = false;

    /**
     * field for Hidden_tax_amount
     */
    protected String localHidden_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHidden_tax_amountTracker = false;

    /**
     * field for Base_hidden_tax_amount
     */
    protected String localBase_hidden_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_hidden_tax_amountTracker = false;

    /**
     * field for Shipping_hidden_tax_amount
     */
    protected String localShipping_hidden_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_hidden_tax_amountTracker = false;

    /**
     * field for Base_shipping_hidden_tax_amount
     */
    protected String localBase_shipping_hidden_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_hidden_tax_amountTracker = false;

    /**
     * field for Hidden_tax_invoiced
     */
    protected String localHidden_tax_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHidden_tax_invoicedTracker = false;

    /**
     * field for Base_hidden_tax_invoiced
     */
    protected String localBase_hidden_tax_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_hidden_tax_invoicedTracker = false;

    /**
     * field for Hidden_tax_refunded
     */
    protected String localHidden_tax_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHidden_tax_refundedTracker = false;

    /**
     * field for Base_hidden_tax_refunded
     */
    protected String localBase_hidden_tax_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_hidden_tax_refundedTracker = false;

    /**
     * field for Shipping_incl_tax
     */
    protected String localShipping_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_incl_taxTracker = false;

    /**
     * field for Base_shipping_incl_tax
     */
    protected String localBase_shipping_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_incl_taxTracker = false;

    /**
     * field for Base_customer_balance_amount
     */
    protected String localBase_customer_balance_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_customer_balance_amountTracker = false;

    /**
     * field for Customer_balance_amount
     */
    protected String localCustomer_balance_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_balance_amountTracker = false;

    /**
     * field for Base_customer_balance_invoiced
     */
    protected String localBase_customer_balance_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_customer_balance_invoicedTracker = false;

    /**
     * field for Customer_balance_invoiced
     */
    protected String localCustomer_balance_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_balance_invoicedTracker = false;

    /**
     * field for Base_customer_balance_refunded
     */
    protected String localBase_customer_balance_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_customer_balance_refundedTracker = false;

    /**
     * field for Customer_balance_refunded
     */
    protected String localCustomer_balance_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_balance_refundedTracker = false;

    /**
     * field for Base_customer_balance_total_refunded
     */
    protected String localBase_customer_balance_total_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_customer_balance_total_refundedTracker = false;

    /**
     * field for Customer_balance_total_refunded
     */
    protected String localCustomer_balance_total_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_balance_total_refundedTracker = false;

    /**
     * field for Gift_cards
     */
    protected String localGift_cards;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_cardsTracker = false;

    /**
     * field for Base_gift_cards_amount
     */
    protected String localBase_gift_cards_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_gift_cards_amountTracker = false;

    /**
     * field for Gift_cards_amount
     */
    protected String localGift_cards_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_cards_amountTracker = false;

    /**
     * field for Base_gift_cards_invoiced
     */
    protected String localBase_gift_cards_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_gift_cards_invoicedTracker = false;

    /**
     * field for Gift_cards_invoiced
     */
    protected String localGift_cards_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_cards_invoicedTracker = false;

    /**
     * field for Base_gift_cards_refunded
     */
    protected String localBase_gift_cards_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_gift_cards_refundedTracker = false;

    /**
     * field for Gift_cards_refunded
     */
    protected String localGift_cards_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_cards_refundedTracker = false;

    /**
     * field for Reward_points_balance
     */
    protected String localReward_points_balance;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_points_balanceTracker = false;

    /**
     * field for Base_reward_currency_amount
     */
    protected String localBase_reward_currency_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_reward_currency_amountTracker = false;

    /**
     * field for Reward_currency_amount
     */
    protected String localReward_currency_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_currency_amountTracker = false;

    /**
     * field for Base_reward_currency_amount_invoiced
     */
    protected String localBase_reward_currency_amount_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_reward_currency_amount_invoicedTracker = false;

    /**
     * field for Reward_currency_amount_invoiced
     */
    protected String localReward_currency_amount_invoiced;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_currency_amount_invoicedTracker = false;

    /**
     * field for Base_reward_currency_amount_refunded
     */
    protected String localBase_reward_currency_amount_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_reward_currency_amount_refundedTracker = false;

    /**
     * field for Reward_currency_amount_refunded
     */
    protected String localReward_currency_amount_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_currency_amount_refundedTracker = false;

    /**
     * field for Reward_points_balance_refunded
     */
    protected String localReward_points_balance_refunded;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_points_balance_refundedTracker = false;

    /**
     * field for Reward_points_balance_to_refund
     */
    protected String localReward_points_balance_to_refund;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_points_balance_to_refundTracker = false;

    /**
     * field for Reward_salesrule_points
     */
    protected String localReward_salesrule_points;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localReward_salesrule_pointsTracker = false;

    /**
     * field for Firstname
     */
    protected String localFirstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFirstnameTracker = false;

    /**
     * field for Lastname
     */
    protected String localLastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localLastnameTracker = false;

    /**
     * field for Telephone
     */
    protected String localTelephone;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTelephoneTracker = false;

    /**
     * field for Postcode
     */
    protected String localPostcode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPostcodeTracker = false;

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

    public boolean isCoupon_codeSpecified() {
        return localCoupon_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCoupon_code() {
        return localCoupon_code;
    }

    /**
     * Auto generated setter method
     * @param param Coupon_code
     */
    public void setCoupon_code(String param) {
        localCoupon_codeTracker = param != null;

        this.localCoupon_code = param;
    }

    public boolean isProtect_codeSpecified() {
        return localProtect_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getProtect_code() {
        return localProtect_code;
    }

    /**
     * Auto generated setter method
     * @param param Protect_code
     */
    public void setProtect_code(String param) {
        localProtect_codeTracker = param != null;

        this.localProtect_code = param;
    }

    public boolean isBase_discount_canceledSpecified() {
        return localBase_discount_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_discount_canceled() {
        return localBase_discount_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Base_discount_canceled
     */
    public void setBase_discount_canceled(String param) {
        localBase_discount_canceledTracker = param != null;

        this.localBase_discount_canceled = param;
    }

    public boolean isBase_discount_invoicedSpecified() {
        return localBase_discount_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_discount_invoiced() {
        return localBase_discount_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_discount_invoiced
     */
    public void setBase_discount_invoiced(String param) {
        localBase_discount_invoicedTracker = param != null;

        this.localBase_discount_invoiced = param;
    }

    public boolean isBase_discount_refundedSpecified() {
        return localBase_discount_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_discount_refunded() {
        return localBase_discount_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_discount_refunded
     */
    public void setBase_discount_refunded(String param) {
        localBase_discount_refundedTracker = param != null;

        this.localBase_discount_refunded = param;
    }

    public boolean isBase_shipping_canceledSpecified() {
        return localBase_shipping_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_canceled() {
        return localBase_shipping_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_canceled
     */
    public void setBase_shipping_canceled(String param) {
        localBase_shipping_canceledTracker = param != null;

        this.localBase_shipping_canceled = param;
    }

    public boolean isBase_shipping_invoicedSpecified() {
        return localBase_shipping_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_invoiced() {
        return localBase_shipping_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_invoiced
     */
    public void setBase_shipping_invoiced(String param) {
        localBase_shipping_invoicedTracker = param != null;

        this.localBase_shipping_invoiced = param;
    }

    public boolean isBase_shipping_refundedSpecified() {
        return localBase_shipping_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_refunded() {
        return localBase_shipping_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_refunded
     */
    public void setBase_shipping_refunded(String param) {
        localBase_shipping_refundedTracker = param != null;

        this.localBase_shipping_refunded = param;
    }

    public boolean isBase_shipping_tax_amountSpecified() {
        return localBase_shipping_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_tax_amount() {
        return localBase_shipping_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_tax_amount
     */
    public void setBase_shipping_tax_amount(String param) {
        localBase_shipping_tax_amountTracker = param != null;

        this.localBase_shipping_tax_amount = param;
    }

    public boolean isBase_shipping_tax_refundedSpecified() {
        return localBase_shipping_tax_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_tax_refunded() {
        return localBase_shipping_tax_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_tax_refunded
     */
    public void setBase_shipping_tax_refunded(String param) {
        localBase_shipping_tax_refundedTracker = param != null;

        this.localBase_shipping_tax_refunded = param;
    }

    public boolean isBase_subtotal_canceledSpecified() {
        return localBase_subtotal_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_subtotal_canceled() {
        return localBase_subtotal_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Base_subtotal_canceled
     */
    public void setBase_subtotal_canceled(String param) {
        localBase_subtotal_canceledTracker = param != null;

        this.localBase_subtotal_canceled = param;
    }

    public boolean isBase_subtotal_invoicedSpecified() {
        return localBase_subtotal_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_subtotal_invoiced() {
        return localBase_subtotal_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_subtotal_invoiced
     */
    public void setBase_subtotal_invoiced(String param) {
        localBase_subtotal_invoicedTracker = param != null;

        this.localBase_subtotal_invoiced = param;
    }

    public boolean isBase_subtotal_refundedSpecified() {
        return localBase_subtotal_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_subtotal_refunded() {
        return localBase_subtotal_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_subtotal_refunded
     */
    public void setBase_subtotal_refunded(String param) {
        localBase_subtotal_refundedTracker = param != null;

        this.localBase_subtotal_refunded = param;
    }

    public boolean isBase_tax_canceledSpecified() {
        return localBase_tax_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_tax_canceled() {
        return localBase_tax_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_canceled
     */
    public void setBase_tax_canceled(String param) {
        localBase_tax_canceledTracker = param != null;

        this.localBase_tax_canceled = param;
    }

    public boolean isBase_tax_invoicedSpecified() {
        return localBase_tax_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_tax_invoiced() {
        return localBase_tax_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_invoiced
     */
    public void setBase_tax_invoiced(String param) {
        localBase_tax_invoicedTracker = param != null;

        this.localBase_tax_invoiced = param;
    }

    public boolean isBase_tax_refundedSpecified() {
        return localBase_tax_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_tax_refunded() {
        return localBase_tax_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_refunded
     */
    public void setBase_tax_refunded(String param) {
        localBase_tax_refundedTracker = param != null;

        this.localBase_tax_refunded = param;
    }

    public boolean isBase_total_invoiced_costSpecified() {
        return localBase_total_invoiced_costTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_invoiced_cost() {
        return localBase_total_invoiced_cost;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_invoiced_cost
     */
    public void setBase_total_invoiced_cost(String param) {
        localBase_total_invoiced_costTracker = param != null;

        this.localBase_total_invoiced_cost = param;
    }

    public boolean isDiscount_canceledSpecified() {
        return localDiscount_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getDiscount_canceled() {
        return localDiscount_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Discount_canceled
     */
    public void setDiscount_canceled(String param) {
        localDiscount_canceledTracker = param != null;

        this.localDiscount_canceled = param;
    }

    public boolean isDiscount_invoicedSpecified() {
        return localDiscount_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getDiscount_invoiced() {
        return localDiscount_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Discount_invoiced
     */
    public void setDiscount_invoiced(String param) {
        localDiscount_invoicedTracker = param != null;

        this.localDiscount_invoiced = param;
    }

    public boolean isDiscount_refundedSpecified() {
        return localDiscount_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getDiscount_refunded() {
        return localDiscount_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Discount_refunded
     */
    public void setDiscount_refunded(String param) {
        localDiscount_refundedTracker = param != null;

        this.localDiscount_refunded = param;
    }

    public boolean isShipping_canceledSpecified() {
        return localShipping_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_canceled() {
        return localShipping_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_canceled
     */
    public void setShipping_canceled(String param) {
        localShipping_canceledTracker = param != null;

        this.localShipping_canceled = param;
    }

    public boolean isShipping_invoicedSpecified() {
        return localShipping_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_invoiced() {
        return localShipping_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_invoiced
     */
    public void setShipping_invoiced(String param) {
        localShipping_invoicedTracker = param != null;

        this.localShipping_invoiced = param;
    }

    public boolean isShipping_refundedSpecified() {
        return localShipping_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_refunded() {
        return localShipping_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_refunded
     */
    public void setShipping_refunded(String param) {
        localShipping_refundedTracker = param != null;

        this.localShipping_refunded = param;
    }

    public boolean isShipping_tax_amountSpecified() {
        return localShipping_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_tax_amount() {
        return localShipping_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_tax_amount
     */
    public void setShipping_tax_amount(String param) {
        localShipping_tax_amountTracker = param != null;

        this.localShipping_tax_amount = param;
    }

    public boolean isShipping_tax_refundedSpecified() {
        return localShipping_tax_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_tax_refunded() {
        return localShipping_tax_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_tax_refunded
     */
    public void setShipping_tax_refunded(String param) {
        localShipping_tax_refundedTracker = param != null;

        this.localShipping_tax_refunded = param;
    }

    public boolean isSubtotal_canceledSpecified() {
        return localSubtotal_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSubtotal_canceled() {
        return localSubtotal_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Subtotal_canceled
     */
    public void setSubtotal_canceled(String param) {
        localSubtotal_canceledTracker = param != null;

        this.localSubtotal_canceled = param;
    }

    public boolean isSubtotal_invoicedSpecified() {
        return localSubtotal_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSubtotal_invoiced() {
        return localSubtotal_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Subtotal_invoiced
     */
    public void setSubtotal_invoiced(String param) {
        localSubtotal_invoicedTracker = param != null;

        this.localSubtotal_invoiced = param;
    }

    public boolean isSubtotal_refundedSpecified() {
        return localSubtotal_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSubtotal_refunded() {
        return localSubtotal_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Subtotal_refunded
     */
    public void setSubtotal_refunded(String param) {
        localSubtotal_refundedTracker = param != null;

        this.localSubtotal_refunded = param;
    }

    public boolean isTax_canceledSpecified() {
        return localTax_canceledTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTax_canceled() {
        return localTax_canceled;
    }

    /**
     * Auto generated setter method
     * @param param Tax_canceled
     */
    public void setTax_canceled(String param) {
        localTax_canceledTracker = param != null;

        this.localTax_canceled = param;
    }

    public boolean isTax_invoicedSpecified() {
        return localTax_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTax_invoiced() {
        return localTax_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Tax_invoiced
     */
    public void setTax_invoiced(String param) {
        localTax_invoicedTracker = param != null;

        this.localTax_invoiced = param;
    }

    public boolean isTax_refundedSpecified() {
        return localTax_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTax_refunded() {
        return localTax_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Tax_refunded
     */
    public void setTax_refunded(String param) {
        localTax_refundedTracker = param != null;

        this.localTax_refunded = param;
    }

    public boolean isCan_ship_partiallySpecified() {
        return localCan_ship_partiallyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCan_ship_partially() {
        return localCan_ship_partially;
    }

    /**
     * Auto generated setter method
     * @param param Can_ship_partially
     */
    public void setCan_ship_partially(String param) {
        localCan_ship_partiallyTracker = param != null;

        this.localCan_ship_partially = param;
    }

    public boolean isCan_ship_partially_itemSpecified() {
        return localCan_ship_partially_itemTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCan_ship_partially_item() {
        return localCan_ship_partially_item;
    }

    /**
     * Auto generated setter method
     * @param param Can_ship_partially_item
     */
    public void setCan_ship_partially_item(String param) {
        localCan_ship_partially_itemTracker = param != null;

        this.localCan_ship_partially_item = param;
    }

    public boolean isEdit_incrementSpecified() {
        return localEdit_incrementTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getEdit_increment() {
        return localEdit_increment;
    }

    /**
     * Auto generated setter method
     * @param param Edit_increment
     */
    public void setEdit_increment(String param) {
        localEdit_incrementTracker = param != null;

        this.localEdit_increment = param;
    }

    public boolean isForced_do_shipment_with_invoiceSpecified() {
        return localForced_do_shipment_with_invoiceTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getForced_do_shipment_with_invoice() {
        return localForced_do_shipment_with_invoice;
    }

    /**
     * Auto generated setter method
     * @param param Forced_do_shipment_with_invoice
     */
    public void setForced_do_shipment_with_invoice(String param) {
        localForced_do_shipment_with_invoiceTracker = param != null;

        this.localForced_do_shipment_with_invoice = param;
    }

    public boolean isPayment_authorization_expirationSpecified() {
        return localPayment_authorization_expirationTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPayment_authorization_expiration() {
        return localPayment_authorization_expiration;
    }

    /**
     * Auto generated setter method
     * @param param Payment_authorization_expiration
     */
    public void setPayment_authorization_expiration(String param) {
        localPayment_authorization_expirationTracker = param != null;

        this.localPayment_authorization_expiration = param;
    }

    public boolean isPaypal_ipn_customer_notifiedSpecified() {
        return localPaypal_ipn_customer_notifiedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPaypal_ipn_customer_notified() {
        return localPaypal_ipn_customer_notified;
    }

    /**
     * Auto generated setter method
     * @param param Paypal_ipn_customer_notified
     */
    public void setPaypal_ipn_customer_notified(String param) {
        localPaypal_ipn_customer_notifiedTracker = param != null;

        this.localPaypal_ipn_customer_notified = param;
    }

    public boolean isQuote_address_idSpecified() {
        return localQuote_address_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getQuote_address_id() {
        return localQuote_address_id;
    }

    /**
     * Auto generated setter method
     * @param param Quote_address_id
     */
    public void setQuote_address_id(String param) {
        localQuote_address_idTracker = param != null;

        this.localQuote_address_id = param;
    }

    public boolean isAdjustment_negativeSpecified() {
        return localAdjustment_negativeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getAdjustment_negative() {
        return localAdjustment_negative;
    }

    /**
     * Auto generated setter method
     * @param param Adjustment_negative
     */
    public void setAdjustment_negative(String param) {
        localAdjustment_negativeTracker = param != null;

        this.localAdjustment_negative = param;
    }

    public boolean isAdjustment_positiveSpecified() {
        return localAdjustment_positiveTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getAdjustment_positive() {
        return localAdjustment_positive;
    }

    /**
     * Auto generated setter method
     * @param param Adjustment_positive
     */
    public void setAdjustment_positive(String param) {
        localAdjustment_positiveTracker = param != null;

        this.localAdjustment_positive = param;
    }

    public boolean isBase_adjustment_negativeSpecified() {
        return localBase_adjustment_negativeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_adjustment_negative() {
        return localBase_adjustment_negative;
    }

    /**
     * Auto generated setter method
     * @param param Base_adjustment_negative
     */
    public void setBase_adjustment_negative(String param) {
        localBase_adjustment_negativeTracker = param != null;

        this.localBase_adjustment_negative = param;
    }

    public boolean isBase_adjustment_positiveSpecified() {
        return localBase_adjustment_positiveTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_adjustment_positive() {
        return localBase_adjustment_positive;
    }

    /**
     * Auto generated setter method
     * @param param Base_adjustment_positive
     */
    public void setBase_adjustment_positive(String param) {
        localBase_adjustment_positiveTracker = param != null;

        this.localBase_adjustment_positive = param;
    }

    public boolean isBase_shipping_discount_amountSpecified() {
        return localBase_shipping_discount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_discount_amount() {
        return localBase_shipping_discount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_discount_amount
     */
    public void setBase_shipping_discount_amount(String param) {
        localBase_shipping_discount_amountTracker = param != null;

        this.localBase_shipping_discount_amount = param;
    }

    public boolean isBase_subtotal_incl_taxSpecified() {
        return localBase_subtotal_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_subtotal_incl_tax() {
        return localBase_subtotal_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Base_subtotal_incl_tax
     */
    public void setBase_subtotal_incl_tax(String param) {
        localBase_subtotal_incl_taxTracker = param != null;

        this.localBase_subtotal_incl_tax = param;
    }

    public boolean isBase_total_dueSpecified() {
        return localBase_total_dueTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_total_due() {
        return localBase_total_due;
    }

    /**
     * Auto generated setter method
     * @param param Base_total_due
     */
    public void setBase_total_due(String param) {
        localBase_total_dueTracker = param != null;

        this.localBase_total_due = param;
    }

    public boolean isPayment_authorization_amountSpecified() {
        return localPayment_authorization_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPayment_authorization_amount() {
        return localPayment_authorization_amount;
    }

    /**
     * Auto generated setter method
     * @param param Payment_authorization_amount
     */
    public void setPayment_authorization_amount(String param) {
        localPayment_authorization_amountTracker = param != null;

        this.localPayment_authorization_amount = param;
    }

    public boolean isShipping_discount_amountSpecified() {
        return localShipping_discount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_discount_amount() {
        return localShipping_discount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_discount_amount
     */
    public void setShipping_discount_amount(String param) {
        localShipping_discount_amountTracker = param != null;

        this.localShipping_discount_amount = param;
    }

    public boolean isSubtotal_incl_taxSpecified() {
        return localSubtotal_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSubtotal_incl_tax() {
        return localSubtotal_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Subtotal_incl_tax
     */
    public void setSubtotal_incl_tax(String param) {
        localSubtotal_incl_taxTracker = param != null;

        this.localSubtotal_incl_tax = param;
    }

    public boolean isTotal_dueSpecified() {
        return localTotal_dueTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_due() {
        return localTotal_due;
    }

    /**
     * Auto generated setter method
     * @param param Total_due
     */
    public void setTotal_due(String param) {
        localTotal_dueTracker = param != null;

        this.localTotal_due = param;
    }

    public boolean isCustomer_dobSpecified() {
        return localCustomer_dobTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_dob() {
        return localCustomer_dob;
    }

    /**
     * Auto generated setter method
     * @param param Customer_dob
     */
    public void setCustomer_dob(String param) {
        localCustomer_dobTracker = param != null;

        this.localCustomer_dob = param;
    }

    public boolean isCustomer_middlenameSpecified() {
        return localCustomer_middlenameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_middlename() {
        return localCustomer_middlename;
    }

    /**
     * Auto generated setter method
     * @param param Customer_middlename
     */
    public void setCustomer_middlename(String param) {
        localCustomer_middlenameTracker = param != null;

        this.localCustomer_middlename = param;
    }

    public boolean isCustomer_prefixSpecified() {
        return localCustomer_prefixTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_prefix() {
        return localCustomer_prefix;
    }

    /**
     * Auto generated setter method
     * @param param Customer_prefix
     */
    public void setCustomer_prefix(String param) {
        localCustomer_prefixTracker = param != null;

        this.localCustomer_prefix = param;
    }

    public boolean isCustomer_suffixSpecified() {
        return localCustomer_suffixTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_suffix() {
        return localCustomer_suffix;
    }

    /**
     * Auto generated setter method
     * @param param Customer_suffix
     */
    public void setCustomer_suffix(String param) {
        localCustomer_suffixTracker = param != null;

        this.localCustomer_suffix = param;
    }

    public boolean isCustomer_taxvatSpecified() {
        return localCustomer_taxvatTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_taxvat() {
        return localCustomer_taxvat;
    }

    /**
     * Auto generated setter method
     * @param param Customer_taxvat
     */
    public void setCustomer_taxvat(String param) {
        localCustomer_taxvatTracker = param != null;

        this.localCustomer_taxvat = param;
    }

    public boolean isDiscount_descriptionSpecified() {
        return localDiscount_descriptionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getDiscount_description() {
        return localDiscount_description;
    }

    /**
     * Auto generated setter method
     * @param param Discount_description
     */
    public void setDiscount_description(String param) {
        localDiscount_descriptionTracker = param != null;

        this.localDiscount_description = param;
    }

    public boolean isExt_customer_idSpecified() {
        return localExt_customer_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getExt_customer_id() {
        return localExt_customer_id;
    }

    /**
     * Auto generated setter method
     * @param param Ext_customer_id
     */
    public void setExt_customer_id(String param) {
        localExt_customer_idTracker = param != null;

        this.localExt_customer_id = param;
    }

    public boolean isExt_order_idSpecified() {
        return localExt_order_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getExt_order_id() {
        return localExt_order_id;
    }

    /**
     * Auto generated setter method
     * @param param Ext_order_id
     */
    public void setExt_order_id(String param) {
        localExt_order_idTracker = param != null;

        this.localExt_order_id = param;
    }

    public boolean isHold_before_stateSpecified() {
        return localHold_before_stateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getHold_before_state() {
        return localHold_before_state;
    }

    /**
     * Auto generated setter method
     * @param param Hold_before_state
     */
    public void setHold_before_state(String param) {
        localHold_before_stateTracker = param != null;

        this.localHold_before_state = param;
    }

    public boolean isHold_before_statusSpecified() {
        return localHold_before_statusTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getHold_before_status() {
        return localHold_before_status;
    }

    /**
     * Auto generated setter method
     * @param param Hold_before_status
     */
    public void setHold_before_status(String param) {
        localHold_before_statusTracker = param != null;

        this.localHold_before_status = param;
    }

    public boolean isOriginal_increment_idSpecified() {
        return localOriginal_increment_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getOriginal_increment_id() {
        return localOriginal_increment_id;
    }

    /**
     * Auto generated setter method
     * @param param Original_increment_id
     */
    public void setOriginal_increment_id(String param) {
        localOriginal_increment_idTracker = param != null;

        this.localOriginal_increment_id = param;
    }

    public boolean isRelation_child_idSpecified() {
        return localRelation_child_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getRelation_child_id() {
        return localRelation_child_id;
    }

    /**
     * Auto generated setter method
     * @param param Relation_child_id
     */
    public void setRelation_child_id(String param) {
        localRelation_child_idTracker = param != null;

        this.localRelation_child_id = param;
    }

    public boolean isRelation_child_real_idSpecified() {
        return localRelation_child_real_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getRelation_child_real_id() {
        return localRelation_child_real_id;
    }

    /**
     * Auto generated setter method
     * @param param Relation_child_real_id
     */
    public void setRelation_child_real_id(String param) {
        localRelation_child_real_idTracker = param != null;

        this.localRelation_child_real_id = param;
    }

    public boolean isRelation_parent_idSpecified() {
        return localRelation_parent_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getRelation_parent_id() {
        return localRelation_parent_id;
    }

    /**
     * Auto generated setter method
     * @param param Relation_parent_id
     */
    public void setRelation_parent_id(String param) {
        localRelation_parent_idTracker = param != null;

        this.localRelation_parent_id = param;
    }

    public boolean isRelation_parent_real_idSpecified() {
        return localRelation_parent_real_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getRelation_parent_real_id() {
        return localRelation_parent_real_id;
    }

    /**
     * Auto generated setter method
     * @param param Relation_parent_real_id
     */
    public void setRelation_parent_real_id(String param) {
        localRelation_parent_real_idTracker = param != null;

        this.localRelation_parent_real_id = param;
    }

    public boolean isX_forwarded_forSpecified() {
        return localX_forwarded_forTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getX_forwarded_for() {
        return localX_forwarded_for;
    }

    /**
     * Auto generated setter method
     * @param param X_forwarded_for
     */
    public void setX_forwarded_for(String param) {
        localX_forwarded_forTracker = param != null;

        this.localX_forwarded_for = param;
    }

    public boolean isCustomer_noteSpecified() {
        return localCustomer_noteTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_note() {
        return localCustomer_note;
    }

    /**
     * Auto generated setter method
     * @param param Customer_note
     */
    public void setCustomer_note(String param) {
        localCustomer_noteTracker = param != null;

        this.localCustomer_note = param;
    }

    public boolean isTotal_item_countSpecified() {
        return localTotal_item_countTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_item_count() {
        return localTotal_item_count;
    }

    /**
     * Auto generated setter method
     * @param param Total_item_count
     */
    public void setTotal_item_count(String param) {
        localTotal_item_countTracker = param != null;

        this.localTotal_item_count = param;
    }

    public boolean isCustomer_genderSpecified() {
        return localCustomer_genderTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_gender() {
        return localCustomer_gender;
    }

    /**
     * Auto generated setter method
     * @param param Customer_gender
     */
    public void setCustomer_gender(String param) {
        localCustomer_genderTracker = param != null;

        this.localCustomer_gender = param;
    }

    public boolean isHidden_tax_amountSpecified() {
        return localHidden_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getHidden_tax_amount() {
        return localHidden_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Hidden_tax_amount
     */
    public void setHidden_tax_amount(String param) {
        localHidden_tax_amountTracker = param != null;

        this.localHidden_tax_amount = param;
    }

    public boolean isBase_hidden_tax_amountSpecified() {
        return localBase_hidden_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_hidden_tax_amount() {
        return localBase_hidden_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_hidden_tax_amount
     */
    public void setBase_hidden_tax_amount(String param) {
        localBase_hidden_tax_amountTracker = param != null;

        this.localBase_hidden_tax_amount = param;
    }

    public boolean isShipping_hidden_tax_amountSpecified() {
        return localShipping_hidden_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_hidden_tax_amount() {
        return localShipping_hidden_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_hidden_tax_amount
     */
    public void setShipping_hidden_tax_amount(String param) {
        localShipping_hidden_tax_amountTracker = param != null;

        this.localShipping_hidden_tax_amount = param;
    }

    public boolean isBase_shipping_hidden_tax_amountSpecified() {
        return localBase_shipping_hidden_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_hidden_tax_amount() {
        return localBase_shipping_hidden_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_hidden_tax_amount
     */
    public void setBase_shipping_hidden_tax_amount(String param) {
        localBase_shipping_hidden_tax_amountTracker = param != null;

        this.localBase_shipping_hidden_tax_amount = param;
    }

    public boolean isHidden_tax_invoicedSpecified() {
        return localHidden_tax_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getHidden_tax_invoiced() {
        return localHidden_tax_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Hidden_tax_invoiced
     */
    public void setHidden_tax_invoiced(String param) {
        localHidden_tax_invoicedTracker = param != null;

        this.localHidden_tax_invoiced = param;
    }

    public boolean isBase_hidden_tax_invoicedSpecified() {
        return localBase_hidden_tax_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_hidden_tax_invoiced() {
        return localBase_hidden_tax_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_hidden_tax_invoiced
     */
    public void setBase_hidden_tax_invoiced(String param) {
        localBase_hidden_tax_invoicedTracker = param != null;

        this.localBase_hidden_tax_invoiced = param;
    }

    public boolean isHidden_tax_refundedSpecified() {
        return localHidden_tax_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getHidden_tax_refunded() {
        return localHidden_tax_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Hidden_tax_refunded
     */
    public void setHidden_tax_refunded(String param) {
        localHidden_tax_refundedTracker = param != null;

        this.localHidden_tax_refunded = param;
    }

    public boolean isBase_hidden_tax_refundedSpecified() {
        return localBase_hidden_tax_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_hidden_tax_refunded() {
        return localBase_hidden_tax_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_hidden_tax_refunded
     */
    public void setBase_hidden_tax_refunded(String param) {
        localBase_hidden_tax_refundedTracker = param != null;

        this.localBase_hidden_tax_refunded = param;
    }

    public boolean isShipping_incl_taxSpecified() {
        return localShipping_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_incl_tax() {
        return localShipping_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_incl_tax
     */
    public void setShipping_incl_tax(String param) {
        localShipping_incl_taxTracker = param != null;

        this.localShipping_incl_tax = param;
    }

    public boolean isBase_shipping_incl_taxSpecified() {
        return localBase_shipping_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_shipping_incl_tax() {
        return localBase_shipping_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_incl_tax
     */
    public void setBase_shipping_incl_tax(String param) {
        localBase_shipping_incl_taxTracker = param != null;

        this.localBase_shipping_incl_tax = param;
    }

    public boolean isBase_customer_balance_amountSpecified() {
        return localBase_customer_balance_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_customer_balance_amount() {
        return localBase_customer_balance_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_customer_balance_amount
     */
    public void setBase_customer_balance_amount(String param) {
        localBase_customer_balance_amountTracker = param != null;

        this.localBase_customer_balance_amount = param;
    }

    public boolean isCustomer_balance_amountSpecified() {
        return localCustomer_balance_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_balance_amount() {
        return localCustomer_balance_amount;
    }

    /**
     * Auto generated setter method
     * @param param Customer_balance_amount
     */
    public void setCustomer_balance_amount(String param) {
        localCustomer_balance_amountTracker = param != null;

        this.localCustomer_balance_amount = param;
    }

    public boolean isBase_customer_balance_invoicedSpecified() {
        return localBase_customer_balance_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_customer_balance_invoiced() {
        return localBase_customer_balance_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_customer_balance_invoiced
     */
    public void setBase_customer_balance_invoiced(String param) {
        localBase_customer_balance_invoicedTracker = param != null;

        this.localBase_customer_balance_invoiced = param;
    }

    public boolean isCustomer_balance_invoicedSpecified() {
        return localCustomer_balance_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_balance_invoiced() {
        return localCustomer_balance_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Customer_balance_invoiced
     */
    public void setCustomer_balance_invoiced(String param) {
        localCustomer_balance_invoicedTracker = param != null;

        this.localCustomer_balance_invoiced = param;
    }

    public boolean isBase_customer_balance_refundedSpecified() {
        return localBase_customer_balance_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_customer_balance_refunded() {
        return localBase_customer_balance_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_customer_balance_refunded
     */
    public void setBase_customer_balance_refunded(String param) {
        localBase_customer_balance_refundedTracker = param != null;

        this.localBase_customer_balance_refunded = param;
    }

    public boolean isCustomer_balance_refundedSpecified() {
        return localCustomer_balance_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_balance_refunded() {
        return localCustomer_balance_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Customer_balance_refunded
     */
    public void setCustomer_balance_refunded(String param) {
        localCustomer_balance_refundedTracker = param != null;

        this.localCustomer_balance_refunded = param;
    }

    public boolean isBase_customer_balance_total_refundedSpecified() {
        return localBase_customer_balance_total_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_customer_balance_total_refunded() {
        return localBase_customer_balance_total_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_customer_balance_total_refunded
     */
    public void setBase_customer_balance_total_refunded(String param) {
        localBase_customer_balance_total_refundedTracker = param != null;

        this.localBase_customer_balance_total_refunded = param;
    }

    public boolean isCustomer_balance_total_refundedSpecified() {
        return localCustomer_balance_total_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCustomer_balance_total_refunded() {
        return localCustomer_balance_total_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Customer_balance_total_refunded
     */
    public void setCustomer_balance_total_refunded(String param) {
        localCustomer_balance_total_refundedTracker = param != null;

        this.localCustomer_balance_total_refunded = param;
    }

    public boolean isGift_cardsSpecified() {
        return localGift_cardsTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGift_cards() {
        return localGift_cards;
    }

    /**
     * Auto generated setter method
     * @param param Gift_cards
     */
    public void setGift_cards(String param) {
        localGift_cardsTracker = param != null;

        this.localGift_cards = param;
    }

    public boolean isBase_gift_cards_amountSpecified() {
        return localBase_gift_cards_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_gift_cards_amount() {
        return localBase_gift_cards_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_gift_cards_amount
     */
    public void setBase_gift_cards_amount(String param) {
        localBase_gift_cards_amountTracker = param != null;

        this.localBase_gift_cards_amount = param;
    }

    public boolean isGift_cards_amountSpecified() {
        return localGift_cards_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGift_cards_amount() {
        return localGift_cards_amount;
    }

    /**
     * Auto generated setter method
     * @param param Gift_cards_amount
     */
    public void setGift_cards_amount(String param) {
        localGift_cards_amountTracker = param != null;

        this.localGift_cards_amount = param;
    }

    public boolean isBase_gift_cards_invoicedSpecified() {
        return localBase_gift_cards_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_gift_cards_invoiced() {
        return localBase_gift_cards_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_gift_cards_invoiced
     */
    public void setBase_gift_cards_invoiced(String param) {
        localBase_gift_cards_invoicedTracker = param != null;

        this.localBase_gift_cards_invoiced = param;
    }

    public boolean isGift_cards_invoicedSpecified() {
        return localGift_cards_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGift_cards_invoiced() {
        return localGift_cards_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Gift_cards_invoiced
     */
    public void setGift_cards_invoiced(String param) {
        localGift_cards_invoicedTracker = param != null;

        this.localGift_cards_invoiced = param;
    }

    public boolean isBase_gift_cards_refundedSpecified() {
        return localBase_gift_cards_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_gift_cards_refunded() {
        return localBase_gift_cards_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_gift_cards_refunded
     */
    public void setBase_gift_cards_refunded(String param) {
        localBase_gift_cards_refundedTracker = param != null;

        this.localBase_gift_cards_refunded = param;
    }

    public boolean isGift_cards_refundedSpecified() {
        return localGift_cards_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getGift_cards_refunded() {
        return localGift_cards_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Gift_cards_refunded
     */
    public void setGift_cards_refunded(String param) {
        localGift_cards_refundedTracker = param != null;

        this.localGift_cards_refunded = param;
    }

    public boolean isReward_points_balanceSpecified() {
        return localReward_points_balanceTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_points_balance() {
        return localReward_points_balance;
    }

    /**
     * Auto generated setter method
     * @param param Reward_points_balance
     */
    public void setReward_points_balance(String param) {
        localReward_points_balanceTracker = param != null;

        this.localReward_points_balance = param;
    }

    public boolean isBase_reward_currency_amountSpecified() {
        return localBase_reward_currency_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_reward_currency_amount() {
        return localBase_reward_currency_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_reward_currency_amount
     */
    public void setBase_reward_currency_amount(String param) {
        localBase_reward_currency_amountTracker = param != null;

        this.localBase_reward_currency_amount = param;
    }

    public boolean isReward_currency_amountSpecified() {
        return localReward_currency_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_currency_amount() {
        return localReward_currency_amount;
    }

    /**
     * Auto generated setter method
     * @param param Reward_currency_amount
     */
    public void setReward_currency_amount(String param) {
        localReward_currency_amountTracker = param != null;

        this.localReward_currency_amount = param;
    }

    public boolean isBase_reward_currency_amount_invoicedSpecified() {
        return localBase_reward_currency_amount_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_reward_currency_amount_invoiced() {
        return localBase_reward_currency_amount_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Base_reward_currency_amount_invoiced
     */
    public void setBase_reward_currency_amount_invoiced(String param) {
        localBase_reward_currency_amount_invoicedTracker = param != null;

        this.localBase_reward_currency_amount_invoiced = param;
    }

    public boolean isReward_currency_amount_invoicedSpecified() {
        return localReward_currency_amount_invoicedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_currency_amount_invoiced() {
        return localReward_currency_amount_invoiced;
    }

    /**
     * Auto generated setter method
     * @param param Reward_currency_amount_invoiced
     */
    public void setReward_currency_amount_invoiced(String param) {
        localReward_currency_amount_invoicedTracker = param != null;

        this.localReward_currency_amount_invoiced = param;
    }

    public boolean isBase_reward_currency_amount_refundedSpecified() {
        return localBase_reward_currency_amount_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getBase_reward_currency_amount_refunded() {
        return localBase_reward_currency_amount_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Base_reward_currency_amount_refunded
     */
    public void setBase_reward_currency_amount_refunded(String param) {
        localBase_reward_currency_amount_refundedTracker = param != null;

        this.localBase_reward_currency_amount_refunded = param;
    }

    public boolean isReward_currency_amount_refundedSpecified() {
        return localReward_currency_amount_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_currency_amount_refunded() {
        return localReward_currency_amount_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Reward_currency_amount_refunded
     */
    public void setReward_currency_amount_refunded(String param) {
        localReward_currency_amount_refundedTracker = param != null;

        this.localReward_currency_amount_refunded = param;
    }

    public boolean isReward_points_balance_refundedSpecified() {
        return localReward_points_balance_refundedTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_points_balance_refunded() {
        return localReward_points_balance_refunded;
    }

    /**
     * Auto generated setter method
     * @param param Reward_points_balance_refunded
     */
    public void setReward_points_balance_refunded(String param) {
        localReward_points_balance_refundedTracker = param != null;

        this.localReward_points_balance_refunded = param;
    }

    public boolean isReward_points_balance_to_refundSpecified() {
        return localReward_points_balance_to_refundTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_points_balance_to_refund() {
        return localReward_points_balance_to_refund;
    }

    /**
     * Auto generated setter method
     * @param param Reward_points_balance_to_refund
     */
    public void setReward_points_balance_to_refund(String param) {
        localReward_points_balance_to_refundTracker = param != null;

        this.localReward_points_balance_to_refund = param;
    }

    public boolean isReward_salesrule_pointsSpecified() {
        return localReward_salesrule_pointsTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getReward_salesrule_points() {
        return localReward_salesrule_points;
    }

    /**
     * Auto generated setter method
     * @param param Reward_salesrule_points
     */
    public void setReward_salesrule_points(String param) {
        localReward_salesrule_pointsTracker = param != null;

        this.localReward_salesrule_points = param;
    }

    public boolean isFirstnameSpecified() {
        return localFirstnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getFirstname() {
        return localFirstname;
    }

    /**
     * Auto generated setter method
     * @param param Firstname
     */
    public void setFirstname(String param) {
        localFirstnameTracker = param != null;

        this.localFirstname = param;
    }

    public boolean isLastnameSpecified() {
        return localLastnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getLastname() {
        return localLastname;
    }

    /**
     * Auto generated setter method
     * @param param Lastname
     */
    public void setLastname(String param) {
        localLastnameTracker = param != null;

        this.localLastname = param;
    }

    public boolean isTelephoneSpecified() {
        return localTelephoneTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTelephone() {
        return localTelephone;
    }

    /**
     * Auto generated setter method
     * @param param Telephone
     */
    public void setTelephone(String param) {
        localTelephoneTracker = param != null;

        this.localTelephone = param;
    }

    public boolean isPostcodeSpecified() {
        return localPostcodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPostcode() {
        return localPostcode;
    }

    /**
     * Auto generated setter method
     * @param param Postcode
     */
    public void setPostcode(String param) {
        localPostcodeTracker = param != null;

        this.localPostcode = param;
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
                    namespacePrefix + ":salesOrderListEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "salesOrderListEntity", xmlWriter);
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

        if (localCoupon_codeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "coupon_code", xmlWriter);

            if (localCoupon_code == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "coupon_code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCoupon_code);
            }

            xmlWriter.writeEndElement();
        }

        if (localProtect_codeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "protect_code", xmlWriter);

            if (localProtect_code == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "protect_code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localProtect_code);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_discount_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_discount_canceled",
                xmlWriter);

            if (localBase_discount_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_discount_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_discount_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_discount_invoiced",
                xmlWriter);

            if (localBase_discount_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_discount_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_discount_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_discount_refunded",
                xmlWriter);

            if (localBase_discount_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_discount_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_canceled",
                xmlWriter);

            if (localBase_shipping_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_invoiced",
                xmlWriter);

            if (localBase_shipping_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_refunded",
                xmlWriter);

            if (localBase_shipping_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_tax_amount",
                xmlWriter);

            if (localBase_shipping_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_tax_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_tax_refunded",
                xmlWriter);

            if (localBase_shipping_tax_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_tax_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_tax_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_subtotal_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_subtotal_canceled",
                xmlWriter);

            if (localBase_subtotal_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_subtotal_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_subtotal_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_subtotal_invoiced",
                xmlWriter);

            if (localBase_subtotal_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_subtotal_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_subtotal_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_subtotal_refunded",
                xmlWriter);

            if (localBase_subtotal_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_subtotal_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_tax_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_tax_canceled", xmlWriter);

            if (localBase_tax_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_tax_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_tax_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_tax_invoiced", xmlWriter);

            if (localBase_tax_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_tax_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_tax_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_tax_refunded", xmlWriter);

            if (localBase_tax_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_tax_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_invoiced_costTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_invoiced_cost",
                xmlWriter);

            if (localBase_total_invoiced_cost == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_invoiced_cost cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_invoiced_cost);
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_canceled", xmlWriter);

            if (localDiscount_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDiscount_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_invoiced", xmlWriter);

            if (localDiscount_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDiscount_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_refunded", xmlWriter);

            if (localDiscount_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDiscount_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_canceled", xmlWriter);

            if (localShipping_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_invoiced", xmlWriter);

            if (localShipping_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_refunded", xmlWriter);

            if (localShipping_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_tax_amount", xmlWriter);

            if (localShipping_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_tax_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_tax_refunded",
                xmlWriter);

            if (localShipping_tax_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_tax_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_tax_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localSubtotal_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "subtotal_canceled", xmlWriter);

            if (localSubtotal_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSubtotal_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localSubtotal_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "subtotal_invoiced", xmlWriter);

            if (localSubtotal_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSubtotal_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localSubtotal_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "subtotal_refunded", xmlWriter);

            if (localSubtotal_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSubtotal_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_canceledTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_canceled", xmlWriter);

            if (localTax_canceled == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_canceled cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTax_canceled);
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_invoiced", xmlWriter);

            if (localTax_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTax_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_refunded", xmlWriter);

            if (localTax_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTax_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localCan_ship_partiallyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "can_ship_partially", xmlWriter);

            if (localCan_ship_partially == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "can_ship_partially cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCan_ship_partially);
            }

            xmlWriter.writeEndElement();
        }

        if (localCan_ship_partially_itemTracker) {
            namespace = "";
            writeStartElement(null, namespace, "can_ship_partially_item",
                xmlWriter);

            if (localCan_ship_partially_item == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "can_ship_partially_item cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCan_ship_partially_item);
            }

            xmlWriter.writeEndElement();
        }

        if (localEdit_incrementTracker) {
            namespace = "";
            writeStartElement(null, namespace, "edit_increment", xmlWriter);

            if (localEdit_increment == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "edit_increment cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localEdit_increment);
            }

            xmlWriter.writeEndElement();
        }

        if (localForced_do_shipment_with_invoiceTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "forced_do_shipment_with_invoice", xmlWriter);

            if (localForced_do_shipment_with_invoice == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "forced_do_shipment_with_invoice cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localForced_do_shipment_with_invoice);
            }

            xmlWriter.writeEndElement();
        }

        if (localPayment_authorization_expirationTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "payment_authorization_expiration", xmlWriter);

            if (localPayment_authorization_expiration == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "payment_authorization_expiration cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPayment_authorization_expiration);
            }

            xmlWriter.writeEndElement();
        }

        if (localPaypal_ipn_customer_notifiedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "paypal_ipn_customer_notified",
                xmlWriter);

            if (localPaypal_ipn_customer_notified == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "paypal_ipn_customer_notified cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPaypal_ipn_customer_notified);
            }

            xmlWriter.writeEndElement();
        }

        if (localQuote_address_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "quote_address_id", xmlWriter);

            if (localQuote_address_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "quote_address_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localQuote_address_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localAdjustment_negativeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "adjustment_negative", xmlWriter);

            if (localAdjustment_negative == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "adjustment_negative cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localAdjustment_negative);
            }

            xmlWriter.writeEndElement();
        }

        if (localAdjustment_positiveTracker) {
            namespace = "";
            writeStartElement(null, namespace, "adjustment_positive", xmlWriter);

            if (localAdjustment_positive == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "adjustment_positive cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localAdjustment_positive);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_adjustment_negativeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_adjustment_negative",
                xmlWriter);

            if (localBase_adjustment_negative == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_adjustment_negative cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_adjustment_negative);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_adjustment_positiveTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_adjustment_positive",
                xmlWriter);

            if (localBase_adjustment_positive == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_adjustment_positive cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_adjustment_positive);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_discount_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_discount_amount",
                xmlWriter);

            if (localBase_shipping_discount_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_discount_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_discount_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_subtotal_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_subtotal_incl_tax",
                xmlWriter);

            if (localBase_subtotal_incl_tax == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_subtotal_incl_tax);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_total_dueTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_total_due", xmlWriter);

            if (localBase_total_due == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_due cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_total_due);
            }

            xmlWriter.writeEndElement();
        }

        if (localPayment_authorization_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "payment_authorization_amount",
                xmlWriter);

            if (localPayment_authorization_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "payment_authorization_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPayment_authorization_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_discount_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_discount_amount",
                xmlWriter);

            if (localShipping_discount_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_discount_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_discount_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localSubtotal_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "subtotal_incl_tax", xmlWriter);

            if (localSubtotal_incl_tax == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSubtotal_incl_tax);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_dueTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_due", xmlWriter);

            if (localTotal_due == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_due cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_due);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_dobTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_dob", xmlWriter);

            if (localCustomer_dob == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_dob cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_dob);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_middlenameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_middlename", xmlWriter);

            if (localCustomer_middlename == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_middlename cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_middlename);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_prefixTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_prefix", xmlWriter);

            if (localCustomer_prefix == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_prefix cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_prefix);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_suffixTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_suffix", xmlWriter);

            if (localCustomer_suffix == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_suffix cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_suffix);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_taxvatTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_taxvat", xmlWriter);

            if (localCustomer_taxvat == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_taxvat cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_taxvat);
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_descriptionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_description", xmlWriter);

            if (localDiscount_description == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_description cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDiscount_description);
            }

            xmlWriter.writeEndElement();
        }

        if (localExt_customer_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "ext_customer_id", xmlWriter);

            if (localExt_customer_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "ext_customer_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localExt_customer_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localExt_order_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "ext_order_id", xmlWriter);

            if (localExt_order_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "ext_order_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localExt_order_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localHold_before_stateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "hold_before_state", xmlWriter);

            if (localHold_before_state == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "hold_before_state cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localHold_before_state);
            }

            xmlWriter.writeEndElement();
        }

        if (localHold_before_statusTracker) {
            namespace = "";
            writeStartElement(null, namespace, "hold_before_status", xmlWriter);

            if (localHold_before_status == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "hold_before_status cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localHold_before_status);
            }

            xmlWriter.writeEndElement();
        }

        if (localOriginal_increment_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "original_increment_id",
                xmlWriter);

            if (localOriginal_increment_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "original_increment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOriginal_increment_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localRelation_child_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "relation_child_id", xmlWriter);

            if (localRelation_child_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_child_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRelation_child_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localRelation_child_real_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "relation_child_real_id",
                xmlWriter);

            if (localRelation_child_real_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_child_real_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRelation_child_real_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localRelation_parent_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "relation_parent_id", xmlWriter);

            if (localRelation_parent_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_parent_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRelation_parent_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localRelation_parent_real_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "relation_parent_real_id",
                xmlWriter);

            if (localRelation_parent_real_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_parent_real_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRelation_parent_real_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localX_forwarded_forTracker) {
            namespace = "";
            writeStartElement(null, namespace, "x_forwarded_for", xmlWriter);

            if (localX_forwarded_for == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "x_forwarded_for cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localX_forwarded_for);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_noteTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_note", xmlWriter);

            if (localCustomer_note == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_note cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_note);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_item_countTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_item_count", xmlWriter);

            if (localTotal_item_count == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_item_count cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_item_count);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_genderTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_gender", xmlWriter);

            if (localCustomer_gender == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_gender cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_gender);
            }

            xmlWriter.writeEndElement();
        }

        if (localHidden_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "hidden_tax_amount", xmlWriter);

            if (localHidden_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "hidden_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localHidden_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_hidden_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_hidden_tax_amount",
                xmlWriter);

            if (localBase_hidden_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_hidden_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_hidden_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_hidden_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_hidden_tax_amount",
                xmlWriter);

            if (localShipping_hidden_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_hidden_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_hidden_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_hidden_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_shipping_hidden_tax_amount", xmlWriter);

            if (localBase_shipping_hidden_tax_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_hidden_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_hidden_tax_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localHidden_tax_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "hidden_tax_invoiced", xmlWriter);

            if (localHidden_tax_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "hidden_tax_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localHidden_tax_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_hidden_tax_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_hidden_tax_invoiced",
                xmlWriter);

            if (localBase_hidden_tax_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_hidden_tax_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_hidden_tax_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localHidden_tax_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "hidden_tax_refunded", xmlWriter);

            if (localHidden_tax_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "hidden_tax_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localHidden_tax_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_hidden_tax_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_hidden_tax_refunded",
                xmlWriter);

            if (localBase_hidden_tax_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_hidden_tax_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_hidden_tax_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_incl_tax", xmlWriter);

            if (localShipping_incl_tax == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_incl_tax);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_shipping_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_shipping_incl_tax",
                xmlWriter);

            if (localBase_shipping_incl_tax == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_shipping_incl_tax);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_customer_balance_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_customer_balance_amount",
                xmlWriter);

            if (localBase_customer_balance_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_customer_balance_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_balance_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_balance_amount",
                xmlWriter);

            if (localCustomer_balance_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_balance_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_customer_balance_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_customer_balance_invoiced", xmlWriter);

            if (localBase_customer_balance_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_customer_balance_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_balance_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_balance_invoiced",
                xmlWriter);

            if (localCustomer_balance_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_balance_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_customer_balance_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_customer_balance_refunded", xmlWriter);

            if (localBase_customer_balance_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_customer_balance_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_balance_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_balance_refunded",
                xmlWriter);

            if (localCustomer_balance_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_balance_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_customer_balance_total_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_customer_balance_total_refunded", xmlWriter);

            if (localBase_customer_balance_total_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_total_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_customer_balance_total_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_balance_total_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "customer_balance_total_refunded", xmlWriter);

            if (localCustomer_balance_total_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_total_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustomer_balance_total_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localGift_cardsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_cards", xmlWriter);

            if (localGift_cards == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_cards);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_gift_cards_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_gift_cards_amount",
                xmlWriter);

            if (localBase_gift_cards_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_gift_cards_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_gift_cards_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localGift_cards_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_cards_amount", xmlWriter);

            if (localGift_cards_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_cards_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_gift_cards_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_gift_cards_invoiced",
                xmlWriter);

            if (localBase_gift_cards_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_gift_cards_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_gift_cards_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localGift_cards_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_cards_invoiced", xmlWriter);

            if (localGift_cards_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_cards_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_gift_cards_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_gift_cards_refunded",
                xmlWriter);

            if (localBase_gift_cards_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_gift_cards_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_gift_cards_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localGift_cards_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_cards_refunded", xmlWriter);

            if (localGift_cards_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_cards_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_points_balanceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "reward_points_balance",
                xmlWriter);

            if (localReward_points_balance == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_points_balance cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_points_balance);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_reward_currency_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_reward_currency_amount",
                xmlWriter);

            if (localBase_reward_currency_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_reward_currency_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_reward_currency_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_currency_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "reward_currency_amount",
                xmlWriter);

            if (localReward_currency_amount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_currency_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_currency_amount);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_reward_currency_amount_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_reward_currency_amount_invoiced", xmlWriter);

            if (localBase_reward_currency_amount_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_reward_currency_amount_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_reward_currency_amount_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_currency_amount_invoicedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "reward_currency_amount_invoiced", xmlWriter);

            if (localReward_currency_amount_invoiced == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_currency_amount_invoiced cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_currency_amount_invoiced);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_reward_currency_amount_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_reward_currency_amount_refunded", xmlWriter);

            if (localBase_reward_currency_amount_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "base_reward_currency_amount_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localBase_reward_currency_amount_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_currency_amount_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "reward_currency_amount_refunded", xmlWriter);

            if (localReward_currency_amount_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_currency_amount_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_currency_amount_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_points_balance_refundedTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "reward_points_balance_refunded", xmlWriter);

            if (localReward_points_balance_refunded == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_points_balance_refunded cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_points_balance_refunded);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_points_balance_to_refundTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "reward_points_balance_to_refund", xmlWriter);

            if (localReward_points_balance_to_refund == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_points_balance_to_refund cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_points_balance_to_refund);
            }

            xmlWriter.writeEndElement();
        }

        if (localReward_salesrule_pointsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "reward_salesrule_points",
                xmlWriter);

            if (localReward_salesrule_points == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_salesrule_points cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localReward_salesrule_points);
            }

            xmlWriter.writeEndElement();
        }

        if (localFirstnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "firstname", xmlWriter);

            if (localFirstname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "firstname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localFirstname);
            }

            xmlWriter.writeEndElement();
        }

        if (localLastnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "lastname", xmlWriter);

            if (localLastname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "lastname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localLastname);
            }

            xmlWriter.writeEndElement();
        }

        if (localTelephoneTracker) {
            namespace = "";
            writeStartElement(null, namespace, "telephone", xmlWriter);

            if (localTelephone == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "telephone cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTelephone);
            }

            xmlWriter.writeEndElement();
        }

        if (localPostcodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "postcode", xmlWriter);

            if (localPostcode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "postcode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPostcode);
            }

            xmlWriter.writeEndElement();
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

        if (localCoupon_codeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "coupon_code"));

            if (localCoupon_code != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCoupon_code));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "coupon_code cannot be null!!");
            }
        }

        if (localProtect_codeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "protect_code"));

            if (localProtect_code != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localProtect_code));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "protect_code cannot be null!!");
            }
        }

        if (localBase_discount_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_discount_canceled"));

            if (localBase_discount_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_discount_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_canceled cannot be null!!");
            }
        }

        if (localBase_discount_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_discount_invoiced"));

            if (localBase_discount_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_discount_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_invoiced cannot be null!!");
            }
        }

        if (localBase_discount_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_discount_refunded"));

            if (localBase_discount_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_discount_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_refunded cannot be null!!");
            }
        }

        if (localBase_shipping_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_canceled"));

            if (localBase_shipping_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_canceled cannot be null!!");
            }
        }

        if (localBase_shipping_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_invoiced"));

            if (localBase_shipping_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_invoiced cannot be null!!");
            }
        }

        if (localBase_shipping_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_refunded"));

            if (localBase_shipping_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_refunded cannot be null!!");
            }
        }

        if (localBase_shipping_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_tax_amount"));

            if (localBase_shipping_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_tax_amount cannot be null!!");
            }
        }

        if (localBase_shipping_tax_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_tax_refunded"));

            if (localBase_shipping_tax_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_tax_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_tax_refunded cannot be null!!");
            }
        }

        if (localBase_subtotal_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_subtotal_canceled"));

            if (localBase_subtotal_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_subtotal_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_canceled cannot be null!!");
            }
        }

        if (localBase_subtotal_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_subtotal_invoiced"));

            if (localBase_subtotal_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_subtotal_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_invoiced cannot be null!!");
            }
        }

        if (localBase_subtotal_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_subtotal_refunded"));

            if (localBase_subtotal_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_subtotal_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_refunded cannot be null!!");
            }
        }

        if (localBase_tax_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_tax_canceled"));

            if (localBase_tax_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_tax_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_canceled cannot be null!!");
            }
        }

        if (localBase_tax_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_tax_invoiced"));

            if (localBase_tax_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_tax_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_invoiced cannot be null!!");
            }
        }

        if (localBase_tax_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_tax_refunded"));

            if (localBase_tax_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_tax_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_refunded cannot be null!!");
            }
        }

        if (localBase_total_invoiced_costTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_total_invoiced_cost"));

            if (localBase_total_invoiced_cost != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_invoiced_cost));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_invoiced_cost cannot be null!!");
            }
        }

        if (localDiscount_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "discount_canceled"));

            if (localDiscount_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_canceled cannot be null!!");
            }
        }

        if (localDiscount_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "discount_invoiced"));

            if (localDiscount_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_invoiced cannot be null!!");
            }
        }

        if (localDiscount_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "discount_refunded"));

            if (localDiscount_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_refunded cannot be null!!");
            }
        }

        if (localShipping_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_canceled"));

            if (localShipping_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_canceled cannot be null!!");
            }
        }

        if (localShipping_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_invoiced"));

            if (localShipping_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_invoiced cannot be null!!");
            }
        }

        if (localShipping_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_refunded"));

            if (localShipping_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_refunded cannot be null!!");
            }
        }

        if (localShipping_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_tax_amount"));

            if (localShipping_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_tax_amount cannot be null!!");
            }
        }

        if (localShipping_tax_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_tax_refunded"));

            if (localShipping_tax_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_tax_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_tax_refunded cannot be null!!");
            }
        }

        if (localSubtotal_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "subtotal_canceled"));

            if (localSubtotal_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSubtotal_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_canceled cannot be null!!");
            }
        }

        if (localSubtotal_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "subtotal_invoiced"));

            if (localSubtotal_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSubtotal_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_invoiced cannot be null!!");
            }
        }

        if (localSubtotal_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "subtotal_refunded"));

            if (localSubtotal_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSubtotal_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_refunded cannot be null!!");
            }
        }

        if (localTax_canceledTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_canceled"));

            if (localTax_canceled != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_canceled));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_canceled cannot be null!!");
            }
        }

        if (localTax_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_invoiced"));

            if (localTax_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_invoiced cannot be null!!");
            }
        }

        if (localTax_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_refunded"));

            if (localTax_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_refunded cannot be null!!");
            }
        }

        if (localCan_ship_partiallyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "can_ship_partially"));

            if (localCan_ship_partially != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCan_ship_partially));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "can_ship_partially cannot be null!!");
            }
        }

        if (localCan_ship_partially_itemTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "can_ship_partially_item"));

            if (localCan_ship_partially_item != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCan_ship_partially_item));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "can_ship_partially_item cannot be null!!");
            }
        }

        if (localEdit_incrementTracker) {
            elementList.add(new javax.xml.namespace.QName("", "edit_increment"));

            if (localEdit_increment != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localEdit_increment));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "edit_increment cannot be null!!");
            }
        }

        if (localForced_do_shipment_with_invoiceTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "forced_do_shipment_with_invoice"));

            if (localForced_do_shipment_with_invoice != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localForced_do_shipment_with_invoice));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "forced_do_shipment_with_invoice cannot be null!!");
            }
        }

        if (localPayment_authorization_expirationTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "payment_authorization_expiration"));

            if (localPayment_authorization_expiration != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPayment_authorization_expiration));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "payment_authorization_expiration cannot be null!!");
            }
        }

        if (localPaypal_ipn_customer_notifiedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "paypal_ipn_customer_notified"));

            if (localPaypal_ipn_customer_notified != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPaypal_ipn_customer_notified));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "paypal_ipn_customer_notified cannot be null!!");
            }
        }

        if (localQuote_address_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "quote_address_id"));

            if (localQuote_address_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localQuote_address_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "quote_address_id cannot be null!!");
            }
        }

        if (localAdjustment_negativeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "adjustment_negative"));

            if (localAdjustment_negative != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localAdjustment_negative));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "adjustment_negative cannot be null!!");
            }
        }

        if (localAdjustment_positiveTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "adjustment_positive"));

            if (localAdjustment_positive != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localAdjustment_positive));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "adjustment_positive cannot be null!!");
            }
        }

        if (localBase_adjustment_negativeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_adjustment_negative"));

            if (localBase_adjustment_negative != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_adjustment_negative));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_adjustment_negative cannot be null!!");
            }
        }

        if (localBase_adjustment_positiveTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_adjustment_positive"));

            if (localBase_adjustment_positive != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_adjustment_positive));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_adjustment_positive cannot be null!!");
            }
        }

        if (localBase_shipping_discount_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_discount_amount"));

            if (localBase_shipping_discount_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_discount_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_discount_amount cannot be null!!");
            }
        }

        if (localBase_subtotal_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_subtotal_incl_tax"));

            if (localBase_subtotal_incl_tax != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_subtotal_incl_tax));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_subtotal_incl_tax cannot be null!!");
            }
        }

        if (localBase_total_dueTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_total_due"));

            if (localBase_total_due != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_total_due));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_total_due cannot be null!!");
            }
        }

        if (localPayment_authorization_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "payment_authorization_amount"));

            if (localPayment_authorization_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPayment_authorization_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "payment_authorization_amount cannot be null!!");
            }
        }

        if (localShipping_discount_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_discount_amount"));

            if (localShipping_discount_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_discount_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_discount_amount cannot be null!!");
            }
        }

        if (localSubtotal_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "subtotal_incl_tax"));

            if (localSubtotal_incl_tax != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSubtotal_incl_tax));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "subtotal_incl_tax cannot be null!!");
            }
        }

        if (localTotal_dueTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_due"));

            if (localTotal_due != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_due));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_due cannot be null!!");
            }
        }

        if (localCustomer_dobTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_dob"));

            if (localCustomer_dob != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_dob));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_dob cannot be null!!");
            }
        }

        if (localCustomer_middlenameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_middlename"));

            if (localCustomer_middlename != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_middlename));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_middlename cannot be null!!");
            }
        }

        if (localCustomer_prefixTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_prefix"));

            if (localCustomer_prefix != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_prefix));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_prefix cannot be null!!");
            }
        }

        if (localCustomer_suffixTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_suffix"));

            if (localCustomer_suffix != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_suffix));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_suffix cannot be null!!");
            }
        }

        if (localCustomer_taxvatTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_taxvat"));

            if (localCustomer_taxvat != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_taxvat));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_taxvat cannot be null!!");
            }
        }

        if (localDiscount_descriptionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "discount_description"));

            if (localDiscount_description != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_description));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_description cannot be null!!");
            }
        }

        if (localExt_customer_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "ext_customer_id"));

            if (localExt_customer_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localExt_customer_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "ext_customer_id cannot be null!!");
            }
        }

        if (localExt_order_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "ext_order_id"));

            if (localExt_order_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localExt_order_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "ext_order_id cannot be null!!");
            }
        }

        if (localHold_before_stateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "hold_before_state"));

            if (localHold_before_state != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localHold_before_state));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "hold_before_state cannot be null!!");
            }
        }

        if (localHold_before_statusTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "hold_before_status"));

            if (localHold_before_status != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localHold_before_status));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "hold_before_status cannot be null!!");
            }
        }

        if (localOriginal_increment_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "original_increment_id"));

            if (localOriginal_increment_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOriginal_increment_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "original_increment_id cannot be null!!");
            }
        }

        if (localRelation_child_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "relation_child_id"));

            if (localRelation_child_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRelation_child_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_child_id cannot be null!!");
            }
        }

        if (localRelation_child_real_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "relation_child_real_id"));

            if (localRelation_child_real_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRelation_child_real_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_child_real_id cannot be null!!");
            }
        }

        if (localRelation_parent_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "relation_parent_id"));

            if (localRelation_parent_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRelation_parent_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_parent_id cannot be null!!");
            }
        }

        if (localRelation_parent_real_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "relation_parent_real_id"));

            if (localRelation_parent_real_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRelation_parent_real_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "relation_parent_real_id cannot be null!!");
            }
        }

        if (localX_forwarded_forTracker) {
            elementList.add(new javax.xml.namespace.QName("", "x_forwarded_for"));

            if (localX_forwarded_for != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localX_forwarded_for));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "x_forwarded_for cannot be null!!");
            }
        }

        if (localCustomer_noteTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_note"));

            if (localCustomer_note != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_note));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_note cannot be null!!");
            }
        }

        if (localTotal_item_countTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_item_count"));

            if (localTotal_item_count != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_item_count));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_item_count cannot be null!!");
            }
        }

        if (localCustomer_genderTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_gender"));

            if (localCustomer_gender != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_gender));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_gender cannot be null!!");
            }
        }

        if (localHidden_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "hidden_tax_amount"));

            if (localHidden_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localHidden_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "hidden_tax_amount cannot be null!!");
            }
        }

        if (localBase_hidden_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_hidden_tax_amount"));

            if (localBase_hidden_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_hidden_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_hidden_tax_amount cannot be null!!");
            }
        }

        if (localShipping_hidden_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_hidden_tax_amount"));

            if (localShipping_hidden_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_hidden_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_hidden_tax_amount cannot be null!!");
            }
        }

        if (localBase_shipping_hidden_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_hidden_tax_amount"));

            if (localBase_shipping_hidden_tax_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_hidden_tax_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_hidden_tax_amount cannot be null!!");
            }
        }

        if (localHidden_tax_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "hidden_tax_invoiced"));

            if (localHidden_tax_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localHidden_tax_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "hidden_tax_invoiced cannot be null!!");
            }
        }

        if (localBase_hidden_tax_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_hidden_tax_invoiced"));

            if (localBase_hidden_tax_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_hidden_tax_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_hidden_tax_invoiced cannot be null!!");
            }
        }

        if (localHidden_tax_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "hidden_tax_refunded"));

            if (localHidden_tax_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localHidden_tax_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "hidden_tax_refunded cannot be null!!");
            }
        }

        if (localBase_hidden_tax_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_hidden_tax_refunded"));

            if (localBase_hidden_tax_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_hidden_tax_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_hidden_tax_refunded cannot be null!!");
            }
        }

        if (localShipping_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_incl_tax"));

            if (localShipping_incl_tax != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_incl_tax));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_incl_tax cannot be null!!");
            }
        }

        if (localBase_shipping_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_shipping_incl_tax"));

            if (localBase_shipping_incl_tax != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_shipping_incl_tax));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_shipping_incl_tax cannot be null!!");
            }
        }

        if (localBase_customer_balance_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_customer_balance_amount"));

            if (localBase_customer_balance_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_customer_balance_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_amount cannot be null!!");
            }
        }

        if (localCustomer_balance_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_balance_amount"));

            if (localCustomer_balance_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_balance_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_amount cannot be null!!");
            }
        }

        if (localBase_customer_balance_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_customer_balance_invoiced"));

            if (localBase_customer_balance_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_customer_balance_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_invoiced cannot be null!!");
            }
        }

        if (localCustomer_balance_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_balance_invoiced"));

            if (localCustomer_balance_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_balance_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_invoiced cannot be null!!");
            }
        }

        if (localBase_customer_balance_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_customer_balance_refunded"));

            if (localBase_customer_balance_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_customer_balance_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_refunded cannot be null!!");
            }
        }

        if (localCustomer_balance_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_balance_refunded"));

            if (localCustomer_balance_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_balance_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_refunded cannot be null!!");
            }
        }

        if (localBase_customer_balance_total_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_customer_balance_total_refunded"));

            if (localBase_customer_balance_total_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_customer_balance_total_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_customer_balance_total_refunded cannot be null!!");
            }
        }

        if (localCustomer_balance_total_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "customer_balance_total_refunded"));

            if (localCustomer_balance_total_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_balance_total_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_balance_total_refunded cannot be null!!");
            }
        }

        if (localGift_cardsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "gift_cards"));

            if (localGift_cards != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_cards));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards cannot be null!!");
            }
        }

        if (localBase_gift_cards_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_gift_cards_amount"));

            if (localBase_gift_cards_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_gift_cards_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_gift_cards_amount cannot be null!!");
            }
        }

        if (localGift_cards_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "gift_cards_amount"));

            if (localGift_cards_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_cards_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards_amount cannot be null!!");
            }
        }

        if (localBase_gift_cards_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_gift_cards_invoiced"));

            if (localBase_gift_cards_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_gift_cards_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_gift_cards_invoiced cannot be null!!");
            }
        }

        if (localGift_cards_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "gift_cards_invoiced"));

            if (localGift_cards_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_cards_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards_invoiced cannot be null!!");
            }
        }

        if (localBase_gift_cards_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_gift_cards_refunded"));

            if (localBase_gift_cards_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_gift_cards_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_gift_cards_refunded cannot be null!!");
            }
        }

        if (localGift_cards_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "gift_cards_refunded"));

            if (localGift_cards_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_cards_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_cards_refunded cannot be null!!");
            }
        }

        if (localReward_points_balanceTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_points_balance"));

            if (localReward_points_balance != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_points_balance));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_points_balance cannot be null!!");
            }
        }

        if (localBase_reward_currency_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_reward_currency_amount"));

            if (localBase_reward_currency_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_reward_currency_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_reward_currency_amount cannot be null!!");
            }
        }

        if (localReward_currency_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_currency_amount"));

            if (localReward_currency_amount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_currency_amount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_currency_amount cannot be null!!");
            }
        }

        if (localBase_reward_currency_amount_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_reward_currency_amount_invoiced"));

            if (localBase_reward_currency_amount_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_reward_currency_amount_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_reward_currency_amount_invoiced cannot be null!!");
            }
        }

        if (localReward_currency_amount_invoicedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_currency_amount_invoiced"));

            if (localReward_currency_amount_invoiced != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_currency_amount_invoiced));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_currency_amount_invoiced cannot be null!!");
            }
        }

        if (localBase_reward_currency_amount_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_reward_currency_amount_refunded"));

            if (localBase_reward_currency_amount_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_reward_currency_amount_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_reward_currency_amount_refunded cannot be null!!");
            }
        }

        if (localReward_currency_amount_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_currency_amount_refunded"));

            if (localReward_currency_amount_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_currency_amount_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_currency_amount_refunded cannot be null!!");
            }
        }

        if (localReward_points_balance_refundedTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_points_balance_refunded"));

            if (localReward_points_balance_refunded != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_points_balance_refunded));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_points_balance_refunded cannot be null!!");
            }
        }

        if (localReward_points_balance_to_refundTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_points_balance_to_refund"));

            if (localReward_points_balance_to_refund != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_points_balance_to_refund));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_points_balance_to_refund cannot be null!!");
            }
        }

        if (localReward_salesrule_pointsTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "reward_salesrule_points"));

            if (localReward_salesrule_points != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localReward_salesrule_points));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "reward_salesrule_points cannot be null!!");
            }
        }

        if (localFirstnameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "firstname"));

            if (localFirstname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localFirstname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "firstname cannot be null!!");
            }
        }

        if (localLastnameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "lastname"));

            if (localLastname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localLastname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "lastname cannot be null!!");
            }
        }

        if (localTelephoneTracker) {
            elementList.add(new javax.xml.namespace.QName("", "telephone"));

            if (localTelephone != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTelephone));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "telephone cannot be null!!");
            }
        }

        if (localPostcodeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "postcode"));

            if (localPostcode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPostcode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "postcode cannot be null!!");
            }
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
        public static SalesOrderListEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            SalesOrderListEntity object = new SalesOrderListEntity();

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

                        if (!"salesOrderListEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (SalesOrderListEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "coupon_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "coupon_code" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCoupon_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "protect_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "protect_code" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setProtect_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_discount_canceled").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_discount_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_discount_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_discount_invoiced").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_discount_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_discount_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_discount_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_discount_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_discount_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_canceled").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_invoiced").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_tax_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_tax_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_tax_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_tax_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_subtotal_canceled").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_subtotal_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_subtotal_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_subtotal_invoiced").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_subtotal_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_subtotal_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_subtotal_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_subtotal_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_subtotal_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_tax_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_tax_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_tax_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_tax_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_tax_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_tax_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_tax_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_tax_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_tax_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_total_invoiced_cost").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_invoiced_cost" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_invoiced_cost(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "discount_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "discount_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setDiscount_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "discount_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "discount_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setDiscount_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "discount_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "discount_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setDiscount_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_tax_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "shipping_tax_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_tax_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_tax_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "subtotal_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "subtotal_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setSubtotal_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "subtotal_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "subtotal_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setSubtotal_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "subtotal_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "subtotal_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setSubtotal_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_canceled").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_canceled" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTax_canceled(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTax_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTax_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "can_ship_partially").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "can_ship_partially" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCan_ship_partially(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "can_ship_partially_item").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "can_ship_partially_item" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCan_ship_partially_item(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "edit_increment").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "edit_increment" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setEdit_increment(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "forced_do_shipment_with_invoice").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "forced_do_shipment_with_invoice" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setForced_do_shipment_with_invoice(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "payment_authorization_expiration").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "payment_authorization_expiration" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPayment_authorization_expiration(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "paypal_ipn_customer_notified").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "paypal_ipn_customer_notified" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPaypal_ipn_customer_notified(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "quote_address_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "quote_address_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setQuote_address_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "adjustment_negative").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "adjustment_negative" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setAdjustment_negative(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "adjustment_positive").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "adjustment_positive" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setAdjustment_positive(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_adjustment_negative").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_adjustment_negative" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_adjustment_negative(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_adjustment_positive").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_adjustment_positive" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_adjustment_positive(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_discount_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_discount_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_discount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_subtotal_incl_tax").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_subtotal_incl_tax" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_subtotal_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_total_due").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_total_due" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_total_due(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "payment_authorization_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "payment_authorization_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPayment_authorization_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "shipping_discount_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_discount_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_discount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "subtotal_incl_tax").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "subtotal_incl_tax" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setSubtotal_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_due").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_due" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_due(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_dob").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_dob" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_dob(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_middlename").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_middlename" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_middlename(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_prefix").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_prefix" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_prefix(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_suffix").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_suffix" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_suffix(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_taxvat").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_taxvat" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_taxvat(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "discount_description").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "discount_description" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setDiscount_description(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "ext_customer_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "ext_customer_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setExt_customer_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "ext_order_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "ext_order_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setExt_order_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "hold_before_state").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "hold_before_state" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setHold_before_state(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "hold_before_status").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "hold_before_status" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setHold_before_status(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "original_increment_id").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "original_increment_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setOriginal_increment_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "relation_child_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "relation_child_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setRelation_child_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "relation_child_real_id").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "relation_child_real_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setRelation_child_real_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "relation_parent_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "relation_parent_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setRelation_parent_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "relation_parent_real_id").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "relation_parent_real_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setRelation_parent_real_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "x_forwarded_for").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "x_forwarded_for" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setX_forwarded_for(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_note").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_note" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_note(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_item_count").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_item_count" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_item_count(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "customer_gender").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_gender" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_gender(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "hidden_tax_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "hidden_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setHidden_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_hidden_tax_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_hidden_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_hidden_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "shipping_hidden_tax_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_hidden_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_hidden_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_hidden_tax_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "base_shipping_hidden_tax_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_hidden_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "hidden_tax_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "hidden_tax_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setHidden_tax_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_hidden_tax_invoiced").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_hidden_tax_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_hidden_tax_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "hidden_tax_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "hidden_tax_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setHidden_tax_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_hidden_tax_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_hidden_tax_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_hidden_tax_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_incl_tax").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_incl_tax" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_shipping_incl_tax").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_shipping_incl_tax" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_shipping_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_customer_balance_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_customer_balance_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_customer_balance_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "customer_balance_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_balance_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_balance_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_customer_balance_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_customer_balance_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_customer_balance_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "customer_balance_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_balance_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_balance_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_customer_balance_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_customer_balance_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_customer_balance_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "customer_balance_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_balance_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_balance_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_customer_balance_total_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "base_customer_balance_total_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_customer_balance_total_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "customer_balance_total_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "customer_balance_total_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCustomer_balance_total_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gift_cards").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_cards" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGift_cards(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_gift_cards_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_gift_cards_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_gift_cards_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gift_cards_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_cards_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGift_cards_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_gift_cards_invoiced").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_gift_cards_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_gift_cards_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gift_cards_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_cards_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGift_cards_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_gift_cards_refunded").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_gift_cards_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_gift_cards_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gift_cards_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_cards_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGift_cards_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_points_balance").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "reward_points_balance" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_points_balance(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_reward_currency_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_reward_currency_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_reward_currency_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_currency_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "reward_currency_amount" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_currency_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_reward_currency_amount_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "base_reward_currency_amount_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_reward_currency_amount_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_currency_amount_invoiced").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "reward_currency_amount_invoiced" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_currency_amount_invoiced(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_reward_currency_amount_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "base_reward_currency_amount_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBase_reward_currency_amount_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_currency_amount_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "reward_currency_amount_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_currency_amount_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_points_balance_refunded").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "reward_points_balance_refunded" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_points_balance_refunded(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_points_balance_to_refund").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "reward_points_balance_to_refund" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_points_balance_to_refund(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "reward_salesrule_points").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "reward_salesrule_points" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setReward_salesrule_points(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "firstname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "firstname" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setFirstname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "lastname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "lastname" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setLastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "telephone").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "telephone" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTelephone(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "postcode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "postcode" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPostcode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

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
