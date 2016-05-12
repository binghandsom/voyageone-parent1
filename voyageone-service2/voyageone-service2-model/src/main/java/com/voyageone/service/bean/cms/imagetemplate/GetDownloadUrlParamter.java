package com.voyageone.service.bean.cms.imagetemplate;

import java.util.List;

/**
 * Created by dell on 2016/5/12.
 */
public class GetDownloadUrlParamter {
    public String getTemplateContent() {
        return templateContent;
    }
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    String templateContent;
    List<String> templateParameter;

    public List<String> getTemplateParameter() {
        return templateParameter;
    }

    public void setTemplateParameter(List<String> templateParameter) {
        this.templateParameter = templateParameter;
    }
}
