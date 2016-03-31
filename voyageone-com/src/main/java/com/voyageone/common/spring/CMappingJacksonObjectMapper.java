package com.voyageone.common.spring;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CMappingJacksonObjectMapper extends ObjectMapper {
    public static Logger logger = LoggerFactory.getLogger(CMappingJacksonObjectMapper.class);
    public CMappingJacksonObjectMapper() {
        super();
        //long 自动按字符串序列化
        this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        //按方法get set 后名字大小写序列化
//        this.setPropertyNamingStrategy(new CDefaultPropertyNamingStrategy());
        //自定义显示日期格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // formatter.parse()
        this.setDateFormat(formatter);

        //UTCDateDeserializer utcDateDeserializer = new UTCDateDeserializer();
        SimpleModule newModule = new SimpleModule("CJacksonDateSerializer", Version.unknownVersion());
        newModule.addSerializer(Date.class, new CJacksonDateSerializer());
        newModule.addDeserializer(Date.class, new CJacksonDateDeserializer());
        this.registerModule(newModule);
    }

    @Override
    protected Object _readMapAndClose(JsonParser jp, JavaType valueType)
            throws IOException, JsonParseException, JsonMappingException {
        try {
            return super._readMapAndClose(jp, valueType);
        }
        catch (IOException ex) {
            // ServletContext
            String msg="反序列化错误:"+ex.getMessage();
            logger.error(msg);
            throw  ex;
        }
    }

    @Override
    public void writeValue(OutputStream out, Object value) throws IOException {
        super.writeValue(out, value);
    }
}
