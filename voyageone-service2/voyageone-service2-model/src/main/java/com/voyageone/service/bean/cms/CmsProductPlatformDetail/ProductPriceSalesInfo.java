package com.voyageone.service.bean.cms.CmsProductPlatformDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/11/18.
 */
public class ProductPriceSalesInfo {

    List<ProductPrice> productPriceList;

    Map<String, Map<String, Object>> sales;


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
