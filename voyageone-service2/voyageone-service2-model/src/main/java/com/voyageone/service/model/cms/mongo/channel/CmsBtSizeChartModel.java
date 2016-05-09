package com.voyageone.service.model.cms.mongo.channel;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjl on 2016/5/5.
 */
public class CmsBtSizeChartModel extends ChannelPartitionModel {
    private int sizeChartId;
    private String sizeChartName;
    private String finish;
    private List<String> brandName;
    private List<String>productType;
    private List<String> sizeType;
    private List<CmsBtSizeChartModelSizeMap> sizeMap = new ArrayList<>();
    private int active;

    public int getSizeChartId() {
        return sizeChartId;
    }

    public void setSizeChartId(int sizeChartId) {
        this.sizeChartId = sizeChartId;
    }

    public String getSizeChartName() {
        return sizeChartName;
    }

    public void setSizeChartName(String sizeChartName) {
        this.sizeChartName = sizeChartName;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public List<String> getBrandName() {
        return brandName;
    }

    public void setBrandName(List<String> brandName) {
        this.brandName = brandName;
    }

    public List<String> getProductType() {
        return productType;
    }

    public void setProductType(List<String> productType) {
        this.productType = productType;
    }

    public List<String> getSizeType() {
        return sizeType;
    }

    public void setSizeType(List<String> sizeType) {
        this.sizeType = sizeType;
    }

    public List<CmsBtSizeChartModelSizeMap> getSizeMap() {
        return sizeMap;
    }

    public void setSizeMap(List<CmsBtSizeChartModelSizeMap> sizeMap) {
        this.sizeMap = sizeMap;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
