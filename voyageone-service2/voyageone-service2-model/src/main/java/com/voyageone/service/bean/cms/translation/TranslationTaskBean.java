package com.voyageone.service.bean.cms.translation;

import com.voyageone.service.bean.cms.CustomPropBean;

import java.util.List;

/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */
public class TranslationTaskBean {

    private long prodId;
    private String productCode;
    private String feedCategory;
    private String catPath;

    TranslationTaskBean_CommonFields commonFields;
    List<CustomPropBean> customProps;


    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public TranslationTaskBean_CommonFields getCommonFields() {
        return commonFields;
    }

    public void setCommonFields(TranslationTaskBean_CommonFields commonFields) {
        this.commonFields = commonFields;
    }

    public List<CustomPropBean> getCustomProps() {
        return customProps;
    }

    public void setCustomProps(List<CustomPropBean> customProps) {
        this.customProps = customProps;
    }

    public String getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(String category) {
        this.feedCategory = category;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }
}
