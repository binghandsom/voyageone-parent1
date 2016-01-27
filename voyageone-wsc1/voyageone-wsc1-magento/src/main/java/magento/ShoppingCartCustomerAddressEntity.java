/**
 * ShoppingCartCustomerAddressEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ShoppingCartCustomerAddressEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ShoppingCartCustomerAddressEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = shoppingCartCustomerAddressEntity
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
     * field for Address_id
     */
    protected java.lang.String localAddress_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAddress_idTracker = false;

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
     * field for Company
     */
    protected java.lang.String localCompany;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCompanyTracker = false;

    /**
     * field for Street
     */
    protected java.lang.String localStreet;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStreetTracker = false;

    /**
     * field for City
     */
    protected java.lang.String localCity;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCityTracker = false;

    /**
     * field for Region
     */
    protected java.lang.String localRegion;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRegionTracker = false;

    /**
     * field for Region_id
     */
    protected java.lang.String localRegion_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localRegion_idTracker = false;

    /**
     * field for Postcode
     */
    protected java.lang.String localPostcode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPostcodeTracker = false;

    /**
     * field for Country_id
     */
    protected java.lang.String localCountry_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCountry_idTracker = false;

    /**
     * field for Telephone
     */
    protected java.lang.String localTelephone;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTelephoneTracker = false;

    /**
     * field for Fax
     */
    protected java.lang.String localFax;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localFaxTracker = false;

    /**
     * field for Is_default_billing
     */
    protected int localIs_default_billing;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_default_billingTracker = false;

    /**
     * field for Is_default_shipping
     */
    protected int localIs_default_shipping;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_default_shippingTracker = false;

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

    public boolean isAddress_idSpecified() {
        return localAddress_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAddress_id() {
        return localAddress_id;
    }

    /**
     * Auto generated setter method
     * @param param Address_id
     */
    public void setAddress_id(java.lang.String param) {
        localAddress_idTracker = param != null;

        this.localAddress_id = param;
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

    public boolean isCompanySpecified() {
        return localCompanyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCompany() {
        return localCompany;
    }

    /**
     * Auto generated setter method
     * @param param Company
     */
    public void setCompany(java.lang.String param) {
        localCompanyTracker = param != null;

        this.localCompany = param;
    }

    public boolean isStreetSpecified() {
        return localStreetTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStreet() {
        return localStreet;
    }

    /**
     * Auto generated setter method
     * @param param Street
     */
    public void setStreet(java.lang.String param) {
        localStreetTracker = param != null;

        this.localStreet = param;
    }

    public boolean isCitySpecified() {
        return localCityTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCity() {
        return localCity;
    }

    /**
     * Auto generated setter method
     * @param param City
     */
    public void setCity(java.lang.String param) {
        localCityTracker = param != null;

        this.localCity = param;
    }

    public boolean isRegionSpecified() {
        return localRegionTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getRegion() {
        return localRegion;
    }

    /**
     * Auto generated setter method
     * @param param Region
     */
    public void setRegion(java.lang.String param) {
        localRegionTracker = param != null;

        this.localRegion = param;
    }

    public boolean isRegion_idSpecified() {
        return localRegion_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getRegion_id() {
        return localRegion_id;
    }

    /**
     * Auto generated setter method
     * @param param Region_id
     */
    public void setRegion_id(java.lang.String param) {
        localRegion_idTracker = param != null;

        this.localRegion_id = param;
    }

    public boolean isPostcodeSpecified() {
        return localPostcodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPostcode() {
        return localPostcode;
    }

    /**
     * Auto generated setter method
     * @param param Postcode
     */
    public void setPostcode(java.lang.String param) {
        localPostcodeTracker = param != null;

        this.localPostcode = param;
    }

    public boolean isCountry_idSpecified() {
        return localCountry_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCountry_id() {
        return localCountry_id;
    }

    /**
     * Auto generated setter method
     * @param param Country_id
     */
    public void setCountry_id(java.lang.String param) {
        localCountry_idTracker = param != null;

        this.localCountry_id = param;
    }

    public boolean isTelephoneSpecified() {
        return localTelephoneTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getTelephone() {
        return localTelephone;
    }

    /**
     * Auto generated setter method
     * @param param Telephone
     */
    public void setTelephone(java.lang.String param) {
        localTelephoneTracker = param != null;

        this.localTelephone = param;
    }

    public boolean isFaxSpecified() {
        return localFaxTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getFax() {
        return localFax;
    }

    /**
     * Auto generated setter method
     * @param param Fax
     */
    public void setFax(java.lang.String param) {
        localFaxTracker = param != null;

        this.localFax = param;
    }

    public boolean isIs_default_billingSpecified() {
        return localIs_default_billingTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_default_billing() {
        return localIs_default_billing;
    }

    /**
     * Auto generated setter method
     * @param param Is_default_billing
     */
    public void setIs_default_billing(int param) {
        // setting primitive attribute tracker to true
        localIs_default_billingTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_default_billing = param;
    }

    public boolean isIs_default_shippingSpecified() {
        return localIs_default_shippingTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_default_shipping() {
        return localIs_default_shipping;
    }

    /**
     * Auto generated setter method
     * @param param Is_default_shipping
     */
    public void setIs_default_shipping(int param) {
        // setting primitive attribute tracker to true
        localIs_default_shippingTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIs_default_shipping = param;
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
                    namespacePrefix + ":shoppingCartCustomerAddressEntity",
                    xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "shoppingCartCustomerAddressEntity", xmlWriter);
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

        if (localAddress_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "address_id", xmlWriter);

            if (localAddress_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "address_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localAddress_id);
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

        if (localCompanyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "company", xmlWriter);

            if (localCompany == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "company cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCompany);
            }

            xmlWriter.writeEndElement();
        }

        if (localStreetTracker) {
            namespace = "";
            writeStartElement(null, namespace, "street", xmlWriter);

            if (localStreet == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "street cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStreet);
            }

            xmlWriter.writeEndElement();
        }

        if (localCityTracker) {
            namespace = "";
            writeStartElement(null, namespace, "city", xmlWriter);

            if (localCity == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "city cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCity);
            }

            xmlWriter.writeEndElement();
        }

        if (localRegionTracker) {
            namespace = "";
            writeStartElement(null, namespace, "region", xmlWriter);

            if (localRegion == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "region cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRegion);
            }

            xmlWriter.writeEndElement();
        }

        if (localRegion_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "region_id", xmlWriter);

            if (localRegion_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "region_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localRegion_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localPostcodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "postcode", xmlWriter);

            if (localPostcode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "postcode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPostcode);
            }

            xmlWriter.writeEndElement();
        }

        if (localCountry_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "country_id", xmlWriter);

            if (localCountry_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "country_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCountry_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localTelephoneTracker) {
            namespace = "";
            writeStartElement(null, namespace, "telephone", xmlWriter);

            if (localTelephone == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "telephone cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTelephone);
            }

            xmlWriter.writeEndElement();
        }

        if (localFaxTracker) {
            namespace = "";
            writeStartElement(null, namespace, "fax", xmlWriter);

            if (localFax == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "fax cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localFax);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_default_billingTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_default_billing", xmlWriter);

            if (localIs_default_billing == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_default_billing cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_default_billing));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_default_shippingTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_default_shipping", xmlWriter);

            if (localIs_default_shipping == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_default_shipping cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_default_shipping));
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

        if (localAddress_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "address_id"));

            if (localAddress_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localAddress_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "address_id cannot be null!!");
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

        if (localCompanyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "company"));

            if (localCompany != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCompany));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "company cannot be null!!");
            }
        }

        if (localStreetTracker) {
            elementList.add(new javax.xml.namespace.QName("", "street"));

            if (localStreet != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStreet));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "street cannot be null!!");
            }
        }

        if (localCityTracker) {
            elementList.add(new javax.xml.namespace.QName("", "city"));

            if (localCity != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCity));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "city cannot be null!!");
            }
        }

        if (localRegionTracker) {
            elementList.add(new javax.xml.namespace.QName("", "region"));

            if (localRegion != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRegion));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "region cannot be null!!");
            }
        }

        if (localRegion_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "region_id"));

            if (localRegion_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localRegion_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "region_id cannot be null!!");
            }
        }

        if (localPostcodeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "postcode"));

            if (localPostcode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPostcode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "postcode cannot be null!!");
            }
        }

        if (localCountry_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "country_id"));

            if (localCountry_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCountry_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "country_id cannot be null!!");
            }
        }

        if (localTelephoneTracker) {
            elementList.add(new javax.xml.namespace.QName("", "telephone"));

            if (localTelephone != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTelephone));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "telephone cannot be null!!");
            }
        }

        if (localFaxTracker) {
            elementList.add(new javax.xml.namespace.QName("", "fax"));

            if (localFax != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localFax));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "fax cannot be null!!");
            }
        }

        if (localIs_default_billingTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "is_default_billing"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_default_billing));
        }

        if (localIs_default_shippingTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "is_default_shipping"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_default_shipping));
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
        public static ShoppingCartCustomerAddressEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            ShoppingCartCustomerAddressEntity object = new ShoppingCartCustomerAddressEntity();

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

                        if (!"shoppingCartCustomerAddressEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (ShoppingCartCustomerAddressEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "address_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "address_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAddress_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                        new javax.xml.namespace.QName("", "company").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "company" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCompany(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "street").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "street" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setStreet(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "city").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "city" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCity(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "region").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "region" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRegion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "region_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "region_id" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRegion_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "postcode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "postcode" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPostcode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "country_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "country_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCountry_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "telephone").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "telephone" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTelephone(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "fax").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "fax" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFax(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_default_billing").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_default_billing" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_default_billing(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_default_billing(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_default_shipping").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_default_shipping" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIs_default_shipping(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_default_shipping(java.lang.Integer.MIN_VALUE);
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
