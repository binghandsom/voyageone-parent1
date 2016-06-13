package com.voyageone.web2.cms.bean.search.index;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/15
 */
public class CmsSearchInfoBean2 {

    private Integer groupPageNum = 0;
    private Integer groupPageSize = 0;
    private Integer productPageNum = 0;
    private Integer productPageSize = 0;

    // ** 共通搜索条件 **
    private String mCatId;
    private String mCatPath;
    private String fCatId;
    private String fCatPath;

    private String categoryStatus;
    // 翻译状态查询用标志位
    private String transStsFlg = null;
    private String taxNoStatus;

    private String createTimeStart;
    private String createTimeTo;

    private String brand;
    private String[] codeList;
    private String[] freeTags;
    private int freeTagType = 0;

    // ** 平台搜索条件 **
    private Integer platformCart;
    private String[] productStatus;
    private String[] platformStatus;

    private String publishTimeStart;
    private String publishTimeTo;

    private String priceType;
    private BigDecimal priceStart;
    private BigDecimal priceEnd;

    private String pCatId;
    private String pCatPath;

    private String[] promotionTags;
    private int promotionTagType = 0;
    // 店铺内分类的查询
    private List<String>  cidValue;
    // 价格变动查询用标志位
    private int priceChgFlg = 0;
    private String propertyStatus;
    private int hasErrorFlg = 0;

    private String salesSortType = null;
    private String salesType = null;
    private String salesStart = null;
    private String salesEnd = null;

    // ** 自定义搜索条件 **
    private String sortOneName;
    private String sortOneType;

    private String sortTwoName;
    private String sortTwoType;

    private String sortThreeName;
    private String sortThreeType;

    // 自定义查询条件
    private List<Map<String, Object>> custAttrMap;

    // 文件下载类型
    private int fileType = 0;

    // ** 其它未定
    // 价格比较查询用标志位
    private int priceDiffFlg = 0;
    // MINI MALL 店铺时查询原始CHANNEL
    private String orgChaId = null;

    private String compareType;
    private Integer inventory;

    public List<String> getCidValue() {
        return cidValue;
    }

    public void setCidValue(List<String> cidValue) {
        this.cidValue = cidValue;
    }

    public String getOrgChaId() {
        return orgChaId;
    }

    public void setOrgChaId(String orgChaId) {
        this.orgChaId = orgChaId;
    }

    public String getTransStsFlg() {
        return transStsFlg;
    }

    public void setTransStsFlg(String transStsFlg) {
        this.transStsFlg = transStsFlg;
    }

    public int getPriceChgFlg() {
        return priceChgFlg;
    }

    public void setPriceChgFlg(int priceChgFlg) {
        this.priceChgFlg = priceChgFlg;
    }

    public int getPriceDiffFlg() {
        return priceDiffFlg;
    }

    public void setPriceDiffFlg(int priceDiffFlg) {
        this.priceDiffFlg = priceDiffFlg;
    }

    public List<Map<String, Object>> getCustAttrMap() {
        return custAttrMap;
    }

    public void setCustAttrMap(List<Map<String, Object>> custAttrMap) {
        this.custAttrMap = custAttrMap;
    }

    public String[] getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String[] productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getPlatformCart() {
        return platformCart;
    }

    public void setPlatformCart(Integer platformCart) {
        this.platformCart = platformCart;
    }

    public String[] getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String[] platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(BigDecimal priceStart) {
        this.priceStart = priceStart;
    }

    public BigDecimal getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(BigDecimal priceEnd) {
        this.priceEnd = priceEnd;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(String createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public String getPublishTimeStart() {
        return publishTimeStart;
    }

    public void setPublishTimeStart(String publishTimeStart) {
        this.publishTimeStart = publishTimeStart;
    }

    public String getPublishTimeTo() {
        return publishTimeTo;
    }

    public void setPublishTimeTo(String publishTimeTo) {
        this.publishTimeTo = publishTimeTo;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String[] getCodeList() {
        return codeList;
    }

    public void setCodeList(String[] codeList) {
        this.codeList = codeList;
    }

    public String getSortOneName() {
        return sortOneName;
    }

    public void setSortOneName(String sortOneName) {
        this.sortOneName = sortOneName;
    }

    public String getSortOneType() {
        return sortOneType;
    }

    public void setSortOneType(String sortOneType) {
        this.sortOneType = sortOneType;
    }

    public String getSortTwoName() {
        return sortTwoName;
    }

    public void setSortTwoName(String sortTwoName) {
        this.sortTwoName = sortTwoName;
    }

    public String getSortTwoType() {
        return sortTwoType;
    }

    public void setSortTwoType(String sortTwoType) {
        this.sortTwoType = sortTwoType;
    }

    public String getSortThreeName() {
        return sortThreeName;
    }

    public void setSortThreeName(String sortThreeName) {
        this.sortThreeName = sortThreeName;
    }

    public String getSortThreeType() {
        return sortThreeType;
    }

    public void setSortThreeType(String sortThreeType) {
        this.sortThreeType = sortThreeType;
    }

    public Integer getGroupPageNum() {
        return groupPageNum;
    }

    public void setGroupPageNum(Integer groupPageNum) {
        this.groupPageNum = groupPageNum != null && groupPageNum > 0 ? groupPageNum : 1;
    }

    public Integer getGroupPageSize() {
        return groupPageSize;
    }

    public void setGroupPageSize(Integer groupPageSize) {
        this.groupPageSize = groupPageSize != null && groupPageSize > 0 ? groupPageSize : 1;
    }

    public Integer getProductPageNum() {
        return productPageNum;
    }

    public void setProductPageNum(Integer productPageNum) {
        this.productPageNum = productPageNum != null && productPageNum > 0 ? productPageNum : 1;
    }

    public Integer getProductPageSize() {
        return productPageSize;
    }

    public void setProductPageSize(Integer productPageSize) {
        this.productPageSize = productPageSize != null && productPageSize > 0 ? productPageSize : 1;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public String getTaxNoStatus() {
        return taxNoStatus;
    }

    public void setTaxNoStatus(String taxNoStatus) {
        this.taxNoStatus = taxNoStatus;
    }

    public String[] getFreeTags() {
        return freeTags;
    }

    public void setFreeTags(String[] freeTags) {
        this.freeTags = freeTags;
    }

    public int getFreeTagType() {
        return freeTagType;
    }

    public void setFreeTagType(int freeTagType) {
        this.freeTagType = freeTagType;
    }

    public String[] getPromotionTags() {
        return promotionTags;
    }

    public void setPromotionTags(String[] promotionTags) {
        this.promotionTags = promotionTags;
    }

    public int getPromotionTagType() {
        return promotionTagType;
    }

    public void setPromotionTagType(int promotionTagType) {
        this.promotionTagType = promotionTagType;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public int getHasErrorFlg() {
        return hasErrorFlg;
    }

    public void setHasErrorFlg(int hasErrorFlg) {
        this.hasErrorFlg = hasErrorFlg;
    }

    public String getSalesSortType() {
        return salesSortType;
    }

    public void setSalesSortType(String salesSortType) {
        this.salesSortType = salesSortType;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public String getMCatId() {
        return mCatId;
    }

    public void setMCatId(String mCatId) {
        this.mCatId = mCatId;
    }

    public String getMCatPath() {
        return mCatPath;
    }

    public void setMCatPath(String mCatPath) {
        this.mCatPath = mCatPath;
    }

    public String getFCatId() {
        return fCatId;
    }

    public void setFCatId(String fCatId) {
        this.fCatId = fCatId;
    }

    public String getFCatPath() {
        return fCatPath;
    }

    public void setFCatPath(String fCatPath) {
        this.fCatPath = fCatPath;
    }

    public String getPCatId() {
        return pCatId;
    }

    public void setPCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public String getPCatPath() {
        return pCatPath;
    }

    public void setPCatPath(String pCatPath) {
        this.pCatPath = pCatPath;
    }

    public String getSalesStart() {
        return salesStart;
    }

    public void setSalesStart(String salesStart) {
        this.salesStart = salesStart;
    }

    public String getSalesEnd() {
        return salesEnd;
    }

    public void setSalesEnd(String salesEnd) {
        this.salesEnd = salesEnd;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
