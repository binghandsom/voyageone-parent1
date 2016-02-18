package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.PromotionSkuService;
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
@RequestMapping(value = "/rest/promotion/sku", method = RequestMethod.POST)
public class PromotionSkuController extends BaseController {

    @Autowired
    private PromotionSkuService promotionSkuService;

    @RequestMapping("selectByParam")
    public PromotionSkuGetResponse getPromotionModelDetailList(
            @RequestBody PromotionSkuGetRequest promotionSkuGetRequest) {
        promotionSkuGetRequest.check();
        return promotionSkuService.getPromotionSkuList(promotionSkuGetRequest);
    }

    @RequestMapping("countByParam")
    public PromotionSkuCountResponse getPromotionModelDetailListCnt(
            @RequestBody PromotionSkuCountRequest promotionSkuCountRequest) {
        promotionSkuCountRequest.check();
        return promotionSkuService.getPromotionSkuListCnt(promotionSkuCountRequest);
    }

    @RequestMapping("deleteByParam")
    public PromotionSkuDeleteResponse remove(
            @RequestBody PromotionSkuDeleteRequest promotionSkuDeleteRequest) {
        promotionSkuDeleteRequest.check();
        return promotionSkuService.remove(promotionSkuDeleteRequest);
    }
}
