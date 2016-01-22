package com.voyageone.web2.cms.views.system;

import com.sun.org.apache.xpath.internal.SourceTree;
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
import org.apache.poi.ss.formula.functions.T;
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
        request.setProductIds((ArrayList<Integer>)params.get("productIds"));
        request.setErrType(vps(params,"errType"));
        request.setProductName(vps(params,"productName"));
        request.setCartId(vpi(params,"cartId"));
        request.setCatId(vps(params,"catId"));
        BusinessLogGetResponse response=voApiClient.execute(request);
        return success(new BeanMap(response));
    }

    //validate parameter int
    private int vpi(Map params,String key){
        try{
            return validateParameter(params,key,Integer.class);
        }catch(Exception e){
            return -1;
        }
    }

    //validate parameter string
    private String vps(Map params,String key){
        return validateParameter(params,key,String.class);
    }

    //validate parameter
    private static <E> E validateParameter(Map params,String key,Class<E> clazz){
        if(params.get(key)!=null)
            return (E)params.get(key);
        return null;
    }

    @RequestMapping("updateFinishStatus")
    public AjaxResponse updateStatus(@RequestBody Map params) {
        BusinessLogUpdateRequest request = new BusinessLogUpdateRequest();
        request.setSeq(Integer.parseInt(params.get("seq").toString()));
        BusinessLogPutResponse response=voApiClient.execute(request);
        return success(new BeanMap(response));
    }

}
