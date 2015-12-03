package com.voyageone.oms.formbean;

public class OutFormOrderdetailPayments {
	/**
	 * order_number
	 */
	private String orderNumber;
	
	/**
	 * auto_number
	 */
	private String autoNumber;

	/**
	 * payment_time
	 */
	private String paymentTime;	
	
	/**
	 * description
	 */
	private String description;
	
	/**
	 * debit
	 */
	private String debit;
	
	/**
	 * credit
	 */
	private String credit;
	
	/**
	 * note_id
	 */
	private String noteId;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAutoNumber() {
		return autoNumber;
	}

	public void setAutoNumber(String autoNumber) {
		this.autoNumber = autoNumber;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	
}
