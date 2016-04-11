package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liang 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PlatformCategoryService extends BaseService {

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    private CmsMtPlatformCategorySchemaDao platformCategorySchemaDao;

    public List<CmsMtPlatformCategoryTreeModel> getPlatformCategories(String channelId, Integer cartId) {
        return platformCategoryDao.selectByChannel_CartId(channelId, cartId);
    }

    public CmsMtPlatformCategorySchemaModel getPlatformCatSchema(String catId, int cartId) {
        return platformCategorySchemaDao.getPlatformCatSchemaModel(catId, cartId);
    }
}
