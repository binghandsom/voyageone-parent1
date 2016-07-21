package com.voyageone.web2.cms.views.product;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sunpeitao
 * @version 2.3.0
 * @since 2.3.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.StatusHistory.ROOT)
public class CmsProductStatusHistoryController extends CmsController {

    private final ProductStatusHistoryService service;

    @Autowired
    public CmsProductStatusHistoryController(ProductStatusHistoryService service) {
        this.service = service;
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.StatusHistory.GetPage)
    public AjaxResponse getPage(@RequestBody PageQueryParameters parameters) {
        parameters.put("channelId", getUser().getSelChannelId());
        return success(service.getPage(parameters));
    }
    @RequestMapping(CmsUrlConstants.PRODUCT.StatusHistory.GetCount)
    public AjaxResponse getCount(@RequestBody PageQueryParameters parameters) {
        parameters.put("channelId", getUser().getSelChannelId());
        return success(service.getCount(parameters));
    }
}
