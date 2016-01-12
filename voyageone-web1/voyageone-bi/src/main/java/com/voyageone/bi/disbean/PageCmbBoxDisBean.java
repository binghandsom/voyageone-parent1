package com.voyageone.bi.disbean;


import java.util.List;

// ComboBox 显示用Bean
public class PageCmbBoxDisBean {
	
	// Code
	private String code;
	// 显示内容
	private String name;
	// children
	private List<PageCmbBoxDisBean> children;

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PageCmbBoxDisBean> getChildren() { return children; }
	public void setChildren(List<PageCmbBoxDisBean> children) { this.children = children;}

}