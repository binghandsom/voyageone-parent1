package com.voyageone.service.model.cms.mongo;

import java.util.List;

/**
 * Created by gjl on 2016/11/3.
 */
public class CmsBtJmImageTemplateModel_TemplateUrls {
    private String url;
    List<String> parameters;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
