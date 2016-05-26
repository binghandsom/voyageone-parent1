package com.voyageone.web2.cms.views.system.cart;

import com.google.common.base.Preconditions;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.CartService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/15 10:10
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RestController
@RequestMapping(value = "/cms/system/cart", method = {RequestMethod.POST})
public class CmsCartController extends CmsController {

    @Resource
    CartService cartService;

    @Autowired
    CmsCartService cmsCartService;


    @RequestMapping("list")
    public AjaxResponse doList(@RequestBody Map<String,String> params) {

        CartBean bean = new CartBean();
        if (!StringUtils.isEmpty(params.get("cart_id"))) {
            bean.setCart_id(params.get("cart_id"));
        }
        if (!StringUtils.isEmpty(params.get("name"))) {
            bean.setName(params.get("name"));
        }
        if (!StringUtils.isEmpty(params.get("cart_type"))) {
            bean.setCart_type(params.get("cart_type"));
        }
        if (!StringUtils.isEmpty(params.get("active"))) {
            bean.setActive(params.get("active"));
        }
        List<CartBean> data = cartService.getCarts(bean);
        return success(Page.fromMap(params).withData(data));

    }

    @RequestMapping("delete")
    public AjaxResponse delete(@RequestBody CartBean con) {
        Preconditions.checkArgument(!StringUtils.isNullOrBlank2(con.getCart_id()));
        con.setModifier(getUser().getUserName());
        cartService.deleteLogic(con);
        return success(true);
    }

    @RequestMapping("saveOrUpdate")
    public AjaxResponse saveOrUpdate(@RequestBody CartBean bean) {
        bean.setModifier(getUser().getUserName());
        cmsCartService.saveOrUpdate(bean);
        return success(true);
    }

    @RequestMapping("save")
    public AjaxResponse save(@RequestBody CartBean bean) {
        bean.setModifier(getUser().getUserName());
        cmsCartService.save(bean);
        return success(true);
    }


}
