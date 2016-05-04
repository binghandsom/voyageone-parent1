package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtSxWorkloadModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private int groupId;
    /**

     */
    private int publish_status;
    /**

     */
    private int cartId;


    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**

     */
    public int getGroupId() {

        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


    /**

     */
    public int getPublishStatus() {

        return this.publish_status;
    }

    public void setPublishStatus(int publish_status) {
        this.publish_status = publish_status;
    }


    /**

     */
    public int getCartId() {

        return this.cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


}