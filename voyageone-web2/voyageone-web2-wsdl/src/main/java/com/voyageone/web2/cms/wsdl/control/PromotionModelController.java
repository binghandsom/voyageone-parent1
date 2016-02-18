package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.PromotionDetailService;
import com.voyageone.web2.cms.wsdl.service.PromotionModelService;
import com.voyageone.web2.sdk.api.request.PromotionDetailAddRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelCountGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelsGetRequest;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelCountGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelDeleteResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = "/rest/promotion/model", method = RequestMethod.POST)
public class PromotionModelController extends BaseController {

    @Autowired
    private PromotionModelService promotionModelService;

    @RequestMapping("selectByParam")
    public PromotionModelsGetResponse getPromotionModelDetailList(
            @RequestBody PromotionModelsGetRequest promotionModelsGetRequest) {
        promotionModelsGetRequest.check();
        return promotionModelService.getPromotionModelDetailList(promotionModelsGetRequest);
    }

    @RequestMapping("countByParam")
    public PromotionModelCountGetResponse getPromotionModelDetailListCnt(
            @RequestBody PromotionModelCountGetRequest promotionModelCountGetRequest) {
        promotionModelCountGetRequest.check();
        return promotionModelService.getPromotionModelDetailListCnt(promotionModelCountGetRequest);
    }

    @RequestMapping("deleteByModel")
    public PromotionModelDeleteResponse remove(
            @RequestBody PromotionModelDeleteRequest promotionModelDeleteRequest) {
        promotionModelDeleteRequest.check();
        return promotionModelService.deleteCmsPromotionModel(promotionModelDeleteRequest);
    }
}
