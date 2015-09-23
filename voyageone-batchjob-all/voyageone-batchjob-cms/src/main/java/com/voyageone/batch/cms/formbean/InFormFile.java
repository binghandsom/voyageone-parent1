package com.voyageone.batch.cms.formbean;


/**
 * 文件读入bean
 * 
 * @author jerry
 *
 */
public class InFormFile {

	private String sourceOrderId;
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public boolean equals(Object obj) {
		if (obj instanceof InFormFile) {
			InFormFile u = (InFormFile) obj;
			return this.sourceOrderId.equals(u.sourceOrderId)
					&& this.fileName.equals(fileName);
		}
		return super.equals(obj);
	}
}
