package com.voyageone.common.util;

import com.taobao.top.schema.exception.TopSchemaException;
import org.junit.Test;

import java.util.List;

public class SchemaConvertUtilTest {

    private String docStr1 = "";


    @Test
    public void testMasterConvertToTaobo() {
        List<com.voyageone.common.masterdate.schema.field.Field> fieldList = com.voyageone.common.masterdate.schema.factory.SchemaReader.readXmlForList(docStr1);

        System.out.println(SchemaConvertUtil.masterConvertToTaobo(fieldList));
    }

    @Test
    public void testTaoboConvertToMaster() throws TopSchemaException {
        List<com.taobao.top.schema.field.Field> fieldList = com.taobao.top.schema.factory.SchemaReader.readXmlForList(docStr1);

        System.out.println(SchemaConvertUtil.taoboConvertToMaster(fieldList));
    }
}
