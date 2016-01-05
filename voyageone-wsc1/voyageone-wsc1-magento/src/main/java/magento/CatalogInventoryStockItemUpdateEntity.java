/**
 * CatalogInventoryStockItemUpdateEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogInventoryStockItemUpdateEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogInventoryStockItemUpdateEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogInventoryStockItemUpdateEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Qty
     */
    protected String localQty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localQtyTracker = false;

    /**
     * field for Is_in_stock
     */
    protected int localIs_in_stock;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_in_stockTracker = false;

    /**
     * field for Manage_stock
     */
    protected int localManage_stock;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localManage_stockTracker = false;

    /**
     * field for Use_config_manage_stock
     */
    protected int localUse_config_manage_stock;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUse_config_manage_stockTracker = false;

    /**
     * field for Min_qty
     */
    protected int localMin_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMin_qtyTracker = false;

    /**
     * field for Use_config_min_qty
     */
    protected int localUse_config_min_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUse_config_min_qtyTracker = false;

    /**
     * field for Min_sale_qty
     */
    protected int localMin_sale_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMin_sale_qtyTracker = false;

    /**
     * field for Use_config_min_sale_qty
     */
    protected int localUse_config_min_sale_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUse_config_min_sale_qtyTracker = false;

    /**
     * field for Max_sale_qty
     */
    protected int localMax_sale_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMax_sale_qtyTracker = false;

    /**
     * field for Use_config_max_sale_qty
     */
    protected int localUse_config_max_sale_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUse_config_max_sale_qtyTracker = false;

    /**
     * field for Is_qty_decimal
     */
    protected int localIs_qty_decimal;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIs_qty_decimalTracker = false;

    /**
     * field for Backorders
     */
    protected int localBackorders;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBackordersTracker = false;

    /**
     * field for Use_config_backorders
     */
    protected int localUse_config_backorders;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUse_config_backordersTracker = false;

    /**
     * field for Notify_stock_qty
     */
    protected int localNotify_stock_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localNotify_stock_qtyTracker = false;

    /**
     * field for Use_config_notify_stock_qty
     */
    protected int localUse_config_notify_stock_qty;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUse_config_notify_stock_qtyTracker = false;

    public boolean isQtySpecified() {
        return localQtyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getQty() {
        return localQty;
    }

    /**
     * Auto generated setter method
     * @param param Qty
     */
    public void setQty(String param) {
        localQtyTracker = param != null;

        this.localQty = param;
    }

    public boolean isIs_in_stockSpecified() {
        return localIs_in_stockTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_in_stock() {
        return localIs_in_stock;
    }

    /**
     * Auto generated setter method
     * @param param Is_in_stock
     */
    public void setIs_in_stock(int param) {
        // setting primitive attribute tracker to true
        localIs_in_stockTracker = param != Integer.MIN_VALUE;

        this.localIs_in_stock = param;
    }

    public boolean isManage_stockSpecified() {
        return localManage_stockTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getManage_stock() {
        return localManage_stock;
    }

    /**
     * Auto generated setter method
     * @param param Manage_stock
     */
    public void setManage_stock(int param) {
        // setting primitive attribute tracker to true
        localManage_stockTracker = param != Integer.MIN_VALUE;

        this.localManage_stock = param;
    }

    public boolean isUse_config_manage_stockSpecified() {
        return localUse_config_manage_stockTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUse_config_manage_stock() {
        return localUse_config_manage_stock;
    }

    /**
     * Auto generated setter method
     * @param param Use_config_manage_stock
     */
    public void setUse_config_manage_stock(int param) {
        // setting primitive attribute tracker to true
        localUse_config_manage_stockTracker = param != Integer.MIN_VALUE;

        this.localUse_config_manage_stock = param;
    }

    public boolean isMin_qtySpecified() {
        return localMin_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getMin_qty() {
        return localMin_qty;
    }

    /**
     * Auto generated setter method
     * @param param Min_qty
     */
    public void setMin_qty(int param) {
        // setting primitive attribute tracker to true
        localMin_qtyTracker = param != Integer.MIN_VALUE;

        this.localMin_qty = param;
    }

    public boolean isUse_config_min_qtySpecified() {
        return localUse_config_min_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUse_config_min_qty() {
        return localUse_config_min_qty;
    }

    /**
     * Auto generated setter method
     * @param param Use_config_min_qty
     */
    public void setUse_config_min_qty(int param) {
        // setting primitive attribute tracker to true
        localUse_config_min_qtyTracker = param != Integer.MIN_VALUE;

        this.localUse_config_min_qty = param;
    }

    public boolean isMin_sale_qtySpecified() {
        return localMin_sale_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getMin_sale_qty() {
        return localMin_sale_qty;
    }

    /**
     * Auto generated setter method
     * @param param Min_sale_qty
     */
    public void setMin_sale_qty(int param) {
        // setting primitive attribute tracker to true
        localMin_sale_qtyTracker = param != Integer.MIN_VALUE;

        this.localMin_sale_qty = param;
    }

    public boolean isUse_config_min_sale_qtySpecified() {
        return localUse_config_min_sale_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUse_config_min_sale_qty() {
        return localUse_config_min_sale_qty;
    }

    /**
     * Auto generated setter method
     * @param param Use_config_min_sale_qty
     */
    public void setUse_config_min_sale_qty(int param) {
        // setting primitive attribute tracker to true
        localUse_config_min_sale_qtyTracker = param != Integer.MIN_VALUE;

        this.localUse_config_min_sale_qty = param;
    }

    public boolean isMax_sale_qtySpecified() {
        return localMax_sale_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getMax_sale_qty() {
        return localMax_sale_qty;
    }

    /**
     * Auto generated setter method
     * @param param Max_sale_qty
     */
    public void setMax_sale_qty(int param) {
        // setting primitive attribute tracker to true
        localMax_sale_qtyTracker = param != Integer.MIN_VALUE;

        this.localMax_sale_qty = param;
    }

    public boolean isUse_config_max_sale_qtySpecified() {
        return localUse_config_max_sale_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUse_config_max_sale_qty() {
        return localUse_config_max_sale_qty;
    }

    /**
     * Auto generated setter method
     * @param param Use_config_max_sale_qty
     */
    public void setUse_config_max_sale_qty(int param) {
        // setting primitive attribute tracker to true
        localUse_config_max_sale_qtyTracker = param != Integer.MIN_VALUE;

        this.localUse_config_max_sale_qty = param;
    }

    public boolean isIs_qty_decimalSpecified() {
        return localIs_qty_decimalTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIs_qty_decimal() {
        return localIs_qty_decimal;
    }

    /**
     * Auto generated setter method
     * @param param Is_qty_decimal
     */
    public void setIs_qty_decimal(int param) {
        // setting primitive attribute tracker to true
        localIs_qty_decimalTracker = param != Integer.MIN_VALUE;

        this.localIs_qty_decimal = param;
    }

    public boolean isBackordersSpecified() {
        return localBackordersTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getBackorders() {
        return localBackorders;
    }

    /**
     * Auto generated setter method
     * @param param Backorders
     */
    public void setBackorders(int param) {
        // setting primitive attribute tracker to true
        localBackordersTracker = param != Integer.MIN_VALUE;

        this.localBackorders = param;
    }

    public boolean isUse_config_backordersSpecified() {
        return localUse_config_backordersTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUse_config_backorders() {
        return localUse_config_backorders;
    }

    /**
     * Auto generated setter method
     * @param param Use_config_backorders
     */
    public void setUse_config_backorders(int param) {
        // setting primitive attribute tracker to true
        localUse_config_backordersTracker = param != Integer.MIN_VALUE;

        this.localUse_config_backorders = param;
    }

    public boolean isNotify_stock_qtySpecified() {
        return localNotify_stock_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getNotify_stock_qty() {
        return localNotify_stock_qty;
    }

    /**
     * Auto generated setter method
     * @param param Notify_stock_qty
     */
    public void setNotify_stock_qty(int param) {
        // setting primitive attribute tracker to true
        localNotify_stock_qtyTracker = param != Integer.MIN_VALUE;

        this.localNotify_stock_qty = param;
    }

    public boolean isUse_config_notify_stock_qtySpecified() {
        return localUse_config_notify_stock_qtyTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getUse_config_notify_stock_qty() {
        return localUse_config_notify_stock_qty;
    }

    /**
     * Auto generated setter method
     * @param param Use_config_notify_stock_qty
     */
    public void setUse_config_notify_stock_qty(int param) {
        // setting primitive attribute tracker to true
        localUse_config_notify_stock_qtyTracker = param != Integer.MIN_VALUE;

        this.localUse_config_notify_stock_qty = param;
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
                    namespacePrefix + ":catalogInventoryStockItemUpdateEntity",
                    xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogInventoryStockItemUpdateEntity", xmlWriter);
            }
        }

        if (localQtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "qty", xmlWriter);

            if (localQty == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localQty);
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_in_stockTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_in_stock", xmlWriter);

            if (localIs_in_stock == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_in_stock cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_in_stock));
            }

            xmlWriter.writeEndElement();
        }

        if (localManage_stockTracker) {
            namespace = "";
            writeStartElement(null, namespace, "manage_stock", xmlWriter);

            if (localManage_stock == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "manage_stock cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localManage_stock));
            }

            xmlWriter.writeEndElement();
        }

        if (localUse_config_manage_stockTracker) {
            namespace = "";
            writeStartElement(null, namespace, "use_config_manage_stock",
                xmlWriter);

            if (localUse_config_manage_stock == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "use_config_manage_stock cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUse_config_manage_stock));
            }

            xmlWriter.writeEndElement();
        }

        if (localMin_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "min_qty", xmlWriter);

            if (localMin_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "min_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMin_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localUse_config_min_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "use_config_min_qty", xmlWriter);

            if (localUse_config_min_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "use_config_min_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUse_config_min_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localMin_sale_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "min_sale_qty", xmlWriter);

            if (localMin_sale_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "min_sale_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMin_sale_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localUse_config_min_sale_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "use_config_min_sale_qty",
                xmlWriter);

            if (localUse_config_min_sale_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "use_config_min_sale_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUse_config_min_sale_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localMax_sale_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "max_sale_qty", xmlWriter);

            if (localMax_sale_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "max_sale_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMax_sale_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localUse_config_max_sale_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "use_config_max_sale_qty",
                xmlWriter);

            if (localUse_config_max_sale_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "use_config_max_sale_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUse_config_max_sale_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localIs_qty_decimalTracker) {
            namespace = "";
            writeStartElement(null, namespace, "is_qty_decimal", xmlWriter);

            if (localIs_qty_decimal == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "is_qty_decimal cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIs_qty_decimal));
            }

            xmlWriter.writeEndElement();
        }

        if (localBackordersTracker) {
            namespace = "";
            writeStartElement(null, namespace, "backorders", xmlWriter);

            if (localBackorders == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "backorders cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localBackorders));
            }

            xmlWriter.writeEndElement();
        }

        if (localUse_config_backordersTracker) {
            namespace = "";
            writeStartElement(null, namespace, "use_config_backorders",
                xmlWriter);

            if (localUse_config_backorders == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "use_config_backorders cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUse_config_backorders));
            }

            xmlWriter.writeEndElement();
        }

        if (localNotify_stock_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "notify_stock_qty", xmlWriter);

            if (localNotify_stock_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "notify_stock_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localNotify_stock_qty));
            }

            xmlWriter.writeEndElement();
        }

        if (localUse_config_notify_stock_qtyTracker) {
            namespace = "";
            writeStartElement(null, namespace, "use_config_notify_stock_qty",
                xmlWriter);

            if (localUse_config_notify_stock_qty == Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "use_config_notify_stock_qty cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUse_config_notify_stock_qty));
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

        if (localQtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "qty"));

            if (localQty != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localQty));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "qty cannot be null!!");
            }
        }

        if (localIs_in_stockTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_in_stock"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_in_stock));
        }

        if (localManage_stockTracker) {
            elementList.add(new javax.xml.namespace.QName("", "manage_stock"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localManage_stock));
        }

        if (localUse_config_manage_stockTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "use_config_manage_stock"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUse_config_manage_stock));
        }

        if (localMin_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "min_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localMin_qty));
        }

        if (localUse_config_min_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "use_config_min_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUse_config_min_qty));
        }

        if (localMin_sale_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "min_sale_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localMin_sale_qty));
        }

        if (localUse_config_min_sale_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "use_config_min_sale_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUse_config_min_sale_qty));
        }

        if (localMax_sale_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "max_sale_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localMax_sale_qty));
        }

        if (localUse_config_max_sale_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "use_config_max_sale_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUse_config_max_sale_qty));
        }

        if (localIs_qty_decimalTracker) {
            elementList.add(new javax.xml.namespace.QName("", "is_qty_decimal"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIs_qty_decimal));
        }

        if (localBackordersTracker) {
            elementList.add(new javax.xml.namespace.QName("", "backorders"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localBackorders));
        }

        if (localUse_config_backordersTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "use_config_backorders"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUse_config_backorders));
        }

        if (localNotify_stock_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("", "notify_stock_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localNotify_stock_qty));
        }

        if (localUse_config_notify_stock_qtyTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "use_config_notify_stock_qty"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localUse_config_notify_stock_qty));
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
        public static CatalogInventoryStockItemUpdateEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            CatalogInventoryStockItemUpdateEntity object = new CatalogInventoryStockItemUpdateEntity();

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

                        if (!"catalogInventoryStockItemUpdateEntity".equals(
                                    type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogInventoryStockItemUpdateEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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

                    object.setQty(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_in_stock").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_in_stock" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_in_stock(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_in_stock(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "manage_stock").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "manage_stock" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setManage_stock(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setManage_stock(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "use_config_manage_stock").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "use_config_manage_stock" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUse_config_manage_stock(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUse_config_manage_stock(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "min_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "min_qty" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setMin_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setMin_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "use_config_min_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "use_config_min_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUse_config_min_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUse_config_min_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "min_sale_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "min_sale_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setMin_sale_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setMin_sale_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "use_config_min_sale_qty").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "use_config_min_sale_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUse_config_min_sale_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUse_config_min_sale_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "max_sale_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "max_sale_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setMax_sale_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setMax_sale_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "use_config_max_sale_qty").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "use_config_max_sale_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUse_config_max_sale_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUse_config_max_sale_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "is_qty_decimal").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "is_qty_decimal" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setIs_qty_decimal(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIs_qty_decimal(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "backorders").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "backorders" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setBackorders(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setBackorders(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "use_config_backorders").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "use_config_backorders" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUse_config_backorders(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUse_config_backorders(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "notify_stock_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "notify_stock_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setNotify_stock_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setNotify_stock_qty(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "use_config_notify_stock_qty").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "use_config_notify_stock_qty" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUse_config_notify_stock_qty(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setUse_config_notify_stock_qty(Integer.MIN_VALUE);
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
