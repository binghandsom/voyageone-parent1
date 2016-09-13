package com.voyageone.common.spring.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.voyageone.common.util.DateTimeUtil;

import java.io.IOException;
import java.util.Date;


/**
 * java日期对象经过Jackson库转换成JSON日期格式化自定义类
 *
 * @author godfox 2010-5-3
 */
public class CJacksonDateSerializer extends DateSerializer {

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        if (CJacksonSerializerUtil.isCustom()) {
            String result = null;
            if (!DateTimeUtil.getCreatedDefaultDate().equals(value)) {
                result = DateTimeUtil.format(value, null);
            }
            if (result == null) {
                jgen.writeString("");
            } else {
                jgen.writeString(result);
            }
        } else {
            super.serialize(value, jgen, provider);
        }
    }
}