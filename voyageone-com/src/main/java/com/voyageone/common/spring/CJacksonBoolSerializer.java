package com.voyageone.common.spring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CJacksonBoolSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.toString().toLowerCase());
    }
}