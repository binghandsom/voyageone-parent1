package com.voyageone.web2.cms.views.system.platform;

import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/18 10:48
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RestController
@RequestMapping(value = "/cms/system/platform", method = {RequestMethod.POST})
public class CmsPlatformController extends CmsController{

    @Resource
    PlatformService platformService;

    @RequestMapping("list")
    public AjaxResponse list() {
        return success(platformService.getAll());
    }
}
