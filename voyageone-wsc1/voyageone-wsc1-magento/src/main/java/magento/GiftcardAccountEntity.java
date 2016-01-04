/**
 * GiftcardAccountEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  GiftcardAccountEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class GiftcardAccountEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = giftcardAccountEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Giftcard_id
     */
    protected int localGiftcard_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGiftcard_idTracker = false;

    /**
     * field for Code
     */
    protected String localCode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCodeTracker = false;

    /**
     * field for Store_id
     */
    protected int localStore_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_idTracker = false;

    /**
     * field for Date_created
     */
    protected String localDate_created;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDate_createdTracker = false;

    /**
     * field for Expire_date
     */
    protected String localExpire_date;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localExpire_dateTracker = false;

    /**
     * field for Is_active
     */
    protected int localIs_active;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_activeTracker = false;

    /**
     * field for Is_redeemable
     */
    protected int localIs_redeemable;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_redeemableTracker = false;

    /**
     * field for Status
     */
    protected String localStatus;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStatusTracker = false;

    /**
     * field for Balance
     */
    protected double localBalance;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBalanceTracker = false;

    /**
     * field for History
     */
    protected magento.GiftcardAccountEntityHistoryArray localHistory;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localHistoryTracker = false;

    public boolean isGiftcard_idSpecified() {
        return localGiftcard_idTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getGiftcard_id() {
        return localGiftcard_id;
    }

    /**
     * Auto generated setter method
     * @param param Giftcard_id
     */
    public void setGiftcard_id(int param) {
        // setting primitive attribute tracker to true
        localGiftcard_idTracker = param != Integer.MIN_VALUE;

        this.localGiftcard_id = param;
    }

    public boolean isCodeSpecified() {
        return localCodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCode() {
        return localCode;
    }

    /**
     * Auto generated setter method
     * @param param Code
     */
    public void setCode(String param) {
        localCodeTracker = param != null;

        this.localCode = param;
    }

    public boolean isStore_idSpecified() {
        return localStore_idTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getStore_id() {
        return localStore_id;
    }

    /**
     * Auto generated setter method
     * @param param Store_id
     */
    public void setStore_id(int param) {
        // setting primitive attribute tracker to true
        localStore_idTracker = param != Integer.MIN_VALUE;

        this.localStore_id = param;
    }

    public boolean isDate_createdSpecified() {
        return localDate_createdTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getDate_created() {
        return localDate_created;
    }

    /**
     * Auto generated setter method
     * @param param Date_created
     */
    public void setDate_created(String param) {
        localDate_createdTracker = param != null;

        this.localDate_created = param;
    }

    public boolean isExpire_dateSpecified() {
        return localExpire_dateTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getExpire_date() {
        return localExpire_date;
    }

    /**
     * Auto generated setter method
     * @param param Expire_date
     */
    public void setExpire_date(String param) {
        localExpire_dateTracker = param != null;

        this.localExpire_date = param;
    }

    public boolean isIs_activeSpecified() {
        return localIs_activeTracker;
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
        // setting primitive attribute tracker to true
        localIs_activeTracker = param != Integer.MIN_VALUE;

        this.localIs_active = param;
    }

    public boolean isIs_redeemableSpecified() {
        return localIs_redeemableTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_redeemable() {
        return localIs_redeemable;
    }

    /**
     * Auto generated setter method
     * @param param Is_redeemable
     */
    public void setIs_redeemable(int param) {
        // setting primitive attribute tracker to true
        localIs_redeemableTracker = param != Integer.MIN_VALUE;

        this.localIs_redeemable = param;
    }

    public boolean isStatusSpecified() {
        return localStatusTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStatus() {
        return localStatus;
    }

    /**
     * Auto generated setter method
     * @param param Status
     */
    public void setStatus(String param) {
        localStatusTracker = param != null;

        this.localStatus = param;
    }

    public boolean isBalanceSpecified() {
        return localBalanceTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getBalance() {
        return localBalance;
    }

    /**
     * Auto generated setter method
     * @param param Balance
     */
    public void setBalance(double param) {
        // setting primitive attribute tracker to true
        localBalanceTracker = !Double.isNaN(param);

        this.localBalance = param;
    }

    public boolean isHistorySpecified() {
        return localHistoryTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.GiftcardAccountEntityHistoryArray
     */
    public magento.GiftcardAccountEntityHistoryArray getHistory() {
        return localHistory;
    }

    /**
     * Auto generated setter method
     * @param param History
     */
    public void setHistory(magento.GiftcardAccountEntityHistoryArray param) {
        localHistoryTracker = param != null;

        this.localHistory = param;
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
                    namespacePrefix + ":giftcardAccountEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "giftcardAccountEntity", xmlWriter);
            }
        }

        if (localGiftcard_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "giftcard_id", xmlWriter);

            if (localGiftcard_id == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "giftcard_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGiftcard_id));
            }

            xmlWriter.writeEndElement();
        }

        if (localCodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "code", xmlWriter);

            if (localCode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "code cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCode);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_id", xmlWriter);

            if (localStore_id == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_id));
            }

            xmlWriter.writeEndElement();
        }

        if (localDate_createdTracker) {
            namespace = "";
            writeStartElement(null, namespace, "date_created", xmlWriter);

            if (localDate_created == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "date_created cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDate_created);
            }

            xmlWriter.writeEndElement();
        }

        if (localExpire_dateTracker) {
            namespace = "";
            writeStartElement(null, namespace, "expire_date", xmlWriter);

            if (localExpire_date == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "expire_date cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localExpire_date);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_activeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_active", xmlWriter);

            if (localIs_active == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_active cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_active));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_redeemableTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_redeemable", xmlWriter);

            if (localIs_redeemable == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_redeemable cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_redeemable));
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

        if (localBalanceTracker) {
            namespace = "";
            writeStartElement(null, namespace, "balance", xmlWriter);

            if (Double.isNaN(localBalance)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "balance cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBalance));
            }

            xmlWriter.writeEndElement();
        }

        if (localHistoryTracker) {
            if (localHistory == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "history cannot be null!!");
            }

            localHistory.serialize(new javax.xml.namespace.QName("", "history"),
                xmlWriter);
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

        if (localGiftcard_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "giftcard_id"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localGiftcard_id));
        }

        if (localCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "code"));

            if (localCode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "code cannot be null!!");
            }
        }

        if (localStore_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "store_id"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localStore_id));
        }

        if (localDate_createdTracker) {
            elementList.add(new javax.xml.namespace.QName("", "date_created"));

            if (localDate_created != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDate_created));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "date_created cannot be null!!");
            }
        }

        if (localExpire_dateTracker) {
            elementList.add(new javax.xml.namespace.QName("", "expire_date"));

            if (localExpire_date != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localExpire_date));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "expire_date cannot be null!!");
            }
        }

        if (localIs_activeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_active"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_active));
        }

        if (localIs_redeemableTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_redeemable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_redeemable));
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

        if (localBalanceTracker) {
            elementList.add(new javax.xml.namespace.QName("", "balance"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBalance));
        }

        if (localHistoryTracker) {
            elementList.add(new javax.xml.namespace.QName("", "history"));

            if (localHistory == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "history cannot be null!!");
            }

            elementList.add(localHistory);
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
        public static GiftcardAccountEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            GiftcardAccountEntity object = new GiftcardAccountEntity();

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

                        if (!"giftcardAccountEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (GiftcardAccountEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "giftcard_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "giftcard_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setGiftcard_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setGiftcard_id(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "code" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "store_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "store_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setStore_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setStore_id(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "date_created").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "date_created" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setDate_created(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "expire_date").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "expire_date" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setExpire_date(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                    String content = reader.getElementText();

                    object.setIs_active(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_active(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_redeemable").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_redeemable" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_redeemable(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_redeemable(Integer.MIN_VALUE);
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

                    String content = reader.getElementText();

                    object.setStatus(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "balance").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "balance" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBalance(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBalance(Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "history").equals(
                            reader.getName())) {
                    object.setHistory(magento.GiftcardAccountEntityHistoryArray.Factory.parse(
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
