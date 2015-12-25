package com.voyageone.web2.cms.wsdl.control.test;

import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.sdk.api.request.ProductGroupsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGroupGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.service.ProductGroupGetClient;
import com.voyageone.web2.sdk.api.service.ProductGroupsGetClient;
import com.voyageone.web2.sdk.api.service.ProductsGetClient;
import org.springframework.beans.factory.annotation.Autowired;
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
        value  = "/rest/puroduct/group",
        method = RequestMethod.POST
)
public class TestProductGroupGetController extends BaseController {

    @Autowired
    protected ProductGroupGetClient productGroupGetClient;

    @Autowired
    protected ProductGroupsGetClient productGroupsGetClient;


    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("testSelectOne")
    public ProductGroupGetResponse testSelectOne() {

        //SDK取得Product 数据
        //CmsBtProductModel_Group_Platform model = productGroupGetClient.getProductGroupByGroupId("300", 134);
        CmsBtProductModel_Group_Platform model = productGroupGetClient.getProductGroupByNumIId("300", 21, "2000347");


        ProductGroupGetResponse result = new ProductGroupGetResponse();
        result.setProductGroupPlatform(model);

        // 返回用户信息
        return result;
    }

    /**
     * 返回selectOne
     * @return
     */
    @RequestMapping("testSelectList")
    public ProductGroupsGetResponse testSelectList() {

        ProductGroupsGetRequest requestModel = new ProductGroupsGetRequest("300");
        // add query
//        requestModel.getGroupIds().add(134L);
//        requestModel.getGroupIds().add(132L);

        requestModel.setCartId(21);
        requestModel.getNumIIds().add("2000347");
        requestModel.getNumIIds().add("2000348");
        requestModel.getNumIIds().add("2000349");


//        requestModel.addProp("fields.brand", "Jewelry4");

        // add column
//        requestModel.addField("fields.code");
//        requestModel.addField("fields.brand");
//        requestModel.addField("fields.productName");
//        requestModel.addField("fields.middleTitle");
//        // add sort
//        requestModel.addSort("fields.code", false);
//        requestModel.addSort("fields.brand", false);
        // add pagesize
//        requestModel.setPageNo(2);
//        requestModel.setPageSize(1);


        ProductGroupsGetResponse response = productGroupsGetClient.getMainGroups(requestModel);

        // 返回用户信息
        return response;
    }

}
