package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.PromotionModelService;
import com.voyageone.web2.cms.wsdl.service.PromotionTaskService;
import com.voyageone.web2.sdk.api.request.PromotionModelsGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionTaskAddRequest;
import com.voyageone.web2.sdk.api.response.PromotionModelsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionTaskAddResponse;
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
@RequestMapping(value = "/rest/promotion/task", method = RequestMethod.POST)
public class PromotionTaskController extends BaseController {

    @Autowired
    private PromotionTaskService promotionTaskService;

    @RequestMapping("insert")
    public PromotionTaskAddResponse insertPromotionTask(
            @RequestBody PromotionTaskAddRequest promotionTaskAddRequest) {
        promotionTaskAddRequest.check();
        return promotionTaskService.insertPromotionTask(promotionTaskAddRequest);
    }

}
