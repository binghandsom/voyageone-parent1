package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.vms.channeladvisor.product.ProductGroupResultModel;
import com.voyageone.service.impl.vms.feed.CAFeedProductService;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProductModel;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.service.CAProductService;
import com.voyageone.service.bean.vms.channeladvisor.product.ProductGroupModel;
import com.voyageone.service.bean.vms.channeladvisor.request.ProductGroupRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
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
    private CAFeedProductService caFeedProductService;

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

        List<CmsBtCAdProductModel> cmsMtCAdProudcts = JacksonUtil.jsonToBeanList(JacksonUtil.bean2Json(productGroups),CmsBtCAdProductModel.class);
        String response = caFeedProductService.updateProduct(channelId,cmsMtCAdProudcts);

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

        List<CmsBtCAdProductModel> cmsMtCAdProudcts = JacksonUtil.jsonToBeanList(JacksonUtil.bean2Json(productGroups),CmsBtCAdProductModel.class);
        String response = caFeedProductService.updateQuantityPrice(channelId, cmsMtCAdProudcts);
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
