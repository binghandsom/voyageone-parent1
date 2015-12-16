package com.voyageone.web2.cms.views.search;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.web2.cms.dao.CmsPromotionDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsSearchIndexService {

    @Autowired
    private CmsPromotionDao cmsPromotionDao;

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) {

        Map<String, Object> masterData = new HashMap<>();

        // 获取product status
        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));

        // 获取publish status
        masterData.put("publishStatusList", TypeConfigEnums.MastType.publishStatus.getList(language));

        // 获取label
        masterData.put("labelTypeList", TypeConfigEnums.MastType.label.getList(language));

        // 获取price type
        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));

        // 获取brand list
        masterData.put("brandList", TypeChannel.getOptions(Constants.comMtType.BRAND, userInfo.getSelChannelId()));

        // 获取promotion list
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", userInfo.getSelChannelId());
        masterData.put("promotionList", cmsPromotionDao.getPromotionList(params));

        return masterData;
    }
}
