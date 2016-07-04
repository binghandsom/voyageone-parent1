package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */

@RestController
@RequestMapping(
        value  = CmsUrlConstants.POP.FIELD_EDIT.ROOT,
        method = RequestMethod.POST
)
public class CmsFieldEditController extends CmsController {

    @Autowired
    private CmsFieldEditService propChangeService;

    /**
     * 获取pop画面options.
     */
    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.GET_POP_OPTIONS)
    public AjaxResponse getPopOptions(){
        List<CmsMtCommonPropDefModel> result = propChangeService.getPopOptions(getLang(), getUser().getSelChannelId());
        return success(result);
    }

    /**
     * 批量修改属性.
     */
    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.SET_PRODUCT_FIELDS)
    public AjaxResponse setProductFields(@RequestBody Map<String, Object> params) {
        CmsSessionBean cmsSession = getCmsSession();
        String prop = (String) params.get("_option");
        if (prop != null) {
            if ("approval".equals(prop)) {
                // 商品审批
                Map<String, Object> rs = propChangeService.setProductApproval(params, getUser(), cmsSession);
                return success(rs);
            } else if ("putonoff".equals(prop)) {
                Map<String, Object> rs = propChangeService.setProductOnOff(params, getUser(), cmsSession);
                return success(rs);
            } else if ("saleprice".equals(prop)) {
                Map<String, Object> rs = propChangeService.setProductSalePrice(params, getUser(), cmsSession);
                return success(rs);
            }
            return success(null);
        }
        Integer cartId = (Integer) params.get("cartId");
        int cartIdVal = 0;
        if (cartId == null) {
            cartIdVal = Integer.valueOf(cmsSession.getPlatformType().get("cartId").toString());
        } else {
            cartIdVal = cartId;
        }

        Map<String, Object> rs = propChangeService.setProductFields(params, getUser(), cartIdVal, cmsSession);
        return success(rs);
    }
}
