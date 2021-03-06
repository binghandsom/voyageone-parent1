package com.voyageone.common.spring.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * java日期对象经过Jackson库转换成JSON日期格式化自定义类
 *
 * @author godfox 2010-5-3
 */
public class CJacksonDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String fieldData = parser.getText();

        if (CJacksonSerializerUtil.isCustom()) {
            if (fieldData == null || "".equals(fieldData)) {
                return DateTimeUtil.getCreatedDefaultDate();
            }

            if (StringUtils.isNumeric(fieldData)) {
                return DateDeserializers.DateDeserializer.instance.deserialize(parser, context);
            } else {
                if (fieldData.length() < 19) {
                    fieldData += "0000-00-00 00:00:00".substring(fieldData.length());
                }
                return DateTimeUtil.parse(fieldData);
            }
        } else {
            return DateDeserializers.DateDeserializer.instance.deserialize(parser, context);
        }
    }
}