package com.voyageone.cms.modelbean;

import java.util.Date;

public class HistoryPriceSetting {
	private Integer id;

	private Integer categoryModelProductId;

	private String channelId;

	private String priceSettingType;

	private String basePriceId;

	private String pricingFactor;

	private String exchangeRate;

	private String overHeard1;

	private String overHeard2;

	private String overHeard3;

	private String overHeard4;

	private String overHeard5;

	private String shippingCompensation;

	private Date created;

	private String creater;

	private Date modified;

	private String modifier;

	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryModelProductId() {
		return categoryModelProductId;
	}

	public void setCategoryModelProductId(Integer categoryModelProductId) {
		this.categoryModelProductId = categoryModelProductId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId == null ? null : channelId.trim();
	}

	public String getPriceSettingType() {
		return priceSettingType;
	}

	public void setPriceSettingType(String priceSettingType) {
		this.priceSettingType = priceSettingType == null ? null : priceSettingType.trim();
	}

	/**
	 * @return the basePriceId
	 */
	public String getBasePriceId() {
		return basePriceId;
	}

	/**
	 * @param basePriceId
	 *            the basePriceId to set
	 */
	public void setBasePriceId(String basePriceId) {
		this.basePriceId = basePriceId;
	}

	/**
	 * @return the pricingFactor
	 */
	public String getPricingFactor() {
		return pricingFactor;
	}

	/**
	 * @param pricingFactor
	 *            the pricingFactor to set
	 */
	public void setPricingFactor(String pricingFactor) {
		this.pricingFactor = pricingFactor;
	}

	/**
	 * @return the exchangeRate
	 */
	public String getExchangeRate() {
		return exchangeRate;
	}

	/**
	 * @param exchangeRate
	 *            the exchangeRate to set
	 */
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	/**
	 * @return the shippingCompensation
	 */
	public String getShippingCompensation() {
		return shippingCompensation;
	}

	/**
	 * @param shippingCompensation
	 *            the shippingCompensation to set
	 */
	public void setShippingCompensation(String shippingCompensation) {
		this.shippingCompensation = shippingCompensation;
	}

	/**
	 * @return the overHeard1
	 */
	public String getOverHeard1() {
		return overHeard1;
	}

	/**
	 * @param overHeard1
	 *            the overHeard1 to set
	 */
	public void setOverHeard1(String overHeard1) {
		this.overHeard1 = overHeard1;
	}

	/**
	 * @return the overHeard2
	 */
	public String getOverHeard2() {
		return overHeard2;
	}

	/**
	 * @param overHeard2
	 *            the overHeard2 to set
	 */
	public void setOverHeard2(String overHeard2) {
		this.overHeard2 = overHeard2;
	}

	/**
	 * @return the overHeard3
	 */
	public String getOverHeard3() {
		return overHeard3;
	}

	/**
	 * @param overHeard3
	 *            the overHeard3 to set
	 */
	public void setOverHeard3(String overHeard3) {
		this.overHeard3 = overHeard3;
	}

	/**
	 * @return the overHeard4
	 */
	public String getOverHeard4() {
		return overHeard4;
	}

	/**
	 * @param overHeard4
	 *            the overHeard4 to set
	 */
	public void setOverHeard4(String overHeard4) {
		this.overHeard4 = overHeard4;
	}

	/**
	 * @return the overHeard5
	 */
	public String getOverHeard5() {
		return overHeard5;
	}

	/**
	 * @param overHeard5
	 *            the overHeard5 to set
	 */
	public void setOverHeard5(String overHeard5) {
		this.overHeard5 = overHeard5;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater == null ? null : creater.trim();
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier == null ? null : modifier.trim();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment == null ? null : comment.trim();
	}
}