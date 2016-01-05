/**
 * CatalogProductDownloadableLinkAddEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductDownloadableLinkAddEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductDownloadableLinkAddEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductDownloadableLinkAddEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Title
     */
    protected String localTitle;

    /**
     * field for Price
     */
    protected String localPrice;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPriceTracker = false;

    /**
     * field for Is_unlimited
     */
    protected int localIs_unlimited;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_unlimitedTracker = false;

    /**
     * field for Number_of_downloads
     */
    protected int localNumber_of_downloads;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localNumber_of_downloadsTracker = false;

    /**
     * field for Is_shareable
     */
    protected int localIs_shareable;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_shareableTracker = false;

    /**
     * field for Sample
     */
    protected magento.CatalogProductDownloadableLinkAddSampleEntity localSample;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSampleTracker = false;

    /**
     * field for Type
     */
    protected String localType;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTypeTracker = false;

    /**
     * field for File
     */
    protected magento.CatalogProductDownloadableLinkFileEntity localFile;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFileTracker = false;

    /**
     * field for Link_url
     */
    protected String localLink_url;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localLink_urlTracker = false;

    /**
     * field for Sample_url
     */
    protected String localSample_url;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSample_urlTracker = false;

    /**
     * field for Sort_order
     */
    protected int localSort_order;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSort_orderTracker = false;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTitle() {
        return localTitle;
    }

    /**
     * Auto generated setter method
     * @param param Title
     */
    public void setTitle(String param) {
        this.localTitle = param;
    }

    public boolean isPriceSpecified() {
        return localPriceTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPrice() {
        return localPrice;
    }

    /**
     * Auto generated setter method
     * @param param Price
     */
    public void setPrice(String param) {
        localPriceTracker = param != null;

        this.localPrice = param;
    }

    public boolean isIs_unlimitedSpecified() {
        return localIs_unlimitedTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_unlimited() {
        return localIs_unlimited;
    }

    /**
     * Auto generated setter method
     * @param param Is_unlimited
     */
    public void setIs_unlimited(int param) {
        // setting primitive attribute tracker to true
        localIs_unlimitedTracker = param != Integer.MIN_VALUE;

        this.localIs_unlimited = param;
    }

    public boolean isNumber_of_downloadsSpecified() {
        return localNumber_of_downloadsTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getNumber_of_downloads() {
        return localNumber_of_downloads;
    }

    /**
     * Auto generated setter method
     * @param param Number_of_downloads
     */
    public void setNumber_of_downloads(int param) {
        // setting primitive attribute tracker to true
        localNumber_of_downloadsTracker = param != Integer.MIN_VALUE;

        this.localNumber_of_downloads = param;
    }

    public boolean isIs_shareableSpecified() {
        return localIs_shareableTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_shareable() {
        return localIs_shareable;
    }

    /**
     * Auto generated setter method
     * @param param Is_shareable
     */
    public void setIs_shareable(int param) {
        // setting primitive attribute tracker to true
        localIs_shareableTracker = param != Integer.MIN_VALUE;

        this.localIs_shareable = param;
    }

    public boolean isSampleSpecified() {
        return localSampleTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductDownloadableLinkAddSampleEntity
     */
    public magento.CatalogProductDownloadableLinkAddSampleEntity getSample() {
        return localSample;
    }

    /**
     * Auto generated setter method
     * @param param Sample
     */
    public void setSample(
        magento.CatalogProductDownloadableLinkAddSampleEntity param) {
        localSampleTracker = param != null;

        this.localSample = param;
    }

    public boolean isTypeSpecified() {
        return localTypeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * @param param Type
     */
    public void setType(String param) {
        localTypeTracker = param != null;

        this.localType = param;
    }

    public boolean isFileSpecified() {
        return localFileTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductDownloadableLinkFileEntity
     */
    public magento.CatalogProductDownloadableLinkFileEntity getFile() {
        return localFile;
    }

    /**
     * Auto generated setter method
     * @param param File
     */
    public void setFile(magento.CatalogProductDownloadableLinkFileEntity param) {
        localFileTracker = param != null;

        this.localFile = param;
    }

    public boolean isLink_urlSpecified() {
        return localLink_urlTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getLink_url() {
        return localLink_url;
    }

    /**
     * Auto generated setter method
     * @param param Link_url
     */
    public void setLink_url(String param) {
        localLink_urlTracker = param != null;

        this.localLink_url = param;
    }

    public boolean isSample_urlSpecified() {
        return localSample_urlTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSample_url() {
        return localSample_url;
    }

    /**
     * Auto generated setter method
     * @param param Sample_url
     */
    public void setSample_url(String param) {
        localSample_urlTracker = param != null;

        this.localSample_url = param;
    }

    public boolean isSort_orderSpecified() {
        return localSort_orderTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getSort_order() {
        return localSort_order;
    }

    /**
     * Auto generated setter method
     * @param param Sort_order
     */
    public void setSort_order(int param) {
        // setting primitive attribute tracker to true
        localSort_orderTracker = param != Integer.MIN_VALUE;

        this.localSort_order = param;
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
        String prefix = null;
        String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            String namespacePrefix = registerPrefix(xmlWriter,
                    "urn:Magento");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix +
                    ":catalogProductDownloadableLinkAddEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductDownloadableLinkAddEntity", xmlWriter);
            }
        }

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

        if (localIs_unlimitedTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_unlimited", xmlWriter);

            if (localIs_unlimited == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_unlimited cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_unlimited));
            }

            xmlWriter.writeEndElement();
        }

        if (localNumber_of_downloadsTracker) {
            namespace = "";
            writeStartElement(null, namespace, "number_of_downloads", xmlWriter);

            if (localNumber_of_downloads == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "number_of_downloads cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localNumber_of_downloads));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_shareableTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_shareable", xmlWriter);

            if (localIs_shareable == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_shareable cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_shareable));
            }

            xmlWriter.writeEndElement();
        }

        if (localSampleTracker) {
            if (localSample == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "sample cannot be null!!");
            }

            localSample.serialize(new javax.xml.namespace.QName("", "sample"),
                xmlWriter);
        }

        if (localTypeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "type", xmlWriter);

            if (localType == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "type cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localType);
            }

            xmlWriter.writeEndElement();
        }

        if (localFileTracker) {
            if (localFile == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "file cannot be null!!");
            }

            localFile.serialize(new javax.xml.namespace.QName("", "file"),
                xmlWriter);
        }

        if (localLink_urlTracker) {
            namespace = "";
            writeStartElement(null, namespace, "link_url", xmlWriter);

            if (localLink_url == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "link_url cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localLink_url);
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

        if (localSort_orderTracker) {
            namespace = "";
            writeStartElement(null, namespace, "sort_order", xmlWriter);

            if (localSort_order == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "sort_order cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSort_order));
            }

            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement();
    }

    private static String generatePrefix(String namespace) {
        if (namespace.equals("urn:Magento")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(String prefix,
        String namespace, String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String writerPrefix = xmlWriter.getPrefix(namespace);

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
    private void writeAttribute(String prefix,
        String namespace, String attName,
        String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
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
    private void writeAttribute(String namespace,
        String attName, String attValue,
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
    private void writeQNameAttribute(String namespace,
        String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String attributeNamespace = qname.getNamespaceURI();
        String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        String attributeValue;

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
        String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            String prefix = xmlWriter.getPrefix(namespaceURI);

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
            StringBuffer stringToWrite = new StringBuffer();
            String namespaceURI = null;
            String prefix = null;

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
    private String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
        throws javax.xml.stream.XMLStreamException {
        String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                String uri = nsContext.getNamespaceURI(prefix);

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

        elementList.add(new javax.xml.namespace.QName("", "title"));

        if (localTitle != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localTitle));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "title cannot be null!!");
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

        if (localIs_unlimitedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_unlimited"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_unlimited));
        }

        if (localNumber_of_downloadsTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "number_of_downloads"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localNumber_of_downloads));
        }

        if (localIs_shareableTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_shareable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_shareable));
        }

        if (localSampleTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sample"));

            if (localSample == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "sample cannot be null!!");
            }

            elementList.add(localSample);
        }

        if (localTypeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "type"));

            if (localType != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localType));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "type cannot be null!!");
            }
        }

        if (localFileTracker) {
            elementList.add(new javax.xml.namespace.QName("", "file"));

            if (localFile == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "file cannot be null!!");
            }

            elementList.add(localFile);
        }

        if (localLink_urlTracker) {
            elementList.add(new javax.xml.namespace.QName("", "link_url"));

            if (localLink_url != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localLink_url));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "link_url cannot be null!!");
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

        if (localSort_orderTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sort_order"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSort_order));
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
        public static CatalogProductDownloadableLinkAddEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            CatalogProductDownloadableLinkAddEntity object = new CatalogProductDownloadableLinkAddEntity();

            int event;
            String nillableValue = null;
            String prefix = "";
            String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"catalogProductDownloadableLinkAddEntity".equals(
                                    type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductDownloadableLinkAddEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "title").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "title" + "  cannot be null");
                    }

                    String content = reader.getElementText();

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

                    String content = reader.getElementText();

                    object.setPrice(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_unlimited").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_unlimited" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_unlimited(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_unlimited(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "number_of_downloads").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "number_of_downloads" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setNumber_of_downloads(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setNumber_of_downloads(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_shareable").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_shareable" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_shareable(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_shareable(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sample").equals(
                            reader.getName())) {
                    object.setSample(magento.CatalogProductDownloadableLinkAddSampleEntity.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "type" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "file").equals(
                            reader.getName())) {
                    object.setFile(magento.CatalogProductDownloadableLinkFileEntity.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "link_url").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "link_url" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setLink_url(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    String content = reader.getElementText();

                    object.setSample_url(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                    String content = reader.getElementText();

                    object.setSort_order(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setSort_order(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }
    } //end of factory class
}
