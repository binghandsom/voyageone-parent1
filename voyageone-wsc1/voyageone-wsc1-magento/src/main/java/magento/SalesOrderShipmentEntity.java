/**
 * SalesOrderShipmentEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  SalesOrderShipmentEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class SalesOrderShipmentEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = salesOrderShipmentEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Increment_id
     */
    protected String localIncrement_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIncrement_idTracker = false;

    /**
     * field for Parent_id
     */
    protected String localParent_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localParent_idTracker = false;

    /**
     * field for Store_id
     */
    protected String localStore_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStore_idTracker = false;

    /**
     * field for Created_at
     */
    protected String localCreated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCreated_atTracker = false;

    /**
     * field for Updated_at
     */
    protected String localUpdated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUpdated_atTracker = false;

    /**
     * field for Is_active
     */
    protected String localIs_active;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_activeTracker = false;

    /**
     * field for Shipping_address_id
     */
    protected String localShipping_address_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_address_idTracker = false;

    /**
     * field for Shipping_firstname
     */
    protected String localShipping_firstname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_firstnameTracker = false;

    /**
     * field for Shipping_lastname
     */
    protected String localShipping_lastname;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipping_lastnameTracker = false;

    /**
     * field for Order_id
     */
    protected String localOrder_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_idTracker = false;

    /**
     * field for Order_increment_id
     */
    protected String localOrder_increment_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_increment_idTracker = false;

    /**
     * field for Order_created_at
     */
    protected String localOrder_created_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOrder_created_atTracker = false;

    /**
     * field for Total_qty
     */
    protected String localTotal_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTotal_qtyTracker = false;

    /**
     * field for Shipment_id
     */
    protected String localShipment_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localShipment_idTracker = false;

    /**
     * field for Items
     */
    protected magento.SalesOrderShipmentItemEntityArray localItems;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localItemsTracker = false;

    /**
     * field for Tracks
     */
    protected magento.SalesOrderShipmentTrackEntityArray localTracks;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTracksTracker = false;

    /**
     * field for Comments
     */
    protected magento.SalesOrderShipmentCommentEntityArray localComments;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCommentsTracker = false;

    public boolean isIncrement_idSpecified() {
        return localIncrement_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getIncrement_id() {
        return localIncrement_id;
    }

    /**
     * Auto generated setter method
     * @param param Increment_id
     */
    public void setIncrement_id(String param) {
        localIncrement_idTracker = param != null;

        this.localIncrement_id = param;
    }

    public boolean isParent_idSpecified() {
        return localParent_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getParent_id() {
        return localParent_id;
    }

    /**
     * Auto generated setter method
     * @param param Parent_id
     */
    public void setParent_id(String param) {
        localParent_idTracker = param != null;

        this.localParent_id = param;
    }

    public boolean isStore_idSpecified() {
        return localStore_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getStore_id() {
        return localStore_id;
    }

    /**
     * Auto generated setter method
     * @param param Store_id
     */
    public void setStore_id(String param) {
        localStore_idTracker = param != null;

        this.localStore_id = param;
    }

    public boolean isCreated_atSpecified() {
        return localCreated_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCreated_at() {
        return localCreated_at;
    }

    /**
     * Auto generated setter method
     * @param param Created_at
     */
    public void setCreated_at(String param) {
        localCreated_atTracker = param != null;

        this.localCreated_at = param;
    }

    public boolean isUpdated_atSpecified() {
        return localUpdated_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getUpdated_at() {
        return localUpdated_at;
    }

    /**
     * Auto generated setter method
     * @param param Updated_at
     */
    public void setUpdated_at(String param) {
        localUpdated_atTracker = param != null;

        this.localUpdated_at = param;
    }

    public boolean isIs_activeSpecified() {
        return localIs_activeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getIs_active() {
        return localIs_active;
    }

    /**
     * Auto generated setter method
     * @param param Is_active
     */
    public void setIs_active(String param) {
        localIs_activeTracker = param != null;

        this.localIs_active = param;
    }

    public boolean isShipping_address_idSpecified() {
        return localShipping_address_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_address_id() {
        return localShipping_address_id;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_address_id
     */
    public void setShipping_address_id(String param) {
        localShipping_address_idTracker = param != null;

        this.localShipping_address_id = param;
    }

    public boolean isShipping_firstnameSpecified() {
        return localShipping_firstnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_firstname() {
        return localShipping_firstname;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_firstname
     */
    public void setShipping_firstname(String param) {
        localShipping_firstnameTracker = param != null;

        this.localShipping_firstname = param;
    }

    public boolean isShipping_lastnameSpecified() {
        return localShipping_lastnameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipping_lastname() {
        return localShipping_lastname;
    }

    /**
     * Auto generated setter method
     * @param param Shipping_lastname
     */
    public void setShipping_lastname(String param) {
        localShipping_lastnameTracker = param != null;

        this.localShipping_lastname = param;
    }

    public boolean isOrder_idSpecified() {
        return localOrder_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getOrder_id() {
        return localOrder_id;
    }

    /**
     * Auto generated setter method
     * @param param Order_id
     */
    public void setOrder_id(String param) {
        localOrder_idTracker = param != null;

        this.localOrder_id = param;
    }

    public boolean isOrder_increment_idSpecified() {
        return localOrder_increment_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getOrder_increment_id() {
        return localOrder_increment_id;
    }

    /**
     * Auto generated setter method
     * @param param Order_increment_id
     */
    public void setOrder_increment_id(String param) {
        localOrder_increment_idTracker = param != null;

        this.localOrder_increment_id = param;
    }

    public boolean isOrder_created_atSpecified() {
        return localOrder_created_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getOrder_created_at() {
        return localOrder_created_at;
    }

    /**
     * Auto generated setter method
     * @param param Order_created_at
     */
    public void setOrder_created_at(String param) {
        localOrder_created_atTracker = param != null;

        this.localOrder_created_at = param;
    }

    public boolean isTotal_qtySpecified() {
        return localTotal_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getTotal_qty() {
        return localTotal_qty;
    }

    /**
     * Auto generated setter method
     * @param param Total_qty
     */
    public void setTotal_qty(String param) {
        localTotal_qtyTracker = param != null;

        this.localTotal_qty = param;
    }

    public boolean isShipment_idSpecified() {
        return localShipment_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getShipment_id() {
        return localShipment_id;
    }

    /**
     * Auto generated setter method
     * @param param Shipment_id
     */
    public void setShipment_id(String param) {
        localShipment_idTracker = param != null;

        this.localShipment_id = param;
    }

    public boolean isItemsSpecified() {
        return localItemsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderShipmentItemEntityArray
     */
    public magento.SalesOrderShipmentItemEntityArray getItems() {
        return localItems;
    }

    /**
     * Auto generated setter method
     * @param param Items
     */
    public void setItems(magento.SalesOrderShipmentItemEntityArray param) {
        localItemsTracker = param != null;

        this.localItems = param;
    }

    public boolean isTracksSpecified() {
        return localTracksTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderShipmentTrackEntityArray
     */
    public magento.SalesOrderShipmentTrackEntityArray getTracks() {
        return localTracks;
    }

    /**
     * Auto generated setter method
     * @param param Tracks
     */
    public void setTracks(magento.SalesOrderShipmentTrackEntityArray param) {
        localTracksTracker = param != null;

        this.localTracks = param;
    }

    public boolean isCommentsSpecified() {
        return localCommentsTracker;
    }

    /**
     * Auto generated getter method
     * @return magento.SalesOrderShipmentCommentEntityArray
     */
    public magento.SalesOrderShipmentCommentEntityArray getComments() {
        return localComments;
    }

    /**
     * Auto generated setter method
     * @param param Comments
     */
    public void setComments(magento.SalesOrderShipmentCommentEntityArray param) {
        localCommentsTracker = param != null;

        this.localComments = param;
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
                    namespacePrefix + ":salesOrderShipmentEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "salesOrderShipmentEntity", xmlWriter);
            }
        }

        if (localIncrement_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "increment_id", xmlWriter);

            if (localIncrement_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "increment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localIncrement_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localParent_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "parent_id", xmlWriter);

            if (localParent_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "parent_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localParent_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localStore_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "store_id", xmlWriter);

            if (localStore_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "store_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localStore_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localCreated_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "created_at", xmlWriter);

            if (localCreated_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "created_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCreated_at);
            }

            xmlWriter.writeEndElement();
        }

        if (localUpdated_atTracker) {
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
        }

        if (localIs_activeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_active", xmlWriter);

            if (localIs_active == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "is_active cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localIs_active);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_address_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_address_id", xmlWriter);

            if (localShipping_address_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_address_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_address_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_firstnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_firstname", xmlWriter);

            if (localShipping_firstname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_firstname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_firstname);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipping_lastnameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipping_lastname", xmlWriter);

            if (localShipping_lastname == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_lastname cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipping_lastname);
            }

            xmlWriter.writeEndElement();
        }

        if (localOrder_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_id", xmlWriter);

            if (localOrder_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localOrder_increment_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_increment_id", xmlWriter);

            if (localOrder_increment_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_increment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_increment_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localOrder_created_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "order_created_at", xmlWriter);

            if (localOrder_created_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "order_created_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOrder_created_at);
            }

            xmlWriter.writeEndElement();
        }

        if (localTotal_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "total_qty", xmlWriter);

            if (localTotal_qty == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "total_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localTotal_qty);
            }

            xmlWriter.writeEndElement();
        }

        if (localShipment_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "shipment_id", xmlWriter);

            if (localShipment_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "shipment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localShipment_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localItemsTracker) {
            if (localItems == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "items cannot be null!!");
            }

            localItems.serialize(new javax.xml.namespace.QName("", "items"),
                xmlWriter);
        }

        if (localTracksTracker) {
            if (localTracks == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tracks cannot be null!!");
            }

            localTracks.serialize(new javax.xml.namespace.QName("", "tracks"),
                xmlWriter);
        }

        if (localCommentsTracker) {
            if (localComments == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "comments cannot be null!!");
            }

            localComments.serialize(new javax.xml.namespace.QName("", "comments"),
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

        if (localIncrement_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "increment_id"));

            if (localIncrement_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIncrement_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "increment_id cannot be null!!");
            }
        }

        if (localParent_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "parent_id"));

            if (localParent_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localParent_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "parent_id cannot be null!!");
            }
        }

        if (localStore_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "store_id"));

            if (localStore_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localStore_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "store_id cannot be null!!");
            }
        }

        if (localCreated_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "created_at"));

            if (localCreated_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCreated_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "created_at cannot be null!!");
            }
        }

        if (localUpdated_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "updated_at"));

            if (localUpdated_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUpdated_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "updated_at cannot be null!!");
            }
        }

        if (localIs_activeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_active"));

            if (localIs_active != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_active));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_active cannot be null!!");
            }
        }

        if (localShipping_address_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_address_id"));

            if (localShipping_address_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_address_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_address_id cannot be null!!");
            }
        }

        if (localShipping_firstnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_firstname"));

            if (localShipping_firstname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_firstname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_firstname cannot be null!!");
            }
        }

        if (localShipping_lastnameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "shipping_lastname"));

            if (localShipping_lastname != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipping_lastname));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipping_lastname cannot be null!!");
            }
        }

        if (localOrder_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "order_id"));

            if (localOrder_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_id cannot be null!!");
            }
        }

        if (localOrder_increment_idTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "order_increment_id"));

            if (localOrder_increment_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_increment_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_increment_id cannot be null!!");
            }
        }

        if (localOrder_created_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "order_created_at"));

            if (localOrder_created_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOrder_created_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "order_created_at cannot be null!!");
            }
        }

        if (localTotal_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "total_qty"));

            if (localTotal_qty != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localTotal_qty));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "total_qty cannot be null!!");
            }
        }

        if (localShipment_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "shipment_id"));

            if (localShipment_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localShipment_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "shipment_id cannot be null!!");
            }
        }

        if (localItemsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "items"));

            if (localItems == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "items cannot be null!!");
            }

            elementList.add(localItems);
        }

        if (localTracksTracker) {
            elementList.add(new javax.xml.namespace.QName("", "tracks"));

            if (localTracks == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "tracks cannot be null!!");
            }

            elementList.add(localTracks);
        }

        if (localCommentsTracker) {
            elementList.add(new javax.xml.namespace.QName("", "comments"));

            if (localComments == null) {
                throw new org.apache.axis2.databinding.ADBException(
                    "comments cannot be null!!");
            }

            elementList.add(localComments);
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
        public static SalesOrderShipmentEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            SalesOrderShipmentEntity object = new SalesOrderShipmentEntity();

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

                        if (!"salesOrderShipmentEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (SalesOrderShipmentEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "increment_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "increment_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIncrement_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "parent_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "parent_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setParent_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    object.setStore_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "created_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "created_at" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCreated_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
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

                    String content = reader.getElementText();

                    object.setUpdated_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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

                    object.setIs_active(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_address_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_address_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_address_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_firstname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_firstname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_firstname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipping_lastname").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipping_lastname" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipping_lastname(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_id" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setOrder_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_increment_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_increment_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setOrder_increment_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "order_created_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "order_created_at" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setOrder_created_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "total_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "total_qty" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setTotal_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "shipment_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "shipment_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setShipment_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "items").equals(
                            reader.getName())) {
                    object.setItems(magento.SalesOrderShipmentItemEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "tracks").equals(
                            reader.getName())) {
                    object.setTracks(magento.SalesOrderShipmentTrackEntityArray.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "comments").equals(
                            reader.getName())) {
                    object.setComments(magento.SalesOrderShipmentCommentEntityArray.Factory.parse(
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
