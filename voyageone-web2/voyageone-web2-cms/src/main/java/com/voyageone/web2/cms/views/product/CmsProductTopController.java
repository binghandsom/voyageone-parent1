package com.voyageone.web2.cms.views.product;

import com.voyageone.service.bean.cms.producttop.ProductTopPageParameter;
import com.voyageone.service.impl.cms.product.ProductTopService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dell on 2016/11/28.
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.ProductTop.ROOT)
public class CmsProductTopController extends CmsController {

    @Autowired
    ProductTopService service;

    @RequestMapping(CmsUrlConstants.ProductTop.GetPage)
    public AjaxResponse getPage(@RequestBody ProductTopPageParameter param) {

        return success(service.getPage(param, getUser().getSelChannelId()));
    }

    @RequestMapping(CmsUrlConstants.ProductTop.GetCount)
    public Object getCount(@RequestBody ProductTopPageParameter param) {

        return success(service.getCount(param, getUser().getSelChannelId()));
    }

}
