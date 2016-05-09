package com.voyageone.service.model.cms.mongo;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import java.util.List;
public class CmsBtImageTemplateModel extends BaseMongoModel {
    public Integer getActive() {
        return active;
    }
    public void setActive(Integer active) {
        this.active = active;
    }
    private  String channelId;
    private  Integer cartId;
    private Long imageTemplateId;
    private   String imageTemplateName;
    private  List<String> brandName;//品牌                多选
    private  List<String> productType;//产品类型     多选
    private   List<String> sizeType;//尺码类型             多选
    private   Integer viewType;//     0:PC端  1：APP端
    private  Integer  imageTemplateType;//模板类型
    private  String imageTemplateContent;//模板内容 content
    private  String  templateModified;//模板内容改变时间戳
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

    public Long getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(Long imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
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

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public Integer getImageTemplateType() {
        return imageTemplateType;
    }

    public void setImageTemplateType(Integer imageTemplateType) {
        this.imageTemplateType = imageTemplateType;
    }

    public String getImageTemplateContent() {
        return imageTemplateContent;
    }

    public void setImageTemplateContent(String imageTemplateContent) {
        this.imageTemplateContent = imageTemplateContent;
    }

    public String getTemplateModified() {
        return templateModified;
    }

    public void setTemplateModified(String templateModified) {
        this.templateModified = templateModified;
    }
}

