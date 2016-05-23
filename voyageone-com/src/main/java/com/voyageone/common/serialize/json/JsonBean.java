package com.voyageone.common.serialize.json;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={"obj"})
public class JsonBean {
	
	public static final String TYPE_STR = "type";
	public static final String VALUE_STR = "value";
	
	private String type;
	private String value;
	private Object obj;
	
	public JsonBean() {}
	
	public JsonBean(Object o)
	{
		obj = o;
		type = o.getClass().getName();
	}
	
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
