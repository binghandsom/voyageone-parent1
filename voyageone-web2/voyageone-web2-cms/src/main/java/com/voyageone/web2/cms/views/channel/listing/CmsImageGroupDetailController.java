package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.service.bean.cms.CmsBtImageGroupBean;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/5/5.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsImageGroupDetailController extends CmsController {

    @Autowired
    private CmsImageGroupDetailService cmsImageGroupDetailService;

    /**
     *  初始化
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.INIT_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse init(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        // 初始化（取得检索条件信息)
        Map<String, Object> resultBean  = cmsImageGroupDetailService.init(param);
        //返回数据的类型
        return success(resultBean);
    }


    /**
     *  检索图片
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.SEARCH_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse search(@RequestBody Map<String, Object> param){
        param.put("lang", this.getLang());
        List<CmsBtImageGroupModel_Image> result = cmsImageGroupDetailService.search(param);
        return success(result);
    }

    /**
     *  编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.SAVE_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse save(@RequestBody Map<String, Object> param){
        param.put("channelId", this.getUser().getSelChannelId());
        cmsImageGroupDetailService.save(param);
        return success(null);
    }

    /**
     *  保存Image信息
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.SAVE_IMAGE_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse saveImage(@RequestParam Map<String, Object> param) {
        param.put("channelId", this.getUser().getSelChannelId());
        cmsImageGroupDetailService.saveImage(param, null);
        return success(null);
    }

    /**
     *  保存Image信息
     *
     * @param param 客户端参数
     * @param file 导入文件
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.SAVE_UPLOAD_IMAGE_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse saveUploadImage(@RequestParam Map<String, Object> param, @RequestParam MultipartFile file) {
        param.put("channelId", this.getUser().getSelChannelId());
        cmsImageGroupDetailService.saveImage(param, file);
        return success(null);
    }

    /**
     *  删除Image信息
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.DELETE_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse delete(@RequestBody Map<String, Object> param){
        cmsImageGroupDetailService.delete(param);
        return success(null);
    }

    /**
     *  重刷
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.REFRESH_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse refresh(@RequestBody Map<String, Object> param){
//        cmsImageGroupDetailService.refresh(param);
        return success(null);
    }

    /**
     *  移动
     *
     * @param param 客户端参数
     * @return 结果
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_GROUP_DETAIL.MOVE_CHANNEL_IMAGE_GROUP_DETAIL)
    public AjaxResponse move(@RequestBody Map<String, Object> param){
//        cmsImageGroupDetailService.refresh(param);
        return success(null);
    }
}
