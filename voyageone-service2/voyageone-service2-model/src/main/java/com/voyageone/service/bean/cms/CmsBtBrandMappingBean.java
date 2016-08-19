package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;

/**
 * 品牌映射属性
 * @author Wangtd 2016/07/25
 * @since 2.3.0
 */
public class CmsBtBrandMappingBean extends CmsMtBrandsMappingModel {
	
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
