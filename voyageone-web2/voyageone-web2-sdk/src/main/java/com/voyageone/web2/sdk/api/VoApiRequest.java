package com.voyageone.web2.sdk.api;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TOP请求接口。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class VoApiRequest<T extends VoApiResponse> {

	protected Long timestamp;

	/**
	 * @return 指定或默认的时间戳
	 */
	public Long getTimestamp() {
		if (timestamp == null) {
			return DateTimeUtil.getNowTimeStampLong();
		}
		return timestamp;
	}

	/**
	 * 设置时间戳，如果不设置,发送请求时将使用当时的时间。
	 *
	 * @param timestamp 时间戳
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Header info
	 */
	protected HttpHeaders headers;
	public HttpHeaders getHeaders() {
		if (headers == null) {
			return new HttpHeaders();
		}
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	/**
	 * Http Method
	 * Default POST
	 */
	protected HttpMethod httpMethod = HttpMethod.POST;
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * 获取TOP的API名称。
	 *
	 * @return API名称
	 */
	public abstract String getApiURLPath();

	/**
	 * get Response Class type
	 * @return class Type
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getResponseClass() {
		Type sooper = getClass().getGenericSuperclass();
		Type t = ((ParameterizedType)sooper).getActualTypeArguments()[ 0 ];
		return (Class<T>) t;
	}

	/**
	 * 客户端参数检查，减少服务端无效调用
	 */
	public void check() throws ApiRuleException {
		requestCheck();
	}

	/**
	 * 客户端参数检查
	 * @throws ApiRuleException
     */
	public abstract void requestCheck() throws ApiRuleException;


	/**
	 * 需返回的字段列表.可选值:Product数据结构中的所有字段;多个字段之间用" ; "分隔.
	 */
	protected String fields;
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public void addField(String field) {
		if (fields == null) {
			fields = field;
		} else {
			fields = fields + " ; " + field;
		}
	}

	/**
	 * sort condition
	 */
	protected String sorts;
	public String getSorts() {
		return sorts;
	}
	public void setSorts(String sorts) {
		this.sorts = sorts;
	}
	public void addSort(String field, boolean isAsc) {
		String asc = "-1";
		if (isAsc) {
			asc = "1";
		}
		if (sorts == null) {
			sorts = field + " : " + asc;
		} else {
			sorts = sorts + " ; " + field + " : " + asc;
		}
	}

	/**
	 *modifier
	 */
	protected String modifier;
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * modified
	 */
	protected String modified;
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
}
