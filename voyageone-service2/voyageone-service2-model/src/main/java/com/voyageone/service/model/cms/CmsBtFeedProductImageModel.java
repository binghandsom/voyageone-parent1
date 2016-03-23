package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by james.li on 2015/11/30.
 */
public class CmsBtFeedProductImageModel extends BaseModel {
    private String channelId;
    private String code;
    private String imageUrl;
    private String imageName;
    private int imageTypeId = 1;
    private Integer sentFlag = 0;

    public CmsBtFeedProductImageModel(String channelId,String code, String imageUrl,int index, String modifier){
        this.channelId = channelId;
        this.imageUrl = imageUrl;
        if("010".equalsIgnoreCase(channelId)||"012".equalsIgnoreCase(channelId)) {
            this.imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        }else{
            if("015".equalsIgnoreCase(channelId)){
                this.imageName = code + "-" + index;
            }else{
                this.imageName = channelId + "-" + code + "-" + index;
            }
//            this.imageName = channelId + "-" + code + "-" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        }
        this.code = code;
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

    public Integer getSentFlag() {
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
