package com.voyageone.components.sneakerhead.bean.platformstatus.cnPlatformModel;

import java.util.List;

/**
 * Created by vantis on 2016/11/30.
 * 闲舟江流夕照晚 =。=
 */
public class DbCodesPlatformUpdateModel {
    private List<String> codes;
    private String flagColumnName;
    private String numIIdColumnName;
    private String numIId;
    private String status;

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlagColumnName() {
        return flagColumnName;
    }

    public void setFlagColumnName(String flagColumnName) {
        this.flagColumnName = flagColumnName;
    }

    public String getNumIIdColumnName() {
        return numIIdColumnName;
    }

    public void setNumIIdColumnName(String numIIdColumnName) {
        this.numIIdColumnName = numIIdColumnName;
    }
}
