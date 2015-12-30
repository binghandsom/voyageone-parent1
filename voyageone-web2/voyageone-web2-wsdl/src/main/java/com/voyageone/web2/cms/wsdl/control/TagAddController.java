package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductGetService;
import com.voyageone.web2.cms.wsdl.service.TagAddService;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * product Controller
 *
 * @author jerry 15/12/29
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/tag",
        method = RequestMethod.POST
)
public class TagAddController extends BaseController {

    @Autowired
    private TagAddService tagAddService;

    /**
     * selectOne
     *
     * @return CmsBtProductModel
     */
    @RequestMapping("add")
    public TagAddResponse add(@RequestBody TagAddRequest request) {
        return tagAddService.addTag(request);
    }
}