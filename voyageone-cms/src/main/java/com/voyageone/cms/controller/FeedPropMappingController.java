package com.voyageone.cms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.cms.feed.OperationBean;
import com.voyageone.cms.formbean.FeedMappingProp;
import com.voyageone.cms.modelbean.*;
import com.voyageone.cms.service.FeedPropMappingService;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.voyageone.cms.UrlConstants.FeedPropMapping.*;

/**
 * 第三方品牌数据，和主数据，进行属性和值的匹配
 *
 * Created by Jonas on 9/1/15.
 */
@RestController
@RequestMapping(value = ROOT, method = RequestMethod.POST)
public class FeedPropMappingController extends BaseController {

    @Autowired
    private FeedPropMappingService feedPropMappingService;

    @RequestMapping(GET_PATH)
    public AjaxResponseBean getPath(@RequestBody Map<String, Object> params) {

        int categoryId = (int) params.get("categoryId");

        String path = feedPropMappingService.getCategoryPath(categoryId);

        return success(path);
    }

    @RequestMapping(GET_PROPS)
    public AjaxResponseBean getProps(@RequestBody DtRequest<Integer> dtRequest) {

        DtResponse<List<FeedMappingProp>> props = feedPropMappingService.getProps(dtRequest, getUser().getSelChannel());

        return success(props);
    }

    @RequestMapping(GET_CONST)
    public AjaxResponseBean getConst() {

        List<OperationBean> operationBeans = feedPropMappingService.getConditionOperations();

        List<CmsFieldEnum.CmsModelEnum> cmsModelEnums = feedPropMappingService.getCmsProps();

        List<FeedConfig> configs = feedPropMappingService.getFeedProps(getUser().getSelChannel());

        JsonObj jsonObj = new JsonObj();
        jsonObj.add("operations", operationBeans);
        jsonObj.add("feedProps", configs);
        jsonObj.add("cmsProps", cmsModelEnums);

        return success(jsonObj);
    }

    @RequestMapping(GET_MAPPINGS)
    public AjaxResponseBean getMappings(@RequestBody Map<String, Object> params) {

        int prop_id = (int) params.get("prop_id");
        String channel_id = getUser().getSelChannel();

        List<FeedPropMapping> mappings = feedPropMappingService.getPropMappings(prop_id, channel_id);

        return success(mappings);
    }

    @RequestMapping(GET_PROP_OPTIONS)
    public AjaxResponseBean getOptions(@RequestBody Map<String, Object> params) {

        int prop_id = (int) params.get("prop_id");

        List<PropertyOption> options = feedPropMappingService.getPropOptions(prop_id);

        return success(options);
    }

    @RequestMapping(GET_FEED_VALUES)
    public AjaxResponseBean getFeedValues(@RequestBody FeedConfig feedConfig) {

        List<FeedValue> values = feedPropMappingService.getFeedValues(feedConfig.getCfg_val1(), getUser().getSelChannel());

        return success(values);
    }

    @RequestMapping(SET_IGNORE)
    public AjaxResponseBean setIgnore(@RequestBody FeedMappingProp prop) {

        int count = feedPropMappingService.updateIgnore(prop, getUser());

        return success(count);
    }

    @RequestMapping(ADD_MAPPING)
    public AjaxResponseBean addMapping(@RequestBody FeedPropMapping mapping) {

        FeedPropMapping mappingFromDb = feedPropMappingService.addMapping(mapping, getUser());

        return success(mappingFromDb);
    }

    @RequestMapping(SET_MAPPING)
    public AjaxResponseBean setMapping(@RequestBody FeedPropMapping mapping) {

        FeedPropMapping mappingFromDb = feedPropMappingService.updateMapping(mapping, getUser());

        return success(mappingFromDb);
    }

    @RequestMapping(DEL_MAPPING)
    public AjaxResponseBean delMapping(@RequestBody FeedPropMapping mapping) {

        int count = feedPropMappingService.deleteMapping(mapping, getUser());

        return success(count);
    }

    @RequestMapping(GET_DEFAULT)
    public AjaxResponseBean getDefaultValue(@RequestBody FeedMappingProp prop) {

        String value = feedPropMappingService.getDefaultValue(prop, getUser().getSelChannel());

        return success(value);
    }
}
