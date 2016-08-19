package com.voyageone.service.impl.cms.tools;

import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCommonSchemaDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCommonSchemaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对平台类目, 通用属性部分进行查询
 * <p>
 * Created by jonas on 8/17/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
public class CmsMtPlatformCommonSchemaService extends BaseService {

    private final CmsMtPlatformCommonSchemaDao commonSchemaDao;

    @Autowired
    public CmsMtPlatformCommonSchemaService(CmsMtPlatformCommonSchemaDao commonSchemaDao) {
        this.commonSchemaDao = commonSchemaDao;
    }

    public CmsMtPlatformCommonSchemaModel get(int cartId) {
        return commonSchemaDao.selectOne(cartId);
    }
}
