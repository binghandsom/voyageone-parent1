package com.voyageone.web2.cms.views.product;

import com.voyageone.service.bean.cms.producttop.GetTopListParameter;
import com.voyageone.service.bean.cms.producttop.ProductInfo;
import com.voyageone.service.bean.cms.producttop.ProductPageParameter;
import com.voyageone.service.impl.cms.product.ProductTopService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dell on 2016/11/28.
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.ProductTop.ROOT)
public class CmsProductTopController extends CmsController {

    @Autowired
    ProductTopService service;

    @RequestMapping(CmsUrlConstants.ProductTop.GetPage)
    public AjaxResponse getPage(@RequestBody ProductPageParameter param) {

        return success(service.getPage(param, getUser().getSelChannelId()));
    }

    @RequestMapping(CmsUrlConstants.ProductTop.GetCount)
    public Object getCount(@RequestBody ProductPageParameter param) {

        return success(service.getCount(param, getUser().getSelChannelId()));
    }
    @RequestMapping(CmsUrlConstants.ProductTop.GetTopList)
    public AjaxResponse getTopList(GetTopListParameter parameter) {
        return success(service.getTopList(parameter, getUser().getSelChannelId()));

    }


    }
