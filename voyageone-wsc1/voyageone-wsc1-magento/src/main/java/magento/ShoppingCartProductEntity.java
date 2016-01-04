/**
 * ShoppingCartProductEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ShoppingCartProductEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ShoppingCartProductEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = shoppingCartProductEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Product_id
     */
    protected String localProduct_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localProduct_idTracker = false;

    /**
     * field for Sku
     */
    protected String localSku;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSkuTracker = false;

    /**
     * field for Qty
     */
    protected double localQty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localQtyTracker = false;

    /**
     * field for Options
     */
    protected magento.AssociativeArray localOptions;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOptionsTracker = false;

    /**
     * field for Bundle_option
     */
    protected magento.AssociativeArray localBundle_option;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBundle_optionTracker = false;

    /**
     * field for Bundle_option_qty
     */
    protected magento.AssociativeArray localBundle_option_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBundle_option_qtyTracker = false;

    /**
     * field for Links
     */
    protected magento.ArrayOfString localLinks;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localLinksTracker = false;

    public boolean isProduct_idSpecified() {
        return localProduct_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getProduct_id() {
        return localProduct_id;
    }

    /**
     * Auto generated setter method
     * @param param Product_id
     */
    public void setProduct_id(String param) {
        localProduct_idTracker = param != null;

        this.localProduct_id = param;
    }

    public boolean isSkuSpecified() {
        return localSkuTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getSku() {
        return localSku;
    }

    /**
     * Auto generated setter method
     * @param param Sku
     */
    public void setSku(String param) {
        localSkuTracker = param != null;

        this.localSku = param;
    }

    public boolean isQtySpecified() {
        return localQtyTracker;
    }

    /**
     * Auto generated getter method
     * @return double
     */
    public double getQty() {
        return localQty;
    }

    /**
     * Auto generated setter method
     * @param param Qty
     */
    public void setQty(double param) {
        // setting primitive attribute tracker to true
        localQtyTracker = !Double.isNaN(param);

        this.localQty = param;
    }

    public boolean isOptionsSpecified() {
        return localOptionsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.AssociativeArray
     */
    public magento.AssociativeArray getOptions() {
        return localOptions;
    }

    /**
     * Auto generated setter method
     * @param param Options
     */
    public void setOptions(magento.AssociativeArray param) {
        localOptionsTracker = param != null;

        this.localOptions = param;
    }

    public boolean isBundle_optionSpecified() {
        return localBundle_optionTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.AssociativeArray
     */
    public magento.AssociativeArray getBundle_option() {
        return localBundle_option;
    }

    /**
     * Auto generated setter method
     * @param param Bundle_option
     */
    public void setBundle_option(magento.AssociativeArray param) {
        localBundle_optionTracker = param != null;

        this.localBundle_option = param;
    }

    public boolean isBundle_option_qtySpecified() {
        return localBundle_option_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.AssociativeArray
     */
    public magento.AssociativeArray getBundle_option_qty() {
        return localBundle_option_qty;
    }

    /**
     * Auto generated setter method
     * @param param Bundle_option_qty
     */
    public void setBundle_option_qty(magento.AssociativeArray param) {
        localBundle_option_qtyTracker = param != null;

        this.localBundle_option_qty = param;
    }

    public boolean isLinksSpecified() {
        return localLinksTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getLinks() {
        return localLinks;
    }

    /**
     * Auto generated setter method
     * @param param Links
     */
    public void setLinks(magento.ArrayOfString param) {
        localLinksTracker = param != null;

        this.localLinks = param;
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
                    namespacePrefix + ":shoppingCartProductEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "shoppingCartProductEntity", xmlWriter);
            }
        }

        if (localProduct_idTracker) {
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
        }

        if (localSkuTracker) {
            namespace = "";
            writeStartElement(null, namespace, "sku", xmlWriter);

            if (localSku == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "sku cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localSku);
            }

            xmlWriter.writeEndElement();
        }

        if (localQtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "qty", xmlWriter);

            if (Double.isNaN(localQty)) {
                throw new org.apache.axis2.databinding.ADBException(
                    "qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localQty));
            }

            xmlWriter.writeEndElement();
        }

        if (localOptionsTracker) {
            if (localOptions == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "options cannot be null!!");
            }

            localOptions.serialize(new javax.xml.namespace.QName("", "options"),
                xmlWriter);
        }

        if (localBundle_optionTracker) {
            if (localBundle_option == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "bundle_option cannot be null!!");
            }

            localBundle_option.serialize(new javax.xml.namespace.QName("",
                    "bundle_option"), xmlWriter);
        }

        if (localBundle_option_qtyTracker) {
            if (localBundle_option_qty == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "bundle_option_qty cannot be null!!");
            }

            localBundle_option_qty.serialize(new javax.xml.namespace.QName("",
                    "bundle_option_qty"), xmlWriter);
        }

        if (localLinksTracker) {
            if (localLinks == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "links cannot be null!!");
            }

            localLinks.serialize(new javax.xml.namespace.QName("", "links"),
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

        if (localProduct_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "product_id"));

            if (localProduct_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localProduct_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "product_id cannot be null!!");
            }
        }

        if (localSkuTracker) {
            elementList.add(new javax.xml.namespace.QName("", "sku"));

            if (localSku != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localSku));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "sku cannot be null!!");
            }
        }

        if (localQtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localQty));
        }

        if (localOptionsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "options"));

            if (localOptions == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "options cannot be null!!");
            }

            elementList.add(localOptions);
        }

        if (localBundle_optionTracker) {
            elementList.add(new javax.xml.namespace.QName("", "bundle_option"));

            if (localBundle_option == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "bundle_option cannot be null!!");
            }

            elementList.add(localBundle_option);
        }

        if (localBundle_option_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "bundle_option_qty"));

            if (localBundle_option_qty == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "bundle_option_qty cannot be null!!");
            }

            elementList.add(localBundle_option_qty);
        }

        if (localLinksTracker) {
            elementList.add(new javax.xml.namespace.QName("", "links"));

            if (localLinks == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "links cannot be null!!");
            }

            elementList.add(localLinks);
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
        public static ShoppingCartProductEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            ShoppingCartProductEntity object = new ShoppingCartProductEntity();

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

                        if (!"shoppingCartProductEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (ShoppingCartProductEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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

                    String content = reader.getElementText();

                    object.setProduct_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sku").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sku" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setSku(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "qty" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setQty(org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setQty(Double.NaN);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "options").equals(
                            reader.getName())) {
                    object.setOptions(magento.AssociativeArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "bundle_option").equals(
                            reader.getName())) {
                    object.setBundle_option(magento.AssociativeArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "bundle_option_qty").equals(
                            reader.getName())) {
                    object.setBundle_option_qty(magento.AssociativeArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "links").equals(
                            reader.getName())) {
                    object.setLinks(magento.ArrayOfString.Factory.parse(reader));

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
