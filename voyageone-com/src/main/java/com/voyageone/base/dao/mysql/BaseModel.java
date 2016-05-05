package com.voyageone.base.dao.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.DateTimeUtil;

import java.util.Date;

/**
 * 对数据模型提供基础字段
 * @author Jonas
 * @version 2.0.0, 12/4/15
 */
public class BaseModel {

    protected Integer id;

    protected Date created;

    protected String creater;

    protected Date modified;

    protected String modifier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.creater = creater;
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
        this.modifier = modifier;
    }

    @Deprecated
    @JsonIgnore
    public void setModifiedStr(String modifiedStr) {
        this.modified = DateTimeUtil.parse(modifiedStr);
    }

    @Deprecated
    @JsonIgnore
    public void setCreatedStr(String createdStr) {
        this.created = DateTimeUtil.parse(createdStr);
    }

}
