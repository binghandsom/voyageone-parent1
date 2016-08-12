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
	
	/** 状态 [1：可用，0：禁用] */
	private Integer active;
	
	/** 区分保存动作 */
	private Boolean modified;
	
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

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Boolean getModified() {
		return modified;
	}

	public void setModified(Boolean modified) {
		this.modified = modified;
	}
	
}
