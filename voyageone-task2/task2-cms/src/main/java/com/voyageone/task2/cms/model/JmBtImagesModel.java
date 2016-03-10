package com.voyageone.task2.cms.model;

import java.util.Date;

public class JmBtImagesModel {
    private Integer seq;

    private String channelId;

    private String imageKey;

    private Integer imageType;

    private String imageTypeExtend;

    private Integer imageIndex;

    private String originUrl;

    private String jmUrl;

    private Integer synFlg;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    // 附加属性 分组记录件数
    private Integer recCount;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey == null ? null : imageKey.trim();
    }

    public Integer getImageType() {
        return imageType;
    }

    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    public String getImageTypeExtend() {
        return imageTypeExtend;
    }

    public void setImageTypeExtend(String imageTypeExtend) {
        this.imageTypeExtend = imageTypeExtend == null ? null : imageTypeExtend.trim();
    }

    public Integer getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(Integer imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl == null ? null : originUrl.trim();
    }

    public String getJmUrl() {
        return jmUrl;
    }

    public void setJmUrl(String jmUrl) {
        this.jmUrl = jmUrl == null ? null : jmUrl.trim();
    }

    public Integer getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(Integer synFlg) {
        this.synFlg = synFlg;
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

    public Integer getRecCount() {
        return recCount;
    }

    public void setRecCount(Integer recCount) {
        this.recCount = recCount;
    }
}