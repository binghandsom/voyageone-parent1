/**
 * CatalogProductCustomOptionValueInfoEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductCustomOptionValueInfoEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductCustomOptionValueInfoEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductCustomOptionValueInfoEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Value_id
     */
    protected java.lang.String localValue_id;

    /**
     * field for Option_id
     */
    protected java.lang.String localOption_id;

    /**
     * field for Sku
     */
    protected java.lang.String localSku;

    /**
     * field for Sort_order
     */
    protected java.lang.String localSort_order;

    /**
     * field for Default_price
     */
    protected java.lang.String localDefault_price;

    /**
     * field for Default_price_type
     */
    protected java.lang.String localDefault_price_type;

    /**
     * field for Store_price
     */
    protected java.lang.String localStore_price;

    /**
     * field for Store_price_type
     */
    protected java.lang.String localStore_price_type;

    /**
     * field for Price
     */
    protected java.lang.String localPrice;

    /**
     * field for Price_type
     */
    protected java.lang.String localPrice_type;

    /**
     * field for Default_title
     */
    protected java.lang.String localDefault_title;

    /**
     * field for Store_title
     */
    protected java.lang.String localStore_title;

    /**
     * field for Title
     */
    protected java.lang.String localTitle;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getValue_id() {
        return localValue_id;
    }

    /**
     * Auto generated setter method
     * @param param Value_id
     */
    public void setValue_id(java.lang.String param) {
        this.localValue_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOption_id() {
        return localOption_id;
    }

    /**
     * Auto generated setter method
     * @param param Option_id
     */
    public void setOption_id(java.lang.String param) {
        this.localOption_id = param;
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
        this.localSku = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSort_order() {
        return localSort_order;
    }

    /**
     * Auto generated setter method
     * @param param Sort_order
     */
    public void setSort_order(java.lang.String param) {
        this.localSort_order = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDefault_price() {
        return localDefault_price;
    }

    /**
     * Auto generated setter method
     * @param param Default_price
     */
    public void setDefault_price(java.lang.String param) {
        this.localDefault_price = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDefault_price_type() {
        return localDefault_price_type;
    }

    /**
     * Auto generated setter method
     * @param param Default_price_type
     */
    public void setDefault_price_type(java.lang.String param) {
        this.localDefault_price_type = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStore_price() {
        return localStore_price;
    }

    /**
     * Auto generated setter method
     * @param param Store_price
     */
    public void setStore_price(java.lang.String param) {
        this.localStore_price = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStore_price_type() {
        return localStore_price_type;
    }

    /**
     * Auto generated setter method
     * @param param Store_price_type
     */
    public void setStore_price_type(java.lang.String param) {
        this.localStore_price_type = param;
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
        this.localPrice = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPrice_type() {
        return localPrice_type;
    }

    /**
     * Auto generated setter method
     * @param param Price_type
     */
    public void setPrice_type(java.lang.String param) {
        this.localPrice_type = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDefault_title() {
        return localDefault_title;
    }

    /**
     * Auto generated setter method
     * @param param Default_title
     */
    public void setDefault_title(java.lang.String param) {
        this.localDefault_title = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStore_title() {
        return localStore_title;
    }

    /**
     * Auto generated setter method
     * @param param Store_title
     */
    public void setStore_title(java.lang.String param) {
        this.localStore_title = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getTitle() {
        return localTitle;
    }

    /**
     * Auto generated setter method
     * @param param Title
     */
    public void setTitle(java.lang.String param) {
        this.localTitle = param;
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
                    namespacePrefix +
                    ":catalogProductCustomOptionValueInfoEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductCustomOptionValueInfoEntity", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "value_id", xmlWriter);

        if (localValue_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "value_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localValue_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "option_id", xmlWriter);

        if (localOption_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "option_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localOption_id);
        }

        xmlWriter.writeEndElement();

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

        namespace = "";
        writeStartElement(null, namespace, "sort_order", xmlWriter);

        if (localSort_order == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "sort_order cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSort_order);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "default_price", xmlWriter);

        if (localDefault_price == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "default_price cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDefault_price);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "default_price_type", xmlWriter);

        if (localDefault_price_type == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "default_price_type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDefault_price_type);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "store_price", xmlWriter);

        if (localStore_price == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "store_price cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localStore_price);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "store_price_type", xmlWriter);

        if (localStore_price_type == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "store_price_type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localStore_price_type);
        }

        xmlWriter.writeEndElement();

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

        namespace = "";
        writeStartElement(null, namespace, "price_type", xmlWriter);

        if (localPrice_type == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "price_type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localPrice_type);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "default_title", xmlWriter);

        if (localDefault_title == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "default_title cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDefault_title);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "store_title", xmlWriter);

        if (localStore_title == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "store_title cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localStore_title);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "title", xmlWriter);

        if (localTitle == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "title cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localTitle);
        }

        xmlWriter.writeEndElement();

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

        elementList.add(new javax.xml.namespace.QName("", "value_id"));

        if (localValue_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localValue_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "value_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "option_id"));

        if (localOption_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localOption_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "option_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "sku"));

        if (localSku != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSku));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "sku cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "sort_order"));

        if (localSort_order != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSort_order));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "sort_order cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "default_price"));

        if (localDefault_price != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localDefault_price));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "default_price cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "default_price_type"));

        if (localDefault_price_type != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localDefault_price_type));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "default_price_type cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "store_price"));

        if (localStore_price != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localStore_price));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "store_price cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "store_price_type"));

        if (localStore_price_type != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localStore_price_type));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "store_price_type cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "price"));

        if (localPrice != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localPrice));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "price cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "price_type"));

        if (localPrice_type != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localPrice_type));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "price_type cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "default_title"));

        if (localDefault_title != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localDefault_title));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "default_title cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "store_title"));

        if (localStore_title != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localStore_title));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "store_title cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "title"));

        if (localTitle != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localTitle));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "title cannot be null!!");
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
        public static CatalogProductCustomOptionValueInfoEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CatalogProductCustomOptionValueInfoEntity object = new CatalogProductCustomOptionValueInfoEntity();

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

                        if (!"catalogProductCustomOptionValueInfoEntity".equals(
                                    type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductCustomOptionValueInfoEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "value_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "value_id" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setValue_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "option_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "option_id" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOption_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sort_order").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sort_order" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSort_order(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "default_price").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "default_price" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefault_price(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "default_price_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "default_price_type" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefault_price_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "store_price").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_price" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setStore_price(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "store_price_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_price_type" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setStore_price_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "price_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "price_type" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPrice_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "default_title").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "default_title" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefault_title(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "store_title").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_title" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setStore_title(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "title").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "title" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTitle(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
