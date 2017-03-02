package com.voyageone.web2.cms.views.channel;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.CmsBtTranslateService;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
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
    private CmsBtTranslateService cmsBtTranslateService;


    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.INIT)
    public AjaxResponse getFeedCustPropValueList(@RequestBody Map<String, Object> params) {
        Map<String, Object> ret = new HashMap();

        //前端组建中预定义好的分页属性名curr、size
        Integer skip = (Integer) params.get("curr"),
                limit = (Integer) params.get("size");

        ret.put("resultData", cmsBtTranslateService.select(getUser().getSelChannelId(), (Integer) params.get("type"), (String) params.get("propName"), (String) params.get("propValue"), skip, limit));
        ret.put("total", cmsBtTranslateService.selectCnt(getUser().getSelChannelId(), (Integer) params.get("type"), (String) params.get("propName"), (String) params.get("propValue")));
        return success(ret);
    }


    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.ADD)
    public AjaxResponse addFeedCustPropValue(@RequestBody CmsBtTranslateModel params) {
        params.setCreater(getUser().getUserName());
        params.setModifier(getUser().getUserName());
        params.setChannelId(getUser().getSelChannelId());
        cmsBtTranslateService.insertOrUpdate(params);
        return success(true);
    }


    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM_TRANSLATE.SAVE)
    public AjaxResponse saveFeedCustPropValue(@RequestBody CmsBtTranslateModel params) {
        params.setModifier(getUser().getUserName());
        params.setModified(DateTimeUtil.getNow());
        cmsBtTranslateService.insertOrUpdate(params);
        return success(true);
    }
}
