package com.voyageone.wsdl.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtil {
	
	public static final String DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss.SSS";

	public static String getStringDateTime(String dateFormat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
}
