package com.voyageone.cms.formbean;

import java.util.List;

import com.voyageone.cms.modelbean.Promotion;

public class PromotionMonthList {
	
	private String promotionMonth;

	private List<Promotion> promotionInfo;

	public String getPromotionMonth() {
		return promotionMonth;
	}

	public void setPromotionMonth(String promotionMonth) {
		this.promotionMonth = promotionMonth;
	}

	public List<Promotion> getPromotionInfo() {
		return promotionInfo;
	}

	public void setPromotionInfo(List<Promotion> promotionInfo) {
		this.promotionInfo = promotionInfo;
	}
	
	

}
