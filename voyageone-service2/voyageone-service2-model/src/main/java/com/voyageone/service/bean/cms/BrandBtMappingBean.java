package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;

/**
 * Created by Wangtd on 7/25/16.
 */
public class BrandBtMappingBean extends CmsMtBrandsMappingModel {
	
	/** 主品牌名称 */
	private String masterName;
	
	/** 客户品牌名称 */
	private String brandName;

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}
