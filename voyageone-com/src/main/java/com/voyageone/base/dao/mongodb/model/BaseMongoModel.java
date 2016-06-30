package com.voyageone.base.dao.mongodb.model;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * BaseMongoModel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
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

    protected String created = DateTimeUtil.getNow();
    protected String creater = "0";
    protected String modified = DateTimeUtil.getNowTimeStamp();
    protected String modifier = "0";

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
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
