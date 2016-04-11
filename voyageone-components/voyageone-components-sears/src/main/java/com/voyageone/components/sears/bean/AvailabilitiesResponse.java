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
public class AvailabilitiesResponse {

    private PaginationBean pagination;
    private List<AvailabilityBean> product;

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<AvailabilityBean> getProduct() {
        return product;
    }

    public void setProduct(List<AvailabilityBean> product) {
        this.product = product;
    }

    public List<AvailabilityBean> getProductBeans() {
        return product;
    }

    public void setProductBeans(List<AvailabilityBean> productBeans) {
        this.product = productBeans;
    }
}
