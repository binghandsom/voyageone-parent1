package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/5/5.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.ROOT,
        method = RequestMethod.POST
)
public class CmsImageGroupController extends CmsController {

    @Autowired
    private CmsImageGroupService cmsImageGroupService;
@Autowired
ImageGroupService imageGroupService;
    /**
     *  初始化
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.INIT_CHANNEL_IMAGE_GROUP)
    public AjaxResponse init(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        // 初始化（取得检索条件信息)
        Map<String, Object> result  = cmsImageGroupService.init(param);
        //返回数据的类型
        return success(result);
    }

    /**
     *  检索
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.SEARCH_CHANNEL_IMAGE_GROUP)
    public AjaxResponse search(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        Map<String, Object>  result = cmsImageGroupService.search(param);
        return success(result);
    }

    /**
     *  新加ImageGroup信息
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.SAVE_CHANNEL_IMAGE_GROUP)
    public AjaxResponse save(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("userName", this.getUser().getUserName());
        cmsImageGroupService.save(param);
        return success(null);
    }

    /**
     *  逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.DELETE_CHANNEL_IMAGE_GROUP)
    public AjaxResponse delete(@RequestBody Map<String, Object> param){
        param.put("userName", this.getUser().getUserName());
        cmsImageGroupService.delete(param,this.getUser().getSelChannelId());
        return success(null);
    }
    @RequestMapping( CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.GetNoMatchSizeImageGroupList)
    public AjaxResponse getNoMatchSizeImageGroupList() {
        String channelId = this.getUser().getSelChannelId();
        return success(imageGroupService.getNoMatchSizeImageGroupList(channelId));
    }
}
