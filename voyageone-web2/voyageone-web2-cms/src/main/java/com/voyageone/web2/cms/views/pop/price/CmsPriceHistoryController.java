package com.voyageone.web2.cms.views.pop.price;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.POP.PRICE.ROOT,
        method = RequestMethod.POST
)
public class CmsPriceHistoryController extends CmsController {

    @Autowired
    private CmsPriceHistoryService cmsPriceHistoryService;

    @RequestMapping(CmsUrlConstants.POP.PRICE.GET_PRICE_HISTORY)
    public AjaxResponse getPriceHistory(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = cmsPriceHistoryService.getPriceHistory(params, getUser().getSelChannelId());
        return success(result);
    }

}
