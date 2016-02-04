/**
 * MagentoServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.voyageone.common.magento.api.base;


/**
 *  MagentoServiceCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class MagentoServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public MagentoServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public MagentoServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryLevel method
     * override this method for handling normal response from catalogCategoryLevel operation
     */
    public void receiveResultcatalogCategoryLevel(
        magento.CatalogCategoryLevelResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryLevel operation
     */
    public void receiveErrorcatalogCategoryLevel(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for storeList method
     * override this method for handling normal response from storeList operation
     */
    public void receiveResultstoreList(magento.StoreListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from storeList operation
     */
    public void receiveErrorstoreList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentGetCarriers method
     * override this method for handling normal response from salesOrderShipmentGetCarriers operation
     */
    public void receiveResultsalesOrderShipmentGetCarriers(
        magento.SalesOrderShipmentGetCarriersResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentGetCarriers operation
     */
    public void receiveErrorsalesOrderShipmentGetCarriers(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardAccountInfo method
     * override this method for handling normal response from giftcardAccountInfo operation
     */
    public void receiveResultgiftcardAccountInfo(
        magento.GiftcardAccountInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardAccountInfo operation
     */
    public void receiveErrorgiftcardAccountInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionTypes method
     * override this method for handling normal response from catalogProductCustomOptionTypes operation
     */
    public void receiveResultcatalogProductCustomOptionTypes(
        magento.CatalogProductCustomOptionTypesResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionTypes operation
     */
    public void receiveErrorcatalogProductCustomOptionTypes(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductDownloadableLinkRemove method
     * override this method for handling normal response from catalogProductDownloadableLinkRemove operation
     */
    public void receiveResultcatalogProductDownloadableLinkRemove(
        magento.CatalogProductDownloadableLinkRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductDownloadableLinkRemove operation
     */
    public void receiveErrorcatalogProductDownloadableLinkRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardAccountUpdate method
     * override this method for handling normal response from giftcardAccountUpdate operation
     */
    public void receiveResultgiftcardAccountUpdate(
        magento.GiftcardAccountUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardAccountUpdate operation
     */
    public void receiveErrorgiftcardAccountUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartShippingList method
     * override this method for handling normal response from shoppingCartShippingList operation
     */
    public void receiveResultshoppingCartShippingList(
        magento.ShoppingCartShippingListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartShippingList operation
     */
    public void receiveErrorshoppingCartShippingList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductLinkAttributes method
     * override this method for handling normal response from catalogProductLinkAttributes operation
     */
    public void receiveResultcatalogProductLinkAttributes(
        magento.CatalogProductLinkAttributesResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductLinkAttributes operation
     */
    public void receiveErrorcatalogProductLinkAttributes(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardAccountList method
     * override this method for handling normal response from giftcardAccountList operation
     */
    public void receiveResultgiftcardAccountList(
        magento.GiftcardAccountListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardAccountList operation
     */
    public void receiveErrorgiftcardAccountList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductTypeList method
     * override this method for handling normal response from catalogProductTypeList operation
     */
    public void receiveResultcatalogProductTypeList(
        magento.CatalogProductTypeListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductTypeList operation
     */
    public void receiveErrorcatalogProductTypeList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentRemoveTrack method
     * override this method for handling normal response from salesOrderShipmentRemoveTrack operation
     */
    public void receiveResultsalesOrderShipmentRemoveTrack(
        magento.SalesOrderShipmentRemoveTrackResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentRemoveTrack operation
     */
    public void receiveErrorsalesOrderShipmentRemoveTrack(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceCapture method
     * override this method for handling normal response from salesOrderInvoiceCapture operation
     */
    public void receiveResultsalesOrderInvoiceCapture(
        magento.SalesOrderInvoiceCaptureResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceCapture operation
     */
    public void receiveErrorsalesOrderInvoiceCapture(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryAttributeCurrentStore method
     * override this method for handling normal response from catalogCategoryAttributeCurrentStore operation
     */
    public void receiveResultcatalogCategoryAttributeCurrentStore(
        magento.CatalogCategoryAttributeCurrentStoreResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryAttributeCurrentStore operation
     */
    public void receiveErrorcatalogCategoryAttributeCurrentStore(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionInfo method
     * override this method for handling normal response from catalogProductCustomOptionInfo operation
     */
    public void receiveResultcatalogProductCustomOptionInfo(
        magento.CatalogProductCustomOptionInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionInfo operation
     */
    public void receiveErrorcatalogProductCustomOptionInfo(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCreate method
     * override this method for handling normal response from catalogProductCreate operation
     */
    public void receiveResultcatalogProductCreate(
        magento.CatalogProductCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCreate operation
     */
    public void receiveErrorcatalogProductCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderList method
     * override this method for handling normal response from salesOrderList operation
     */
    public void receiveResultsalesOrderList(
        magento.SalesOrderListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderList operation
     */
    public void receiveErrorsalesOrderList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderCreditmemoCancel method
     * override this method for handling normal response from salesOrderCreditmemoCancel operation
     */
    public void receiveResultsalesOrderCreditmemoCancel(
        magento.SalesOrderCreditmemoCancelResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderCreditmemoCancel operation
     */
    public void receiveErrorsalesOrderCreditmemoCancel(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for resources method
     * override this method for handling normal response from resources operation
     */
    public void receiveResultresources(magento.ResourcesResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from resources operation
     */
    public void receiveErrorresources(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeTierPriceInfo method
     * override this method for handling normal response from catalogProductAttributeTierPriceInfo operation
     */
    public void receiveResultcatalogProductAttributeTierPriceInfo(
        magento.CatalogProductAttributeTierPriceInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeTierPriceInfo operation
     */
    public void receiveErrorcatalogProductAttributeTierPriceInfo(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductTagInfo method
     * override this method for handling normal response from catalogProductTagInfo operation
     */
    public void receiveResultcatalogProductTagInfo(
        magento.CatalogProductTagInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductTagInfo operation
     */
    public void receiveErrorcatalogProductTagInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCustomerbalanceSetAmount method
     * override this method for handling normal response from shoppingCartCustomerbalanceSetAmount operation
     */
    public void receiveResultshoppingCartCustomerbalanceSetAmount(
        magento.ShoppingCartCustomerbalanceSetAmountResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCustomerbalanceSetAmount operation
     */
    public void receiveErrorshoppingCartCustomerbalanceSetAmount(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeTierPriceUpdate method
     * override this method for handling normal response from catalogProductAttributeTierPriceUpdate operation
     */
    public void receiveResultcatalogProductAttributeTierPriceUpdate(
        magento.CatalogProductAttributeTierPriceUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeTierPriceUpdate operation
     */
    public void receiveErrorcatalogProductAttributeTierPriceUpdate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeAddOption method
     * override this method for handling normal response from catalogProductAttributeAddOption operation
     */
    public void receiveResultcatalogProductAttributeAddOption(
        magento.CatalogProductAttributeAddOptionResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeAddOption operation
     */
    public void receiveErrorcatalogProductAttributeAddOption(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaCreate method
     * override this method for handling normal response from catalogProductAttributeMediaCreate operation
     */
    public void receiveResultcatalogProductAttributeMediaCreate(
        magento.CatalogProductAttributeMediaCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaCreate operation
     */
    public void receiveErrorcatalogProductAttributeMediaCreate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for directoryRegionList method
     * override this method for handling normal response from directoryRegionList operation
     */
    public void receiveResultdirectoryRegionList(
        magento.DirectoryRegionListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from directoryRegionList operation
     */
    public void receiveErrordirectoryRegionList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for endSession method
     * override this method for handling normal response from endSession operation
     */
    public void receiveResultendSession(magento.EndSessionResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from endSession operation
     */
    public void receiveErrorendSession(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderAddComment method
     * override this method for handling normal response from salesOrderAddComment operation
     */
    public void receiveResultsalesOrderAddComment(
        magento.SalesOrderAddCommentResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderAddComment operation
     */
    public void receiveErrorsalesOrderAddComment(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartProductAdd method
     * override this method for handling normal response from shoppingCartProductAdd operation
     */
    public void receiveResultshoppingCartProductAdd(
        magento.ShoppingCartProductAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartProductAdd operation
     */
    public void receiveErrorshoppingCartProductAdd(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartPaymentMethod method
     * override this method for handling normal response from shoppingCartPaymentMethod operation
     */
    public void receiveResultshoppingCartPaymentMethod(
        magento.ShoppingCartPaymentMethodResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartPaymentMethod operation
     */
    public void receiveErrorshoppingCartPaymentMethod(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductTagList method
     * override this method for handling normal response from catalogProductTagList operation
     */
    public void receiveResultcatalogProductTagList(
        magento.CatalogProductTagListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductTagList operation
     */
    public void receiveErrorcatalogProductTagList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCustomerAddresses method
     * override this method for handling normal response from shoppingCartCustomerAddresses operation
     */
    public void receiveResultshoppingCartCustomerAddresses(
        magento.ShoppingCartCustomerAddressesResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCustomerAddresses operation
     */
    public void receiveErrorshoppingCartCustomerAddresses(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for storeInfo method
     * override this method for handling normal response from storeInfo operation
     */
    public void receiveResultstoreInfo(magento.StoreInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from storeInfo operation
     */
    public void receiveErrorstoreInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductSetSpecialPrice method
     * override this method for handling normal response from catalogProductSetSpecialPrice operation
     */
    public void receiveResultcatalogProductSetSpecialPrice(
        magento.CatalogProductSetSpecialPriceResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductSetSpecialPrice operation
     */
    public void receiveErrorcatalogProductSetSpecialPrice(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartProductList method
     * override this method for handling normal response from shoppingCartProductList operation
     */
    public void receiveResultshoppingCartProductList(
        magento.ShoppingCartProductListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartProductList operation
     */
    public void receiveErrorshoppingCartProductList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInfo method
     * override this method for handling normal response from salesOrderInfo operation
     */
    public void receiveResultsalesOrderInfo(
        magento.SalesOrderInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInfo operation
     */
    public void receiveErrorsalesOrderInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductLinkUpdate method
     * override this method for handling normal response from catalogProductLinkUpdate operation
     */
    public void receiveResultcatalogProductLinkUpdate(
        magento.CatalogProductLinkUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductLinkUpdate operation
     */
    public void receiveErrorcatalogProductLinkUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductGetSpecialPrice method
     * override this method for handling normal response from catalogProductGetSpecialPrice operation
     */
    public void receiveResultcatalogProductGetSpecialPrice(
        magento.CatalogProductGetSpecialPriceResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductGetSpecialPrice operation
     */
    public void receiveErrorcatalogProductGetSpecialPrice(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeList method
     * override this method for handling normal response from catalogProductAttributeList operation
     */
    public void receiveResultcatalogProductAttributeList(
        magento.CatalogProductAttributeListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeList operation
     */
    public void receiveErrorcatalogProductAttributeList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetAttributeRemove method
     * override this method for handling normal response from catalogProductAttributeSetAttributeRemove operation
     */
    public void receiveResultcatalogProductAttributeSetAttributeRemove(
        magento.CatalogProductAttributeSetAttributeRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetAttributeRemove operation
     */
    public void receiveErrorcatalogProductAttributeSetAttributeRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceInfo method
     * override this method for handling normal response from salesOrderInvoiceInfo operation
     */
    public void receiveResultsalesOrderInvoiceInfo(
        magento.SalesOrderInvoiceInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceInfo operation
     */
    public void receiveErrorsalesOrderInvoiceInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartInfo method
     * override this method for handling normal response from shoppingCartInfo operation
     */
    public void receiveResultshoppingCartInfo(
        magento.ShoppingCartInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartInfo operation
     */
    public void receiveErrorshoppingCartInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartProductMoveToCustomerQuote method
     * override this method for handling normal response from shoppingCartProductMoveToCustomerQuote operation
     */
    public void receiveResultshoppingCartProductMoveToCustomerQuote(
        magento.ShoppingCartProductMoveToCustomerQuoteResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartProductMoveToCustomerQuote operation
     */
    public void receiveErrorshoppingCartProductMoveToCustomerQuote(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryInfo method
     * override this method for handling normal response from catalogCategoryInfo operation
     */
    public void receiveResultcatalogCategoryInfo(
        magento.CatalogCategoryInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryInfo operation
     */
    public void receiveErrorcatalogCategoryInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartGiftcardAdd method
     * override this method for handling normal response from shoppingCartGiftcardAdd operation
     */
    public void receiveResultshoppingCartGiftcardAdd(
        magento.ShoppingCartGiftcardAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartGiftcardAdd operation
     */
    public void receiveErrorshoppingCartGiftcardAdd(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryAssignProduct method
     * override this method for handling normal response from catalogCategoryAssignProduct operation
     */
    public void receiveResultcatalogCategoryAssignProduct(
        magento.CatalogCategoryAssignProductResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryAssignProduct operation
     */
    public void receiveErrorcatalogCategoryAssignProduct(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeCurrentStore method
     * override this method for handling normal response from catalogProductAttributeCurrentStore operation
     */
    public void receiveResultcatalogProductAttributeCurrentStore(
        magento.CatalogProductAttributeCurrentStoreResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeCurrentStore operation
     */
    public void receiveErrorcatalogProductAttributeCurrentStore(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaCurrentStore method
     * override this method for handling normal response from catalogProductAttributeMediaCurrentStore operation
     */
    public void receiveResultcatalogProductAttributeMediaCurrentStore(
        magento.CatalogProductAttributeMediaCurrentStoreResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaCurrentStore operation
     */
    public void receiveErrorcatalogProductAttributeMediaCurrentStore(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductUpdate method
     * override this method for handling normal response from catalogProductUpdate operation
     */
    public void receiveResultcatalogProductUpdate(
        magento.CatalogProductUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductUpdate operation
     */
    public void receiveErrorcatalogProductUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaList method
     * override this method for handling normal response from catalogProductAttributeMediaList operation
     */
    public void receiveResultcatalogProductAttributeMediaList(
        magento.CatalogProductAttributeMediaListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaList operation
     */
    public void receiveErrorcatalogProductAttributeMediaList(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeInfo method
     * override this method for handling normal response from catalogProductAttributeInfo operation
     */
    public void receiveResultcatalogProductAttributeInfo(
        magento.CatalogProductAttributeInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeInfo operation
     */
    public void receiveErrorcatalogProductAttributeInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for startSession method
     * override this method for handling normal response from startSession operation
     */
    public void receiveResultstartSession(
        magento.StartSessionResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from startSession operation
     */
    public void receiveErrorstartSession(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductMultiUpdate method
     * override this method for handling normal response from catalogProductMultiUpdate operation
     */
    public void receiveResultcatalogProductMultiUpdate(
        magento.CatalogProductMultiUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductMultiUpdate operation
     */
    public void receiveErrorcatalogProductMultiUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentList method
     * override this method for handling normal response from salesOrderShipmentList operation
     */
    public void receiveResultsalesOrderShipmentList(
        magento.SalesOrderShipmentListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentList operation
     */
    public void receiveErrorsalesOrderShipmentList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for enterpriseCustomerbalanceBalance method
     * override this method for handling normal response from enterpriseCustomerbalanceBalance operation
     */
    public void receiveResultenterpriseCustomerbalanceBalance(
        magento.EnterpriseCustomerbalanceBalanceResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from enterpriseCustomerbalanceBalance operation
     */
    public void receiveErrorenterpriseCustomerbalanceBalance(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductLinkAssign method
     * override this method for handling normal response from catalogProductLinkAssign operation
     */
    public void receiveResultcatalogProductLinkAssign(
        magento.CatalogProductLinkAssignResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductLinkAssign operation
     */
    public void receiveErrorcatalogProductLinkAssign(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceList method
     * override this method for handling normal response from salesOrderInvoiceList operation
     */
    public void receiveResultsalesOrderInvoiceList(
        magento.SalesOrderInvoiceListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceList operation
     */
    public void receiveErrorsalesOrderInvoiceList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductLinkList method
     * override this method for handling normal response from catalogProductLinkList operation
     */
    public void receiveResultcatalogProductLinkList(
        magento.CatalogProductLinkListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductLinkList operation
     */
    public void receiveErrorcatalogProductLinkList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeCreate method
     * override this method for handling normal response from catalogProductAttributeCreate operation
     */
    public void receiveResultcatalogProductAttributeCreate(
        magento.CatalogProductAttributeCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeCreate operation
     */
    public void receiveErrorcatalogProductAttributeCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceCreate method
     * override this method for handling normal response from salesOrderInvoiceCreate operation
     */
    public void receiveResultsalesOrderInvoiceCreate(
        magento.SalesOrderInvoiceCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceCreate operation
     */
    public void receiveErrorsalesOrderInvoiceCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerGroupList method
     * override this method for handling normal response from customerGroupList operation
     */
    public void receiveResultcustomerGroupList(
        magento.CustomerGroupListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerGroupList operation
     */
    public void receiveErrorcustomerGroupList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionUpdate method
     * override this method for handling normal response from catalogProductCustomOptionUpdate operation
     */
    public void receiveResultcatalogProductCustomOptionUpdate(
        magento.CatalogProductCustomOptionUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionUpdate operation
     */
    public void receiveErrorcatalogProductCustomOptionUpdate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetList method
     * override this method for handling normal response from catalogProductAttributeSetList operation
     */
    public void receiveResultcatalogProductAttributeSetList(
        magento.CatalogProductAttributeSetListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetList operation
     */
    public void receiveErrorcatalogProductAttributeSetList(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionValueRemove method
     * override this method for handling normal response from catalogProductCustomOptionValueRemove operation
     */
    public void receiveResultcatalogProductCustomOptionValueRemove(
        magento.CatalogProductCustomOptionValueRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionValueRemove operation
     */
    public void receiveErrorcatalogProductCustomOptionValueRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryMove method
     * override this method for handling normal response from catalogCategoryMove operation
     */
    public void receiveResultcatalogCategoryMove(
        magento.CatalogCategoryMoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryMove operation
     */
    public void receiveErrorcatalogCategoryMove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryUpdate method
     * override this method for handling normal response from catalogCategoryUpdate operation
     */
    public void receiveResultcatalogCategoryUpdate(
        magento.CatalogCategoryUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryUpdate operation
     */
    public void receiveErrorcatalogCategoryUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetRemove method
     * override this method for handling normal response from catalogProductAttributeSetRemove operation
     */
    public void receiveResultcatalogProductAttributeSetRemove(
        magento.CatalogProductAttributeSetRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetRemove operation
     */
    public void receiveErrorcatalogProductAttributeSetRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryAttributeList method
     * override this method for handling normal response from catalogCategoryAttributeList operation
     */
    public void receiveResultcatalogCategoryAttributeList(
        magento.CatalogCategoryAttributeListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryAttributeList operation
     */
    public void receiveErrorcatalogCategoryAttributeList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartProductUpdate method
     * override this method for handling normal response from shoppingCartProductUpdate operation
     */
    public void receiveResultshoppingCartProductUpdate(
        magento.ShoppingCartProductUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartProductUpdate operation
     */
    public void receiveErrorshoppingCartProductUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerAddressInfo method
     * override this method for handling normal response from customerAddressInfo operation
     */
    public void receiveResultcustomerAddressInfo(
        magento.CustomerAddressInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerAddressInfo operation
     */
    public void receiveErrorcustomerAddressInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionList method
     * override this method for handling normal response from catalogProductCustomOptionList operation
     */
    public void receiveResultcatalogProductCustomOptionList(
        magento.CatalogProductCustomOptionListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionList operation
     */
    public void receiveErrorcatalogProductCustomOptionList(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceVoid method
     * override this method for handling normal response from salesOrderInvoiceVoid operation
     */
    public void receiveResultsalesOrderInvoiceVoid(
        magento.SalesOrderInvoiceVoidResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceVoid operation
     */
    public void receiveErrorsalesOrderInvoiceVoid(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetCreate method
     * override this method for handling normal response from catalogProductAttributeSetCreate operation
     */
    public void receiveResultcatalogProductAttributeSetCreate(
        magento.CatalogProductAttributeSetCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetCreate operation
     */
    public void receiveErrorcatalogProductAttributeSetCreate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryCreate method
     * override this method for handling normal response from catalogCategoryCreate operation
     */
    public void receiveResultcatalogCategoryCreate(
        magento.CatalogCategoryCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryCreate operation
     */
    public void receiveErrorcatalogCategoryCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaInfo method
     * override this method for handling normal response from catalogProductAttributeMediaInfo operation
     */
    public void receiveResultcatalogProductAttributeMediaInfo(
        magento.CatalogProductAttributeMediaInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaInfo operation
     */
    public void receiveErrorcatalogProductAttributeMediaInfo(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerCustomerUpdate method
     * override this method for handling normal response from customerCustomerUpdate operation
     */
    public void receiveResultcustomerCustomerUpdate(
        magento.CustomerCustomerUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerCustomerUpdate operation
     */
    public void receiveErrorcustomerCustomerUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for magentoInfo method
     * override this method for handling normal response from magentoInfo operation
     */
    public void receiveResultmagentoInfo(
        magento.MagentoInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from magentoInfo operation
     */
    public void receiveErrormagentoInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardAccountCreate method
     * override this method for handling normal response from giftcardAccountCreate operation
     */
    public void receiveResultgiftcardAccountCreate(
        magento.GiftcardAccountCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardAccountCreate operation
     */
    public void receiveErrorgiftcardAccountCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCreate method
     * override this method for handling normal response from shoppingCartCreate operation
     */
    public void receiveResultshoppingCartCreate(
        magento.ShoppingCartCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCreate operation
     */
    public void receiveErrorshoppingCartCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftMessageSetForQuote method
     * override this method for handling normal response from giftMessageSetForQuote operation
     */
    public void receiveResultgiftMessageSetForQuote(
        magento.GiftMessageForQuoteResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftMessageSetForQuote operation
     */
    public void receiveErrorgiftMessageSetForQuote(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentSendInfo method
     * override this method for handling normal response from salesOrderShipmentSendInfo operation
     */
    public void receiveResultsalesOrderShipmentSendInfo(
        magento.SalesOrderShipmentSendInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentSendInfo operation
     */
    public void receiveErrorsalesOrderShipmentSendInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetAttributeAdd method
     * override this method for handling normal response from catalogProductAttributeSetAttributeAdd operation
     */
    public void receiveResultcatalogProductAttributeSetAttributeAdd(
        magento.CatalogProductAttributeSetAttributeAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetAttributeAdd operation
     */
    public void receiveErrorcatalogProductAttributeSetAttributeAdd(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartProductRemove method
     * override this method for handling normal response from shoppingCartProductRemove operation
     */
    public void receiveResultshoppingCartProductRemove(
        magento.ShoppingCartProductRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartProductRemove operation
     */
    public void receiveErrorshoppingCartProductRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for login method
     * override this method for handling normal response from login operation
     */
    public void receiveResultlogin(magento.LoginResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from login operation
     */
    public void receiveErrorlogin(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionAdd method
     * override this method for handling normal response from catalogProductCustomOptionAdd operation
     */
    public void receiveResultcatalogProductCustomOptionAdd(
        magento.CatalogProductCustomOptionAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionAdd operation
     */
    public void receiveErrorcatalogProductCustomOptionAdd(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryAttributeOptions method
     * override this method for handling normal response from catalogCategoryAttributeOptions operation
     */
    public void receiveResultcatalogCategoryAttributeOptions(
        magento.CatalogCategoryAttributeOptionsResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryAttributeOptions operation
     */
    public void receiveErrorcatalogCategoryAttributeOptions(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderCreditmemoInfo method
     * override this method for handling normal response from salesOrderCreditmemoInfo operation
     */
    public void receiveResultsalesOrderCreditmemoInfo(
        magento.SalesOrderCreditmemoInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderCreditmemoInfo operation
     */
    public void receiveErrorsalesOrderCreditmemoInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerCustomerInfo method
     * override this method for handling normal response from customerCustomerInfo operation
     */
    public void receiveResultcustomerCustomerInfo(
        magento.CustomerCustomerInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerCustomerInfo operation
     */
    public void receiveErrorcustomerCustomerInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderCreditmemoCreate method
     * override this method for handling normal response from salesOrderCreditmemoCreate operation
     */
    public void receiveResultsalesOrderCreditmemoCreate(
        magento.SalesOrderCreditmemoCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderCreditmemoCreate operation
     */
    public void receiveErrorsalesOrderCreditmemoCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderCancel method
     * override this method for handling normal response from salesOrderCancel operation
     */
    public void receiveResultsalesOrderCancel(
        magento.SalesOrderCancelResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderCancel operation
     */
    public void receiveErrorsalesOrderCancel(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCouponRemove method
     * override this method for handling normal response from shoppingCartCouponRemove operation
     */
    public void receiveResultshoppingCartCouponRemove(
        magento.ShoppingCartCouponRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCouponRemove operation
     */
    public void receiveErrorshoppingCartCouponRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartShippingMethod method
     * override this method for handling normal response from shoppingCartShippingMethod operation
     */
    public void receiveResultshoppingCartShippingMethod(
        magento.ShoppingCartShippingMethodResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartShippingMethod operation
     */
    public void receiveErrorshoppingCartShippingMethod(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCustomerbalanceRemoveAmount method
     * override this method for handling normal response from shoppingCartCustomerbalanceRemoveAmount operation
     */
    public void receiveResultshoppingCartCustomerbalanceRemoveAmount(
        magento.ShoppingCartCustomerbalanceRemoveAmountResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCustomerbalanceRemoveAmount operation
     */
    public void receiveErrorshoppingCartCustomerbalanceRemoveAmount(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentAddTrack method
     * override this method for handling normal response from salesOrderShipmentAddTrack operation
     */
    public void receiveResultsalesOrderShipmentAddTrack(
        magento.SalesOrderShipmentAddTrackResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentAddTrack operation
     */
    public void receiveErrorsalesOrderShipmentAddTrack(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductTagUpdate method
     * override this method for handling normal response from catalogProductTagUpdate operation
     */
    public void receiveResultcatalogProductTagUpdate(
        magento.CatalogProductTagUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductTagUpdate operation
     */
    public void receiveErrorcatalogProductTagUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductDownloadableLinkAdd method
     * override this method for handling normal response from catalogProductDownloadableLinkAdd operation
     */
    public void receiveResultcatalogProductDownloadableLinkAdd(
        magento.CatalogProductDownloadableLinkAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductDownloadableLinkAdd operation
     */
    public void receiveErrorcatalogProductDownloadableLinkAdd(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartExternalId method
     * override this method for handling normal response from shoppingCartExternalId operation
     */
    public void receiveResultshoppingCartExternalId(
        magento.ShoppingCartExternalIdResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartExternalId operation
     */
    public void receiveErrorshoppingCartExternalId(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for resourceFaults method
     * override this method for handling normal response from resourceFaults operation
     */
    public void receiveResultresourceFaults(
        magento.ResourceFaultsResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from resourceFaults operation
     */
    public void receiveErrorresourceFaults(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerAddressList method
     * override this method for handling normal response from customerAddressList operation
     */
    public void receiveResultcustomerAddressList(
        magento.CustomerAddressListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerAddressList operation
     */
    public void receiveErrorcustomerAddressList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogInventoryStockItemUpdate method
     * override this method for handling normal response from catalogInventoryStockItemUpdate operation
     */
    public void receiveResultcatalogInventoryStockItemUpdate(
        magento.CatalogInventoryStockItemUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogInventoryStockItemUpdate operation
     */
    public void receiveErrorcatalogInventoryStockItemUpdate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartPaymentList method
     * override this method for handling normal response from shoppingCartPaymentList operation
     */
    public void receiveResultshoppingCartPaymentList(
        magento.ShoppingCartPaymentListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartPaymentList operation
     */
    public void receiveErrorshoppingCartPaymentList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderCreditmemoAddComment method
     * override this method for handling normal response from salesOrderCreditmemoAddComment operation
     */
    public void receiveResultsalesOrderCreditmemoAddComment(
        magento.SalesOrderCreditmemoAddCommentResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderCreditmemoAddComment operation
     */
    public void receiveErrorsalesOrderCreditmemoAddComment(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductTagRemove method
     * override this method for handling normal response from catalogProductTagRemove operation
     */
    public void receiveResultcatalogProductTagRemove(
        magento.CatalogProductTagRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductTagRemove operation
     */
    public void receiveErrorcatalogProductTagRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentInfo method
     * override this method for handling normal response from salesOrderShipmentInfo operation
     */
    public void receiveResultsalesOrderShipmentInfo(
        magento.SalesOrderShipmentInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentInfo operation
     */
    public void receiveErrorsalesOrderShipmentInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartProductSetCustomPrice method
     * override this method for handling normal response from shoppingCartProductSetCustomPrice operation
     */
    public void receiveResultshoppingCartProductSetCustomPrice(
        magento.ShoppingCartProductCustomPriceResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartProductSetCustomPrice operation
     */
    public void receiveErrorshoppingCartProductSetCustomPrice(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartGiftcardRemove method
     * override this method for handling normal response from shoppingCartGiftcardRemove operation
     */
    public void receiveResultshoppingCartGiftcardRemove(
        magento.ShoppingCartGiftcardRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartGiftcardRemove operation
     */
    public void receiveErrorshoppingCartGiftcardRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryDelete method
     * override this method for handling normal response from catalogCategoryDelete operation
     */
    public void receiveResultcatalogCategoryDelete(
        magento.CatalogCategoryDeleteResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryDelete operation
     */
    public void receiveErrorcatalogCategoryDelete(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeOptions method
     * override this method for handling normal response from catalogProductAttributeOptions operation
     */
    public void receiveResultcatalogProductAttributeOptions(
        magento.CatalogProductAttributeOptionsResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeOptions operation
     */
    public void receiveErrorcatalogProductAttributeOptions(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerAddressDelete method
     * override this method for handling normal response from customerAddressDelete operation
     */
    public void receiveResultcustomerAddressDelete(
        magento.CustomerAddressDeleteResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerAddressDelete operation
     */
    public void receiveErrorcustomerAddressDelete(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for enterpriseCustomerbalanceHistory method
     * override this method for handling normal response from enterpriseCustomerbalanceHistory operation
     */
    public void receiveResultenterpriseCustomerbalanceHistory(
        magento.EnterpriseCustomerbalanceHistoryResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from enterpriseCustomerbalanceHistory operation
     */
    public void receiveErrorenterpriseCustomerbalanceHistory(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCouponAdd method
     * override this method for handling normal response from shoppingCartCouponAdd operation
     */
    public void receiveResultshoppingCartCouponAdd(
        magento.ShoppingCartCouponAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCouponAdd operation
     */
    public void receiveErrorshoppingCartCouponAdd(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for directoryCountryList method
     * override this method for handling normal response from directoryCountryList operation
     */
    public void receiveResultdirectoryCountryList(
        magento.DirectoryCountryListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from directoryCountryList operation
     */
    public void receiveErrordirectoryCountryList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryCurrentStore method
     * override this method for handling normal response from catalogCategoryCurrentStore operation
     */
    public void receiveResultcatalogCategoryCurrentStore(
        magento.CatalogCategoryCurrentStoreResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryCurrentStore operation
     */
    public void receiveErrorcatalogCategoryCurrentStore(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryUpdateProduct method
     * override this method for handling normal response from catalogCategoryUpdateProduct operation
     */
    public void receiveResultcatalogCategoryUpdateProduct(
        magento.CatalogCategoryUpdateProductResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryUpdateProduct operation
     */
    public void receiveErrorcatalogCategoryUpdateProduct(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeUpdate method
     * override this method for handling normal response from catalogProductAttributeUpdate operation
     */
    public void receiveResultcatalogProductAttributeUpdate(
        magento.CatalogProductAttributeUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeUpdate operation
     */
    public void receiveErrorcatalogProductAttributeUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for globalFaults method
     * override this method for handling normal response from globalFaults operation
     */
    public void receiveResultglobalFaults(
        magento.GlobalFaultsResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from globalFaults operation
     */
    public void receiveErrorglobalFaults(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartOrder method
     * override this method for handling normal response from shoppingCartOrder operation
     */
    public void receiveResultshoppingCartOrder(
        magento.ShoppingCartOrderResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartOrder operation
     */
    public void receiveErrorshoppingCartOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerCustomerDelete method
     * override this method for handling normal response from customerCustomerDelete operation
     */
    public void receiveResultcustomerCustomerDelete(
        magento.CustomerCustomerDeleteResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerCustomerDelete operation
     */
    public void receiveErrorcustomerCustomerDelete(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryTree method
     * override this method for handling normal response from catalogCategoryTree operation
     */
    public void receiveResultcatalogCategoryTree(
        magento.CatalogCategoryTreeResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryTree operation
     */
    public void receiveErrorcatalogCategoryTree(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetGroupAdd method
     * override this method for handling normal response from catalogProductAttributeSetGroupAdd operation
     */
    public void receiveResultcatalogProductAttributeSetGroupAdd(
        magento.CatalogProductAttributeSetGroupAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetGroupAdd operation
     */
    public void receiveErrorcatalogProductAttributeSetGroupAdd(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaRemove method
     * override this method for handling normal response from catalogProductAttributeMediaRemove operation
     */
    public void receiveResultcatalogProductAttributeMediaRemove(
        magento.CatalogProductAttributeMediaRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaRemove operation
     */
    public void receiveErrorcatalogProductAttributeMediaRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderUnhold method
     * override this method for handling normal response from salesOrderUnhold operation
     */
    public void receiveResultsalesOrderUnhold(
        magento.SalesOrderUnholdResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderUnhold operation
     */
    public void receiveErrorsalesOrderUnhold(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftMessageSetForQuoteItem method
     * override this method for handling normal response from giftMessageSetForQuoteItem operation
     */
    public void receiveResultgiftMessageSetForQuoteItem(
        magento.GiftMessageForQuoteItemResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftMessageSetForQuoteItem operation
     */
    public void receiveErrorgiftMessageSetForQuoteItem(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeRemove method
     * override this method for handling normal response from catalogProductAttributeRemove operation
     */
    public void receiveResultcatalogProductAttributeRemove(
        magento.CatalogProductAttributeRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeRemove operation
     */
    public void receiveErrorcatalogProductAttributeRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for call method
     * override this method for handling normal response from call operation
     */
    public void receiveResultcall(magento.CallResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from call operation
     */
    public void receiveErrorcall(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaTypes method
     * override this method for handling normal response from catalogProductAttributeMediaTypes operation
     */
    public void receiveResultcatalogProductAttributeMediaTypes(
        magento.CatalogProductAttributeMediaTypesResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaTypes operation
     */
    public void receiveErrorcatalogProductAttributeMediaTypes(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeMediaUpdate method
     * override this method for handling normal response from catalogProductAttributeMediaUpdate operation
     */
    public void receiveResultcatalogProductAttributeMediaUpdate(
        magento.CatalogProductAttributeMediaUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeMediaUpdate operation
     */
    public void receiveErrorcatalogProductAttributeMediaUpdate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogInventoryStockItemList method
     * override this method for handling normal response from catalogInventoryStockItemList operation
     */
    public void receiveResultcatalogInventoryStockItemList(
        magento.CatalogInventoryStockItemListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogInventoryStockItemList operation
     */
    public void receiveErrorcatalogInventoryStockItemList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerCustomerList method
     * override this method for handling normal response from customerCustomerList operation
     */
    public void receiveResultcustomerCustomerList(
        magento.CustomerCustomerListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerCustomerList operation
     */
    public void receiveErrorcustomerCustomerList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardCustomerRedeem method
     * override this method for handling normal response from giftcardCustomerRedeem operation
     */
    public void receiveResultgiftcardCustomerRedeem(
        magento.GiftcardCustomerRedeemResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardCustomerRedeem operation
     */
    public void receiveErrorgiftcardCustomerRedeem(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetGroupRemove method
     * override this method for handling normal response from catalogProductAttributeSetGroupRemove operation
     */
    public void receiveResultcatalogProductAttributeSetGroupRemove(
        magento.CatalogProductAttributeSetGroupRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetGroupRemove operation
     */
    public void receiveErrorcatalogProductAttributeSetGroupRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductLinkRemove method
     * override this method for handling normal response from catalogProductLinkRemove operation
     */
    public void receiveResultcatalogProductLinkRemove(
        magento.CatalogProductLinkRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductLinkRemove operation
     */
    public void receiveErrorcatalogProductLinkRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardCustomerInfo method
     * override this method for handling normal response from giftcardCustomerInfo operation
     */
    public void receiveResultgiftcardCustomerInfo(
        magento.GiftcardCustomerInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardCustomerInfo operation
     */
    public void receiveErrorgiftcardCustomerInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductList method
     * override this method for handling normal response from catalogProductList operation
     */
    public void receiveResultcatalogProductList(
        magento.CatalogProductListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductList operation
     */
    public void receiveErrorcatalogProductList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionValueAdd method
     * override this method for handling normal response from catalogProductCustomOptionValueAdd operation
     */
    public void receiveResultcatalogProductCustomOptionValueAdd(
        magento.CatalogProductCustomOptionValueAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionValueAdd operation
     */
    public void receiveErrorcatalogProductCustomOptionValueAdd(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductDownloadableLinkList method
     * override this method for handling normal response from catalogProductDownloadableLinkList operation
     */
    public void receiveResultcatalogProductDownloadableLinkList(
        magento.CatalogProductDownloadableLinkListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductDownloadableLinkList operation
     */
    public void receiveErrorcatalogProductDownloadableLinkList(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductDelete method
     * override this method for handling normal response from catalogProductDelete operation
     */
    public void receiveResultcatalogProductDelete(
        magento.CatalogProductDeleteResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductDelete operation
     */
    public void receiveErrorcatalogProductDelete(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderHold method
     * override this method for handling normal response from salesOrderHold operation
     */
    public void receiveResultsalesOrderHold(
        magento.SalesOrderHoldResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderHold operation
     */
    public void receiveErrorsalesOrderHold(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartGiftcardList method
     * override this method for handling normal response from shoppingCartGiftcardList operation
     */
    public void receiveResultshoppingCartGiftcardList(
        magento.ShoppingCartGiftcardListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartGiftcardList operation
     */
    public void receiveErrorshoppingCartGiftcardList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerAddressUpdate method
     * override this method for handling normal response from customerAddressUpdate operation
     */
    public void receiveResultcustomerAddressUpdate(
        magento.CustomerAddressUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerAddressUpdate operation
     */
    public void receiveErrorcustomerAddressUpdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftMessageSetForQuoteProduct method
     * override this method for handling normal response from giftMessageSetForQuoteProduct operation
     */
    public void receiveResultgiftMessageSetForQuoteProduct(
        magento.GiftMessageForQuoteProductResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftMessageSetForQuoteProduct operation
     */
    public void receiveErrorgiftMessageSetForQuoteProduct(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionValueList method
     * override this method for handling normal response from catalogProductCustomOptionValueList operation
     */
    public void receiveResultcatalogProductCustomOptionValueList(
        magento.CatalogProductCustomOptionValueListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionValueList operation
     */
    public void receiveErrorcatalogProductCustomOptionValueList(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceAddComment method
     * override this method for handling normal response from salesOrderInvoiceAddComment operation
     */
    public void receiveResultsalesOrderInvoiceAddComment(
        magento.SalesOrderInvoiceAddCommentResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceAddComment operation
     */
    public void receiveErrorsalesOrderInvoiceAddComment(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryRemoveProduct method
     * override this method for handling normal response from catalogCategoryRemoveProduct operation
     */
    public void receiveResultcatalogCategoryRemoveProduct(
        magento.CatalogCategoryRemoveProductResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryRemoveProduct operation
     */
    public void receiveErrorcatalogCategoryRemoveProduct(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionValueUpdate method
     * override this method for handling normal response from catalogProductCustomOptionValueUpdate operation
     */
    public void receiveResultcatalogProductCustomOptionValueUpdate(
        magento.CatalogProductCustomOptionValueUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionValueUpdate operation
     */
    public void receiveErrorcatalogProductCustomOptionValueUpdate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductLinkTypes method
     * override this method for handling normal response from catalogProductLinkTypes operation
     */
    public void receiveResultcatalogProductLinkTypes(
        magento.CatalogProductLinkTypesResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductLinkTypes operation
     */
    public void receiveErrorcatalogProductLinkTypes(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionValueInfo method
     * override this method for handling normal response from catalogProductCustomOptionValueInfo operation
     */
    public void receiveResultcatalogProductCustomOptionValueInfo(
        magento.CatalogProductCustomOptionValueInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionValueInfo operation
     */
    public void receiveErrorcatalogProductCustomOptionValueInfo(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderInvoiceCancel method
     * override this method for handling normal response from salesOrderInvoiceCancel operation
     */
    public void receiveResultsalesOrderInvoiceCancel(
        magento.SalesOrderInvoiceCancelResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderInvoiceCancel operation
     */
    public void receiveErrorsalesOrderInvoiceCancel(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerCustomerCreate method
     * override this method for handling normal response from customerCustomerCreate operation
     */
    public void receiveResultcustomerCustomerCreate(
        magento.CustomerCustomerCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerCustomerCreate operation
     */
    public void receiveErrorcustomerCustomerCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogCategoryAssignedProducts method
     * override this method for handling normal response from catalogCategoryAssignedProducts operation
     */
    public void receiveResultcatalogCategoryAssignedProducts(
        magento.CatalogCategoryAssignedProductsResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogCategoryAssignedProducts operation
     */
    public void receiveErrorcatalogCategoryAssignedProducts(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartLicense method
     * override this method for handling normal response from shoppingCartLicense operation
     */
    public void receiveResultshoppingCartLicense(
        magento.ShoppingCartLicenseResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartLicense operation
     */
    public void receiveErrorshoppingCartLicense(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductTagAdd method
     * override this method for handling normal response from catalogProductTagAdd operation
     */
    public void receiveResultcatalogProductTagAdd(
        magento.CatalogProductTagAddResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductTagAdd operation
     */
    public void receiveErrorcatalogProductTagAdd(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartTotals method
     * override this method for handling normal response from shoppingCartTotals operation
     */
    public void receiveResultshoppingCartTotals(
        magento.ShoppingCartTotalsResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartTotals operation
     */
    public void receiveErrorshoppingCartTotals(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for customerAddressCreate method
     * override this method for handling normal response from customerAddressCreate operation
     */
    public void receiveResultcustomerAddressCreate(
        magento.CustomerAddressCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from customerAddressCreate operation
     */
    public void receiveErrorcustomerAddressCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCurrentStore method
     * override this method for handling normal response from catalogProductCurrentStore operation
     */
    public void receiveResultcatalogProductCurrentStore(
        magento.CatalogProductCurrentStoreResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCurrentStore operation
     */
    public void receiveErrorcatalogProductCurrentStore(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductInfo method
     * override this method for handling normal response from catalogProductInfo operation
     */
    public void receiveResultcatalogProductInfo(
        magento.CatalogProductInfoResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductInfo operation
     */
    public void receiveErrorcatalogProductInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shoppingCartCustomerSet method
     * override this method for handling normal response from shoppingCartCustomerSet operation
     */
    public void receiveResultshoppingCartCustomerSet(
        magento.ShoppingCartCustomerSetResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shoppingCartCustomerSet operation
     */
    public void receiveErrorshoppingCartCustomerSet(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for multiCall method
     * override this method for handling normal response from multiCall operation
     */
    public void receiveResultmultiCall(magento.MultiCallResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from multiCall operation
     */
    public void receiveErrormultiCall(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductCustomOptionRemove method
     * override this method for handling normal response from catalogProductCustomOptionRemove operation
     */
    public void receiveResultcatalogProductCustomOptionRemove(
        magento.CatalogProductCustomOptionRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductCustomOptionRemove operation
     */
    public void receiveErrorcatalogProductCustomOptionRemove(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogProductAttributeSetGroupRename method
     * override this method for handling normal response from catalogProductAttributeSetGroupRename operation
     */
    public void receiveResultcatalogProductAttributeSetGroupRename(
        magento.CatalogProductAttributeSetGroupRenameResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogProductAttributeSetGroupRename operation
     */
    public void receiveErrorcatalogProductAttributeSetGroupRename(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for catalogInventoryStockItemMultiUpdate method
     * override this method for handling normal response from catalogInventoryStockItemMultiUpdate operation
     */
    public void receiveResultcatalogInventoryStockItemMultiUpdate(
        magento.CatalogInventoryStockItemMultiUpdateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from catalogInventoryStockItemMultiUpdate operation
     */
    public void receiveErrorcatalogInventoryStockItemMultiUpdate(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for giftcardAccountRemove method
     * override this method for handling normal response from giftcardAccountRemove operation
     */
    public void receiveResultgiftcardAccountRemove(
        magento.GiftcardAccountRemoveResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from giftcardAccountRemove operation
     */
    public void receiveErrorgiftcardAccountRemove(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentAddComment method
     * override this method for handling normal response from salesOrderShipmentAddComment operation
     */
    public void receiveResultsalesOrderShipmentAddComment(
        magento.SalesOrderShipmentAddCommentResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentAddComment operation
     */
    public void receiveErrorsalesOrderShipmentAddComment(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderShipmentCreate method
     * override this method for handling normal response from salesOrderShipmentCreate operation
     */
    public void receiveResultsalesOrderShipmentCreate(
        magento.SalesOrderShipmentCreateResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderShipmentCreate operation
     */
    public void receiveErrorsalesOrderShipmentCreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for salesOrderCreditmemoList method
     * override this method for handling normal response from salesOrderCreditmemoList operation
     */
    public void receiveResultsalesOrderCreditmemoList(
        magento.SalesOrderCreditmemoListResponseParam result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from salesOrderCreditmemoList operation
     */
    public void receiveErrorsalesOrderCreditmemoList(java.lang.Exception e) {
    }
}
