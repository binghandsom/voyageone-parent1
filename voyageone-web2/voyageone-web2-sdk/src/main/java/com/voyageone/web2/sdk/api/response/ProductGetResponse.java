package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiResponse;


/**
 * /puroduct/selectOne product get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGetResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private CmsBtProductModel product;

	public CmsBtProductModel getProduct() {
		return product;
	}

	public void setProduct(CmsBtProductModel product) {
		this.product = product;
	}
}
