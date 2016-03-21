package com.voyageone.web2.cms.bean.promotion.task;

import java.math.BigDecimal;

/**
 * Created by morse.lu on 2016/3/21.
 */
public class StockExcelBean {
    private enum Property {
        property1, property2, property3, property4
    }

    private String product_model;
    private String product_code;
    private String sku;
    private String cart_id;
    private String property1;
    private String property2;
    private String property3;
    private String property4;
    private BigDecimal qty;
    private BigDecimal separate_qty;
    private String status;

    public String getProduct_model() {
        return product_model;
    }

    public void setProduct_model(String product_model) {
        this.product_model = product_model;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public String getProperty3() {
        return property3;
    }

    public void setProperty3(String property3) {
        this.property3 = property3;
    }

    public String getProperty4() {
        return property4;
    }

    public void setProperty4(String property4) {
        this.property4 = property4;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getSeparate_qty() {
        return separate_qty;
    }

    public void setSeparate_qty(BigDecimal separate_qty) {
        this.separate_qty = separate_qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProperty(String propertyNa) {
        Property property = Property.valueOf(propertyNa);
        if (Property.property1.equals(property)) {
            return this.getProperty1();
        }
        if (Property.property2.equals(property)) {
            return this.getProperty2();
        }
        if (Property.property3.equals(property)) {
            return this.getProperty3();
        }
        if (Property.property4.equals(property)) {
            return this.getProperty4();
        }

        return null;
    }
}
