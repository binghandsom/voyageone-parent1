package com.voyageone.service.bean.cms.task;

import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;

import java.util.List;

/**
 * Created by gjl on 2016/5/10.
 */
public class CmsBtSizeChartBean extends CmsBtSizeChartModel {
    private List<String> brandNameTrans;
    private List<String> productTypeTrans;
    private List<String> sizeTypeTrans;
    private List<TypeChannelBean> brandNameTransBean;
    private List<TypeChannelBean> productTypeTransBean;
    private List<TypeChannelBean> sizeTypeTransBean;

    public List<TypeChannelBean> getBrandNameTransBean() {
        return brandNameTransBean;
    }

    public void setBrandNameTransBean(List<TypeChannelBean> brandNameTransBean) {
        this.brandNameTransBean = brandNameTransBean;
    }

    public List<TypeChannelBean> getSizeTypeTransBean() {
        return sizeTypeTransBean;
    }

    public void setSizeTypeTransBean(List<TypeChannelBean> sizeTypeTransBean) {
        this.sizeTypeTransBean = sizeTypeTransBean;
    }

    public List<TypeChannelBean> getProductTypeTransBean() {
        return productTypeTransBean;
    }

    public void setProductTypeTransBean(List<TypeChannelBean> productTypeTransBean) {
        this.productTypeTransBean = productTypeTransBean;
    }

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
