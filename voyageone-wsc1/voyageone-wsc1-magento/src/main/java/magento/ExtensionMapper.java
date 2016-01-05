/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static Object getTypeObject(
        String namespaceURI, String typeName,
        javax.xml.stream.XMLStreamReader reader) throws Exception {
        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeMediaTypeEntity".equals(typeName)) {
            return magento.CatalogProductAttributeMediaTypeEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftMessageEntity".equals(typeName)) {
            return magento.GiftMessageEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerCustomerEntity".equals(typeName)) {
            return magento.CustomerCustomerEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "directoryRegionEntityArray".equals(typeName)) {
            return magento.DirectoryRegionEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionInfoEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionInfoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerCustomerEntityToCreate".equals(typeName)) {
            return magento.CustomerCustomerEntityToCreate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartLicenseEntityArray".equals(typeName)) {
            return magento.ShoppingCartLicenseEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountEntity".equals(typeName)) {
            return magento.GiftcardAccountEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogAttributeEntityArray".equals(typeName)) {
            return magento.CatalogAttributeEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogCategoryEntityCreate".equals(typeName)) {
            return magento.CatalogCategoryEntityCreate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentEntity".equals(typeName)) {
            return magento.SalesOrderShipmentEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoEntityArray".equals(typeName)) {
            return magento.SalesOrderCreditmemoEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderItemEntity".equals(typeName)) {
            return magento.SalesOrderItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderInvoiceItemEntity".equals(typeName)) {
            return magento.SalesOrderInvoiceItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTagUpdateEntity".equals(typeName)) {
            return magento.CatalogProductTagUpdateEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartPaymentMethodEntity".equals(typeName)) {
            return magento.ShoppingCartPaymentMethodEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartTotalsEntityArray".equals(typeName)) {
            return magento.ShoppingCartTotalsEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoItemEntityArray".equals(typeName)) {
            return magento.SalesOrderCreditmemoItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "orderItemIdQtyArray".equals(typeName)) {
            return magento.OrderItemIdQtyArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "enterpriseGiftcardaccountListEntity".equals(typeName)) {
            return magento.EnterpriseGiftcardaccountListEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "ArrayOfExistsFaltures".equals(typeName)) {
            return magento.ArrayOfExistsFaltures.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogAssignedProductArray".equals(typeName)) {
            return magento.CatalogAssignedProductArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderStatusHistoryEntity".equals(typeName)) {
            return magento.SalesOrderStatusHistoryEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "storeEntity".equals(typeName)) {
            return magento.StoreEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "enterpriseCustomerbalanceHistoryItemEntityArray".equals(
                    typeName)) {
            return magento.EnterpriseCustomerbalanceHistoryItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) && "filters".equals(typeName)) {
            return magento.Filters.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentEntityArray".equals(typeName)) {
            return magento.SalesOrderShipmentEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartAddressEntity".equals(typeName)) {
            return magento.ShoppingCartAddressEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkAddSampleEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkAddSampleEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "ArrayOfCatalogCategoryEntitiesNoChildren".equals(typeName)) {
            return magento.ArrayOfCatalogCategoryEntitiesNoChildren.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTierPriceEntity".equals(typeName)) {
            return magento.CatalogProductTierPriceEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionAdditionalFieldsEntity".equals(
                    typeName)) {
            return magento.CatalogProductCustomOptionAdditionalFieldsEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartProductEntity".equals(typeName)) {
            return magento.ShoppingCartProductEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountEntityToUpdate".equals(typeName)) {
            return magento.GiftcardAccountEntityToUpdate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartProductResponseEntityArray".equals(typeName)) {
            return magento.ShoppingCartProductResponseEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeOptionEntityToAdd".equals(typeName)) {
            return magento.CatalogProductAttributeOptionEntityToAdd.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "ArrayOfCatalogCategoryEntities".equals(typeName)) {
            return magento.ArrayOfCatalogCategoryEntities.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "orderItemIdQty".equals(typeName)) {
            return magento.OrderItemIdQty.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartPaymentEntity".equals(typeName)) {
            return magento.ShoppingCartPaymentEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionValueAddArray".equals(typeName)) {
            return magento.CatalogProductCustomOptionValueAddArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionValueUpdateEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionValueUpdateEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "ArrayOfString".equals(typeName)) {
            return magento.ArrayOfString.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeEntityToCreate".equals(typeName)) {
            return magento.CatalogProductAttributeEntityToCreate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductSpecialPriceReturnEntity".equals(typeName)) {
            return magento.CatalogProductSpecialPriceReturnEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderPaymentEntity".equals(typeName)) {
            return magento.SalesOrderPaymentEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerAddressEntityItem".equals(typeName)) {
            return magento.CustomerAddressEntityItem.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTypeEntityArray".equals(typeName)) {
            return magento.CatalogProductTypeEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogCategoryInfo".equals(typeName)) {
            return magento.CatalogCategoryInfo.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTierPriceEntityArray".equals(typeName)) {
            return magento.CatalogProductTierPriceEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionToAdd".equals(typeName)) {
            return magento.CatalogProductCustomOptionToAdd.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeFrontendLabelArray".equals(typeName)) {
            return magento.CatalogProductAttributeFrontendLabelArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentCommentEntity".equals(typeName)) {
            return magento.SalesOrderShipmentCommentEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "ArrayOfApiMethods".equals(typeName)) {
            return magento.ArrayOfApiMethods.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "complexFilterArray".equals(typeName)) {
            return magento.ComplexFilterArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerCustomerEntityArray".equals(typeName)) {
            return magento.CustomerCustomerEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTagAddEntity".equals(typeName)) {
            return magento.CatalogProductTagAddEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionToUpdate".equals(typeName)) {
            return magento.CatalogProductCustomOptionToUpdate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeEntityToUpdate".equals(typeName)) {
            return magento.CatalogProductAttributeEntityToUpdate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTagInfoEntity".equals(typeName)) {
            return magento.CatalogProductTagInfoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerGroupEntityArray".equals(typeName)) {
            return magento.CustomerGroupEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartShippingMethodEntityArray".equals(typeName)) {
            return magento.ShoppingCartShippingMethodEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductImageEntity".equals(typeName)) {
            return magento.CatalogProductImageEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogInventoryStockItemUpdateEntity".equals(typeName)) {
            return magento.CatalogInventoryStockItemUpdateEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeSetEntityArray".equals(typeName)) {
            return magento.CatalogProductAttributeSetEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentCommentEntityArray".equals(typeName)) {
            return magento.SalesOrderShipmentCommentEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardCustomerEntity".equals(typeName)) {
            return magento.GiftcardCustomerEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerAddressEntityCreate".equals(typeName)) {
            return magento.CustomerAddressEntityCreate.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderListEntityArray".equals(typeName)) {
            return magento.SalesOrderListEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCreateEntity".equals(typeName)) {
            return magento.CatalogProductCreateEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "enterpriseCustomerbalanceHistoryItemEntity".equals(typeName)) {
            return magento.EnterpriseCustomerbalanceHistoryItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerAddressEntityArray".equals(typeName)) {
            return magento.CustomerAddressEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeSetEntity".equals(typeName)) {
            return magento.CatalogProductAttributeSetEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftMessageAssociativeProductsEntity".equals(typeName)) {
            return magento.GiftMessageAssociativeProductsEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentTrackEntity".equals(typeName)) {
            return magento.SalesOrderShipmentTrackEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartItemEntityArray".equals(typeName)) {
            return magento.ShoppingCartItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkSampleEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkSampleEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartCustomerEntity".equals(typeName)) {
            return magento.ShoppingCartCustomerEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogAttributeOptionEntity".equals(typeName)) {
            return magento.CatalogAttributeOptionEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "associativeArray".equals(typeName)) {
            return magento.AssociativeArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartLicenseEntity".equals(typeName)) {
            return magento.ShoppingCartLicenseEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionListArray".equals(typeName)) {
            return magento.CatalogProductCustomOptionListArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionValueListEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionValueListEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductEntityArray".equals(typeName)) {
            return magento.CatalogProductEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "customerGroupEntity".equals(typeName)) {
            return magento.CustomerGroupEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderInvoiceCommentEntity".equals(typeName)) {
            return magento.SalesOrderInvoiceCommentEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartInfoEntity".equals(typeName)) {
            return magento.ShoppingCartInfoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionAdditionalFieldsArray".equals(
                    typeName)) {
            return magento.CatalogProductCustomOptionAdditionalFieldsArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogAttributeOptionEntityArray".equals(typeName)) {
            return magento.CatalogAttributeOptionEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "ArrayOfApis".equals(typeName)) {
            return magento.ArrayOfApis.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductImageFileEntity".equals(typeName)) {
            return magento.CatalogProductImageFileEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "associativeEntity".equals(typeName)) {
            return magento.AssociativeEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "directoryCountryEntity".equals(typeName)) {
            return magento.DirectoryCountryEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeOptionLabelEntity".equals(typeName)) {
            return magento.CatalogProductAttributeOptionLabelEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "directoryCountryEntityArray".equals(typeName)) {
            return magento.DirectoryCountryEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderAddressEntity".equals(typeName)) {
            return magento.SalesOrderAddressEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogInventoryStockItemEntity".equals(typeName)) {
            return magento.CatalogInventoryStockItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductLinkAttributeEntityArray".equals(typeName)) {
            return magento.CatalogProductLinkAttributeEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "existsFaltureEntity".equals(typeName)) {
            return magento.ExistsFaltureEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeMediaTypeEntityArray".equals(typeName)) {
            return magento.CatalogProductAttributeMediaTypeEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTypeEntity".equals(typeName)) {
            return magento.CatalogProductTypeEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoCommentEntity".equals(typeName)) {
            return magento.SalesOrderCreditmemoCommentEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoCommentEntityArray".equals(typeName)) {
            return magento.SalesOrderCreditmemoCommentEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftMessageResponseArray".equals(typeName)) {
            return magento.GiftMessageResponseArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "apiMethodEntity".equals(typeName)) {
            return magento.ApiMethodEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkFileInfoEntityArray".equals(
                    typeName)) {
            return magento.CatalogProductDownloadableLinkFileInfoEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderStatusHistoryEntityArray".equals(typeName)) {
            return magento.SalesOrderStatusHistoryEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartPaymentMethodResponseEntity".equals(typeName)) {
            return magento.ShoppingCartPaymentMethodResponseEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogCategoryTree".equals(typeName)) {
            return magento.CatalogCategoryTree.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "directoryRegionEntity".equals(typeName)) {
            return magento.DirectoryRegionEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) && "apiEntity".equals(typeName)) {
            return magento.ApiEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentItemEntityArray".equals(typeName)) {
            return magento.SalesOrderShipmentItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductLinkEntityArray".equals(typeName)) {
            return magento.CatalogProductLinkEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkFileInfoEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkFileInfoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoEntity".equals(typeName)) {
            return magento.SalesOrderCreditmemoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogAttributeEntity".equals(typeName)) {
            return magento.CatalogAttributeEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "enterpriseGiftcardaccountListEntityArray".equals(typeName)) {
            return magento.EnterpriseGiftcardaccountListEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeFrontendLabelEntity".equals(typeName)) {
            return magento.CatalogProductAttributeFrontendLabelEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "magentoInfoEntity".equals(typeName)) {
            return magento.MagentoInfoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductLinkAttributeEntity".equals(typeName)) {
            return magento.CatalogProductLinkAttributeEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionValueInfoEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionValueInfoEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "complexFilter".equals(typeName)) {
            return magento.ComplexFilter.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoItemEntity".equals(typeName)) {
            return magento.SalesOrderCreditmemoItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountEntityHistoryArray".equals(typeName)) {
            return magento.GiftcardAccountEntityHistoryArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionTypesArray".equals(typeName)) {
            return magento.CatalogProductCustomOptionTypesArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeOptionLabelArray".equals(typeName)) {
            return magento.CatalogProductAttributeOptionLabelArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderInvoiceItemEntityArray".equals(typeName)) {
            return magento.SalesOrderInvoiceItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftMessageAssociativeProductsEntityArray".equals(typeName)) {
            return magento.GiftMessageAssociativeProductsEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftMessageResponse".equals(typeName)) {
            return magento.GiftMessageResponse.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkSampleEntityArray".equals(
                    typeName)) {
            return magento.CatalogProductDownloadableLinkSampleEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogInventoryStockItemUpdateEntityArray".equals(typeName)) {
            return magento.CatalogInventoryStockItemUpdateEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountCreateGiftcardAccountData".equals(typeName)) {
            return magento.GiftcardAccountCreateGiftcardAccountData.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductEntity".equals(typeName)) {
            return magento.CatalogProductEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductImageEntityArray".equals(typeName)) {
            return magento.CatalogProductImageEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogCategoryEntity".equals(typeName)) {
            return magento.CatalogCategoryEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartCustomerAddressEntityArray".equals(typeName)) {
            return magento.ShoppingCartCustomerAddressEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartShippingMethodEntity".equals(typeName)) {
            return magento.ShoppingCartShippingMethodEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductLinkEntity".equals(typeName)) {
            return magento.CatalogProductLinkEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountCreateNotificationData".equals(typeName)) {
            return magento.GiftcardAccountCreateNotificationData.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTagListEntityArray".equals(typeName)) {
            return magento.CatalogProductTagListEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartCustomerAddressEntity".equals(typeName)) {
            return magento.ShoppingCartCustomerAddressEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountEntityArray".equals(typeName)) {
            return magento.GiftcardAccountEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkEntityArray".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderInvoiceCommentEntityArray".equals(typeName)) {
            return magento.SalesOrderInvoiceCommentEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductRequestAttributes".equals(typeName)) {
            return magento.CatalogProductRequestAttributes.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderListEntity".equals(typeName)) {
            return magento.SalesOrderListEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeMediaCreateEntity".equals(typeName)) {
            return magento.CatalogProductAttributeMediaCreateEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkFileEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkFileEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkListEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkListEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductAttributeEntity".equals(typeName)) {
            return magento.CatalogProductAttributeEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogCategoryEntityNoChildren".equals(typeName)) {
            return magento.CatalogCategoryEntityNoChildren.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderItemEntityArray".equals(typeName)) {
            return magento.SalesOrderItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderInvoiceEntity".equals(typeName)) {
            return magento.SalesOrderInvoiceEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionValueAddEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionValueAddEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "storeEntityArray".equals(typeName)) {
            return magento.StoreEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartTotalsEntity".equals(typeName)) {
            return magento.ShoppingCartTotalsEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogAssignedProduct".equals(typeName)) {
            return magento.CatalogAssignedProduct.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderEntity".equals(typeName)) {
            return magento.SalesOrderEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductDownloadableLinkAddEntity".equals(typeName)) {
            return magento.CatalogProductDownloadableLinkAddEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductTagListEntity".equals(typeName)) {
            return magento.CatalogProductTagListEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentItemEntity".equals(typeName)) {
            return magento.SalesOrderShipmentItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartPaymentMethodResponseEntityArray".equals(typeName)) {
            return magento.ShoppingCartPaymentMethodResponseEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartItemEntity".equals(typeName)) {
            return magento.ShoppingCartItemEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderCreditmemoData".equals(typeName)) {
            return magento.SalesOrderCreditmemoData.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionTypesEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionTypesEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionListEntity".equals(typeName)) {
            return magento.CatalogProductCustomOptionListEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCreateEntityArray".equals(typeName)) {
            return magento.CatalogProductCreateEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductCustomOptionValueListArray".equals(typeName)) {
            return magento.CatalogProductCustomOptionValueListArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogInventoryStockItemEntityArray".equals(typeName)) {
            return magento.CatalogInventoryStockItemEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "shoppingCartProductEntityArray".equals(typeName)) {
            return magento.ShoppingCartProductEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderInvoiceEntityArray".equals(typeName)) {
            return magento.SalesOrderInvoiceEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "catalogProductReturnEntity".equals(typeName)) {
            return magento.CatalogProductReturnEntity.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "salesOrderShipmentTrackEntityArray".equals(typeName)) {
            return magento.SalesOrderShipmentTrackEntityArray.Factory.parse(reader);
        }

        if ("urn:Magento".equals(namespaceURI) &&
                "giftcardAccountEntityHistory".equals(typeName)) {
            return magento.GiftcardAccountEntityHistory.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
