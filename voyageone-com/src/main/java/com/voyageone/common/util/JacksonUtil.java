package com.voyageone.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JacksonUtil {

    /**
     * 根据json字符串返回对应java类型
     *
     * @param obj Object
     * @return String
     */
    public static String bean2Json(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        mapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }
    public static String bean2JsonNotNull(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        mapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }

    /**
     * 根据json字符串返回对应java类型
     *
     * @param jsonStr String
     * @param cls Class<T>
     * @return T
     */
    public static <T> T json2Bean(String jsonStr, Class<T> cls) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, cls);
    }

    /**
     * 根据json字符串返回对应Map类型
     *
     * @param jsonString String
     * @return Map<String, Object>
     */
    public static Map<String, Object> jsonToMap(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * 根据json字符串返回对应java类型List
     *
     * @param jsonString String
     * @param cls Class
     * @return List
     */
    public static <T> List<T> jsonToBeanList(String jsonString, Class<T> cls) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, cls);
        return mapper.readValue(jsonString, javaType);
    }

    /**
     * 根据json字符串返回对应Map类型List
     *
     * @param jsonString String
     * @return List
     */
    public static List<Map<String, Object>> jsonToMapList(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, Map.class);
        return mapper.readValue(jsonString, javaType);
    }


}