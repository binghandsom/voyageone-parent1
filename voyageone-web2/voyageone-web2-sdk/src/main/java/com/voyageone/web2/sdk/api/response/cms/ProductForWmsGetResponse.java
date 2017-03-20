package com.voyageone.web2.sdk.api.response.cms;

import com.voyageone.service.bean.cms.product.ProductForWmsBean;
import com.voyageone.web2.sdk.api.VoApiResponse;


/**
 * /product/getWmsProductsInfo
 * Created on 2016-02-03
 *
 * @author edward.lin
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductForWmsGetResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private ProductForWmsBean resultInfo;

	/**
	 * 返回check结果
	 */
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ProductForWmsBean getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(ProductForWmsBean resultInfo) {
		this.resultInfo = resultInfo;
	}
}
