package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;


/**
 * /puroduct/selectList products get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PostProductSelectListResponse extends VoApiResponse {

	/**
	 * 结果总数
	 */
	private Long totalCount;

	/**
	 * 数据体信息
	 */
    private List<CmsBtProductModel> products;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<CmsBtProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<CmsBtProductModel> products) {
		this.products = products;
	}
}
