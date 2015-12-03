package com.voyageone.oms.formbean;

public class OutFormOrderdetailNotes {
	/**
	 * Id
	 */
	private String id;
	
	/**
	 * type
	 */
	private String type;

	/**
	 * 订单号
	 */
	private String numericKey;
	
	/**
	 * Note明细号
	 */
	private String itemNumber;
	
	/**
	 * 录入日
	 */
	private String entryDate;
	
	/**
	 * 录入时
	 */
	private String entryTime;
	
	/**
	 * 内容
	 */
	private String notes;
	
	/**
	 * 录入人 
	 */
	private String enteredBy;

	/**
	 * Note图片路径（含Url）
	 */
	private String imgPath;
	
	/**
	 * 文件路径
	 */
	private String filePath;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumericKey() {
		return numericKey;
	}

	public void setNumericKey(String numericKey) {
		this.numericKey = numericKey;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
