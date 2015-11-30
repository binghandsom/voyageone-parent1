package com.voyageone.base.dao.mongodb.model;

import com.voyageone.common.util.JsonUtil;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class BaseMongoModel {

    @Id
    @MongoObjectId
    protected String _id;
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    private Date createTime = new Date();
    private String creater = "0";
    private Date updateTime = new Date();
    private String updater = "0";

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this._id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }
        BaseMongoModel that = (BaseMongoModel) obj;
        return this._id.equals(that.get_id());
    }

    @Override
    public int hashCode() {
        return _id == null ? 0 : _id.hashCode();
    }

    @Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }

}
