package com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/11/3.
 */
public class AddProductSaveParameter {
    int cartId;

    int isSelAll;

    List<String> codeList;

    List<Long> idList;

    List<TagTreeNode> listTagTreeNode;

    //价格类型    1 建议售价   2指导售价  3最终售价  4固定售价
    int priceTypeId;

    // 1小数点向上取整    2个位向下取整    3个位向上取整    4无特殊处理
    int roundType;

    // 1.商品内，SKU统一最高价    2.商品内，SKU统一最低价    3.商品内，SKU价格不统一
    int skuUpdType;
    // + -  *  =
    String optType;
    //price value
    double priceValue;

    Map<String,Object> searchInfo;

    public Map<String, Object> getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(Map<String, Object> searchInfo) {
        this.searchInfo = searchInfo;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getIsSelAll() {
        return isSelAll;
    }

    public void setIsSelAll(int isSelAll) {
        this.isSelAll = isSelAll;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public List<TagTreeNode> getListTagTreeNode() {
        return listTagTreeNode;
    }

    public void setListTagTreeNode(List<TagTreeNode> listTagTreeNode) {
        this.listTagTreeNode = listTagTreeNode;
    }

    public int getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(int priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public int getRoundType() {
        return roundType;
    }

    public void setRoundType(int roundType) {
        this.roundType = roundType;
    }

    public int getSkuUpdType() {
        return skuUpdType;
    }

    public void setSkuUpdType(int skuUpdType) {
        this.skuUpdType = skuUpdType;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }
}
