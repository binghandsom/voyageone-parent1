package com.voyageone.web2.cms.views.system.error;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/01/26
 */
@Service
class CmsErrorListService extends BaseAppService {

    @Autowired
    protected ChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private BusinessLogService businessLogService;

    /**
     * 获取检索页面初始化的master data数据
     *
     * @param userInfo UserSessionBean
     * @return Map
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取platform信息
        masterData.put("platformTypeList", TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language));

        // 获取category list
        masterData.put("categoryList", cmsBtChannelCategoryService.getFinallyCategoriesByChannelId(userInfo.getSelChannelId()));

        // 获取错误信息列表
        masterData.put("errorTypeList", TypeConfigEnums.MastType.errorType.getList(language));

        return masterData;
    }

    /**
     * 检索错误信息
     *
     * @param params 搜索参数
     * @return 包含错误信息的 Map, 使用了 key: [ errorList, errorCnt ]
     */
    public Map<String, Object> search(Map params) {
        Map<String, Object> resultBean = new HashMap<>();
        resultBean.put("errorList", businessLogService.getList(params));
        resultBean.put("errorCnt", businessLogService.getCount(params));
        return resultBean;
    }

    /**
     * 更新error错误状态
     *
     * @param params Map
     * @return int
     */
    Map<String, Object> updateStatus(Map params, UserSessionBean userInfo) {
        Map<String, Object> request = new HashMap<>();
        request.put("seq", Integer.parseInt(params.get("seq").toString()));
        request.put("modifier", userInfo.getUserName());
        int modifiedCount = businessLogService.updateFinishStatus(request);

        Map<String, Object> resultBean = new HashMap<>();
        resultBean.put("modifiedCount", modifiedCount);
        return resultBean;
    }
}
