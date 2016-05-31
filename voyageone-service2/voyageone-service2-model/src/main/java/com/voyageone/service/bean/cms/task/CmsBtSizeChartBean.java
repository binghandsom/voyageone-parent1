package com.voyageone.service.bean.cms.task;

import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;

import java.util.List;

/**
 * Created by gjl on 2016/5/10.
 */
public class CmsBtSizeChartBean extends CmsBtSizeChartModel {
    private List<String> brandNameTrans;
    private List<String> productTypeTrans;
    private List<String> sizeTypeTrans;

    public List<String> getBrandNameTrans() {
        return brandNameTrans;
    }

    public void setBrandNameTrans(List<String> brandNameTrans) {
        this.brandNameTrans = brandNameTrans;
    }

    public List<String> getProductTypeTrans() {
        return productTypeTrans;
    }

    public void setProductTypeTrans(List<String> productTypeTrans) {
        this.productTypeTrans = productTypeTrans;
    }

    public List<String> getSizeTypeTrans() {
        return sizeTypeTrans;
    }

    public void setSizeTypeTrans(List<String> sizeTypeTrans) {
        this.sizeTypeTrans = sizeTypeTrans;
    }
}
