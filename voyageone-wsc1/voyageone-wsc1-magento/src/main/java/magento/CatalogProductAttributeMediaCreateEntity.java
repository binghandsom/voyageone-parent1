/**
 * CatalogProductAttributeMediaCreateEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductAttributeMediaCreateEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductAttributeMediaCreateEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductAttributeMediaCreateEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for File
     */
    protected magento.CatalogProductImageFileEntity localFile;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFileTracker = false;

    /**
     * field for Label
     */
    protected String localLabel;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localLabelTracker = false;

    /**
     * field for Position
     */
    protected String localPosition;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPositionTracker = false;

    /**
     * field for Types
     */
    protected magento.ArrayOfString localTypes;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTypesTracker = false;

    /**
     * field for Exclude
     */
    protected String localExclude;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localExcludeTracker = false;

    /**
     * field for Remove
     */
    protected String localRemove;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRemoveTracker = false;

    public boolean isFileSpecified() {
        return localFileTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.CatalogProductImageFileEntity
     */
    public magento.CatalogProductImageFileEntity getFile() {
        return localFile;
    }

    /**
     * Auto generated setter method
     * @param param File
     */
    public void setFile(magento.CatalogProductImageFileEntity param) {
        localFileTracker = param != null;

        this.localFile = param;
    }

    public boolean isLabelSpecified() {
        return localLabelTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getLabel() {
        return localLabel;
    }

    /**
     * Auto generated setter method
     * @param param Label
     */
    public void setLabel(String param) {
        localLabelTracker = param != null;

        this.localLabel = param;
    }

    public boolean isPositionSpecified() {
        return localPositionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPosition() {
        return localPosition;
    }

    /**
     * Auto generated setter method
     * @param param Position
     */
    public void setPosition(String param) {
        localPositionTracker = param != null;

        this.localPosition = param;
    }

    public boolean isTypesSpecified() {
        return localTypesTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getTypes() {
        return localTypes;
    }

    /**
     * Auto generated setter method
     * @param param Types
     */
    public void setTypes(magento.ArrayOfString param) {
        localTypesTracker = param != null;

        this.localTypes = param;
    }

    public boolean isExcludeSpecified() {
        return localExcludeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getExclude() {
        return localExclude;
    }

    /**
     * Auto generated setter method
     * @param param Exclude
     */
    public void setExclude(String param) {
        localExcludeTracker = param != null;

        this.localExclude = param;
    }

    public boolean isRemoveSpecified() {
        return localRemoveTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getRemove() {
        return localRemove;
    }

    /**
     * Auto generated setter method
     * @param param Remove
     */
    public void setRemove(String param) {
        localRemoveTracker = param != null;

        this.localRemove = param;
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
                    ":catalogProductAttributeMediaCreateEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductAttributeMediaCreateEntity", xmlWriter);
            }
        }

        if (localFileTracker) {
            if (localFile == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "file cannot be null!!");
            }

            localFile.serialize(new javax.xml.namespace.QName("", "file"),
                xmlWriter);
        }

        if (localLabelTracker) {
            namespace = "";
            writeStartElement(null, namespace, "label", xmlWriter);

            if (localLabel == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "label cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localLabel);
            }

            xmlWriter.writeEndElement();
        }

        if (localPositionTracker) {
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
        }

        if (localTypesTracker) {
            if (localTypes == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "types cannot be null!!");
            }

            localTypes.serialize(new javax.xml.namespace.QName("", "types"),
                xmlWriter);
        }

        if (localExcludeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "exclude", xmlWriter);

            if (localExclude == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "exclude cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localExclude);
            }

            xmlWriter.writeEndElement();
        }

        if (localRemoveTracker) {
            namespace = "";
            writeStartElement(null, namespace, "remove", xmlWriter);

            if (localRemove == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "remove cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRemove);
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

        if (localFileTracker) {
            elementList.add(new javax.xml.namespace.QName("", "file"));

            if (localFile == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "file cannot be null!!");
            }

            elementList.add(localFile);
        }

        if (localLabelTracker) {
            elementList.add(new javax.xml.namespace.QName("", "label"));

            if (localLabel != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localLabel));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "label cannot be null!!");
            }
        }

        if (localPositionTracker) {
            elementList.add(new javax.xml.namespace.QName("", "position"));

            if (localPosition != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPosition));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "position cannot be null!!");
            }
        }

        if (localTypesTracker) {
            elementList.add(new javax.xml.namespace.QName("", "types"));

            if (localTypes == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "types cannot be null!!");
            }

            elementList.add(localTypes);
        }

        if (localExcludeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "exclude"));

            if (localExclude != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localExclude));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "exclude cannot be null!!");
            }
        }

        if (localRemoveTracker) {
            elementList.add(new javax.xml.namespace.QName("", "remove"));

            if (localRemove != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRemove));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "remove cannot be null!!");
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
        public static CatalogProductAttributeMediaCreateEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            CatalogProductAttributeMediaCreateEntity object = new CatalogProductAttributeMediaCreateEntity();

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

                        if (!"catalogProductAttributeMediaCreateEntity".equals(
                                    type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductAttributeMediaCreateEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "file").equals(
                            reader.getName())) {
                    object.setFile(magento.CatalogProductImageFileEntity.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "label").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "label" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setLabel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                    String content = reader.getElementText();

                    object.setPosition(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "types").equals(
                            reader.getName())) {
                    object.setTypes(magento.ArrayOfString.Factory.parse(reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "exclude").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "exclude" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setExclude(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "remove").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "remove" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setRemove(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                throw new Exception(e);
            }

            return object;
        }
    } //end of factory class
}
