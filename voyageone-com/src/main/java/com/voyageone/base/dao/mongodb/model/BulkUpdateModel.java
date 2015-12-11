package com.voyageone.base.dao.mongodb.model;

import java.util.HashMap;

/**
 * Created by dell on 2015/12/11.
 */
public class BulkUpdateModel {
    HashMap<String, Object> updateMap;
    HashMap<String, Object> queryMap;

    public HashMap<String, Object> getUpdateMap() {
        return updateMap;
    }

    public void setUpdateMap(HashMap<String, Object> updateMap) {
        this.updateMap = updateMap;
    }

    public HashMap<String, Object> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(HashMap<String, Object> queryMap) {
        this.queryMap = queryMap;
    }
}
