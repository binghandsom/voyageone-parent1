package com.voyageone.web2.cms.views.usa.product;

import com.voyageone.service.impl.cms.usa.UsaProductDetailService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.views.search.CmsAdvSearchOtherService;
import com.voyageone.web2.cms.views.usa.UsaCmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 2017/7/18.
 *
 */
@RestController
@RequestMapping(value = UsaCmsUrlConstants.PRODUCT.ROOT)
public class UsaCmsProductDetailController extends CmsController {

    private final UsaProductDetailService usaProductDetailService;

    @Autowired
    public UsaCmsProductDetailController(UsaProductDetailService usaProductDetailService) {
        this.usaProductDetailService = usaProductDetailService;
    }
    @Autowired
    private CmsAdvSearchOtherService advSearchOtherService;

    @RequestMapping(UsaCmsUrlConstants.PRODUCT.GET_PRODUCT_INFO)
    public AjaxResponse doGetMastProductInfo(@RequestBody Map requestMap) {

        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String channelId = getUser().getSelChannelId();
        return success(usaProductDetailService.getMastProductInfo(channelId, prodId));
    }

    @RequestMapping(UsaCmsUrlConstants.PRODUCT.GET_PRODUCT_PLATFORM)
    public AjaxResponse doGetProductPlatform(@RequestBody Map requestMap) {

        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        Integer cartId = (Integer) requestMap.get("cartId");

        String channelId = getUser().getSelChannelId();
        return success(usaProductDetailService.getProductPlatform(channelId, prodId, cartId));
    }

    @RequestMapping(UsaCmsUrlConstants.PRODUCT.UPDATE_COMMON_PRODUCT_INFO)
    public AjaxResponse doUpdateCommonProductInfo(@RequestBody Map requestMap) {

        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));

        String channelId = getUser().getSelChannelId();

        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        Map<String, Object> platform = (Map<String, Object>) dataMap.get("platform");
        Map<String, Object> productComm = (Map<String, Object>) dataMap.get("productComm");
        usaProductDetailService.updateCommonProductInfo(channelId, prodId, productComm, getUser().getUserName());
        usaProductDetailService.updateProductPlatform(channelId, prodId, platform, getUser().getUserName());
        return success(true);
    }

    //根据productCode获取中国和美国的平台价格信息
    @RequestMapping(value = UsaCmsUrlConstants.PRODUCT.GET_ALL_PLATFORMS_PRICE)
    public AjaxResponse getAllPlatformsPrice(@RequestBody Long prodId) {
        UserSessionBean user = getUser();
        HashMap<String, Map<String, Double>> allPlatformsPrice = usaProductDetailService.getAllPlatformsPrice(prodId, user.getSelChannelId());
        // 返回用户信息
        return success(allPlatformsPrice);
    }

    //单个修改价格
    @RequestMapping(value = UsaCmsUrlConstants.PRODUCT.UPDATE_ONE_PRICE)
    public AjaxResponse updateOnePrice(@RequestBody Map params) {
        UserSessionBean user = getUser();
        usaProductDetailService.updatePrice(params,user.getSelChannelId(),user.getUserName());
        // 返回用户信息
        return success(null);
    }
}
