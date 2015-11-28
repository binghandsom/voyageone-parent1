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
    private Map<String, Object> group = new HashMap<>();
    private List<Map<String, Object>> skus = new ArrayList<>();
    private List<Map<String, Object>> tags = new ArrayList<>();
    private Map<String, Object> feedAtts = new HashMap<>();

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

    private Date updateTime = new Date();
    private String updater = "0";
    private Date createTime = new Date();
    private String creater = "0";

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


    public Map getGroup() {
        return group;
    }

    public void setGroup(Map group) {
        this.group = group;
    }

    public Object getGroupAtt(String key) {
        return this.group.get(key);
    }

    public void setGroupAtt(String key, Object value) {
        if (value == null) {
            this.group.remove(key);
        } else {
            this.group.put(key, value);
        }
    }

    public List<Map<String, Object>> getSkus() {
        return skus;
    }

    public void setSkus(List<Map<String, Object>> skus) {
        this.skus = skus;
    }

    public List<Map<String, Object>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, Object>> tags) {
        this.tags = tags;
    }

    public Map getFeedAtts() {
        return feedAtts;
    }

    public void setFeedAtts(Map feedAtt) {
        this.feedAtts = feedAtt;
    }

    public Object getFeedAtt(String key) {
        return this.feedAtts.get(key);
    }

    public void setFeedAtt(String key, Object value) {
        if (value == null) {
            this.feedAtts.remove(key);
        } else {
            this.feedAtts.put(key, value);
        }
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

}