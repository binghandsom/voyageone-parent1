package com.voyageone.oms.utils;

import java.util.Arrays;

import com.voyageone.oms.OmsConstants;

public class Verification {
	
	public static String CHECK_SIGNATURE_VOYAGEONE = "VoyageOne";
	
	public static boolean isVerification4IdCard(String timeStamp, String signature) {
		return signature.equals(getVerificationString(timeStamp));
	}
	
	public static String getVerificationString(String timeStamp) {
		String[] ss = new String[] { OmsConstants.CHECK_SIGNATURE, timeStamp };
		
		Arrays.sort(ss);
		
		String sha1 = "";
		for (String s : ss) {
			sha1 += s;
		}
		
		sha1 = new SHA1().getDigestOfString(sha1.getBytes());
		
		return sha1.toLowerCase();
	}
	
	public static String getVoyageOneVerificationString(String timeStamp) {
		String[] ss = new String[] { CHECK_SIGNATURE_VOYAGEONE, timeStamp };
		
		Arrays.sort(ss);
		
		String sha1 = "";
		for (String s : ss) {
			sha1 += s;
		}
		
		sha1 = new SHA1().getDigestOfString(sha1.getBytes());
		
		return sha1.toLowerCase();
	}
}
