package com.voyageone.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	/**
	 * 将输入的字符串转为32位的MD5
	 * @param sourceStr 输入的字符串
	 * @return 32位的MD5
	 */
	public static String getMD5(String sourceStr) {

		String result = "";

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder();
			for (byte aB : b) {
				i = aB;
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			result = buf.toString();

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将输入的字符串转为16位的MD5
	 * @param sourceStr 输入的字符串
	 * @return 16位的MD5
	 */
	public static String getMd5_16(String sourceStr) {
		String result;

		result = getMD5(sourceStr);
		if (StringUtils.isEmpty(result)) {
			return "";
		} else {
			return result.substring(8, 24); // 16位
		}

	}

}