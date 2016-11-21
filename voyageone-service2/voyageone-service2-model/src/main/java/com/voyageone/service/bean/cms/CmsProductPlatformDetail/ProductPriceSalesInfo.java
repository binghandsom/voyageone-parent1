package com.voyageone.service.bean.cms.CmsProductPlatformDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/11/18.
 */
public class ProductPriceSalesInfo {

    List<ProductPrice> productPriceList;

    Map<String, Map<String, Object>> sales;
    String brand;
    int quantity;
    String image1;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    String productNameEn;


    public List<ProductPrice> getProductPriceList() {
        return productPriceList;
    }

    public void setProductPriceList(List<ProductPrice> productPriceList) {
        this.productPriceList = productPriceList;
    }

    public Map<String, Map<String, Object>> getSales() {
        return sales;
    }

    public void setSales(Map<String, Map<String, Object>> sales) {
        this.sales = sales;
    }

}
