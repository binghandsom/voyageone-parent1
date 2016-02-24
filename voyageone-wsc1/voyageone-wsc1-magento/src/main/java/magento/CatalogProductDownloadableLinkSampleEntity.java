/**
 * CatalogProductDownloadableLinkSampleEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductDownloadableLinkSampleEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductDownloadableLinkSampleEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductDownloadableLinkSampleEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Sample_id
     */
    protected java.lang.String localSample_id;

    /**
     * field for Product_id
     */
    protected java.lang.String localProduct_id;

    /**
     * field for Sample_file
     */
    protected java.lang.String localSample_file;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSample_fileTracker = false;

    /**
     * field for Sample_url
     */
    protected java.lang.String localSample_url;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSample_urlTracker = false;

    /**
     * field for Sample_type
     */
    protected java.lang.String localSample_type;

    /**
     * field for Sort_order
     */
    protected java.lang.String localSort_order;

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
    public java.lang.String getSample_id() {
        return localSample_id;
    }

    /**
     * Auto generated setter method
     * @param param Sample_id
     */
    public void setSample_id(java.lang.String param) {
        this.localSample_id = param;
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
        this.localProduct_id = param;
    }

    public boolean isSample_fileSpecified() {
        return localSample_fileTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSample_file() {
        return localSample_file;
    }

    /**
     * Auto generated setter method
     * @param param Sample_file
     */
    public void setSample_file(java.lang.String param) {
        localSample_fileTracker = param != null;

        this.localSample_file = param;
    }

    public boolean isSample_urlSpecified() {
        return localSample_urlTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSample_url() {
        return localSample_url;
    }

    /**
     * Auto generated setter method
     * @param param Sample_url
     */
    public void setSample_url(java.lang.String param) {
        localSample_urlTracker = param != null;

        this.localSample_url = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSample_type() {
        return localSample_type;
    }

    /**
     * Auto generated setter method
     * @param param Sample_type
     */
    public void setSample_type(java.lang.String param) {
        this.localSample_type = param;
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
                    ":catalogProductDownloadableLinkSampleEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductDownloadableLinkSampleEntity", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "sample_id", xmlWriter);

        if (localSample_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "sample_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSample_id);
        }

        xmlWriter.writeEndElement();

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

        if (localSample_fileTracker) {
            namespace = "";
            writeStartElement(null, namespace, "sample_file", xmlWriter);

            if (localSample_file == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "sample_file cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSample_file);
            }

            xmlWriter.writeEndElement();
        }

        if (localSample_urlTracker) {
            namespace = "";
            writeStartElement(null, namespace, "sample_url", xmlWriter);

            if (localSample_url == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "sample_url cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSample_url);
            }

            xmlWriter.writeEndElement();
        }

        namespace = "";
        writeStartElement(null, namespace, "sample_type", xmlWriter);

        if (localSample_type == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "sample_type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSample_type);
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

        elementList.add(new javax.xml.namespace.QName("", "sample_id"));

        if (localSample_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSample_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "sample_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "product_id"));

        if (localProduct_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localProduct_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "product_id cannot be null!!");
        }

        if (localSample_fileTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sample_file"));

            if (localSample_file != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSample_file));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "sample_file cannot be null!!");
            }
        }

        if (localSample_urlTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sample_url"));

            if (localSample_url != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSample_url));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "sample_url cannot be null!!");
            }
        }

        elementList.add(new javax.xml.namespace.QName("", "sample_type"));

        if (localSample_type != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSample_type));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "sample_type cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "sort_order"));

        if (localSort_order != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSort_order));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "sort_order cannot be null!!");
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
        public static CatalogProductDownloadableLinkSampleEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CatalogProductDownloadableLinkSampleEntity object = new CatalogProductDownloadableLinkSampleEntity();

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

                        if (!"catalogProductDownloadableLinkSampleEntity".equals(
                                    type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductDownloadableLinkSampleEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "sample_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sample_id" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSample_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sample_file").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sample_file" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSample_file(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sample_url").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sample_url" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSample_url(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sample_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sample_type" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSample_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
