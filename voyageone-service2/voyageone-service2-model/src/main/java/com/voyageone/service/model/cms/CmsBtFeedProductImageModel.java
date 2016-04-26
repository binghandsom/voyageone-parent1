package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtFeedProductImageModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private String code;
    /**

     */
    private int imageTypeId;
    /**

     */
    private String imageUrl;
    /**

     */
    private String imageName;
    /**

     */
    private int sentFlag;


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
    public String getCode() {

        return this.code;
    }

    public void setCode(String code) {
        if (code != null) {
            this.code = code;
        } else {
            this.code = "";
        }

    }


    /**

     */
    public int getImageTypeId() {

        return this.imageTypeId;
    }

    public void setImageTypeId(int imageTypeId) {
        this.imageTypeId = imageTypeId;
    }


    /**

     */
    public String getImageUrl() {

        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        } else {
            this.imageUrl = "";
        }

    }


    /**

     */
    public String getImageName() {

        return this.imageName;
    }

    public void setImageName(String imageName) {
        if (imageName != null) {
            this.imageName = imageName;
        } else {
            this.imageName = "";
        }

    }


    /**

     */
    public int getSentFlag() {

        return this.sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }


}