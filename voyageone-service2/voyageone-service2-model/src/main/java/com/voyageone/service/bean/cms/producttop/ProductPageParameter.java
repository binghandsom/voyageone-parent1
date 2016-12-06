package com.voyageone.service.bean.cms.producttop;

import java.util.List;

/**
 * Created by dell on 2016/11/28.
 */
public class ProductPageParameter {
    int cartId;//平台id
    List<String> brandList;//品牌名称
    boolean isInclude;//  brand是否包含
    String compareType;
    Integer quantity;//库存数量
    String sellerCatId;//店铺内分类
    String sellerCatPath;
    List<String> codeList;//   款号/Code/SKU   换行分隔
    String sortColumnName;// 排序列名称
    int sortType;//排序类型   1：升序         -1：降序
    int pageIndex;//当前页
    int pageSize;//当前页行数



    public boolean isInclude() {
        return isInclude;
    }

    public void setInclude(boolean include) {
        isInclude = include;
    }

    public String getSellerCatId() {
        return sellerCatId;
    }

    public void setSellerCatId(String sellerCatId) {
        this.sellerCatId = sellerCatId;
    }

    public String getSellerCatPath() {
        return sellerCatPath;
    }

    public void setSellerCatName(String sellerCatPath) {
        this.sellerCatPath = sellerCatPath;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    public List<String> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<String> brandList) {
        this.brandList = brandList;
    }

    public boolean getIsInclude() {
        return isInclude;
    }

    public void setIsInclude(boolean include) {
        isInclude = include;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public String getSortColumnName() {
        return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
