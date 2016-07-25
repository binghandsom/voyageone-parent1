package com.voyageone.common.spring.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * java日期对象经过Jackson库转换成JSON日期格式化自定义类
 *
 * @author godfox
 * @date 2010-5-3
 */
public class CJacksonTimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser parser, DeserializationContext context) throws IOException{
        String fieldData = parser.getText();
        if (StringUtils.isNumeric(fieldData)) {
            return new Timestamp(Long.parseLong(fieldData));
        }

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {

            //Timestamp
            if (fieldData == null || "".equals(fieldData)) {
                return new Timestamp(DateTimeUtil.getCreatedDefaultDate().getTime());
            }
            if (fieldData.length() < 19) {
                fieldData += "0000-00-00 00:00:00".substring(fieldData.length());
            }

            return new Timestamp(sdf.parse(fieldData).getTime());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}