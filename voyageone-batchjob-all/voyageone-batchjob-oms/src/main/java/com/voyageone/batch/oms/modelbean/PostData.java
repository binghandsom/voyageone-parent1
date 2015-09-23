/**
 * 
 */
package com.voyageone.batch.oms.modelbean;

import java.util.List;

/**
 * @author jacky
 *
 */
public class PostData {

	private String channelId;
	
	private String screctKey;
	
	private String sessionKey;
	
	private List<Order> orderDatas;
	
	private List<OrderDetails> orderDetailDatas;
	
	private String startTime;
	
	private String endTime;
	
//	private List<NotesBean> notesDatas;
//	
//	private List<OrderDiscountsBean> orderDiscountsDatas;

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
	 * @return the screctKey
	 */
	public String getScrectKey() {
		return screctKey;
	}

	/**
	 * @param screctKey the screctKey to set
	 */
	public void setScrectKey(String screctKey) {
		this.screctKey = screctKey;
	}

	/**
	 * @return the sessionKey
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * @return the orderDatas
	 */
	public List<Order> getOrderDatas() {
		return orderDatas;
	}

	/**
	 * @param orderDatas the orderDatas to set
	 */
	public void setOrderDatas(List<Order> orderDatas) {
		this.orderDatas = orderDatas;
	}

	/**
	 * @return the orderDetailDatas
	 */
	public List<OrderDetails> getOrderDetailDatas() {
		return orderDetailDatas;
	}

	/**
	 * @param orderDetailDatas the orderDetailDatas to set
	 */
	public void setOrderDetailDatas(List<OrderDetails> orderDetailDatas) {
		this.orderDetailDatas = orderDetailDatas;
	}
	
//	/**
//	 * @return the notesDatas
//	 */	
//	public List<NotesBean> getNotesDatas() {
//		return notesDatas;
//	}
//	
//	/**
//	 * @param notesDatas the notesDatas to set
//	 */
//	public void setNotesDatas(List<NotesBean> notesDatas) {
//		this.notesDatas = notesDatas;
//	}
//	
//	
//	/**
//	 * @return the orderDiscountsDatas
//	 */	
//	public List<OrderDiscountsBean> getOrderDiscountsDatas() {
//		return orderDiscountsDatas;
//	}
//	
//	/**
//	 * @param orderDiscountsDatas the orderDiscountsDatas to set
//	 */
//	public void setOrderDiscountsDatas(List<OrderDiscountsBean> orderDiscountsDatas) {
//		this.orderDiscountsDatas = orderDiscountsDatas;
//	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
