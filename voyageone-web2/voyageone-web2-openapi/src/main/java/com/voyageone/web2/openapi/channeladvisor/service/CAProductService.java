package com.voyageone.web2.openapi.channeladvisor.service;

import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.sdk.api.channeladvisor.domain.ProductGroupModel;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ResponseStatusEnum;
import com.voyageone.web2.sdk.api.channeladvisor.exception.ErrorModel;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CAProductService extends CAOpenApiBaseService {

    public ActionResponse getProducts(String groupFields, String buyableFields) {

        System.out.println(getClientChannelId());

        ActionResponse response = new ActionResponse();

        List<ProductGroupModel> models = new ArrayList<>();
        // TODO: 2016/9/7 此处models需要根据请求数据进行处理获得，调用其他api获取model数据
        if (StringUtils.isEmpty(groupFields)) ;
        // TODO: 2016/9/7 根据api描述，如果入参groupFields为空，那么响应group 里边的fields为空
        if (StringUtils.isEmpty(buyableFields)) ;
        // TODO: 2016/9/7 如果 buyablefileds为空，那么响应buyable里边的fields为空
        List<ErrorModel> errors = new ArrayList<>();
        // TODO: 2016/9/7 error根据实际的业务处理情况构造

        response.setResponseBody(models);
        response.setStatus(ResponseStatusEnum.Complete);
        response.setPendingUri(null);
        response.setErrors(errors);
        response.setHasErrors(!CollectionUtils.isEmpty(errors));
        return response;
    }
}
