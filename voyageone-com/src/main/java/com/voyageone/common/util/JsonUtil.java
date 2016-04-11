package com.voyageone.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jacky
 */
public final class JsonUtil {

    /**
     * 根据json字符串返回对应java类型
     */
    public static <T> T jsonToBean(String jsonString, Class<T> cls) {

        Gson gson = new Gson();

        return gson.fromJson(jsonString, cls);
    }

    /**
     * 根据json字符串返回对应java类型List
     */
    public static <T> List<T> jsonToBeanList(String jsonString, Class<T> cls) {

        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonList = new Gson().fromJson(jsonString, type);

        ArrayList<T> listCls = new ArrayList<T>();
        if (jsonList != null && !jsonList.isEmpty()) {
            for (JsonObject json : jsonList) {
                listCls.add(new Gson().fromJson(json, cls));
            }
        }

        return listCls;
    }

    /**
     * 根据json字符串返回对应Map类型
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String jsonString) {
        Map<String, Object> map;
        Gson gson = new Gson();
        map = gson.fromJson(jsonString, Map.class);

        return map;
    }

    /**
     * 根据json字符串返回对应Map类型List
     */
    public static List<Map<String, Object>> jsonToMapList(String jsonString) {
        List<Map<String, Object>> list;

        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {
        }.getType());

        return list;
    }

    /**
     * 根据json字符串返回对应java类型
     */
    public static String getJsonString(Object data) {
        return new GsonBuilder().serializeNulls().create().toJson(data);
    }

    public static String bean2Json(Object obj){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }
}
