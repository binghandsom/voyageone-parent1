package com.voyageone.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

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
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}