package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.feed.CmsBtCAdProductService;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProudctModel;
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
@Profile("product")
public class CAProductServiceImpl extends CAOpenApiBaseService implements CAProductService {

    @Autowired
    private CmsBtCAdProductService cmsBtCAdProductService;

    public ActionResponse getProducts(String groupFields, String buyableFields) {
        String channelId = getClientChannelId();
        List<ProductGroupModel> responseBody = new ArrayList<>();

        // TODO: 根据实际的业务处理


        return success(responseBody);
    }

    public ActionResponse updateProducts(List<ProductGroupRequest> productGroups) {
        String channelId = getClientChannelId();
        List<ProductGroupResultModel> responseBody = new ArrayList<>();

        // check
        // productGroups is empty return empty response
        if (CollectionUtils.isEmpty(productGroups)) {
            success(new ArrayList<>());
        }

        List<CmsBtCAdProudctModel> cmsMtCAdProudcts = JacksonUtil.jsonToBeanList(JacksonUtil.bean2Json(productGroups),CmsBtCAdProudctModel.class);
        String response = cmsBtCAdProductService.updateProduct(channelId,cmsMtCAdProudcts);

        if(!StringUtil.isEmpty(response)){
            responseBody = JacksonUtil.jsonToBeanList(response,ProductGroupResultModel.class);
        }

        return success(responseBody);
    }

    public ActionResponse updateQuantityPrice(List<ProductGroupRequest> productGroups) {
        String channelId = getClientChannelId();
        List<ProductGroupResultModel> responseBody = new ArrayList<>();

        // check
        // productGroups is empty return empty response
        if (CollectionUtils.isEmpty(productGroups)) {
            success(new ArrayList<>());
        }

        List<CmsBtCAdProudctModel> cmsMtCAdProudcts = JacksonUtil.jsonToBeanList(JacksonUtil.bean2Json(productGroups),CmsBtCAdProudctModel.class);
        String response = cmsBtCAdProductService.updateQuantityPrice(channelId, cmsMtCAdProudcts);
        if(!StringUtil.isEmpty(response)){
            responseBody = JacksonUtil.jsonToBeanList(response,ProductGroupResultModel.class);
        }
        return success(responseBody);
    }

    public ActionResponse updateStatus(List<ProductGroupRequest> productGroups) {
        String channelId = getClientChannelId();
        List<ProductGroupResultModel> responseBody = new ArrayList<>();

        // check
        // productGroups is empty return empty response
        if (CollectionUtils.isEmpty(productGroups)) {
            success(new ArrayList<>());
        }

        // TODO: 根据实际的业务处理

        return success(responseBody);
    }

}
