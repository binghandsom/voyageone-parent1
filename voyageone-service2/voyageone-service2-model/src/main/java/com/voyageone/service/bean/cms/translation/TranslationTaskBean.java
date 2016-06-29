package com.voyageone.service.bean.cms.translation;

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

    TranslationTaskBean_CommonFields commonFields;
    List<TranslationTaskBean_CustomProps> CustomProps;


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

    public List<TranslationTaskBean_CustomProps> getCustomProps() {
        return CustomProps;
    }

    public void setCustomProps(List<TranslationTaskBean_CustomProps> customProps) {
        CustomProps = customProps;
    }
}
