package com.voyageone.web2.cms.views.mapping.feed;

import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.MAPPING.FEED;
import com.voyageone.web2.cms.bean.setting.mapping.feed.GetFieldMappingBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SaveFieldMappingBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SetMappingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Feed 映射到主数据类目画面专供
 *
 * @author Jonas
 * @version 2.0.0, 12/8/15
 */
@RestController
@RequestMapping(value = FEED.ROOT, method = RequestMethod.POST)
public class CmsFeedMappingController extends CmsController {

    @Autowired
    private CmsFeedMappingService cmsFeedMappingService;

    @Autowired
    private CmsFeedPropMappingService feedPropMappingService;

    @RequestMapping(FEED.GET_FEED_CATEGORY_TREE)
    public AjaxResponse getFeedCategoryTree() {
        return success(cmsFeedMappingService.getFeedCategoryMap(getUser()));
    }

    @RequestMapping(FEED.GET_MAIN_CATEGORIES)
    public AjaxResponse getMainCategories() {
        return success(cmsFeedMappingService.getMainCategories(getUser()));
    }

    @RequestMapping(FEED.SET_MAPPING)
    public AjaxResponse setMapping(@RequestBody SetMappingBean setMappingBean) {
        return success(cmsFeedMappingService.setMapping(setMappingBean, getUser()));
    }

    @RequestMapping(FEED.EXTENDS_MAPPING)
    public AjaxResponse extendsMapping(@RequestBody CmsFeedCategoryModel feedCategoryModel) {
        return success(cmsFeedMappingService.extendsMapping(feedCategoryModel, getUser()));
    }

    @RequestMapping(FEED.GET_MAIN_PROPS)
    public AjaxResponse getMainCategoryProps(@RequestBody Map<String, String> params) {

        String feedCategoryPath = params.get("feedCategoryPath");

        return success(feedPropMappingService.getCategoryPropsByFeed(feedCategoryPath, getUser()));
    }

    @RequestMapping(FEED.GET_MATCHED)
    public AjaxResponse getMatched(@RequestBody Map<String, String> params) {

        String feedCategoryPath = params.get("feedCategoryPath");
        String mainCategoryPath = params.get("mainCategoryPath");

        return success(feedPropMappingService.getMatched(feedCategoryPath, mainCategoryPath, getUser()));
    }

    @RequestMapping(FEED.GET_FIELD_MAPPING)
    public AjaxResponse getFieldMapping(@RequestBody GetFieldMappingBean params) {

        return success(feedPropMappingService.getFieldMapping(params, getUser()));
    }

    @RequestMapping(FEED.GET_FEED_ATTRS)
    public AjaxResponse getFeedAttributes(@RequestBody Map<String, String> params) {

        String feedCategoryPath = params.get("feedCategoryPath");

        return success(feedPropMappingService.getFeedAttributes(feedCategoryPath, getUser()));
    }

    @RequestMapping(FEED.SAVE_FIELD_MAPPING)
    public AjaxResponse saveFieldMapping(@RequestBody SaveFieldMappingBean params) {
        feedPropMappingService.saveFeedMapping(params, getUser());
        return success(true);
    }

    @RequestMapping(FEED.DIRECT_MATCH_OVER)
    public AjaxResponse switchMatchOver(@RequestBody Map<String, String> params) {
        String feedCategoryPath = params.get("feedCategoryPath");
        return success(cmsFeedMappingService.switchMatchOver(feedCategoryPath, getUser()));
    }

    @RequestMapping(FEED.GET_MATCH_OVER)
    public AjaxResponse getMatchOver(@RequestBody Map<String, String> params) {
        String feedCategoryPath = params.get("feedCategoryPath");
        return success(cmsFeedMappingService.getMatchOver(feedCategoryPath, getUser()));
    }
}
