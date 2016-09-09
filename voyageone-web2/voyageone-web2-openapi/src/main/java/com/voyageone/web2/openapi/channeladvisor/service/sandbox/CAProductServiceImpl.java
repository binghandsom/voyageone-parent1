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

import java.util.ArrayList;
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

    public ActionResponse updateProducts(List<ProductGroupRequest> productGroups) {
        // check
        // productGroups is empty return empty response
        if (CollectionUtils.isEmpty(productGroups)) {
            success(new ArrayList<>());
        }

        List<ProductGroupResultModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "updateProducts", ProductGroupResultModel.class);

        return success(responseBody);
    }

    public ActionResponse updateQuantityPrice(List<ProductGroupRequest> productGroups) {
        // check
        // productGroups is empty return empty response
        if (CollectionUtils.isEmpty(productGroups)) {
            success(new ArrayList<>());
        }

        List<ProductGroupResultModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "updateQuantityPrice", ProductGroupResultModel.class);

        return success(responseBody);
    }

    public ActionResponse updateStatus(List<ProductGroupRequest> productGroups) {
        // check
        // productGroups is empty return empty response
        if (CollectionUtils.isEmpty(productGroups)) {
            success(new ArrayList<>());
        }

        List<ProductGroupResultModel> responseBody = resourcesService.getResourceDataList(this.getClass().getName(), "updateStatus", ProductGroupResultModel.class);

        return success(responseBody);
    }

}
