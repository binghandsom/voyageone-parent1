package com.voyageone.service.model.cms.mongo.feed;

/**
 * Feed 各平台待Approve信息
 *
 * @Author rex.wu
 * @Create 2017-07-10 19:46
 */
public class CmsBtFeedInfoModel_ApproveItem {
    /**
     * 平台ID
     */
    private Integer cartId;
    /**
     * 是否Approve, 1->Approve; 0->Not approve
     */
    private Integer approve;
    /**
     * 	Days Old Before Sharing, 默认0
     */
    private int day;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
