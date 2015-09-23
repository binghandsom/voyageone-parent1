package com.voyageone.base.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -470808102948204904L;

    private Throwable cause;

    private String code;

    private Object[] info;

    public Object[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public BusinessException(String msg_code, Object... info) {
        this(msg_code, null, info);
    }

    public BusinessException(String msg_code, Throwable ex, Object... info) {
        super(msg_code, ex);
        this.code = msg_code;
        this.info = info;
    }

    public BusinessException(String code, String msg, Throwable ex) {
        super(msg, ex);
        this.code = code;
        this.cause = ex;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        String message = super.getMessage();
        Throwable cause = getCause();
        if (cause != null) {
            message = message + "; cause is " + cause;
        }
        return message;
    }

    public void printStackTrace(PrintStream ps) {
        if (getCause() == null) {
            super.printStackTrace(ps);
        } else {
            ps.println(this);
            getCause().printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter pw) {
        if (getCause() == null) {
            super.printStackTrace(pw);
        } else {
            pw.println(this);
            getCause().printStackTrace(pw);
        }
    }

    public void printStackTrace() {
        if (getCause() == null) {
            super.printStackTrace();
        } else {
            getCause().printStackTrace();
        }
    }
}
