package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "products")
public class ProductResponse {

    private PaginationBean pagination;
    private List<ProductBean> product;

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<ProductBean> getProduct() {
        return product;
    }

    public void setProduct(List<ProductBean> product) {
        this.product = product;
    }

    public List<ProductBean> getProductBeans() {
        return product;
    }

    public void setProductBeans(List<ProductBean> productBeans) {
        this.product = productBeans;
    }
}
