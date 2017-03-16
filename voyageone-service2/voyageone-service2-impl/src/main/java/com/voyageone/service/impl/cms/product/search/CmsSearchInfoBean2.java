package com.voyageone.service.impl.cms.product.search;

import com.voyageone.common.util.JsonUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/15
 */
public class CmsSearchInfoBean2 implements Serializable {

    private Integer groupPageNum = 0;
    private Integer groupPageSize = 0;
    private Integer productPageNum = 0;
    private Integer productPageSize = 0;

    // ** 共通搜索条件 **
    private String mCatPath;
    private List<String> fCatPathList;

    private String mCatStatus;
    // 翻译状态查询用标志位
    private String transStsFlg = null;
    private String taxNoStatus;
    private Integer inventory;

    private String createTimeStart;
    private String createTimeTo;

    private List<String> brandList;
    private int brandSelType = 0;
    private List<String> freeTags;
    private int freeTagType = 0;
    private String lockFlg;

    private String[] codeList;
    private String fuzzyStr;
    // MINI MALL 店铺时查询原始CHANNEL
    private int supplierType = 0;
    private List<String> supplierList = null;
    
    private String productSelType;
    private String sizeSelType;
    private List<String> productTypeList;
    private List<String> sizeTypeList;

    // ** 平台搜索条件 **
    private Integer cartId = 0;
    private List<String> productStatus;
    private List<String> platformStatus;
    private List<String> pRealStatus;

    private String publishTimeStart;
    private String publishTimeTo;

    private String priceType;
    private Double priceStart;
    private Double priceEnd;

    private List<String> pCatPathList;
    private int pCatStatus = 0;

    private String[] promotionTags;
    private int promotionTagType = 0;
    // 店铺内分类的查询
    private List<String> cidValue;
    private int shopCatStatus = 0;

    // 价格变动查询用标志位
    private String priceChgFlg = null;
    // 价格比较查询用标志位
    private String priceDiffFlg = null;
    private String propertyStatus;
    private int hasErrorFlg = 0;

    private String salesSortType = null;
    private String salesType = null;
    private Double salesStart = null;
    private Double salesEnd = null;

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
    private String compareType;
    // NumIID
    private String numIIds;
    // 自定义条件组合类型
    private String custGroupType;

    private Boolean isNewSku;

    private Boolean noSale;

    public Boolean getNewSku() {
        return isNewSku;
    }

    public void setNewSku(Boolean newSku) {
        isNewSku = newSku;
    }

    public Boolean getNoSale() {
        return noSale;
    }

    public void setNoSale(Boolean noSale) {
        this.noSale = noSale;
    }

    public List<String> getCidValue() {
        return cidValue;
    }

    public void setCidValue(List<String> cidValue) {
        this.cidValue = cidValue;
    }

    public List<String> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<String> supplierList) {
        this.supplierList = supplierList;
    }

    public String getTransStsFlg() {
        return transStsFlg;
    }

    public void setTransStsFlg(String transStsFlg) {
        this.transStsFlg = transStsFlg;
    }

    public String getPriceChgFlg() {
        return priceChgFlg;
    }

    public void setPriceChgFlg(String priceChgFlg) {
        this.priceChgFlg = priceChgFlg;
    }

    public String getPriceDiffFlg() {
        return priceDiffFlg;
    }

    public void setPriceDiffFlg(String priceDiffFlg) {
        this.priceDiffFlg = priceDiffFlg;
    }

    public List<Map<String, Object>> getCustAttrMap() {
        return custAttrMap;
    }

    public void setCustAttrMap(List<Map<String, Object>> custAttrMap) {
        this.custAttrMap = custAttrMap;
    }

    public List<String> getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(List<String> productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<String> getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(List<String> platformStatus) {
        this.platformStatus = platformStatus;
    }

    public List<String> getpRealStatus() {
        return pRealStatus;
    }

    public void setpRealStatus(List<String> pRealStatus) {
        this.pRealStatus = pRealStatus;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Double priceStart) {
        this.priceStart = priceStart;
    }

    public Double getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Double priceEnd) {
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

    public List<String> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<String> brandList) {
        this.brandList = brandList;
    }

    public int getBrandSelType() {
        return brandSelType;
    }

    public void setBrandSelType(int brandSelType) {
        this.brandSelType = brandSelType;
    }

    public String[] getCodeList() {
        return codeList;
    }

    public void setCodeList(String[] codeList) {
        this.codeList = codeList;
    }

    public String getFuzzyStr() {
        return fuzzyStr;
    }

    public void setFuzzyStr(String fuzzyStr) {
        this.fuzzyStr = fuzzyStr;
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

    public String getmCatStatus() {
        return mCatStatus;
    }

    public void setmCatStatus(String categoryStatus) {
        this.mCatStatus = categoryStatus;
    }

    public String getTaxNoStatus() {
        return taxNoStatus;
    }

    public void setTaxNoStatus(String taxNoStatus) {
        this.taxNoStatus = taxNoStatus;
    }

    public List<String> getFreeTags() {
        return freeTags;
    }

    public void setFreeTags(List<String> freeTags) {
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

    public Double getSalesStart() {
        return salesStart;
    }

    public void setSalesStart(Double salesStart) {
        this.salesStart = salesStart;
    }

    public Double getSalesEnd() {
        return salesEnd;
    }

    public void setSalesEnd(Double salesEnd) {
        this.salesEnd = salesEnd;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getShopCatStatus() {
        return shopCatStatus;
    }

    public void setShopCatStatus(int shopCatStatus) {
        this.shopCatStatus = shopCatStatus;
    }

    public int getPCatStatus() {
        return pCatStatus;
    }

    public void setPCatStatus(int pCatStatus) {
        this.pCatStatus = pCatStatus;
    }

    public String getmCatPath() {
        return mCatPath;
    }

    public void setmCatPath(String mCatPath) {
        this.mCatPath = mCatPath;
    }

    public List<String> getfCatPathList() {
        return fCatPathList;
    }

    public void setfCatPathList(List<String> fCatPathList) {
        this.fCatPathList = fCatPathList;
    }

    public List<String> getpCatPathList() {
        return pCatPathList;
    }

    public void setpCatPathList(List<String> pCatPathList) {
        this.pCatPathList = pCatPathList;
    }

    public int getpCatStatus() {
        return pCatStatus;
    }

    public void setpCatStatus(int pCatStatus) {
        this.pCatStatus = pCatStatus;
    }

    public String getLockFlg() {
        return lockFlg;
    }

    public void setLockFlg(String lockFlg) {
        this.lockFlg = lockFlg;
    }

	public String getProductSelType() {
		return productSelType;
	}

	public void setProductSelType(String productSelType) {
		this.productSelType = productSelType;
	}

	public String getSizeSelType() {
		return sizeSelType;
	}

	public void setSizeSelType(String sizeSelType) {
		this.sizeSelType = sizeSelType;
	}
	
	public List<String> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<String> productTypeList) {
		this.productTypeList = productTypeList;
	}

	public List<String> getSizeTypeList() {
		return sizeTypeList;
	}

	public void setSizeTypeList(List<String> sizeTypeList) {
		this.sizeTypeList = sizeTypeList;
	}

	@Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }

    public int getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(int supplierType) {
        this.supplierType = supplierType;
    }

	public String getNumIIds() {
		return numIIds;
	}

	public void setNumIIds(String numIIds) {
		this.numIIds = numIIds;
	}

	public String getCustGroupType() {
		return custGroupType;
	}

	public void setCustGroupType(String custGroupType) {
		this.custGroupType = custGroupType;
	}

    public Boolean getIsNewSku() {
        return isNewSku;
    }

    public void setIsNewSku(Boolean isNewSku) {
        this.isNewSku = isNewSku;
    }
}
