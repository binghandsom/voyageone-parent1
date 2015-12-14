package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.Util.JsonUtil;
import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;


public class SchemaJsonWriter {

    public static String writeXmlToJsonString(String xmlStirng) {
        List<Field> fields = SchemaReader.readXmlForList(xmlStirng);
        return writeString(fields);
    }

    public static String writeString(List<Field> fields) {
        return JsonUtil.bean2Json(fields);
    }

    public static String writeString(Field field) {
        return JsonUtil.bean2Json(field);
    }


}
