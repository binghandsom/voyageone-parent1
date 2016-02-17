package com.voyageone.common.masterdate.schema.utils;

import com.voyageone.common.masterdate.schema.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {

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

    /**
     * 把str中的【.】替换成【->】
     */
    public static String replaceDot(String str){
        Pattern special_symbol = Pattern.compile("[.]");
        return special_symbol.matcher(str).replaceAll("->");
    }

    /**
     * 把str中的【->】替换成【.】
     */
    public static String replaceToDot(String str){
        return str.replaceAll("->", ".");
    }

    public static String getStringValue(Object valueObj) {
        String result = "";
        if (valueObj == null) {
            return result;
        }
        if (valueObj instanceof String) {
            result = (String) valueObj;
        } else if (valueObj instanceof Value) {
            Value value = (Value)valueObj;
            result = value.getValue()!=null?value.getValue():"";
        } else {
            result = valueObj.toString();
        }
        return result;
    }
}
