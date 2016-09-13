package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import com.voyageone.web2.openapi.channeladvisor.service.CAProductService;
import com.voyageone.service.bean.vms.channeladvisor.request.ProductGroupRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * ProductController
 *
 * @author chuanyu.liang, 16/09/06
 * @version 2.0.0
 * @since 2.0.0
 */

@RestController
@RequestMapping(
        value = CAUrlConstants.ROOT
)
public class CAProductController extends OpenApiBaseController {

    @Autowired
    private CAProductService productService;

    /**
     * 批量获取产品
     *
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.PRODUCTS.GET_PRODUCTS, method = RequestMethod.GET)
    public ActionResponse getProducts(HttpServletRequest request) {
        return productService.getProducts(request.getParameter("groupFields"), request.getParameter("buyableFields"));
    }

    /**
     * 批量添加或者更新产品
     *
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.PRODUCTS.UPDATE_PRODUCTS, method = RequestMethod.POST)
    public ActionResponse updateProducts(@RequestBody List<ProductGroupRequest> request) {
        return productService.updateProducts(request);
    }

    /**
     * 批量更新数量或者价格
     *
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.PRODUCTS.UPDATE_QUANTITY_PRICE, method = RequestMethod.POST)
    public ActionResponse updateQuantityPrice(@RequestBody List<ProductGroupRequest> request) {
        return productService.updateQuantityPrice(request);
    }

    /**
     * 批量更新状态
     *
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.PRODUCTS.UPDATE_STATUS, method = RequestMethod.POST)
    public ActionResponse updateStatus(@RequestBody List<ProductGroupRequest> request) {
        return productService.updateStatus(request);
    }

}
