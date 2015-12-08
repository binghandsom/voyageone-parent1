package com.voyageone.cms.formbean;

import java.util.List;

public class PromotionYearList {
	
 
	private String promotionYear;
	
	private List<PromotionMonthList> promotionMonthList;


	public String getPromotionYear() {
		return promotionYear;
	}

	public void setPromotionYear(String promotionYear) {
		this.promotionYear = promotionYear;
	}

	public List<PromotionMonthList> getPromotionMonthList() {
		return promotionMonthList;
	}

	public void setPromotionMonthList(List<PromotionMonthList> promotionMonthList) {
		this.promotionMonthList = promotionMonthList;
	}
	
	
	
}
