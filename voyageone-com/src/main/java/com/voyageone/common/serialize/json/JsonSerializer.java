package com.voyageone.common.serialize.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.common.serialize.Serializer;

public class JsonSerializer implements Serializer {

    private ObjectMapper objectMapper;

    public JsonSerializer() {
        objectMapper = new ObjectMapper();

        new JsonBeanSerializeModule().attach(objectMapper);
        new TmallSerializeModule().attach(objectMapper);
    }

    @Override
    public String serialize(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new JsonBean(obj));
    }

    @Override
    public Object deserialize(String str) throws IOException {
        return objectMapper.readValue(str, JsonBean.class).getObj();
    }

}
