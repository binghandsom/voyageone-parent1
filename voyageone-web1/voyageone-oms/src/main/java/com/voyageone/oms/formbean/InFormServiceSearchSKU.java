package com.voyageone.oms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

import java.util.List;

/**
 * 画面传入Sku检索条件bean
 * 
 * @author jerry
 *
 */
public class InFormServiceSearchSKU extends AjaxRequestBean {
	/**
	 * sku开始
	 */
	private String skuStartsWith;
	
	/**
	 * sku包含
	 */
	private String skuIncludes;

	/**
	 * name开始
	 */
	private String nameStartsWith;
	
	/**
	 * name包含
	 */
	private String nameIncludes;
	
	/**
	 * description开始
	 */
	private String descriptionStartsWith;
	
	/**
	 * description包含
	 */
	private String descriptionIncludes;
	
	private String channelId;

	private String cartId;

	/**
	 * sku 一览（C# service用）
	 */
	private String skuJson;

	/**
	 * sku 一览（java service用）
	 */
	private List<String> skuList;
	
	public String getSkuStartsWith() {
		return this.skuStartsWith;
	}

	public void setSkuStartsWith(String skuStartsWith) {
		this.skuStartsWith = skuStartsWith;
	}

	public String getSkuIncludes() {
		return this.skuIncludes;
	}

	public void setSkuIncludes(String skuIncludes) {
		this.skuIncludes = skuIncludes;
	}
	
	public String getNameStartsWith() {
		return this.nameStartsWith;
	}
	
	public void setNameStartsWith(String nameStartsWith) {
		this.nameStartsWith = nameStartsWith;
	}

	public String getNameIncludes() {
		return this.nameIncludes;
	}

	public void setNameIncludes(String nameIncludes) {
		this.nameIncludes = nameIncludes;
	}

	public String getDescriptionStartsWith() {
		return this.descriptionStartsWith;
	}

	public void setDescriptionStartsWith(String descriptionStartsWith) {
		this.descriptionStartsWith = descriptionStartsWith;
	}

	public String getDescriptionIncludes() {
		return this.descriptionIncludes;
	}

	public void setDescriptionIncludes(String descriptionIncludes) {
		this.descriptionIncludes = descriptionIncludes;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSkuJson() {
		return this.skuJson;
	}

	public void setSkuJson(String skuJson) {
		this.skuJson = skuJson;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
