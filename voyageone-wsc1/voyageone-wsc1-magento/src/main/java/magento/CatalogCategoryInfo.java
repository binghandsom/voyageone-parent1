/**
 * CatalogCategoryInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogCategoryInfo bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogCategoryInfo implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogCategoryInfo
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Category_id
     */
    protected java.lang.String localCategory_id;

    /**
     * field for Is_active
     */
    protected int localIs_active;

    /**
     * field for Position
     */
    protected java.lang.String localPosition;

    /**
     * field for Level
     */
    protected java.lang.String localLevel;

    /**
     * field for Parent_id
     */
    protected java.lang.String localParent_id;

    /**
     * field for All_children
     */
    protected java.lang.String localAll_children;

    /**
     * field for Children
     */
    protected java.lang.String localChildren;

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
     * field for Name
     */
    protected java.lang.String localName;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localNameTracker = false;

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
     * field for Description
     */
    protected java.lang.String localDescription;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDescriptionTracker = false;

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
     * field for Meta_keywords
     */
    protected java.lang.String localMeta_keywords;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMeta_keywordsTracker = false;

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
     * field for Path
     */
    protected java.lang.String localPath;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPathTracker = false;

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
     * field for Children_count
     */
    protected int localChildren_count;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localChildren_countTracker = false;

    /**
     * field for Display_mode
     */
    protected java.lang.String localDisplay_mode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDisplay_modeTracker = false;

    /**
     * field for Is_anchor
     */
    protected int localIs_anchor;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_anchorTracker = false;

    /**
     * field for Available_sort_by
     */
    protected magento.ArrayOfString localAvailable_sort_by;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAvailable_sort_byTracker = false;

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
     * field for Custom_design_apply
     */
    protected java.lang.String localCustom_design_apply;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_design_applyTracker = false;

    /**
     * field for Custom_design_from
     */
    protected java.lang.String localCustom_design_from;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_design_fromTracker = false;

    /**
     * field for Custom_design_to
     */
    protected java.lang.String localCustom_design_to;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_design_toTracker = false;

    /**
     * field for Page_layout
     */
    protected java.lang.String localPage_layout;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPage_layoutTracker = false;

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
     * field for Default_sort_by
     */
    protected java.lang.String localDefault_sort_by;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDefault_sort_byTracker = false;

    /**
     * field for Landing_page
     */
    protected int localLanding_page;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localLanding_pageTracker = false;

    /**
     * field for Include_in_menu
     */
    protected int localInclude_in_menu;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localInclude_in_menuTracker = false;

    /**
     * field for Custom_use_parent_settings
     */
    protected int localCustom_use_parent_settings;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_use_parent_settingsTracker = false;

    /**
     * field for Custom_apply_to_products
     */
    protected int localCustom_apply_to_products;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustom_apply_to_productsTracker = false;

    /**
     * field for Filter_price_range
     */
    protected java.lang.String localFilter_price_range;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFilter_price_rangeTracker = false;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCategory_id() {
        return localCategory_id;
    }

    /**
     * Auto generated setter method
     * @param param Category_id
     */
    public void setCategory_id(java.lang.String param) {
        this.localCategory_id = param;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_active() {
        return localIs_active;
    }

    /**
     * Auto generated setter method
     * @param param Is_active
     */
    public void setIs_active(int param) {
        this.localIs_active = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPosition() {
        return localPosition;
    }

    /**
     * Auto generated setter method
     * @param param Position
     */
    public void setPosition(java.lang.String param) {
        this.localPosition = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getLevel() {
        return localLevel;
    }

    /**
     * Auto generated setter method
     * @param param Level
     */
    public void setLevel(java.lang.String param) {
        this.localLevel = param;
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
        this.localParent_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAll_children() {
        return localAll_children;
    }

    /**
     * Auto generated setter method
     * @param param All_children
     */
    public void setAll_children(java.lang.String param) {
        this.localAll_children = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getChildren() {
        return localChildren;
    }

    /**
     * Auto generated setter method
     * @param param Children
     */
    public void setChildren(java.lang.String param) {
        this.localChildren = param;
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

    public boolean isMeta_keywordsSpecified() {
        return localMeta_keywordsTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMeta_keywords() {
        return localMeta_keywords;
    }

    /**
     * Auto generated setter method
     * @param param Meta_keywords
     */
    public void setMeta_keywords(java.lang.String param) {
        localMeta_keywordsTracker = param != null;

        this.localMeta_keywords = param;
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

    public boolean isPathSpecified() {
        return localPathTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPath() {
        return localPath;
    }

    /**
     * Auto generated setter method
     * @param param Path
     */
    public void setPath(java.lang.String param) {
        localPathTracker = param != null;

        this.localPath = param;
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

    public boolean isChildren_countSpecified() {
        return localChildren_countTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getChildren_count() {
        return localChildren_count;
    }

    /**
     * Auto generated setter method
     * @param param Children_count
     */
    public void setChildren_count(int param) {
        // setting primitive attribute tracker to true
        localChildren_countTracker = param != java.lang.Integer.MIN_VALUE;

        this.localChildren_count = param;
    }

    public boolean isDisplay_modeSpecified() {
        return localDisplay_modeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDisplay_mode() {
        return localDisplay_mode;
    }

    /**
     * Auto generated setter method
     * @param param Display_mode
     */
    public void setDisplay_mode(java.lang.String param) {
        localDisplay_modeTracker = param != null;

        this.localDisplay_mode = param;
    }

    public boolean isIs_anchorSpecified() {
        return localIs_anchorTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_anchor() {
        return localIs_anchor;
    }

    /**
     * Auto generated setter method
     * @param param Is_anchor
     */
    public void setIs_anchor(int param) {
        // setting primitive attribute tracker to true
        localIs_anchorTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_anchor = param;
    }

    public boolean isAvailable_sort_bySpecified() {
        return localAvailable_sort_byTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getAvailable_sort_by() {
        return localAvailable_sort_by;
    }

    /**
     * Auto generated setter method
     * @param param Available_sort_by
     */
    public void setAvailable_sort_by(magento.ArrayOfString param) {
        localAvailable_sort_byTracker = param != null;

        this.localAvailable_sort_by = param;
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

    public boolean isCustom_design_applySpecified() {
        return localCustom_design_applyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCustom_design_apply() {
        return localCustom_design_apply;
    }

    /**
     * Auto generated setter method
     * @param param Custom_design_apply
     */
    public void setCustom_design_apply(java.lang.String param) {
        localCustom_design_applyTracker = param != null;

        this.localCustom_design_apply = param;
    }

    public boolean isCustom_design_fromSpecified() {
        return localCustom_design_fromTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCustom_design_from() {
        return localCustom_design_from;
    }

    /**
     * Auto generated setter method
     * @param param Custom_design_from
     */
    public void setCustom_design_from(java.lang.String param) {
        localCustom_design_fromTracker = param != null;

        this.localCustom_design_from = param;
    }

    public boolean isCustom_design_toSpecified() {
        return localCustom_design_toTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCustom_design_to() {
        return localCustom_design_to;
    }

    /**
     * Auto generated setter method
     * @param param Custom_design_to
     */
    public void setCustom_design_to(java.lang.String param) {
        localCustom_design_toTracker = param != null;

        this.localCustom_design_to = param;
    }

    public boolean isPage_layoutSpecified() {
        return localPage_layoutTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPage_layout() {
        return localPage_layout;
    }

    /**
     * Auto generated setter method
     * @param param Page_layout
     */
    public void setPage_layout(java.lang.String param) {
        localPage_layoutTracker = param != null;

        this.localPage_layout = param;
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

    public boolean isDefault_sort_bySpecified() {
        return localDefault_sort_byTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDefault_sort_by() {
        return localDefault_sort_by;
    }

    /**
     * Auto generated setter method
     * @param param Default_sort_by
     */
    public void setDefault_sort_by(java.lang.String param) {
        localDefault_sort_byTracker = param != null;

        this.localDefault_sort_by = param;
    }

    public boolean isLanding_pageSpecified() {
        return localLanding_pageTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getLanding_page() {
        return localLanding_page;
    }

    /**
     * Auto generated setter method
     * @param param Landing_page
     */
    public void setLanding_page(int param) {
        // setting primitive attribute tracker to true
        localLanding_pageTracker = param != java.lang.Integer.MIN_VALUE;

        this.localLanding_page = param;
    }

    public boolean isInclude_in_menuSpecified() {
        return localInclude_in_menuTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getInclude_in_menu() {
        return localInclude_in_menu;
    }

    /**
     * Auto generated setter method
     * @param param Include_in_menu
     */
    public void setInclude_in_menu(int param) {
        // setting primitive attribute tracker to true
        localInclude_in_menuTracker = param != java.lang.Integer.MIN_VALUE;

        this.localInclude_in_menu = param;
    }

    public boolean isCustom_use_parent_settingsSpecified() {
        return localCustom_use_parent_settingsTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getCustom_use_parent_settings() {
        return localCustom_use_parent_settings;
    }

    /**
     * Auto generated setter method
     * @param param Custom_use_parent_settings
     */
    public void setCustom_use_parent_settings(int param) {
        // setting primitive attribute tracker to true
        localCustom_use_parent_settingsTracker = param != java.lang.Integer.MIN_VALUE;

        this.localCustom_use_parent_settings = param;
    }

    public boolean isCustom_apply_to_productsSpecified() {
        return localCustom_apply_to_productsTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getCustom_apply_to_products() {
        return localCustom_apply_to_products;
    }

    /**
     * Auto generated setter method
     * @param param Custom_apply_to_products
     */
    public void setCustom_apply_to_products(int param) {
        // setting primitive attribute tracker to true
        localCustom_apply_to_productsTracker = param != java.lang.Integer.MIN_VALUE;

        this.localCustom_apply_to_products = param;
    }

    public boolean isFilter_price_rangeSpecified() {
        return localFilter_price_rangeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getFilter_price_range() {
        return localFilter_price_range;
    }

    /**
     * Auto generated setter method
     * @param param Filter_price_range
     */
    public void setFilter_price_range(java.lang.String param) {
        localFilter_price_rangeTracker = param != null;

        this.localFilter_price_range = param;
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
                    namespacePrefix + ":catalogCategoryInfo", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogCategoryInfo", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "category_id", xmlWriter);

        if (localCategory_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "category_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localCategory_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "is_active", xmlWriter);

        if (localIs_active == java.lang.Integer.MIN_VALUE) {
            throw new org.apache.axis2.databinding.ADBException(
                "is_active cannot be null!!");
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_active));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "position", xmlWriter);

        if (localPosition == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "position cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localPosition);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "level", xmlWriter);

        if (localLevel == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "level cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localLevel);
        }

        xmlWriter.writeEndElement();

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

        namespace = "";
        writeStartElement(null, namespace, "all_children", xmlWriter);

        if (localAll_children == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "all_children cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localAll_children);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "children", xmlWriter);

        if (localChildren == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "children cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localChildren);
        }

        xmlWriter.writeEndElement();

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

        if (localMeta_keywordsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "meta_keywords", xmlWriter);

            if (localMeta_keywords == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_keywords cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMeta_keywords);
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

        if (localPathTracker) {
            namespace = "";
            writeStartElement(null, namespace, "path", xmlWriter);

            if (localPath == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "path cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPath);
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

        if (localChildren_countTracker) {
            namespace = "";
            writeStartElement(null, namespace, "children_count", xmlWriter);

            if (localChildren_count == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "children_count cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localChildren_count));
            }

            xmlWriter.writeEndElement();
        }

        if (localDisplay_modeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "display_mode", xmlWriter);

            if (localDisplay_mode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "display_mode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDisplay_mode);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_anchorTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_anchor", xmlWriter);

            if (localIs_anchor == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_anchor cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_anchor));
            }

            xmlWriter.writeEndElement();
        }

        if (localAvailable_sort_byTracker) {
            if (localAvailable_sort_by == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "available_sort_by cannot be null!!");
            }

            localAvailable_sort_by.serialize(new javax.xml.namespace.QName("",
                    "available_sort_by"), xmlWriter);
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

        if (localCustom_design_applyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_design_apply", xmlWriter);

            if (localCustom_design_apply == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design_apply cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustom_design_apply);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_design_fromTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_design_from", xmlWriter);

            if (localCustom_design_from == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design_from cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustom_design_from);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_design_toTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_design_to", xmlWriter);

            if (localCustom_design_to == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design_to cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCustom_design_to);
            }

            xmlWriter.writeEndElement();
        }

        if (localPage_layoutTracker) {
            namespace = "";
            writeStartElement(null, namespace, "page_layout", xmlWriter);

            if (localPage_layout == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "page_layout cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPage_layout);
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

        if (localDefault_sort_byTracker) {
            namespace = "";
            writeStartElement(null, namespace, "default_sort_by", xmlWriter);

            if (localDefault_sort_by == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "default_sort_by cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDefault_sort_by);
            }

            xmlWriter.writeEndElement();
        }

        if (localLanding_pageTracker) {
            namespace = "";
            writeStartElement(null, namespace, "landing_page", xmlWriter);

            if (localLanding_page == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "landing_page cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localLanding_page));
            }

            xmlWriter.writeEndElement();
        }

        if (localInclude_in_menuTracker) {
            namespace = "";
            writeStartElement(null, namespace, "include_in_menu", xmlWriter);

            if (localInclude_in_menu == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "include_in_menu cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localInclude_in_menu));
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_use_parent_settingsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_use_parent_settings",
                xmlWriter);

            if (localCustom_use_parent_settings == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_use_parent_settings cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_use_parent_settings));
            }

            xmlWriter.writeEndElement();
        }

        if (localCustom_apply_to_productsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "custom_apply_to_products",
                xmlWriter);

            if (localCustom_apply_to_products == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_apply_to_products cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_apply_to_products));
            }

            xmlWriter.writeEndElement();
        }

        if (localFilter_price_rangeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "filter_price_range", xmlWriter);

            if (localFilter_price_range == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "filter_price_range cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localFilter_price_range);
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

        elementList.add(new javax.xml.namespace.QName("", "category_id"));

        if (localCategory_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localCategory_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "category_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "is_active"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                localIs_active));

        elementList.add(new javax.xml.namespace.QName("", "position"));

        if (localPosition != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localPosition));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "position cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "level"));

        if (localLevel != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localLevel));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "level cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "parent_id"));

        if (localParent_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localParent_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "parent_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "all_children"));

        if (localAll_children != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localAll_children));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "all_children cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "children"));

        if (localChildren != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localChildren));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "children cannot be null!!");
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

        if (localMeta_keywordsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "meta_keywords"));

            if (localMeta_keywords != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMeta_keywords));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "meta_keywords cannot be null!!");
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

        if (localPathTracker) {
            elementList.add(new javax.xml.namespace.QName("", "path"));

            if (localPath != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPath));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "path cannot be null!!");
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

        if (localChildren_countTracker) {
            elementList.add(new javax.xml.namespace.QName("", "children_count"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localChildren_count));
        }

        if (localDisplay_modeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "display_mode"));

            if (localDisplay_mode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDisplay_mode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "display_mode cannot be null!!");
            }
        }

        if (localIs_anchorTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_anchor"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_anchor));
        }

        if (localAvailable_sort_byTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "available_sort_by"));

            if (localAvailable_sort_by == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "available_sort_by cannot be null!!");
            }

            elementList.add(localAvailable_sort_by);
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

        if (localCustom_design_applyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "custom_design_apply"));

            if (localCustom_design_apply != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_design_apply));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design_apply cannot be null!!");
            }
        }

        if (localCustom_design_fromTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "custom_design_from"));

            if (localCustom_design_from != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_design_from));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design_from cannot be null!!");
            }
        }

        if (localCustom_design_toTracker) {
            elementList.add(new javax.xml.namespace.QName("", "custom_design_to"));

            if (localCustom_design_to != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustom_design_to));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "custom_design_to cannot be null!!");
            }
        }

        if (localPage_layoutTracker) {
            elementList.add(new javax.xml.namespace.QName("", "page_layout"));

            if (localPage_layout != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPage_layout));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "page_layout cannot be null!!");
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

        if (localDefault_sort_byTracker) {
            elementList.add(new javax.xml.namespace.QName("", "default_sort_by"));

            if (localDefault_sort_by != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDefault_sort_by));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "default_sort_by cannot be null!!");
            }
        }

        if (localLanding_pageTracker) {
            elementList.add(new javax.xml.namespace.QName("", "landing_page"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localLanding_page));
        }

        if (localInclude_in_menuTracker) {
            elementList.add(new javax.xml.namespace.QName("", "include_in_menu"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localInclude_in_menu));
        }

        if (localCustom_use_parent_settingsTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "custom_use_parent_settings"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localCustom_use_parent_settings));
        }

        if (localCustom_apply_to_productsTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "custom_apply_to_products"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localCustom_apply_to_products));
        }

        if (localFilter_price_rangeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "filter_price_range"));

            if (localFilter_price_range != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localFilter_price_range));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "filter_price_range cannot be null!!");
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
        public static CatalogCategoryInfo parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CatalogCategoryInfo object = new CatalogCategoryInfo();

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

                        if (!"catalogCategoryInfo".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogCategoryInfo) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "category_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "category_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCategory_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
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

                    object.setIs_active(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "position").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "position" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPosition(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "level").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "level" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "all_children").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "all_children" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAll_children(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "children").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "children" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setChildren(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
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
                        new javax.xml.namespace.QName("", "meta_keywords").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "meta_keywords" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMeta_keywords(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "path").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "path" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPath(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "children_count").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "children_count" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setChildren_count(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setChildren_count(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "display_mode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "display_mode" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDisplay_mode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_anchor").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_anchor" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_anchor(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_anchor(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "available_sort_by").equals(
                            reader.getName())) {
                    object.setAvailable_sort_by(magento.ArrayOfString.Factory.parse(
                            reader));

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
                        new javax.xml.namespace.QName("", "custom_design_apply").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_design_apply" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_design_apply(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "custom_design_from").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_design_from" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_design_from(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "custom_design_to").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_design_to" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_design_to(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "page_layout").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "page_layout" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPage_layout(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "default_sort_by").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "default_sort_by" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefault_sort_by(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "landing_page").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "landing_page" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLanding_page(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setLanding_page(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "include_in_menu").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "include_in_menu" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setInclude_in_menu(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setInclude_in_menu(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "custom_use_parent_settings").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_use_parent_settings" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_use_parent_settings(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setCustom_use_parent_settings(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "custom_apply_to_products").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "custom_apply_to_products" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom_apply_to_products(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setCustom_apply_to_products(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "filter_price_range").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "filter_price_range" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFilter_price_range(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
