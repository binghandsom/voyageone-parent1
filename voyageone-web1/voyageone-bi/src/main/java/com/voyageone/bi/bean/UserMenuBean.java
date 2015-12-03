package com.voyageone.bi.bean;

import java.io.Serializable;

public class UserMenuBean  implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7114524619946448537L;
	
	private String code;
	private String name;
	private String pname;
	private String link;
	private String reportSize;
	private boolean isSelect = false;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getReportSize() {
		return reportSize;
	}
	public void setReportSize(String reportSize) {
		this.reportSize = reportSize;
	}

	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
}
