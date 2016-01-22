package com.voyageone.web2.cms.views.system;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.sdk.api.VoApiClient;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.BusinessLogGetRequest;
import com.voyageone.web2.sdk.api.request.BusinessLogUpdateRequest;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;
import com.voyageone.web2.sdk.api.response.BusinessLogPutResponse;
import net.minidev.json.JSONObject;
import org.apache.commons.beanutils.BeanMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author aooer 2016/1/21.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(
        value = "/cms/system/businesslog",
        method = RequestMethod.POST
)
public class CmsBtBusinessLogController extends CmsController {

    @Autowired
    private VoApiDefaultClient voApiClient;

    @RequestMapping("getlist")
    public AjaxResponse getCategoryList(@RequestBody Map params) {
        BusinessLogGetRequest request=new BusinessLogGetRequest();
        String[] productIdstr=params.get("productIds").toString().split(",");
        List<Integer> productIds=new ArrayList<Integer>();
        for(String productId:productIdstr){
            productIds.add(Integer.parseInt(productId));
        }
        request.setProductIds(productIds);
        request.setErrType(params.get("errType").toString());
        BusinessLogGetResponse response=voApiClient.execute(request);
        return success(new BeanMap(response));
    }

    @RequestMapping("updateFinishStatus")
    public AjaxResponse updateStatus(@RequestBody Map params) {
        BusinessLogUpdateRequest request = new BusinessLogUpdateRequest();
        request.setSeq(Integer.parseInt(params.get("seq").toString()));
        BusinessLogPutResponse response=voApiClient.execute(request);
        return success(new BeanMap(response));
    }

}
