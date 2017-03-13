package com.voyageone.web2.sdk.api.response.cms;

import com.voyageone.service.bean.cms.product.ProductForOmsBean;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;


/**
 * /product/getOmsProductsInfo
 * Created on 2016-02-03
 *
 * @author edward.lin
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductForOmsGetResponse extends VoApiResponse {

	/**
	 * 数据体信息
	 */
	private List<ProductForOmsBean> resultInfo;

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

	public List<ProductForOmsBean> getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(List<ProductForOmsBean> resultInfo) {
		this.resultInfo = resultInfo;
	}
}
