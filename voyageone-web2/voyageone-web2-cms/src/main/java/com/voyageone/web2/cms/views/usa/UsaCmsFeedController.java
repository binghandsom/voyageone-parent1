package com.voyageone.web2.cms.views.usa;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.usa.UsaFeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.usa.FeedRequest;

import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public AjaxResponse getFeedDetail(@RequestBody FeedRequest reqParams) {
        return success(feedInfoService.getProductByCode(getUser().getSelChannelId(), reqParams.getCode()));
    }

    /**
     * 条件查询feed列表信息
     * @param params
     * @return
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.LIST)
    public AjaxResponse getFeedList(@RequestBody Map params) {
        Map<String, Object> resultBean = new HashMap<String, Object>();
        UserSessionBean userInfo = getUser();
        ChannelConfigEnums.Channel selChannel = userInfo.getSelChannel();
        //获取数据列表
        List<CmsBtFeedInfoModel> feedList = usaFeedInfoService.getFeedList(params, selChannel.toString());
        resultBean.put("feedList", feedList);
        //获取数据总量
        Long feedListTotal = usaFeedInfoService.getFeedCount(params, selChannel.toString());
        resultBean.put("feedListTotal", feedListTotal);

        // 返回feed信息
        return success(resultBean);
    }


}
