package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;


/**
 * //tag/remove tag remove response
 * Created on 2015-12-14
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagRemoveResponse extends VoApiResponse {

	private boolean removeResult;

	public boolean isRemoveResult() {
		return removeResult;
	}

	public void setRemoveResult(boolean removeResult) {
		this.removeResult = removeResult;
	}
}
