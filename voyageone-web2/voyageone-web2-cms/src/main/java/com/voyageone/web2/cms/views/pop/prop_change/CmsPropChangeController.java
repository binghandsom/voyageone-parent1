package com.voyageone.web2.cms.views.pop.prop_change;

import com.voyageone.cms.service.model.CmsMtCommonPropDefModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */

@RestController
@RequestMapping(
        value  = CmsUrlConstants.POP.PROP_CHANGE.ROOT,
        method = RequestMethod.POST
)
public class CmsPropChangeController extends CmsController {

    @Autowired
    private CmsPropChangeService propChangeService;

    /**
     * 获取pop画面options.
     */
    @RequestMapping(CmsUrlConstants.POP.PROP_CHANGE.GET_POP_OPTIONS)
    public AjaxResponse getPopOptions(){
        String channel_id = getUser().getSelChannelId();
        List<CmsMtCommonPropDefModel> result = propChangeService.getPopOptions(channel_id);
        return success(result);
    }

    /**
     * 批量修改属性.
     */
    @RequestMapping(CmsUrlConstants.POP.PROP_CHANGE.SET_PRODUCT_FIELDS)
    public AjaxResponse setProductFields(@RequestBody Map<String, Object> params) {
        propChangeService.setProductFields(params, getUser());
        return success(true);
    }
}
