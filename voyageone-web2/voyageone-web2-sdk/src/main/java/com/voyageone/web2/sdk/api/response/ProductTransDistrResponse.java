package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;


/**
 * /product/translate/distribute products get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductTransDistrResponse extends VoApiResponse {

	/**
	 * 数据体信息
	 */
    private List<CmsBtProductModel> products;

	/**
	 * 用户已完成的数量.
	 */
	private int userDoneCount;

	/**
	 * 已完成的数量.
	 */
	private int totalDoneCount;

	/**
	 * 所有未完成的数量.
	 */
	private int totalUndoneCount;

	public List<CmsBtProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<CmsBtProductModel> products) {
		this.products = products;
	}

	public int getUserDoneCount() {
		return userDoneCount;
	}

	public void setUserDoneCount(int userDoneCount) {
		this.userDoneCount = userDoneCount;
	}

	public int getTotalDoneCount() {
		return totalDoneCount;
	}

	public void setTotalDoneCount(int totalDoneCount) {
		this.totalDoneCount = totalDoneCount;
	}

	public int getTotalUndoneCount() {
		return totalUndoneCount;
	}

	public void setTotalUndoneCount(int totalUndoneCount) {
		this.totalUndoneCount = totalUndoneCount;
	}
}
