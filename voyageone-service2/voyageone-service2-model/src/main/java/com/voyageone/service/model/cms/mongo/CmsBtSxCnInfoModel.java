package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class CmsBtSxCnInfoModel extends ChannelPartitionModel {
    private String orgChannelId; // 原始channelId
    private Long groupId;
    private Integer cartId;
    private List<String> catIds;
    private String code;
    private Long prodId; // cms产品id
    private String productXml;
    private String skuXml;
    private Integer publishFlg; // 0:等待上传，1:上传中，2:上传结束，4:上传终止

    public String getOrgChannelId() {
        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<String> getCatIds() {
        return catIds;
    }

    public void setCatIds(List<String> catIds) {
        this.catIds = catIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProductXml() {
        return productXml;
    }

    public void setProductXml(String productXml) {
        this.productXml = productXml;
    }

    public String getSkuXml() {
        return skuXml;
    }

    public void setSkuXml(String skuXml) {
        this.skuXml = skuXml;
    }

    public Integer getPublishFlg() {
        return publishFlg;
    }

    public void setPublishFlg(Integer publishFlg) {
        this.publishFlg = publishFlg;
    }
}