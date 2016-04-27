package com.voyageone.web2.cms.views.system;

import com.google.common.collect.ImmutableMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.dao.CmsChannelConfigDao;
import com.voyageone.service.impl.cms.StoreOperationService;
import com.voyageone.service.model.cms.CmsBtChannelConfigModel;
import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 全店页面操作
 *
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/26 14:33
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RestController
@RequestMapping(value = "/cms/system/store_operation", method = RequestMethod.POST)
public class StoreOperationCtrl extends CmsController {

    public static LocalDateTime lastExecuteTime = null;//用于检查倒计时的

    public static final int INTERVAL_DEFAULT = 2; //默认值为2


    @Resource
    StoreOperationService storeOperationService;

    @Resource
    CmsChannelConfigs config;

    public int getConfigHours(String channelId) {
        CmsChannelConfigBean config = CmsChannelConfigs.getConfigBean(channelId, "STORE_OPERATION_INTERVAL_TIME", "default");

        if (config == null || config.getConfigValue1() == null) {
            return INTERVAL_DEFAULT;
        }
        return Integer.valueOf(config.getConfigValue1());
    }

    /**
     * @return uploadCnt (可供上新商品总数),feedImportCng(可重新feed导入商品总数这里暂不实现)
     */
    @RequestMapping("init")
    public AjaxResponse init() {
        String channelId = getUser().getSelChannelId();
        long uploadCnt = storeOperationService.countProductsThatCanUploaded(channelId);
        return success(ImmutableMap.of("uploadCnt", uploadCnt));
    }

    /**
     * 如果上次执行时间距离现在执行时间超过指定阈值,则抛异常
     */
    public synchronized void checkInInterval(String channelId) {
        if (lastExecuteTime == null) {
            lastExecuteTime = LocalDateTime.now();
            return;
        }
        int interval = getConfigHours(channelId);
        if(lastExecuteTime.plusHours(interval).isAfter(LocalDateTime.now())){
            throw new BusinessException("操作时间间隔必须在"+interval+"小时以上!");
        }
    }


    /**
     * 重新上新所有商品
     */
    @RequestMapping("rePublish")
    public AjaxResponse rePublish() {
        String channelId = getUser().getSelChannelId();
        checkInInterval(channelId);
        String userName = getUser().getUserName();
        storeOperationService.rePublish(channelId, userName);
        return success(true);
    }


    /**
     * 重新导入所有feed商品（清空共通属性）
     *
     * @param cleanCommonProperties 清空共通属性标志
     */
    @RequestMapping("reUpload")
    public AjaxResponse reUpload(@RequestBody Boolean cleanCommonProperties) {
        String userName = getUser().getUserName();
        String channelId = getUser().getSelChannelId();
        checkInInterval(channelId);
        boolean result = storeOperationService.reUpload(channelId, cleanCommonProperties, userName);
        return success(result);
    }

    /**
     * 刷新商品图片（暂不实现）
     */
    @RequestMapping("rePublishImage")
    public AjaxResponse rePublishImage() {
        throw new UnsupportedOperationException("暂不支持刷新商品图片");
    }


    /**
     * 价格同步（同步到各个平台）
     */
    @RequestMapping("rePublishPrice")
    public AjaxResponse rePublishPrice() {
        String userName = getUser().getUserName();
        String channelId = getUser().getSelChannelId();
        checkInInterval(channelId);
        storeOperationService.rePublishPrice(channelId,userName);
        throw new UnsupportedOperationException();
    }

    /**
     * 获取操作历史
     */
    @RequestMapping("getHistory")
    public AjaxResponse getHistory(@RequestBody Map<String, Object> params) {

        List<CmsBtStoreOperationHistoryModel> historys = storeOperationService.getHistoryBy(params);
        return success(Page.fromMap(params).withData(historys));
    }

}
