package com.voyageone.batch.ims.bean;

import java.util.List;

/**
 * Created by Leo on 2015/5/28.
 */
public class UpJobParamBean {
    public static final String METHOD_ADD = "add";
    public static final String METHOD_UPDATE = "update";

    private String method;
    //这里存放可以单独上新某model下若干个code,如果上新整个model，则该值为空
    private List<String> codes;
    private boolean forceAdd;

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isForceAdd() {
        return forceAdd;
    }

    public void setForceAdd(boolean forceAdd) {
        this.forceAdd = forceAdd;
    }
}
