package com.voyageone.common.serialize;

public interface Serializer {

	String serialize(Object obj) throws Exception;
	
	Object deserialize(String str) throws Exception;
}
