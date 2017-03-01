package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.CmsBtTranslateService;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiang on 2016/2/24.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.ROOT, method = RequestMethod.POST)
public class CmsBtCustomTranslateController extends CmsController {

    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    @Autowired
    private CmsBtTranslateService cmsBtTranslateService;


    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.INIT)
    public AjaxResponse getFeedCustPropValueList(@RequestBody Map<String, Object> params) {
        Map<String, Object> ret = new HashMap();
        ret.put("resultData",cmsBtTranslateService.select(getUser().getSelChannelId(), (Integer) params.get("type"),(String) params.get("propName"),(String) params.get("propValue"),(Integer) params.get("skip"),(Integer) params.get("limit")));
        ret.put("total",cmsBtTranslateService.selectCnt(getUser().getSelChannelId(), (Integer) params.get("type"),(String) params.get("propName"),(String) params.get("propValue")));
        return success(ret);
    }


    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.ADD)
    public AjaxResponse addFeedCustPropValue(@RequestBody CmsBtTranslateModel params) {
        cmsBtTranslateService.insertOrUpdate(params);
        return success(true);
    }


    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.SAVE)
    public AjaxResponse saveFeedCustPropValue(@RequestBody CmsBtTranslateModel params) {
        params.setCreater(getUser().getUserName());
        cmsBtTranslateService.insertOrUpdate(params);
        return success(true);
    }
}
