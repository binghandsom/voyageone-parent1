package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.BusinessLogService;
import com.voyageone.web2.cms.wsdl.service.ProductService;
import com.voyageone.web2.sdk.api.request.BusinessLogGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value  = "/rest/businesslog",  method = RequestMethod.POST)
public class BusinessLogController extends BaseController{

    @Autowired
    private BusinessLogService businessLogService;

    /**
     *
     * @param request BusinessLogGetRequest
     * @return BusinessLogGetResponse
     */
    @RequestMapping("findlist")
    public BusinessLogGetResponse findList(@RequestBody BusinessLogGetRequest request) {
        return businessLogService.findList(request);
    }

}
