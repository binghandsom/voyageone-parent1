package com.voyageone.web2.cms.bean.channel;

import java.util.List;

/**
 * Created by jonas on 9/12/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
public class CmsBlackBrandParamBean {
    private String channelId;

    private int brandType;

    private List<Integer> cartIdList;

    private Boolean status;

    private String brand;

    private int pageNumber;

    private int pageSize;

    private List<CmsBlackBrandViewBean> brandList;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getBrandType() {
        return brandType;
    }

    public void setBrandType(int brandType) {
        this.brandType = brandType;
    }

    public List<Integer> getCartIdList() {
        return cartIdList;
    }

    public void setCartIdList(List<Integer> cartIdList) {
        this.cartIdList = cartIdList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<CmsBlackBrandViewBean> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<CmsBlackBrandViewBean> brandList) {
        this.brandList = brandList;
    }
}
