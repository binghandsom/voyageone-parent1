package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductGroupGetService;
import com.voyageone.web2.sdk.api.request.ProductGroupGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGroupGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/product/group",
        method = RequestMethod.POST
)
public class ProductGroupController extends BaseController {

    @Autowired
    private ProductGroupGetService productGroupService;

    /**
     * selectOne
     * @return CmsBtProductModel
     */
    @RequestMapping("selectOne")
    public ProductGroupGetResponse selectOne(@RequestBody ProductGroupGetRequest request) {
        return productGroupService.selectOne(request);
    }

    /**
     * selectList
     * @return List<CmsBtProductModel>
     */
    @RequestMapping("selectList")
    public ProductGroupsGetResponse selectList(@RequestBody ProductGroupsGetRequest request) {
        return productGroupService.selectList(request);
    }
}
