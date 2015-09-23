package com.voyageone.batch.bi.bean.modelbean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Kylin on 2015/7/15.
 */
public class ViewProductBean {

    private int shop_id;

    private String num_iid;

    private int num;

    private BigDecimal price;

    private String approve_status;

    private Date list_time;

    private Date modified;

    private Date delist_time;

    private String cid;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public Date getList_time() {
        return list_time;
    }

    public void setList_time(Date list_time) {
        this.list_time = list_time;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getDelist_time() {
        return delist_time;
    }

    public void setDelist_time(Date delist_time) {
        this.delist_time = delist_time;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
