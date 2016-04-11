package com.voyageone.web2.sdk.api;


import com.voyageone.common.masterdate.schema.utils.JsonUtil;

/**
 * Respose Entity
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoApiUpdateResponse extends VoApiResponse {

    protected int insertedCount = 0;

    protected int matchedCount = 0;

    protected int removedCount = 0;

    protected int modifiedCount = 0;

    public int getInsertedCount() {
        return insertedCount;
    }

    public void setInsertedCount(int insertedCount) {
        this.insertedCount = insertedCount;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public int getRemovedCount() {
        return removedCount;
    }

    public void setRemovedCount(int removedCount) {
        this.removedCount = removedCount;
    }

    public int getModifiedCount() {
        return modifiedCount;
    }

    public void setModifiedCount(int modifiedCount) {
        this.modifiedCount = modifiedCount;
    }

    @Override
    public String toString() {
        return JsonUtil.bean2Json(this);
    }
}
