package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.UrlConstants;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.channeladvisor.request.ProductGroupRequest;
import org.springframework.web.bind.annotation.*;


/**
 * ProductController
 *
 * @author chuanyu.liang, 16/09/06
 * @version 2.0.0
 * @since 2.0.0
 */

@RestController
@RequestMapping(
        value = UrlConstants.PRODUCTS.ROOT
)
public class ProductController extends OpenApiBaseController {

    @RequestMapping(value = UrlConstants.PRODUCTS.GET_PRODUCTS, method = RequestMethod.GET)
    public VoApiResponse getProducts(@RequestParam String groupFields, @RequestParam String buyableFields) {


        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.PRODUCTS.UPDATE_PRODUCTS, method = RequestMethod.POST)
    public VoApiResponse updateProducts(@RequestBody ProductGroupRequest request) {


        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.PRODUCTS.UPDATE_QUANTITY_PRICE, method = RequestMethod.POST)
    public VoApiResponse updateQuantityPrice(@RequestBody ProductGroupRequest request) {


        //check param

        // call service


        return null;
    }

    @RequestMapping(value = UrlConstants.PRODUCTS.UPDATE_STATUS, method = RequestMethod.GET)
    public VoApiResponse updateStatus(@RequestBody ProductGroupRequest request) {


        //check param

        // call service


        return null;
    }

}
