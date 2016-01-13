package com.voyageone.cms.modelbean;

import java.util.Date;

public class CnTMProductExtend  {
    private String tbName;

    private String tbAttachmentName;

    private String tmName;

    private String tmAttachmentName;

    private String tgName;

    private String tgAttachmentName;

    private String tmShortDescription;

    private Integer tmCategoryId;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    private String tmLongDescription;

    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName == null ? null : tbName.trim();
    }

    public String getTbAttachmentName() {
        return tbAttachmentName;
    }

    public void setTbAttachmentName(String tbAttachmentName) {
        this.tbAttachmentName = tbAttachmentName == null ? null : tbAttachmentName.trim();
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName == null ? null : tmName.trim();
    }

    public String getTmAttachmentName() {
        return tmAttachmentName;
    }

    public void setTmAttachmentName(String tmAttachmentName) {
        this.tmAttachmentName = tmAttachmentName == null ? null : tmAttachmentName.trim();
    }

    public String getTgName() {
        return tgName;
    }

    public void setTgName(String tgName) {
        this.tgName = tgName == null ? null : tgName.trim();
    }

    public String getTgAttachmentName() {
        return tgAttachmentName;
    }

    public void setTgAttachmentName(String tgAttachmentName) {
        this.tgAttachmentName = tgAttachmentName == null ? null : tgAttachmentName.trim();
    }

    public String getTmShortDescription() {
        return tmShortDescription;
    }

    public void setTmShortDescription(String tmShortDescription) {
        this.tmShortDescription = tmShortDescription == null ? null : tmShortDescription.trim();
    }

    public Integer getTmCategoryId() {
        return tmCategoryId;
    }

    public void setTmCategoryId(Integer tmCategoryId) {
        this.tmCategoryId = tmCategoryId;
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

    public String getTmLongDescription() {
        return tmLongDescription;
    }

    public void setTmLongDescription(String tmLongDescription) {
        this.tmLongDescription = tmLongDescription == null ? null : tmLongDescription.trim();
    }
}