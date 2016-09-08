package com.voyageone.web2.openapi.channeladvisor.service.sandbox;

import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.service.CAProductService;
import com.voyageone.web2.sdk.api.channeladvisor.domain.ProductGroupModel;
import com.voyageone.web2.sdk.api.channeladvisor.domain.ProductGroupResultModel;
import com.voyageone.web2.sdk.api.channeladvisor.request.ProductGroupRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Profile("sandbox")
public class CAProductServiceImpl extends CAOpenApiBaseService implements CAProductService {

    @Autowired
    private JsonResourcesService resourcesService;

    public ActionResponse getProducts(String groupFields, String buyableFields) {

        List<ProductGroupModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "getProducts", ProductGroupModel.class);

        return success(responseBody);
    }

    public ActionResponse updateProducts(List<ProductGroupRequest> request) {
        if (CollectionUtils.isEmpty(request)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        // TODO: 2016/9/7 获取jsonbody 响应 mock response

        List<ProductGroupResultModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "updateProducts", ProductGroupResultModel.class);

        return success(responseBody);
    }

    public ActionResponse updateQuantityPrice(List<ProductGroupRequest> request) {
        if (CollectionUtils.isEmpty(request)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        List<ProductGroupResultModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "updateQuantityPrice", ProductGroupResultModel.class);

        return success(responseBody);
    }

    public ActionResponse updateStatus(List<ProductGroupRequest> request) {
        if (CollectionUtils.isEmpty(request)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        List<ProductGroupResultModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "updateStatus", ProductGroupResultModel.class);

        return success(responseBody);
    }

}
