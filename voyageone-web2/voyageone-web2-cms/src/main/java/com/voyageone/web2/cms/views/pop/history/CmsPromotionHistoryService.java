package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtPromotionLogDao;
import com.voyageone.web2.cms.model.CmsBtPromotionLogModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/21
 * @version 2.0.0
 */
@Service
public class CmsPromotionHistoryService extends BaseAppService {

    @Autowired
    private CmsBtPromotionLogDao cmsBtPromotionLogDao;

    public Map<String, Object> getPromotionList(Map<String, Object> params, UserSessionBean userInfo, String language) {

        int count;
        Map<String, Object> result = new HashMap<>();
        List<CmsBtPromotionLogModel> promotionList;

        params.put("channelId", userInfo.getSelChannelId());
        params.put("lang", language);
        promotionList = cmsBtPromotionLogDao.selectPromotionLog(params);
        count = cmsBtPromotionLogDao.selectPromotionLogCnt(params);
        result.put("list", promotionList);
        result.put("total", count);

        // 获取cart list
        result.put("cartList", TypeChannel.getOptionsWithLang(Constants.comMtType.CARTS, userInfo.getSelChannelId(), language));
        return result;
    }

}
