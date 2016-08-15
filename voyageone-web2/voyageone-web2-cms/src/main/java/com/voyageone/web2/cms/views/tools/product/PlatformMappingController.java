package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PLATFORM_MAPPING;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 搜索, 获取和保存平台类目的属性匹配
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@RestController
@RequestMapping(value = PLATFORM_MAPPING.ROOT, method = RequestMethod.POST)
public class PlatformMappingController extends CmsController {


    @RequestMapping(PLATFORM_MAPPING.LIST)
    public AjaxResponse list(@RequestBody Map a) {







        return success(null);
    }

    @RequestMapping(PLATFORM_MAPPING.GET)
    public AjaxResponse get(@RequestBody Map a) {
        return success(null);
    }

    @RequestMapping(PLATFORM_MAPPING.SAVE)
    public AjaxResponse save(@RequestBody Map a) {
        return success(null);
    }
}
