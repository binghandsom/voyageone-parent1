package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.service.PromotionDetailService;
import com.voyageone.web2.sdk.api.request.PromotionDetailAddRequest;
import com.voyageone.web2.sdk.api.request.PromotionDetailDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionDetailUpdateRequest;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = "/rest/promotion/detail", method = RequestMethod.POST)
public class PromotionDetailController {

    @Autowired
    private PromotionDetailService promotionDetailService;

    @RequestMapping("insert")
    public PromotionDetailPutResponse insert(
            @RequestBody PromotionDetailAddRequest promotionDetailAddRequest) {
        return promotionDetailService.insert(promotionDetailAddRequest);
    }

    @RequestMapping("update")
    public PromotionDetailPutResponse update(
            @RequestBody PromotionDetailUpdateRequest promotionDetailUpdateRequest) {
        return promotionDetailService.update(promotionDetailUpdateRequest);
    }

    @RequestMapping("remove")
    public PromotionDetailPutResponse remove(
            @RequestBody PromotionDetailDeleteRequest promotionDetailDeleteRequest) {
        return promotionDetailService.remove(promotionDetailDeleteRequest);
    }
}
