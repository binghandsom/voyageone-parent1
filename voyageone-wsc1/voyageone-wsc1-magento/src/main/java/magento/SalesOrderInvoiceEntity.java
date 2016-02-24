/**
 * SalesOrderInvoiceEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  SalesOrderInvoiceEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class SalesOrderInvoiceEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = salesOrderInvoiceEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Increment_id
     */
    protected java.lang.String localIncrement_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIncrement_idTracker = false;

    /**
     * field for Parent_id
     */
    protected java.lang.String localParent_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localParent_idTracker = false;

    /**
     * field for Store_id
     */
    protected java.lang.String localStore_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_idTracker = false;

    /**
     * field for Created_at
     */
    protected java.lang.String localCreated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCreated_atTracker = false;

    /**
     * field for Updated_at
     */
    protected java.lang.String localUpdated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUpdated_atTracker = false;

    /**
     * field for Is_active
     */
    protected java.lang.String localIs_active;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_activeTracker = false;

    /**
     * field for Global_currency_code
     */
    protected java.lang.String localGlobal_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGlobal_currency_codeTracker = false;

    /**
     * field for Base_currency_code
     */
    protected java.lang.String localBase_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_currency_codeTracker = false;

    /**
     * field for Store_currency_code
     */
    protected java.lang.String localStore_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_currency_codeTracker = false;

    /**
     * field for Order_currency_code
     */
    protected java.lang.String localOrder_currency_code;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_currency_codeTracker = false;

    /**
     * field for Store_to_base_rate
     */
    protected java.lang.String localStore_to_base_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_to_base_rateTracker = false;

    /**
     * field for Store_to_order_rate
     */
    protected java.lang.String localStore_to_order_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_to_order_rateTracker = false;

    /**
     * field for Base_to_global_rate
     */
    protected java.lang.String localBase_to_global_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_to_global_rateTracker = false;

    /**
     * field for Base_to_order_rate
     */
    protected java.lang.String localBase_to_order_rate;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_to_order_rateTracker = false;

    /**
     * field for Subtotal
     */
    protected java.lang.String localSubtotal;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubtotalTracker = false;

    /**
     * field for Base_subtotal
     */
    protected java.lang.String localBase_subtotal;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_subtotalTracker = false;

    /**
     * field for Base_grand_total
     */
    protected java.lang.String localBase_grand_total;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_grand_totalTracker = false;

    /**
     * field for Discount_amount
     */
    protected java.lang.String localDiscount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_amountTracker = false;

    /**
     * field for Base_discount_amount
     */
    protected java.lang.String localBase_discount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_discount_amountTracker = false;

    /**
     * field for Shipping_amount
     */
    protected java.lang.String localShipping_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_amountTracker = false;

    /**
     * field for Base_shipping_amount
     */
    protected java.lang.String localBase_shipping_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_shipping_amountTracker = false;

    /**
     * field for Tax_amount
     */
    protected java.lang.String localTax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_amountTracker = false;

    /**
     * field for Base_tax_amount
     */
    protected java.lang.String localBase_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_amountTracker = false;

    /**
     * field for Billing_address_id
     */
    protected java.lang.String localBilling_address_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_address_idTracker = false;

    /**
     * field for Billing_firstname
     */
    protected java.lang.String localBilling_firstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_firstnameTracker = false;

    /**
     * field for Billing_lastname
     */
    protected java.lang.String localBilling_lastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBilling_lastnameTracker = false;

    /**
     * field for Order_id
     */
    protected java.lang.String localOrder_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_idTracker = false;

    /**
     * field for Order_increment_id
     */
    protected java.lang.String localOrder_increment_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_increment_idTracker = false;

    /**
     * field for Order_created_at
     */
    protected java.lang.String localOrder_created_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_created_atTracker = false;

    /**
     * field for State
     */
    protected java.lang.String localState;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStateTracker = false;

    /**
     * field for Grand_total
     */
    protected java.lang.String localGrand_total;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGrand_totalTracker = false;

    /**
     * field for Invoice_id
     */
    protected java.lang.String localInvoice_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localInvoice_idTracker = false;

    /**
     * field for Items
     */
    protected magento.SalesOrderInvoiceItemEntityArray localItems;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localItemsTracker = false;

    /**
     * field for Comments
     */
    protected magento.SalesOrderInvoiceCommentEntityArray localComments;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCommentsTracker = false;

    public boolean isIncrement_idSpecified() {
        return localIncrement_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getIncrement_id() {
        return localIncrement_id;
    }

    /**
     * Auto generated setter method
     * @param param Increment_id
     */
    public void setIncrement_id(java.lang.String param) {
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
    public java.lang.String getParent_id() {
        return localParent_id;
    }

    /**
     * Auto generated setter method
     * @param param Parent_id
     */
    public void setParent_id(java.lang.String param) {
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
    public java.lang.String getStore_id() {
        return localStore_id;
    }

    /**
     * Auto generated setter method
     * @param param Store_id
     */
    public void setStore_id(java.lang.String param) {
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
    public java.lang.String getCreated_at() {
        return localCreated_at;
    }

    /**
     * Auto generated setter method
     * @param param Created_at
     */
    public void setCreated_at(java.lang.String param) {
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
    public java.lang.String getUpdated_at() {
        return localUpdated_at;
    }

    /**
     * Auto generated setter method
     * @param param Updated_at
     */
    public void setUpdated_at(java.lang.String param) {
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
    public java.lang.String getIs_active() {
        return localIs_active;
    }

    /**
     * Auto generated setter method
     * @param param Is_active
     */
    public void setIs_active(java.lang.String param) {
        localIs_activeTracker = param != null;

        this.localIs_active = param;
    }

    public boolean isGlobal_currency_codeSpecified() {
        return localGlobal_currency_codeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getGlobal_currency_code() {
        return localGlobal_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Global_currency_code
     */
    public void setGlobal_currency_code(java.lang.String param) {
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
    public java.lang.String getBase_currency_code() {
        return localBase_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Base_currency_code
     */
    public void setBase_currency_code(java.lang.String param) {
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
    public java.lang.String getStore_currency_code() {
        return localStore_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Store_currency_code
     */
    public void setStore_currency_code(java.lang.String param) {
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
    public java.lang.String getOrder_currency_code() {
        return localOrder_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Order_currency_code
     */
    public void setOrder_currency_code(java.lang.String param) {
        localOrder_currency_codeTracker = param != null;

        this.localOrder_currency_code = param;
    }

    public boolean isStore_to_base_rateSpecified() {
        return localStore_to_base_rateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStore_to_base_rate() {
        return localStore_to_base_rate;
    }

    /**
     * Auto generated setter method
     * @param param Store_to_base_rate
     */
    public void setStore_to_base_rate(java.lang.String param) {
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
    public java.lang.String getStore_to_order_rate() {
        return localStore_to_order_rate;
    }

    /**
     * Auto generated setter method
     * @param param Store_to_order_rate
     */
    public void setStore_to_order_rate(java.lang.String param) {
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
    public java.lang.String getBase_to_global_rate() {
        return localBase_to_global_rate;
    }

    /**
     * Auto generated setter method
     * @param param Base_to_global_rate
     */
    public void setBase_to_global_rate(java.lang.String param) {
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
    public java.lang.String getBase_to_order_rate() {
        return localBase_to_order_rate;
    }

    /**
     * Auto generated setter method
     * @param param Base_to_order_rate
     */
    public void setBase_to_order_rate(java.lang.String param) {
        localBase_to_order_rateTracker = param != null;

        this.localBase_to_order_rate = param;
    }

    public boolean isSubtotalSpecified() {
        return localSubtotalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSubtotal() {
        return localSubtotal;
    }

    /**
     * Auto generated setter method
     * @param param Subtotal
     */
    public void setSubtotal(java.lang.String param) {
        localSubtotalTracker = param != null;

        this.localSubtotal = param;
    }

    public boolean isBase_subtotalSpecified() {
        return localBase_subtotalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBase_subtotal() {
        return localBase_subtotal;
    }

    /**
     * Auto generated setter method
     * @param param Base_subtotal
     */
    public void setBase_subtotal(java.lang.String param) {
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
    public java.lang.String getBase_grand_total() {
        return localBase_grand_total;
    }

    /**
     * Auto generated setter method
     * @param param Base_grand_total
     */
    public void setBase_grand_total(java.lang.String param) {
        localBase_grand_totalTracker = param != null;

        this.localBase_grand_total = param;
    }

    public boolean isDiscount_amountSpecified() {
        return localDiscount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDiscount_amount() {
        return localDiscount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Discount_amount
     */
    public void setDiscount_amount(java.lang.String param) {
        localDiscount_amountTracker = param != null;

        this.localDiscount_amount = param;
    }

    public boolean isBase_discount_amountSpecified() {
        return localBase_discount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBase_discount_amount() {
        return localBase_discount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_discount_amount
     */
    public void setBase_discount_amount(java.lang.String param) {
        localBase_discount_amountTracker = param != null;

        this.localBase_discount_amount = param;
    }

    public boolean isShipping_amountSpecified() {
        return localShipping_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getShipping_amount() {
        return localShipping_amount;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_amount
     */
    public void setShipping_amount(java.lang.String param) {
        localShipping_amountTracker = param != null;

        this.localShipping_amount = param;
    }

    public boolean isBase_shipping_amountSpecified() {
        return localBase_shipping_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBase_shipping_amount() {
        return localBase_shipping_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_shipping_amount
     */
    public void setBase_shipping_amount(java.lang.String param) {
        localBase_shipping_amountTracker = param != null;

        this.localBase_shipping_amount = param;
    }

    public boolean isTax_amountSpecified() {
        return localTax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getTax_amount() {
        return localTax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Tax_amount
     */
    public void setTax_amount(java.lang.String param) {
        localTax_amountTracker = param != null;

        this.localTax_amount = param;
    }

    public boolean isBase_tax_amountSpecified() {
        return localBase_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBase_tax_amount() {
        return localBase_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_amount
     */
    public void setBase_tax_amount(java.lang.String param) {
        localBase_tax_amountTracker = param != null;

        this.localBase_tax_amount = param;
    }

    public boolean isBilling_address_idSpecified() {
        return localBilling_address_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBilling_address_id() {
        return localBilling_address_id;
    }

    /**
     * Auto generated setter method
     * @param param Billing_address_id
     */
    public void setBilling_address_id(java.lang.String param) {
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
    public java.lang.String getBilling_firstname() {
        return localBilling_firstname;
    }

    /**
     * Auto generated setter method
     * @param param Billing_firstname
     */
    public void setBilling_firstname(java.lang.String param) {
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
    public java.lang.String getBilling_lastname() {
        return localBilling_lastname;
    }

    /**
     * Auto generated setter method
     * @param param Billing_lastname
     */
    public void setBilling_lastname(java.lang.String param) {
        localBilling_lastnameTracker = param != null;

        this.localBilling_lastname = param;
    }

    public boolean isOrder_idSpecified() {
        return localOrder_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOrder_id() {
        return localOrder_id;
    }

    /**
     * Auto generated setter method
     * @param param Order_id
     */
    public void setOrder_id(java.lang.String param) {
        localOrder_idTracker = param != null;

        this.localOrder_id = param;
    }

    public boolean isOrder_increment_idSpecified() {
        return localOrder_increment_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOrder_increment_id() {
        return localOrder_increment_id;
    }

    /**
     * Auto generated setter method
     * @param param Order_increment_id
     */
    public void setOrder_increment_id(java.lang.String param) {
        localOrder_increment_idTracker = param != null;

        this.localOrder_increment_id = param;
    }

    public boolean isOrder_created_atSpecified() {
        return localOrder_created_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOrder_created_at() {
        return localOrder_created_at;
    }

    /**
     * Auto generated setter method
     * @param param Order_created_at
     */
    public void setOrder_created_at(java.lang.String param) {
        localOrder_created_atTracker = param != null;

        this.localOrder_created_at = param;
    }

    public boolean isStateSpecified() {
        return localStateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getState() {
        return localState;
    }

    /**
     * Auto generated setter method
     * @param param State
     */
    public void setState(java.lang.String param) {
        localStateTracker = param != null;

        this.localState = param;
    }

    public boolean isGrand_totalSpecified() {
        return localGrand_totalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getGrand_total() {
        return localGrand_total;
    }

    /**
     * Auto generated setter method
     * @param param Grand_total
     */
    public void setGrand_total(java.lang.String param) {
        localGrand_totalTracker = param != null;

        this.localGrand_total = param;
    }

    public boolean isInvoice_idSpecified() {
        return localInvoice_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getInvoice_id() {
        return localInvoice_id;
    }

    /**
     * Auto generated setter method
     * @param param Invoice_id
     */
    public void setInvoice_id(java.lang.String param) {
        localInvoice_idTracker = param != null;

        this.localInvoice_id = param;
    }

    public boolean isItemsSpecified() {
        return localItemsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderInvoiceItemEntityArray
     */
    public magento.SalesOrderInvoiceItemEntityArray getItems() {
        return localItems;
    }

    /**
     * Auto generated setter method
     * @param param Items
     */
    public void setItems(magento.SalesOrderInvoiceItemEntityArray param) {
        localItemsTracker = param != null;

        this.localItems = param;
    }

    public boolean isCommentsSpecified() {
        return localCommentsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderInvoiceCommentEntityArray
     */
    public magento.SalesOrderInvoiceCommentEntityArray getComments() {
        return localComments;
    }

    /**
     * Auto generated setter method
     * @param param Comments
     */
    public void setComments(magento.SalesOrderInvoiceCommentEntityArray param) {
        localCommentsTracker = param != null;

        this.localComments = param;
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
        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "urn:Magento");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":salesOrderInvoiceEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "salesOrderInvoiceEntity", xmlWriter);
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

        if (localOrder_increment_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_increment_id", xmlWriter);

            if (localOrder_increment_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_increment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_increment_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localOrder_created_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_created_at", xmlWriter);

            if (localOrder_created_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_created_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_created_at);
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

        if (localInvoice_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "invoice_id", xmlWriter);

            if (localInvoice_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "invoice_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localInvoice_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localItemsTracker) {
            if (localItems == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "items cannot be null!!");
            }

            localItems.serialize(new javax.xml.namespace.QName("", "items"),
                xmlWriter);
        }

        if (localCommentsTracker) {
            if (localComments == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "comments cannot be null!!");
            }

            localComments.serialize(new javax.xml.namespace.QName("", "comments"),
                xmlWriter);
        }

        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("urn:Magento")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix,
        java.lang.String namespace, java.lang.String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

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
    private void writeAttribute(java.lang.String prefix,
        java.lang.String namespace, java.lang.String attName,
        java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
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
    private void writeAttribute(java.lang.String namespace,
        java.lang.String attName, java.lang.String attValue,
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
    private void writeQNameAttribute(java.lang.String namespace,
        java.lang.String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        java.lang.String attributeValue;

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
        java.lang.String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);

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
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

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
    private java.lang.String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);

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

        if (localOrder_increment_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "order_increment_id"));

            if (localOrder_increment_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_increment_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_increment_id cannot be null!!");
            }
        }

        if (localOrder_created_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "order_created_at"));

            if (localOrder_created_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_created_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_created_at cannot be null!!");
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

        if (localInvoice_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "invoice_id"));

            if (localInvoice_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localInvoice_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "invoice_id cannot be null!!");
            }
        }

        if (localItemsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "items"));

            if (localItems == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "items cannot be null!!");
            }

            elementList.add(localItems);
        }

        if (localCommentsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "comments"));

            if (localComments == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "comments cannot be null!!");
            }

            elementList.add(localComments);
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
        public static SalesOrderInvoiceEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            SalesOrderInvoiceEntity object = new SalesOrderInvoiceEntity();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"salesOrderInvoiceEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (SalesOrderInvoiceEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

                    object.setIs_active(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

                    object.setOrder_currency_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

                    object.setBase_to_order_rate(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setSubtotal(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

                    object.setBase_grand_total(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setDiscount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setBase_discount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setShipping_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setBase_shipping_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setTax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setBase_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

                    object.setBilling_lastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setOrder_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_increment_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_increment_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOrder_increment_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_created_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_created_at" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOrder_created_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setState(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setGrand_total(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "invoice_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "invoice_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setInvoice_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "items").equals(
                            reader.getName())) {
                    object.setItems(magento.SalesOrderInvoiceItemEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "comments").equals(
                            reader.getName())) {
                    object.setComments(magento.SalesOrderInvoiceCommentEntityArray.Factory.parse(
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
                throw new java.lang.Exception(e);
            }

            return object;
        }
    } //end of factory class
}
