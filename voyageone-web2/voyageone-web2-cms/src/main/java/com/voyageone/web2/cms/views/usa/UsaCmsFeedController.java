package com.voyageone.web2.cms.views.usa;

import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.usa.UsaFeedInfoService;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.bean.usa.FeedRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 美国CMS Feed相关Controller
 *
 * @Author rex.wu
 * @Create 2017-07-05 17:09
 */
@Controller
@RequestMapping(value = UsaCmsUrlConstants.FEED.ROOT)
public class UsaCmsFeedController extends BaseController {

    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private UsaFeedInfoService usaFeedInfoService;

    @RequestMapping(value = UsaCmsUrlConstants.FEED.DETAIL)
    @ResponseBody
    public AjaxResponse getFeedDetail(@RequestBody FeedRequest reqParams) {
        return success(feedInfoService.getProductByCode(getUser().getSelChannelId(), reqParams.getCode()));
    }


}
