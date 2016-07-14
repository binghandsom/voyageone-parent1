package com.voyageone.web2.vms.views.feed;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VmsFeedSearchController
 * Created on 16/07/11.
 * @author jeff.duan
 * @version 1.0
 */
@RestController
@RequestMapping( value = VmsUrlConstants.FEED.FEED_SEARCH.ROOT )
public class VmsFeedSearchController extends BaseController {

    @Autowired
    private VmsFeedSearchService vmsFeedSearchService;

    /**
     *  检索
     *
     * @param params 客户端参数
     * @return 结果
     */
    @RequestMapping(VmsUrlConstants.FEED.FEED_SEARCH.SEARCH)
    public AjaxResponse search(@RequestBody Map params) {
        Map<String, Object> resultBean = new HashMap<String, Object>();
        UserSessionBean userInfo = getUser();

        // 获取feed列表
        List<CmsBtFeedInfoModel> feedInfoList = vmsFeedSearchService.getFeedList(params, userInfo);
        resultBean.put("feedInfoList", feedInfoList);
        long total = vmsFeedSearchService.getFeedCnt(params, userInfo);
        resultBean.put("total", total);

        // 返回feed信息
        return success(resultBean);
    }
}
