/**
 * 
 */
package com.voyageone.batch.cms.model;

/**
 * @author jacky
 *
 */
public class PropPropertiesModel {

    private Integer property_id;
	private Long cid;
	private Integer is_product;
	private String prop_code;
	private String prop_content;
    private String brand_id;
    private String creater;
    private String modifier;

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Integer getProperty_id() {
        return property_id;
    }

    public void setProperty_id(Integer property_id) {
        this.property_id = property_id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Integer getIs_product() {
        return is_product;
    }

    public void setIs_product(Integer is_product) {
        this.is_product = is_product;
    }

    public String getProp_code() {
        return prop_code;
    }

    public void setProp_code(String prop_code) {
        this.prop_code = prop_code;
    }

    public String getProp_content() {
        return prop_content;
    }

    public void setProp_content(String prop_content) {
        this.prop_content = prop_content;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }
}
