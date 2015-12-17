package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiResponse;


/**
 * TOP API: product get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PostProductSelectOneResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private CmsBtProductModel data;

	public CmsBtProductModel getData() {
		return data;
	}

	public void setData(CmsBtProductModel data) {
		this.data = data;
	}
}
