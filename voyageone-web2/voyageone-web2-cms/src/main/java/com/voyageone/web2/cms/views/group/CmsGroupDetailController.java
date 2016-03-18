package com.voyageone.web2.cms.views.group;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/1/14
 */
@RestController
@RequestMapping(value = CmsUrlConstants.GROUP.DETAIL.ROOT, method = RequestMethod.POST)
public class CmsGroupDetailController extends CmsController {

    @Autowired
    private CmsGroupDetailService cmsGroupListService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.GROUP.DETAIL.INIT)
    public AjaxResponse init(@RequestBody Map<String, Object> params) throws Exception {

        Map<String, Object> resultBean = new HashMap<>();

        // 获取master数据
        resultBean.put("masterData", cmsGroupListService.getMasterData(getUser(), getLang()));

        // 返回product数据
        List<CmsBtProductModel> productList = cmsGroupListService.getProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList);
        long totalCount = cmsGroupListService.getProductCnt(params, getUser(), getCmsSession());
        resultBean.put("productListTotal", totalCount);

        List<CmsBtProductModel> productIds = cmsGroupListService.getProductIdList(params, getUser(), getCmsSession());
        resultBean.put("productIds", productIds);

        return success(resultBean);
    }

    /**
     * 分页检索product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.GROUP.DETAIL.GET_PRODUCT_LIST)
    public AjaxResponse getProductList(@RequestBody Map<String, Object> params) {

        Map<String, Object> resultBean = new HashMap<>();

        List<CmsBtProductModel> productList = cmsGroupListService.getProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList);
        long totalCount = cmsGroupListService.getProductCnt(params, getUser(), getCmsSession());
        resultBean.put("productListTotal", totalCount);

        List<CmsBtProductModel> productIds = cmsGroupListService.getProductIdList(params, getUser(), getCmsSession());
        resultBean.put("productIds", productIds);

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 设置该group的主商品
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.GROUP.DETAIL.SET_MAIN_PRODUCT)
    public AjaxResponse setMainProduct(@RequestBody Map<String, Object> params) {

        UserSessionBean userSession = super.getUser();
        Map<String, Object> resultBean = cmsGroupListService.updateMainProduct(params,userSession);

        // 返回用户信息
        return success(resultBean);
    }

}
