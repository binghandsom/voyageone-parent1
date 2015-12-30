package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;


/**
 * product sku get response
 * /puroduct/sku/selectList
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductSkusGetResponse extends VoApiListResponse {

    /**
     * 数据体信息
     */
    private List<CmsBtProductModel_Sku> productSkus;

	public List<CmsBtProductModel_Sku> getProductSkus() {
		return productSkus;
	}

	public void setProductSkus(List<CmsBtProductModel_Sku> productSkus) {
		this.productSkus = productSkus;
	}
}
