package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;

public class ModelTMBean extends ModelBaseBean{

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tbName;

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tbAttachmentName;

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tmName;

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tmAttachmentName;

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tgName;

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tgAttachmentName;

	@Table(name="cms_bt_cn_tm_model_extend")
    private String tmShortDescription;

	@Table(name="cms_bt_cn_tm_model_extend")
    @Extends
    private String tmCategoryId;
	
	@Extends
	private String tmCategoryName;
	
	@Table(name="cms_bt_cn_tm_model_extend")
    private String tmLongDescription;
	
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

	public String getTmCategoryId() {
		return tmCategoryId;
	}

	public void setTmCategoryId(String tmCategoryId) {
		this.tmCategoryId = tmCategoryId;
	}

	public String getTmLongDescription() {
		return tmLongDescription;
	}

	public void setTmLongDescription(String tmLongDescription) {
		this.tmLongDescription = tmLongDescription;
	}

	public String getTmCategoryName() {
		return tmCategoryName;
	}

	public void setTmCategoryName(String tmCategoryName) {
		this.tmCategoryName = tmCategoryName;
	}
	
}
