package com.voyageone.components.jumei.bean;

import com.voyageone.common.util.StringUtils;

import java.util.List;

/**
 * @author james.li on 2016/2/15.
 * @version 2.0.0
 */
public class JmGetProductInfoRes extends JmBaseBean {
    private String product_id;

    private Integer category_v3_4;

    private Integer brand_id;

    private String brand_name;

    private String name;

    private String foreign_language_name;

    private String function_ids;

    private String hash_ids;

    private List<JmGetProductInfo_Spus> spus;

    public String getHash_ids() {
        if(!StringUtils.isEmpty(hash_ids)){
            String[] temp = hash_ids.split(",");
            return temp[temp.length-1];
        }
        return hash_ids;
    }

    public void setHash_ids(String hash_ids) {
        this.hash_ids = hash_ids;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Integer getCategory_v3_4() {
        return category_v3_4;
    }

    public void setCategory_v3_4(Integer category_v3_4) {
        this.category_v3_4 = category_v3_4;
    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
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

    public List<JmGetProductInfo_Spus> getSpus() {
        return spus;
    }

    public void setSpus(List<JmGetProductInfo_Spus> spus) {
        this.spus = spus;
    }
}
