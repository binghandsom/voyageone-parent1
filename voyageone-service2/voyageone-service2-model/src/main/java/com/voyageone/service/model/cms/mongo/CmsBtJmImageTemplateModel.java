package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * Created by james on 2016/10/18.
 */
public class CmsBtJmImageTemplateModel extends BaseMongoModel {
    private Integer imageId;
    private String name;
    private List<String> templateUrls;
    private List<String> parameters;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTemplateUrls() {
        return templateUrls;
    }

    public void setTemplateUrls(List<String> templateUrls) {
        this.templateUrls = templateUrls;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
