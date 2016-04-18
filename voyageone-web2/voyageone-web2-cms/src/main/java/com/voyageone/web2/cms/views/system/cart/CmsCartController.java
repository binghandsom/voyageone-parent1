package com.voyageone.web2.cms.views.system.cart;

import com.google.common.base.Preconditions;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.CartService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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


    @RequestMapping("list")
    public AjaxResponse doList(@RequestBody CartBean con) {

        List<CartBean> data = cartService.getListBy(con);
        return success(data);

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
        checkCart(bean);
        bean.setModifier(getUser().getUserName());
        cartService.saveOrUpdate(bean);
        return success(true);
    }

    private void checkCart(@RequestBody CartBean bean) {
        Preconditions.checkArgument(org.apache.commons.lang3.StringUtils.isNotBlank(bean.getCart_id()), "cart_id不能为空");
        Preconditions.checkArgument(org.apache.commons.lang3.StringUtils.isNotBlank(bean.getName()), "cart_name不能为空");
        Preconditions.checkArgument(org.apache.commons.lang3.StringUtils.isNotBlank(bean.getShort_name()), "short_name不能为空");
        Preconditions.checkArgument(org.apache.commons.lang3.StringUtils.isNotBlank(bean.getPlatform_id()), "platform_id不能为空");
        Preconditions.checkArgument(org.apache.commons.lang3.StringUtils.isNotBlank(bean.getCart_type()), "cart_type不能为空");
    }


    @RequestMapping("save")
    public AjaxResponse save(@RequestBody CartBean bean) {
        checkCart(bean);
        cartService.save(bean);
        return success(true);
    }


}
