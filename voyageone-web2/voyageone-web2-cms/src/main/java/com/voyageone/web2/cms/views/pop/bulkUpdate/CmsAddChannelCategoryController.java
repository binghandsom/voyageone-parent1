package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by gjl on 2016/5/23.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.POP.ADD_TO_CHANNEL_CATEGORY.ROOT,method = RequestMethod.POST)
public class CmsAddChannelCategoryController  extends CmsController {
    @Autowired
    private  CmsAddChannelCategoryService cmsAddChannelCategoryService;
    /**
     * 店铺内分类达标初始化
     * @param params
     * @return params
     */
    @RequestMapping(CmsUrlConstants.POP.ADD_TO_CHANNEL_CATEGORY.GET_CHANNEL_CATEGORY_INFO)
    public AjaxResponse init (@RequestBody Map<String, Object> params){
        //取得channelId
        params.put("channelId", this.getUser().getSelChannelId());
        Map result = cmsAddChannelCategoryService.getChannelCategory(params,getLang());
        return success(result);
    }

    /**
     * 店铺内分类达标保存
     * @param params
     * @return params
     */
    @RequestMapping(CmsUrlConstants.POP.ADD_TO_CHANNEL_CATEGORY.SAVE_CHANNEL_CATEGORY_INFO)
    public AjaxResponse save (@RequestBody Map<String, Object> params){
        //公司平台销售渠道
        params.put("channelId", this.getUser().getSelChannelId());
        //创建者/更新者用
        params.put("userName", this.getUser().getUserName());
        Map<String, Object> resultMap = cmsAddChannelCategoryService.saveChannelCategory(params, getCmsSession());
        return success(resultMap);
    }
}
