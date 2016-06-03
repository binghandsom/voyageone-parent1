package com.voyageone.web2.cms.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 16-1-5.
 */
public class CustomAttributesBean {

    private Map<String,Object> orgAtts;

    private Map<String,Object> cnAtts;

    private List<String> customIds;

    private List<String> customIdsCn;

    private Map<String, String[]> cnAttsShow;

    public Map<String, Object> getOrgAtts() {
        return orgAtts;
    }

    public void setOrgAtts(Map<String, Object> orgAtts) {
        this.orgAtts = orgAtts;
    }

    public Map<String, Object> getCnAtts() {
        return cnAtts;
    }

    public void setCnAtts(Map<String, Object> cnAtts) {
        this.cnAtts = cnAtts;
    }

    public List<String> getCustomIds() {
        return customIds;
    }

    public void setCustomIds(List<String> customIds) {
        this.customIds = customIds;
    }

    public Map<String, String[]> getCnAttsShow() {
        return cnAttsShow;
    }

    public void setCnAttsShow(Map<String, String[]> cnAttsShow) {
        this.cnAttsShow = cnAttsShow;
    }

    public List<String> getCustomIdsCn() {
        return customIdsCn;
    }

    public void setCustomIdsCn(List<String> customIdsCn) {
        this.customIdsCn = customIdsCn;
    }
}
