package com.voyageone.web2.cms.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 16-1-5.
 */
public class FeedAttributesBean {

    private Map<String,Object> orgAtts;

    private Map<String,Object> cnAtts;

    private List<String> customIds;

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
}
