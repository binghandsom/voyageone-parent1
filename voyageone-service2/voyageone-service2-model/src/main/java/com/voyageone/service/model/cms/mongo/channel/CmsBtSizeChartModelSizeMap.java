package com.voyageone.service.model.cms.mongo.channel;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

/**
 * Created by gjl on 2016/5/5.
 */
public class CmsBtSizeChartModelSizeMap extends BaseMongoMap<String, Object> {
    private String originalSize;
    private String adjustSize;
    private String usual;

    public String getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(String originalSize) {
        this.originalSize = originalSize;
    }

    public String getAdjustSize() {
        return adjustSize;
    }

    public void setAdjustSize(String adjustSize) {
        this.adjustSize = adjustSize;
    }

    public String getUsual() {
        return usual;
    }

    public void setUsual(String usual) {
        this.usual = usual;
    }

}
