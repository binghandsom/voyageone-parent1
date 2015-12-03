package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;

public class ProductCNJDProductInfo extends ProductBaseBean{
	@Extends
	@Table(name="cms_bt_cn_jd_product_extend")
	private String jdName;	
	@Extends
	@Table(name="cms_bt_cn_jd_product_extend")
	private String jgName;	
	
	@Extends
	@Table(name="cms_bt_cn_jd_product_extend")
	private String jdShortDescription;
	
	@Extends
	@Table(name="cms_bt_cn_jd_product_extend")
	private String jdLongDescription;	
	@Extends
	@Table(name="cms_bt_cn_jd_product_extend")
	private String jdCategoryId	;	
	
	@Table(name="cms_bt_cn_jd_product_extend")
	private String jdCategoryName;	
	

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

	public String getJdLongDescription() {
		return jdLongDescription;
	}

	public void setJdLongDescription(String jdLongDescription) {
		this.jdLongDescription = jdLongDescription;
	}

	public String getJdCategoryId() {
		return jdCategoryId;
	}

	public void setJdCategoryId(String jdCategoryId) {
		this.jdCategoryId = jdCategoryId;
	}

	public String getJdCategoryName() {
		return jdCategoryName;
	}

	public void setJdCategoryName(String jdCategoryName) {
		this.jdCategoryName = jdCategoryName;
	}


}
