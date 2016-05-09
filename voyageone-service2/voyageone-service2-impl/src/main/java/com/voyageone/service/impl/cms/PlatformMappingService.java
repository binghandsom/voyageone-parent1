package com.voyageone.service.impl.cms;

import com.mongodb.WriteResult;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.daoext.cms.CmsMtPlatformSpecialFieldDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
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
    private CmsMtPlatformSpecialFieldDaoExt platformSpecialFieldDao;

    @Autowired
    private CmsMtPlatformMappingDao platformMappingDao;

    public CmsMtPlatformMappingModel getMapping(String mainCategoryId, String platformCategoryId, int cartId, String channelId) {
        return platformMappingDao.selectMapping(mainCategoryId, platformCategoryId, cartId, channelId);
    }

    public List<CmsMtPlatformMappingModel> getMappings(String channelId, int cartId) {
        return platformMappingDao.selectMappings(channelId, cartId);
    }

    // 20160506 tom 这个功能不需要, 删掉 START
//    public List<CmsMtPlatformMappingModel> getMappingsByMainCatId(String channelId, String mainCatId) {
//        return platformMappingDao.selectMappingByMainCatId(channelId, mainCatId);
//    }
    // 20160506 tom 这个功能不需要, 删掉 END

    public CmsMtPlatformMappingModel getMappingByMainCatId(String channelId, int cartId, String mainCatId) {
        return platformMappingDao.selectMappingByMainCatId(channelId, cartId, mainCatId);
    }

    public int savePlatformMapping(CmsMtPlatformMappingModel platformMappingModel) {
        WriteResult res = platformMappingDao.update(platformMappingModel);
        return res.getN();
    }

    public List<CmsMtPlatformSpecialFieldModel> getPlatformSpecialField(int cartId, String platformCategoryId) {
        return platformSpecialFieldDao.select(cartId, platformCategoryId, null, null);
    }

    public String getSpecialMappingType(Integer cartId, String platformCategoryId, String propertyId) {
        return platformSpecialFieldDao.selectSpecialMappingType(cartId, platformCategoryId, propertyId);
    }


}
