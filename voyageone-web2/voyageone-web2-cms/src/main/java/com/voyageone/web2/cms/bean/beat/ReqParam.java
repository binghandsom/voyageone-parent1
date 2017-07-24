package com.voyageone.web2.cms.bean.beat;

import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;

/**
 * 价格披露请求参数模型(适配多请求)
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class ReqParam {

    private int offset;

    private int size;

    private BeatFlag flag;

    private Integer beat_id;

    private Integer task_id;

    private String num_iid;

    private Boolean force;

    private String code;

    private String productModel;

    private int promotionId;

    private String searchKey;

    private Integer cartId;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public BeatFlag getFlag() {
        return flag;
    }

    public void setFlag(BeatFlag flag) {
        this.flag = flag;
    }

    public Integer getBeat_id() {
        return beat_id;
    }

    public void setBeat_id(Integer beat_id) {
        this.beat_id = beat_id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}
