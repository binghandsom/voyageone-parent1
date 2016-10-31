package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PLATFORM_MAPPING;
import com.voyageone.web2.cms.bean.tools.product.PlatformMappingSaveBean;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @version 2.9.0
 * @since 2.4.0
 */
@RestController
@RequestMapping(value = PLATFORM_MAPPING.ROOT, method = RequestMethod.POST)
public class PlatformMappingController extends CmsController {

    private final PlatformMappingService platformMappingService;

    private final PlatformMappingViewService platformMappingViewService;

    @Autowired
    public PlatformMappingController(PlatformMappingService platformMappingService,
                                     PlatformMappingViewService platformMappingViewService) {
        this.platformMappingService = platformMappingService;
        this.platformMappingViewService = platformMappingViewService;
    }

    @RequestMapping(PLATFORM_MAPPING.PAGE)
    public AjaxResponse page(@RequestBody PageQueryParameters params) {

        Map<String, Object> parameters = params.getParameters();

        Integer cartId = (Integer) parameters.get("cartId");
        Integer categoryType = (Integer) parameters.get("categoryType");
        String categoryPath = (String) parameters.get("categoryPath");

        int page = params.getPageIndex();
        int size = params.getPageRowCount();

        Map<String, Object> jsonMap = platformMappingViewService.page(cartId, categoryType, categoryPath, page, size,
                getUser());

        return success(jsonMap);
    }

    @RequestMapping(PLATFORM_MAPPING.GET)
    public AjaxResponse get(@RequestBody CmsBtPlatformMappingModel platformMappingModel) {
        return success(
                platformMappingViewService.get(platformMappingModel, getUser().getSelChannelId(), getLang())
        );
    }

    @RequestMapping(PLATFORM_MAPPING.SAVE)
    public AjaxResponse save(@RequestBody PlatformMappingSaveBean params) {
        return success(platformMappingViewService.save(params, getUser()));
    }

    @RequestMapping(PLATFORM_MAPPING.DELETE)
    public AjaxResponse delete(@RequestBody CmsBtPlatformMappingModel platformMappingModel) {
        return success(platformMappingService.delete(platformMappingModel));
    }

    @RequestMapping(PLATFORM_MAPPING.GET_COMMONSCHEMA)
    public AjaxResponse getCommonSchema() {
        return success(platformMappingViewService.getCommonSchema());
    }

    @RequestMapping(PLATFORM_MAPPING.GET_FEEDCUSTOMPROPS)
    public AjaxResponse getFeedCustomProps() {
        return success(platformMappingViewService.getFeedCustomProps(getUser().getSelChannelId()));
    }
}
