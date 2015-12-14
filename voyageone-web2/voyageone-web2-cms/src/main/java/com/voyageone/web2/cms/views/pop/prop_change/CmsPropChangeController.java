package com.voyageone.web2.cms.views.pop.prop_change;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROP;
import com.voyageone.cms.service.model.CmsMtCommonPropDefModel;
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
        value  = PROP.CHANGE.ROOT,
        method = RequestMethod.POST
)
public class CmsPropChangeController extends CmsController {

    @Autowired
    private CmsPropChangeService propChangeService;

    /**
     * 获取pop画面options.
     */
    @RequestMapping(PROP.CHANGE.GET_POP_OPTIONS)
    public AjaxResponse getPopOptions(){
        String channel_id = getUser().getSelChannelId();
        List<CmsMtCommonPropDefModel> result = propChangeService.getPopOptions(channel_id);
        return success(result);
    }

    /**
     * 批量修改属性.
     */
    @RequestMapping(PROP.CHANGE.SET_PRODUCT_FIELDS)
    public AjaxResponse setProductFields(@RequestBody Map<String, Object> params) {
        String user_name = getUser().getUserName();
        propChangeService.setProductFields(params, user_name);
        return success(true);
    }
}
