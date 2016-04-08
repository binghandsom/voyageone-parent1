package com.voyageone.components.jumei.bean;

/**
 * @author james.li on 2016/1/23.
 * @version 2.0.0
 */
public class JmProductBean_Spus extends JmBaseBean {
    private String partner_spu_no;

    private String jumei_spu_no;

    private String upc_code;

    private String propery;

    private String size;

    private String attribute;

    private Double abroad_price;

    private String area_code;

    private String abroad_url;

    private String verticalImage;

    private JmProductBean_Spus_Sku skuInfo;

    public String getPartner_spu_no() {
        return partner_spu_no;
    }

    public void setPartner_spu_no(String partner_spu_no) {
        this.partner_spu_no = partner_spu_no;
    }

    public String getJumei_spu_no() {
        return jumei_spu_no;
    }

    public void setJumei_spu_no(String jumei_spu_no) {
        this.jumei_spu_no = jumei_spu_no;
    }

    public String getUpc_code() {
        return upc_code;
    }

    public void setUpc_code(String upc_code) {
        this.upc_code = upc_code;
    }

    public String getPropery() {
        return propery;
    }

    public void setPropery(String propery) {
        this.propery = propery;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Double getAbroad_price() {
        return abroad_price;
    }

    public void setAbroad_price(Double abroad_price) {
        this.abroad_price = abroad_price;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getAbroad_url() {
        return abroad_url;
    }

    public void setAbroad_url(String abroad_url) {
        this.abroad_url = abroad_url;
    }

    public String getVerticalImage() {
        return verticalImage;
    }

    public void setVerticalImage(String verticalImage) {
        this.verticalImage = verticalImage;
    }

    public JmProductBean_Spus_Sku getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(JmProductBean_Spus_Sku skuInfo) {
        this.skuInfo = skuInfo;
    }
}
