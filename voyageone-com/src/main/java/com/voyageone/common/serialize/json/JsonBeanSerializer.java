package com.voyageone.common.serialize.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonBeanSerializer extends StdSerializer<JsonBean> {

	private JsonBeanSerializeModule module;
	
	protected JsonBeanSerializer(Class<JsonBean> t) {
		super(t);
	}

	@Override
	public void serialize(JsonBean value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		ObjectMapper om = module.getObjectMapper();

		String beanValue = om.writeValueAsString(value.getObj());
		value.setValue(beanValue);
		
		jgen.writeStartObject();
		
		jgen.writeFieldName("type");
		jgen.writeString(value.getType());
		jgen.writeFieldName("value");
		jgen.writeString(value.getValue());
		
		jgen.writeEndObject();
	}

	public JsonBeanSerializeModule getModule() {
		return module;
	}

	public void setModule(JsonBeanSerializeModule module) {
		this.module = module;
	}

}
