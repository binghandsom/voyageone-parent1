package com.voyageone.service.impl.cms.vomq.vomessage.body;

/**
 * 推送产品信息到wms系统
 *
 * @Author edward.lin
 * @Create 2017-04-18 16:03
 */
public class WmsCreateOrUpdateProductMQMessageBody_detail {

    private String origin;
    private Double kgWeight;
    private Double lbWeight;
    private Double price;
    private Double priceUnit;
    private String hsCode;
    private String declareName;
    private String declareUnit;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Double getKgWeight() {
        return kgWeight;
    }

    public void setKgWeight(Double kgWeight) {
        this.kgWeight = kgWeight;
    }

    public Double getLbWeight() {
        return lbWeight;
    }

    public void setLbWeight(Double lbWeight) {
        this.lbWeight = lbWeight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getDeclareName() {
        return declareName;
    }

    public void setDeclareName(String declareName) {
        this.declareName = declareName;
    }

    public String getDeclareUnit() {
        return declareUnit;
    }

    public void setDeclareUnit(String declareUnit) {
        this.declareUnit = declareUnit;
    }
}
