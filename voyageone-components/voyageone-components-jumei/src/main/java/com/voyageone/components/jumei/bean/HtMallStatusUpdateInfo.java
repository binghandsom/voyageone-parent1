package com.voyageone.components.jumei.bean;

/**
 * 聚美商城 批量上下架商城商品[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallStatusUpdateInfo {

    private String jumeiMallId;  // 聚美Mall Id.
    private String status;  // 状态  display:显示,hidden:隐藏,forbidden:黑名单

    public String getJumeiMallId() {
        return jumeiMallId;
    }

    public void setJumeiMallId(String jumeiMallId) {
        this.jumeiMallId = jumeiMallId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
