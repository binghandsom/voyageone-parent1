package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.UrlConstants;
import com.voyageone.web2.openapi.channeladvisor.service.CAProductService;
import com.voyageone.web2.sdk.api.channeladvisor.request.ProductGroupRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * ProductController
 *
 * @author chuanyu.liang, 16/09/06
 * @version 2.0.0
 * @since 2.0.0
 */

@RestController
@RequestMapping(
        value = UrlConstants.ROOT
)
public class CAProductController extends OpenApiBaseController {

    @Autowired
    private CAProductService productService;

    @RequestMapping(value = UrlConstants.PRODUCTS.GET_PRODUCTS, method = RequestMethod.GET)
    public ActionResponse getProducts(HttpServletRequest request) {
        return productService.getProducts(request.getParameter("groupFields"),request.getParameter("buyableFields"));
    }

    @RequestMapping(value = UrlConstants.PRODUCTS.UPDATE_PRODUCTS, method = RequestMethod.POST)
    public ActionResponse updateProducts(@RequestBody ProductGroupRequest request) {


        //check param
        if(CollectionUtils.isEmpty(request.getBuyableProducts()))
            ;// TODO: 2016/9/7 非空校验
            // if()
        // call service

        String jsonData="{\"ResponseBody\":[{\"SellerSKU\":\"REBEL X-WING\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"REBEL X-WING\",\"MarketPlaceItemID\":\"REBEL X-WING\",\"URL\":\"http://your-url.com/products/REBEL X-WING\",\"Errors\":null}],\"Errors\":null},{\"SellerSKU\":\"LIGHTSABER\",\"BuyableProductResults\":[{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_MED\",\"MarketPlaceItemID\":\"LIGHTSABER_RED_MED\",\"URL\":\"http://your-url.com/products/LIGHTSABER_RED_MED\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_RED_LG\",\"MarketPlaceItemID\":\"LIGHTSABER_RED_LG\",\"URL\":\"http://your-url.com/products/LIGHTSABER_RED_LG\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_MED\",\"MarketPlaceItemID\":\"LIGHTSABER_BLUE_MED\",\"URL\":\"http://your-url.com/products/LIGHTSABER_BLUE_MED\",\"Errors\":null},{\"RequestResult\":\"Success\",\"SellerSKU\":\"LIGHTSABER_BLUE_LG\",\"MarketPlaceItemID\":\"LIGHTSABER_BLUE_LG\",\"URL\":\"http://your-url.com/products/LIGHTSABER_BLUE_LG\",\"Errors\":null}],\"Errors\":null}],\"Status\":\"Complete\",\"PendingUri\":null,\"Errors\":[]}";
        return JacksonUtil.json2Bean(jsonData,ActionResponse.class);
    }

    @RequestMapping(value = UrlConstants.PRODUCTS.UPDATE_QUANTITY_PRICE, method = RequestMethod.POST)
    public ActionResponse updateQuantityPrice(@RequestBody ProductGroupRequest request) {


        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.PRODUCTS.UPDATE_STATUS, method = RequestMethod.GET)
    public ActionResponse updateStatus(@RequestBody ProductGroupRequest request) {


        //check param

        // call service


        return null;
    }

}
