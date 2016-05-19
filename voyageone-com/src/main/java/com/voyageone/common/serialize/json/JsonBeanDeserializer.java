package com.voyageone.common.serialize.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonBeanDeserializer extends JsonDeserializer<JsonBean> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JsonBeanSerializeModule module;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonBean deserialize(JsonParser p,
			DeserializationContext ctxt) throws IOException {
		JsonBean jsonBean = new JsonBean();
		
		p.nextToken();
		
		do {
			if (JsonBean.TYPE_STR.equals(p.getCurrentName())) {
				jsonBean.setType(p.nextTextValue());
			} else if (JsonBean.VALUE_STR.equals(p.getCurrentName())) {
				jsonBean.setValue(p.nextTextValue());
			}
		} while (p.nextToken() != JsonToken.END_OBJECT);

		Class clazz;
		try {
			clazz = Class.forName(jsonBean.getType());
			Object obj = module.getObjectMapper().readValue(jsonBean.getValue(), clazz);
/*
			JsonDeserializer<Object> objDeserializer = ctxt.findRootValueDeserializer(ctxt.getTypeFactory().constructType(clazz));
			Object obj = objDeserializer.deserialize(p, ctxt);
*/
			jsonBean.setObj(obj);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return jsonBean;
	}

	public JsonBeanSerializeModule getModule() {
		return module;
	}

	public void setModule(JsonBeanSerializeModule module) {
		this.module = module;
	}

}
