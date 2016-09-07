package com.voyageone.service.impl.cms;

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

    private final CmsBtBrandBlockDao btBrandBlockDao;

    @Autowired
    public CmsBtBrandBlockService(CmsBtBrandBlockDao btBrandBlockDao) {
        this.btBrandBlockDao = btBrandBlockDao;
    }

    public boolean isBlocked(String channelId, int cartIt, String feedBrand, int masterBrandId, String platformBrand) {
        // TODO 后续实现
        return false;
    }
}