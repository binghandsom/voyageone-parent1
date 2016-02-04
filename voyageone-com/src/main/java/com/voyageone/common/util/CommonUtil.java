package com.voyageone.common.util;

import com.voyageone.common.Constants;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jacky, Jonas
 * @version 0.0.2
 */
public final class CommonUtil {

    /**
     * 生成token
     *
     * @return 长度36的大写字符串
     */
    public static String generateToken() {
        return new BigInteger(165, Constants.RANDOM).toString(36).toUpperCase();
    }

    /**
     * 生成指定一场的概要错误信息
     *
     * @param e 目标异常
     * @return 概要错误信息
     */
    public static String getExceptionContent(Exception e) {
        return getExceptionContent(e, null);
    }

    /**
     * 生成指定一场的概要错误信息，并附加一些额外的信息
     *
     * @param e      目标异常
     * @param attMsg 附加信息
     * @return 概要错误信息（包含附加）
     */
    public static String getExceptionContent(Exception e, List<String> attMsg) {
        StackTraceElement[] stack = e.getStackTrace();
        StringBuilder builder = new StringBuilder();

        builder.append("<p>异常信息</p>");
        builder.append(e.getMessage());
        builder.append("<p>堆栈信息</p>");
        for (StackTraceElement s : stack) {
            builder.append("<p>").append(s.getClassName()).append(".").append(s.getMethodName()).append("</p>");
        }

        if (attMsg != null && attMsg.size() > 0) {
            builder.append("<p>附加信息</p>");
            for (String s : attMsg) {
                builder.append("<p>").append(s).append("</p>");
            }
        }

        builder.append("<p>&nbsp;</p>");
        return builder.toString();
    }

    public static String getExceptionSimpleContent(Exception e) {
        String top = filterLocation(e, "com.voyageone");

        Throwable inner = getInner(e);

        if (inner != e) {
            top += " \r\n ... \r\n " + getLocation(inner);
        }

        return top;
    }

    /**
     * 获取异常内的所有信息
     *
     * @param throwable 顶层异常
     * @return 所有异常信息的叠加
     */
    public static String getMessages(Throwable throwable) {

        StringBuilder builder = new StringBuilder();

        while (throwable != null) {
            builder.append(throwable.toString());
            builder.append("\r\n");
            throwable = throwable.getCause();
        }

        return builder.toString();
    }

    /**
     * 递归查找，最底层的抛出，如果没有，则返回自身
     *
     * @param throwable 抛出的内容
     * @return 最底层的抛出
     */
    private static Throwable getInner(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null)
            return throwable;

        return getInner(cause);
    }

    /**
     * 获取抛出的最底层位置
     *
     * @param throwable 抛出的内容
     * @return 地址信息
     */
    private static String getLocation(Throwable throwable) {
        StackTraceElement[] traceElements = throwable.getStackTrace();

        StackTraceElement element = traceElements[0];

        return element.toString();
    }

    /**
     * 根据关键字，过滤筛选堆栈中的地址
     *
     * @param throwable 抛出的内容
     * @param filter    关键字
     * @return 地址信息
     */
    private static String filterLocation(Throwable throwable, String filter) {
        StackTraceElement[] traceElements = throwable.getStackTrace();

        StringBuilder builder = new StringBuilder();

        for (StackTraceElement element : traceElements) {
            String location = element.toString();

            if (!StringUtils.isEmpty(filter) && !location.contains(filter)) continue;

            builder.append(location);
            builder.append(" ... ");
            builder.append("\r\n");
        }

        return builder.toString();
    }

    /**
     * 给出指定文件的编码
     *
     * @param fileName 文件名
     * @return 地址信息
     */
    public static String getCharset(String fileName) throws IOException {

        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();

        String code;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        bin.close();
        return code;
    }


    public static boolean isPhoneNum(String phone) {

        // 电话号码以1开头的11位数字
        Pattern pattern = Pattern.compile("^1[\\d]{10}$");

        Matcher match = pattern.matcher(phone);

        return match.matches();

    }

    /**
     * 分割List
     *
     * @param list     待分割的list
     * @param pageSize 每段list的大小
     * @return List[List[T]]
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

        int total = list.size();
        int start = 0;
        int end;

        List<List<T>> listArray = new ArrayList<>();

        if (total <= pageSize) {
            listArray.add(list);
            return listArray;
        }

        while (start < total) {

            end = start + pageSize;

            if (end > total) end = total;

            listArray.add(list.subList(start, end));

            start = end;
        }

        return listArray;
    }

    /**
     * 保留两位小数
     *
     * @param doubleValue
     * @return ret
     */
    public static double getRoundUp2Digits(double doubleValue) {
        double ret = 0d;

        BigDecimal bd1 = new BigDecimal(doubleValue);

        ret = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        return ret;
    }
}
