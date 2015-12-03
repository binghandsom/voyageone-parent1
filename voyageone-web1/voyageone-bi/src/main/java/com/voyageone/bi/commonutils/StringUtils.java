package com.voyageone.bi.commonutils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

	public static String null2Space(String input){
		if(input == null){
			return "";
		}else{
			return input;
		}
	}
	
	public static String null2Space2(String input){
		if(input == null){
			return "";
		}else if("null".equalsIgnoreCase(input)){
			return "";
	    }else{
			return input;
		}
	}
	
	public static String formatNum(String number) {
		if (isEmpty(number))
			return number;
		
		return formatNum(new Double(number));
	}
	
	public static String formatNum(Number number){
		DecimalFormat a = new DecimalFormat("0.00");
		return a.format(number);
	}
	
	// 空白Check
	public static boolean isEmpty(String chkParam){
		boolean ret = false;
		
		if(chkParam == null || "".equals(chkParam)){
			ret = true;
		}
		
		return ret;
	}
	
	// 数字Check
	public static boolean isDigit(String chkParam){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(chkParam);
		
		if (match.matches() == false){
			return false;
		} else {
			return true;
		}
	}
	
	// 数值Check
	public static boolean isNumeric(String chkParam){
		boolean ret = true;
		try{
			Float.valueOf(chkParam);
		} catch(Exception e){
			ret = false;
		}
		
		return ret;
	}
	
	// Web Service 用
	public static boolean isNullOrBlank2(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else if ("null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	// ArrayList 转为 页面输出字符串
	public static String arrayListToString(ArrayList<String> errorList){
		// 异常信息输出
		String errorOutput = "";
    	StringBuffer outputBuff = new StringBuffer();
    	for (int i = 0; i < errorList.size(); i++){
    		if (i == 0){
    			outputBuff.append(errorList.get(i));
    		} else {
    			outputBuff.append("\\n ");
    			outputBuff.append(errorList.get(i));
    		}
    	}
    	errorOutput = outputBuff.toString();
    	
    	return errorOutput;
	}
	
	// ArrayList 转为 页面输出字符串
	public static String arrayListToString2(ArrayList<String> errorList){
		// 异常信息输出
		String errorOutput = "";
    	StringBuffer outputBuff = new StringBuffer();
    	for (int i = 0; i < errorList.size(); i++){
    		if (i == 0){
    			outputBuff.append(errorList.get(i));
    		} else {
    			outputBuff.append("\n");
    			outputBuff.append(errorList.get(i));
    		}
    	}
    	errorOutput = outputBuff.toString();
    	
    	return errorOutput;
	}
	
	// 小数点2位精度取得
	public static String getNumPrecision2(double value) {
		
		BigDecimal b = new BigDecimal(value);
		
		return String.valueOf(b.setScale(2, BigDecimal.ROUND_HALF_UP));
	}
	
	public static boolean isPhoneNum(String phone){
		
		// 电话号码以1开头的11位数字
		Pattern pattern = Pattern.compile("^1[\\d]{10}$");

		Matcher match = pattern.matcher(phone);

		return match.matches();

	}
	
	public static String null2Zero(String input){
		if(input == null){
			return "0";
		}else{
			return input;
		}
	}
	
//	public static void main(String[] args){
//		ArrayList<String> errorList = new ArrayList<String>();
//		errorList.add("1");
//		errorList.add("2");
//		
//		String a = arrayListToString(errorList);
//		
//		System.out.println(a);
//	}
}
