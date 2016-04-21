package com.voyageone.common.util;
/**
 * Created by dell on 2016/4/11.
 */
public class ExceptionUtil {
    public static String getErrorMsg(Exception ex) {
        String msg = "";
        msg = ex.getMessage();
        if (ex.getStackTrace().length > 0) {
            msg = msg + ex.getStackTrace()[0].toString();
        }
        return msg;
    }
}