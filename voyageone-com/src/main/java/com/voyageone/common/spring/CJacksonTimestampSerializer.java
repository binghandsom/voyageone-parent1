package com.voyageone.common.spring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.voyageone.common.help.DateHelp;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *  java日期对象经过Jackson库转换成JSON日期格式化自定义类
 * @author godfox
 * @date 2010-5-3
 */
public class CJacksonTimestampSerializer extends JsonSerializer<Timestamp> {

    @Override
    public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        String result=null;
        if(!DateHelp.getDefaultDate().equals(value)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             result = formatter.format(value);
        }
        if(result==null) {
            jgen.writeString("");
        }
        else
        {
            jgen.writeString(result);
        }
    }
}