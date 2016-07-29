package com.voyageone.service.bean.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by Wangtd on 7/26/16.
 */
public class CmsBtBrandBean extends BaseModel {
	
	/** 品牌ID */
	private String brandId;
	
	/** 品牌名称 */
	private String brandName;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}
