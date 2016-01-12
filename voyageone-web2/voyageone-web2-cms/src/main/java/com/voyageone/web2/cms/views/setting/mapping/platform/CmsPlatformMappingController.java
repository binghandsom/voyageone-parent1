package com.voyageone.web2.cms.views.setting.mapping.platform;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 平台到主数据的类目匹配
 *
 * @author Jonas, 1/11/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.MAPPING.PLATFORM.ROOT, method = RequestMethod.POST)
public class CmsPlatformMappingController extends CmsController {

    @Autowired
    private CmsPlatformMappingService platformMappingService;

    @RequestMapping(CmsUrlConstants.MAPPING.PLATFORM.GET_MAIN_CATEGORY)
    public AjaxResponse getMainFinalCategoryMap(@RequestBody Map<String, Integer> params) {

        Integer cartId = params.get("cartId");

        return success(platformMappingService.getMainFinalCategoryMap(cartId, getUser()));
    }

    @RequestMapping(CmsUrlConstants.MAPPING.PLATFORM.GET_OTHER_MAPPING_PATH)
    public AjaxResponse getOtherPlatformMapping(@RequestBody Map<String, String> params) {

        String mainCategoryId = params.get("mainCategoryId");

        return success(platformMappingService.getOtherPlatformMapping(mainCategoryId, getUser()));
    }

    @RequestMapping(CmsUrlConstants.MAPPING.PLATFORM.GET_PLATFORM_CATEGORY)
    public AjaxResponse getPlatformCategories(@RequestBody Map<String, Integer> params) {

        Integer cartId = params.get("cartId");

        return success(platformMappingService.getPlatformCategories(getUser(), cartId));
    }
}
