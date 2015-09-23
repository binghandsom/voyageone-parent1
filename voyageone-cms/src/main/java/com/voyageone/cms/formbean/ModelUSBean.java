package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;
import com.voyageone.common.util.StringUtils;

public class ModelUSBean extends ModelBaseBean{

	@Table(name="cms_bt_model")
	private Integer productTypeId;

	@Table(name="cms_bt_model")
	private Integer brandId;

	@Table(name="cms_bt_model")
	private String model;

	@Table(name="cms_bt_model")
	private String name;

	@Table(name="cms_bt_model")
	private String usAbstract;

	@Table(name="cms_bt_model")
	private String shortDescription;

	@Table(name="cms_bt_model")
	private Integer sizeTypeId;

	@Table(name="cms_bt_model")
	private Boolean isUnisex;

	@Table(name="cms_bt_model")
	private String sizeOffset;

	@Table(name="cms_bt_model")
	private String weight;

	@Table(name="cms_bt_model")
	private Boolean isTaxable;

	@Table(name="cms_bt_model")
	private String accessory;

	@Table(name="cms_bt_model")
	private String promotionTag;

	@Table(name="cms_bt_model")
	private String blogUrl;

	@Table(name="cms_bt_model")
	private String urlKey;

	@Table(name="cms_bt_model")
	private Boolean isEffective;

	@Table(name="cms_bt_model")
	private String longDescription;

	@Table(name="cms_bt_model_extend")
	@Extends
	private String seoTitle;

	@Table(name="cms_bt_model_extend")
	@Extends
	private String seoDescription;

	@Table(name="cms_bt_model_extend")
	@Extends
	private String seoKeywords;
	
	@Table(name="cms_bt_model_extend")
	@Extends
	private String mainCategoryId;

	@Table(name="cms_bt_amazon_model_extend")
	@Extends
	private String amazonBrowseCategoryId;

	@Table(name="cms_bt_google_model_extend")
	@Extends
	private String googleCategoryId;

	@Table(name="cms_bt_pricegrabber_model_extend")
	@Extends
	private String priceGrabberCategoryId;
	
	private String productTypeName;
	
	private String brand;
    
	public Integer getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model == null ? null : model.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription == null ? null : shortDescription.trim();
	}

	public Integer getSizeTypeId() {
		return sizeTypeId;
	}

	public void setSizeTypeId(Integer sizeTypeId) {
		this.sizeTypeId = sizeTypeId;
	}

	public String getSizeOffset() {
		if(StringUtils.isEmpty(sizeOffset)){
			sizeOffset = "0";
		}
		return sizeOffset;
	}

	public void setSizeOffset(String sizeOffset) {
		this.sizeOffset = sizeOffset;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory == null ? null : accessory.trim();
	}

	public String getPromotionTag() {
		return promotionTag;
	}

	public void setPromotionTag(String promotionTag) {
		this.promotionTag = promotionTag == null ? null : promotionTag.trim();
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl == null ? null : blogUrl.trim();
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey == null ? null : urlKey.trim();
	}


	public Boolean getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(Boolean isEffective) {
		this.isEffective = isEffective;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription == null ? null : longDescription.trim();
	}

	public String getUsAbstract() {
		return usAbstract;
	}

	public void setUsAbstract(String usAbstract) {
		this.usAbstract = usAbstract;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getGoogleCategoryId() {
		return googleCategoryId;
	}

	public void setGoogleCategoryId(String googleCategoryId) {
		this.googleCategoryId = googleCategoryId;
	}

	/**
	 * @return the amazonBrowseCategoryId
	 */
	public String getAmazonBrowseCategoryId() {
		return amazonBrowseCategoryId;
	}

	/**
	 * @param amazonBrowseCategoryId the amazonBrowseCategoryId to set
	 */
	public void setAmazonBrowseCategoryId(String amazonBrowseCategoryId) {
		this.amazonBrowseCategoryId = amazonBrowseCategoryId;
	}

	/**
	 * @return the priceGrabberCategoryId
	 */
	public String getPriceGrabberCategoryId() {
		return priceGrabberCategoryId;
	}

	/**
	 * @param priceGrabberCategoryId the priceGrabberCategoryId to set
	 */
	public void setPriceGrabberCategoryId(String priceGrabberCategoryId) {
		this.priceGrabberCategoryId = priceGrabberCategoryId;
	}

	public Boolean getIsUnisex() {
		return isUnisex;
	}

	public void setIsUnisex(Boolean isUnisex) {
		this.isUnisex = isUnisex;
	}

	public Boolean getIsTaxable() {
		return isTaxable;
	}

	public void setIsTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public String getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	/**
	 * @return the productTypeName
	 */
	public String getProductTypeName() {
		return productTypeName;
	}

	/**
	 * @param productTypeName the productTypeName to set
	 */
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
}
