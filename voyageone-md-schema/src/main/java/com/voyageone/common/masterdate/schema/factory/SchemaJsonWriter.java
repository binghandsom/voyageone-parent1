package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.JsonUtil;

import java.util.List;


public class SchemaJsonWriter {

    public static String writeXmlToJsonString(String xmlStirng) throws TopSchemaException {
        List<Field> fields = SchemaReader.readXmlForList(xmlStirng);
        return writeString(fields);
    }

    public static String writeString(List<Field> fields) throws TopSchemaException {
        return JsonUtil.bean2Json(fields);
    }

    public static String writeString(Field field) throws TopSchemaException {
        return JsonUtil.bean2Json(field);
    }


}
