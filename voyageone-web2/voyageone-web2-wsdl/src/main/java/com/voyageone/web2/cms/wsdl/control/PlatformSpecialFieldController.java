package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.service.PlatformSpecialFieldService;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * PlatformSpecialField Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/platformspecialfield",
        method = RequestMethod.POST
)
public class PlatformSpecialFieldController {

    @Autowired
    private PlatformSpecialFieldService platformSpecialFieldService;

    /**
     * get
     *
     * @return PlatformSpecialFieldsGetResponse
     */
    @RequestMapping("get")
    public PlatformSpecialFieldsGetResponse get(@RequestBody PlatformSpecialFieldsGetRequest request) {
        return platformSpecialFieldService.get(request);
    }

    /**
     * put
     *
     * @return ProductPriceLogGetResponse
     */
    @RequestMapping("put")
    public PlatformSpecialFieldsPutResponse put(@RequestBody PlatformSpecialFieldsPutRequest request) {
        return platformSpecialFieldService.put(request);
    }

    /**
     * remove product
     *
     * @return ProductsDeleteResponse
     */
    @RequestMapping("delete")
    public PlatformSpecialFieldDeleteResponse delete(@RequestBody PlatformSpecialFieldDeleteRequest request) {
        return platformSpecialFieldService.delete(request);
    }
}
