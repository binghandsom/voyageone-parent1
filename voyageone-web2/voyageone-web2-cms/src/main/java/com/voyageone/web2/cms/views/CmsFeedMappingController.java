package com.voyageone.web2.cms.views;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.voyageone.web2.cms.CmsUrlConstants.MAPPING.FEED;

/**
 * Feed 映射到主数据类目画面专供
 * @author Jonas
 * @version 2.0.0, 12/8/15
 */
@RestController
@RequestMapping(value = FEED.ROOT, method = RequestMethod.POST)
public class CmsFeedMappingController extends CmsController {

    @Autowired
    private CmsFeedMappingService cmsFeedMappingService;

    @RequestMapping(FEED.GET_FEED_CATEGORIES)
    public AjaxResponse getFeedCategories() {
        return success(cmsFeedMappingService.getFeedCategories(getUser()));
    }

    @RequestMapping(FEED.GET_MAIN_CATEGORIES)
    public AjaxResponse getMainCategories() {
        return success(cmsFeedMappingService.getMainCategoryTree(getUser()));
    }
}
