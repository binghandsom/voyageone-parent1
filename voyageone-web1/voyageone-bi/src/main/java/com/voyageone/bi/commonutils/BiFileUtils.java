package com.voyageone.bi.commonutils;

import javax.servlet.http.HttpServletRequest;

public class BiFileUtils {
	public static String getAbsolutRootPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}
}
