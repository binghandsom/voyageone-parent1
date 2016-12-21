package com.voyageone.components.sneakerhead.bean.platformstatus.cnPlatformModel;


import com.voyageone.components.sneakerhead.enums.CnPlatformStatus;

/**
 * Created by vantis on 2016/12/1.
 * 闲舟江流夕照晚 =。=
 */
public class BaseCnPlatformStatusModel {
    private String numIId;
    private CnPlatformStatus status;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    public CnPlatformStatus getStatus() {
        return status;
    }

    public void setStatus(CnPlatformStatus status) {
        this.status = status;
    }
}
