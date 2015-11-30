package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;

public class CmsBtProductModel extends ChannelPartitionModel {
    public static final String COLLECTION_NAME = "cms_bt_product";

    private int prodId;
    private int cid;
    private String cidPath;

    private CmsBtProductModel_Field field = new CmsBtProductModel_Field();
    private CmsBtProductModel_Group group = new CmsBtProductModel_Group();
    private List<CmsBtProductModel_Sku> skus = new ArrayList<>();
    private List<CmsBtProductModel_Tag> tags = new ArrayList<>();
    private CmsBtProductModel_Feed feedAtts = new CmsBtProductModel_Feed();
    private Date createTime = new Date();
    private String creater = "0";
    private Date updateTime = new Date();
    private String updater = "0";

    public CmsBtProductModel(String channelId) {
        super(channelId);
    }

    public String getCollectionName() {
        return getCollectionName(this.channelId);
    }

    public static String getCollectionName(String channelId) {
        return COLLECTION_NAME + getPartitionValue(channelId);
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCidPath() {
        return cidPath;
    }

    public void setCidPath(String cidPath) {
        this.cidPath = cidPath;
    }


    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public CmsBtProductModel_Field getField() {
        return field;
    }

    public void setField(CmsBtProductModel_Field field) {
        this.field = field;
    }

    public CmsBtProductModel_Group getGroup() {
        return group;
    }

    public void setGroup(CmsBtProductModel_Group group) {
        this.group = group;
    }

    public List<CmsBtProductModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtProductModel_Sku> skus) {
        this.skus = skus;
    }

    public List<CmsBtProductModel_Tag> getTags() {
        return tags;
    }

    public void setTags(List<CmsBtProductModel_Tag> tags) {
        this.tags = tags;
    }

    public CmsBtProductModel_Feed getFeedAtts() {
        return feedAtts;
    }

    public void setFeedAtts(CmsBtProductModel_Feed feedAtt) {
        this.feedAtts = feedAtt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

}