package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Administrator on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ProductBean {
    private String itemId;
    private String upc;
    private String status;
    private PriceBean price;
    private AvailabilityBean availability;

    @XmlElement(name = "product_details")
    private ProductDetailBean productDetails;

    public ProductDetailBean getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetailBean productDetails) {
        this.productDetails = productDetails;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PriceBean getPrice() {
        return price;
    }

    public void setPrice(PriceBean price) {
        this.price = price;
    }

    public AvailabilityBean getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityBean availability) {
        this.availability = availability;
    }
}
