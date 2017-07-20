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

    //根据productCode获取中国和美国的平台价格信息
    @RequestMapping(value = UsaCmsUrlConstants.PRODUCT.GETALLPLATFORMSPRICE)
    public AjaxResponse getAllPlatformsPrice(@RequestBody Long prodId) {
        UserSessionBean user = getUser();
        HashMap<String, Map<String, Double>> allPlatformsPrice = advSearchOtherService.getAllPlatformsPrice(prodId, user);
        // 返回用户信息
        return success(allPlatformsPrice);
    }

    //单个修改价格
    @RequestMapping(value = UsaCmsUrlConstants.PRODUCT.UPDATEONEPRICE)
    public AjaxResponse updateOnePrice(@RequestBody Map params) {
        UserSessionBean user = getUser();
        advSearchOtherService.updateOnePrice(params, user);
        // 返回用户信息
        return success(null);
    }
}
