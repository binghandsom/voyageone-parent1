package com.voyageone.common.mq.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author aooer 2016/4/26.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MQConfigUtils {

    public static final String EXISTS_IP = "_EXISTS_IP";

    private static String addStr = null;

    public static String getAddStrQueneName(String queneName) {
        return queneName + "_" + getIP() + EXISTS_IP;
    }

    public static String getIP(){
        if (addStr == null) {
            try {
                addStr= InetAddress.getLocalHost().getHostAddress().replaceAll("\\.", "_");
            } catch (UnknownHostException ignored) {
                addStr= "UnknownHost";
            }
        }
        return addStr;
    }
}
