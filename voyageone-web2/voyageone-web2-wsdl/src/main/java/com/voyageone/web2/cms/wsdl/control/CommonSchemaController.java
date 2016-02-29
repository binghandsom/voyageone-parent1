package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.CommonSchemaService;
import com.voyageone.web2.sdk.api.request.CommonSchemaGetRequest;
import com.voyageone.web2.sdk.api.response.CommonSchemaGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(
        value  = "/rest/commonSchema",
        method = RequestMethod.POST
)
public class CommonSchemaController extends BaseController {

    @Autowired
    private CommonSchemaService commonSchemaService;

    @RequestMapping("selectAll")
    public CommonSchemaGetResponse selectAll(@RequestBody CommonSchemaGetRequest request) {
        request.check();
        return commonSchemaService.selectAll();
    }

}
