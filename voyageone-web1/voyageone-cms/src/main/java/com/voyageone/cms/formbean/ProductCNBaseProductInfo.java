package com.voyageone.cms.formbean;

import java.math.BigDecimal;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;
public class ProductCNBaseProductInfo extends ProductBaseBean {
	private String code;								
	private String cnName;									
	private String madeInCountryName;									
	private String materialFabric1Name	;								
	private String materialFabric2Name	;								
	private String materialFabric3Name;									
	private String cnColor;									
	private String urlKey;	
	private Boolean cnIsApprovedDescription;	
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private String cnAbstract;	
	
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private String cnShortDescription;	
	
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private String cnLongDescription;	
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private String displayImages;	
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private BigDecimal referenceMsrp;
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private BigDecimal referencePrice;
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private Integer hsCodeId	;		
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private Integer hsCodePuId;	
	@Extends
	@Table(name="cms_bt_cn_product_extend")
	private String mainCategoryId;								
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getMadeInCountryName() {
		return madeInCountryName;
	}
	public void setMadeInCountryName(String madeInCountryName) {
		this.madeInCountryName = madeInCountryName;
	}
	public String getMaterialFabric1Name() {
		return materialFabric1Name;
	}
	public void setMaterialFabric1Name(String materialFabric1Name) {
		this.materialFabric1Name = materialFabric1Name;
	}
	public String getMaterialFabric2Name() {
		return materialFabric2Name;
	}
	public void setMaterialFabric2Name(String materialFabric2Name) {
		this.materialFabric2Name = materialFabric2Name;
	}
	public String getMaterialFabric3Name() {
		return materialFabric3Name;
	}
	public void setMaterialFabric3Name(String materialFabric3Name) {
		this.materialFabric3Name = materialFabric3Name;
	}
	public String getCnColor() {
		return cnColor;
	}
	public void setCnColor(String cnColor) {
		this.cnColor = cnColor;
	}
	public String getUrlKey() {
		return urlKey;
	}
	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}
	public String getCnAbstract() {
		return cnAbstract;
	}
	public void setCnAbstract(String cnAbstract) {
		this.cnAbstract = cnAbstract;
	}
	public String getCnShortDescription() {
		return cnShortDescription;
	}
	public void setCnShortDescription(String cnShortDescription) {
		this.cnShortDescription = cnShortDescription;
	}
	public String getCnLongDescription() {
		return cnLongDescription;
	}
	public void setCnLongDescription(String cnLongDescription) {
		this.cnLongDescription = cnLongDescription;
	}
	public String getDisplayImages() {
		return displayImages;
	}
	public void setDisplayImages(String displayImages) {
		this.displayImages = displayImages;
	}
	
	public BigDecimal getReferenceMsrp() {
		return referenceMsrp;
	}
	public void setReferenceMsrp(BigDecimal referenceMsrp) {
		this.referenceMsrp = referenceMsrp;
	}
	public BigDecimal getReferencePrice() {
		return referencePrice;
	}
	public void setReferencePrice(BigDecimal referencePrice) {
		this.referencePrice = referencePrice;
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
	public String getMainCategoryId() {
		return mainCategoryId;
	}
	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}
	/**
	 * @return the cnIsApprovedDescription
	 */
	public Boolean getCnIsApprovedDescription() {
		return cnIsApprovedDescription;
	}
	/**
	 * @param cnIsApprovedDescription the cnIsApprovedDescription to set
	 */
	public void setCnIsApprovedDescription(Boolean cnIsApprovedDescription) {
		this.cnIsApprovedDescription = cnIsApprovedDescription;
	}
}
