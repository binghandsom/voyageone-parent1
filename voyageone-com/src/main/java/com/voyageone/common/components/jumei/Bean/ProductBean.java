package com.voyageone.common.components.jumei.Bean;

import java.util.List;

/**
 * @author james.li on 2016/1/23.
 * @version 2.0.0
 */
public class ProductBean {
    private String product_spec_number;

    private Integer category_v3_4_id;

    private Integer brand_id;

    private String name;

    private String foreign_language_name;

    private String function_ids;

    private String normalImage;

    private String verticalImage;

    private String diaoxingImage;

    private List<SpusBean> spus;

    private DealInfoBean dealInfo;

    public String getProduct_spec_number() {
        return product_spec_number;
    }

    public void setProduct_spec_number(String product_spec_number) {
        this.product_spec_number = product_spec_number;
    }

    public Integer getCategory_v3_4_id() {
        return category_v3_4_id;
    }

    public void setCategory_v3_4_id(Integer category_v3_4_id) {
        this.category_v3_4_id = category_v3_4_id;
    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForeign_language_name() {
        return foreign_language_name;
    }

    public void setForeign_language_name(String foreign_language_name) {
        this.foreign_language_name = foreign_language_name;
    }

    public String getFunction_ids() {
        return function_ids;
    }

    public void setFunction_ids(String function_ids) {
        this.function_ids = function_ids;
    }

    public String getNormalImage() {
        return normalImage;
    }

    public void setNormalImage(String normalImage) {
        this.normalImage = normalImage;
    }

    public String getVerticalImage() {
        return verticalImage;
    }

    public void setVerticalImage(String verticalImage) {
        this.verticalImage = verticalImage;
    }

    public String getDiaoxingImage() {
        return diaoxingImage;
    }

    public void setDiaoxingImage(String diaoxingImage) {
        this.diaoxingImage = diaoxingImage;
    }

    public List<SpusBean> getSpus() {
        return spus;
    }

    public void setSpus(List<SpusBean> spus) {
        this.spus = spus;
    }

    public DealInfoBean getDealInfo() {
        return dealInfo;
    }

    public void setDealInfo(DealInfoBean dealInfo) {
        this.dealInfo = dealInfo;
    }
}
