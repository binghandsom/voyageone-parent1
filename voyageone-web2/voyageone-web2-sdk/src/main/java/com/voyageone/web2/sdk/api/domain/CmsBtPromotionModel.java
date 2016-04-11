package com.voyageone.web2.sdk.api.domain;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * 
 * @author gbb Jan 13, 2016
 */
public class CmsBtPromotionModel extends BaseMongoModel {

	/** promotionId */
	private Integer promotionId;

	/** channelId */
	private String channelId;

	/** cartId */
	private Integer cartId;

	/** promotionStatus */
	private Boolean promotionStatus;

	/** promotionName */
	private String promotionName;

	/** prePeriodStart */
	private String prePeriodStart;

	/** prePeriodEnd */
	private String prePeriodEnd;

	/** preSaleStart */
	private String preSaleStart;

	/** preSaleEnd */
	private String preSaleEnd;

	/** activityStart */
	private String activityStart;

	/** activityEnd */
	private String activityEnd;

	/** tejiabaoId */
	private String tejiabaoId;

	/** promotionType */
	private String promotionType;

	/** cartName */
	private String cartName;

	/** isActive */
	private Boolean isActive;

	/** refTagId */
	private int refTagId;

	/** isAllPromotion */
	private Boolean isAllPromotion;

	private List<CmsBtTagModel> tagList;

	/**
	 * @return the promotionId
	 */
	public Integer getPromotionId() {
		return promotionId;
	}

	/**
	 * @param promotionId
	 *            the promotionId to set
	 */
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	/**
	 * @return the cartId
	 */
	public Integer getCartId() {
		return cartId;
	}

	/**
	 * @param cartId
	 *            the cartId to set
	 */
	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	/**
	 * @return the promotionStatus
	 */
	public Boolean getPromotionStatus() {
		return promotionStatus;
	}

	/**
	 * @param promotionStatus
	 *            the promotionStatus to set
	 */
	public void setPromotionStatus(Boolean promotionStatus) {
		this.promotionStatus = promotionStatus;
	}

	/**
	 * @return the preSaleStart
	 */
	public String getPreSaleStart() {
		return preSaleStart;
	}

	/**
	 * @param preSaleStart
	 *            the preSaleStart to set
	 */
	public void setPreSaleStart(String preSaleStart) {
		this.preSaleStart = preSaleStart;
	}

	/**
	 * @return the preSaleEnd
	 */
	public String getPreSaleEnd() {
		return preSaleEnd;
	}

	/**
	 * @param preSaleEnd
	 *            the preSaleEnd to set
	 */
	public void setPreSaleEnd(String preSaleEnd) {
		this.preSaleEnd = preSaleEnd;
	}

	/**
	 * @return the cartName
	 */
	public String getCartName() {
		return cartName;
	}

	/**
	 * @param cartName
	 *            the cartName to set
	 */
	public void setCartName(String cartName) {
		this.cartName = cartName;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the refTagId
	 */
	public int getRefTagId() {
		return refTagId;
	}

	/**
	 * @param refTagId
	 *            the refTagId to set
	 */
	public void setRefTagId(int refTagId) {
		this.refTagId = refTagId;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @return the promotionName
	 */
	public String getPromotionName() {
		return promotionName;
	}

	/**
	 * @return the prePeriodStart
	 */
	public String getPrePeriodStart() {
		return prePeriodStart;
	}

	/**
	 * @return the prePeriodEnd
	 */
	public String getPrePeriodEnd() {
		return prePeriodEnd;
	}

	/**
	 * @return the activityStart
	 */
	public String getActivityStart() {
		return activityStart;
	}

	/**
	 * @return the activityEnd
	 */
	public String getActivityEnd() {
		return activityEnd;
	}

	/**
	 * @return the tejiabaoId
	 */
	public String getTejiabaoId() {
		return tejiabaoId;
	}

	/**
	 * @return the promotionType
	 */
	public String getPromotionType() {
		return promotionType;
	}

	/**
	 * @param channelId
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId == null ? null : channelId.trim();
	}

	/**
	 * @param promotionName
	 */
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName == null ? null : promotionName
				.trim();
	}

	/**
	 * @param prePeriodStart
	 */
	public void setPrePeriodStart(String prePeriodStart) {
		this.prePeriodStart = prePeriodStart == null ? null : prePeriodStart
				.trim();
	}

	/**
	 * @param prePeriodEnd
	 */
	public void setPrePeriodEnd(String prePeriodEnd) {
		this.prePeriodEnd = prePeriodEnd == null ? null : prePeriodEnd.trim();
	}

	/**
	 * @param activityStart
	 */
	public void setActivityStart(String activityStart) {
		this.activityStart = activityStart == null ? null : activityStart
				.trim();
	}

	/**
	 * @param activityEnd
	 */
	public void setActivityEnd(String activityEnd) {
		this.activityEnd = activityEnd == null ? null : activityEnd.trim();
	}

	/**
	 * @param tejiabaoId
	 */
	public void setTejiabaoId(String tejiabaoId) {
		this.tejiabaoId = tejiabaoId == null ? null : tejiabaoId.trim();
	}

	/**
	 * @param promotionType
	 */
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType == null ? null : promotionType
				.trim();
	}

	public Boolean getIsAllPromotion() {
		return isAllPromotion;
	}

	public void setIsAllPromotion(Boolean isAllPromotion) {
		this.isAllPromotion = isAllPromotion;
	}

	public List<CmsBtTagModel> getTagList() {
		return tagList;
	}

	public void setTagList(List<CmsBtTagModel> tagList) {
		this.tagList = tagList;
	}
}