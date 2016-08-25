package com.voyageone.base.dao.mysql;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ParameterMapping;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/24
 */
public class SqlMetaData {
	
	private String sql;
	
	private List<Object> parameters;
	
	private List<ParameterMapping> parameterMappings;
	
	private Object parameterObject;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		if (StringUtils.isBlank(sql)) {
			this.sql = sql;
		} else {
		    StringTokenizer whitespaceTokenizer = new StringTokenizer(sql);
		    StringBuilder builder = new StringBuilder();
		    while (whitespaceTokenizer.hasMoreTokens()) {
		    	builder.append(whitespaceTokenizer.nextToken());
		    	builder.append(" ");
		    }
		    this.sql = builder.toString();
		}
	}

	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public void setParameterMappings(List<ParameterMapping> parameterMappings) {
		this.parameterMappings = parameterMappings;
	}

	public Object getParameterObject() {
		return parameterObject;
	}

	public void setParameterObject(Object parameterObject) {
		this.parameterObject = parameterObject;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getParameters() throws Exception {
		if (parameters == null) {
			parameters = Collections.emptyList();
			if (parameterMappings != null && parameterMappings.size() > 0) {
				parameters = new ArrayList<Object>(parameterMappings.size());
				
				for (ParameterMapping parameterMapping : parameterMappings) {
					String parameterName = parameterMapping.getProperty();
					if (Map.class.isAssignableFrom(parameterObject.getClass())) {
						Map<String, Object> objectMap = (Map<String, Object>) parameterObject;
						parameters.add(objectMap.get(parameterName));
					} else {
						PropertyDescriptor propDesc = new PropertyDescriptor(parameterName, parameterObject.getClass());
						Method readMethod = propDesc.getReadMethod();
						parameters.add(readMethod.invoke(parameterObject));
					}
				}
			}
		}
		
		return parameters;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SqlMetaData [sql=");
		builder.append(sql);
		builder.append(", parameters=");
		builder.append(parameters);
		builder.append(", parameterMappings=");
		builder.append(parameterMappings);
		builder.append(", parameterObject=");
		builder.append(parameterObject);
		builder.append("]");
		return builder.toString();
	}
	
}
