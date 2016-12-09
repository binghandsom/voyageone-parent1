package com.voyageone.components.sneakerhead.bean.platformstatus.usPlatformModel;

import java.util.List;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public class CodeUsPlatformModel {
    private String code;
    private List<UsPlatformStatusModel> statuses;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UsPlatformStatusModel> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<UsPlatformStatusModel> statuses) {
        this.statuses = statuses;
    }
}
