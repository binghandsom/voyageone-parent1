package com.voyageone.common.serialize.json;


public class JsonSerializerFactory {

	private static JsonSerializer jsonSerializer = null;
	
	public static JsonSerializer getSerializer() {
		if (jsonSerializer == null)
		{
			jsonSerializer = new JsonSerializer();
		}
		
		return jsonSerializer;
	}
}
