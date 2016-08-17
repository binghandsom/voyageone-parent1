package com.voyageone.web2.admin.bean;

/**
 * 系统的表单基本项，包括分页属性和表的公共字段等。
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
public class AdminFormBean {
	
	/** 页码 */
	private Integer pageNum;
	/** 每页大小 */
	private Integer pageSize;
	
	public Integer getPageNum() {
		return pageNum;
	}
	
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}	
}
