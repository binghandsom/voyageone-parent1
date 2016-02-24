/**
 * EnterpriseCustomerbalanceHistoryItemEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  EnterpriseCustomerbalanceHistoryItemEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class EnterpriseCustomerbalanceHistoryItemEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = enterpriseCustomerbalanceHistoryItemEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for History_id
     */
    protected java.lang.String localHistory_id;

    /**
     * field for Balance_id
     */
    protected java.lang.String localBalance_id;

    /**
     * field for Updated_at
     */
    protected java.lang.String localUpdated_at;

    /**
     * field for Action
     */
    protected java.lang.String localAction;

    /**
     * field for Balance_amount
     */
    protected java.lang.String localBalance_amount;

    /**
     * field for Balance_delta
     */
    protected java.lang.String localBalance_delta;

    /**
     * field for Additional_info
     */
    protected java.lang.String localAdditional_info;

    /**
     * field for Is_customer_notified
     */
    protected java.lang.String localIs_customer_notified;

    /**
     * field for Customer_id
     */
    protected java.lang.String localCustomer_id;

    /**
     * field for Website_id
     */
    protected java.lang.String localWebsite_id;

    /**
     * field for Base_currency_code
     */
    protected java.lang.String localBase_currency_code;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getHistory_id() {
        return localHistory_id;
    }

    /**
     * Auto generated setter method
     * @param param History_id
     */
    public void setHistory_id(java.lang.String param) {
        this.localHistory_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBalance_id() {
        return localBalance_id;
    }

    /**
     * Auto generated setter method
     * @param param Balance_id
     */
    public void setBalance_id(java.lang.String param) {
        this.localBalance_id = param;
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
        this.localUpdated_at = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAction() {
        return localAction;
    }

    /**
     * Auto generated setter method
     * @param param Action
     */
    public void setAction(java.lang.String param) {
        this.localAction = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBalance_amount() {
        return localBalance_amount;
    }

    /**
     * Auto generated setter method
     * @param param Balance_amount
     */
    public void setBalance_amount(java.lang.String param) {
        this.localBalance_amount = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBalance_delta() {
        return localBalance_delta;
    }

    /**
     * Auto generated setter method
     * @param param Balance_delta
     */
    public void setBalance_delta(java.lang.String param) {
        this.localBalance_delta = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAdditional_info() {
        return localAdditional_info;
    }

    /**
     * Auto generated setter method
     * @param param Additional_info
     */
    public void setAdditional_info(java.lang.String param) {
        this.localAdditional_info = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getIs_customer_notified() {
        return localIs_customer_notified;
    }

    /**
     * Auto generated setter method
     * @param param Is_customer_notified
     */
    public void setIs_customer_notified(java.lang.String param) {
        this.localIs_customer_notified = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCustomer_id() {
        return localCustomer_id;
    }

    /**
     * Auto generated setter method
     * @param param Customer_id
     */
    public void setCustomer_id(java.lang.String param) {
        this.localCustomer_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getWebsite_id() {
        return localWebsite_id;
    }

    /**
     * Auto generated setter method
     * @param param Website_id
     */
    public void setWebsite_id(java.lang.String param) {
        this.localWebsite_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBase_currency_code() {
        return localBase_currency_code;
    }

    /**
     * Auto generated setter method
     * @param param Base_currency_code
     */
    public void setBase_currency_code(java.lang.String param) {
        this.localBase_currency_code = param;
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
                    ":enterpriseCustomerbalanceHistoryItemEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "enterpriseCustomerbalanceHistoryItemEntity", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "history_id", xmlWriter);

        if (localHistory_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "history_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localHistory_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "balance_id", xmlWriter);

        if (localBalance_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "balance_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBalance_id);
        }

        xmlWriter.writeEndElement();

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

        namespace = "";
        writeStartElement(null, namespace, "action", xmlWriter);

        if (localAction == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "action cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localAction);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "balance_amount", xmlWriter);

        if (localBalance_amount == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "balance_amount cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBalance_amount);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "balance_delta", xmlWriter);

        if (localBalance_delta == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "balance_delta cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBalance_delta);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "additional_info", xmlWriter);

        if (localAdditional_info == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "additional_info cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localAdditional_info);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "is_customer_notified", xmlWriter);

        if (localIs_customer_notified == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "is_customer_notified cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localIs_customer_notified);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "customer_id", xmlWriter);

        if (localCustomer_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "customer_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localCustomer_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "website_id", xmlWriter);

        if (localWebsite_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "website_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localWebsite_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "base_currency_code", xmlWriter);

        if (localBase_currency_code == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "base_currency_code cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localBase_currency_code);
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

        elementList.add(new javax.xml.namespace.QName("", "history_id"));

        if (localHistory_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localHistory_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "history_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "balance_id"));

        if (localBalance_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBalance_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "balance_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "updated_at"));

        if (localUpdated_at != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUpdated_at));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "updated_at cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "action"));

        if (localAction != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localAction));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "action cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "balance_amount"));

        if (localBalance_amount != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBalance_amount));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "balance_amount cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "balance_delta"));

        if (localBalance_delta != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBalance_delta));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "balance_delta cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "additional_info"));

        if (localAdditional_info != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localAdditional_info));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "additional_info cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "is_customer_notified"));

        if (localIs_customer_notified != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_customer_notified));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "is_customer_notified cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "customer_id"));

        if (localCustomer_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localCustomer_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "customer_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "website_id"));

        if (localWebsite_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWebsite_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "website_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "base_currency_code"));

        if (localBase_currency_code != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBase_currency_code));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "base_currency_code cannot be null!!");
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
        public static EnterpriseCustomerbalanceHistoryItemEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            EnterpriseCustomerbalanceHistoryItemEntity object = new EnterpriseCustomerbalanceHistoryItemEntity();

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

                        if (!"enterpriseCustomerbalanceHistoryItemEntity".equals(
                                    type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (EnterpriseCustomerbalanceHistoryItemEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "history_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "history_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setHistory_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "balance_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "balance_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBalance_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "action").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "action" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAction(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "balance_amount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "balance_amount" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBalance_amount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "balance_delta").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "balance_delta" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBalance_delta(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "additional_info").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "additional_info" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAdditional_info(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "is_customer_notified").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_customer_notified" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_customer_notified(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "customer_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "customer_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustomer_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "website_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "website_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWebsite_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "base_currency_code").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "base_currency_code" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setBase_currency_code(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
