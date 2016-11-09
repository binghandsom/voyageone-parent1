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
    private List<CmsBtJmImageTemplateModel_TemplateUrls> templateUrls;
    private String dateFormat;

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

    public List<CmsBtJmImageTemplateModel_TemplateUrls> getTemplateUrls() {
        return templateUrls;
    }

    public void setTemplateUrls(List<CmsBtJmImageTemplateModel_TemplateUrls> templateUrls) {
        this.templateUrls = templateUrls;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
