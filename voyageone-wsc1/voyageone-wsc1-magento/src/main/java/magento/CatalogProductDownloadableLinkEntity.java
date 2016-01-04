/**
 * CatalogProductDownloadableLinkEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductDownloadableLinkEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductDownloadableLinkEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductDownloadableLinkEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Link_id
     */
    protected String localLink_id;

    /**
     * field for Title
     */
    protected String localTitle;

    /**
     * field for Price
     */
    protected String localPrice;

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
     * field for Is_unlimited
     */
    protected int localIs_unlimited;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_unlimitedTracker = false;

    /**
     * field for Is_shareable
     */
    protected int localIs_shareable;

    /**
     * field for Link_url
     */
    protected String localLink_url;

    /**
     * field for Link_type
     */
    protected String localLink_type;

    /**
     * field for Sample_file
     */
    protected String localSample_file;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSample_fileTracker = false;

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
     * field for Sample_type
     */
    protected String localSample_type;

    /**
     * field for Sort_order
     */
    protected int localSort_order;

    /**
     * field for File_save
     */
    protected magento.CatalogProductDownloadableLinkFileInfoEntityArray localFile_save;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFile_saveTracker = false;

    /**
     * field for Sample_file_save
     */
    protected magento.CatalogProductDownloadableLinkFileInfoEntityArray localSample_file_save;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSample_file_saveTracker = false;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getLink_id() {
        return localLink_id;
    }

    /**
     * Auto generated setter method
     * @param param Link_id
     */
    public void setLink_id(String param) {
        this.localLink_id = param;
    }

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
        this.localPrice = param;
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
        this.localIs_shareable = param;
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
        this.localLink_url = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getLink_type() {
        return localLink_type;
    }

    /**
     * Auto generated setter method
     * @param param Link_type
     */
    public void setLink_type(String param) {
        this.localLink_type = param;
    }

    public boolean isSample_fileSpecified() {
        return localSample_fileTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSample_file() {
        return localSample_file;
    }

    /**
     * Auto generated setter method
     * @param param Sample_file
     */
    public void setSample_file(String param) {
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

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSample_type() {
        return localSample_type;
    }

    /**
     * Auto generated setter method
     * @param param Sample_type
     */
    public void setSample_type(String param) {
        this.localSample_type = param;
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
        this.localSort_order = param;
    }

    public boolean isFile_saveSpecified() {
        return localFile_saveTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductDownloadableLinkFileInfoEntityArray
     */
    public magento.CatalogProductDownloadableLinkFileInfoEntityArray getFile_save() {
        return localFile_save;
    }

    /**
     * Auto generated setter method
     * @param param File_save
     */
    public void setFile_save(
        magento.CatalogProductDownloadableLinkFileInfoEntityArray param) {
        localFile_saveTracker = param != null;

        this.localFile_save = param;
    }

    public boolean isSample_file_saveSpecified() {
        return localSample_file_saveTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductDownloadableLinkFileInfoEntityArray
     */
    public magento.CatalogProductDownloadableLinkFileInfoEntityArray getSample_file_save() {
        return localSample_file_save;
    }

    /**
     * Auto generated setter method
     * @param param Sample_file_save
     */
    public void setSample_file_save(
        magento.CatalogProductDownloadableLinkFileInfoEntityArray param) {
        localSample_file_saveTracker = param != null;

        this.localSample_file_save = param;
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
                    namespacePrefix + ":catalogProductDownloadableLinkEntity",
                    xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductDownloadableLinkEntity", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "link_id", xmlWriter);

        if (localLink_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "link_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localLink_id);
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

        namespace = "";
        writeStartElement(null, namespace, "link_type", xmlWriter);

        if (localLink_type == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "link_type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localLink_type);
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

        if (localSort_order == Integer.MIN_VALUE) {
            throw new org.apache.axis2.databinding.ADBException(
                "sort_order cannot be null!!");
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSort_order));
        }

        xmlWriter.writeEndElement();

        if (localFile_saveTracker) {
            if (localFile_save == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "file_save cannot be null!!");
            }

            localFile_save.serialize(new javax.xml.namespace.QName("",
                    "file_save"), xmlWriter);
        }

        if (localSample_file_saveTracker) {
            if (localSample_file_save == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "sample_file_save cannot be null!!");
            }

            localSample_file_save.serialize(new javax.xml.namespace.QName("",
                    "sample_file_save"), xmlWriter);
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

        elementList.add(new javax.xml.namespace.QName("", "link_id"));

        if (localLink_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localLink_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "link_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "title"));

        if (localTitle != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localTitle));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "title cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "price"));

        if (localPrice != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localPrice));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "price cannot be null!!");
        }

        if (localNumber_of_downloadsTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "number_of_downloads"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localNumber_of_downloads));
        }

        if (localIs_unlimitedTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_unlimited"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_unlimited));
        }

        elementList.add(new javax.xml.namespace.QName("", "is_shareable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                localIs_shareable));

        elementList.add(new javax.xml.namespace.QName("", "link_url"));

        if (localLink_url != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localLink_url));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "link_url cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "link_type"));

        if (localLink_type != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localLink_type));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "link_type cannot be null!!");
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

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                localSort_order));

        if (localFile_saveTracker) {
            elementList.add(new javax.xml.namespace.QName("", "file_save"));

            if (localFile_save == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "file_save cannot be null!!");
            }

            elementList.add(localFile_save);
        }

        if (localSample_file_saveTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sample_file_save"));

            if (localSample_file_save == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "sample_file_save cannot be null!!");
            }

            elementList.add(localSample_file_save);
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
        public static CatalogProductDownloadableLinkEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            CatalogProductDownloadableLinkEntity object = new CatalogProductDownloadableLinkEntity();

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

                        if (!"catalogProductDownloadableLinkEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductDownloadableLinkEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "link_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "link_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setLink_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "link_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "link_type" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setLink_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    String content = reader.getElementText();

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

                    String content = reader.getElementText();

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

                    String content = reader.getElementText();

                    object.setSort_order(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
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
                        new javax.xml.namespace.QName("", "file_save").equals(
                            reader.getName())) {
                    object.setFile_save(magento.CatalogProductDownloadableLinkFileInfoEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sample_file_save").equals(
                            reader.getName())) {
                    object.setSample_file_save(magento.CatalogProductDownloadableLinkFileInfoEntityArray.Factory.parse(
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
                throw new Exception(e);
            }

            return object;
        }
    } //end of factory class
}
