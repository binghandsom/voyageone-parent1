package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liang 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PlatformMappingService extends BaseService {


    @Autowired
    private CmsMtPlatformMappingDao platformMappingDao;

    public List<CmsMtPlatformMappingModel> getMappings(String channelId, int cartId) {
        return platformMappingDao.selectMappings(channelId, cartId);
    }

    public List<CmsMtPlatformMappingModel> getMappingsByMainCatId(String channelId, String mainCatId) {
        return platformMappingDao.selectMappingByMainCatId(channelId, mainCatId);
    }

    public CmsMtPlatformMappingModel getMappingByMainCatId(String channelId, int cartId, String mainCatId) {
        return platformMappingDao.selectMappingByMainCatId(channelId, cartId, mainCatId);
    }

    public void savePlatformMapping(CmsMtPlatformMappingModel platformMappingModel) {
        platformMappingDao.update(platformMappingModel);
    }
}
