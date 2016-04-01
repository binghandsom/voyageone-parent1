package com.voyageone.task2.cms.bean;

import com.csvreader.CsvReader;
import com.voyageone.common.util.MD5;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author jonasvlag. 16/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ShoeCityFeedBean {

    private String upc;

    private String code;

    private String size;

    private String category;

    private BigDecimal cost;

    private String brand;

    private BigDecimal vo_cost;

    private String img_id;

    private String color;

    private String size_type;

    private String product_type;

    private String md5;

    private int saved;

    public ShoeCityFeedBean() {
        // MyBatis Used
    }

    public ShoeCityFeedBean(CsvReader reader) throws IOException {

        this.upc = reader.get(0);
        this.code = reader.get(1);
        this.size = reader.get(2);
        this.category = reader.get(3);
        // 4 为库存不读取
        String cost = reader.get(5);
        this.cost = new BigDecimal(cost);
        this.brand = reader.get(6);
        String voCost = reader.get(7);
        this.vo_cost = new BigDecimal(voCost);
        this.img_id = reader.get(8);
        this.color = reader.get(9);
        this.size_type = reader.get(10);
        this.product_type = reader.get(11);

        this.saved = 1;

        String contents = this.upc +
                this.code +
                this.size +
                this.category +
                this.cost +
                this.brand +
                this.vo_cost +
                this.img_id +
                this.color +
                this.size_type +
                this.product_type;

        this.md5 = MD5.getMD5(contents);
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getVo_cost() {
        return vo_cost;
    }

    public void setVo_cost(BigDecimal vo_cost) {
        this.vo_cost = vo_cost;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize_type() {
        return size_type;
    }

    public void setSize_type(String size_type) {
        this.size_type = size_type;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSku() {
        return getCode().replace(" ", "-") + "-" + getSize();
    }

    public String getClientSku() {
        return getCode() + "-" + getSize();
    }

    public int getSaved() {
        return saved;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }
}
