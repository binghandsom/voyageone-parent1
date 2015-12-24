package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.util.XmlUtils;
import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;
import org.dom4j.Element;


public class SchemaWriter {

    public static String writeRuleXmlString(List<Field> fields) {
        Element root = XmlUtils.createRootElement("itemRule");

        for (Field field : fields) {
            Element fieldNode = field.toElement();
            XmlUtils.appendElement(root, fieldNode);
        }

        return XmlUtils.nodeToString(root);
    }

    public static String writeParamXmlString(List<Field> fields) {
        Element root = XmlUtils.createRootElement("itemParam");

        for (Field field : fields) {
            Element fieldNode = field.toParamElement();
            XmlUtils.appendElement(root, fieldNode);
        }

        return XmlUtils.nodeToString(root);
    }
}
