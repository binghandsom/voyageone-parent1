package com.voyageone.web2.cms.views.pop.jmPromotion;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.channel.CmsBlackBrandParamBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author piao
 * @version 2.8.0
 * @since 2.8.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.BLACK_BRAND.ROOT, method = RequestMethod.POST)
public class JmImageUploadController extends CmsController {


    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.INIT)
    public AjaxResponse init(@RequestBody CmsBlackBrandParamBean blackBrandParamBean) {
        return success(new Object());
    }


}

