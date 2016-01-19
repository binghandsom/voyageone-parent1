package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionGroupModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionDetailDeleteRequest extends VoApiRequest<PromotionDetailPutResponse> {

    private  List<CmsBtPromotionGroupModel> promotionModes;
    private String channelId;

    @Override
    public String getApiURLPath() {
        return "/promotion/detail/remove";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        RequestUtils.checkNotEmpty("promotionModes",promotionModes);
        RequestUtils.checkNotEmpty("promotionModes must have At least one element!",promotionModes.get(0));
        RequestUtils.checkNotEmpty("channelId",channelId);
    }

    public List<CmsBtPromotionGroupModel> getPromotionModes() {
        return promotionModes;
    }

    public void setPromotionModes(List<CmsBtPromotionGroupModel> promotionModes) {
        this.promotionModes = promotionModes;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

}
