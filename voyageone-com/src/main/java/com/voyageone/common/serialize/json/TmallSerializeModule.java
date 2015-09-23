package com.voyageone.common.serialize.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.rule.Rule;

@SuppressWarnings("rawtypes")
public class TmallSerializeModule{

	List<JsonSerializer> serializers;
	List<JsonDeserializer> deserializers;
	SimpleModule tmallFieldModule;
	
	ObjectMapper objectMapper;
	
	
	public TmallSerializeModule() {
		tmallFieldModule = new SimpleModule();
		serializers = new ArrayList<>();
		deserializers = new ArrayList<>();
		
		addSerializerAndDeserializer(Field.class, null, new TmallFieldDeserializer());
		addSerializerAndDeserializer(Rule.class, null, new TmallRuleDeserializer());
	}
	
	@SuppressWarnings("unchecked")
	public void addSerializerAndDeserializer(Class clazz, JsonSerializer<?> serializer, JsonDeserializer<?> deserializer)
	{
		if (serializer != null)
		{
			tmallFieldModule.addSerializer(serializer);
			serializers.add(serializer);
		}
		
		if (deserializer != null)
		{
			tmallFieldModule.addDeserializer(clazz, deserializer);
			deserializers.add(deserializer);
			if (Rule.class == clazz)
			{
				TmallRuleDeserializer tmallRuleDeserializer = (TmallRuleDeserializer) deserializer;
				tmallRuleDeserializer.setModule(this);
			}
		}
	}
	
	public void attach(ObjectMapper om)
	{
		om.registerModule(tmallFieldModule);
		objectMapper = om;
	}

	public List<JsonSerializer> getSerializers() {
		return serializers;
	}

	public void setSerializers(List<JsonSerializer> serializers) {
		this.serializers = serializers;
	}

	public SimpleModule getTmallFieldModule() {
		return tmallFieldModule;
	}

	public void setTmallFieldModule(SimpleModule tmallFieldModule) {
		this.tmallFieldModule = tmallFieldModule;
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
