package com.voyageone.core.ajax.dt;

/**
 * 对应 datatables 传入的搜索参数
 * <p>
 * Created by Jonas on 7/21/15.
 */
public class DtSearch {
    private String value;

    private boolean regex;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }
}
