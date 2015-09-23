package com.voyageone.wsdl.core.util;

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
	
	/**
	 * DB 日期时间去除
	 * 
	 * @param dbDate DB中日期值（含时间）
	 * @param dbDateSuf DB中日期时间（ " 00:00:00.0"）
	 * @return
	 */
	public static String trimDBDateTime(String dbDate) {
		String ret = "";
		
		if (dbDate != null) {
			// 数据库日期后缀
			String dbDateSuf = " 00:00:00.0"; 
			
			ret = dbDate.replace(dbDateSuf, "");
		}
		
		return ret;
	}
	
	/**
	 * DB 日期时间毫秒去除
	 * 
	 * @param dbDate DB中日期值（含时间）
	 * @return
	 */
	public static String trimDBDateTimeMs(String dbDate) {
		String ret = "";
		
		if (dbDate != null) {
			// 毫秒后缀
			String dbMsSuf = ".0"; 
			
			ret = dbDate.replace(dbMsSuf, "");			
		}
		
		return ret;
	}
	
	/**
	 * DB 日期取得
	 * 
	 * @param dbDate DB中日期值（含时间）
	 * @return
	 */
	public static String getDate(String dbDate) {
		String ret = "";
		
		if (dbDate != null) {
			String[] dateTime = dbDate.split(" ");
			if (dateTime.length == 2) {
				ret = dateTime[0];
			}			
		}
		
		return ret;
	}
	
	/**
	 * DB 时间取得
	 * 
	 * @param dbDate DB中日期值（含时间）
	 * @return
	 */
	public static String getTime(String dbDate) {
		String ret = "";
		
		if (dbDate != null) {
			String[] dateTime = dbDate.split(" ");
			if (dateTime.length == 2) {
				ret = dateTime[1];
			}			
		}
		
		return ret;
	}
	
	/**
	 * DB 日期时间取得
	 * 
	 * @param dbDate DB中日期值（含时间）
	 * @param dbTime DB中时间值（含时间）
	 * @return
	 */
	public static String getDateTime(String dbDate, String dbTime) {
		String ret = "";
		
		if (dbDate != null && dbTime != null) {
			ret = getDate(dbDate) + " " + getTime(dbTime);	
		}
		
		return ret;
	}
	
	/**
	 * DB 金额取得
	 * 
	 * @param dbMoney DB中金额（0.0000）
	 * @return
	 */
	public static String getFormatedMoney(String dbMoney) {	
		String ret = "";
		
		if (dbMoney != null) {
			double retD = Double.parseDouble(dbMoney);		
			BigDecimal b = new BigDecimal(retD);
			
			ret = String.valueOf(b.setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		
		return ret;
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
