package com.voyageone.bi.base;

import java.util.HashMap;

import com.voyageone.bi.commonutils.StringUtils;

public class BiApplication {
	//对应配置文件表 静态变量
	private static HashMap<String , String> msgEnMap = new HashMap<String , String>();
	private static HashMap<String , String> msgCnMap = new HashMap<String , String>();
	private static HashMap<String , String> keyValueMap = new HashMap<String , String>();
	
	public final static String LOCAL_EN = "1";
	public final static String LOCAL_CN = "2";	
	
	public static HashMap<String, String> getKeyValueMap() {
		return keyValueMap;
	}

	public static String getLocalEn() {
		return LOCAL_EN;
	}


	public static String getLocalCn() {
		return LOCAL_CN;
	}


	public static void setKeyValueMap(HashMap<String, String> keyValueMap) {
		BiApplication.keyValueMap = keyValueMap;
	}

	public static HashMap<String, String> getMsgEnMap() {
		return msgEnMap;
	}

	public static void setMsgEnMap(HashMap<String, String> msgEnMap) {
		BiApplication.msgEnMap = msgEnMap;
	}

	public static HashMap<String, String> getMsgCnMap() {
		return msgCnMap;
	}

	public static void setMsgCnMap(HashMap<String, String> msgCnMap) {
		BiApplication.msgCnMap = msgCnMap;
	}
	
	public static String getMsg(String local,String key) {
		String ret = "";
		
		if (local.equals(LOCAL_CN)){
			ret = BiApplication.getMsgCnMap().get(key);
		}else if(local.equals(LOCAL_EN)){
			ret = BiApplication.getMsgEnMap().get(key);
		}
		
		return ret;
	}
	
	public static String getKeyValue(String key) {
		String ret = "";
		ret = StringUtils.null2Space(BiApplication.getKeyValueMap().get(key));
		
		return ret;
	}
	
	public static String readValue(String key) {
		String propValue = keyValueMap.get(key);
		
		return propValue;
	}
}
