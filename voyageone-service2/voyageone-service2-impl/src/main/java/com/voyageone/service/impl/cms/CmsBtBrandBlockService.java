package com.voyageone.service.impl.cms;

import com.voyageone.common.util.MapUtil;
import com.voyageone.service.dao.cms.CmsBtBrandBlockDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 品牌屏蔽（品牌黑名单）相关的操作
 * Created by jonas on 9/6/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
public class CmsBtBrandBlockService extends BaseService {

    private final static int BRAND_TYPE_FEED = 0;
    private final static int BRAND_TYPE_MASTER = 1;
    private final static int BRAND_TYPE_PLATFORM = 2;

    private final CmsBtBrandBlockDao brandBlockDao;

    @Autowired
    public CmsBtBrandBlockService(CmsBtBrandBlockDao brandBlockDao) {
        this.brandBlockDao = brandBlockDao;
    }

    public boolean isBlocked(String channelId, int cartId, String feedBrand, String masterBrand, int platformBrandId) {

        int count = brandBlockDao.selectCount(MapUtil.toMap("channelId", channelId,
                "cartId", cartId,
                "type", BRAND_TYPE_FEED,
                "brand", feedBrand));

        if (count > 0)
            return true;

        count = brandBlockDao.selectCount(MapUtil.toMap("channelId", channelId,
                "cartId", cartId,
                "type", BRAND_TYPE_MASTER,
                "brand", masterBrand));

        if (count > 0)
            return true;

        count = brandBlockDao.selectCount(MapUtil.toMap("channelId", channelId,
                "cartId", cartId,
                "type", BRAND_TYPE_PLATFORM,
                "brand", platformBrandId));

        return count > 0;
    }
}