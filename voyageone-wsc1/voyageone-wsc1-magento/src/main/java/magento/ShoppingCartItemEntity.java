/**
 * ShoppingCartItemEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ShoppingCartItemEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ShoppingCartItemEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = shoppingCartItemEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Item_id
     */
    protected java.lang.String localItem_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localItem_idTracker = false;

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
     * field for Product_id
     */
    protected java.lang.String localProduct_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localProduct_idTracker = false;

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
     * field for Parent_item_id
     */
    protected java.lang.String localParent_item_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localParent_item_idTracker = false;

    /**
     * field for Is_virtual
     */
    protected int localIs_virtual;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_virtualTracker = false;

    /**
     * field for Sku
     */
    protected java.lang.String localSku;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSkuTracker = false;

    /**
     * field for Name
     */
    protected java.lang.String localName;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localNameTracker = false;

    /**
     * field for Description
     */
    protected java.lang.String localDescription;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDescriptionTracker = false;

    /**
     * field for Applied_rule_ids
     */
    protected java.lang.String localApplied_rule_ids;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localApplied_rule_idsTracker = false;

    /**
     * field for Additional_data
     */
    protected java.lang.String localAdditional_data;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdditional_dataTracker = false;

    /**
     * field for Free_shipping
     */
    protected java.lang.String localFree_shipping;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFree_shippingTracker = false;

    /**
     * field for Is_qty_decimal
     */
    protected java.lang.String localIs_qty_decimal;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_qty_decimalTracker = false;

    /**
     * field for No_discount
     */
    protected java.lang.String localNo_discount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localNo_discountTracker = false;

    /**
     * field for Weight
     */
    protected double localWeight;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeightTracker = false;

    /**
     * field for Qty
     */
    protected double localQty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localQtyTracker = false;

    /**
     * field for Price
     */
    protected double localPrice;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPriceTracker = false;

    /**
     * field for Base_price
     */
    protected double localBase_price;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_priceTracker = false;

    /**
     * field for Custom_price
     */
    protected double localCustom_price;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_priceTracker = false;

    /**
     * field for Discount_percent
     */
    protected double localDiscount_percent;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_percentTracker = false;

    /**
     * field for Discount_amount
     */
    protected double localDiscount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDiscount_amountTracker = false;

    /**
     * field for Base_discount_amount
     */
    protected double localBase_discount_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_discount_amountTracker = false;

    /**
     * field for Tax_percent
     */
    protected double localTax_percent;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_percentTracker = false;

    /**
     * field for Tax_amount
     */
    protected double localTax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_amountTracker = false;

    /**
     * field for Base_tax_amount
     */
    protected double localBase_tax_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_amountTracker = false;

    /**
     * field for Row_total
     */
    protected double localRow_total;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRow_totalTracker = false;

    /**
     * field for Base_row_total
     */
    protected double localBase_row_total;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_row_totalTracker = false;

    /**
     * field for Row_total_with_discount
     */
    protected double localRow_total_with_discount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRow_total_with_discountTracker = false;

    /**
     * field for Row_weight
     */
    protected double localRow_weight;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRow_weightTracker = false;

    /**
     * field for Product_type
     */
    protected java.lang.String localProduct_type;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localProduct_typeTracker = false;

    /**
     * field for Base_tax_before_discount
     */
    protected double localBase_tax_before_discount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_tax_before_discountTracker = false;

    /**
     * field for Tax_before_discount
     */
    protected double localTax_before_discount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_before_discountTracker = false;

    /**
     * field for Original_custom_price
     */
    protected double localOriginal_custom_price;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOriginal_custom_priceTracker = false;

    /**
     * field for Base_cost
     */
    protected double localBase_cost;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_costTracker = false;

    /**
     * field for Price_incl_tax
     */
    protected double localPrice_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPrice_incl_taxTracker = false;

    /**
     * field for Base_price_incl_tax
     */
    protected double localBase_price_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_price_incl_taxTracker = false;

    /**
     * field for Row_total_incl_tax
     */
    protected double localRow_total_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRow_total_incl_taxTracker = false;

    /**
     * field for Base_row_total_incl_tax
     */
    protected double localBase_row_total_incl_tax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_row_total_incl_taxTracker = false;

    /**
     * field for Gift_message_id
     */
    protected java.lang.String localGift_message_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_message_idTracker = false;

    /**
     * field for Gift_message
     */
    protected java.lang.String localGift_message;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_messageTracker = false;

    /**
     * field for Gift_message_available
     */
    protected java.lang.String localGift_message_available;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGift_message_availableTracker = false;

    /**
     * field for Weee_tax_applied
     */
    protected double localWeee_tax_applied;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeee_tax_appliedTracker = false;

    /**
     * field for Weee_tax_applied_amount
     */
    protected double localWeee_tax_applied_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeee_tax_applied_amountTracker = false;

    /**
     * field for Weee_tax_applied_row_amount
     */
    protected double localWeee_tax_applied_row_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeee_tax_applied_row_amountTracker = false;

    /**
     * field for Base_weee_tax_applied_amount
     */
    protected double localBase_weee_tax_applied_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_weee_tax_applied_amountTracker = false;

    /**
     * field for Base_weee_tax_applied_row_amount
     */
    protected double localBase_weee_tax_applied_row_amount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_weee_tax_applied_row_amountTracker = false;

    /**
     * field for Weee_tax_disposition
     */
    protected double localWeee_tax_disposition;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeee_tax_dispositionTracker = false;

    /**
     * field for Weee_tax_row_disposition
     */
    protected double localWeee_tax_row_disposition;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeee_tax_row_dispositionTracker = false;

    /**
     * field for Base_weee_tax_disposition
     */
    protected double localBase_weee_tax_disposition;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_weee_tax_dispositionTracker = false;

    /**
     * field for Base_weee_tax_row_disposition
     */
    protected double localBase_weee_tax_row_disposition;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBase_weee_tax_row_dispositionTracker = false;

    /**
     * field for Tax_class_id
     */
    protected java.lang.String localTax_class_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_class_idTracker = false;

    public boolean isItem_idSpecified() {
        return localItem_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getItem_id() {
        return localItem_id;
    }

    /**
     * Auto generated setter method
     * @param param Item_id
     */
    public void setItem_id(java.lang.String param) {
        localItem_idTracker = param != null;

        this.localItem_id = param;
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

    public boolean isProduct_idSpecified() {
        return localProduct_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getProduct_id() {
        return localProduct_id;
    }

    /**
     * Auto generated setter method
     * @param param Product_id
     */
    public void setProduct_id(java.lang.String param) {
        localProduct_idTracker = param != null;

        this.localProduct_id = param;
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

    public boolean isParent_item_idSpecified() {
        return localParent_item_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getParent_item_id() {
        return localParent_item_id;
    }

    /**
     * Auto generated setter method
     * @param param Parent_item_id
     */
    public void setParent_item_id(java.lang.String param) {
        localParent_item_idTracker = param != null;

        this.localParent_item_id = param;
    }

    public boolean isIs_virtualSpecified() {
        return localIs_virtualTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_virtual() {
        return localIs_virtual;
    }

    /**
     * Auto generated setter method
     * @param param Is_virtual
     */
    public void setIs_virtual(int param) {
        // setting primitive attribute tracker to true
        localIs_virtualTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_virtual = param;
    }

    public boolean isSkuSpecified() {
        return localSkuTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSku() {
        return localSku;
    }

    /**
     * Auto generated setter method
     * @param param Sku
     */
    public void setSku(java.lang.String param) {
        localSkuTracker = param != null;

        this.localSku = param;
    }

    public boolean isNameSpecified() {
        return localNameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return localName;
    }

    /**
     * Auto generated setter method
     * @param param Name
     */
    public void setName(java.lang.String param) {
        localNameTracker = param != null;

        this.localName = param;
    }

    public boolean isDescriptionSpecified() {
        return localDescriptionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDescription() {
        return localDescription;
    }

    /**
     * Auto generated setter method
     * @param param Description
     */
    public void setDescription(java.lang.String param) {
        localDescriptionTracker = param != null;

        this.localDescription = param;
    }

    public boolean isApplied_rule_idsSpecified() {
        return localApplied_rule_idsTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getApplied_rule_ids() {
        return localApplied_rule_ids;
    }

    /**
     * Auto generated setter method
     * @param param Applied_rule_ids
     */
    public void setApplied_rule_ids(java.lang.String param) {
        localApplied_rule_idsTracker = param != null;

        this.localApplied_rule_ids = param;
    }

    public boolean isAdditional_dataSpecified() {
        return localAdditional_dataTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAdditional_data() {
        return localAdditional_data;
    }

    /**
     * Auto generated setter method
     * @param param Additional_data
     */
    public void setAdditional_data(java.lang.String param) {
        localAdditional_dataTracker = param != null;

        this.localAdditional_data = param;
    }

    public boolean isFree_shippingSpecified() {
        return localFree_shippingTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getFree_shipping() {
        return localFree_shipping;
    }

    /**
     * Auto generated setter method
     * @param param Free_shipping
     */
    public void setFree_shipping(java.lang.String param) {
        localFree_shippingTracker = param != null;

        this.localFree_shipping = param;
    }

    public boolean isIs_qty_decimalSpecified() {
        return localIs_qty_decimalTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getIs_qty_decimal() {
        return localIs_qty_decimal;
    }

    /**
     * Auto generated setter method
     * @param param Is_qty_decimal
     */
    public void setIs_qty_decimal(java.lang.String param) {
        localIs_qty_decimalTracker = param != null;

        this.localIs_qty_decimal = param;
    }

    public boolean isNo_discountSpecified() {
        return localNo_discountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getNo_discount() {
        return localNo_discount;
    }

    /**
     * Auto generated setter method
     * @param param No_discount
     */
    public void setNo_discount(java.lang.String param) {
        localNo_discountTracker = param != null;

        this.localNo_discount = param;
    }

    public boolean isWeightSpecified() {
        return localWeightTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getWeight() {
        return localWeight;
    }

    /**
     * Auto generated setter method
     * @param param Weight
     */
    public void setWeight(double param) {
        // setting primitive attribute tracker to true
        localWeightTracker = !java.lang.Double.isNaN(param);

        this.localWeight = param;
    }

    public boolean isQtySpecified() {
        return localQtyTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getQty() {
        return localQty;
    }

    /**
     * Auto generated setter method
     * @param param Qty
     */
    public void setQty(double param) {
        // setting primitive attribute tracker to true
        localQtyTracker = !java.lang.Double.isNaN(param);

        this.localQty = param;
    }

    public boolean isPriceSpecified() {
        return localPriceTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getPrice() {
        return localPrice;
    }

    /**
     * Auto generated setter method
     * @param param Price
     */
    public void setPrice(double param) {
        // setting primitive attribute tracker to true
        localPriceTracker = !java.lang.Double.isNaN(param);

        this.localPrice = param;
    }

    public boolean isBase_priceSpecified() {
        return localBase_priceTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_price() {
        return localBase_price;
    }

    /**
     * Auto generated setter method
     * @param param Base_price
     */
    public void setBase_price(double param) {
        // setting primitive attribute tracker to true
        localBase_priceTracker = !java.lang.Double.isNaN(param);

        this.localBase_price = param;
    }

    public boolean isCustom_priceSpecified() {
        return localCustom_priceTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getCustom_price() {
        return localCustom_price;
    }

    /**
     * Auto generated setter method
     * @param param Custom_price
     */
    public void setCustom_price(double param) {
        // setting primitive attribute tracker to true
        localCustom_priceTracker = !java.lang.Double.isNaN(param);

        this.localCustom_price = param;
    }

    public boolean isDiscount_percentSpecified() {
        return localDiscount_percentTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getDiscount_percent() {
        return localDiscount_percent;
    }

    /**
     * Auto generated setter method
     * @param param Discount_percent
     */
    public void setDiscount_percent(double param) {
        // setting primitive attribute tracker to true
        localDiscount_percentTracker = !java.lang.Double.isNaN(param);

        this.localDiscount_percent = param;
    }

    public boolean isDiscount_amountSpecified() {
        return localDiscount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getDiscount_amount() {
        return localDiscount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Discount_amount
     */
    public void setDiscount_amount(double param) {
        // setting primitive attribute tracker to true
        localDiscount_amountTracker = !java.lang.Double.isNaN(param);

        this.localDiscount_amount = param;
    }

    public boolean isBase_discount_amountSpecified() {
        return localBase_discount_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_discount_amount() {
        return localBase_discount_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_discount_amount
     */
    public void setBase_discount_amount(double param) {
        // setting primitive attribute tracker to true
        localBase_discount_amountTracker = !java.lang.Double.isNaN(param);

        this.localBase_discount_amount = param;
    }

    public boolean isTax_percentSpecified() {
        return localTax_percentTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getTax_percent() {
        return localTax_percent;
    }

    /**
     * Auto generated setter method
     * @param param Tax_percent
     */
    public void setTax_percent(double param) {
        // setting primitive attribute tracker to true
        localTax_percentTracker = !java.lang.Double.isNaN(param);

        this.localTax_percent = param;
    }

    public boolean isTax_amountSpecified() {
        return localTax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getTax_amount() {
        return localTax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Tax_amount
     */
    public void setTax_amount(double param) {
        // setting primitive attribute tracker to true
        localTax_amountTracker = !java.lang.Double.isNaN(param);

        this.localTax_amount = param;
    }

    public boolean isBase_tax_amountSpecified() {
        return localBase_tax_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_tax_amount() {
        return localBase_tax_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_amount
     */
    public void setBase_tax_amount(double param) {
        // setting primitive attribute tracker to true
        localBase_tax_amountTracker = !java.lang.Double.isNaN(param);

        this.localBase_tax_amount = param;
    }

    public boolean isRow_totalSpecified() {
        return localRow_totalTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getRow_total() {
        return localRow_total;
    }

    /**
     * Auto generated setter method
     * @param param Row_total
     */
    public void setRow_total(double param) {
        // setting primitive attribute tracker to true
        localRow_totalTracker = !java.lang.Double.isNaN(param);

        this.localRow_total = param;
    }

    public boolean isBase_row_totalSpecified() {
        return localBase_row_totalTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_row_total() {
        return localBase_row_total;
    }

    /**
     * Auto generated setter method
     * @param param Base_row_total
     */
    public void setBase_row_total(double param) {
        // setting primitive attribute tracker to true
        localBase_row_totalTracker = !java.lang.Double.isNaN(param);

        this.localBase_row_total = param;
    }

    public boolean isRow_total_with_discountSpecified() {
        return localRow_total_with_discountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getRow_total_with_discount() {
        return localRow_total_with_discount;
    }

    /**
     * Auto generated setter method
     * @param param Row_total_with_discount
     */
    public void setRow_total_with_discount(double param) {
        // setting primitive attribute tracker to true
        localRow_total_with_discountTracker = !java.lang.Double.isNaN(param);

        this.localRow_total_with_discount = param;
    }

    public boolean isRow_weightSpecified() {
        return localRow_weightTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getRow_weight() {
        return localRow_weight;
    }

    /**
     * Auto generated setter method
     * @param param Row_weight
     */
    public void setRow_weight(double param) {
        // setting primitive attribute tracker to true
        localRow_weightTracker = !java.lang.Double.isNaN(param);

        this.localRow_weight = param;
    }

    public boolean isProduct_typeSpecified() {
        return localProduct_typeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getProduct_type() {
        return localProduct_type;
    }

    /**
     * Auto generated setter method
     * @param param Product_type
     */
    public void setProduct_type(java.lang.String param) {
        localProduct_typeTracker = param != null;

        this.localProduct_type = param;
    }

    public boolean isBase_tax_before_discountSpecified() {
        return localBase_tax_before_discountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_tax_before_discount() {
        return localBase_tax_before_discount;
    }

    /**
     * Auto generated setter method
     * @param param Base_tax_before_discount
     */
    public void setBase_tax_before_discount(double param) {
        // setting primitive attribute tracker to true
        localBase_tax_before_discountTracker = !java.lang.Double.isNaN(param);

        this.localBase_tax_before_discount = param;
    }

    public boolean isTax_before_discountSpecified() {
        return localTax_before_discountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getTax_before_discount() {
        return localTax_before_discount;
    }

    /**
     * Auto generated setter method
     * @param param Tax_before_discount
     */
    public void setTax_before_discount(double param) {
        // setting primitive attribute tracker to true
        localTax_before_discountTracker = !java.lang.Double.isNaN(param);

        this.localTax_before_discount = param;
    }

    public boolean isOriginal_custom_priceSpecified() {
        return localOriginal_custom_priceTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getOriginal_custom_price() {
        return localOriginal_custom_price;
    }

    /**
     * Auto generated setter method
     * @param param Original_custom_price
     */
    public void setOriginal_custom_price(double param) {
        // setting primitive attribute tracker to true
        localOriginal_custom_priceTracker = !java.lang.Double.isNaN(param);

        this.localOriginal_custom_price = param;
    }

    public boolean isBase_costSpecified() {
        return localBase_costTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_cost() {
        return localBase_cost;
    }

    /**
     * Auto generated setter method
     * @param param Base_cost
     */
    public void setBase_cost(double param) {
        // setting primitive attribute tracker to true
        localBase_costTracker = !java.lang.Double.isNaN(param);

        this.localBase_cost = param;
    }

    public boolean isPrice_incl_taxSpecified() {
        return localPrice_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getPrice_incl_tax() {
        return localPrice_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Price_incl_tax
     */
    public void setPrice_incl_tax(double param) {
        // setting primitive attribute tracker to true
        localPrice_incl_taxTracker = !java.lang.Double.isNaN(param);

        this.localPrice_incl_tax = param;
    }

    public boolean isBase_price_incl_taxSpecified() {
        return localBase_price_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_price_incl_tax() {
        return localBase_price_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Base_price_incl_tax
     */
    public void setBase_price_incl_tax(double param) {
        // setting primitive attribute tracker to true
        localBase_price_incl_taxTracker = !java.lang.Double.isNaN(param);

        this.localBase_price_incl_tax = param;
    }

    public boolean isRow_total_incl_taxSpecified() {
        return localRow_total_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getRow_total_incl_tax() {
        return localRow_total_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Row_total_incl_tax
     */
    public void setRow_total_incl_tax(double param) {
        // setting primitive attribute tracker to true
        localRow_total_incl_taxTracker = !java.lang.Double.isNaN(param);

        this.localRow_total_incl_tax = param;
    }

    public boolean isBase_row_total_incl_taxSpecified() {
        return localBase_row_total_incl_taxTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_row_total_incl_tax() {
        return localBase_row_total_incl_tax;
    }

    /**
     * Auto generated setter method
     * @param param Base_row_total_incl_tax
     */
    public void setBase_row_total_incl_tax(double param) {
        // setting primitive attribute tracker to true
        localBase_row_total_incl_taxTracker = !java.lang.Double.isNaN(param);

        this.localBase_row_total_incl_tax = param;
    }

    public boolean isGift_message_idSpecified() {
        return localGift_message_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getGift_message_id() {
        return localGift_message_id;
    }

    /**
     * Auto generated setter method
     * @param param Gift_message_id
     */
    public void setGift_message_id(java.lang.String param) {
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
    public java.lang.String getGift_message() {
        return localGift_message;
    }

    /**
     * Auto generated setter method
     * @param param Gift_message
     */
    public void setGift_message(java.lang.String param) {
        localGift_messageTracker = param != null;

        this.localGift_message = param;
    }

    public boolean isGift_message_availableSpecified() {
        return localGift_message_availableTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getGift_message_available() {
        return localGift_message_available;
    }

    /**
     * Auto generated setter method
     * @param param Gift_message_available
     */
    public void setGift_message_available(java.lang.String param) {
        localGift_message_availableTracker = param != null;

        this.localGift_message_available = param;
    }

    public boolean isWeee_tax_appliedSpecified() {
        return localWeee_tax_appliedTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getWeee_tax_applied() {
        return localWeee_tax_applied;
    }

    /**
     * Auto generated setter method
     * @param param Weee_tax_applied
     */
    public void setWeee_tax_applied(double param) {
        // setting primitive attribute tracker to true
        localWeee_tax_appliedTracker = !java.lang.Double.isNaN(param);

        this.localWeee_tax_applied = param;
    }

    public boolean isWeee_tax_applied_amountSpecified() {
        return localWeee_tax_applied_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getWeee_tax_applied_amount() {
        return localWeee_tax_applied_amount;
    }

    /**
     * Auto generated setter method
     * @param param Weee_tax_applied_amount
     */
    public void setWeee_tax_applied_amount(double param) {
        // setting primitive attribute tracker to true
        localWeee_tax_applied_amountTracker = !java.lang.Double.isNaN(param);

        this.localWeee_tax_applied_amount = param;
    }

    public boolean isWeee_tax_applied_row_amountSpecified() {
        return localWeee_tax_applied_row_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getWeee_tax_applied_row_amount() {
        return localWeee_tax_applied_row_amount;
    }

    /**
     * Auto generated setter method
     * @param param Weee_tax_applied_row_amount
     */
    public void setWeee_tax_applied_row_amount(double param) {
        // setting primitive attribute tracker to true
        localWeee_tax_applied_row_amountTracker = !java.lang.Double.isNaN(param);

        this.localWeee_tax_applied_row_amount = param;
    }

    public boolean isBase_weee_tax_applied_amountSpecified() {
        return localBase_weee_tax_applied_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_weee_tax_applied_amount() {
        return localBase_weee_tax_applied_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_weee_tax_applied_amount
     */
    public void setBase_weee_tax_applied_amount(double param) {
        // setting primitive attribute tracker to true
        localBase_weee_tax_applied_amountTracker = !java.lang.Double.isNaN(param);

        this.localBase_weee_tax_applied_amount = param;
    }

    public boolean isBase_weee_tax_applied_row_amountSpecified() {
        return localBase_weee_tax_applied_row_amountTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_weee_tax_applied_row_amount() {
        return localBase_weee_tax_applied_row_amount;
    }

    /**
     * Auto generated setter method
     * @param param Base_weee_tax_applied_row_amount
     */
    public void setBase_weee_tax_applied_row_amount(double param) {
        // setting primitive attribute tracker to true
        localBase_weee_tax_applied_row_amountTracker = !java.lang.Double.isNaN(param);

        this.localBase_weee_tax_applied_row_amount = param;
    }

    public boolean isWeee_tax_dispositionSpecified() {
        return localWeee_tax_dispositionTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getWeee_tax_disposition() {
        return localWeee_tax_disposition;
    }

    /**
     * Auto generated setter method
     * @param param Weee_tax_disposition
     */
    public void setWeee_tax_disposition(double param) {
        // setting primitive attribute tracker to true
        localWeee_tax_dispositionTracker = !java.lang.Double.isNaN(param);

        this.localWeee_tax_disposition = param;
    }

    public boolean isWeee_tax_row_dispositionSpecified() {
        return localWeee_tax_row_dispositionTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getWeee_tax_row_disposition() {
        return localWeee_tax_row_disposition;
    }

    /**
     * Auto generated setter method
     * @param param Weee_tax_row_disposition
     */
    public void setWeee_tax_row_disposition(double param) {
        // setting primitive attribute tracker to true
        localWeee_tax_row_dispositionTracker = !java.lang.Double.isNaN(param);

        this.localWeee_tax_row_disposition = param;
    }

    public boolean isBase_weee_tax_dispositionSpecified() {
        return localBase_weee_tax_dispositionTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_weee_tax_disposition() {
        return localBase_weee_tax_disposition;
    }

    /**
     * Auto generated setter method
     * @param param Base_weee_tax_disposition
     */
    public void setBase_weee_tax_disposition(double param) {
        // setting primitive attribute tracker to true
        localBase_weee_tax_dispositionTracker = !java.lang.Double.isNaN(param);

        this.localBase_weee_tax_disposition = param;
    }

    public boolean isBase_weee_tax_row_dispositionSpecified() {
        return localBase_weee_tax_row_dispositionTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBase_weee_tax_row_disposition() {
        return localBase_weee_tax_row_disposition;
    }

    /**
     * Auto generated setter method
     * @param param Base_weee_tax_row_disposition
     */
    public void setBase_weee_tax_row_disposition(double param) {
        // setting primitive attribute tracker to true
        localBase_weee_tax_row_dispositionTracker = !java.lang.Double.isNaN(param);

        this.localBase_weee_tax_row_disposition = param;
    }

    public boolean isTax_class_idSpecified() {
        return localTax_class_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getTax_class_id() {
        return localTax_class_id;
    }

    /**
     * Auto generated setter method
     * @param param Tax_class_id
     */
    public void setTax_class_id(java.lang.String param) {
        localTax_class_idTracker = param != null;

        this.localTax_class_id = param;
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
                    namespacePrefix + ":shoppingCartItemEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "shoppingCartItemEntity", xmlWriter);
            }
        }

        if (localItem_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "item_id", xmlWriter);

            if (localItem_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "item_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localItem_id);
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

        if (localProduct_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "product_id", xmlWriter);

            if (localProduct_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "product_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localProduct_id);
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

        if (localParent_item_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "parent_item_id", xmlWriter);

            if (localParent_item_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "parent_item_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localParent_item_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_virtualTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_virtual", xmlWriter);

            if (localIs_virtual == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_virtual cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_virtual));
            }

            xmlWriter.writeEndElement();
        }

        if (localSkuTracker) {
            namespace = "";
            writeStartElement(null, namespace, "sku", xmlWriter);

            if (localSku == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "sku cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSku);
            }

            xmlWriter.writeEndElement();
        }

        if (localNameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "name", xmlWriter);

            if (localName == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "name cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localName);
            }

            xmlWriter.writeEndElement();
        }

        if (localDescriptionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "description", xmlWriter);

            if (localDescription == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "description cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDescription);
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

        if (localAdditional_dataTracker) {
            namespace = "";
            writeStartElement(null, namespace, "additional_data", xmlWriter);

            if (localAdditional_data == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_data cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localAdditional_data);
            }

            xmlWriter.writeEndElement();
        }

        if (localFree_shippingTracker) {
            namespace = "";
            writeStartElement(null, namespace, "free_shipping", xmlWriter);

            if (localFree_shipping == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "free_shipping cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localFree_shipping);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_qty_decimalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_qty_decimal", xmlWriter);

            if (localIs_qty_decimal == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "is_qty_decimal cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localIs_qty_decimal);
            }

            xmlWriter.writeEndElement();
        }

        if (localNo_discountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "no_discount", xmlWriter);

            if (localNo_discount == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "no_discount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localNo_discount);
            }

            xmlWriter.writeEndElement();
        }

        if (localWeightTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weight", xmlWriter);

            if (java.lang.Double.isNaN(localWeight)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "weight cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeight));
            }

            xmlWriter.writeEndElement();
        }

        if (localQtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "qty", xmlWriter);

            if (java.lang.Double.isNaN(localQty)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localQty));
            }

            xmlWriter.writeEndElement();
        }

        if (localPriceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "price", xmlWriter);

            if (java.lang.Double.isNaN(localPrice)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "price cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPrice));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_priceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_price", xmlWriter);

            if (java.lang.Double.isNaN(localBase_price)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_price cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_price));
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_priceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_price", xmlWriter);

            if (java.lang.Double.isNaN(localCustom_price)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_price cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_price));
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_percentTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_percent", xmlWriter);

            if (java.lang.Double.isNaN(localDiscount_percent)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_percent cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_percent));
            }

            xmlWriter.writeEndElement();
        }

        if (localDiscount_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "discount_amount", xmlWriter);

            if (java.lang.Double.isNaN(localDiscount_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "discount_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDiscount_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_discount_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_discount_amount", xmlWriter);

            if (java.lang.Double.isNaN(localBase_discount_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_discount_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_discount_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_percentTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_percent", xmlWriter);

            if (java.lang.Double.isNaN(localTax_percent)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_percent cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_percent));
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_amount", xmlWriter);

            if (java.lang.Double.isNaN(localTax_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_tax_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_tax_amount", xmlWriter);

            if (java.lang.Double.isNaN(localBase_tax_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_tax_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localRow_totalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "row_total", xmlWriter);

            if (java.lang.Double.isNaN(localRow_total)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "row_total cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRow_total));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_row_totalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_row_total", xmlWriter);

            if (java.lang.Double.isNaN(localBase_row_total)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_row_total cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_row_total));
            }

            xmlWriter.writeEndElement();
        }

        if (localRow_total_with_discountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "row_total_with_discount",
                xmlWriter);

            if (java.lang.Double.isNaN(localRow_total_with_discount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "row_total_with_discount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRow_total_with_discount));
            }

            xmlWriter.writeEndElement();
        }

        if (localRow_weightTracker) {
            namespace = "";
            writeStartElement(null, namespace, "row_weight", xmlWriter);

            if (java.lang.Double.isNaN(localRow_weight)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "row_weight cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRow_weight));
            }

            xmlWriter.writeEndElement();
        }

        if (localProduct_typeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "product_type", xmlWriter);

            if (localProduct_type == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "product_type cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localProduct_type);
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_tax_before_discountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_tax_before_discount",
                xmlWriter);

            if (java.lang.Double.isNaN(localBase_tax_before_discount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_tax_before_discount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_tax_before_discount));
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_before_discountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_before_discount", xmlWriter);

            if (java.lang.Double.isNaN(localTax_before_discount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_before_discount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_before_discount));
            }

            xmlWriter.writeEndElement();
        }

        if (localOriginal_custom_priceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "original_custom_price",
                xmlWriter);

            if (java.lang.Double.isNaN(localOriginal_custom_price)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "original_custom_price cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOriginal_custom_price));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_costTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_cost", xmlWriter);

            if (java.lang.Double.isNaN(localBase_cost)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_cost cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_cost));
            }

            xmlWriter.writeEndElement();
        }

        if (localPrice_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "price_incl_tax", xmlWriter);

            if (java.lang.Double.isNaN(localPrice_incl_tax)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "price_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPrice_incl_tax));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_price_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_price_incl_tax", xmlWriter);

            if (java.lang.Double.isNaN(localBase_price_incl_tax)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_price_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_price_incl_tax));
            }

            xmlWriter.writeEndElement();
        }

        if (localRow_total_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "row_total_incl_tax", xmlWriter);

            if (java.lang.Double.isNaN(localRow_total_incl_tax)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "row_total_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRow_total_incl_tax));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_row_total_incl_taxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_row_total_incl_tax",
                xmlWriter);

            if (java.lang.Double.isNaN(localBase_row_total_incl_tax)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_row_total_incl_tax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_row_total_incl_tax));
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

        if (localGift_message_availableTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gift_message_available",
                xmlWriter);

            if (localGift_message_available == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_message_available cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localGift_message_available);
            }

            xmlWriter.writeEndElement();
        }

        if (localWeee_tax_appliedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weee_tax_applied", xmlWriter);

            if (java.lang.Double.isNaN(localWeee_tax_applied)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "weee_tax_applied cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeee_tax_applied));
            }

            xmlWriter.writeEndElement();
        }

        if (localWeee_tax_applied_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weee_tax_applied_amount",
                xmlWriter);

            if (java.lang.Double.isNaN(localWeee_tax_applied_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "weee_tax_applied_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeee_tax_applied_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localWeee_tax_applied_row_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weee_tax_applied_row_amount",
                xmlWriter);

            if (java.lang.Double.isNaN(localWeee_tax_applied_row_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "weee_tax_applied_row_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeee_tax_applied_row_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_weee_tax_applied_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_weee_tax_applied_amount",
                xmlWriter);

            if (java.lang.Double.isNaN(localBase_weee_tax_applied_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_weee_tax_applied_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_weee_tax_applied_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_weee_tax_applied_row_amountTracker) {
            namespace = "";
            writeStartElement(null, namespace,
                "base_weee_tax_applied_row_amount", xmlWriter);

            if (java.lang.Double.isNaN(localBase_weee_tax_applied_row_amount)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_weee_tax_applied_row_amount cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_weee_tax_applied_row_amount));
            }

            xmlWriter.writeEndElement();
        }

        if (localWeee_tax_dispositionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weee_tax_disposition", xmlWriter);

            if (java.lang.Double.isNaN(localWeee_tax_disposition)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "weee_tax_disposition cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeee_tax_disposition));
            }

            xmlWriter.writeEndElement();
        }

        if (localWeee_tax_row_dispositionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "weee_tax_row_disposition",
                xmlWriter);

            if (java.lang.Double.isNaN(localWeee_tax_row_disposition)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "weee_tax_row_disposition cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWeee_tax_row_disposition));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_weee_tax_dispositionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_weee_tax_disposition",
                xmlWriter);

            if (java.lang.Double.isNaN(localBase_weee_tax_disposition)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_weee_tax_disposition cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_weee_tax_disposition));
            }

            xmlWriter.writeEndElement();
        }

        if (localBase_weee_tax_row_dispositionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "base_weee_tax_row_disposition",
                xmlWriter);

            if (java.lang.Double.isNaN(localBase_weee_tax_row_disposition)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "base_weee_tax_row_disposition cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBase_weee_tax_row_disposition));
            }

            xmlWriter.writeEndElement();
        }

        if (localTax_class_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "tax_class_id", xmlWriter);

            if (localTax_class_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_class_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTax_class_id);
            }

            xmlWriter.writeEndElement();
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

        if (localItem_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "item_id"));

            if (localItem_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localItem_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "item_id cannot be null!!");
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

        if (localProduct_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "product_id"));

            if (localProduct_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localProduct_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "product_id cannot be null!!");
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

        if (localParent_item_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "parent_item_id"));

            if (localParent_item_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localParent_item_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "parent_item_id cannot be null!!");
            }
        }

        if (localIs_virtualTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_virtual"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_virtual));
        }

        if (localSkuTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sku"));

            if (localSku != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSku));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "sku cannot be null!!");
            }
        }

        if (localNameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "name"));

            if (localName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localName));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "name cannot be null!!");
            }
        }

        if (localDescriptionTracker) {
            elementList.add(new javax.xml.namespace.QName("", "description"));

            if (localDescription != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDescription));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "description cannot be null!!");
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

        if (localAdditional_dataTracker) {
            elementList.add(new javax.xml.namespace.QName("", "additional_data"));

            if (localAdditional_data != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localAdditional_data));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_data cannot be null!!");
            }
        }

        if (localFree_shippingTracker) {
            elementList.add(new javax.xml.namespace.QName("", "free_shipping"));

            if (localFree_shipping != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localFree_shipping));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "free_shipping cannot be null!!");
            }
        }

        if (localIs_qty_decimalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_qty_decimal"));

            if (localIs_qty_decimal != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_qty_decimal));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_qty_decimal cannot be null!!");
            }
        }

        if (localNo_discountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "no_discount"));

            if (localNo_discount != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localNo_discount));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "no_discount cannot be null!!");
            }
        }

        if (localWeightTracker) {
            elementList.add(new javax.xml.namespace.QName("", "weight"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWeight));
        }

        if (localQtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localQty));
        }

        if (localPriceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "price"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localPrice));
        }

        if (localBase_priceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_price"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_price));
        }

        if (localCustom_priceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "custom_price"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localCustom_price));
        }

        if (localDiscount_percentTracker) {
            elementList.add(new javax.xml.namespace.QName("", "discount_percent"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localDiscount_percent));
        }

        if (localDiscount_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "discount_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localDiscount_amount));
        }

        if (localBase_discount_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_discount_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_discount_amount));
        }

        if (localTax_percentTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_percent"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localTax_percent));
        }

        if (localTax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localTax_amount));
        }

        if (localBase_tax_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_tax_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_tax_amount));
        }

        if (localRow_totalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "row_total"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localRow_total));
        }

        if (localBase_row_totalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_row_total"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_row_total));
        }

        if (localRow_total_with_discountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "row_total_with_discount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localRow_total_with_discount));
        }

        if (localRow_weightTracker) {
            elementList.add(new javax.xml.namespace.QName("", "row_weight"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localRow_weight));
        }

        if (localProduct_typeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "product_type"));

            if (localProduct_type != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localProduct_type));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "product_type cannot be null!!");
            }
        }

        if (localBase_tax_before_discountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_tax_before_discount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_tax_before_discount));
        }

        if (localTax_before_discountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "tax_before_discount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localTax_before_discount));
        }

        if (localOriginal_custom_priceTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "original_custom_price"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localOriginal_custom_price));
        }

        if (localBase_costTracker) {
            elementList.add(new javax.xml.namespace.QName("", "base_cost"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_cost));
        }

        if (localPrice_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("", "price_incl_tax"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localPrice_incl_tax));
        }

        if (localBase_price_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_price_incl_tax"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_price_incl_tax));
        }

        if (localRow_total_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "row_total_incl_tax"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localRow_total_incl_tax));
        }

        if (localBase_row_total_incl_taxTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_row_total_incl_tax"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_row_total_incl_tax));
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

        if (localGift_message_availableTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "gift_message_available"));

            if (localGift_message_available != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGift_message_available));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "gift_message_available cannot be null!!");
            }
        }

        if (localWeee_tax_appliedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "weee_tax_applied"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWeee_tax_applied));
        }

        if (localWeee_tax_applied_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "weee_tax_applied_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWeee_tax_applied_amount));
        }

        if (localWeee_tax_applied_row_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "weee_tax_applied_row_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWeee_tax_applied_row_amount));
        }

        if (localBase_weee_tax_applied_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_weee_tax_applied_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_weee_tax_applied_amount));
        }

        if (localBase_weee_tax_applied_row_amountTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_weee_tax_applied_row_amount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_weee_tax_applied_row_amount));
        }

        if (localWeee_tax_dispositionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "weee_tax_disposition"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWeee_tax_disposition));
        }

        if (localWeee_tax_row_dispositionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "weee_tax_row_disposition"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWeee_tax_row_disposition));
        }

        if (localBase_weee_tax_dispositionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_weee_tax_disposition"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_weee_tax_disposition));
        }

        if (localBase_weee_tax_row_dispositionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "base_weee_tax_row_disposition"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_weee_tax_row_disposition));
        }

        if (localTax_class_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tax_class_id"));

            if (localTax_class_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTax_class_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "tax_class_id cannot be null!!");
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
        public static ShoppingCartItemEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            ShoppingCartItemEntity object = new ShoppingCartItemEntity();

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

                        if (!"shoppingCartItemEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (ShoppingCartItemEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "item_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "item_id" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setItem_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "product_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "product_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setProduct_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "parent_item_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "parent_item_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setParent_item_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setIs_virtual(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_virtual(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sku").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sku" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSku(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "name").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "name" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "description").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "description" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDescription(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setApplied_rule_ids(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "additional_data").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "additional_data" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAdditional_data(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "free_shipping").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "free_shipping" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFree_shipping(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_qty_decimal").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_qty_decimal" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_qty_decimal(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "no_discount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "no_discount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setNo_discount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setWeight(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWeight(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "qty" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setQty(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setQty(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "price").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "price" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPrice(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setPrice(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_price").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_price" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_price(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_price(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "custom_price").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_price" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_price(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setCustom_price(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "discount_percent").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "discount_percent" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDiscount_percent(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setDiscount_percent(java.lang.Double.NaN);
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

                    object.setDiscount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setDiscount_amount(java.lang.Double.NaN);
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

                    object.setBase_discount_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_discount_amount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_percent").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_percent" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTax_percent(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setTax_percent(java.lang.Double.NaN);
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

                    object.setTax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setTax_amount(java.lang.Double.NaN);
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

                    object.setBase_tax_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_tax_amount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "row_total").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "row_total" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRow_total(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setRow_total(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_row_total").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_row_total" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_row_total(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_row_total(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "row_total_with_discount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "row_total_with_discount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRow_total_with_discount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setRow_total_with_discount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "row_weight").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "row_weight" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRow_weight(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setRow_weight(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "product_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "product_type" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setProduct_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_tax_before_discount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_tax_before_discount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_tax_before_discount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_tax_before_discount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_before_discount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_before_discount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTax_before_discount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setTax_before_discount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "original_custom_price").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "original_custom_price" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOriginal_custom_price(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setOriginal_custom_price(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_cost").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_cost" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_cost(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_cost(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "price_incl_tax").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "price_incl_tax" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPrice_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setPrice_incl_tax(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "base_price_incl_tax").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_price_incl_tax" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_price_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_price_incl_tax(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "row_total_incl_tax").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "row_total_incl_tax" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRow_total_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setRow_total_incl_tax(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_row_total_incl_tax").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_row_total_incl_tax" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_row_total_incl_tax(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_row_total_incl_tax(java.lang.Double.NaN);
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

                    java.lang.String content = reader.getElementText();

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

                    java.lang.String content = reader.getElementText();

                    object.setGift_message(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "gift_message_available").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gift_message_available" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setGift_message_available(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "weee_tax_applied").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "weee_tax_applied" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWeee_tax_applied(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWeee_tax_applied(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "weee_tax_applied_amount").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "weee_tax_applied_amount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWeee_tax_applied_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWeee_tax_applied_amount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "weee_tax_applied_row_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "weee_tax_applied_row_amount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWeee_tax_applied_row_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWeee_tax_applied_row_amount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_weee_tax_applied_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_weee_tax_applied_amount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_weee_tax_applied_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_weee_tax_applied_amount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_weee_tax_applied_row_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " +
                            "base_weee_tax_applied_row_amount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_weee_tax_applied_row_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_weee_tax_applied_row_amount(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "weee_tax_disposition").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "weee_tax_disposition" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWeee_tax_disposition(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWeee_tax_disposition(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "weee_tax_row_disposition").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "weee_tax_row_disposition" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWeee_tax_row_disposition(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWeee_tax_row_disposition(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_weee_tax_disposition").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_weee_tax_disposition" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_weee_tax_disposition(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_weee_tax_disposition(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "base_weee_tax_row_disposition").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_weee_tax_row_disposition" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_weee_tax_row_disposition(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBase_weee_tax_row_disposition(java.lang.Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tax_class_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "tax_class_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTax_class_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                throw new java.lang.Exception(e);
            }

            return object;
        }
    } //end of factory class
}
