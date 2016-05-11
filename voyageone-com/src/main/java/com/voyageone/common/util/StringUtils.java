package com.voyageone.common.util;

import org.apache.commons.net.util.Base64;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final Pattern special_symbol = Pattern.compile("[.]");

    public static String null2Space(String input) {
        if (input == null) {
            return "";
        } else {
            return input;
        }
    }

    public static String null2Space2(String input) {
        if (input == null) {
            return "";
        } else if ("null".equalsIgnoreCase(input)) {
            return "";
        } else {
            return input;
        }
    }

    public static String formatNum(String number) {
        if (isEmpty(number))
            return number;

        return formatNum(new Double(number));
    }

    public static String formatNum(Number number) {
        DecimalFormat a = new DecimalFormat("0.00");
        return a.format(number);
    }

    /**
     * 空白Check
     */
    public static boolean isEmpty(String chkParam) {
        boolean ret = false;

        if (chkParam == null || "".equals(chkParam)) {
            ret = true;
        }

        return ret;
    }

    /**
     * 数字Check
     */
    public static boolean isDigit(String chkParam) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(chkParam);

        return match.matches();
    }

    /**
     * 数值Check
     */
    public static boolean isNumeric(String chkParam) {
        boolean ret = true;
        try {
            Float.valueOf(chkParam);
        } catch (Exception e) {
            ret = false;
        }

        return ret;
    }

    /**
     * Web Service 用
     */
    public static boolean isNullOrBlank2(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    /**
     * ArrayList 转为 页面输出字符串
     */
    public static String arrayListToString(List<String> errorList) {
        // 异常信息输出
        StringBuilder outputBuff = new StringBuilder();
        for (int i = 0; i < errorList.size(); i++) {
            if (i == 0) {
                outputBuff.append(errorList.get(i));
            } else {
                outputBuff.append("\\n ");
                outputBuff.append(errorList.get(i));
            }
        }
        return outputBuff.toString();
    }

    /**
     * ArrayList 转为 页面输出字符串
     */
    public static String arrayListToString2(List<String> errorList) {
        // 异常信息输出
        StringBuilder outputBuff = new StringBuilder();
        for (int i = 0; i < errorList.size(); i++) {
            if (i == 0) {
                outputBuff.append(errorList.get(i));
            } else {
                outputBuff.append("\n");
                outputBuff.append(errorList.get(i));
            }
        }
        return outputBuff.toString();
    }

    /**
     * 小数点2位精度取得
     */
    public static String getNumPrecision2(double value) {

        BigDecimal b = BigDecimal.valueOf(value);

        return String.valueOf(b.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean isPhoneNum(String phone) {

        // 电话号码以1开头的11位数字
        Pattern pattern = Pattern.compile("^1[\\d]{10}$");

        Matcher match = pattern.matcher(phone);

        return match.matches();

    }

    /**
     * DB 日期时间去除
     *
     * @param dbDate DB中日期值（含时间）
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
     */
    public static String getFormatedMoney(String dbMoney) {
        String ret = "";

        if (dbMoney != null) {
            double retD = Double.parseDouble(dbMoney);
            BigDecimal b = BigDecimal.valueOf(retD);

            ret = String.valueOf(b.setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return ret;
    }

    public static String join(final List<?> list, final String separator) {
        return join(list.iterator(), separator);
    }

    public static String join(final Iterator<?> iterator, final String separator) {
        if (iterator == null) return null;

        if (!iterator.hasNext()) return "";

        final Object first = iterator.next();

        if (!iterator.hasNext()) {
            @SuppressWarnings( "deprecation" ) // ObjectUtils.toString(Object) has been deprecated in 3.2
            final String result = String.valueOf(first);
            return result;
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small

        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) buf.append(separator);

            final Object obj = iterator.next();

            if (obj != null) buf.append(obj);
        }

        return buf.toString();
    }

    /**
     * 解码 Unicode
     * @param str 一串 Unicode 编码
     * @return String
     */
    public static String decodeUnicode(String str) {
        String[] buf = str.split("//u");
        StringBuilder sb = new StringBuilder();
        for (String string : buf) {
            if (string.length() > 0) {
                sb.append((char) Integer.parseInt(string, 16));
            }
        }
        return sb.toString();
    }
    
    /**
     * 判断字符串中是否包含某些字符串
     */
    public static boolean containstr(String str,String[]... comp){
        for (String[] s : comp) {
            for (String value : s) {
                if (str.contains(value)) {
                    return true;
                }
            }
		}
        return false;
    }
    
    /**
     * 字符串首字母大写
     */
    public static String uppercaseFirst(String name)  
    {  
        return name.substring(0, 1).toUpperCase() + name.substring(1);  
    }

    /**
     * 转换数据中的特殊字符
     */
    public static String transferStr(String data) {
        if (StringUtils.isNullOrBlank2(data)) {
            return "";
        } else {
            return data.replace("'", "''").replace("\\", "\\\\").replace("\r\n", " ").replace("\n", " ").replace("\r", " ");
        }
    }

    /**
     * Discription: 指定的字符串累加
     */
    public static String strAdd(String chr, int len) {
        if (len > 0) {
            StringBuilder ret = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                ret.append(chr);
            }
            return ret.toString();
        } else {
            return "";
        }
    }

    /**
     * Discription: 给字符串补足到指定的长度，从左边补足chr指定的字符
     */
    public static String lPad(String source, String chr, int len) {
        int lenleft = len - source.length();
        if (lenleft < 0) {
            lenleft = 0;
        }
        return strAdd(chr, lenleft) + source;
    }

    /**
     * Discription: 给字符串补足到指定的长度，从右边补足chr指定的字符
     */
    public static String rPad(String source, String chr, int len) {
        int lenleft = len - source.length();
        if (lenleft < 0) {
            lenleft = 0;
        }
        return source + strAdd(chr, lenleft);
    }

    /**
     * Discription: 取得字符的Byte长度
     */
    public static int getByteLength(String content, String charsetName) {
        int byteLength = 0;

        try {
            byte[] byteContent = content.getBytes(charsetName);
            byteLength = byteContent.length;
        } catch (Exception ignored) {
        }

        return byteLength;
    }

    /**
     * Discription: <img></> 元素删除
     */
    public static String trimImgElement(String content) {
        String ret = content;

        int imgBeginIndex = content.indexOf("<img");
        while (imgBeginIndex != -1) {
            int imgEndIndex = ret.indexOf(">", imgBeginIndex);

            // 正常匹配的场合
            if (imgEndIndex != -1) {
                imgEndIndex = imgEndIndex + 1;
                ret = ret.substring(0,imgBeginIndex) + ret.substring(imgEndIndex, ret.length());
            } else {
            // 异常数据的场合
                break;
            }

            imgBeginIndex = ret.indexOf("<img");
        }

        return ret;
    }
	
	
	/**
     * 把str中的【.】替换成【->】
     */
    public static String replaceDot(String str){
        return special_symbol.matcher(str).replaceAll("->");
    }

    /**
     * 把str中的【->】替换成【.】
     */
    public static String replaceToDot(String str){
        return str.replaceAll("->", ".");
    }

    /**
     * BASE64字符串二进制数据编码为
     */
    public static String decodeBase64(String input) {
        String result = null;
        if (input != null) {
            result = new String(Base64.decodeBase64(input.getBytes()));
        }
        return result;
    }

    /**
     * 二进制数据编码为BASE64字符串
     */
    public static String encodeBase64(String input) {
        String result = null;
        if (input != null) {
            result = new String(Base64.encodeBase64(input.getBytes()));
        }
        return result;
    }

    public static String generCatId(String catPath) {
        return MD5.getMD5(catPath);
    }

    public static String format(String msg, String... params) {
        if ((params == null) || (params.length == 0)) {
            return msg;
        }
        String msgTmp = msg;
        int i = 0;
        for (int lens = params.length; i < lens; i++) {
            msgTmp = replace(msgTmp, "{" + i + "}", params[i]);
        }
        return msgTmp;
    }

    public static String replace(String val, String find, String rep) {
        if ((val == null) || ("".equals(val))) {
            return "";
        }
        StringBuilder ret = new StringBuilder();
        int end = val.indexOf(find);
        int start = 0;
        while (end >= 0) {
            ret.append(val.substring(start, end));
            ret.append(rep);
            start = end + find.length();
            end = val.indexOf(find, start);
        }
        ret.append(val.substring(start));
        return ret.toString();
    }

    public static String replaceBlankToDash(String val) {
        if ((val == null) || ("".equals(val)))
            return "";
        return val.trim().replaceAll(" ", "-");
    }

}
