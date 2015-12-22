package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
public class CmsBtTagLogModel extends BaseModel {

    private int seq;
    private long productId;
    private int tagId;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
