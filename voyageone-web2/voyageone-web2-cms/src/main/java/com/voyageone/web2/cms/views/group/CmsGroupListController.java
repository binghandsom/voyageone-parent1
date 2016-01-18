package com.voyageone.web2.cms.views.group;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/1/14
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.GROUP.LIST.ROOT,
        method = RequestMethod.POST
)
public class CmsGroupListController extends CmsController {

    @Autowired
    private CmsGroupListService cmsGroupListService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.INDEX.INIT)
    public AjaxResponse init(@RequestBody Map<String, Object> params) throws Exception {

        Map<String, Object> resultBean = new HashMap<>();

        // 获取master数据
        resultBean.put("masterData", cmsGroupListService.getMasterData(getUser(), getLang()));

        // 返回product数据
        ProductsGetResponse productList = cmsGroupListService.GetProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList.getProducts());
        resultBean.put("productListTotal", productList.getTotalCount());

        ProductsGetResponse productIds = cmsGroupListService.GetProductIdList(params, getUser(), getCmsSession());
        resultBean.put("productIds", productIds.getProducts());

        return success(resultBean);
    }

    /**
     * 分页检索product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.GROUP.LIST.GET_PRODUCT_LIST)
    public AjaxResponse getProductList(@RequestBody Map<String, Object> params) {

        Map<String, Object> resultBean = new HashMap<>();

        ProductsGetResponse productList = cmsGroupListService.GetProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList.getProducts());
        resultBean.put("productListTotal", productList.getTotalCount());

        ProductsGetResponse productIds = cmsGroupListService.GetProductIdList(params, getUser(), getCmsSession());
        resultBean.put("productIds", productIds.getProducts());

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 设置该group的主商品
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.GROUP.LIST.SET_MAIN_PRODUCT)
    public AjaxResponse setMainProduct(@RequestBody Map<String, Object> params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 返回用户信息
        return success(resultBean);
    }

}
