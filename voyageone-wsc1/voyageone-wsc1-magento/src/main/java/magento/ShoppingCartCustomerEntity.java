/**
 * ShoppingCartCustomerEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ShoppingCartCustomerEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ShoppingCartCustomerEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = shoppingCartCustomerEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Mode
     */
    protected java.lang.String localMode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localModeTracker = false;

    /**
     * field for Customer_id
     */
    protected int localCustomer_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCustomer_idTracker = false;

    /**
     * field for Email
     */
    protected java.lang.String localEmail;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localEmailTracker = false;

    /**
     * field for Firstname
     */
    protected java.lang.String localFirstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFirstnameTracker = false;

    /**
     * field for Lastname
     */
    protected java.lang.String localLastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localLastnameTracker = false;

    /**
     * field for Password
     */
    protected java.lang.String localPassword;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPasswordTracker = false;

    /**
     * field for Confirmation
     */
    protected java.lang.String localConfirmation;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localConfirmationTracker = false;

    /**
     * field for Website_id
     */
    protected int localWebsite_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localWebsite_idTracker = false;

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
     * field for Group_id
     */
    protected int localGroup_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGroup_idTracker = false;

    public boolean isModeSpecified() {
        return localModeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMode() {
        return localMode;
    }

    /**
     * Auto generated setter method
     * @param param Mode
     */
    public void setMode(java.lang.String param) {
        localModeTracker = param != null;

        this.localMode = param;
    }

    public boolean isCustomer_idSpecified() {
        return localCustomer_idTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getCustomer_id() {
        return localCustomer_id;
    }

    /**
     * Auto generated setter method
     * @param param Customer_id
     */
    public void setCustomer_id(int param) {
        // setting primitive attribute tracker to true
        localCustomer_idTracker = param != java.lang.Integer.MIN_VALUE;

        this.localCustomer_id = param;
    }

    public boolean isEmailSpecified() {
        return localEmailTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getEmail() {
        return localEmail;
    }

    /**
     * Auto generated setter method
     * @param param Email
     */
    public void setEmail(java.lang.String param) {
        localEmailTracker = param != null;

        this.localEmail = param;
    }

    public boolean isFirstnameSpecified() {
        return localFirstnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getFirstname() {
        return localFirstname;
    }

    /**
     * Auto generated setter method
     * @param param Firstname
     */
    public void setFirstname(java.lang.String param) {
        localFirstnameTracker = param != null;

        this.localFirstname = param;
    }

    public boolean isLastnameSpecified() {
        return localLastnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getLastname() {
        return localLastname;
    }

    /**
     * Auto generated setter method
     * @param param Lastname
     */
    public void setLastname(java.lang.String param) {
        localLastnameTracker = param != null;

        this.localLastname = param;
    }

    public boolean isPasswordSpecified() {
        return localPasswordTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPassword() {
        return localPassword;
    }

    /**
     * Auto generated setter method
     * @param param Password
     */
    public void setPassword(java.lang.String param) {
        localPasswordTracker = param != null;

        this.localPassword = param;
    }

    public boolean isConfirmationSpecified() {
        return localConfirmationTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getConfirmation() {
        return localConfirmation;
    }

    /**
     * Auto generated setter method
     * @param param Confirmation
     */
    public void setConfirmation(java.lang.String param) {
        localConfirmationTracker = param != null;

        this.localConfirmation = param;
    }

    public boolean isWebsite_idSpecified() {
        return localWebsite_idTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getWebsite_id() {
        return localWebsite_id;
    }

    /**
     * Auto generated setter method
     * @param param Website_id
     */
    public void setWebsite_id(int param) {
        // setting primitive attribute tracker to true
        localWebsite_idTracker = param != java.lang.Integer.MIN_VALUE;

        this.localWebsite_id = param;
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
        localStore_idTracker = param != java.lang.Integer.MIN_VALUE;

        this.localStore_id = param;
    }

    public boolean isGroup_idSpecified() {
        return localGroup_idTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getGroup_id() {
        return localGroup_id;
    }

    /**
     * Auto generated setter method
     * @param param Group_id
     */
    public void setGroup_id(int param) {
        // setting primitive attribute tracker to true
        localGroup_idTracker = param != java.lang.Integer.MIN_VALUE;

        this.localGroup_id = param;
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
                    namespacePrefix + ":shoppingCartCustomerEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "shoppingCartCustomerEntity", xmlWriter);
            }
        }

        if (localModeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "mode", xmlWriter);

            if (localMode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "mode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMode);
            }

            xmlWriter.writeEndElement();
        }

        if (localCustomer_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "customer_id", xmlWriter);

            if (localCustomer_id == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "customer_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCustomer_id));
            }

            xmlWriter.writeEndElement();
        }

        if (localEmailTracker) {
            namespace = "";
            writeStartElement(null, namespace, "email", xmlWriter);

            if (localEmail == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "email cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localEmail);
            }

            xmlWriter.writeEndElement();
        }

        if (localFirstnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "firstname", xmlWriter);

            if (localFirstname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "firstname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localFirstname);
            }

            xmlWriter.writeEndElement();
        }

        if (localLastnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "lastname", xmlWriter);

            if (localLastname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "lastname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localLastname);
            }

            xmlWriter.writeEndElement();
        }

        if (localPasswordTracker) {
            namespace = "";
            writeStartElement(null, namespace, "password", xmlWriter);

            if (localPassword == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "password cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPassword);
            }

            xmlWriter.writeEndElement();
        }

        if (localConfirmationTracker) {
            namespace = "";
            writeStartElement(null, namespace, "confirmation", xmlWriter);

            if (localConfirmation == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "confirmation cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localConfirmation);
            }

            xmlWriter.writeEndElement();
        }

        if (localWebsite_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "website_id", xmlWriter);

            if (localWebsite_id == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "website_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localWebsite_id));
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_id", xmlWriter);

            if (localStore_id == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_id));
            }

            xmlWriter.writeEndElement();
        }

        if (localGroup_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "group_id", xmlWriter);

            if (localGroup_id == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "group_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGroup_id));
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

        if (localModeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "mode"));

            if (localMode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "mode cannot be null!!");
            }
        }

        if (localCustomer_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "customer_id"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localCustomer_id));
        }

        if (localEmailTracker) {
            elementList.add(new javax.xml.namespace.QName("", "email"));

            if (localEmail != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localEmail));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "email cannot be null!!");
            }
        }

        if (localFirstnameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "firstname"));

            if (localFirstname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localFirstname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "firstname cannot be null!!");
            }
        }

        if (localLastnameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "lastname"));

            if (localLastname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localLastname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "lastname cannot be null!!");
            }
        }

        if (localPasswordTracker) {
            elementList.add(new javax.xml.namespace.QName("", "password"));

            if (localPassword != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPassword));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "password cannot be null!!");
            }
        }

        if (localConfirmationTracker) {
            elementList.add(new javax.xml.namespace.QName("", "confirmation"));

            if (localConfirmation != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localConfirmation));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "confirmation cannot be null!!");
            }
        }

        if (localWebsite_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "website_id"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localWebsite_id));
        }

        if (localStore_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "store_id"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localStore_id));
        }

        if (localGroup_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "group_id"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localGroup_id));
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
        public static ShoppingCartCustomerEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            ShoppingCartCustomerEntity object = new ShoppingCartCustomerEntity();

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

                        if (!"shoppingCartCustomerEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (ShoppingCartCustomerEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "mode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "mode" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                    object.setCustomer_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setCustomer_id(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "email").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "email" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setEmail(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "firstname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "firstname" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFirstname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "lastname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "lastname" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "password").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "password" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPassword(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "confirmation").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "confirmation" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setConfirmation(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                    object.setWebsite_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setWebsite_id(java.lang.Integer.MIN_VALUE);
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

                    java.lang.String content = reader.getElementText();

                    object.setStore_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setStore_id(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "group_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "group_id" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setGroup_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setGroup_id(java.lang.Integer.MIN_VALUE);
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
