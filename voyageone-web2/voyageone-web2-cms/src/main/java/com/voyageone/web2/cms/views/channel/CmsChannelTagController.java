package com.voyageone.web2.cms.views.channel;

import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.usa.UsaCmsUrlConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/4/19.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.ROOT, method = RequestMethod.POST)
public class CmsChannelTagController extends CmsController {
    @Autowired
    private CmsChannelTagService cmsChannelTagService;

    /**
     * 标签管理初始化
     *
     * @return success
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.INIT_CHANNEL_TAG)
    public AjaxResponse init(@RequestBody Map param) {
        //公司平台销售渠道
        param.put("channelId", this.getUser().getSelChannelId());
        //取得标签初始化的数据
        Map<String, Object> resultMap = cmsChannelTagService.getInitTagInfo(param, getLang());
        //返回数据的类型
        return success(resultMap);
    }

    /**
     * 查询指定标签类型下的所有标签(list形式)
     *
     * @return AjaxResponse
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.GET_TAG_LIST)
    public AjaxResponse getTagList(@RequestBody Map param) {
        // 公司平台销售渠道
        param.put("channelId", this.getUser().getSelChannelId());
        // 取得标签初始化的数据
        List<CmsBtTagBean> resultMap = cmsChannelTagService.getTagInfoByChannelId(param);
        // 返回数据
        return success(resultMap);
    }

    /**
     * 标签管理增加标签
     *
     * @return success
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.SAVE_CHANNEL_TAG)
    public AjaxResponse save(@RequestBody Map param) {
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        // 渠道id
        param.put("channelId", this.getUser().getSelChannelId());
        //根据前台传入的参数保存的数据库
        cmsChannelTagService.saveTagInfo(param);
        //返回数据的类型
        return success(param);
    }

    /**
     * 标签管理删除标签
     *
     * @return success
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.DEL_CHANNEL_TAG)
    public AjaxResponse del(@RequestBody Map param) {
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //根据前台传入的参数保存的数据库
        cmsChannelTagService.DelTagInfo(param);
        //返回数据的类型
        return success(param);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.GET_TAG)
    public AjaxResponse getTag(@RequestBody Map<String, Object> params) {
        Integer tagId = (Integer) params.get("tagId");
        return success(cmsChannelTagService.getTagByTagId(tagId));
    }

    /**
     * 编辑Tag, 只能编辑tagPathName
     *
     * @param params 参数
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CHANNEL_TAG.EDIT_TAG_NAME)
    public AjaxResponse editTag(@RequestBody Map<String, Object> params) {
        Integer tagId = (Integer) params.get("tagId");
        String tagName = (String) params.get("tagName");
        return success(cmsChannelTagService.editTagPathName(tagId, tagName, getUser().getUserName()));
    }
}
