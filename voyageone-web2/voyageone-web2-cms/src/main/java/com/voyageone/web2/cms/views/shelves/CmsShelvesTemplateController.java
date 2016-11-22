package com.voyageone.web2.cms.views.shelves;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelClientType;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelTemplateType;
import com.voyageone.service.impl.cms.CmsBtShelvesTemplateService;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rex.wu on 2016/11/14.
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.SHELVES.TEMPLATE.ROOT)
public class CmsShelvesTemplateController extends CmsController {

    @Autowired
    private CmsBtShelvesTemplateService cmsBtShelvesTemplateService;

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.INIT)
    public AjaxResponse init(@RequestBody CmsBtShelvesTemplateBean searchBean) {
        searchBean.setChannelId(getUser().getSelChannelId());
        UserSessionBean user = getUser();
        Map<String, String> carts = null;
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(user.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang());
        if (CollectionUtils.isNotEmpty(cartList)) {
            carts = new HashMap<String, String>();
            for (TypeChannelBean cart:cartList) {
                carts.put(cart.getValue(), cart.getName());
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("carts", carts);
        resultMap.put("searBean", searchBean);
        resultMap.put("clientTypes", CmsBtShelvesTemplateModelClientType.KV);
        resultMap.put("templateTypes", CmsBtShelvesTemplateModelTemplateType.KV);
        resultMap.put("templates", cmsBtShelvesTemplateService.search(searchBean));
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.SEARCH)
    public AjaxResponse search(@RequestBody CmsBtShelvesTemplateBean searchBean) {
        searchBean.setChannelId(getUser().getSelChannelId());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("templates", cmsBtShelvesTemplateService.search(searchBean));
        return success(resultMap);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.ADD)
    public AjaxResponse addShelvesTemplate(@RequestBody CmsBtShelvesTemplateModel model){
        cmsBtShelvesTemplateService.insert(model, getUser().getUserName(), getUser().getSelChannelId());
        return success("");
    }

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.EDIT)
    public AjaxResponse editShelvesTemplate(@RequestBody Map<String, Object> params){
        CmsBtShelvesTemplateModel model = JsonUtil.jsonToBean(JacksonUtil.bean2Json(params), CmsBtShelvesTemplateModel.class);
        String clearVal = (String) params.get("clear");
        Integer clear = 0;
        if (!StringUtils.isEmpty(clearVal) && StringUtils.isNumeric(clearVal)) {
            clear = Integer.valueOf(clearVal);
        }
        clear = clear == 0 ? clear : 1;
        cmsBtShelvesTemplateService.update(model, getUser().getUserName(), clear);
        return success("");
    }

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.DETAIL)
    public AjaxResponse detail(@RequestBody Map<String, String> params){
        String templateId = params.get("templateId");
        return success(cmsBtShelvesTemplateService.selectById(Integer.parseInt(templateId)));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.DELETE)
    public AjaxResponse deleteShelvesTemplate(@RequestBody Map<String, String> params){
        String id = params.get("templateId");
        cmsBtShelvesTemplateService.inactive(id, getUser().getUserName(), getUser().getSelChannelId());
        return success("");
    }
}
