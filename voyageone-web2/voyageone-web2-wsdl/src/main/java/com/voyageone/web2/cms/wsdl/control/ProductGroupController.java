package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductGroupService;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
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
    private ProductGroupService productGroupService;

    /**
     * selectOne
     * @return ProductGroupGetResponse
     */
    @RequestMapping("selectOne")
    public ProductGroupGetResponse selectOne(@RequestBody ProductGroupGetRequest request) {
        return productGroupService.selectOne(request);
    }

    /**
     * selectList
     * @return ProductGroupsGetResponse
     */
    @RequestMapping("selectList")
    public ProductGroupsGetResponse selectList(@RequestBody ProductGroupsGetRequest request) {
        return productGroupService.selectList(request);
    }

    /**
     * selectList
     * @return ProductSkusPutResponse
     */
    @RequestMapping("put")
    public ProductGroupsPutResponse put(@RequestBody ProductGroupsPutRequest request) {
        return productGroupService.saveGroups(request);
    }

    /**
     * delete
     * @return ProductGroupsDeleteResponse
     */
    @RequestMapping("delete")
    public ProductGroupsDeleteResponse deleteList(@RequestBody ProductGroupsDeleteRequest request) {
        return productGroupService.deleteList(request);
    }

    @RequestMapping("updMainProduct")
    public VoApiUpdateResponse updMainProduct(@RequestBody GroupMainProductUpdateRequest request){
        request.check();
        return productGroupService.updateMainProduct(request.getGroupId(),request.getChannelId(),request.getProductId(),request.getModifier());

    }
}
