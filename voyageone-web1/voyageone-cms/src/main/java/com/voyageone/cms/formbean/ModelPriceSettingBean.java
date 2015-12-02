package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;
import com.voyageone.common.util.StringUtils;

public class ModelPriceSettingBean  extends ModelBaseBean  implements IPriceSetting{
	@Table(name="cms_bt_cn_model_price_setting")
	@Extends
	private String basePriceId;
	@Table(name="cms_bt_cn_model_price_setting")
	@Extends
	private String pricingFactor;
	@Table(name="cms_bt_cn_model_price_setting")
	@Extends
	private String exchangeRate;
	@Table(name="cms_bt_cn_model_price_setting")
	@Extends
	private String overHeard1;
	@Table(name="cms_bt_cn_model_price_setting")
	@Extends
	private String overHeard2;
	
	@Table(name="cms_bt_cn_model_price_setting")
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
	 * @return the overHear1
	 */
	public String getOverHeard1() {
		return overHeard1;
	}
	/**
	 * @param overHear1 the overHear1 to set
	 */
	public void setOverHeard1(String overHeard1) {
		this.overHeard1 = overHeard1;
	}
	/**
	 * @return the overHear2
	 */
	public String getOverHeard2() {
		return overHeard2;
	}
	/**
	 * @param overHear2 the overHear2 to set
	 */
	public void setOverHeard2(String overHeard2) {
		this.overHeard2 = overHeard2;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean isExistNull(){
		if(StringUtils.isEmpty(basePriceId) 
			|| StringUtils.isEmpty(pricingFactor)
			|| StringUtils.isEmpty(exchangeRate)
			|| StringUtils.isEmpty(overHeard1)
			|| StringUtils.isEmpty(overHeard2)
			){
			return true;
		}
		return false;

	}
	
}
