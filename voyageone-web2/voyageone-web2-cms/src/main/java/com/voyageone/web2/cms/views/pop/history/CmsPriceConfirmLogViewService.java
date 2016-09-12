package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.service.dao.cms.CmsBtPriceConfirmLogDao;
import com.voyageone.service.model.cms.CmsBtPriceConfirmLogModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jonas on 9/1/16.
 *
 * @author jonas
 * @version 2.5.0
 * @since 2.5.0
 */
@Service
class CmsPriceConfirmLogViewService extends BaseAppService {

    private final CmsBtPriceConfirmLogDao priceConfirmLogDao;

    @Autowired
    CmsPriceConfirmLogViewService(CmsBtPriceConfirmLogDao priceConfirmLogDao) {
        this.priceConfirmLogDao = priceConfirmLogDao;
    }

    Map<String, Object> getPage(String skuCode, String code, String cartId, String channelId, int pageNumber, int limit) {

        if (cartId.equals(""))
            cartId = null;

        MySqlPageHelper.PageBoundsMap pageBoundsMap = MySqlPageHelper.build()
                .addQuery("channelId", channelId)
                .addQuery("cartId", cartId)
                .addQuery("code", code)
                .addQuery("skuCode", skuCode);

        int count = priceConfirmLogDao.selectCount(pageBoundsMap.toMap());

        List<CmsBtPriceConfirmLogModel> data = priceConfirmLogDao.selectList(pageBoundsMap.page(pageNumber).limit(limit).toMap());

        return new HashMap<String, Object>() {{
            put("data", data);
            put("count", count);
        }};
    }
}
