package com.voyageone.base.dao.mongodb.model;

import com.voyageone.common.util.JsonUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BaseMongoModel {
    @Id
    protected String _id;
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /**
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return _id == null ? 0 : _id.hashCode();
    }

    /**
     *(non-Javadoc)
     * @return JsonUtil.getJsonString
     */
    @Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }
}
