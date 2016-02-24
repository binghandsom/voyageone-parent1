/**
 * CustomerCustomerEntityToCreate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CustomerCustomerEntityToCreate bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CustomerCustomerEntityToCreate implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = customerCustomerEntityToCreate
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

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
     * field for Middlename
     */
    protected java.lang.String localMiddlename;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMiddlenameTracker = false;

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

    /**
     * field for Prefix
     */
    protected java.lang.String localPrefix;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPrefixTracker = false;

    /**
     * field for Suffix
     */
    protected java.lang.String localSuffix;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSuffixTracker = false;

    /**
     * field for Dob
     */
    protected java.lang.String localDob;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDobTracker = false;

    /**
     * field for Taxvat
     */
    protected java.lang.String localTaxvat;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTaxvatTracker = false;

    /**
     * field for Gender
     */
    protected int localGender;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localGenderTracker = false;

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

    public boolean isMiddlenameSpecified() {
        return localMiddlenameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMiddlename() {
        return localMiddlename;
    }

    /**
     * Auto generated setter method
     * @param param Middlename
     */
    public void setMiddlename(java.lang.String param) {
        localMiddlenameTracker = param != null;

        this.localMiddlename = param;
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

    public boolean isPrefixSpecified() {
        return localPrefixTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPrefix() {
        return localPrefix;
    }

    /**
     * Auto generated setter method
     * @param param Prefix
     */
    public void setPrefix(java.lang.String param) {
        localPrefixTracker = param != null;

        this.localPrefix = param;
    }

    public boolean isSuffixSpecified() {
        return localSuffixTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSuffix() {
        return localSuffix;
    }

    /**
     * Auto generated setter method
     * @param param Suffix
     */
    public void setSuffix(java.lang.String param) {
        localSuffixTracker = param != null;

        this.localSuffix = param;
    }

    public boolean isDobSpecified() {
        return localDobTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDob() {
        return localDob;
    }

    /**
     * Auto generated setter method
     * @param param Dob
     */
    public void setDob(java.lang.String param) {
        localDobTracker = param != null;

        this.localDob = param;
    }

    public boolean isTaxvatSpecified() {
        return localTaxvatTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getTaxvat() {
        return localTaxvat;
    }

    /**
     * Auto generated setter method
     * @param param Taxvat
     */
    public void setTaxvat(java.lang.String param) {
        localTaxvatTracker = param != null;

        this.localTaxvat = param;
    }

    public boolean isGenderSpecified() {
        return localGenderTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getGender() {
        return localGender;
    }

    /**
     * Auto generated setter method
     * @param param Gender
     */
    public void setGender(int param) {
        // setting primitive attribute tracker to true
        localGenderTracker = param != java.lang.Integer.MIN_VALUE;

        this.localGender = param;
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
                    namespacePrefix + ":customerCustomerEntityToCreate",
                    xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "customerCustomerEntityToCreate", xmlWriter);
            }
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

        if (localMiddlenameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "middlename", xmlWriter);

            if (localMiddlename == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "middlename cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMiddlename);
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

        if (localPrefixTracker) {
            namespace = "";
            writeStartElement(null, namespace, "prefix", xmlWriter);

            if (localPrefix == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "prefix cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPrefix);
            }

            xmlWriter.writeEndElement();
        }

        if (localSuffixTracker) {
            namespace = "";
            writeStartElement(null, namespace, "suffix", xmlWriter);

            if (localSuffix == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "suffix cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSuffix);
            }

            xmlWriter.writeEndElement();
        }

        if (localDobTracker) {
            namespace = "";
            writeStartElement(null, namespace, "dob", xmlWriter);

            if (localDob == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "dob cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDob);
            }

            xmlWriter.writeEndElement();
        }

        if (localTaxvatTracker) {
            namespace = "";
            writeStartElement(null, namespace, "taxvat", xmlWriter);

            if (localTaxvat == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "taxvat cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTaxvat);
            }

            xmlWriter.writeEndElement();
        }

        if (localGenderTracker) {
            namespace = "";
            writeStartElement(null, namespace, "gender", xmlWriter);

            if (localGender == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "gender cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localGender));
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

        if (localMiddlenameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "middlename"));

            if (localMiddlename != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMiddlename));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "middlename cannot be null!!");
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

        if (localPrefixTracker) {
            elementList.add(new javax.xml.namespace.QName("", "prefix"));

            if (localPrefix != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPrefix));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "prefix cannot be null!!");
            }
        }

        if (localSuffixTracker) {
            elementList.add(new javax.xml.namespace.QName("", "suffix"));

            if (localSuffix != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSuffix));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "suffix cannot be null!!");
            }
        }

        if (localDobTracker) {
            elementList.add(new javax.xml.namespace.QName("", "dob"));

            if (localDob != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDob));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "dob cannot be null!!");
            }
        }

        if (localTaxvatTracker) {
            elementList.add(new javax.xml.namespace.QName("", "taxvat"));

            if (localTaxvat != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTaxvat));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "taxvat cannot be null!!");
            }
        }

        if (localGenderTracker) {
            elementList.add(new javax.xml.namespace.QName("", "gender"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localGender));
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
        public static CustomerCustomerEntityToCreate parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CustomerCustomerEntityToCreate object = new CustomerCustomerEntityToCreate();

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

                        if (!"customerCustomerEntityToCreate".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CustomerCustomerEntityToCreate) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "middlename").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "middlename" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMiddlename(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "prefix").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "prefix" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPrefix(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "suffix").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "suffix" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSuffix(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "dob").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "dob" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDob(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "taxvat").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "taxvat" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTaxvat(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "gender").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "gender" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setGender(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setGender(java.lang.Integer.MIN_VALUE);
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
