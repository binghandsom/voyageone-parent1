package com.voyageone.web2.sdk.api.response;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiListResponse;
import java.util.List;


/**
 * product groups get response
 * /product/group/selectList
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupsGetResponse extends VoApiListResponse {

    /**
     * 数据体信息
     */
    private List<CmsBtProductModel_Group_Platform> productGroupPlatforms;

	public List<CmsBtProductModel_Group_Platform> getProductGroupPlatforms() {
		return productGroupPlatforms;
	}

	public void setProductGroupPlatforms(List<CmsBtProductModel_Group_Platform> productGroupPlatforms) {
		this.productGroupPlatforms = productGroupPlatforms;
	}
}
