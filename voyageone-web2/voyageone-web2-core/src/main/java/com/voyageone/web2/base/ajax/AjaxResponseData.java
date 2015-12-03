package com.voyageone.web2.base.ajax;

/**
 * Ajax 返回的业务数据部分
 * Created by Jonas on 11/25/15.
 */
public class AjaxResponseData {

    private Object data;

    private String redirectTo;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
}
