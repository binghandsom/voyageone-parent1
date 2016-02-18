package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.PromotionCodeService;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
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
@RequestMapping(value = "/rest/promotion/code", method = RequestMethod.POST)
public class PromotionCodeController extends BaseController {

    @Autowired
    private PromotionCodeService promotionCodeService;

    @RequestMapping("selectByParam")
    public PromotionCodeGetResponse getPromotionModelDetailList(
            @RequestBody PromotionCodeGetRequest promotionCodeGetRequest) {
        promotionCodeGetRequest.check();
        return promotionCodeService.getPromotionCodeList(promotionCodeGetRequest);
    }

    @RequestMapping("countByParam")
    public PromotionCodeGetCountResponse getPromotionModelDetailListCnt(
            @RequestBody PromotionCodeGetCountRequest promotionCodeGetCountRequest) {
        promotionCodeGetCountRequest.check();
        return promotionCodeService.getPromotionCodeListCnt(promotionCodeGetCountRequest);
    }

    @RequestMapping("deleteByModel")
    public PromotionCodeDeleteResponse remove(
            @RequestBody PromotionCodeDeleteRequest promotionCodeDeleteRequest) {
        promotionCodeDeleteRequest.check();
        return promotionCodeService.deletePromotionCode(promotionCodeDeleteRequest);
    }
}
