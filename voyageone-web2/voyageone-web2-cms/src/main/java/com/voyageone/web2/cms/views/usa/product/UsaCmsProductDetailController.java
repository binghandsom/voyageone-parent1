package com.voyageone.web2.cms.views.usa.product;

import com.voyageone.service.impl.cms.usa.UsaProductDetailService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.views.usa.UsaCmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(UsaCmsUrlConstants.PRODUCT.GET_PRODUCT_INFO)
    public AjaxResponse doGetMastProductInfo(@RequestBody Map requestMap) {
        Long prodId = Long.parseLong(String.valueOf(requestMap.get("prodId")));
        String channelId = getUser().getSelChannelId();
        return success(usaProductDetailService.getMastProductInfo(channelId, prodId));
    }
}
