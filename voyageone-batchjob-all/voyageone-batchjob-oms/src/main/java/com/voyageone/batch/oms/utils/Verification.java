package com.voyageone.batch.oms.utils;

import java.util.Arrays;

import com.voyageone.batch.oms.OmsConstants;

public class Verification {
	
	public static String CHECK_SIGNATURE_VOYAGEONE = "VoyageOne";
	
	public static String getSynShipVerificationString(String timeStamp) {
		String[] ss = new String[] { OmsConstants.CHECK_SYNSHIP, timeStamp };
		
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
