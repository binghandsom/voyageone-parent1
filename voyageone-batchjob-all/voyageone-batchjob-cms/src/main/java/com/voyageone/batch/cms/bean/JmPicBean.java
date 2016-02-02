package com.voyageone.batch.cms.bean;

import java.sql.Timestamp;

/**
 * @author aooer 2016/1/25.
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmPicBean/* extends BaseModel*/{

    private int seq;
    private String channelId;
    private String imageKey;
    private int imageType;
    private String imageTypeExtend;
    private int imageIndex;
    private String originUrl;
    private String jmUrl;
    private int synFlg;

    /**
     * 创建时间.
     */
    private Timestamp created;
    /**
     * 创建者.
     */
    private String creater;
    /**
     * 更新时间.
     */
    private Timestamp modified;
    /**
     * 更新者.
     */
    private String modifier;

    public Timestamp getCreated() {
        return created;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }
    public String getCreater() {
        return creater;
    }
    public void setCreater(String creater) {
        this.creater = creater;
    }
    public Timestamp getModified() {
        return modified;
    }
    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
    public String getModifier() {
        return modifier;
    }
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getImageTypeExtend() {
        return imageTypeExtend;
    }

    public void setImageTypeExtend(String imageTypeExtend) {
        this.imageTypeExtend = imageTypeExtend;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getJmUrl() {
        return jmUrl;
    }

    public void setJmUrl(String jmUrl) {
        this.jmUrl = jmUrl;
    }

    public int getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(int synFlg) {
        this.synFlg = synFlg;
    }

    public PicType getPicType() {
        switch (imageType){
            case 1:
            case 2:
            case 3:
                return PicType.product;
            case 4:
            case 5:
                return PicType.brand;
            case 6:
                return PicType.channel;
        }
        throw new IllegalArgumentException("参数校验不通过imageType:"+imageType);
    }

    enum PicType{
        product,
        brand,
        channel
    }
}
