package com.voyageone.web2.cms.views.setting.mapping.platform;

import com.taobao.top.schema.exception.TopSchemaException;
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

    @Autowired
    private CmsPlatformPropMappingService platformPropMappingService;

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

//    @RequestMapping(CmsUrlConstants.MAPPING.PLATFORM.GET_PLATFORM_CATEGORIES)
    public AjaxResponse getPlatformCategories(@RequestBody Map<String, Integer> params) {

        Integer cartId = params.get("cartId");

        return success(platformMappingService.getPlatformCategories(getUser(), cartId));
    }

    @RequestMapping(CmsUrlConstants.MAPPING.PLATFORM.SET_PLATFORM_MAPPING)
    public AjaxResponse setPlatformMapping(@RequestBody Map<String, Object> params) {

        String from = (String) params.get("from");
        String to = (String) params.get("to");
        Integer cartId = (Integer) params.get("cartId");

        platformMappingService.setPlatformMapping(from, to, cartId, getUser());

        return success(true);
    }

    @RequestMapping(CmsUrlConstants.MAPPING.PLATFORM.GET_PLATFORM_CATEGORY)
    public AjaxResponse getPlatformCategory(@RequestBody Map<String, Object> params) throws TopSchemaException {

        String categoryId = (String) params.get("categoryId");

        Integer cartId = (Integer) params.get("cartId");

        return success(platformPropMappingService.getPlatformCategory(categoryId, cartId, getUser()));
    }
}
