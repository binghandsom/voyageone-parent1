package com.voyageone.service.bean.cms.imagetemplate;

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
    public String getTemplateParameter() {
        return templateParameter;
    }
    public void setTemplateParameter(String templateParameter) {
        this.templateParameter = templateParameter;
    }

    String templateContent;
    String templateParameter;
}
