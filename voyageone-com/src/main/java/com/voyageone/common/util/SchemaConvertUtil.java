package com.voyageone.common.util;

import com.taobao.top.schema.exception.TopSchemaException;

import java.util.List;


public class SchemaConvertUtil {

    public static List<com.taobao.top.schema.field.Field> masterConvertToTaobo(List<com.voyageone.common.masterdate.schema.field.Field> inputSchemaList) {
        try {
            String docStr = com.voyageone.common.masterdate.schema.factory.SchemaWriter.writeParamXmlString(inputSchemaList);
            return com.taobao.top.schema.factory.SchemaReader.readXmlForList(docStr);
        } catch (TopSchemaException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<com.voyageone.common.masterdate.schema.field.Field> taoboConvertToMaster(List<com.taobao.top.schema.field.Field> inputSchemaList) {
        try {
            String docStr = com.taobao.top.schema.factory.SchemaWriter.writeParamXmlString(inputSchemaList);
            return com.voyageone.common.masterdate.schema.factory.SchemaReader.readXmlForList(docStr);
        } catch (TopSchemaException e) {
            throw new RuntimeException(e);
        }
    }

}
