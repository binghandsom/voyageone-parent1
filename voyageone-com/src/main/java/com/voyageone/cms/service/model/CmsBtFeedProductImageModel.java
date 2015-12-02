package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * Created by james.li on 2015/11/30.
 */
public class CmsBtFeedProductImageModel extends BaseMongoModel {
    private String channelId;
    private String imageUrl;
    private String imageName;
    private int imageTypeId = 1;
    private int sentFlag = 0;

    public CmsBtFeedProductImageModel(String channelId, String imageUrl,String modifier){
        this.channelId = channelId;
        this.imageUrl = imageUrl;
        this.imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        this.setModifier(modifier);
        this.setCreater(modifier);
    }
    public CmsBtFeedProductImageModel(){
        super();
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageTypeId() {
        return imageTypeId;
    }

    public void setImageTypeId(int imageTypeId) {
        this.imageTypeId = imageTypeId;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
