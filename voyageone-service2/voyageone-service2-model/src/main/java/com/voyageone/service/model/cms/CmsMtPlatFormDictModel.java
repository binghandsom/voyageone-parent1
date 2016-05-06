package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtPlatformDictModel extends BaseModel {


    /**

     */
    private String orderChannelId;
    /**

     */
    private int cartId;
    /**

     */
    private String name;
    /**

     */
    private String value;
    /**

     */
    private String comment;
    /**

     */


    /**

     */
    public String getOrderChannelId() {

        return this.orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        if (orderChannelId != null) {
            this.orderChannelId = orderChannelId;
        } else {
            this.orderChannelId = "";
        }

    }


    /**

     */
    public int getCartId() {

        return this.cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    /**

     */
    public String getName() {

        return this.name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }

    }


    /**

     */
    public String getValue() {

        return this.value;
    }

    public void setValue(String value) {
        if (value != null) {
            this.value = value;
        } else {
            this.value = "";
        }

    }


    /**

     */
    public String getComment() {

        return this.comment;
    }

    public void setComment(String comment) {
        if (comment != null) {
            this.comment = comment;
        } else {
            this.comment = "";
        }

    }

}