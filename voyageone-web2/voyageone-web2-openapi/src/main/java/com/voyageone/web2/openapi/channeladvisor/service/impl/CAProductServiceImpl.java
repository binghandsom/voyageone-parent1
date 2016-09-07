package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.service.CAProductService;
import com.voyageone.web2.sdk.api.channeladvisor.domain.ProductGroupModel;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ResponseStatusEnum;
import com.voyageone.web2.sdk.api.channeladvisor.exception.ErrorModel;
import com.voyageone.web2.sdk.api.channeladvisor.request.ProductGroupRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("product")
public class CAProductServiceImpl extends CAOpenApiBaseService implements CAProductService {

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

    public ActionResponse updateProducts(List<ProductGroupRequest> request) {
        if (CollectionUtils.isEmpty(request)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        String jsonData = "{\"ResponseBody\":[{\"SellerSKU\":\"REBEL X-WING\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"REBEL X-WING\",\"MarketPlaceItemID\":\"REBEL X-WING\",\"URL\":\"http://your-url.com/products/REBEL X-WING\",\"Errors\":null}],\"Errors\":null},{\"SellerSKU\":\"LIGHTSABER\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"MarketPlaceItemID\":\"LIGHTSABER_RED_MED\",\"URL\":\"http://your-url.com/products/LIGHTSABER_RED_MED\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"MarketPlaceItemID\":\"LIGHTSABER_RED_LG\",\"URL\":\"http://your-url.com/products/LIGHTSABER_RED_LG\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"MarketPlaceItemID\":\"LIGHTSABER_BLUE_MED\",\"URL\":\"http://your-url.com/products/LIGHTSABER_BLUE_MED\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"MarketPlaceItemID\":\"LIGHTSABER_BLUE_LG\",\"URL\":\"http://your-url.com/products/LIGHTSABER_BLUE_LG\",\"Errors\":null}],\"Errors\":null}],\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse updateQuantityPrice(List<ProductGroupRequest> request) {
        if (CollectionUtils.isEmpty(request)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        String jsonData = "{\"ResponseBody\":[{\"SellerSKU\":\"REBEL X-WING\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"REBEL X-WING\",\"MarketPlaceItemID\":\"REBEL X-WING\",\"URL\":\"http://your-url.com/products/REBEL X-WING\",\"Errors\":null}],\"Errors\":null},{\"SellerSKU\":\"LIGHTSABER\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"MarketPlaceItemID\":\"LIGHTSABER_RED_MED\",\"URL\":\"http://your-url.com/products/LIGHTSABER_RED_MED\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"MarketPlaceItemID\":\"LIGHTSABER_RED_LG\",\"URL\":\"http://your-url.com/products/LIGHTSABER_RED_LG\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"MarketPlaceItemID\":\"LIGHTSABER_BLUE_MED\",\"URL\":\"http://your-url.com/products/LIGHTSABER_BLUE_MED\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"MarketPlaceItemID\":\"LIGHTSABER_BLUE_LG\",\"URL\":\"http://your-url.com/products/LIGHTSABER_BLUE_LG\",\"Errors\":null}],\"Errors\":null}],\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

    public ActionResponse updateStatus(List<ProductGroupRequest> request) {
        if (CollectionUtils.isEmpty(request)) ;
        // TODO: 2016/9/7 空值处理 logger或其他
        String jsonData = "{\"ResponseBody\":[{\"SellerSKU\":\"REBEL X-WING\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"REBEL X-WING\",\"MarketPlaceItemID\":null,\"URL\":null,\"Errors\":[]}],\"Errors\":[]},{\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"MarketPlaceItemID\":null,\"URL\":null,\"Errors\":[]}],\"Errors\":[]},{\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"MarketPlaceItemID\":null,\"URL\":null,\"Errors\":[]}],\"Errors\":[]},{\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"MarketPlaceItemID\":null,\"URL\":null,\"Errors\":[]}],\"Errors\":[]},{\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"MarketPlaceItemID\":null,\"URL\":null,\"Errors\":[]}],\"Errors\":[]}],\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        // TODO: 2016/9/7 获取jsonbody 响应 mock response
        return JacksonUtil.json2Bean(jsonData, ActionResponse.class);
    }

}
