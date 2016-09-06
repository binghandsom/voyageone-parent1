package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.bi.constants.BiUrlConstants;
import com.voyageone.web2.openapi.channeladvisor.constants.UrlConstants;
import com.voyageone.web2.sdk.api.VoApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
        $info(BiUrlConstants.URL.LIST.SAVE_SHOP_URL_DATA);

        //check param

        // call service


        return null;
    }
}
