package com.voyageone.oms.modelbean;


public class NotesBean {
	private String sourceOrderId;
	private String id;
	private String type;
	private String numericKey;
	private String itemNumber;
	private String entryDate;
	private String entryTime;
	private String notes;
	private String enteredBy;
	private String filePath;
	private String created;
	private String modified;
	private String creater;
	private String modifier;
	
	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
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
	
	public String getCreated() {
		return created;
	}
	
	public void setCreated(String created) {
		this.created = created;
	}
	
	public String getModified() {
		return modified;
	}
	
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
}
