package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtDataAmountModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private String amountName;
    /**

     */
    private String amountVal;
    /**

     */
    private String comment;

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
    public String getAmountName() {

        return this.amountName;
    }

    public void setAmountName(String amountName) {
        if (amountName != null) {
            this.amountName = amountName;
        } else {
            this.amountName = "";
        }

    }


    /**

     */
    public String getAmountVal() {

        return this.amountVal;
    }

    public void setAmountVal(String amountVal) {
        if (amountVal != null) {
            this.amountVal = amountVal;
        } else {
            this.amountVal = "";
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