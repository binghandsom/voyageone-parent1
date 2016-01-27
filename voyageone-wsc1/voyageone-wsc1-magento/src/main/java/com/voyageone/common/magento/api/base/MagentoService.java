/**
 * MagentoService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.voyageone.common.magento.api.base;


/*
 *  MagentoService java interface
 */
public interface MagentoService {
    /**
     * Auto generated method signature
     * Retrieve hierarchical tree of categories.
     * @param catalogCategoryLevelRequestParam1
     */
    public magento.CatalogCategoryLevelResponseParam catalogCategoryLevel(
        magento.CatalogCategoryLevelRequestParam catalogCategoryLevelRequestParam1)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve hierarchical tree of categories.
     * @param catalogCategoryLevelRequestParam1
     */
    public void startcatalogCategoryLevel(
        magento.CatalogCategoryLevelRequestParam catalogCategoryLevelRequestParam1,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * List of stores
     * @param storeListRequestParam3
     */
    public magento.StoreListResponseParam storeList(
        magento.StoreListRequestParam storeListRequestParam3)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * List of stores
     * @param storeListRequestParam3
     */
    public void startstoreList(
        magento.StoreListRequestParam storeListRequestParam3,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of allowed carriers for order
     * @param salesOrderShipmentGetCarriersRequestParam5
     */
    public magento.SalesOrderShipmentGetCarriersResponseParam salesOrderShipmentGetCarriers(
        magento.SalesOrderShipmentGetCarriersRequestParam salesOrderShipmentGetCarriersRequestParam5)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of allowed carriers for order
     * @param salesOrderShipmentGetCarriersRequestParam5
     */
    public void startsalesOrderShipmentGetCarriers(
        magento.SalesOrderShipmentGetCarriersRequestParam salesOrderShipmentGetCarriersRequestParam5,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove gift card account
     * @param giftcardAccountInfoRequestParam7
     */
    public magento.GiftcardAccountInfoResponseParam giftcardAccountInfo(
        magento.GiftcardAccountInfoRequestParam giftcardAccountInfoRequestParam7)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove gift card account
     * @param giftcardAccountInfoRequestParam7
     */
    public void startgiftcardAccountInfo(
        magento.GiftcardAccountInfoRequestParam giftcardAccountInfoRequestParam7,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get list of available custom option types
     * @param catalogProductCustomOptionTypesRequestParam9
     */
    public magento.CatalogProductCustomOptionTypesResponseParam catalogProductCustomOptionTypes(
        magento.CatalogProductCustomOptionTypesRequestParam catalogProductCustomOptionTypesRequestParam9)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get list of available custom option types
     * @param catalogProductCustomOptionTypesRequestParam9
     */
    public void startcatalogProductCustomOptionTypes(
        magento.CatalogProductCustomOptionTypesRequestParam catalogProductCustomOptionTypesRequestParam9,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove links and samples from downloadable product
     * @param catalogProductDownloadableLinkRemoveRequestParam11
     */
    public magento.CatalogProductDownloadableLinkRemoveResponseParam catalogProductDownloadableLinkRemove(
        magento.CatalogProductDownloadableLinkRemoveRequestParam catalogProductDownloadableLinkRemoveRequestParam11)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove links and samples from downloadable product
     * @param catalogProductDownloadableLinkRemoveRequestParam11
     */
    public void startcatalogProductDownloadableLinkRemove(
        magento.CatalogProductDownloadableLinkRemoveRequestParam catalogProductDownloadableLinkRemoveRequestParam11,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove gift card account
     * @param giftcardAccountUpdateRequestParam13
     */
    public magento.GiftcardAccountUpdateResponseParam giftcardAccountUpdate(
        magento.GiftcardAccountUpdateRequestParam giftcardAccountUpdateRequestParam13)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove gift card account
     * @param giftcardAccountUpdateRequestParam13
     */
    public void startgiftcardAccountUpdate(
        magento.GiftcardAccountUpdateRequestParam giftcardAccountUpdateRequestParam13,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get list of available shipping methods
     * @param shoppingCartShippingListRequestParam15
     */
    public magento.ShoppingCartShippingListResponseParam shoppingCartShippingList(
        magento.ShoppingCartShippingListRequestParam shoppingCartShippingListRequestParam15)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get list of available shipping methods
     * @param shoppingCartShippingListRequestParam15
     */
    public void startshoppingCartShippingList(
        magento.ShoppingCartShippingListRequestParam shoppingCartShippingListRequestParam15,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product link type attributes
     * @param catalogProductLinkAttributesRequestParam17
     */
    public magento.CatalogProductLinkAttributesResponseParam catalogProductLinkAttributes(
        magento.CatalogProductLinkAttributesRequestParam catalogProductLinkAttributesRequestParam17)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product link type attributes
     * @param catalogProductLinkAttributesRequestParam17
     */
    public void startcatalogProductLinkAttributes(
        magento.CatalogProductLinkAttributesRequestParam catalogProductLinkAttributesRequestParam17,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove gift card account
     * @param giftcardAccountListRequestParam19
     */
    public magento.GiftcardAccountListResponseParam giftcardAccountList(
        magento.GiftcardAccountListRequestParam giftcardAccountListRequestParam19)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove gift card account
     * @param giftcardAccountListRequestParam19
     */
    public void startgiftcardAccountList(
        magento.GiftcardAccountListRequestParam giftcardAccountListRequestParam19,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product types
     * @param catalogProductTypeListRequestParam21
     */
    public magento.CatalogProductTypeListResponseParam catalogProductTypeList(
        magento.CatalogProductTypeListRequestParam catalogProductTypeListRequestParam21)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product types
     * @param catalogProductTypeListRequestParam21
     */
    public void startcatalogProductTypeList(
        magento.CatalogProductTypeListRequestParam catalogProductTypeListRequestParam21,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove tracking number
     * @param salesOrderShipmentRemoveTrackRequestParam23
     */
    public magento.SalesOrderShipmentRemoveTrackResponseParam salesOrderShipmentRemoveTrack(
        magento.SalesOrderShipmentRemoveTrackRequestParam salesOrderShipmentRemoveTrackRequestParam23)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove tracking number
     * @param salesOrderShipmentRemoveTrackRequestParam23
     */
    public void startsalesOrderShipmentRemoveTrack(
        magento.SalesOrderShipmentRemoveTrackRequestParam salesOrderShipmentRemoveTrackRequestParam23,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Capture invoice
     * @param salesOrderInvoiceCaptureRequestParam25
     */
    public magento.SalesOrderInvoiceCaptureResponseParam salesOrderInvoiceCapture(
        magento.SalesOrderInvoiceCaptureRequestParam salesOrderInvoiceCaptureRequestParam25)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Capture invoice
     * @param salesOrderInvoiceCaptureRequestParam25
     */
    public void startsalesOrderInvoiceCapture(
        magento.SalesOrderInvoiceCaptureRequestParam salesOrderInvoiceCaptureRequestParam25,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set/Get current store view
     * @param catalogCategoryAttributeCurrentStoreRequestParam27
     */
    public magento.CatalogCategoryAttributeCurrentStoreResponseParam catalogCategoryAttributeCurrentStore(
        magento.CatalogCategoryAttributeCurrentStoreRequestParam catalogCategoryAttributeCurrentStoreRequestParam27)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set/Get current store view
     * @param catalogCategoryAttributeCurrentStoreRequestParam27
     */
    public void startcatalogCategoryAttributeCurrentStore(
        magento.CatalogCategoryAttributeCurrentStoreRequestParam catalogCategoryAttributeCurrentStoreRequestParam27,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get full information about custom option in product
     * @param catalogProductCustomOptionInfoRequestParam29
     */
    public magento.CatalogProductCustomOptionInfoResponseParam catalogProductCustomOptionInfo(
        magento.CatalogProductCustomOptionInfoRequestParam catalogProductCustomOptionInfoRequestParam29)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get full information about custom option in product
     * @param catalogProductCustomOptionInfoRequestParam29
     */
    public void startcatalogProductCustomOptionInfo(
        magento.CatalogProductCustomOptionInfoRequestParam catalogProductCustomOptionInfoRequestParam29,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create new product and return product id
     * @param catalogProductCreateRequestParam31
     */
    public magento.CatalogProductCreateResponseParam catalogProductCreate(
        magento.CatalogProductCreateRequestParam catalogProductCreateRequestParam31)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create new product and return product id
     * @param catalogProductCreateRequestParam31
     */
    public void startcatalogProductCreate(
        magento.CatalogProductCreateRequestParam catalogProductCreateRequestParam31,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of orders by filters
     * @param salesOrderListRequestParam33
     */
    public magento.SalesOrderListResponseParam salesOrderList(
        magento.SalesOrderListRequestParam salesOrderListRequestParam33)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of orders by filters
     * @param salesOrderListRequestParam33
     */
    public void startsalesOrderList(
        magento.SalesOrderListRequestParam salesOrderListRequestParam33,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Cancel creditmemo
     * @param salesOrderCreditmemoCancelRequestParam35
     */
    public magento.SalesOrderCreditmemoCancelResponseParam salesOrderCreditmemoCancel(
        magento.SalesOrderCreditmemoCancelRequestParam salesOrderCreditmemoCancelRequestParam35)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Cancel creditmemo
     * @param salesOrderCreditmemoCancelRequestParam35
     */
    public void startsalesOrderCreditmemoCancel(
        magento.SalesOrderCreditmemoCancelRequestParam salesOrderCreditmemoCancelRequestParam35,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * List of available resources
     * @param resourcesRequestParam37
     */
    public magento.ResourcesResponseParam resources(
        magento.ResourcesRequestParam resourcesRequestParam37)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * List of available resources
     * @param resourcesRequestParam37
     */
    public void startresources(
        magento.ResourcesRequestParam resourcesRequestParam37,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product tier prices
     * @param catalogProductAttributeTierPriceInfoRequestParam39
     */
    public magento.CatalogProductAttributeTierPriceInfoResponseParam catalogProductAttributeTierPriceInfo(
        magento.CatalogProductAttributeTierPriceInfoRequestParam catalogProductAttributeTierPriceInfoRequestParam39)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product tier prices
     * @param catalogProductAttributeTierPriceInfoRequestParam39
     */
    public void startcatalogProductAttributeTierPriceInfo(
        magento.CatalogProductAttributeTierPriceInfoRequestParam catalogProductAttributeTierPriceInfoRequestParam39,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product tag info
     * @param catalogProductTagInfoRequestParam41
     */
    public magento.CatalogProductTagInfoResponseParam catalogProductTagInfo(
        magento.CatalogProductTagInfoRequestParam catalogProductTagInfoRequestParam41)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product tag info
     * @param catalogProductTagInfoRequestParam41
     */
    public void startcatalogProductTagInfo(
        magento.CatalogProductTagInfoRequestParam catalogProductTagInfoRequestParam41,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set amount from customer store credit into shopping cart (quote)
     * @param shoppingCartCustomerbalanceSetAmountRequestParam43
     */
    public magento.ShoppingCartCustomerbalanceSetAmountResponseParam shoppingCartCustomerbalanceSetAmount(
        magento.ShoppingCartCustomerbalanceSetAmountRequestParam shoppingCartCustomerbalanceSetAmountRequestParam43)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set amount from customer store credit into shopping cart (quote)
     * @param shoppingCartCustomerbalanceSetAmountRequestParam43
     */
    public void startshoppingCartCustomerbalanceSetAmount(
        magento.ShoppingCartCustomerbalanceSetAmountRequestParam shoppingCartCustomerbalanceSetAmountRequestParam43,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product tier prices
     * @param catalogProductAttributeTierPriceUpdateRequestParam45
     */
    public magento.CatalogProductAttributeTierPriceUpdateResponseParam catalogProductAttributeTierPriceUpdate(
        magento.CatalogProductAttributeTierPriceUpdateRequestParam catalogProductAttributeTierPriceUpdateRequestParam45)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product tier prices
     * @param catalogProductAttributeTierPriceUpdateRequestParam45
     */
    public void startcatalogProductAttributeTierPriceUpdate(
        magento.CatalogProductAttributeTierPriceUpdateRequestParam catalogProductAttributeTierPriceUpdateRequestParam45,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add option to attribute
     * @param catalogProductAttributeAddOptionRequestParam47
     */
    public magento.CatalogProductAttributeAddOptionResponseParam catalogProductAttributeAddOption(
        magento.CatalogProductAttributeAddOptionRequestParam catalogProductAttributeAddOptionRequestParam47)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add option to attribute
     * @param catalogProductAttributeAddOptionRequestParam47
     */
    public void startcatalogProductAttributeAddOption(
        magento.CatalogProductAttributeAddOptionRequestParam catalogProductAttributeAddOptionRequestParam47,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Upload new product image
     * @param catalogProductAttributeMediaCreateRequestParam49
     */
    public magento.CatalogProductAttributeMediaCreateResponseParam catalogProductAttributeMediaCreate(
        magento.CatalogProductAttributeMediaCreateRequestParam catalogProductAttributeMediaCreateRequestParam49)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Upload new product image
     * @param catalogProductAttributeMediaCreateRequestParam49
     */
    public void startcatalogProductAttributeMediaCreate(
        magento.CatalogProductAttributeMediaCreateRequestParam catalogProductAttributeMediaCreateRequestParam49,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * List of regions in specified country
     * @param directoryRegionListRequestParam51
     */
    public magento.DirectoryRegionListResponseParam directoryRegionList(
        magento.DirectoryRegionListRequestParam directoryRegionListRequestParam51)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * List of regions in specified country
     * @param directoryRegionListRequestParam51
     */
    public void startdirectoryRegionList(
        magento.DirectoryRegionListRequestParam directoryRegionListRequestParam51,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * End web service session
     * @param endSessionParam53
     */
    public magento.EndSessionResponseParam endSession(
        magento.EndSessionParam endSessionParam53)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * End web service session
     * @param endSessionParam53
     */
    public void startendSession(magento.EndSessionParam endSessionParam53,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add comment to order
     * @param salesOrderAddCommentRequestParam55
     */
    public magento.SalesOrderAddCommentResponseParam salesOrderAddComment(
        magento.SalesOrderAddCommentRequestParam salesOrderAddCommentRequestParam55)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add comment to order
     * @param salesOrderAddCommentRequestParam55
     */
    public void startsalesOrderAddComment(
        magento.SalesOrderAddCommentRequestParam salesOrderAddCommentRequestParam55,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add product(s) to shopping cart
     * @param shoppingCartProductAddRequestParam57
     */
    public magento.ShoppingCartProductAddResponseParam shoppingCartProductAdd(
        magento.ShoppingCartProductAddRequestParam shoppingCartProductAddRequestParam57)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add product(s) to shopping cart
     * @param shoppingCartProductAddRequestParam57
     */
    public void startshoppingCartProductAdd(
        magento.ShoppingCartProductAddRequestParam shoppingCartProductAddRequestParam57,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set payment method
     * @param shoppingCartPaymentMethodRequestParam59
     */
    public magento.ShoppingCartPaymentMethodResponseParam shoppingCartPaymentMethod(
        magento.ShoppingCartPaymentMethodRequestParam shoppingCartPaymentMethodRequestParam59)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set payment method
     * @param shoppingCartPaymentMethodRequestParam59
     */
    public void startshoppingCartPaymentMethod(
        magento.ShoppingCartPaymentMethodRequestParam shoppingCartPaymentMethodRequestParam59,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of tags by product
     * @param catalogProductTagListRequestParam61
     */
    public magento.CatalogProductTagListResponseParam catalogProductTagList(
        magento.CatalogProductTagListRequestParam catalogProductTagListRequestParam61)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of tags by product
     * @param catalogProductTagListRequestParam61
     */
    public void startcatalogProductTagList(
        magento.CatalogProductTagListRequestParam catalogProductTagListRequestParam61,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set customer's addresses in shopping cart
     * @param shoppingCartCustomerAddressesRequestParam63
     */
    public magento.ShoppingCartCustomerAddressesResponseParam shoppingCartCustomerAddresses(
        magento.ShoppingCartCustomerAddressesRequestParam shoppingCartCustomerAddressesRequestParam63)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set customer's addresses in shopping cart
     * @param shoppingCartCustomerAddressesRequestParam63
     */
    public void startshoppingCartCustomerAddresses(
        magento.ShoppingCartCustomerAddressesRequestParam shoppingCartCustomerAddressesRequestParam63,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Store view info
     * @param storeInfoRequestParam65
     */
    public magento.StoreInfoResponseParam storeInfo(
        magento.StoreInfoRequestParam storeInfoRequestParam65)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Store view info
     * @param storeInfoRequestParam65
     */
    public void startstoreInfo(
        magento.StoreInfoRequestParam storeInfoRequestParam65,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product special price
     * @param catalogProductSetSpecialPriceRequestParam67
     */
    public magento.CatalogProductSetSpecialPriceResponseParam catalogProductSetSpecialPrice(
        magento.CatalogProductSetSpecialPriceRequestParam catalogProductSetSpecialPriceRequestParam67)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product special price
     * @param catalogProductSetSpecialPriceRequestParam67
     */
    public void startcatalogProductSetSpecialPrice(
        magento.CatalogProductSetSpecialPriceRequestParam catalogProductSetSpecialPriceRequestParam67,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get list of products in shopping cart
     * @param shoppingCartProductListRequestParam69
     */
    public magento.ShoppingCartProductListResponseParam shoppingCartProductList(
        magento.ShoppingCartProductListRequestParam shoppingCartProductListRequestParam69)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get list of products in shopping cart
     * @param shoppingCartProductListRequestParam69
     */
    public void startshoppingCartProductList(
        magento.ShoppingCartProductListRequestParam shoppingCartProductListRequestParam69,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve order information
     * @param salesOrderInfoRequestParam71
     */
    public magento.SalesOrderInfoResponseParam salesOrderInfo(
        magento.SalesOrderInfoRequestParam salesOrderInfoRequestParam71)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve order information
     * @param salesOrderInfoRequestParam71
     */
    public void startsalesOrderInfo(
        magento.SalesOrderInfoRequestParam salesOrderInfoRequestParam71,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product link
     * @param catalogProductLinkUpdateRequestParam73
     */
    public magento.CatalogProductLinkUpdateResponseParam catalogProductLinkUpdate(
        magento.CatalogProductLinkUpdateRequestParam catalogProductLinkUpdateRequestParam73)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product link
     * @param catalogProductLinkUpdateRequestParam73
     */
    public void startcatalogProductLinkUpdate(
        magento.CatalogProductLinkUpdateRequestParam catalogProductLinkUpdateRequestParam73,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get product special price data
     * @param catalogProductGetSpecialPriceRequestParam75
     */
    public magento.CatalogProductGetSpecialPriceResponseParam catalogProductGetSpecialPrice(
        magento.CatalogProductGetSpecialPriceRequestParam catalogProductGetSpecialPriceRequestParam75)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get product special price data
     * @param catalogProductGetSpecialPriceRequestParam75
     */
    public void startcatalogProductGetSpecialPrice(
        magento.CatalogProductGetSpecialPriceRequestParam catalogProductGetSpecialPriceRequestParam75,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve attribute list
     * @param catalogProductAttributeListRequestParam77
     */
    public magento.CatalogProductAttributeListResponseParam catalogProductAttributeList(
        magento.CatalogProductAttributeListRequestParam catalogProductAttributeListRequestParam77)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve attribute list
     * @param catalogProductAttributeListRequestParam77
     */
    public void startcatalogProductAttributeList(
        magento.CatalogProductAttributeListRequestParam catalogProductAttributeListRequestParam77,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove attribute from attribute set
     * @param catalogProductAttributeSetAttributeRemoveRequestParam79
     */
    public magento.CatalogProductAttributeSetAttributeRemoveResponseParam catalogProductAttributeSetAttributeRemove(
        magento.CatalogProductAttributeSetAttributeRemoveRequestParam catalogProductAttributeSetAttributeRemoveRequestParam79)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove attribute from attribute set
     * @param catalogProductAttributeSetAttributeRemoveRequestParam79
     */
    public void startcatalogProductAttributeSetAttributeRemove(
        magento.CatalogProductAttributeSetAttributeRemoveRequestParam catalogProductAttributeSetAttributeRemoveRequestParam79,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve invoice information
     * @param salesOrderInvoiceInfoRequestParam81
     */
    public magento.SalesOrderInvoiceInfoResponseParam salesOrderInvoiceInfo(
        magento.SalesOrderInvoiceInfoRequestParam salesOrderInvoiceInfoRequestParam81)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve invoice information
     * @param salesOrderInvoiceInfoRequestParam81
     */
    public void startsalesOrderInvoiceInfo(
        magento.SalesOrderInvoiceInfoRequestParam salesOrderInvoiceInfoRequestParam81,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve information about shopping cart
     * @param shoppingCartInfoRequestParam83
     */
    public magento.ShoppingCartInfoResponseParam shoppingCartInfo(
        magento.ShoppingCartInfoRequestParam shoppingCartInfoRequestParam83)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve information about shopping cart
     * @param shoppingCartInfoRequestParam83
     */
    public void startshoppingCartInfo(
        magento.ShoppingCartInfoRequestParam shoppingCartInfoRequestParam83,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Move product(s) to customer quote
     * @param shoppingCartProductMoveToCustomerQuoteRequestParam85
     */
    public magento.ShoppingCartProductMoveToCustomerQuoteResponseParam shoppingCartProductMoveToCustomerQuote(
        magento.ShoppingCartProductMoveToCustomerQuoteRequestParam shoppingCartProductMoveToCustomerQuoteRequestParam85)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Move product(s) to customer quote
     * @param shoppingCartProductMoveToCustomerQuoteRequestParam85
     */
    public void startshoppingCartProductMoveToCustomerQuote(
        magento.ShoppingCartProductMoveToCustomerQuoteRequestParam shoppingCartProductMoveToCustomerQuoteRequestParam85,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve hierarchical tree of categories.
     * @param catalogCategoryInfoRequestParam87
     */
    public magento.CatalogCategoryInfoResponseParam catalogCategoryInfo(
        magento.CatalogCategoryInfoRequestParam catalogCategoryInfoRequestParam87)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve hierarchical tree of categories.
     * @param catalogCategoryInfoRequestParam87
     */
    public void startcatalogCategoryInfo(
        magento.CatalogCategoryInfoRequestParam catalogCategoryInfoRequestParam87,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Redeem gift card amount
     * @param shoppingCartGiftcardAddRequestParam89
     */
    public magento.ShoppingCartGiftcardAddResponseParam shoppingCartGiftcardAdd(
        magento.ShoppingCartGiftcardAddRequestParam shoppingCartGiftcardAddRequestParam89)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Redeem gift card amount
     * @param shoppingCartGiftcardAddRequestParam89
     */
    public void startshoppingCartGiftcardAdd(
        magento.ShoppingCartGiftcardAddRequestParam shoppingCartGiftcardAddRequestParam89,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Assign product to category
     * @param catalogCategoryAssignProductRequestParam91
     */
    public magento.CatalogCategoryAssignProductResponseParam catalogCategoryAssignProduct(
        magento.CatalogCategoryAssignProductRequestParam catalogCategoryAssignProductRequestParam91)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Assign product to category
     * @param catalogCategoryAssignProductRequestParam91
     */
    public void startcatalogCategoryAssignProduct(
        magento.CatalogCategoryAssignProductRequestParam catalogCategoryAssignProductRequestParam91,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set/Get current store view
     * @param catalogProductAttributeCurrentStoreRequestParam93
     */
    public magento.CatalogProductAttributeCurrentStoreResponseParam catalogProductAttributeCurrentStore(
        magento.CatalogProductAttributeCurrentStoreRequestParam catalogProductAttributeCurrentStoreRequestParam93)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set/Get current store view
     * @param catalogProductAttributeCurrentStoreRequestParam93
     */
    public void startcatalogProductAttributeCurrentStore(
        magento.CatalogProductAttributeCurrentStoreRequestParam catalogProductAttributeCurrentStoreRequestParam93,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set/Get current store view
     * @param catalogProductAttributeMediaCurrentStoreRequestParam95
     */
    public magento.CatalogProductAttributeMediaCurrentStoreResponseParam catalogProductAttributeMediaCurrentStore(
        magento.CatalogProductAttributeMediaCurrentStoreRequestParam catalogProductAttributeMediaCurrentStoreRequestParam95)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set/Get current store view
     * @param catalogProductAttributeMediaCurrentStoreRequestParam95
     */
    public void startcatalogProductAttributeMediaCurrentStore(
        magento.CatalogProductAttributeMediaCurrentStoreRequestParam catalogProductAttributeMediaCurrentStoreRequestParam95,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product
     * @param catalogProductUpdateRequestParam97
     */
    public magento.CatalogProductUpdateResponseParam catalogProductUpdate(
        magento.CatalogProductUpdateRequestParam catalogProductUpdateRequestParam97)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product
     * @param catalogProductUpdateRequestParam97
     */
    public void startcatalogProductUpdate(
        magento.CatalogProductUpdateRequestParam catalogProductUpdateRequestParam97,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product image list
     * @param catalogProductAttributeMediaListRequestParam99
     */
    public magento.CatalogProductAttributeMediaListResponseParam catalogProductAttributeMediaList(
        magento.CatalogProductAttributeMediaListRequestParam catalogProductAttributeMediaListRequestParam99)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product image list
     * @param catalogProductAttributeMediaListRequestParam99
     */
    public void startcatalogProductAttributeMediaList(
        magento.CatalogProductAttributeMediaListRequestParam catalogProductAttributeMediaListRequestParam99,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get full information about attribute with list of options
     * @param catalogProductAttributeInfoRequestParam101
     */
    public magento.CatalogProductAttributeInfoResponseParam catalogProductAttributeInfo(
        magento.CatalogProductAttributeInfoRequestParam catalogProductAttributeInfoRequestParam101)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get full information about attribute with list of options
     * @param catalogProductAttributeInfoRequestParam101
     */
    public void startcatalogProductAttributeInfo(
        magento.CatalogProductAttributeInfoRequestParam catalogProductAttributeInfoRequestParam101,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Start web service session
     */
    public magento.StartSessionResponseParam startSession()
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Start web service session
     */
    public void startstartSession(
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Product multi update
     * @param catalogProductMultiUpdateRequestParam105
     */
    public magento.CatalogProductMultiUpdateResponseParam catalogProductMultiUpdate(
        magento.CatalogProductMultiUpdateRequestParam catalogProductMultiUpdateRequestParam105)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Product multi update
     * @param catalogProductMultiUpdateRequestParam105
     */
    public void startcatalogProductMultiUpdate(
        magento.CatalogProductMultiUpdateRequestParam catalogProductMultiUpdateRequestParam105,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of shipments by filters
     * @param salesOrderShipmentListRequestParam107
     */
    public magento.SalesOrderShipmentListResponseParam salesOrderShipmentList(
        magento.SalesOrderShipmentListRequestParam salesOrderShipmentListRequestParam107)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of shipments by filters
     * @param salesOrderShipmentListRequestParam107
     */
    public void startsalesOrderShipmentList(
        magento.SalesOrderShipmentListRequestParam salesOrderShipmentListRequestParam107,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customer store credit balance information
     * @param enterpriseCustomerbalanceBalanceRequestParam109
     */
    public magento.EnterpriseCustomerbalanceBalanceResponseParam enterpriseCustomerbalanceBalance(
        magento.EnterpriseCustomerbalanceBalanceRequestParam enterpriseCustomerbalanceBalanceRequestParam109)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customer store credit balance information
     * @param enterpriseCustomerbalanceBalanceRequestParam109
     */
    public void startenterpriseCustomerbalanceBalance(
        magento.EnterpriseCustomerbalanceBalanceRequestParam enterpriseCustomerbalanceBalanceRequestParam109,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Assign product link
     * @param catalogProductLinkAssignRequestParam111
     */
    public magento.CatalogProductLinkAssignResponseParam catalogProductLinkAssign(
        magento.CatalogProductLinkAssignRequestParam catalogProductLinkAssignRequestParam111)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Assign product link
     * @param catalogProductLinkAssignRequestParam111
     */
    public void startcatalogProductLinkAssign(
        magento.CatalogProductLinkAssignRequestParam catalogProductLinkAssignRequestParam111,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of invoices by filters
     * @param salesOrderInvoiceListRequestParam113
     */
    public magento.SalesOrderInvoiceListResponseParam salesOrderInvoiceList(
        magento.SalesOrderInvoiceListRequestParam salesOrderInvoiceListRequestParam113)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of invoices by filters
     * @param salesOrderInvoiceListRequestParam113
     */
    public void startsalesOrderInvoiceList(
        magento.SalesOrderInvoiceListRequestParam salesOrderInvoiceListRequestParam113,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve linked products
     * @param catalogProductLinkListRequestParam115
     */
    public magento.CatalogProductLinkListResponseParam catalogProductLinkList(
        magento.CatalogProductLinkListRequestParam catalogProductLinkListRequestParam115)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve linked products
     * @param catalogProductLinkListRequestParam115
     */
    public void startcatalogProductLinkList(
        magento.CatalogProductLinkListRequestParam catalogProductLinkListRequestParam115,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create new attribute
     * @param catalogProductAttributeCreateRequestParam117
     */
    public magento.CatalogProductAttributeCreateResponseParam catalogProductAttributeCreate(
        magento.CatalogProductAttributeCreateRequestParam catalogProductAttributeCreateRequestParam117)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create new attribute
     * @param catalogProductAttributeCreateRequestParam117
     */
    public void startcatalogProductAttributeCreate(
        magento.CatalogProductAttributeCreateRequestParam catalogProductAttributeCreateRequestParam117,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create new invoice for order
     * @param salesOrderInvoiceCreateRequestParam119
     */
    public magento.SalesOrderInvoiceCreateResponseParam salesOrderInvoiceCreate(
        magento.SalesOrderInvoiceCreateRequestParam salesOrderInvoiceCreateRequestParam119)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create new invoice for order
     * @param salesOrderInvoiceCreateRequestParam119
     */
    public void startsalesOrderInvoiceCreate(
        magento.SalesOrderInvoiceCreateRequestParam salesOrderInvoiceCreateRequestParam119,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customer groups
     * @param customerGroupListRequestParam121
     */
    public magento.CustomerGroupListResponseParam customerGroupList(
        magento.CustomerGroupListRequestParam customerGroupListRequestParam121)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customer groups
     * @param customerGroupListRequestParam121
     */
    public void startcustomerGroupList(
        magento.CustomerGroupListRequestParam customerGroupListRequestParam121,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product custom option
     * @param catalogProductCustomOptionUpdateRequestParam123
     */
    public magento.CatalogProductCustomOptionUpdateResponseParam catalogProductCustomOptionUpdate(
        magento.CatalogProductCustomOptionUpdateRequestParam catalogProductCustomOptionUpdateRequestParam123)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product custom option
     * @param catalogProductCustomOptionUpdateRequestParam123
     */
    public void startcatalogProductCustomOptionUpdate(
        magento.CatalogProductCustomOptionUpdateRequestParam catalogProductCustomOptionUpdateRequestParam123,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product attribute sets
     * @param catalogProductAttributeSetListRequestParam125
     */
    public magento.CatalogProductAttributeSetListResponseParam catalogProductAttributeSetList(
        magento.CatalogProductAttributeSetListRequestParam catalogProductAttributeSetListRequestParam125)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product attribute sets
     * @param catalogProductAttributeSetListRequestParam125
     */
    public void startcatalogProductAttributeSetList(
        magento.CatalogProductAttributeSetListRequestParam catalogProductAttributeSetListRequestParam125,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove value from custom option
     * @param catalogProductCustomOptionValueRemoveRequestParam127
     */
    public magento.CatalogProductCustomOptionValueRemoveResponseParam catalogProductCustomOptionValueRemove(
        magento.CatalogProductCustomOptionValueRemoveRequestParam catalogProductCustomOptionValueRemoveRequestParam127)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove value from custom option
     * @param catalogProductCustomOptionValueRemoveRequestParam127
     */
    public void startcatalogProductCustomOptionValueRemove(
        magento.CatalogProductCustomOptionValueRemoveRequestParam catalogProductCustomOptionValueRemoveRequestParam127,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Move category in tree
     * @param catalogCategoryMoveRequestParam129
     */
    public magento.CatalogCategoryMoveResponseParam catalogCategoryMove(
        magento.CatalogCategoryMoveRequestParam catalogCategoryMoveRequestParam129)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Move category in tree
     * @param catalogCategoryMoveRequestParam129
     */
    public void startcatalogCategoryMove(
        magento.CatalogCategoryMoveRequestParam catalogCategoryMoveRequestParam129,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update category
     * @param catalogCategoryUpdateRequestParam131
     */
    public magento.CatalogCategoryUpdateResponseParam catalogCategoryUpdate(
        magento.CatalogCategoryUpdateRequestParam catalogCategoryUpdateRequestParam131)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update category
     * @param catalogCategoryUpdateRequestParam131
     */
    public void startcatalogCategoryUpdate(
        magento.CatalogCategoryUpdateRequestParam catalogCategoryUpdateRequestParam131,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove product attribute set
     * @param catalogProductAttributeSetRemoveRequestParam133
     */
    public magento.CatalogProductAttributeSetRemoveResponseParam catalogProductAttributeSetRemove(
        magento.CatalogProductAttributeSetRemoveRequestParam catalogProductAttributeSetRemoveRequestParam133)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove product attribute set
     * @param catalogProductAttributeSetRemoveRequestParam133
     */
    public void startcatalogProductAttributeSetRemove(
        magento.CatalogProductAttributeSetRemoveRequestParam catalogProductAttributeSetRemoveRequestParam133,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve category attributes
     * @param catalogCategoryAttributeListRequestParam135
     */
    public magento.CatalogCategoryAttributeListResponseParam catalogCategoryAttributeList(
        magento.CatalogCategoryAttributeListRequestParam catalogCategoryAttributeListRequestParam135)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve category attributes
     * @param catalogCategoryAttributeListRequestParam135
     */
    public void startcatalogCategoryAttributeList(
        magento.CatalogCategoryAttributeListRequestParam catalogCategoryAttributeListRequestParam135,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product(s) quantities in shopping cart
     * @param shoppingCartProductUpdateRequestParam137
     */
    public magento.ShoppingCartProductUpdateResponseParam shoppingCartProductUpdate(
        magento.ShoppingCartProductUpdateRequestParam shoppingCartProductUpdateRequestParam137)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product(s) quantities in shopping cart
     * @param shoppingCartProductUpdateRequestParam137
     */
    public void startshoppingCartProductUpdate(
        magento.ShoppingCartProductUpdateRequestParam shoppingCartProductUpdateRequestParam137,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customer address data
     * @param customerAddressInfoRequestParam139
     */
    public magento.CustomerAddressInfoResponseParam customerAddressInfo(
        magento.CustomerAddressInfoRequestParam customerAddressInfoRequestParam139)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customer address data
     * @param customerAddressInfoRequestParam139
     */
    public void startcustomerAddressInfo(
        magento.CustomerAddressInfoRequestParam customerAddressInfoRequestParam139,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of product custom options
     * @param catalogProductCustomOptionListRequestParam141
     */
    public magento.CatalogProductCustomOptionListResponseParam catalogProductCustomOptionList(
        magento.CatalogProductCustomOptionListRequestParam catalogProductCustomOptionListRequestParam141)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of product custom options
     * @param catalogProductCustomOptionListRequestParam141
     */
    public void startcatalogProductCustomOptionList(
        magento.CatalogProductCustomOptionListRequestParam catalogProductCustomOptionListRequestParam141,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Void invoice
     * @param salesOrderInvoiceVoidRequestParam143
     */
    public magento.SalesOrderInvoiceVoidResponseParam salesOrderInvoiceVoid(
        magento.SalesOrderInvoiceVoidRequestParam salesOrderInvoiceVoidRequestParam143)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Void invoice
     * @param salesOrderInvoiceVoidRequestParam143
     */
    public void startsalesOrderInvoiceVoid(
        magento.SalesOrderInvoiceVoidRequestParam salesOrderInvoiceVoidRequestParam143,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create product attribute set based on another set
     * @param catalogProductAttributeSetCreateRequestParam145
     */
    public magento.CatalogProductAttributeSetCreateResponseParam catalogProductAttributeSetCreate(
        magento.CatalogProductAttributeSetCreateRequestParam catalogProductAttributeSetCreateRequestParam145)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create product attribute set based on another set
     * @param catalogProductAttributeSetCreateRequestParam145
     */
    public void startcatalogProductAttributeSetCreate(
        magento.CatalogProductAttributeSetCreateRequestParam catalogProductAttributeSetCreateRequestParam145,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create new category and return its id.
     * @param catalogCategoryCreateRequestParam147
     */
    public magento.CatalogCategoryCreateResponseParam catalogCategoryCreate(
        magento.CatalogCategoryCreateRequestParam catalogCategoryCreateRequestParam147)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create new category and return its id.
     * @param catalogCategoryCreateRequestParam147
     */
    public void startcatalogCategoryCreate(
        magento.CatalogCategoryCreateRequestParam catalogCategoryCreateRequestParam147,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product image data
     * @param catalogProductAttributeMediaInfoRequestParam149
     */
    public magento.CatalogProductAttributeMediaInfoResponseParam catalogProductAttributeMediaInfo(
        magento.CatalogProductAttributeMediaInfoRequestParam catalogProductAttributeMediaInfoRequestParam149)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product image data
     * @param catalogProductAttributeMediaInfoRequestParam149
     */
    public void startcatalogProductAttributeMediaInfo(
        magento.CatalogProductAttributeMediaInfoRequestParam catalogProductAttributeMediaInfoRequestParam149,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update customer data
     * @param customerCustomerUpdateRequestParam151
     */
    public magento.CustomerCustomerUpdateResponseParam customerCustomerUpdate(
        magento.CustomerCustomerUpdateRequestParam customerCustomerUpdateRequestParam151)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update customer data
     * @param customerCustomerUpdateRequestParam151
     */
    public void startcustomerCustomerUpdate(
        magento.CustomerCustomerUpdateRequestParam customerCustomerUpdateRequestParam151,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Info about current Magento installation
     * @param magentoInfoRequestParam153
     */
    public magento.MagentoInfoResponseParam magentoInfo(
        magento.MagentoInfoRequestParam magentoInfoRequestParam153)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Info about current Magento installation
     * @param magentoInfoRequestParam153
     */
    public void startmagentoInfo(
        magento.MagentoInfoRequestParam magentoInfoRequestParam153,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create gift card account
     * @param giftcardAccountCreateRequestParam155
     */
    public magento.GiftcardAccountCreateResponseParam giftcardAccountCreate(
        magento.GiftcardAccountCreateRequestParam giftcardAccountCreateRequestParam155)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create gift card account
     * @param giftcardAccountCreateRequestParam155
     */
    public void startgiftcardAccountCreate(
        magento.GiftcardAccountCreateRequestParam giftcardAccountCreateRequestParam155,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create shopping cart
     * @param shoppingCartCreateRequestParam157
     */
    public magento.ShoppingCartCreateResponseParam shoppingCartCreate(
        magento.ShoppingCartCreateRequestParam shoppingCartCreateRequestParam157)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create shopping cart
     * @param shoppingCartCreateRequestParam157
     */
    public void startshoppingCartCreate(
        magento.ShoppingCartCreateRequestParam shoppingCartCreateRequestParam157,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set a gift message to the cart
     * @param giftMessageForQuoteRequestParam159
     */
    public magento.GiftMessageForQuoteResponseParam giftMessageSetForQuote(
        magento.GiftMessageForQuoteRequestParam giftMessageForQuoteRequestParam159)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set a gift message to the cart
     * @param giftMessageForQuoteRequestParam159
     */
    public void startgiftMessageSetForQuote(
        magento.GiftMessageForQuoteRequestParam giftMessageForQuoteRequestParam159,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Send shipment info
     * @param salesOrderShipmentSendInfoRequestParam161
     */
    public magento.SalesOrderShipmentSendInfoResponseParam salesOrderShipmentSendInfo(
        magento.SalesOrderShipmentSendInfoRequestParam salesOrderShipmentSendInfoRequestParam161)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Send shipment info
     * @param salesOrderShipmentSendInfoRequestParam161
     */
    public void startsalesOrderShipmentSendInfo(
        magento.SalesOrderShipmentSendInfoRequestParam salesOrderShipmentSendInfoRequestParam161,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add attribute into attribute set
     * @param catalogProductAttributeSetAttributeAddRequestParam163
     */
    public magento.CatalogProductAttributeSetAttributeAddResponseParam catalogProductAttributeSetAttributeAdd(
        magento.CatalogProductAttributeSetAttributeAddRequestParam catalogProductAttributeSetAttributeAddRequestParam163)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add attribute into attribute set
     * @param catalogProductAttributeSetAttributeAddRequestParam163
     */
    public void startcatalogProductAttributeSetAttributeAdd(
        magento.CatalogProductAttributeSetAttributeAddRequestParam catalogProductAttributeSetAttributeAddRequestParam163,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove product(s) from shopping cart
     * @param shoppingCartProductRemoveRequestParam165
     */
    public magento.ShoppingCartProductRemoveResponseParam shoppingCartProductRemove(
        magento.ShoppingCartProductRemoveRequestParam shoppingCartProductRemoveRequestParam165)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove product(s) from shopping cart
     * @param shoppingCartProductRemoveRequestParam165
     */
    public void startshoppingCartProductRemove(
        magento.ShoppingCartProductRemoveRequestParam shoppingCartProductRemoveRequestParam165,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Login user and retrive session id
     * @param loginParam167
     */
    public magento.LoginResponseParam login(magento.LoginParam loginParam167)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Login user and retrive session id
     * @param loginParam167
     */
    public void startlogin(magento.LoginParam loginParam167,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add new custom option into product
     * @param catalogProductCustomOptionAddRequestParam169
     */
    public magento.CatalogProductCustomOptionAddResponseParam catalogProductCustomOptionAdd(
        magento.CatalogProductCustomOptionAddRequestParam catalogProductCustomOptionAddRequestParam169)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add new custom option into product
     * @param catalogProductCustomOptionAddRequestParam169
     */
    public void startcatalogProductCustomOptionAdd(
        magento.CatalogProductCustomOptionAddRequestParam catalogProductCustomOptionAddRequestParam169,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve attribute options
     * @param catalogCategoryAttributeOptionsRequestParam171
     */
    public magento.CatalogCategoryAttributeOptionsResponseParam catalogCategoryAttributeOptions(
        magento.CatalogCategoryAttributeOptionsRequestParam catalogCategoryAttributeOptionsRequestParam171)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve attribute options
     * @param catalogCategoryAttributeOptionsRequestParam171
     */
    public void startcatalogCategoryAttributeOptions(
        magento.CatalogCategoryAttributeOptionsRequestParam catalogCategoryAttributeOptionsRequestParam171,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve creditmemo information
     * @param salesOrderCreditmemoInfoRequestParam173
     */
    public magento.SalesOrderCreditmemoInfoResponseParam salesOrderCreditmemoInfo(
        magento.SalesOrderCreditmemoInfoRequestParam salesOrderCreditmemoInfoRequestParam173)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve creditmemo information
     * @param salesOrderCreditmemoInfoRequestParam173
     */
    public void startsalesOrderCreditmemoInfo(
        magento.SalesOrderCreditmemoInfoRequestParam salesOrderCreditmemoInfoRequestParam173,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customer data
     * @param customerCustomerInfoRequestParam175
     */
    public magento.CustomerCustomerInfoResponseParam customerCustomerInfo(
        magento.CustomerCustomerInfoRequestParam customerCustomerInfoRequestParam175)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customer data
     * @param customerCustomerInfoRequestParam175
     */
    public void startcustomerCustomerInfo(
        magento.CustomerCustomerInfoRequestParam customerCustomerInfoRequestParam175,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create new creditmemo for order
     * @param salesOrderCreditmemoCreateRequestParam177
     */
    public magento.SalesOrderCreditmemoCreateResponseParam salesOrderCreditmemoCreate(
        magento.SalesOrderCreditmemoCreateRequestParam salesOrderCreditmemoCreateRequestParam177)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create new creditmemo for order
     * @param salesOrderCreditmemoCreateRequestParam177
     */
    public void startsalesOrderCreditmemoCreate(
        magento.SalesOrderCreditmemoCreateRequestParam salesOrderCreditmemoCreateRequestParam177,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Cancel order
     * @param salesOrderCancelRequestParam179
     */
    public magento.SalesOrderCancelResponseParam salesOrderCancel(
        magento.SalesOrderCancelRequestParam salesOrderCancelRequestParam179)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Cancel order
     * @param salesOrderCancelRequestParam179
     */
    public void startsalesOrderCancel(
        magento.SalesOrderCancelRequestParam salesOrderCancelRequestParam179,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove coupon code from shopping cart
     * @param shoppingCartCouponRemoveRequestParam181
     */
    public magento.ShoppingCartCouponRemoveResponseParam shoppingCartCouponRemove(
        magento.ShoppingCartCouponRemoveRequestParam shoppingCartCouponRemoveRequestParam181)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove coupon code from shopping cart
     * @param shoppingCartCouponRemoveRequestParam181
     */
    public void startshoppingCartCouponRemove(
        magento.ShoppingCartCouponRemoveRequestParam shoppingCartCouponRemoveRequestParam181,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set shipping method
     * @param shoppingCartShippingMethodRequestParam183
     */
    public magento.ShoppingCartShippingMethodResponseParam shoppingCartShippingMethod(
        magento.ShoppingCartShippingMethodRequestParam shoppingCartShippingMethodRequestParam183)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set shipping method
     * @param shoppingCartShippingMethodRequestParam183
     */
    public void startshoppingCartShippingMethod(
        magento.ShoppingCartShippingMethodRequestParam shoppingCartShippingMethodRequestParam183,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove amount from shopping cart (quote) and increase customer store credit
     * @param shoppingCartCustomerbalanceRemoveAmountRequestParam185
     */
    public magento.ShoppingCartCustomerbalanceRemoveAmountResponseParam shoppingCartCustomerbalanceRemoveAmount(
        magento.ShoppingCartCustomerbalanceRemoveAmountRequestParam shoppingCartCustomerbalanceRemoveAmountRequestParam185)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove amount from shopping cart (quote) and increase customer store credit
     * @param shoppingCartCustomerbalanceRemoveAmountRequestParam185
     */
    public void startshoppingCartCustomerbalanceRemoveAmount(
        magento.ShoppingCartCustomerbalanceRemoveAmountRequestParam shoppingCartCustomerbalanceRemoveAmountRequestParam185,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add new tracking number
     * @param salesOrderShipmentAddTrackRequestParam187
     */
    public magento.SalesOrderShipmentAddTrackResponseParam salesOrderShipmentAddTrack(
        magento.SalesOrderShipmentAddTrackRequestParam salesOrderShipmentAddTrackRequestParam187)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add new tracking number
     * @param salesOrderShipmentAddTrackRequestParam187
     */
    public void startsalesOrderShipmentAddTrack(
        magento.SalesOrderShipmentAddTrackRequestParam salesOrderShipmentAddTrackRequestParam187,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product tag
     * @param catalogProductTagUpdateRequestParam189
     */
    public magento.CatalogProductTagUpdateResponseParam catalogProductTagUpdate(
        magento.CatalogProductTagUpdateRequestParam catalogProductTagUpdateRequestParam189)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product tag
     * @param catalogProductTagUpdateRequestParam189
     */
    public void startcatalogProductTagUpdate(
        magento.CatalogProductTagUpdateRequestParam catalogProductTagUpdateRequestParam189,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add links to downloadable product
     * @param catalogProductDownloadableLinkAddRequestParam191
     */
    public magento.CatalogProductDownloadableLinkAddResponseParam catalogProductDownloadableLinkAdd(
        magento.CatalogProductDownloadableLinkAddRequestParam catalogProductDownloadableLinkAddRequestParam191)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add links to downloadable product
     * @param catalogProductDownloadableLinkAddRequestParam191
     */
    public void startcatalogProductDownloadableLinkAdd(
        magento.CatalogProductDownloadableLinkAddRequestParam catalogProductDownloadableLinkAddRequestParam191,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * List of resource faults
     * @param resourceFaultsParam193
     */
    public magento.ResourceFaultsResponseParam resourceFaults(
        magento.ResourceFaultsParam resourceFaultsParam193)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * List of resource faults
     * @param resourceFaultsParam193
     */
    public void startresourceFaults(
        magento.ResourceFaultsParam resourceFaultsParam193,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customer addresses
     * @param customerAddressListRequestParam195
     */
    public magento.CustomerAddressListResponseParam customerAddressList(
        magento.CustomerAddressListRequestParam customerAddressListRequestParam195)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customer addresses
     * @param customerAddressListRequestParam195
     */
    public void startcustomerAddressList(
        magento.CustomerAddressListRequestParam customerAddressListRequestParam195,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product stock data
     * @param catalogInventoryStockItemUpdateRequestParam197
     */
    public magento.CatalogInventoryStockItemUpdateResponseParam catalogInventoryStockItemUpdate(
        magento.CatalogInventoryStockItemUpdateRequestParam catalogInventoryStockItemUpdateRequestParam197)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product stock data
     * @param catalogInventoryStockItemUpdateRequestParam197
     */
    public void startcatalogInventoryStockItemUpdate(
        magento.CatalogInventoryStockItemUpdateRequestParam catalogInventoryStockItemUpdateRequestParam197,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get list of available payment methods
     * @param shoppingCartPaymentListRequestParam199
     */
    public magento.ShoppingCartPaymentListResponseParam shoppingCartPaymentList(
        magento.ShoppingCartPaymentListRequestParam shoppingCartPaymentListRequestParam199)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get list of available payment methods
     * @param shoppingCartPaymentListRequestParam199
     */
    public void startshoppingCartPaymentList(
        magento.ShoppingCartPaymentListRequestParam shoppingCartPaymentListRequestParam199,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add new comment to creditmemo
     * @param salesOrderCreditmemoAddCommentRequestParam201
     */
    public magento.SalesOrderCreditmemoAddCommentResponseParam salesOrderCreditmemoAddComment(
        magento.SalesOrderCreditmemoAddCommentRequestParam salesOrderCreditmemoAddCommentRequestParam201)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add new comment to creditmemo
     * @param salesOrderCreditmemoAddCommentRequestParam201
     */
    public void startsalesOrderCreditmemoAddComment(
        magento.SalesOrderCreditmemoAddCommentRequestParam salesOrderCreditmemoAddCommentRequestParam201,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove product tag
     * @param catalogProductTagRemoveRequestParam203
     */
    public magento.CatalogProductTagRemoveResponseParam catalogProductTagRemove(
        magento.CatalogProductTagRemoveRequestParam catalogProductTagRemoveRequestParam203)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove product tag
     * @param catalogProductTagRemoveRequestParam203
     */
    public void startcatalogProductTagRemove(
        magento.CatalogProductTagRemoveRequestParam catalogProductTagRemoveRequestParam203,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve shipment information
     * @param salesOrderShipmentInfoRequestParam205
     */
    public magento.SalesOrderShipmentInfoResponseParam salesOrderShipmentInfo(
        magento.SalesOrderShipmentInfoRequestParam salesOrderShipmentInfoRequestParam205)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve shipment information
     * @param salesOrderShipmentInfoRequestParam205
     */
    public void startsalesOrderShipmentInfo(
        magento.SalesOrderShipmentInfoRequestParam salesOrderShipmentInfoRequestParam205,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set custom product price(s)
     * @param shoppingCartProductCustomPriceRequestParam207
     */
    public magento.ShoppingCartProductCustomPriceResponseParam shoppingCartProductSetCustomPrice(
        magento.ShoppingCartProductCustomPriceRequestParam shoppingCartProductCustomPriceRequestParam207)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set custom product price(s)
     * @param shoppingCartProductCustomPriceRequestParam207
     */
    public void startshoppingCartProductSetCustomPrice(
        magento.ShoppingCartProductCustomPriceRequestParam shoppingCartProductCustomPriceRequestParam207,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Redeem gift card amount
     * @param shoppingCartGiftcardRemoveRequestParam209
     */
    public magento.ShoppingCartGiftcardRemoveResponseParam shoppingCartGiftcardRemove(
        magento.ShoppingCartGiftcardRemoveRequestParam shoppingCartGiftcardRemoveRequestParam209)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Redeem gift card amount
     * @param shoppingCartGiftcardRemoveRequestParam209
     */
    public void startshoppingCartGiftcardRemove(
        magento.ShoppingCartGiftcardRemoveRequestParam shoppingCartGiftcardRemoveRequestParam209,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Delete category
     * @param catalogCategoryDeleteRequestParam211
     */
    public magento.CatalogCategoryDeleteResponseParam catalogCategoryDelete(
        magento.CatalogCategoryDeleteRequestParam catalogCategoryDeleteRequestParam211)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Delete category
     * @param catalogCategoryDeleteRequestParam211
     */
    public void startcatalogCategoryDelete(
        magento.CatalogCategoryDeleteRequestParam catalogCategoryDeleteRequestParam211,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve attribute options
     * @param catalogProductAttributeOptionsRequestParam213
     */
    public magento.CatalogProductAttributeOptionsResponseParam catalogProductAttributeOptions(
        magento.CatalogProductAttributeOptionsRequestParam catalogProductAttributeOptionsRequestParam213)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve attribute options
     * @param catalogProductAttributeOptionsRequestParam213
     */
    public void startcatalogProductAttributeOptions(
        magento.CatalogProductAttributeOptionsRequestParam catalogProductAttributeOptionsRequestParam213,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Delete customer address
     * @param customerAddressDeleteRequestParam215
     */
    public magento.CustomerAddressDeleteResponseParam customerAddressDelete(
        magento.CustomerAddressDeleteRequestParam customerAddressDeleteRequestParam215)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Delete customer address
     * @param customerAddressDeleteRequestParam215
     */
    public void startcustomerAddressDelete(
        magento.CustomerAddressDeleteRequestParam customerAddressDeleteRequestParam215,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customer store credit history information
     * @param enterpriseCustomerbalanceHistoryRequestParam217
     */
    public magento.EnterpriseCustomerbalanceHistoryResponseParam enterpriseCustomerbalanceHistory(
        magento.EnterpriseCustomerbalanceHistoryRequestParam enterpriseCustomerbalanceHistoryRequestParam217)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customer store credit history information
     * @param enterpriseCustomerbalanceHistoryRequestParam217
     */
    public void startenterpriseCustomerbalanceHistory(
        magento.EnterpriseCustomerbalanceHistoryRequestParam enterpriseCustomerbalanceHistoryRequestParam217,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add coupon code for shopping cart
     * @param shoppingCartCouponAddRequestParam219
     */
    public magento.ShoppingCartCouponAddResponseParam shoppingCartCouponAdd(
        magento.ShoppingCartCouponAddRequestParam shoppingCartCouponAddRequestParam219)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add coupon code for shopping cart
     * @param shoppingCartCouponAddRequestParam219
     */
    public void startshoppingCartCouponAdd(
        magento.ShoppingCartCouponAddRequestParam shoppingCartCouponAddRequestParam219,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * List of countries
     * @param directoryCountryListRequestParam221
     */
    public magento.DirectoryCountryListResponseParam directoryCountryList(
        magento.DirectoryCountryListRequestParam directoryCountryListRequestParam221)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * List of countries
     * @param directoryCountryListRequestParam221
     */
    public void startdirectoryCountryList(
        magento.DirectoryCountryListRequestParam directoryCountryListRequestParam221,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set_Get current store view
     * @param catalogCategoryCurrentStoreRequestParam223
     */
    public magento.CatalogCategoryCurrentStoreResponseParam catalogCategoryCurrentStore(
        magento.CatalogCategoryCurrentStoreRequestParam catalogCategoryCurrentStoreRequestParam223)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set_Get current store view
     * @param catalogCategoryCurrentStoreRequestParam223
     */
    public void startcatalogCategoryCurrentStore(
        magento.CatalogCategoryCurrentStoreRequestParam catalogCategoryCurrentStoreRequestParam223,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update assigned product
     * @param catalogCategoryUpdateProductRequestParam225
     */
    public magento.CatalogCategoryUpdateProductResponseParam catalogCategoryUpdateProduct(
        magento.CatalogCategoryUpdateProductRequestParam catalogCategoryUpdateProductRequestParam225)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update assigned product
     * @param catalogCategoryUpdateProductRequestParam225
     */
    public void startcatalogCategoryUpdateProduct(
        magento.CatalogCategoryUpdateProductRequestParam catalogCategoryUpdateProductRequestParam225,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update attribute
     * @param catalogProductAttributeUpdateRequestParam227
     */
    public magento.CatalogProductAttributeUpdateResponseParam catalogProductAttributeUpdate(
        magento.CatalogProductAttributeUpdateRequestParam catalogProductAttributeUpdateRequestParam227)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update attribute
     * @param catalogProductAttributeUpdateRequestParam227
     */
    public void startcatalogProductAttributeUpdate(
        magento.CatalogProductAttributeUpdateRequestParam catalogProductAttributeUpdateRequestParam227,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * List of global faults
     * @param globalFaultsParam229
     */
    public magento.GlobalFaultsResponseParam globalFaults(
        magento.GlobalFaultsParam globalFaultsParam229)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * List of global faults
     * @param globalFaultsParam229
     */
    public void startglobalFaults(
        magento.GlobalFaultsParam globalFaultsParam229,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create an order from shopping cart
     * @param shoppingCartOrderRequestParam231
     */
    public magento.ShoppingCartOrderResponseParam shoppingCartOrder(
        magento.ShoppingCartOrderRequestParam shoppingCartOrderRequestParam231)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create an order from shopping cart
     * @param shoppingCartOrderRequestParam231
     */
    public void startshoppingCartOrder(
        magento.ShoppingCartOrderRequestParam shoppingCartOrderRequestParam231,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Delete customer
     * @param customerCustomerDeleteRequestParam233
     */
    public magento.CustomerCustomerDeleteResponseParam customerCustomerDelete(
        magento.CustomerCustomerDeleteRequestParam customerCustomerDeleteRequestParam233)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Delete customer
     * @param customerCustomerDeleteRequestParam233
     */
    public void startcustomerCustomerDelete(
        magento.CustomerCustomerDeleteRequestParam customerCustomerDeleteRequestParam233,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve hierarchical tree of categories.
     * @param catalogCategoryTreeRequestParam235
     */
    public magento.CatalogCategoryTreeResponseParam catalogCategoryTree(
        magento.CatalogCategoryTreeRequestParam catalogCategoryTreeRequestParam235)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve hierarchical tree of categories.
     * @param catalogCategoryTreeRequestParam235
     */
    public void startcatalogCategoryTree(
        magento.CatalogCategoryTreeRequestParam catalogCategoryTreeRequestParam235,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create group within existing attribute set
     * @param catalogProductAttributeSetGroupAddRequestParam237
     */
    public magento.CatalogProductAttributeSetGroupAddResponseParam catalogProductAttributeSetGroupAdd(
        magento.CatalogProductAttributeSetGroupAddRequestParam catalogProductAttributeSetGroupAddRequestParam237)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create group within existing attribute set
     * @param catalogProductAttributeSetGroupAddRequestParam237
     */
    public void startcatalogProductAttributeSetGroupAdd(
        magento.CatalogProductAttributeSetGroupAddRequestParam catalogProductAttributeSetGroupAddRequestParam237,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove product image
     * @param catalogProductAttributeMediaRemoveRequestParam239
     */
    public magento.CatalogProductAttributeMediaRemoveResponseParam catalogProductAttributeMediaRemove(
        magento.CatalogProductAttributeMediaRemoveRequestParam catalogProductAttributeMediaRemoveRequestParam239)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove product image
     * @param catalogProductAttributeMediaRemoveRequestParam239
     */
    public void startcatalogProductAttributeMediaRemove(
        magento.CatalogProductAttributeMediaRemoveRequestParam catalogProductAttributeMediaRemoveRequestParam239,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Unhold order
     * @param salesOrderUnholdRequestParam241
     */
    public magento.SalesOrderUnholdResponseParam salesOrderUnhold(
        magento.SalesOrderUnholdRequestParam salesOrderUnholdRequestParam241)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Unhold order
     * @param salesOrderUnholdRequestParam241
     */
    public void startsalesOrderUnhold(
        magento.SalesOrderUnholdRequestParam salesOrderUnholdRequestParam241,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Setting a gift messages to the quote item
     * @param giftMessageForQuoteItemRequestParam243
     */
    public magento.GiftMessageForQuoteItemResponseParam giftMessageSetForQuoteItem(
        magento.GiftMessageForQuoteItemRequestParam giftMessageForQuoteItemRequestParam243)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Setting a gift messages to the quote item
     * @param giftMessageForQuoteItemRequestParam243
     */
    public void startgiftMessageSetForQuoteItem(
        magento.GiftMessageForQuoteItemRequestParam giftMessageForQuoteItemRequestParam243,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Delete attribute
     * @param catalogProductAttributeRemoveRequestParam245
     */
    public magento.CatalogProductAttributeRemoveResponseParam catalogProductAttributeRemove(
        magento.CatalogProductAttributeRemoveRequestParam catalogProductAttributeRemoveRequestParam245)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Delete attribute
     * @param catalogProductAttributeRemoveRequestParam245
     */
    public void startcatalogProductAttributeRemove(
        magento.CatalogProductAttributeRemoveRequestParam catalogProductAttributeRemoveRequestParam245,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Call api functionality
     * @param callParam247
     */
    public magento.CallResponseParam call(magento.CallParam callParam247)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Call api functionality
     * @param callParam247
     */
    public void startcall(magento.CallParam callParam247,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product image types
     * @param catalogProductAttributeMediaTypesRequestParam249
     */
    public magento.CatalogProductAttributeMediaTypesResponseParam catalogProductAttributeMediaTypes(
        magento.CatalogProductAttributeMediaTypesRequestParam catalogProductAttributeMediaTypesRequestParam249)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product image types
     * @param catalogProductAttributeMediaTypesRequestParam249
     */
    public void startcatalogProductAttributeMediaTypes(
        magento.CatalogProductAttributeMediaTypesRequestParam catalogProductAttributeMediaTypesRequestParam249,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update product image
     * @param catalogProductAttributeMediaUpdateRequestParam251
     */
    public magento.CatalogProductAttributeMediaUpdateResponseParam catalogProductAttributeMediaUpdate(
        magento.CatalogProductAttributeMediaUpdateRequestParam catalogProductAttributeMediaUpdateRequestParam251)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update product image
     * @param catalogProductAttributeMediaUpdateRequestParam251
     */
    public void startcatalogProductAttributeMediaUpdate(
        magento.CatalogProductAttributeMediaUpdateRequestParam catalogProductAttributeMediaUpdateRequestParam251,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve stock data by product ids
     * @param catalogInventoryStockItemListRequestParam253
     */
    public magento.CatalogInventoryStockItemListResponseParam catalogInventoryStockItemList(
        magento.CatalogInventoryStockItemListRequestParam catalogInventoryStockItemListRequestParam253)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve stock data by product ids
     * @param catalogInventoryStockItemListRequestParam253
     */
    public void startcatalogInventoryStockItemList(
        magento.CatalogInventoryStockItemListRequestParam catalogInventoryStockItemListRequestParam253,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve customers
     * @param customerCustomerListRequestParam255
     */
    public magento.CustomerCustomerListResponseParam customerCustomerList(
        magento.CustomerCustomerListRequestParam customerCustomerListRequestParam255)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve customers
     * @param customerCustomerListRequestParam255
     */
    public void startcustomerCustomerList(
        magento.CustomerCustomerListRequestParam customerCustomerListRequestParam255,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Redeem gift card amount
     * @param giftcardCustomerRedeemRequestParam257
     */
    public magento.GiftcardCustomerRedeemResponseParam giftcardCustomerRedeem(
        magento.GiftcardCustomerRedeemRequestParam giftcardCustomerRedeemRequestParam257)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Redeem gift card amount
     * @param giftcardCustomerRedeemRequestParam257
     */
    public void startgiftcardCustomerRedeem(
        magento.GiftcardCustomerRedeemRequestParam giftcardCustomerRedeemRequestParam257,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove group from attribute set
     * @param catalogProductAttributeSetGroupRemoveRequestParam259
     */
    public magento.CatalogProductAttributeSetGroupRemoveResponseParam catalogProductAttributeSetGroupRemove(
        magento.CatalogProductAttributeSetGroupRemoveRequestParam catalogProductAttributeSetGroupRemoveRequestParam259)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove group from attribute set
     * @param catalogProductAttributeSetGroupRemoveRequestParam259
     */
    public void startcatalogProductAttributeSetGroupRemove(
        magento.CatalogProductAttributeSetGroupRemoveRequestParam catalogProductAttributeSetGroupRemoveRequestParam259,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove product link
     * @param catalogProductLinkRemoveRequestParam261
     */
    public magento.CatalogProductLinkRemoveResponseParam catalogProductLinkRemove(
        magento.CatalogProductLinkRemoveRequestParam catalogProductLinkRemoveRequestParam261)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove product link
     * @param catalogProductLinkRemoveRequestParam261
     */
    public void startcatalogProductLinkRemove(
        magento.CatalogProductLinkRemoveRequestParam catalogProductLinkRemoveRequestParam261,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve gift card data
     * @param giftcardCustomerInfoRequestParam263
     */
    public magento.GiftcardCustomerInfoResponseParam giftcardCustomerInfo(
        magento.GiftcardCustomerInfoRequestParam giftcardCustomerInfoRequestParam263)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve gift card data
     * @param giftcardCustomerInfoRequestParam263
     */
    public void startgiftcardCustomerInfo(
        magento.GiftcardCustomerInfoRequestParam giftcardCustomerInfoRequestParam263,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve products list by filters
     * @param catalogProductListRequestParam265
     */
    public magento.CatalogProductListResponseParam catalogProductList(
        magento.CatalogProductListRequestParam catalogProductListRequestParam265)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve products list by filters
     * @param catalogProductListRequestParam265
     */
    public void startcatalogProductList(
        magento.CatalogProductListRequestParam catalogProductListRequestParam265,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add new custom option values
     * @param catalogProductCustomOptionValueAddRequestParam267
     */
    public magento.CatalogProductCustomOptionValueAddResponseParam catalogProductCustomOptionValueAdd(
        magento.CatalogProductCustomOptionValueAddRequestParam catalogProductCustomOptionValueAddRequestParam267)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add new custom option values
     * @param catalogProductCustomOptionValueAddRequestParam267
     */
    public void startcatalogProductCustomOptionValueAdd(
        magento.CatalogProductCustomOptionValueAddRequestParam catalogProductCustomOptionValueAddRequestParam267,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of links and samples for downloadable product
     * @param catalogProductDownloadableLinkListRequestParam269
     */
    public magento.CatalogProductDownloadableLinkListResponseParam catalogProductDownloadableLinkList(
        magento.CatalogProductDownloadableLinkListRequestParam catalogProductDownloadableLinkListRequestParam269)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of links and samples for downloadable product
     * @param catalogProductDownloadableLinkListRequestParam269
     */
    public void startcatalogProductDownloadableLinkList(
        magento.CatalogProductDownloadableLinkListRequestParam catalogProductDownloadableLinkListRequestParam269,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Delete product
     * @param catalogProductDeleteRequestParam271
     */
    public magento.CatalogProductDeleteResponseParam catalogProductDelete(
        magento.CatalogProductDeleteRequestParam catalogProductDeleteRequestParam271)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Delete product
     * @param catalogProductDeleteRequestParam271
     */
    public void startcatalogProductDelete(
        magento.CatalogProductDeleteRequestParam catalogProductDeleteRequestParam271,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Hold order
     * @param salesOrderHoldRequestParam273
     */
    public magento.SalesOrderHoldResponseParam salesOrderHold(
        magento.SalesOrderHoldRequestParam salesOrderHoldRequestParam273)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Hold order
     * @param salesOrderHoldRequestParam273
     */
    public void startsalesOrderHold(
        magento.SalesOrderHoldRequestParam salesOrderHoldRequestParam273,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Redeem gift card amount
     * @param shoppingCartGiftcardListRequestParam275
     */
    public magento.ShoppingCartGiftcardListResponseParam shoppingCartGiftcardList(
        magento.ShoppingCartGiftcardListRequestParam shoppingCartGiftcardListRequestParam275)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Redeem gift card amount
     * @param shoppingCartGiftcardListRequestParam275
     */
    public void startshoppingCartGiftcardList(
        magento.ShoppingCartGiftcardListRequestParam shoppingCartGiftcardListRequestParam275,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update customer address data
     * @param customerAddressUpdateRequestParam277
     */
    public magento.CustomerAddressUpdateResponseParam customerAddressUpdate(
        magento.CustomerAddressUpdateRequestParam customerAddressUpdateRequestParam277)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update customer address data
     * @param customerAddressUpdateRequestParam277
     */
    public void startcustomerAddressUpdate(
        magento.CustomerAddressUpdateRequestParam customerAddressUpdateRequestParam277,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Setting a gift messages to the quote items by products
     * @param giftMessageForQuoteProductRequestParam279
     */
    public magento.GiftMessageForQuoteProductResponseParam giftMessageSetForQuoteProduct(
        magento.GiftMessageForQuoteProductRequestParam giftMessageForQuoteProductRequestParam279)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Setting a gift messages to the quote items by products
     * @param giftMessageForQuoteProductRequestParam279
     */
    public void startgiftMessageSetForQuoteProduct(
        magento.GiftMessageForQuoteProductRequestParam giftMessageForQuoteProductRequestParam279,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve custom option values list
     * @param catalogProductCustomOptionValueListRequestParam281
     */
    public magento.CatalogProductCustomOptionValueListResponseParam catalogProductCustomOptionValueList(
        magento.CatalogProductCustomOptionValueListRequestParam catalogProductCustomOptionValueListRequestParam281)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve custom option values list
     * @param catalogProductCustomOptionValueListRequestParam281
     */
    public void startcatalogProductCustomOptionValueList(
        magento.CatalogProductCustomOptionValueListRequestParam catalogProductCustomOptionValueListRequestParam281,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add new comment to shipment
     * @param salesOrderInvoiceAddCommentRequestParam283
     */
    public magento.SalesOrderInvoiceAddCommentResponseParam salesOrderInvoiceAddComment(
        magento.SalesOrderInvoiceAddCommentRequestParam salesOrderInvoiceAddCommentRequestParam283)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add new comment to shipment
     * @param salesOrderInvoiceAddCommentRequestParam283
     */
    public void startsalesOrderInvoiceAddComment(
        magento.SalesOrderInvoiceAddCommentRequestParam salesOrderInvoiceAddCommentRequestParam283,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove product assignment from category
     * @param catalogCategoryRemoveProductRequestParam285
     */
    public magento.CatalogCategoryRemoveProductResponseParam catalogCategoryRemoveProduct(
        magento.CatalogCategoryRemoveProductRequestParam catalogCategoryRemoveProductRequestParam285)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove product assignment from category
     * @param catalogCategoryRemoveProductRequestParam285
     */
    public void startcatalogCategoryRemoveProduct(
        magento.CatalogCategoryRemoveProductRequestParam catalogCategoryRemoveProductRequestParam285,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Update custom option value
     * @param catalogProductCustomOptionValueUpdateRequestParam287
     */
    public magento.CatalogProductCustomOptionValueUpdateResponseParam catalogProductCustomOptionValueUpdate(
        magento.CatalogProductCustomOptionValueUpdateRequestParam catalogProductCustomOptionValueUpdateRequestParam287)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Update custom option value
     * @param catalogProductCustomOptionValueUpdateRequestParam287
     */
    public void startcatalogProductCustomOptionValueUpdate(
        magento.CatalogProductCustomOptionValueUpdateRequestParam catalogProductCustomOptionValueUpdateRequestParam287,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product link types
     * @param catalogProductLinkTypesRequestParam289
     */
    public magento.CatalogProductLinkTypesResponseParam catalogProductLinkTypes(
        magento.CatalogProductLinkTypesRequestParam catalogProductLinkTypesRequestParam289)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product link types
     * @param catalogProductLinkTypesRequestParam289
     */
    public void startcatalogProductLinkTypes(
        magento.CatalogProductLinkTypesRequestParam catalogProductLinkTypesRequestParam289,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve custom option value info
     * @param catalogProductCustomOptionValueInfoRequestParam291
     */
    public magento.CatalogProductCustomOptionValueInfoResponseParam catalogProductCustomOptionValueInfo(
        magento.CatalogProductCustomOptionValueInfoRequestParam catalogProductCustomOptionValueInfoRequestParam291)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve custom option value info
     * @param catalogProductCustomOptionValueInfoRequestParam291
     */
    public void startcatalogProductCustomOptionValueInfo(
        magento.CatalogProductCustomOptionValueInfoRequestParam catalogProductCustomOptionValueInfoRequestParam291,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Cancel invoice
     * @param salesOrderInvoiceCancelRequestParam293
     */
    public magento.SalesOrderInvoiceCancelResponseParam salesOrderInvoiceCancel(
        magento.SalesOrderInvoiceCancelRequestParam salesOrderInvoiceCancelRequestParam293)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Cancel invoice
     * @param salesOrderInvoiceCancelRequestParam293
     */
    public void startsalesOrderInvoiceCancel(
        magento.SalesOrderInvoiceCancelRequestParam salesOrderInvoiceCancelRequestParam293,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create customer
     * @param customerCustomerCreateRequestParam295
     */
    public magento.CustomerCustomerCreateResponseParam customerCustomerCreate(
        magento.CustomerCustomerCreateRequestParam customerCustomerCreateRequestParam295)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create customer
     * @param customerCustomerCreateRequestParam295
     */
    public void startcustomerCustomerCreate(
        magento.CustomerCustomerCreateRequestParam customerCustomerCreateRequestParam295,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of assigned products
     * @param catalogCategoryAssignedProductsRequestParam297
     */
    public magento.CatalogCategoryAssignedProductsResponseParam catalogCategoryAssignedProducts(
        magento.CatalogCategoryAssignedProductsRequestParam catalogCategoryAssignedProductsRequestParam297)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of assigned products
     * @param catalogCategoryAssignedProductsRequestParam297
     */
    public void startcatalogCategoryAssignedProducts(
        magento.CatalogCategoryAssignedProductsRequestParam catalogCategoryAssignedProductsRequestParam297,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get terms and conditions
     * @param shoppingCartLicenseRequestParam299
     */
    public magento.ShoppingCartLicenseResponseParam shoppingCartLicense(
        magento.ShoppingCartLicenseRequestParam shoppingCartLicenseRequestParam299)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get terms and conditions
     * @param shoppingCartLicenseRequestParam299
     */
    public void startshoppingCartLicense(
        magento.ShoppingCartLicenseRequestParam shoppingCartLicenseRequestParam299,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add tag(s) to product
     * @param catalogProductTagAddRequestParam301
     */
    public magento.CatalogProductTagAddResponseParam catalogProductTagAdd(
        magento.CatalogProductTagAddRequestParam catalogProductTagAddRequestParam301)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add tag(s) to product
     * @param catalogProductTagAddRequestParam301
     */
    public void startcatalogProductTagAdd(
        magento.CatalogProductTagAddRequestParam catalogProductTagAddRequestParam301,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Get total prices for shopping cart
     * @param shoppingCartTotalsRequestParam303
     */
    public magento.ShoppingCartTotalsResponseParam shoppingCartTotals(
        magento.ShoppingCartTotalsRequestParam shoppingCartTotalsRequestParam303)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Get total prices for shopping cart
     * @param shoppingCartTotalsRequestParam303
     */
    public void startshoppingCartTotals(
        magento.ShoppingCartTotalsRequestParam shoppingCartTotalsRequestParam303,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create customer address
     * @param customerAddressCreateRequestParam305
     */
    public magento.CustomerAddressCreateResponseParam customerAddressCreate(
        magento.CustomerAddressCreateRequestParam customerAddressCreateRequestParam305)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create customer address
     * @param customerAddressCreateRequestParam305
     */
    public void startcustomerAddressCreate(
        magento.CustomerAddressCreateRequestParam customerAddressCreateRequestParam305,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set/Get current store view
     * @param catalogProductCurrentStoreRequestParam307
     */
    public magento.CatalogProductCurrentStoreResponseParam catalogProductCurrentStore(
        magento.CatalogProductCurrentStoreRequestParam catalogProductCurrentStoreRequestParam307)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set/Get current store view
     * @param catalogProductCurrentStoreRequestParam307
     */
    public void startcatalogProductCurrentStore(
        magento.CatalogProductCurrentStoreRequestParam catalogProductCurrentStoreRequestParam307,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve product
     * @param catalogProductInfoRequestParam309
     */
    public magento.CatalogProductInfoResponseParam catalogProductInfo(
        magento.CatalogProductInfoRequestParam catalogProductInfoRequestParam309)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve product
     * @param catalogProductInfoRequestParam309
     */
    public void startcatalogProductInfo(
        magento.CatalogProductInfoRequestParam catalogProductInfoRequestParam309,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Set customer for shopping cart
     * @param shoppingCartCustomerSetRequestParam311
     */
    public magento.ShoppingCartCustomerSetResponseParam shoppingCartCustomerSet(
        magento.ShoppingCartCustomerSetRequestParam shoppingCartCustomerSetRequestParam311)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Set customer for shopping cart
     * @param shoppingCartCustomerSetRequestParam311
     */
    public void startshoppingCartCustomerSet(
        magento.ShoppingCartCustomerSetRequestParam shoppingCartCustomerSetRequestParam311,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Multiple calls of resource functionality
     * @param multiCallParam313
     */
    public magento.MultiCallResponseParam multiCall(
        magento.MultiCallParam multiCallParam313)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Multiple calls of resource functionality
     * @param multiCallParam313
     */
    public void startmultiCall(magento.MultiCallParam multiCallParam313,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove custom option
     * @param catalogProductCustomOptionRemoveRequestParam315
     */
    public magento.CatalogProductCustomOptionRemoveResponseParam catalogProductCustomOptionRemove(
        magento.CatalogProductCustomOptionRemoveRequestParam catalogProductCustomOptionRemoveRequestParam315)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove custom option
     * @param catalogProductCustomOptionRemoveRequestParam315
     */
    public void startcatalogProductCustomOptionRemove(
        magento.CatalogProductCustomOptionRemoveRequestParam catalogProductCustomOptionRemoveRequestParam315,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Rename existing group
     * @param catalogProductAttributeSetGroupRenameRequestParam317
     */
    public magento.CatalogProductAttributeSetGroupRenameResponseParam catalogProductAttributeSetGroupRename(
        magento.CatalogProductAttributeSetGroupRenameRequestParam catalogProductAttributeSetGroupRenameRequestParam317)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Rename existing group
     * @param catalogProductAttributeSetGroupRenameRequestParam317
     */
    public void startcatalogProductAttributeSetGroupRename(
        magento.CatalogProductAttributeSetGroupRenameRequestParam catalogProductAttributeSetGroupRenameRequestParam317,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Multi update product stock data
     * @param catalogInventoryStockItemMultiUpdateRequestParam319
     */
    public magento.CatalogInventoryStockItemMultiUpdateResponseParam catalogInventoryStockItemMultiUpdate(
        magento.CatalogInventoryStockItemMultiUpdateRequestParam catalogInventoryStockItemMultiUpdateRequestParam319)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Multi update product stock data
     * @param catalogInventoryStockItemMultiUpdateRequestParam319
     */
    public void startcatalogInventoryStockItemMultiUpdate(
        magento.CatalogInventoryStockItemMultiUpdateRequestParam catalogInventoryStockItemMultiUpdateRequestParam319,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Remove gift card account
     * @param giftcardAccountRemoveRequestParam321
     */
    public magento.GiftcardAccountRemoveResponseParam giftcardAccountRemove(
        magento.GiftcardAccountRemoveRequestParam giftcardAccountRemoveRequestParam321)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Remove gift card account
     * @param giftcardAccountRemoveRequestParam321
     */
    public void startgiftcardAccountRemove(
        magento.GiftcardAccountRemoveRequestParam giftcardAccountRemoveRequestParam321,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Add new comment to shipment
     * @param salesOrderShipmentAddCommentRequestParam323
     */
    public magento.SalesOrderShipmentAddCommentResponseParam salesOrderShipmentAddComment(
        magento.SalesOrderShipmentAddCommentRequestParam salesOrderShipmentAddCommentRequestParam323)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Add new comment to shipment
     * @param salesOrderShipmentAddCommentRequestParam323
     */
    public void startsalesOrderShipmentAddComment(
        magento.SalesOrderShipmentAddCommentRequestParam salesOrderShipmentAddCommentRequestParam323,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Create new shipment for order
     * @param salesOrderShipmentCreateRequestParam325
     */
    public magento.SalesOrderShipmentCreateResponseParam salesOrderShipmentCreate(
        magento.SalesOrderShipmentCreateRequestParam salesOrderShipmentCreateRequestParam325)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Create new shipment for order
     * @param salesOrderShipmentCreateRequestParam325
     */
    public void startsalesOrderShipmentCreate(
        magento.SalesOrderShipmentCreateRequestParam salesOrderShipmentCreateRequestParam325,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     * Retrieve list of creditmemos by filters
     * @param salesOrderCreditmemoListRequestParam327
     */
    public magento.SalesOrderCreditmemoListResponseParam salesOrderCreditmemoList(
        magento.SalesOrderCreditmemoListRequestParam salesOrderCreditmemoListRequestParam327)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     * Retrieve list of creditmemos by filters
     * @param salesOrderCreditmemoListRequestParam327
     */
    public void startsalesOrderCreditmemoList(
        magento.SalesOrderCreditmemoListRequestParam salesOrderCreditmemoListRequestParam327,
        final com.voyageone.common.magento.api.base.MagentoServiceCallbackHandler callback)
        throws java.rmi.RemoteException;

    //
}
