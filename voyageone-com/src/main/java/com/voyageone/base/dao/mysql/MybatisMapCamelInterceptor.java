package com.voyageone.base.dao.mysql;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * MyBatis拦截器：将检索返回类型为Map的键名转换为驼峰式命名。
 * @author Wangtd
 * @since 2.0.0 2016/8/11
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts({@Signature(type=ResultSetHandler.class, method="handleResultSets", args={Statement.class})})
public class MybatisMapCamelInterceptor implements Interceptor {
	
	private static final String TARGET_GETTER_KEY = "target";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		try {
			if (target instanceof ResultSetHandler) {
				Object result = invocation.proceed();
				
				if (result != null) {
					ResultSetHandler resultSetHandler = (ResultSetHandler) target;
					MappedStatement mappedStatement = getMetaObjectForHandler(resultSetHandler, "mappedStatement");
					
					if (mappedStatement.getConfiguration().isMapUnderscoreToCamelCase()) {
						List<ResultMap> resultMaps = mappedStatement.getResultMaps();
						ResultMap resultMap = resultMaps.size() > 0 ? resultMaps.get(0) : null;
						if (resultMap != null && Map.class.isAssignableFrom(resultMap.getType())) {
							new MapUnderscoreToCamelCase().executeForMap(result);
						}
					}
				}

				return result;
			}
			
			return invocation.proceed();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static class MapUnderscoreToCamelCase {
		
		private static final char FIELD_SEPARATOR = '_';
		
		public void executeForMap(Object result) {
			if (result != null) {
				if (result instanceof List) {
					List resultList = (List) result;
					for (int i = 0; i < resultList.size(); i++) {
						toCamelCaseMap((Map<String, Object>) resultList.get(i));
					}
				} else if (result instanceof Map) {
					toCamelCaseMap((Map<String, Object>) result);
				}
			}
		}
		
		private void toCamelCaseMap(Map<String, Object> dataMap) {
			if (dataMap != null && dataMap.size() > 0) {
				Map<String, Object> dataMapTemp = new HashMap<String, Object>();
				Iterator<Entry<String, Object>> it = dataMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					String camelCaseKey = getCamelCaseKeyName(entry.getKey());
					dataMapTemp.put(camelCaseKey, entry.getValue());
				}
				dataMap.clear();
				dataMap.putAll(dataMapTemp);
			}
		}
		
		private String getCamelCaseKeyName(String keyName) {
			StringBuilder sb = new StringBuilder(keyName.length());
	        boolean upperCase = false;
	        for (int i = 0; i < keyName.length(); i++) {
	            char c = keyName.charAt(i);
	            if (c == FIELD_SEPARATOR) {
	                upperCase = true;
	            } else if (upperCase) {
	                sb.append(Character.toUpperCase(c));
	                upperCase = false;
	            } else {
	                sb.append(c);
	            }
	        }
	        return sb.toString();
		}
		
	}
	
	private <T> T getMetaObjectForHandler(Object handler, String metaKey) {
		MetaObject metaStatement = SystemMetaObject.forObject(handler);
		while (metaStatement.hasGetter(TARGET_GETTER_KEY)) {
			Object object = metaStatement.getValue(TARGET_GETTER_KEY);
			metaStatement = SystemMetaObject.forObject(object);
		}
		return (T) metaStatement.getValue(metaKey);
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof ResultSetHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		// nothing
	}

}
