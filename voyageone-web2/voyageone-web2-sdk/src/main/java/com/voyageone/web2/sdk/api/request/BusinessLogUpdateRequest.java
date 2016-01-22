package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.BusinessLogPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * @author aooer 2016/1/21.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BusinessLogUpdateRequest extends VoApiRequest<BusinessLogPutResponse>{

    private Integer seq;

    @Override
    public String getApiURLPath() {
        return "/businesslog/updatefinishstatus";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        RequestUtils.checkNotEmpty("seq",seq);
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

}
