package com.voyageone.cms.modelbean;

import java.util.Date;

public class JdProductExtend  {
    private String jdName;

    private String jgName;

    private String jdShortDescription;

    private Integer jdCategoryId;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    private String jdLongDescription;

    public String getJdName() {
        return jdName;
    }

    public void setJdName(String jdName) {
        this.jdName = jdName == null ? null : jdName.trim();
    }

    public String getJgName() {
        return jgName;
    }

    public void setJgName(String jgName) {
        this.jgName = jgName == null ? null : jgName.trim();
    }

    public String getJdShortDescription() {
        return jdShortDescription;
    }

    public void setJdShortDescription(String jdShortDescription) {
        this.jdShortDescription = jdShortDescription == null ? null : jdShortDescription.trim();
    }

    public Integer getJdCategoryId() {
        return jdCategoryId;
    }

    public void setJdCategoryId(Integer jdCategoryId) {
        this.jdCategoryId = jdCategoryId;
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

    public String getJdLongDescription() {
        return jdLongDescription;
    }

    public void setJdLongDescription(String jdLongDescription) {
        this.jdLongDescription = jdLongDescription == null ? null : jdLongDescription.trim();
    }
}