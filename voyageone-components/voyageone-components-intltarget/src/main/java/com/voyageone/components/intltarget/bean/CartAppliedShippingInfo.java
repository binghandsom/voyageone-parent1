package com.voyageone.components.intltarget.bean;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CartAppliedShippingInfo {

    private String orderId;//	orderId

    private String orderItemId;//	orderItemId

    private String addressId;//	addressId

    private String addressLine;//	addressLine

    private String firstName;//	First Name

    private String middleName;//	Middle Name

    private String lastName;//	Last Name

    private String city;//	city

    private String country;//	country

    private String stateOrProvinceName;//	stateOrProvinceName

    private String zipCode;//	Zip Code

    private String shipModeCode;//	shipModeCode

    private String quantity;//	quantity

    private String price;//	price

    private String expectedDeliveryDate;//	expectedDeliveryDate

    private String signatureRequired;//	Whether signature is required at the time of delivery.

    private String pickUpPersonName;//	Tha name of the pick up person if the order item is store pick up.

    private String storeId;//	Store id of the store where the orderitem will be picked up.

    private String pickUpPhoneNumber;//	Phone number to which the pick up order status has to be notified.

    private String description;//	Name of the product

    private String attachment_path;//	url of the attachment

    private String attachment_usage;//	Resolution of the retrieved image

    private String catalogEntryId;//	Catalog entry id of the product

    private String partNumber;//	Part number of the product

    private String DPCI;//	DPCI of the item

    private String fullfillmentType;//	Applied fullfillmentType

    private String isBulky;//	A flag which indicates item is bulky or not



    /*~~~~~~~~~~~~~~~~ getter setter ~~~~~~~~~~~~~~~*/

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateOrProvinceName() {
        return stateOrProvinceName;
    }

    public void setStateOrProvinceName(String stateOrProvinceName) {
        this.stateOrProvinceName = stateOrProvinceName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getShipModeCode() {
        return shipModeCode;
    }

    public void setShipModeCode(String shipModeCode) {
        this.shipModeCode = shipModeCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getSignatureRequired() {
        return signatureRequired;
    }

    public void setSignatureRequired(String signatureRequired) {
        this.signatureRequired = signatureRequired;
    }

    public String getPickUpPersonName() {
        return pickUpPersonName;
    }

    public void setPickUpPersonName(String pickUpPersonName) {
        this.pickUpPersonName = pickUpPersonName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPickUpPhoneNumber() {
        return pickUpPhoneNumber;
    }

    public void setPickUpPhoneNumber(String pickUpPhoneNumber) {
        this.pickUpPhoneNumber = pickUpPhoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public String getAttachment_usage() {
        return attachment_usage;
    }

    public void setAttachment_usage(String attachment_usage) {
        this.attachment_usage = attachment_usage;
    }

    public String getCatalogEntryId() {
        return catalogEntryId;
    }

    public void setCatalogEntryId(String catalogEntryId) {
        this.catalogEntryId = catalogEntryId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getDPCI() {
        return DPCI;
    }

    public void setDPCI(String DPCI) {
        this.DPCI = DPCI;
    }

    public String getFullfillmentType() {
        return fullfillmentType;
    }

    public void setFullfillmentType(String fullfillmentType) {
        this.fullfillmentType = fullfillmentType;
    }

    public String getIsBulky() {
        return isBulky;
    }

    public void setIsBulky(String isBulky) {
        this.isBulky = isBulky;
    }
}
