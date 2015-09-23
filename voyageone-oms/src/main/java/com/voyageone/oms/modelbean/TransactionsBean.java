package com.voyageone.oms.modelbean;

public class TransactionsBean {
	
	
	private String sourceOrderId;

	private String originSourceOrderId;
	
	/**
	 * 
	 */
	private String orderNumber;
	
	/**
	 * transaction_time
	 */
	private String transactionTime;
	
	/**
	 * sku
	 */
	private String sku;
	
	/**
	 * 
	 */
	private String description;
	
	/**
	 * 
	 */
	private String itemNumber;
	
	/**
	 * 
	 */
	private String debit;
	
	/**
	 * 
	 */
	private String credit;
	
	/**
	 * note_id
	 */
	private String noteId;

	/**
	 * type
	 */
	private String type;
	
	/**
	 * 
	 */
	private String creater;
	
	/**
	 * 
	 */
	private String modifier;	
	
	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public String getOriginSourceOrderId() {
		return originSourceOrderId;
	}

	public void setOriginSourceOrderId(String originSourceOrderId) {
		this.originSourceOrderId = originSourceOrderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	
	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
}
