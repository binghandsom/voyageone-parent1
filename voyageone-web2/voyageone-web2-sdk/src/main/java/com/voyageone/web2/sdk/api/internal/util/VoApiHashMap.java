package com.voyageone.web2.sdk.api.internal.util;

import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 符合VO API习惯的纯字符串字典结构。
 *
 * @author carver.gu
 * @since 1.0, Sep 13, 2009
 */
public class VoApiHashMap extends HashMap<String, String> {

	private static final long serialVersionUID = -1277791390393392630L;

	public VoApiHashMap() {
		super();
	}

	public VoApiHashMap(Map<? extends String, ? extends String> m) {
		super(m);
	}

	public String put(String key, Object value) {
		String strValue;

		if (value == null) {
			strValue = null;
		} else if (value instanceof String) {
			strValue = (String) value;
		} else if (value instanceof Integer) {
			strValue = ((Integer) value).toString();
		} else if (value instanceof Long) {
			strValue = ((Long) value).toString();
		} else if (value instanceof Float) {
			strValue = ((Float) value).toString();
		} else if (value instanceof Double) {
			strValue = ((Double) value).toString();
		} else if (value instanceof Boolean) {
			strValue = ((Boolean) value).toString();
		} else if (value instanceof Date) {
			DateFormat format = new SimpleDateFormat(VoApiConstants.DATE_TIME_FORMAT);
			format.setTimeZone(TimeZone.getTimeZone(VoApiConstants.DATE_TIMEZONE));
			strValue = format.format((Date) value);
		} else {
			strValue = value.toString();
		}

		return this.put(key, strValue);
	}

	public String put(String key, String value) {
		if (StringUtils.areNotEmpty(key, value)) {
			return super.put(key, value);
		} else {
			return null;
		}
	}

}
