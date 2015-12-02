package com.voyageone.cms.modelbean;

import java.util.Date;

public class GoogleProductExtend  {
    private Integer googleCategoryId;

    private Integer googleDepartmentId;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    public Integer getGoogleCategoryId() {
        return googleCategoryId;
    }

    public void setGoogleCategoryId(Integer googleCategoryId) {
        this.googleCategoryId = googleCategoryId;
    }

    public Integer getGoogleDepartmentId() {
        return googleDepartmentId;
    }

    public void setGoogleDepartmentId(Integer googleDepartmentId) {
        this.googleDepartmentId = googleDepartmentId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
}