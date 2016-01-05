/**
 * ShoppingCartPaymentMethodEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ShoppingCartPaymentMethodEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ShoppingCartPaymentMethodEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = shoppingCartPaymentMethodEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Po_number
     */
    protected String localPo_number;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPo_numberTracker = false;

    /**
     * field for Method
     */
    protected String localMethod;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMethodTracker = false;

    /**
     * field for Cc_cid
     */
    protected String localCc_cid;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_cidTracker = false;

    /**
     * field for Cc_owner
     */
    protected String localCc_owner;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_ownerTracker = false;

    /**
     * field for Cc_number
     */
    protected String localCc_number;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_numberTracker = false;

    /**
     * field for Cc_type
     */
    protected String localCc_type;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_typeTracker = false;

    /**
     * field for Cc_exp_year
     */
    protected String localCc_exp_year;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_exp_yearTracker = false;

    /**
     * field for Cc_exp_month
     */
    protected String localCc_exp_month;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_exp_monthTracker = false;

    public boolean isPo_numberSpecified() {
        return localPo_numberTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPo_number() {
        return localPo_number;
    }

    /**
     * Auto generated setter method
     * @param param Po_number
     */
    public void setPo_number(String param) {
        localPo_numberTracker = param != null;

        this.localPo_number = param;
    }

    public boolean isMethodSpecified() {
        return localMethodTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getMethod() {
        return localMethod;
    }

    /**
     * Auto generated setter method
     * @param param Method
     */
    public void setMethod(String param) {
        localMethodTracker = param != null;

        this.localMethod = param;
    }

    public boolean isCc_cidSpecified() {
        return localCc_cidTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_cid() {
        return localCc_cid;
    }

    /**
     * Auto generated setter method
     * @param param Cc_cid
     */
    public void setCc_cid(String param) {
        localCc_cidTracker = param != null;

        this.localCc_cid = param;
    }

    public boolean isCc_ownerSpecified() {
        return localCc_ownerTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_owner() {
        return localCc_owner;
    }

    /**
     * Auto generated setter method
     * @param param Cc_owner
     */
    public void setCc_owner(String param) {
        localCc_ownerTracker = param != null;

        this.localCc_owner = param;
    }

    public boolean isCc_numberSpecified() {
        return localCc_numberTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_number() {
        return localCc_number;
    }

    /**
     * Auto generated setter method
     * @param param Cc_number
     */
    public void setCc_number(String param) {
        localCc_numberTracker = param != null;

        this.localCc_number = param;
    }

    public boolean isCc_typeSpecified() {
        return localCc_typeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_type() {
        return localCc_type;
    }

    /**
     * Auto generated setter method
     * @param param Cc_type
     */
    public void setCc_type(String param) {
        localCc_typeTracker = param != null;

        this.localCc_type = param;
    }

    public boolean isCc_exp_yearSpecified() {
        return localCc_exp_yearTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_exp_year() {
        return localCc_exp_year;
    }

    /**
     * Auto generated setter method
     * @param param Cc_exp_year
     */
    public void setCc_exp_year(String param) {
        localCc_exp_yearTracker = param != null;

        this.localCc_exp_year = param;
    }

    public boolean isCc_exp_monthSpecified() {
        return localCc_exp_monthTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_exp_month() {
        return localCc_exp_month;
    }

    /**
     * Auto generated setter method
     * @param param Cc_exp_month
     */
    public void setCc_exp_month(String param) {
        localCc_exp_monthTracker = param != null;

        this.localCc_exp_month = param;
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
                    namespacePrefix + ":shoppingCartPaymentMethodEntity",
                    xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "shoppingCartPaymentMethodEntity", xmlWriter);
            }
        }

        if (localPo_numberTracker) {
            namespace = "";
            writeStartElement(null, namespace, "po_number", xmlWriter);

            if (localPo_number == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "po_number cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPo_number);
            }

            xmlWriter.writeEndElement();
        }

        if (localMethodTracker) {
            namespace = "";
            writeStartElement(null, namespace, "method", xmlWriter);

            if (localMethod == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "method cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMethod);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_cidTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_cid", xmlWriter);

            if (localCc_cid == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_cid cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_cid);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_ownerTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_owner", xmlWriter);

            if (localCc_owner == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_owner cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_owner);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_numberTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_number", xmlWriter);

            if (localCc_number == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_number cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_number);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_typeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_type", xmlWriter);

            if (localCc_type == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_type cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_type);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_exp_yearTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_exp_year", xmlWriter);

            if (localCc_exp_year == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_year cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_exp_year);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_exp_monthTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_exp_month", xmlWriter);

            if (localCc_exp_month == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_month cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_exp_month);
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

        if (localPo_numberTracker) {
            elementList.add(new javax.xml.namespace.QName("", "po_number"));

            if (localPo_number != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPo_number));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "po_number cannot be null!!");
            }
        }

        if (localMethodTracker) {
            elementList.add(new javax.xml.namespace.QName("", "method"));

            if (localMethod != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMethod));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "method cannot be null!!");
            }
        }

        if (localCc_cidTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_cid"));

            if (localCc_cid != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_cid));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_cid cannot be null!!");
            }
        }

        if (localCc_ownerTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_owner"));

            if (localCc_owner != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_owner));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_owner cannot be null!!");
            }
        }

        if (localCc_numberTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_number"));

            if (localCc_number != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_number));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_number cannot be null!!");
            }
        }

        if (localCc_typeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_type"));

            if (localCc_type != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_type));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_type cannot be null!!");
            }
        }

        if (localCc_exp_yearTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_exp_year"));

            if (localCc_exp_year != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_exp_year));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_year cannot be null!!");
            }
        }

        if (localCc_exp_monthTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_exp_month"));

            if (localCc_exp_month != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_exp_month));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_month cannot be null!!");
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
        public static ShoppingCartPaymentMethodEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            ShoppingCartPaymentMethodEntity object = new ShoppingCartPaymentMethodEntity();

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

                        if (!"shoppingCartPaymentMethodEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (ShoppingCartPaymentMethodEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "po_number").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "po_number" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPo_number(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "method").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "method" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setMethod(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_cid").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_cid" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_cid(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_owner").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_owner" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_owner(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_number").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_number" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_number(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_type" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_exp_year").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_exp_year" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_exp_year(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_exp_month").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_exp_month" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_exp_month(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
