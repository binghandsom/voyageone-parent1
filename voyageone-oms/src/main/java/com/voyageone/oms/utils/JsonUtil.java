/**
 * 
 */
package com.voyageone.oms.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * @author jacky
 *
 */
public class JsonUtil {

	/**
	 * 根据json字符串返回对应java类型
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T jsonToBean(String jsonString, Class<T> cls) {
		T t = null;
		
		Gson gson = new Gson();
		t = gson.fromJson(jsonString, cls);
		
		return t;
	}
	
	/**
	 * 根据json字符串返回对应java类型List
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> jsonToBeanList(String jsonString, Class<T> cls) {
		Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
		ArrayList<JsonObject> jsonList = new Gson().fromJson(jsonString, type);
		
		ArrayList<T> listCls = new ArrayList<T>();
		if (jsonList != null && jsonList.size() > 0) {
			for (JsonObject json : jsonList) {
				listCls.add(new Gson().fromJson(json, cls));
			}
		}
		
		return listCls;
	}
	
	/**
	 * 根据json字符串返回对应Map类型
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String jsonString) {
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		map = gson.fromJson(jsonString, Map.class);
		
		return map;
	}
	
	/**
	 * 根据json字符串返回对应Map类型List
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> jsonToMapList(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Gson gson = new Gson();
		list = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>(){}.getType());
		
		return list;
	}
	
	/**
	 * 根据json字符串返回对应java类型
	 * 
	 * @param data
	 * @return
	 */
	public static String getJsonString(Object data) {
		return new GsonBuilder().serializeNulls().create().toJson(data);
	}
	
	/**
	 * 替换掉字符串中的双引号
	 * @return
	 */
	public static String replaceDoubleQuota(String before) {
		if (before == null || "".equals(before)) {
			return before;
		}
		
		String after = before.replace("\"", "'").replace("″", "'").replace("&quot;", "'").replace("&quot", "'");
		return after;
	}
}
