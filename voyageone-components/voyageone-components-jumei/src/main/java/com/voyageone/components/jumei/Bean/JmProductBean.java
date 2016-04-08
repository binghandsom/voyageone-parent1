package com.voyageone.components.jumei.Bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/1/23.
 * @version 2.0.0
 */
public class JmProductBean extends JmBaseBean {
    private String product_spec_number;

    private String jumei_product_id;

    private Integer category_v3_4_id;

    private Integer brand_id;

    private String name;

    private String foreign_language_name;

    private String function_ids;

    private String normalImage;

    private String verticalImage;

    private String diaoxingImage;

    @JsonIgnore
    private List<JmProductBean_Spus> spus;

    @JsonIgnore
    private JmProductBean_DealInfo dealInfo;

    public String getProduct_spec_number() {
        return product_spec_number;
    }

    public void setProduct_spec_number(String product_spec_number) {
        this.product_spec_number = product_spec_number;
    }

    public String getJumei_product_id() {
        return jumei_product_id;
    }

    public void setJumei_product_id(String jumei_product_id) {
        this.jumei_product_id = jumei_product_id;
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

    public List<JmProductBean_Spus> getSpus() {
        return spus;
    }

    public void setSpus(List<JmProductBean_Spus> spus) {
        this.spus = spus;
    }

    public JmProductBean_DealInfo getDealInfo() {
        return dealInfo;
    }

    public void setDealInfo(JmProductBean_DealInfo dealInfo) {
        this.dealInfo = dealInfo;
    }

    public String toJsonStr() throws Exception {
        JmProductBean resultBean = new JmProductBean();
        resultBean.setProduct_spec_number(product_spec_number);
        resultBean.setCategory_v3_4_id(category_v3_4_id);
        resultBean.setBrand_id(brand_id);
        resultBean.setName(name);
        resultBean.setForeign_language_name(foreign_language_name);
        resultBean.setFunction_ids(function_ids);
        resultBean.setNormalImage(normalImage);
        resultBean.setVerticalImage(verticalImage);
        resultBean.setDiaoxingImage(diaoxingImage);
        return JacksonUtil.bean2JsonNotNull(resultBean);
    }

    @JsonIgnore
    public String getSpusString() throws IOException {
        return JacksonUtil.bean2JsonNotNull(spus);
    }

    @JsonIgnore
    public String getDealInfoString() throws IOException {
        List<JmProductBean_DealInfo> dealInfos = new ArrayList<>();
        dealInfos.add(dealInfo);
        return JacksonUtil.bean2JsonNotNull(dealInfos);
    }

    public void check() {

    }
}
