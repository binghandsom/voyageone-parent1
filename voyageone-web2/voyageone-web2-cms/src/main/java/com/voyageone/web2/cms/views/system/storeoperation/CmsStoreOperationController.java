package com.voyageone.web2.cms.views.system.storeoperation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.service.bean.cms.CmsBtStoreOperationHistoryBean;
import com.voyageone.service.impl.cms.StoreOperationService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class CmsStoreOperationController extends CmsController {

//    public static LocalDateTime lastExecuteTime = null;

    //channelId->lastExecuteTime
    public static final ConcurrentHashMap<String, LocalDateTime> lastExecuteTimes = new ConcurrentHashMap<>();

    public static final int INTERVAL_DEFAULT = 2; //默认值为2

    @Autowired
    StoreOperationService storeOperationService;


    public int getConfigHours() {
        CmsChannelConfigBean config = CmsChannelConfigs.getConfigBeanNoCode(ChannelConfigEnums.Channel.NONE.getId(), CmsConstants.ChannelConfig.STORE_OPERATION_INTERVAL_TIME);

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
        return success(ImmutableMap.of("uploadCnt", uploadCnt,
                "storeOperationTypeList", Types.getTypeList(75, this.getLang())));
    }

    /**
     * 如果上次执行时间距离现在执行时间超过指定阈值,则抛异常
     */
    public void checkInInterval(String channelId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(channelId), "channelId不能为空!");

        LocalDateTime lastExecuteTime = lastExecuteTimes.get(channelId);
        if (lastExecuteTime == null) {
            lastExecuteTimes.put(channelId, LocalDateTime.now()); //ok
            return;
        }

        int interval = getConfigHours();
        boolean inRange = lastExecuteTime.plusHours(interval).isAfter(LocalDateTime.now());
        if (inRange) {
            throw new BusinessException("操作时间间隔必须在" + interval + "小时以上!");
        } else {
            lastExecuteTimes.put(channelId, LocalDateTime.now()); //更新上次操作时间
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
        storeOperationService.rePublishPrice(channelId, userName);
//        throw new UnsupportedOperationException();
        return success(true);
    }

    /**
     * 获取操作历史
     */
    @RequestMapping("getHistory")
    public AjaxResponse getHistory(@RequestBody Map<String, Object> params) {
        params.put("lang", this.getLang());
        List<CmsBtStoreOperationHistoryBean> historyList = storeOperationService.getHistoryBy(params);
        return success(Page.fromMap(params).withData(historyList));
    }
}
