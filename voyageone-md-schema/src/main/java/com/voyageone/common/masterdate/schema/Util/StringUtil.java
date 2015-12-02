package com.voyageone.common.masterdate.schema.Util;

public class StringUtil {
    public StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static int getByteLenth(String tempvalue) {
        int sum = 0;

        for(int i = 0; i < tempvalue.length(); ++i) {
            if(tempvalue.charAt(i) >= 0 && tempvalue.charAt(i) <= 255) {
                ++sum;
            } else {
                sum += 2;
            }
        }

        return sum;
    }
}
