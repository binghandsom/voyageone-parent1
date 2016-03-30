package com.voyageone.common.serialize.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SuppressWarnings("rawtypes")
public class JsonBeanSerializeModule {

	List<JsonSerializer> serializers;
	List<JsonDeserializer> deserializers;
	SimpleModule jsonBeanModule;
	ObjectMapper objectMapper;
	
	
	public JsonBeanSerializeModule() {
		jsonBeanModule = new SimpleModule();
		serializers = new ArrayList<>();
		deserializers = new ArrayList<>();
		
		addSerializerAndDeserializer(JsonBean.class, new JsonBeanSerializer(JsonBean.class), new JsonBeanDeserializer());
	}
	
	@SuppressWarnings("unchecked")
	public void addSerializerAndDeserializer(Class clazz, JsonBeanSerializer serializer, JsonBeanDeserializer deserializer)
	{
		if (serializer != null)
		{
			jsonBeanModule.addSerializer(serializer);
			serializers.add(serializer);
			serializer.setModule(this);
		}
		
		if (deserializer != null)
		{
			jsonBeanModule.addDeserializer(clazz, deserializer);
			deserializers.add(deserializer);
			deserializer.setModule(this);
		}
	}
	
	public void attach(ObjectMapper om)
	{
		om.registerModule(jsonBeanModule);
		objectMapper = om;
	}

	public List<JsonSerializer> getSerializers() {
		return serializers;
	}

	public void setSerializers(List<JsonSerializer> serializers) {
		this.serializers = serializers;
	}

	public SimpleModule getJsonBeanModule() {
		return jsonBeanModule;
	}

	public void setJsonBeanModule(SimpleModule jsonBeanModule) {
		this.jsonBeanModule = jsonBeanModule;
	}

	public List<JsonDeserializer> getDeserializers() {
		return deserializers;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	
	
}
