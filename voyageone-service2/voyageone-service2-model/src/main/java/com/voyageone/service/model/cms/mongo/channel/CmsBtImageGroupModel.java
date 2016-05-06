package com.voyageone.service.model.cms.mongo.channel;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * {@link CmsBtImageGroupModel} 的图片管理Model
 * @author jeff.duan, 16/5/5
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtImageGroupModel extends BaseMongoModel {

    private String channelId;
    private Integer cartId;
    private Long imageGroupId;
    private String imageGroupName;
    private Integer imageType;
    private Integer viewType;
    private List<String> brandName;
    private List<String> productType;
    private List<String> sizeType;
    private List<CmsBtImageGroupModel_Image> image;
    private Integer active;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Long getImageGroupId() {
        return imageGroupId;
    }

    public void setImageGroupId(Long imageGroupId) {
        this.imageGroupId = imageGroupId;
    }

    public String getImageGroupName() {
        return imageGroupName;
    }

    public void setImageGroupName(String imageGroupName) {
        this.imageGroupName = imageGroupName;
    }

    public Integer getImageType() {
        return imageType;
    }

    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public List<String> getBrandName() {
        return brandName;
    }

    public void setBrandName(List<String> brandName) {
        this.brandName = brandName;
    }

    public List<String> getProductType() {
        return productType;
    }

    public void setProductType(List<String> productType) {
        this.productType = productType;
    }

    public List<String> getSizeType() {
        return sizeType;
    }

    public void setSizeType(List<String> sizeType) {
        this.sizeType = sizeType;
    }

    public List<CmsBtImageGroupModel_Image> getImage() {
        return image;
    }

    public void setImage(List<CmsBtImageGroupModel_Image> image) {
        this.image = image;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}