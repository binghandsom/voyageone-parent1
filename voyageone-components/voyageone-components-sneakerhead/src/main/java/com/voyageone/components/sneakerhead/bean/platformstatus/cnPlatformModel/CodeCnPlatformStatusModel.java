package com.voyageone.components.sneakerhead.bean.platformstatus.cnPlatformModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vantis on 2016/11/30.
 * 闲舟江流夕照晚 =。=
 */
public class CodeCnPlatformStatusModel {
    private List<String> codes;
    private List<CnPlatformStatusModel> statuses = new ArrayList<>();

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<CnPlatformStatusModel> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<CnPlatformStatusModel> statuses) {
        this.statuses = statuses;
    }
}
