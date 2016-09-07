package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtBrandBlockDao;
import com.voyageone.service.daoext.cms.CmsBtBrandBlockDaoExt;
import com.voyageone.service.impl.BaseService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final CmsBtBrandBlockDao btBrandBlockDao;
    @Autowired
    CmsBtBrandBlockDaoExt cmsBtBrandBlockDaoExt;

    @Autowired
    public CmsBtBrandBlockService(CmsBtBrandBlockDao btBrandBlockDao) {
        this.btBrandBlockDao = btBrandBlockDao;
    }

    public boolean isBlocked(String channelId, int cartIt, String feedBrand, String masterBrandId, String platformBrand) {
        // TODO 后续实现
        return false;
    }
    //匹配类型，0、1、2 分别代表：feed、master、platform
    public int getBrandCount(String channelId, String cartId, int type) {
        Map<String, Object> map = new HashedMap();
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        map.put("type", type);
        return cmsBtBrandBlockDaoExt.selectCount(map);
    }
}