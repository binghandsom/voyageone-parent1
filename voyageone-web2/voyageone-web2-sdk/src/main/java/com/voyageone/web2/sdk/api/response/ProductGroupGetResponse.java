package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiResponse;


/**
 * main group product get response
 * /puroduct/group/selectOne
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupGetResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private CmsBtProductModel_Group_Platform productGroupPlatform;

	public CmsBtProductModel_Group_Platform getProductGroupPlatform() {
		return productGroupPlatform;
	}

	public void setProductGroupPlatform(CmsBtProductModel_Group_Platform productGroupPlatform) {
		this.productGroupPlatform = productGroupPlatform;
	}
}
