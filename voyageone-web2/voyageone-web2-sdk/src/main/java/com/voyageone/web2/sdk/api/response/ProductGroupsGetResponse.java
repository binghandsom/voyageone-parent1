package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;


/**
 * product groups get response
 * /puroduct/group/selectList
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupsGetResponse extends VoApiResponse {

	/**
	 * 结果总数
	 */
	private Long totalCount;
    /**
     * 数据体信息
     */
    private List<CmsBtProductModel_Group_Platform> productGroupPlatforms;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<CmsBtProductModel_Group_Platform> getProductGroupPlatforms() {
		return productGroupPlatforms;
	}

	public void setProductGroupPlatforms(List<CmsBtProductModel_Group_Platform> productGroupPlatforms) {
		this.productGroupPlatforms = productGroupPlatforms;
	}
}
