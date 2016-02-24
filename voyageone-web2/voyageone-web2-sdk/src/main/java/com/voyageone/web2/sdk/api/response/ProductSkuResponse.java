package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.Map;


/**
 * /product/selectOne product get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductSkuResponse extends VoApiResponse {

    /**VoApiResponse
     * 数据体信息
     */
    private Map<String, Integer> skuInventories;

	public Map<String, Integer> getSkuInventories() {
		return skuInventories;
	}

	public void setSkuInventories(Map<String, Integer> skuInventories) {
		this.skuInventories = skuInventories;
	}
}
