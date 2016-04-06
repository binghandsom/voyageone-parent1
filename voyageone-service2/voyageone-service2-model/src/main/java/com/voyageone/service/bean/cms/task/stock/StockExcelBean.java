package com.voyageone.service.bean.cms.task.stock;

import java.math.BigDecimal;

/**
 * Created by morse.lu on 2016/3/21.
 */
public class StockExcelBean {
    private enum Property {
        property1, property2, property3, property4
    }

    private String productModel;
    private String productCode;
    private String sku;
    private String cartId;
    private String property1;
    private String property2;
    private String property3;
    private String property4;
    private BigDecimal qty;
    private BigDecimal separateQty;
    private String status;

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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

    public BigDecimal getSeparateQty() {
        return separateQty;
    }

    public void setSeparateQty(BigDecimal separateQty) {
        this.separateQty = separateQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProperty(String propertyNa, String property) {

        if (Property.property1.name().equals(propertyNa)) {
            this.setProperty1(property);
        }
        if (Property.property2.name().equals(propertyNa)) {
            this.setProperty2(property);
        }
        if (Property.property3.name().equals(propertyNa)) {
            this.setProperty3(property);
        }
        if (Property.property4.name().equals(propertyNa)) {
            this.setProperty4(property);
        }
    }

    public String getProperty(String propertyNa) {

        if (Property.property1.name().equals(propertyNa)) {
            return this.getProperty1();
        }
        if (Property.property2.name().equals(propertyNa)) {
            return this.getProperty2();
        }
        if (Property.property3.name().equals(propertyNa)) {
            return this.getProperty3();
        }
        if (Property.property4.name().equals(propertyNa)) {
            return this.getProperty4();
        }

        return null;
    }
}
