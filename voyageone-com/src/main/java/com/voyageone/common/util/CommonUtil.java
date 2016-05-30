package com.voyageone.common.util;

import com.voyageone.common.Constants;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author jacky, Jonas
 * @version 0.0.2
 */
public final class CommonUtil {

    public static final String USERNAME = "USERNAME";

    public static final String COMPUTERNAME = "COMPUTERNAME";

    public static final String IP = "IP";

    public static final String MAC = "MAC";

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

        if (attMsg != null && !attMsg.isEmpty()) {
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
        Throwable throwableTmp = throwable;
        while (throwableTmp != null) {
            builder.append(throwableTmp.toString());
            builder.append("\r\n");
            throwableTmp = throwableTmp.getCause();
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
     */
    public static double getRoundUp2Digits(double doubleValue) {
        BigDecimal bd1 = BigDecimal.valueOf(doubleValue);
        return bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将integer的list转成long的list
     */
    public static List<Long> changeListType(List<Integer> list) {
        if (list != null && !list.isEmpty())
            return list.stream().map(Integer::longValue).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }


    public static Map<String, String> getLocalInfo() {

        Map<String, String> infoMap = new HashMap<>();

        try {
            InetAddress address = InetAddress.getLocalHost();

            infoMap.put(IP, address.getHostAddress());

            infoMap.put(COMPUTERNAME, address.getHostName());

            NetworkInterface ni = NetworkInterface.getByInetAddress(address);

            byte[] mac = ni.getHardwareAddress();

            Formatter formatter = new Formatter();

            for (int i = 0; i < mac.length; i++)
                formatter.format("%s%X", (i > 0) ? ":" : "", mac[i] & 0xFF);

            infoMap.put(MAC, formatter.toString());

        } catch (UnknownHostException ignored) {
            infoMap.put(IP, "UnknownHost");
        } catch (SocketException e) {
            infoMap.put(MAC, "SocketException");
        }

        Map<String, String> env = System.getenv();

        if (env != null) {
            // 使用 env 数据尝试补全
            if (!infoMap.containsKey(COMPUTERNAME) && env.containsKey(COMPUTERNAME))
                infoMap.put(COMPUTERNAME, env.get(COMPUTERNAME));

            infoMap.put(USERNAME, env.get(USERNAME));
        }

        return infoMap;
    }

    /**
     * 获取图片的后缀名
     */
    public static String getImageExtend(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
    }

    /**
     * 获取IP Set
     */
    public static Set<String> getLocalIPs() {
        Set<String> ipList = new HashSet<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException ignored) {
        }
        return ipList;
    }
}
