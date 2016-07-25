package com.voyageone.web2.cms.bean;

import java.io.Serializable;

/**
 * 品牌的关系映射的属性集（平台品牌设置一览）
 * @author Wangtd
 */
public class BrandMappingBean implements Serializable {

	private static final long serialVersionUID = 6382536229319502841L;
	
	//------------------ 数据存储用字段 --------------------
	
	/** 渠道ID */
	private String channelId;
	
	/** 店铺ID */
	private Integer cartId;
	
	/** 品牌ID */
	private String brandId;
	
	/** CMS品牌 */
	private String cmsBrand;
	
	/** 语言ID */
	private String langId;
	
	//------------------- 检索用字段 ----------------------
	
	/** 品牌匹配状态 */
	private Integer mappingState;
	
	/** 品牌名称 */
	private String brandName;
	
	//---------------- Getters & Setters --------------
	
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
	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	public String getCmsBrand() {
		return cmsBrand;
	}
	public void setCmsBrand(String cmsBrand) {
		this.cmsBrand = cmsBrand;
	}
	
	public String getLangId() {
		return langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
	}

	public Integer getMappingState() {
		return mappingState;
	}
	
	public void setMappingState(Integer mappingState) {
		this.mappingState = mappingState;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

}
