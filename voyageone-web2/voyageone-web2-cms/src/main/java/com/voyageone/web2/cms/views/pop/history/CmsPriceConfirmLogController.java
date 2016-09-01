package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.POP.PRICE_CONFIRM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by jonas on 9/1/16.
 *
 * @author jonas
 * @version 2.5.0
 * @since 2.5.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = PRICE_CONFIRM.ROOT)
public class CmsPriceConfirmLogController extends CmsController {

    private final CmsPriceConfirmLogViewService priceConfirmLogViewService;

    @Autowired
    public CmsPriceConfirmLogController(CmsPriceConfirmLogViewService priceConfirmLogViewService) {
        this.priceConfirmLogViewService = priceConfirmLogViewService;
    }

    @RequestMapping(PRICE_CONFIRM.PAGE)
    public AjaxResponse page(@RequestBody Map<String, Object> params) {

        String skuCode = (String) params.get("skuCode");
        String code = (String) params.get("code");
        int cartId = (int) params.get("cartId");
        String channelId = (String) params.get("channelId");
        int pageNumber = (int) params.get("pageNumber");
        int limit = (int) params.get("limit");

        return success(priceConfirmLogViewService.getPage(skuCode, code, cartId, channelId, pageNumber, limit));
    }
}
