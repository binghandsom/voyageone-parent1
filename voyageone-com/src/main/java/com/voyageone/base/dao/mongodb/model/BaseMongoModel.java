package com.voyageone.base.dao.mongodb.model;

import com.voyageone.common.util.JsonUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document
public class BaseMongoModel {
    @Id
    protected BigInteger id;

    /**
     * Returns the identifier of the document.
     *
     * @return the id
     */
    public BigInteger getId() {
        return id;
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

        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        BaseMongoModel that = (BaseMongoModel) obj;

        return this.id.equals(that.getId());
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
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
