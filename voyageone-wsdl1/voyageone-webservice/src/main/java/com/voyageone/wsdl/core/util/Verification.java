package com.voyageone.wsdl.core.util;

import java.util.Arrays;

public class Verification {

	private static String CHECK_TOKEN_IDCARD = "VoyageOne";
	
	public static boolean isVerification4IdCard(String timeStamp, String signature) {
		boolean isCheckSuccess = false;
		
		if (!StringUtils.isEmpty(timeStamp) && !StringUtils.isEmpty(signature)) {
			isCheckSuccess = signature.equals(getVerificationString(timeStamp));
		}
		
		return isCheckSuccess;
	}
	
	private static String getVerificationString(String timeStamp) {
		String[] ss = new String[] { CHECK_TOKEN_IDCARD, timeStamp };
		
		Arrays.sort(ss);
		
		String sha1 = "";
		for (String s : ss) {
			sha1 += s;
		}
		
		sha1 = new SHA1().getDigestOfString(sha1.getBytes());
		
		return sha1.toLowerCase();
	}
	
	public static void main(String[] args) {
		String ss = getVerificationString("2014-09-11 15:01:18");
		System.out.println(ss);
	}
}
