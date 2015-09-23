package com.voyageone.common.serialize;


import com.voyageone.common.serialize.json.JsonSerializerFactory;

public class SerializerFactory {

	@SuppressWarnings("unchecked")
	public static <T extends Serializer> T getSerializer(SerializeType serializeType)
	{
		switch(serializeType)
		{
		case JSON:
			return (T) JsonSerializerFactory.getSerializer();
		case XML:
			return null;
		default:
			return null;
		}
	}
}
