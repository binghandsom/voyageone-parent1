package com.voyageone.web2.cms.views.system;

import com.google.common.collect.ImmutableMap;
import com.voyageone.service.impl.cms.StoreOperationService;
import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    StoreOperationService storeOperationService;


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
     * 重新上新所有商品
     */
    @RequestMapping("rePublish")
    public AjaxResponse rePublish() {

        storeOperationService.rePublish(getUser().getSelChannelId(),getUser().getUserName());
        return success(true);
    }


    /**
     * 重新导入所有feed商品（清空共通属性）
     *
     * @param cleanCommonProperties  清空共通属性标志
     */
    @RequestMapping("reUpload")
    public AjaxResponse reUpload(@RequestBody Boolean cleanCommonProperties) {

        boolean result = storeOperationService.reUpload(getUser().getSelChannelId(),cleanCommonProperties);
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
