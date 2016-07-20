package com.voyageone.base.dao.mongodb.model;

import com.voyageone.common.util.JsonUtil;

import java.util.Map;

/**
 * BulkUpdateModel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class BulkUpdateModel {
    Map<String, Object> updateMap;
    Map<String, Object> queryMap;

    public Map<String, Object> getUpdateMap() {
        return updateMap;
    }

    public void setUpdateMap(Map<String, Object> updateMap) {
        this.updateMap = updateMap;
    }

    public Map<String, Object> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, Object> queryMap) {
        this.queryMap = queryMap;
    }

    @Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }
}
