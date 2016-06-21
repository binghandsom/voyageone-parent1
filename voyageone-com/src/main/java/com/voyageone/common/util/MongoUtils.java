package com.voyageone.common.util;

import java.math.BigDecimal;

/**
 * @author Edward
 * @version 2.0.0, 15/12/22
 */
public final class MongoUtils {

    /**
     * 将传入的值拼接成mongodb的查询语句语法
     * @param itemName
     * @param obj
     * @param compareType
     * @return
     */
    @Deprecated //此方法有待完善，推荐使用jongo的模板查询方式
    public static String splicingValue(String itemName, Object obj, String... compareType) {

        // 如果obj不为空才执行判断
        if (obj != null) {
            StringBuilder result = new StringBuilder();

            if (compareType.length == 0 || !"$or".equals(compareType[0]))
                result.append("\"" + itemName + "\":");

            // 如果obj为String类型
            if (obj instanceof String) {

                // 如果带自定义的比较符
                if (compareType.length > 0){

                    // 查询以obj开头的数据
                    if ("$elemMatch".equals(compareType[0]))
                        result.append("{" + compareType[0] + ": " + obj.toString() + "}");
                    // 如果比较符不为$eq($eq不是mongo的比较符,只是为了区分=的判断)
                    else if (!"$eq".equals(compareType[0]))
                        result.append("{" + compareType[0] + ": \"" + obj.toString() + "\"}");
                    // 如果比较符为$eq
                    else
                        result.append("\"" + obj.toString() + "\"");
                }
                // 如果不带自定义的比较符
                else {
                    result.append("\"" + obj.toString() + "\"");
                }
            }
            // 如果obj为数值类型
            else if (obj instanceof Number) {
                // 如果带自定义的比较符,并且比较符不为$eq($eq不是mongo的比较符,只是为了区分=的判断)
                if (compareType.length > 0 && !"$eq".equals(compareType[0])){
                    result.append("{" + compareType[0] + ": " + obj + "}");
                }
                // 如果不带自定义的比较符,或者比较符为$eq
                else {
                    result.append(obj);
                }
            }
            // 如果obj为String[]类型
            else if (obj instanceof String[]) {
                if (compareType.length > 0 && "$or".equals(compareType[0])) {
                    result.append("$or:[");
                    String[] options = (String[]) obj;

                    for (int i = 0; i < options.length; i++) {
                        if (i < options.length - 1) {
                            result.append("{" + options[i] + "},");
                        } else {
                            result.append("{" + options[i] + "}");
                        }
                    }
                    result.append("]");
                } else if (compareType.length > 0 && "$nin".equals(compareType[0])) {
                    result.append("{$nin:[");
                    String[] options = (String[]) obj;

                    for (int i = 0; i < options.length; i++) {
                        if (options[i] == null) {
                            if (i < options.length - 1) {
                                result.append("null,");
                            } else {
                                result.append("null");
                            }
                        } else {
                            if (i < options.length - 1) {
                                result.append("\"" + options[i] + "\",");
                            } else {
                                result.append("\"" + options[i] + "\"");
                            }
                        }
                    }
                    result.append("]}");
                } else {
                    result.append("{$in:[");
                    String[] options = (String[]) obj;

                    for (int i = 0; i < options.length; i++) {
                        if (options[i] == null) {
                            if (i < options.length - 1) {
                                result.append("null,");
                            } else {
                                result.append("null");
                            }
                        } else {
                            if (i < options.length - 1) {
                                result.append("\"" + options[i] + "\",");
                            } else {
                                result.append("\"" + options[i] + "\"");
                            }
                        }
                    }
                    result.append("]}");
                }
            }
            // 如果obj为Object[]类型
            else if (obj instanceof Object[]) {
                if (compareType.length > 0 && "$or".equals(compareType[0])) {
                    result.append("$or:[");
                    Object[] options = (Object[]) obj;

                    for (int i = 0; i < options.length; i++) {
                        if (i < options.length - 1) {
                            result.append("{" + options[i] + "},");
                        } else {
                            result.append("{" + options[i] + "}");
                        }
                    }
                    result.append("]");
                } else {
                    result.append("{$in:[");
                    Object[] options = (Object[]) obj;

                    for (int i = 0; i < options.length; i++) {
                        if (i < options.length - 1) {
                            result.append(options[i] + ",");
                        } else {
                            result.append(options[i]);
                        }
                    }
                    result.append("]}");
                }
            }
            return result.toString();
        } else {
            return null;
        }
    }
}
