package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by gjl on 2016/12/21.
 */
public class CmsMtFeedConfigModel extends BaseModel {


    private String orderChannelId;

    private String cfgName;

    private String cfgVal2;

    private String cfgVal3;

    private Integer isAttribute;

    private Integer attributeType;

    private String comment;

    private Integer displaySort;

    private Integer status;

    private String cfgVal1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId == null ? null : orderChannelId.trim();
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName == null ? null : cfgName.trim();
    }

    public String getCfgVal2() {
        return cfgVal2;
    }

    public void setCfgVal2(String cfgVal2) {
        this.cfgVal2 = cfgVal2 == null ? null : cfgVal2.trim();
    }

    public String getCfgVal3() {
        return cfgVal3;
    }

    public void setCfgVal3(String cfgVal3) {
        this.cfgVal3 = cfgVal3 == null ? null : cfgVal3.trim();
    }

    public Integer getIsAttribute() {
        return isAttribute;
    }

    public void setIsAttribute(Integer isAttribute) {
        this.isAttribute = isAttribute;
    }

    public Integer getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(Integer attributeType) {
        this.attributeType = attributeType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getDisplaySort() {
        return displaySort;
    }

    public void setDisplaySort(Integer displaySort) {
        this.displaySort = displaySort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCfgVal1() {
        return cfgVal1;
    }

    public void setCfgVal1(String cfgVal1) {
        this.cfgVal1 = cfgVal1 == null ? null : cfgVal1.trim();
    }
}
