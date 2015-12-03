package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;

public class ModelJDBean extends ModelBaseBean{

	@Table(name="cms_bt_cn_jd_model_extend")
    private String jdName;

	@Table(name="cms_bt_cn_jd_model_extend")
    private String jgName;

	@Table(name="cms_bt_cn_jd_model_extend")
    private String jdShortDescription;

	@Table(name="cms_bt_cn_jd_model_extend")
    @Extends
    private String jdCategoryId;
	
	@Extends
	private String jdCategoryName;

	@Table(name="cms_bt_cn_jd_model_extend")
    private String jdLongDescription;
	
	public String getJdName() {
		return jdName;
	}

	public void setJdName(String jdName) {
		this.jdName = jdName;
	}

	public String getJgName() {
		return jgName;
	}

	public void setJgName(String jgName) {
		this.jgName = jgName;
	}

	public String getJdShortDescription() {
		return jdShortDescription;
	}

	public void setJdShortDescription(String jdShortDescription) {
		this.jdShortDescription = jdShortDescription;
	}

	public String getJdCategoryId() {
		return jdCategoryId;
	}

	public void setJdCategoryId(String jdCategoryId) {
		this.jdCategoryId = jdCategoryId;
	}

	public String getJdLongDescription() {
		return jdLongDescription;
	}

	public void setJdLongDescription(String jdLongDescription) {
		this.jdLongDescription = jdLongDescription;
	}

	public String getJdCategoryName() {
		return jdCategoryName;
	}

	public void setJdCategoryName(String jdCategoryName) {
		this.jdCategoryName = jdCategoryName;
	}

}
