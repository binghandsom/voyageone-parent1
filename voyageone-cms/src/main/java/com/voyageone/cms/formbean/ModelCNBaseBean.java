package com.voyageone.cms.formbean;

import java.math.BigDecimal;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;

public class ModelCNBaseBean extends ModelBaseBean{
	
	@Table(name="cms_bt_cn_model")
	private String cnName;

	@Table(name="cms_bt_cn_model")
	private String urlKey;
	
	@Table(name="cms_bt_cn_model")
	private String cnAbstract;

	@Table(name="cms_bt_cn_model")
	private String cnShortDescription;

	@Table(name="cms_bt_cn_model")
	private Integer cnSizeTypeId;

	@Table(name="cms_bt_cn_model")
	private String displayImages;

	@Table(name="cms_bt_cn_model")
	private String referenceMsrp;

	@Table(name="cms_bt_cn_model")
	private String referencePrice;

	@Table(name="cms_bt_cn_model")
	private String cnLongDescription;
	
	@Table(name="cms_bt_cn_model")
	private String model;
	
	@Table(name="cms_bt_cn_model_extend")
	@Extends
	private String cnSeoTitle;

	@Table(name="cms_bt_cn_model_extend")
	@Extends
	private String cnSeoDescription;
	
	@Table(name="cms_bt_cn_model_extend")
	@Extends
	private String cnSeoKeywords;
	
	@Table(name="cms_bt_cn_model_extend")
	@Extends
	private Integer hsCodeId;
	
	@Table(name="cms_bt_cn_model_extend")
	@Extends
	private Integer hsCodePuId;
	
	@Table(name="cms_bt_cn_model_extend")
	@Extends
	private Integer sizeChartId;
	
	private String sizeChartName;
	
	private String sizeChartUrl;
	
	@Table(name="cms_bt_cn_model_extend")
	@Extends	
	private String mainCategoryId;
	
	private String mainCategoryName;
	
	private String mainParentCategoryId;
	
	private int mainPaerntCategoryTypeId;
	
	
    
	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName == null ? null : cnName.trim();
	}

	public String getCnAbstract() {
		return cnAbstract;
	}

	public void setCnAbstract(String cnAbstract) {
		this.cnAbstract = cnAbstract == null ? null : cnAbstract.trim();
	}

	public String getCnShortDescription() {
		return cnShortDescription;
	}

	public void setCnShortDescription(String cnShortDescription) {
		this.cnShortDescription = cnShortDescription == null ? null : cnShortDescription.trim();
	}

	public Integer getCnSizeTypeId() {
		return cnSizeTypeId;
	}

	public void setCnSizeTypeId(Integer cnSizeTypeId) {
		this.cnSizeTypeId = cnSizeTypeId;
	}

	public String getDisplayImages() {
		return displayImages;
	}

	public void setDisplayImages(String displayImages) {
		this.displayImages = displayImages == null ? null : displayImages.trim();
	}

	public String getReferenceMsrp() {
		return referenceMsrp;
	}

	public void setReferenceMsrp(String referenceMsrp) {
		this.referenceMsrp = referenceMsrp;
	}

	public String getReferencePrice() {
		return referencePrice;
	}

	public void setReferencePrice(String referencePrice) {
		this.referencePrice = referencePrice;
	}

	public String getCnLongDescription() {
		return cnLongDescription;
	}

	public void setCnLongDescription(String cnLongDescription) {
		this.cnLongDescription = cnLongDescription == null ? null : cnLongDescription.trim();
	}

	public String getCnSeoTitle() {
		return cnSeoTitle;
	}

	public void setCnSeoTitle(String cnSeoTitle) {
		this.cnSeoTitle = cnSeoTitle;
	}

	public String getCnSeoDescription() {
		return cnSeoDescription;
	}

	public void setCnSeoDescription(String cnSeoDescription) {
		this.cnSeoDescription = cnSeoDescription;
	}

	public String getCnSeoKeywords() {
		return cnSeoKeywords;
	}

	public void setCnSeoKeywords(String cnSeoKeywords) {
		this.cnSeoKeywords = cnSeoKeywords;
	}

	public Integer getHsCodeId() {
		return hsCodeId;
	}

	public void setHsCodeId(Integer hsCodeId) {
		this.hsCodeId = hsCodeId;
	}

	public Integer getHsCodePuId() {
		return hsCodePuId;
	}

	public void setHsCodePuId(Integer hsCodePuId) {
		this.hsCodePuId = hsCodePuId;
	}

	public Integer getSizeChartId() {
		return sizeChartId;
	}

	public void setSizeChartId(Integer sizeChartId) {
		this.sizeChartId = sizeChartId;
	}
	
	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	public String getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public String getMainCategoryName() {
		return mainCategoryName;
	}

	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName;
	}

	public String getMainParentCategoryId() {
		return mainParentCategoryId;
	}

	public void setMainParentCategoryId(String mainParentCategoryId) {
		this.mainParentCategoryId = mainParentCategoryId;
	}

	public int getMainPaerntCategoryTypeId() {
		return mainPaerntCategoryTypeId;
	}

	public void setMainPaerntCategoryTypeId(int mainPaerntCategoryTypeId) {
		this.mainPaerntCategoryTypeId = mainPaerntCategoryTypeId;
	}

	public String getSizeChartName() {
		return sizeChartName;
	}

	public void setSizeChartName(String sizeChartName) {
		this.sizeChartName = sizeChartName;
	}

	public String getSizeChartUrl() {
		return sizeChartUrl;
	}

	public void setSizeChartUrl(String sizeChartUrl) {
		this.sizeChartUrl = sizeChartUrl;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

}
