/*
 * CmsBtJmMasterBrandModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class CmsBtJmMasterBrandModel extends BaseModel {
    protected String name;

    protected Integer jmMasterBrandId;

    protected String enName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getJmMasterBrandId() {
        return jmMasterBrandId;
    }

    public void setJmMasterBrandId(Integer jmMasterBrandId) {
        this.jmMasterBrandId = jmMasterBrandId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }
}