package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;

public class CategoryPriceSettingBean extends CategoryBaseBean  implements IPriceSetting{
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
    
    private String comment;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
  
	
}
