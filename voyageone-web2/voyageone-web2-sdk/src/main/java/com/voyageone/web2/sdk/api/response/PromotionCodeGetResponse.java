package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;

import java.util.List;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionCodeGetResponse extends VoApiListResponse {

    private List<CmsBtPromotionCodeModel> codeList;

    public List<CmsBtPromotionCodeModel> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CmsBtPromotionCodeModel> codeList) {
        this.codeList = codeList;
    }
}
