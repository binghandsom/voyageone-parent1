package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.promotion.task.CmsTaskStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Map<String, Object> resultBean  = cmsImageGroupService.init(param);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     *  检索
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.SEARCH_CHANNEL_IMAGE_GROUP)
    public AjaxResponse search(@RequestBody Map<String, Object> param){
        Map<String, Object> resultBean = new HashMap<>();
        return success(resultBean);
    }

    /**
     *  新加/编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP.SAVE_CHANNEL_IMAGE_GROUP)
    public AjaxResponse save(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        cmsImageGroupService.save(param);
        return success(null);
    }
}
