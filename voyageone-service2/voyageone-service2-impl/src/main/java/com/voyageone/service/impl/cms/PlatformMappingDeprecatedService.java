package com.voyageone.service.impl.cms;

import com.mongodb.WriteResult;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDeprecatedDao;
import com.voyageone.service.daoext.cms.CmsMtPlatformSpecialFieldDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 早期版本的平台类目匹配, 现已废弃, 不再使用
 *
 * @author liang 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@Deprecated
public class PlatformMappingDeprecatedService extends BaseService {

    @Autowired
    private CmsMtPlatformSpecialFieldDaoExt platformSpecialFieldDao;

    @Autowired
    private CmsMtPlatformMappingDeprecatedDao platformMappingDao;

    public CmsMtPlatformMappingDeprecatedModel getMapping(String mainCategoryId, String platformCategoryId, int cartId, String channelId) {
        return platformMappingDao.selectMapping(mainCategoryId, platformCategoryId, cartId, channelId);
    }

    public List<CmsMtPlatformMappingDeprecatedModel> getMappings(String channelId, int cartId) {
        return platformMappingDao.selectMappings(channelId, cartId);
    }

    // 20160506 tom 这个功能不需要, 删掉 START
//    public List<CmsMtPlatformMappingDeprecatedModel> getMappingsByMainCatId(String channelId, String mainCatId) {
//        return platformMappingDao.selectMappingByMainCatId(channelId, mainCatId);
//    }
    // 20160506 tom 这个功能不需要, 删掉 END

    public CmsMtPlatformMappingDeprecatedModel getMappingByMainCatId(String channelId, int cartId, String mainCatId) {
        return platformMappingDao.selectMappingByMainCatId(channelId, cartId, mainCatId);
    }

    public int savePlatformMapping(CmsMtPlatformMappingDeprecatedModel platformMappingModel) {
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
