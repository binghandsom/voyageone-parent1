package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;

public class ProductCNTMProductInfo extends ProductBaseBean {
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tbName;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tbAttachmentName;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tmName;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tmAttachmentName;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tgName;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tgAttachmentName;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tmShortDescription;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tmLongDescription;
	@Extends
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tmCategoryId;
	@Table(name="cms_bt_cn_tm_product_extend")
	private String tmCategoryName;

	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public String getTbAttachmentName() {
		return tbAttachmentName;
	}

	public void setTbAttachmentName(String tbAttachmentName) {
		this.tbAttachmentName = tbAttachmentName;
	}

	public String getTmName() {
		return tmName;
	}

	public void setTmName(String tmName) {
		this.tmName = tmName;
	}

	public String getTmAttachmentName() {
		return tmAttachmentName;
	}

	public void setTmAttachmentName(String tmAttachmentName) {
		this.tmAttachmentName = tmAttachmentName;
	}

	public String getTgName() {
		return tgName;
	}

	public void setTgName(String tgName) {
		this.tgName = tgName;
	}

	public String getTgAttachmentName() {
		return tgAttachmentName;
	}

	public void setTgAttachmentName(String tgAttachmentName) {
		this.tgAttachmentName = tgAttachmentName;
	}

	public String getTmShortDescription() {
		return tmShortDescription;
	}

	public void setTmShortDescription(String tmShortDescription) {
		this.tmShortDescription = tmShortDescription;
	}

	public String getTmLongDescription() {
		return tmLongDescription;
	}

	public void setTmLongDescription(String tmLongDescription) {
		this.tmLongDescription = tmLongDescription;
	}

	public String getTmCategoryId() {
		return tmCategoryId;
	}

	public void setTmCategoryId(String tmCategoryId) {
		this.tmCategoryId = tmCategoryId;
	}

	public String getTmCategoryName() {
		return tmCategoryName;
	}

	public void setTmCategoryName(String tmCategoryName) {
		this.tmCategoryName = tmCategoryName;
	}

}
