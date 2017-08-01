package com.voyageone.web2.cms.views.usa;

import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.usa.UsaTagService;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * USA CMS2 Tag管理器
 *
 * @Author rex.wu
 * @Create 2017-07-19 11:26
 */
@RestController
@RequestMapping(value = UsaCmsUrlConstants.TAG.ROOT)
public class UsaCmsChannelTagController extends BaseController {

    @Autowired
    private UsaTagService usaTagService;

    /**
     * 统一的当前语言环境提供
     */
    @Override
    public String getLang() {
        return "en";
    }

    @RequestMapping(value = UsaCmsUrlConstants.TAG.INIT)
    public AjaxResponse init(@RequestBody Map<String, Object> params) {

        return success(usaTagService.getInitTagInfo(getUser().getSelChannelId(), params, getLang()));
    }


}
