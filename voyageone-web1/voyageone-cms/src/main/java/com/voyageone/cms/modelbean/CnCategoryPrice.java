package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

import com.voyageone.cms.annotation.Extends;

public class CnCategoryPrice  {
	
    private int categoryId;
	
	private String channelId;
	@Extends
    private String basePriceId;
	@Extends
    private String pricingFactor;
	@Extends
    private String exchangeRate;
	@Extends
    private String overHeard1;
	@Extends
    private String overHeard2;
	@Extends
    private String overHeard3;
	@Extends
    private String overHeard4;
	@Extends
    private String overHeard5;
	@Extends
    private String shippingCompensation;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	
	/**
	 * @return the overHeard1
	 */
	public String getOverHeard1() {
		return overHeard1;
	}

	/**
	 * @param overHeard1 the overHeard1 to set
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
	 * @param overHeard2 the overHeard2 to set
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
	 * @param overHeard3 the overHeard3 to set
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
	 * @param overHeard4 the overHeard4 to set
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
	 * @param overHeard5 the overHeard5 to set
	 */
	public void setOverHeard5(String overHeard5) {
		this.overHeard5 = overHeard5;
	}

	

	/**
	 * @return the basePriceId
	 */
	public String getBasePriceId() {
		return basePriceId;
	}

	/**
	 * @param basePriceId the basePriceId to set
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
	 * @param pricingFactor the pricingFactor to set
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
	 * @param exchangeRate the exchangeRate to set
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
	 * @param shippingCompensation the shippingCompensation to set
	 */
	public void setShippingCompensation(String shippingCompensation) {
		this.shippingCompensation = shippingCompensation;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the creater
	 */
	public String getCreater() {
		return creater;
	}

	/**
	 * @param creater the creater to set
	 */
	public void setCreater(String creater) {
		this.creater = creater;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

    
    
}