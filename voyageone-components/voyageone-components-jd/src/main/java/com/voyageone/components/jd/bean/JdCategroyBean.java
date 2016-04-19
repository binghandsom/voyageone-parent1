package com.voyageone.components.jd.bean;

/**
 * 京东类目对象结构
 *
 * Created on 2016/04/08
 *
 * @author desmond
 * @version 2.0.0
 * @since 2.0.0
 */
public class JdCategroyBean {
    private String channelId;
    private Integer cartId;
    private Integer platformId; // 第三方平台Id
    private String platformCid;
    private String parentCid;
    private Integer isParent;
    private String cidName;
    private String cidPath;
    private Integer sortOrder;

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

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPlatformCid() {
        return platformCid;
    }

    public void setPlatformCid(String platformCid) {
        this.platformCid = platformCid;
    }

    public String getParentCid() {
        return parentCid;
    }

    public void setParentCid(String parentCid) {
        this.parentCid = parentCid;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public String getCidName() {
        return cidName;
    }

    public void setCidName(String cidName) {
        this.cidName = cidName;
    }

    public String getCidPath() {
        return cidPath;
    }

    public void setCidPath(String cidPath) {
        this.cidPath = cidPath;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
