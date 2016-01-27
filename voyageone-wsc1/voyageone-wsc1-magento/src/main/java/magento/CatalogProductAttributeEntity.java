/**
 * CatalogProductAttributeEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductAttributeEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductAttributeEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductAttributeEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Attribute_id
     */
    protected java.lang.String localAttribute_id;

    /**
     * field for Attribute_code
     */
    protected java.lang.String localAttribute_code;

    /**
     * field for Frontend_input
     */
    protected java.lang.String localFrontend_input;

    /**
     * field for Scope
     */
    protected java.lang.String localScope;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localScopeTracker = false;

    /**
     * field for Default_value
     */
    protected java.lang.String localDefault_value;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDefault_valueTracker = false;

    /**
     * field for Is_unique
     */
    protected int localIs_unique;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_uniqueTracker = false;

    /**
     * field for Is_required
     */
    protected int localIs_required;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_requiredTracker = false;

    /**
     * field for Apply_to
     */
    protected magento.ArrayOfString localApply_to;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localApply_toTracker = false;

    /**
     * field for Is_configurable
     */
    protected int localIs_configurable;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_configurableTracker = false;

    /**
     * field for Is_searchable
     */
    protected int localIs_searchable;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_searchableTracker = false;

    /**
     * field for Is_visible_in_advanced_search
     */
    protected int localIs_visible_in_advanced_search;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_visible_in_advanced_searchTracker = false;

    /**
     * field for Is_comparable
     */
    protected int localIs_comparable;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_comparableTracker = false;

    /**
     * field for Is_used_for_promo_rules
     */
    protected int localIs_used_for_promo_rules;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_used_for_promo_rulesTracker = false;

    /**
     * field for Is_visible_on_front
     */
    protected int localIs_visible_on_front;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_visible_on_frontTracker = false;

    /**
     * field for Used_in_product_listing
     */
    protected int localUsed_in_product_listing;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUsed_in_product_listingTracker = false;

    /**
     * field for Additional_fields
     */
    protected magento.AssociativeArray localAdditional_fields;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdditional_fieldsTracker = false;

    /**
     * field for Options
     */
    protected magento.CatalogAttributeOptionEntityArray localOptions;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOptionsTracker = false;

    /**
     * field for Frontend_label
     */
    protected magento.CatalogProductAttributeFrontendLabelArray localFrontend_label;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAttribute_id() {
        return localAttribute_id;
    }

    /**
     * Auto generated setter method
     * @param param Attribute_id
     */
    public void setAttribute_id(java.lang.String param) {
        this.localAttribute_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAttribute_code() {
        return localAttribute_code;
    }

    /**
     * Auto generated setter method
     * @param param Attribute_code
     */
    public void setAttribute_code(java.lang.String param) {
        this.localAttribute_code = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getFrontend_input() {
        return localFrontend_input;
    }

    /**
     * Auto generated setter method
     * @param param Frontend_input
     */
    public void setFrontend_input(java.lang.String param) {
        this.localFrontend_input = param;
    }

    public boolean isScopeSpecified() {
        return localScopeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getScope() {
        return localScope;
    }

    /**
     * Auto generated setter method
     * @param param Scope
     */
    public void setScope(java.lang.String param) {
        localScopeTracker = param != null;

        this.localScope = param;
    }

    public boolean isDefault_valueSpecified() {
        return localDefault_valueTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDefault_value() {
        return localDefault_value;
    }

    /**
     * Auto generated setter method
     * @param param Default_value
     */
    public void setDefault_value(java.lang.String param) {
        localDefault_valueTracker = param != null;

        this.localDefault_value = param;
    }

    public boolean isIs_uniqueSpecified() {
        return localIs_uniqueTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_unique() {
        return localIs_unique;
    }

    /**
     * Auto generated setter method
     * @param param Is_unique
     */
    public void setIs_unique(int param) {
        // setting primitive attribute tracker to true
        localIs_uniqueTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_unique = param;
    }

    public boolean isIs_requiredSpecified() {
        return localIs_requiredTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_required() {
        return localIs_required;
    }

    /**
     * Auto generated setter method
     * @param param Is_required
     */
    public void setIs_required(int param) {
        // setting primitive attribute tracker to true
        localIs_requiredTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_required = param;
    }

    public boolean isApply_toSpecified() {
        return localApply_toTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getApply_to() {
        return localApply_to;
    }

    /**
     * Auto generated setter method
     * @param param Apply_to
     */
    public void setApply_to(magento.ArrayOfString param) {
        localApply_toTracker = param != null;

        this.localApply_to = param;
    }

    public boolean isIs_configurableSpecified() {
        return localIs_configurableTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_configurable() {
        return localIs_configurable;
    }

    /**
     * Auto generated setter method
     * @param param Is_configurable
     */
    public void setIs_configurable(int param) {
        // setting primitive attribute tracker to true
        localIs_configurableTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_configurable = param;
    }

    public boolean isIs_searchableSpecified() {
        return localIs_searchableTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_searchable() {
        return localIs_searchable;
    }

    /**
     * Auto generated setter method
     * @param param Is_searchable
     */
    public void setIs_searchable(int param) {
        // setting primitive attribute tracker to true
        localIs_searchableTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_searchable = param;
    }

    public boolean isIs_visible_in_advanced_searchSpecified() {
        return localIs_visible_in_advanced_searchTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_visible_in_advanced_search() {
        return localIs_visible_in_advanced_search;
    }

    /**
     * Auto generated setter method
     * @param param Is_visible_in_advanced_search
     */
    public void setIs_visible_in_advanced_search(int param) {
        // setting primitive attribute tracker to true
        localIs_visible_in_advanced_searchTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_visible_in_advanced_search = param;
    }

    public boolean isIs_comparableSpecified() {
        return localIs_comparableTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_comparable() {
        return localIs_comparable;
    }

    /**
     * Auto generated setter method
     * @param param Is_comparable
     */
    public void setIs_comparable(int param) {
        // setting primitive attribute tracker to true
        localIs_comparableTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_comparable = param;
    }

    public boolean isIs_used_for_promo_rulesSpecified() {
        return localIs_used_for_promo_rulesTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_used_for_promo_rules() {
        return localIs_used_for_promo_rules;
    }

    /**
     * Auto generated setter method
     * @param param Is_used_for_promo_rules
     */
    public void setIs_used_for_promo_rules(int param) {
        // setting primitive attribute tracker to true
        localIs_used_for_promo_rulesTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_used_for_promo_rules = param;
    }

    public boolean isIs_visible_on_frontSpecified() {
        return localIs_visible_on_frontTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_visible_on_front() {
        return localIs_visible_on_front;
    }

    /**
     * Auto generated setter method
     * @param param Is_visible_on_front
     */
    public void setIs_visible_on_front(int param) {
        // setting primitive attribute tracker to true
        localIs_visible_on_frontTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_visible_on_front = param;
    }

    public boolean isUsed_in_product_listingSpecified() {
        return localUsed_in_product_listingTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUsed_in_product_listing() {
        return localUsed_in_product_listing;
    }

    /**
     * Auto generated setter method
     * @param param Used_in_product_listing
     */
    public void setUsed_in_product_listing(int param) {
        // setting primitive attribute tracker to true
        localUsed_in_product_listingTracker = param != java.lang.Integer.MIN_VALUE;

        this.localUsed_in_product_listing = param;
    }

    public boolean isAdditional_fieldsSpecified() {
        return localAdditional_fieldsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.AssociativeArray
     */
    public magento.AssociativeArray getAdditional_fields() {
        return localAdditional_fields;
    }

    /**
     * Auto generated setter method
     * @param param Additional_fields
     */
    public void setAdditional_fields(magento.AssociativeArray param) {
        localAdditional_fieldsTracker = param != null;

        this.localAdditional_fields = param;
    }

    public boolean isOptionsSpecified() {
        return localOptionsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogAttributeOptionEntityArray
     */
    public magento.CatalogAttributeOptionEntityArray getOptions() {
        return localOptions;
    }

    /**
     * Auto generated setter method
     * @param param Options
     */
    public void setOptions(magento.CatalogAttributeOptionEntityArray param) {
        localOptionsTracker = param != null;

        this.localOptions = param;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductAttributeFrontendLabelArray
     */
    public magento.CatalogProductAttributeFrontendLabelArray getFrontend_label() {
        return localFrontend_label;
    }

    /**
     * Auto generated setter method
     * @param param Frontend_label
     */
    public void setFrontend_label(
        magento.CatalogProductAttributeFrontendLabelArray param) {
        this.localFrontend_label = param;
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
                    namespacePrefix + ":catalogProductAttributeEntity",
                    xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductAttributeEntity", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "attribute_id", xmlWriter);

        if (localAttribute_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "attribute_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localAttribute_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "attribute_code", xmlWriter);

        if (localAttribute_code == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "attribute_code cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localAttribute_code);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "frontend_input", xmlWriter);

        if (localFrontend_input == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "frontend_input cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localFrontend_input);
        }

        xmlWriter.writeEndElement();

        if (localScopeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "scope", xmlWriter);

            if (localScope == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "scope cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localScope);
            }

            xmlWriter.writeEndElement();
        }

        if (localDefault_valueTracker) {
            namespace = "";
            writeStartElement(null, namespace, "default_value", xmlWriter);

            if (localDefault_value == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "default_value cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDefault_value);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_uniqueTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_unique", xmlWriter);

            if (localIs_unique == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_unique cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_unique));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_requiredTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_required", xmlWriter);

            if (localIs_required == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_required cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_required));
            }

            xmlWriter.writeEndElement();
        }

        if (localApply_toTracker) {
            if (localApply_to == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "apply_to cannot be null!!");
            }

            localApply_to.serialize(new javax.xml.namespace.QName("", "apply_to"),
                xmlWriter);
        }

        if (localIs_configurableTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_configurable", xmlWriter);

            if (localIs_configurable == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_configurable cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_configurable));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_searchableTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_searchable", xmlWriter);

            if (localIs_searchable == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_searchable cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_searchable));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_visible_in_advanced_searchTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_visible_in_advanced_search",
                xmlWriter);

            if (localIs_visible_in_advanced_search == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_visible_in_advanced_search cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_visible_in_advanced_search));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_comparableTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_comparable", xmlWriter);

            if (localIs_comparable == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_comparable cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_comparable));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_used_for_promo_rulesTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_used_for_promo_rules",
                xmlWriter);

            if (localIs_used_for_promo_rules == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_used_for_promo_rules cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_used_for_promo_rules));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_visible_on_frontTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_visible_on_front", xmlWriter);

            if (localIs_visible_on_front == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_visible_on_front cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_visible_on_front));
            }

            xmlWriter.writeEndElement();
        }

        if (localUsed_in_product_listingTracker) {
            namespace = "";
            writeStartElement(null, namespace, "used_in_product_listing",
                xmlWriter);

            if (localUsed_in_product_listing == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "used_in_product_listing cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUsed_in_product_listing));
            }

            xmlWriter.writeEndElement();
        }

        if (localAdditional_fieldsTracker) {
            if (localAdditional_fields == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_fields cannot be null!!");
            }

            localAdditional_fields.serialize(new javax.xml.namespace.QName("",
                    "additional_fields"), xmlWriter);
        }

        if (localOptionsTracker) {
            if (localOptions == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "options cannot be null!!");
            }

            localOptions.serialize(new javax.xml.namespace.QName("", "options"),
                xmlWriter);
        }

        if (localFrontend_label == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "frontend_label cannot be null!!");
        }

        localFrontend_label.serialize(new javax.xml.namespace.QName("",
                "frontend_label"), xmlWriter);

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

        elementList.add(new javax.xml.namespace.QName("", "attribute_id"));

        if (localAttribute_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localAttribute_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "attribute_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "attribute_code"));

        if (localAttribute_code != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localAttribute_code));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "attribute_code cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "frontend_input"));

        if (localFrontend_input != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localFrontend_input));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "frontend_input cannot be null!!");
        }

        if (localScopeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "scope"));

            if (localScope != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localScope));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "scope cannot be null!!");
            }
        }

        if (localDefault_valueTracker) {
            elementList.add(new javax.xml.namespace.QName("", "default_value"));

            if (localDefault_value != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDefault_value));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "default_value cannot be null!!");
            }
        }

        if (localIs_uniqueTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_unique"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_unique));
        }

        if (localIs_requiredTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_required"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_required));
        }

        if (localApply_toTracker) {
            elementList.add(new javax.xml.namespace.QName("", "apply_to"));

            if (localApply_to == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "apply_to cannot be null!!");
            }

            elementList.add(localApply_to);
        }

        if (localIs_configurableTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_configurable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_configurable));
        }

        if (localIs_searchableTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_searchable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_searchable));
        }

        if (localIs_visible_in_advanced_searchTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "is_visible_in_advanced_search"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_visible_in_advanced_search));
        }

        if (localIs_comparableTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_comparable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_comparable));
        }

        if (localIs_used_for_promo_rulesTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "is_used_for_promo_rules"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_used_for_promo_rules));
        }

        if (localIs_visible_on_frontTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "is_visible_on_front"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_visible_on_front));
        }

        if (localUsed_in_product_listingTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "used_in_product_listing"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUsed_in_product_listing));
        }

        if (localAdditional_fieldsTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "additional_fields"));

            if (localAdditional_fields == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_fields cannot be null!!");
            }

            elementList.add(localAdditional_fields);
        }

        if (localOptionsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "options"));

            if (localOptions == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "options cannot be null!!");
            }

            elementList.add(localOptions);
        }

        elementList.add(new javax.xml.namespace.QName("", "frontend_label"));

        if (localFrontend_label == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "frontend_label cannot be null!!");
        }

        elementList.add(localFrontend_label);

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
        public static CatalogProductAttributeEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CatalogProductAttributeEntity object = new CatalogProductAttributeEntity();

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

                        if (!"catalogProductAttributeEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductAttributeEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "attribute_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "attribute_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAttribute_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "attribute_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "attribute_code" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAttribute_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "frontend_input").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "frontend_input" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFrontend_input(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "scope").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "scope" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setScope(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "default_value").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "default_value" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefault_value(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_unique").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_unique" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_unique(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_unique(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_required").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_required" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_required(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_required(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "apply_to").equals(
                            reader.getName())) {
                    object.setApply_to(magento.ArrayOfString.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_configurable").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_configurable" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_configurable(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_configurable(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_searchable").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_searchable" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_searchable(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_searchable(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "is_visible_in_advanced_search").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_visible_in_advanced_search" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_visible_in_advanced_search(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_visible_in_advanced_search(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_comparable").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_comparable" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_comparable(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_comparable(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "is_used_for_promo_rules").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_used_for_promo_rules" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_used_for_promo_rules(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_used_for_promo_rules(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_visible_on_front").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_visible_on_front" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_visible_on_front(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_visible_on_front(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "used_in_product_listing").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "used_in_product_listing" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setUsed_in_product_listing(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUsed_in_product_listing(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "additional_fields").equals(
                            reader.getName())) {
                    object.setAdditional_fields(magento.AssociativeArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "options").equals(
                            reader.getName())) {
                    object.setOptions(magento.CatalogAttributeOptionEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "frontend_label").equals(
                            reader.getName())) {
                    object.setFrontend_label(magento.CatalogProductAttributeFrontendLabelArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
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
