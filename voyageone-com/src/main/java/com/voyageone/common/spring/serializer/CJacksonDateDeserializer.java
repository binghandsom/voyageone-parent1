package com.voyageone.common.spring.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.voyageone.common.util.DateTimeUtil;

import java.io.IOException;
import java.util.Date;

/**
 * java日期对象经过Jackson库转换成JSON日期格式化自定义类
 *
 * @author godfox 2010-5-3
 */
public class CJacksonDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String fieldData = parser.getText();
        //Timestamp
        if (fieldData == null || fieldData.equals("")) {
            return DateTimeUtil.getCreatedDefaultDate();
        }
        if (fieldData.length() < 19) {
            fieldData += "0000-00-00 00:00:00".substring(fieldData.length());
        }

        return DateTimeUtil.parse(fieldData);
    }
}