package com.voyageone.components.intltarget.error;

/**
 * @author aooer 2016/4/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetErrorResponse {

    //Json响应未大写，ignore驼峰写法
    private TargetException Error;

    public TargetException getError() {
        return Error;
    }

    public void setError(TargetException error) {
        Error = error;
    }
}
