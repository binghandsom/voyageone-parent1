package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2016/10/18.
 */
public class CmsBtJmImageTemplateModel extends BaseMongoModel {
    private String imageType;
    private String name;
    private List<String> templateUrls;
    private ArrayList<String> parameters;
    private String dateFormat;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
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

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }
}
