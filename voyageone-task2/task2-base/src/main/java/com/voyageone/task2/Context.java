package com.voyageone.task2;

import java.util.HashMap;
import java.util.Map;

/**
 * job运行的上下文
 */
public class Context {

	private static Context conext = new Context();

	private Map<String, Object> contextMap = new HashMap<String, Object>();

	private Context() {
	}

	public static Context getContext() {
		return conext;
	}

	/**
	 * 添加系统参数
	 * 
	 * @param key
	 * @param value
	 */
	public void putAttribute(String key, Object value) {
		this.contextMap.put(key, value);
	}

	/**
	 * 查询系统参数
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key) {
		return this.contextMap.get(key);
	}

	/**
	 * 
	 * @param key
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key
	 */
	public Object removeAttribute(String key) {
		return this.contextMap.remove(key);
	}
}
