/**
 * CatalogProductCreateEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductCreateEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductCreateEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductCreateEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Categories
     */
    protected magento.ArrayOfString localCategories;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCategoriesTracker = false;

    /**
     * field for Websites
     */
    protected magento.ArrayOfString localWebsites;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWebsitesTracker = false;

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
     * field for Short_description
     */
    protected java.lang.String localShort_description;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShort_descriptionTracker = false;

    /**
     * field for Weight
     */
    protected java.lang.String localWeight;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWeightTracker = false;

    /**
     * field for Status
     */
    protected java.lang.String localStatus;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStatusTracker = false;

    /**
     * field for Url_key
     */
    protected java.lang.String localUrl_key;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUrl_keyTracker = false;

    /**
     * field for Url_path
     */
    protected java.lang.String localUrl_path;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUrl_pathTracker = false;

    /**
     * field for Visibility
     */
    protected java.lang.String localVisibility;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localVisibilityTracker = false;

    /**
     * field for Category_ids
     */
    protected magento.ArrayOfString localCategory_ids;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCategory_idsTracker = false;

    /**
     * field for Website_ids
     */
    protected magento.ArrayOfString localWebsite_ids;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWebsite_idsTracker = false;

    /**
     * field for Has_options
     */
    protected java.lang.String localHas_options;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHas_optionsTracker = false;

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
     * field for Price
     */
    protected java.lang.String localPrice;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPriceTracker = false;

    /**
     * field for Special_price
     */
    protected java.lang.String localSpecial_price;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSpecial_priceTracker = false;

    /**
     * field for Special_from_date
     */
    protected java.lang.String localSpecial_from_date;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSpecial_from_dateTracker = false;

    /**
     * field for Special_to_date
     */
    protected java.lang.String localSpecial_to_date;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSpecial_to_dateTracker = false;

    /**
     * field for Tax_class_id
     */
    protected java.lang.String localTax_class_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTax_class_idTracker = false;

    /**
     * field for Tier_price
     */
    protected magento.CatalogProductTierPriceEntityArray localTier_price;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTier_priceTracker = false;

    /**
     * field for Meta_title
     */
    protected java.lang.String localMeta_title;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMeta_titleTracker = false;

    /**
     * field for Meta_keyword
     */
    protected java.lang.String localMeta_keyword;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMeta_keywordTracker = false;

    /**
     * field for Meta_description
     */
    protected java.lang.String localMeta_description;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMeta_descriptionTracker = false;

    /**
     * field for Custom_design
     */
    protected java.lang.String localCustom_design;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_designTracker = false;

    /**
     * field for Custom_layout_update
     */
    protected java.lang.String localCustom_layout_update;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_layout_updateTracker = false;

    /**
     * field for Options_container
     */
    protected java.lang.String localOptions_container;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOptions_containerTracker = false;

    /**
     * field for Additional_attributes
     */
    protected magento.AssociativeArray localAdditional_attributes;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdditional_attributesTracker = false;

    /**
     * field for Stock_data
     */
    protected magento.CatalogInventoryStockItemUpdateEntity localStock_data;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStock_dataTracker = false;

    public boolean isCategoriesSpecified() {
        return localCategoriesTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getCategories() {
        return localCategories;
    }

    /**
     * Auto generated setter method
     * @param param Categories
     */
    public void setCategories(magento.ArrayOfString param) {
        localCategoriesTracker = param != null;

        this.localCategories = param;
    }

    public boolean isWebsitesSpecified() {
        return localWebsitesTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getWebsites() {
        return localWebsites;
    }

    /**
     * Auto generated setter method
     * @param param Websites
     */
    public void setWebsites(magento.ArrayOfString param) {
        localWebsitesTracker = param != null;

        this.localWebsites = param;
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

    public boolean isShort_descriptionSpecified() {
        return localShort_descriptionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getShort_description() {
        return localShort_description;
    }

    /**
     * Auto generated setter method
     * @param param Short_description
     */
    public void setShort_description(java.lang.String param) {
        localShort_descriptionTracker = param != null;

        this.localShort_description = param;
    }

    public boolean isWeightSpecified() {
        return localWeightTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getWeight() {
        return localWeight;
    }

    /**
     * Auto generated setter method
     * @param param Weight
     */
    public void setWeight(java.lang.String param) {
        localWeightTracker = param != null;

        this.localWeight = param;
    }

    public boolean isStatusSpecified() {
        return localStatusTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStatus() {
        return localStatus;
    }

    /**
     * Auto generated setter method
     * @param param Status
     */
    public void setStatus(java.lang.String param) {
        localStatusTracker = param != null;

        this.localStatus = param;
    }

    public boolean isUrl_keySpecified() {
        return localUrl_keyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getUrl_key() {
        return localUrl_key;
    }

    /**
     * Auto generated setter method
     * @param param Url_key
     */
    public void setUrl_key(java.lang.String param) {
        localUrl_keyTracker = param != null;

        this.localUrl_key = param;
    }

    public boolean isUrl_pathSpecified() {
        return localUrl_pathTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getUrl_path() {
        return localUrl_path;
    }

    /**
     * Auto generated setter method
     * @param param Url_path
     */
    public void setUrl_path(java.lang.String param) {
        localUrl_pathTracker = param != null;

        this.localUrl_path = param;
    }

    public boolean isVisibilitySpecified() {
        return localVisibilityTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getVisibility() {
        return localVisibility;
    }

    /**
     * Auto generated setter method
     * @param param Visibility
     */
    public void setVisibility(java.lang.String param) {
        localVisibilityTracker = param != null;

        this.localVisibility = param;
    }

    public boolean isCategory_idsSpecified() {
        return localCategory_idsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getCategory_ids() {
        return localCategory_ids;
    }

    /**
     * Auto generated setter method
     * @param param Category_ids
     */
    public void setCategory_ids(magento.ArrayOfString param) {
        localCategory_idsTracker = param != null;

        this.localCategory_ids = param;
    }

    public boolean isWebsite_idsSpecified() {
        return localWebsite_idsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getWebsite_ids() {
        return localWebsite_ids;
    }

    /**
     * Auto generated setter method
     * @param param Website_ids
     */
    public void setWebsite_ids(magento.ArrayOfString param) {
        localWebsite_idsTracker = param != null;

        this.localWebsite_ids = param;
    }

    public boolean isHas_optionsSpecified() {
        return localHas_optionsTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getHas_options() {
        return localHas_options;
    }

    /**
     * Auto generated setter method
     * @param param Has_options
     */
    public void setHas_options(java.lang.String param) {
        localHas_optionsTracker = param != null;

        this.localHas_options = param;
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

    public boolean isPriceSpecified() {
        return localPriceTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPrice() {
        return localPrice;
    }

    /**
     * Auto generated setter method
     * @param param Price
     */
    public void setPrice(java.lang.String param) {
        localPriceTracker = param != null;

        this.localPrice = param;
    }

    public boolean isSpecial_priceSpecified() {
        return localSpecial_priceTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSpecial_price() {
        return localSpecial_price;
    }

    /**
     * Auto generated setter method
     * @param param Special_price
     */
    public void setSpecial_price(java.lang.String param) {
        localSpecial_priceTracker = param != null;

        this.localSpecial_price = param;
    }

    public boolean isSpecial_from_dateSpecified() {
        return localSpecial_from_dateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSpecial_from_date() {
        return localSpecial_from_date;
    }

    /**
     * Auto generated setter method
     * @param param Special_from_date
     */
    public void setSpecial_from_date(java.lang.String param) {
        localSpecial_from_dateTracker = param != null;

        this.localSpecial_from_date = param;
    }

    public boolean isSpecial_to_dateSpecified() {
        return localSpecial_to_dateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSpecial_to_date() {
        return localSpecial_to_date;
    }

    /**
     * Auto generated setter method
     * @param param Special_to_date
     */
    public void setSpecial_to_date(java.lang.String param) {
        localSpecial_to_dateTracker = param != null;

        this.localSpecial_to_date = param;
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

    public boolean isTier_priceSpecified() {
        return localTier_priceTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductTierPriceEntityArray
     */
    public magento.CatalogProductTierPriceEntityArray getTier_price() {
        return localTier_price;
    }

    /**
     * Auto generated setter method
     * @param param Tier_price
     */
    public void setTier_price(magento.CatalogProductTierPriceEntityArray param) {
        localTier_priceTracker = param != null;

        this.localTier_price = param;
    }

    public boolean isMeta_titleSpecified() {
        return localMeta_titleTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMeta_title() {
        return localMeta_title;
    }

    /**
     * Auto generated setter method
     * @param param Meta_title
     */
    public void setMeta_title(java.lang.String param) {
        localMeta_titleTracker = param != null;

        this.localMeta_title = param;
    }

    public boolean isMeta_keywordSpecified() {
        return localMeta_keywordTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMeta_keyword() {
        return localMeta_keyword;
    }

    /**
     * Auto generated setter method
     * @param param Meta_keyword
     */
    public void setMeta_keyword(java.lang.String param) {
        localMeta_keywordTracker = param != null;

        this.localMeta_keyword = param;
    }

    public boolean isMeta_descriptionSpecified() {
        return localMeta_descriptionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMeta_description() {
        return localMeta_description;
    }

    /**
     * Auto generated setter method
     * @param param Meta_description
     */
    public void setMeta_description(java.lang.String param) {
        localMeta_descriptionTracker = param != null;

        this.localMeta_description = param;
    }

    public boolean isCustom_designSpecified() {
        return localCustom_designTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCustom_design() {
        return localCustom_design;
    }

    /**
     * Auto generated setter method
     * @param param Custom_design
     */
    public void setCustom_design(java.lang.String param) {
        localCustom_designTracker = param != null;

        this.localCustom_design = param;
    }

    public boolean isCustom_layout_updateSpecified() {
        return localCustom_layout_updateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCustom_layout_update() {
        return localCustom_layout_update;
    }

    /**
     * Auto generated setter method
     * @param param Custom_layout_update
     */
    public void setCustom_layout_update(java.lang.String param) {
        localCustom_layout_updateTracker = param != null;

        this.localCustom_layout_update = param;
    }

    public boolean isOptions_containerSpecified() {
        return localOptions_containerTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOptions_container() {
        return localOptions_container;
    }

    /**
     * Auto generated setter method
     * @param param Options_container
     */
    public void setOptions_container(java.lang.String param) {
        localOptions_containerTracker = param != null;

        this.localOptions_container = param;
    }

    public boolean isAdditional_attributesSpecified() {
        return localAdditional_attributesTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.AssociativeArray
     */
    public magento.AssociativeArray getAdditional_attributes() {
        return localAdditional_attributes;
    }

    /**
     * Auto generated setter method
     * @param param Additional_attributes
     */
    public void setAdditional_attributes(magento.AssociativeArray param) {
        localAdditional_attributesTracker = param != null;

        this.localAdditional_attributes = param;
    }

    public boolean isStock_dataSpecified() {
        return localStock_dataTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogInventoryStockItemUpdateEntity
     */
    public magento.CatalogInventoryStockItemUpdateEntity getStock_data() {
        return localStock_data;
    }

    /**
     * Auto generated setter method
     * @param param Stock_data
     */
    public void setStock_data(
        magento.CatalogInventoryStockItemUpdateEntity param) {
        localStock_dataTracker = param != null;

        this.localStock_data = param;
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
                    namespacePrefix + ":catalogProductCreateEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductCreateEntity", xmlWriter);
            }
        }

        if (localCategoriesTracker) {
            if (localCategories == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "categories cannot be null!!");
            }

            localCategories.serialize(new javax.xml.namespace.QName("",
                    "categories"), xmlWriter);
        }

        if (localWebsitesTracker) {
            if (localWebsites == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "websites cannot be null!!");
            }

            localWebsites.serialize(new javax.xml.namespace.QName("", "websites"),
                xmlWriter);
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

        if (localShort_descriptionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "short_description", xmlWriter);

            if (localShort_description == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "short_description cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShort_description);
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

        if (localUrl_keyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "url_key", xmlWriter);

            if (localUrl_key == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "url_key cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localUrl_key);
            }

            xmlWriter.writeEndElement();
        }

        if (localUrl_pathTracker) {
            namespace = "";
            writeStartElement(null, namespace, "url_path", xmlWriter);

            if (localUrl_path == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "url_path cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localUrl_path);
            }

            xmlWriter.writeEndElement();
        }

        if (localVisibilityTracker) {
            namespace = "";
            writeStartElement(null, namespace, "visibility", xmlWriter);

            if (localVisibility == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "visibility cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localVisibility);
            }

            xmlWriter.writeEndElement();
        }

        if (localCategory_idsTracker) {
            if (localCategory_ids == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "category_ids cannot be null!!");
            }

            localCategory_ids.serialize(new javax.xml.namespace.QName("",
                    "category_ids"), xmlWriter);
        }

        if (localWebsite_idsTracker) {
            if (localWebsite_ids == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "website_ids cannot be null!!");
            }

            localWebsite_ids.serialize(new javax.xml.namespace.QName("",
                    "website_ids"), xmlWriter);
        }

        if (localHas_optionsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "has_options", xmlWriter);

            if (localHas_options == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "has_options cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localHas_options);
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

        if (localPriceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "price", xmlWriter);

            if (localPrice == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "price cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPrice);
            }

            xmlWriter.writeEndElement();
        }

        if (localSpecial_priceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "special_price", xmlWriter);

            if (localSpecial_price == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "special_price cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSpecial_price);
            }

            xmlWriter.writeEndElement();
        }

        if (localSpecial_from_dateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "special_from_date", xmlWriter);

            if (localSpecial_from_date == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "special_from_date cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSpecial_from_date);
            }

            xmlWriter.writeEndElement();
        }

        if (localSpecial_to_dateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "special_to_date", xmlWriter);

            if (localSpecial_to_date == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "special_to_date cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSpecial_to_date);
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

        if (localTier_priceTracker) {
            if (localTier_price == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tier_price cannot be null!!");
            }

            localTier_price.serialize(new javax.xml.namespace.QName("",
                    "tier_price"), xmlWriter);
        }

        if (localMeta_titleTracker) {
            namespace = "";
            writeStartElement(null, namespace, "meta_title", xmlWriter);

            if (localMeta_title == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_title cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMeta_title);
            }

            xmlWriter.writeEndElement();
        }

        if (localMeta_keywordTracker) {
            namespace = "";
            writeStartElement(null, namespace, "meta_keyword", xmlWriter);

            if (localMeta_keyword == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_keyword cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMeta_keyword);
            }

            xmlWriter.writeEndElement();
        }

        if (localMeta_descriptionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "meta_description", xmlWriter);

            if (localMeta_description == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_description cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMeta_description);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_designTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_design", xmlWriter);

            if (localCustom_design == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustom_design);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_layout_updateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_layout_update", xmlWriter);

            if (localCustom_layout_update == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_layout_update cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustom_layout_update);
            }

            xmlWriter.writeEndElement();
        }

        if (localOptions_containerTracker) {
            namespace = "";
            writeStartElement(null, namespace, "options_container", xmlWriter);

            if (localOptions_container == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "options_container cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOptions_container);
            }

            xmlWriter.writeEndElement();
        }

        if (localAdditional_attributesTracker) {
            if (localAdditional_attributes == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_attributes cannot be null!!");
            }

            localAdditional_attributes.serialize(new javax.xml.namespace.QName(
                    "", "additional_attributes"), xmlWriter);
        }

        if (localStock_dataTracker) {
            if (localStock_data == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "stock_data cannot be null!!");
            }

            localStock_data.serialize(new javax.xml.namespace.QName("",
                    "stock_data"), xmlWriter);
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

        if (localCategoriesTracker) {
            elementList.add(new javax.xml.namespace.QName("", "categories"));

            if (localCategories == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "categories cannot be null!!");
            }

            elementList.add(localCategories);
        }

        if (localWebsitesTracker) {
            elementList.add(new javax.xml.namespace.QName("", "websites"));

            if (localWebsites == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "websites cannot be null!!");
            }

            elementList.add(localWebsites);
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

        if (localShort_descriptionTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "short_description"));

            if (localShort_description != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShort_description));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "short_description cannot be null!!");
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

        if (localUrl_keyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "url_key"));

            if (localUrl_key != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUrl_key));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "url_key cannot be null!!");
            }
        }

        if (localUrl_pathTracker) {
            elementList.add(new javax.xml.namespace.QName("", "url_path"));

            if (localUrl_path != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUrl_path));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "url_path cannot be null!!");
            }
        }

        if (localVisibilityTracker) {
            elementList.add(new javax.xml.namespace.QName("", "visibility"));

            if (localVisibility != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localVisibility));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "visibility cannot be null!!");
            }
        }

        if (localCategory_idsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "category_ids"));

            if (localCategory_ids == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "category_ids cannot be null!!");
            }

            elementList.add(localCategory_ids);
        }

        if (localWebsite_idsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "website_ids"));

            if (localWebsite_ids == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "website_ids cannot be null!!");
            }

            elementList.add(localWebsite_ids);
        }

        if (localHas_optionsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "has_options"));

            if (localHas_options != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localHas_options));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "has_options cannot be null!!");
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

        if (localPriceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "price"));

            if (localPrice != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPrice));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "price cannot be null!!");
            }
        }

        if (localSpecial_priceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "special_price"));

            if (localSpecial_price != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSpecial_price));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "special_price cannot be null!!");
            }
        }

        if (localSpecial_from_dateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "special_from_date"));

            if (localSpecial_from_date != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSpecial_from_date));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "special_from_date cannot be null!!");
            }
        }

        if (localSpecial_to_dateTracker) {
            elementList.add(new javax.xml.namespace.QName("", "special_to_date"));

            if (localSpecial_to_date != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSpecial_to_date));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "special_to_date cannot be null!!");
            }
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

        if (localTier_priceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tier_price"));

            if (localTier_price == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tier_price cannot be null!!");
            }

            elementList.add(localTier_price);
        }

        if (localMeta_titleTracker) {
            elementList.add(new javax.xml.namespace.QName("", "meta_title"));

            if (localMeta_title != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMeta_title));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_title cannot be null!!");
            }
        }

        if (localMeta_keywordTracker) {
            elementList.add(new javax.xml.namespace.QName("", "meta_keyword"));

            if (localMeta_keyword != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMeta_keyword));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_keyword cannot be null!!");
            }
        }

        if (localMeta_descriptionTracker) {
            elementList.add(new javax.xml.namespace.QName("", "meta_description"));

            if (localMeta_description != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMeta_description));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_description cannot be null!!");
            }
        }

        if (localCustom_designTracker) {
            elementList.add(new javax.xml.namespace.QName("", "custom_design"));

            if (localCustom_design != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_design));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design cannot be null!!");
            }
        }

        if (localCustom_layout_updateTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "custom_layout_update"));

            if (localCustom_layout_update != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_layout_update));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_layout_update cannot be null!!");
            }
        }

        if (localOptions_containerTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "options_container"));

            if (localOptions_container != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOptions_container));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "options_container cannot be null!!");
            }
        }

        if (localAdditional_attributesTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "additional_attributes"));

            if (localAdditional_attributes == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_attributes cannot be null!!");
            }

            elementList.add(localAdditional_attributes);
        }

        if (localStock_dataTracker) {
            elementList.add(new javax.xml.namespace.QName("", "stock_data"));

            if (localStock_data == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "stock_data cannot be null!!");
            }

            elementList.add(localStock_data);
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
        public static CatalogProductCreateEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CatalogProductCreateEntity object = new CatalogProductCreateEntity();

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

                        if (!"catalogProductCreateEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductCreateEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "categories").equals(
                            reader.getName())) {
                    object.setCategories(magento.ArrayOfString.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "websites").equals(
                            reader.getName())) {
                    object.setWebsites(magento.ArrayOfString.Factory.parse(
                            reader));

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
                        new javax.xml.namespace.QName("", "short_description").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "short_description" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setShort_description(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    object.setWeight(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    java.lang.String content = reader.getElementText();

                    object.setStatus(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "url_key").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "url_key" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setUrl_key(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "url_path").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "url_path" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setUrl_path(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "visibility").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "visibility" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setVisibility(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "category_ids").equals(
                            reader.getName())) {
                    object.setCategory_ids(magento.ArrayOfString.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "website_ids").equals(
                            reader.getName())) {
                    object.setWebsite_ids(magento.ArrayOfString.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "has_options").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "has_options" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setHas_options(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    object.setPrice(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "special_price").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "special_price" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSpecial_price(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "special_from_date").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "special_from_date" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSpecial_from_date(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "special_to_date").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "special_to_date" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSpecial_to_date(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tier_price").equals(
                            reader.getName())) {
                    object.setTier_price(magento.CatalogProductTierPriceEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "meta_title").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "meta_title" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMeta_title(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "meta_keyword").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "meta_keyword" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMeta_keyword(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "meta_description").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "meta_description" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMeta_description(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "custom_design").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_design" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_design(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "custom_layout_update").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_layout_update" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_layout_update(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "options_container").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "options_container" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOptions_container(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "additional_attributes").equals(reader.getName())) {
                    object.setAdditional_attributes(magento.AssociativeArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "stock_data").equals(
                            reader.getName())) {
                    object.setStock_data(magento.CatalogInventoryStockItemUpdateEntity.Factory.parse(
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
