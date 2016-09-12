package com.voyageone.service.impl.cms;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtBrandBlockDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public final static int BRAND_TYPE_FEED = 0;
    public final static int BRAND_TYPE_MASTER = 1;
    public final static int BRAND_TYPE_PLATFORM = 2;

    private final CmsBtBrandBlockDao brandBlockDao;

    @Autowired
    public CmsBtBrandBlockService(CmsBtBrandBlockDao brandBlockDao) {
        this.brandBlockDao = brandBlockDao;
    }

    public boolean isBlocked(String channelId, int cartId, String feedBrand, String masterBrand, String platformBrandId) {
        return !StringUtils.isEmpty(feedBrand) && isBlocked(channelId, cartId, BRAND_TYPE_FEED, feedBrand)
                || !StringUtils.isEmpty(masterBrand) && isBlocked(channelId, cartId, BRAND_TYPE_MASTER, masterBrand)
                || !StringUtils.isEmpty(platformBrandId) && isBlocked(channelId, cartId, BRAND_TYPE_PLATFORM, platformBrandId);

    }

    private boolean isBlocked(String channelId, int cartId, int brandType, String brand) {
        return brandBlockDao.selectCount(new HashMap<String, Object>(4, 1f) {{
            put("channelId", channelId);
            put("cartId", cartId);
            put("type", brandType);
            put("brand", brand);
        }}) > 0;
    }

    /**
     * 匹配类型，0、1、2 分别代表：feed、master、platform
     */
    int getBrandCount(String channelId, String cartId, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        map.put("type", type);
        map.put("active",1);
        return brandBlockDao.selectCount(map);
    }
}
