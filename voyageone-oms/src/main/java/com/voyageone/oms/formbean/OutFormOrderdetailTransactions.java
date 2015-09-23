package com.voyageone.oms.formbean;

public class OutFormOrderdetailTransactions {
	/**
	 * order_number
	 */
	private String orderNumber;
	
	/**
	 * auto_number
	 */
	private String autoNumber;

	/**
	 * transaction_time
	 */
	private String transactionTime;	
	
	/**
	 * sku
	 */
	private String sku;
	
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
	
	/**
	 * note
	 */
	private String note;

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

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
