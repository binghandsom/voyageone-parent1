package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.impl.cms.CmsFeedConfigService;
import com.voyageone.service.model.cms.CmsMtFeedConfigInfoModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gjl on 2016/12/20.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.ROOT, method = {RequestMethod.POST})
public class CmsFeedConfigController extends CmsController {

    @Autowired
    private CmsFeedConfigService cmsFeedConfigService;

    /**
     * Feed-Master属性一览初始化
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.SEARCH)
    public AjaxResponse search() {
        UserSessionBean user = getUser();
        return success(cmsFeedConfigService.search(user.getSelChannelId()));
    }

    /**
     * Feed-Master属性一览保存
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.SAVE)
    public AjaxResponse saveList(@RequestBody List<CmsMtFeedConfigBean> saveInfo) {
        UserSessionBean user = getUser();
        cmsFeedConfigService.save(saveInfo, user.getSelChannelId(), user.getUserName());
        return success(null);
    }

    /**
     * Feed属性一览*按模板导出
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.EXPORT)
    public ResponseEntity exportList() {
        UserSessionBean user = getUser();
        byte[] data;
        try {
            data = cmsFeedConfigService.getExcelFileFeedInfo(user.getSelChannelId());
        } catch (Exception e) {
            // 导出异常
            throw new BusinessException("");
        }
        // 返回
        return genResponseEntityFromBytes("feed" + DateTimeUtil.getLocalTime(getUserTimeZone(), DateTimeUtil.DATE_TIME_FORMAT_2) + ".xlsx", data);
    }

    /**
     * Feed属性一览*导入
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.IMPORT)
    public AjaxResponse importList(@RequestParam MultipartFile file) throws Exception {
        UserSessionBean user = getUser();
        cmsFeedConfigService.importExcelFileInfo(file, user.getUserName(), user.getSelChannelId());
        return success("success");
    }

    /**
     * Feed属性一览*删除
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.DELETE)
    public AjaxResponse delete(@RequestBody int id) {
        cmsFeedConfigService.delete(id);
        return success(null);
    }

    /**
     * Feed属性一览*feed属性保存按钮
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.SAVE_FEED)
    public AjaxResponse saveFeed(@RequestBody List<CmsMtFeedConfigInfoModel> cmsMtFeedConfigInfoModelList) {
        UserSessionBean user = getUser();
        cmsFeedConfigService.saveFeed(cmsMtFeedConfigInfoModelList, user.getSelChannelId(), user.getUserName());
        return success(null);
    }

    /**
     * Feed属性一览*feed表保存按钮
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.CREATE_FEED)
    public AjaxResponse createFeed(@RequestBody HashMap map) {
        cmsFeedConfigService.createFeed(map);
        return success(null);
    }

}


