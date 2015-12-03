package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.Util.XmlUtils;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.Field;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;


public class SchemaWriter {
    public SchemaWriter() {
    }

    public static String writeRuleXmlString(List<Field> fields) throws TopSchemaException {
        Element root = XmlUtils.createRootElement("itemRule");
        Iterator i$ = fields.iterator();

        while(i$.hasNext()) {
            Field field = (Field)i$.next();
            Element fieldNode = field.toElement();
            XmlUtils.appendElement(root, fieldNode);
        }

        return XmlUtils.nodeToString(root);
    }

    public static String writeParamXmlString(List<Field> fields) throws TopSchemaException {
        Element root = XmlUtils.createRootElement("itemParam");
        Iterator i$ = fields.iterator();

        while(i$.hasNext()) {
            Field field = (Field)i$.next();
            Element fieldNode = field.toParamElement();
            XmlUtils.appendElement(root, fieldNode);
        }

        return XmlUtils.nodeToString(root);
    }
}
