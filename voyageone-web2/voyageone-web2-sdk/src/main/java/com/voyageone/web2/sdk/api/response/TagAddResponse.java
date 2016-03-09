package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;


/**
 * /tag/add tag add response
 * Created on 2015-12-14
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagAddResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private CmsBtTagModel tag;

	public CmsBtTagModel getTag() {
		return tag;
	}

	public void setTag(CmsBtTagModel tag) {
		this.tag = tag;
	}
}
