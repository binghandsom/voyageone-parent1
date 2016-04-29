package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtImagesModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private String code;
    /**

     */
    private String originalUrl;
    /**

     */
    private String imgName;
    /**

     */
    private int updFlg;


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
    public String getOriginalUrl() {

        return this.originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        if (originalUrl != null) {
            this.originalUrl = originalUrl;
        } else {
            this.originalUrl = "";
        }

    }


    /**

     */
    public String getImgName() {

        return this.imgName;
    }

    public void setImgName(String imgName) {
        if (imgName != null) {
            this.imgName = imgName;
        } else {
            this.imgName = "";
        }

    }


    /**

     */
    public int getUpdFlg() {

        return this.updFlg;
    }

    public void setUpdFlg(int updFlg) {
        this.updFlg = updFlg;
    }


}