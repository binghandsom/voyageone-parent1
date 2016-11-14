package com.voyageone.web2.cms.views.shelves;

import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.impl.cms.CmsBtShelvesTemplateService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/14.
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.SHELVES.TEMPLATE.ROOT)
public class CmsShelvesTemplateController extends CmsController {

    @Autowired
    private CmsBtShelvesTemplateService cmsBtShelvesTemplateService;

    @RequestMapping(CmsUrlConstants.SHELVES.TEMPLATE.INIT)
    public AjaxResponse init() {
        CmsBtShelvesTemplateBean searBean = new CmsBtShelvesTemplateBean();
        searBean.setChannelId(getUser().getSelChannelId());
        return success(cmsBtShelvesTemplateService.search(searBean));
    }
}
