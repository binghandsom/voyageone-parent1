package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author gubuchun 15/12/24
 * @version 2.0.0
 */
public class CmsBtProductLogModel extends BaseModel {
    private int seq;
    private String channelId;
    private int productId;
    private String status;
    private String comment;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
