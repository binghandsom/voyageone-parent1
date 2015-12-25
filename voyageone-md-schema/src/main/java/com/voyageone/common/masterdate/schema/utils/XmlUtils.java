package com.voyageone.common.masterdate.schema.utils;

import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class XmlUtils {
    private static final String DEFAULT_ENCODE = "UTF-8";

    public XmlUtils() {
    }

    public static Document newDocument() {
        Document doc = null;
        doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        return doc;
    }

    public static Document getDocument(File file) throws TopSchemaException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        Document doc = null;

        try {
            doc = reader.read(file);
            return doc;
        } catch (DocumentException var4) {
            throw new TopSchemaException(var4.getMessage());
        }
    }

    public static Document getDocument(InputStream xml) throws TopSchemaException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        Document doc = null;

        try {
            doc = reader.read(xml);
            return doc;
        } catch (DocumentException var4) {
            throw new TopSchemaException(var4.getMessage());
        }
    }

    public static Document getDocument(InputSource xml) throws TopSchemaException {
        SAXReader reader = new SAXReader();
        Document doc = null;

        try {
            doc = reader.read(xml);
            return doc;
        } catch (DocumentException var4) {
            throw new TopSchemaException(var4.getMessage());
        }
    }

    public static Element createRootElement(String tagName) throws TopSchemaException {
        Document doc = newDocument();
        Element root = DocumentHelper.createElement(tagName);
        doc.add(root);
        return root;
    }

    public static Element getRootElementFromStream(InputStream xml) throws TopSchemaException {
        return getDocument(xml).getRootElement();
    }

    public static Element getRootElementFromFile(File xml) throws TopSchemaException {
        return getDocument(xml).getRootElement();
    }

    public static Element getRootElementFromString(String payload) throws TopSchemaException {
        if (payload != null && payload.length() >= 1) {
            StringReader sr = new StringReader(escapeXml(payload));
            InputSource source = new InputSource(sr);
            return getDocument(source).getRootElement();
        } else {
            throw new TopSchemaException("XML_PAYLOAD_EMPTY");
        }
    }

    public static String escapeXml(String payload) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < payload.length(); ++i) {
            char c = payload.charAt(i);
            if (c == 9 || c == 10 || c == 13 || c >= 32 && c <= '\ud7ff' || c >= '\ue000' && c <= '�' || c >= 65536 && c <= 1114111) {
                out.append(c);
            }
        }

        return out.toString();
    }

    public static String xmlToString(File file) throws TopSchemaException {
        return getDocument(file).asXML();
    }

    public static List<Element> getChildElements(Element parent, String tagName) {
        List allNodes = parent.elements(tagName);
        List<Element> elements = new ArrayList<>();

        for (Object allNode : allNodes) {
            if (allNode instanceof Element) {
                Element node = (Element) allNode;
                if (node.getParent() == parent) {
                    elements.add(node);
                }
            }
        }

        return elements;
    }

    public static List<Element> getElements(Element parent, String tagName) {
        List allNodes = parent.elements();
        List<Element> elements = new ArrayList<>();

        for (Object allNode : allNodes) {
            if (allNode instanceof Element) {
                Element node = (Element) allNode;
                elements.add(node);
            }
        }

        return elements;
    }

    public static Element getElement(Element parent, String tagName) {
        List children = getElements(parent, tagName);
        return children.isEmpty() ? null : (Element) children.get(0);
    }

    public static Element getChildElement(Element parent, String tagName) {
        List children = getChildElements(parent, tagName);
        return children.isEmpty() ? null : (Element) children.get(0);
    }

    public static String getElementValue(Element parent, String tagName) {
        Element element = getElement(parent, tagName);
        return element != null ? element.getText() : null;
    }

    public static String getElementValue(Element element) {
        return !StringUtil.isEmpty(element.getText()) ? element.getText() : null;
    }

    public static String getAttributeValue(Element current, String attrName) {
        return current.attributeValue(attrName);
    }

    public static Element appendElement(Element parent, String tagName) {
        return parent.addElement(tagName);
    }

    public static void appendElement(Element parent, Element child) {
        parent.add(child);
    }

    public static String nodeToString(Node node) throws TopSchemaException {
        return node.asXML();
    }

    public static String xmlToString(InputStream in) throws TopSchemaException {
        Element root = getRootElementFromStream(in);
        return nodeToString(root);
    }

    public static void saveToXml(Node doc, File file) throws TopSchemaException {
        FileWriter out = null;

        try {
            out = new FileWriter(file);
            out.write(doc.getDocument().asXML());
        } catch (IOException var11) {
            throw new TopSchemaException(var11.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {
            }
        }

    }

    public static void saveToXml(String xml, File file) throws TopSchemaException {
        FileWriter out = null;

        try {
            out = new FileWriter(file);
            out.write(xml);
        } catch (IOException var11) {
            throw new TopSchemaException(var11.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {
            }
        }

    }
}
