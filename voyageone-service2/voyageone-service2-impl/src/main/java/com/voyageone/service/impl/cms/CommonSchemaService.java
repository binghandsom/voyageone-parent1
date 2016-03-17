package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CommonSchemaService extends BaseService {

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;

    public List getAll() {
        return cmsMtCommonSchemaDao.findAllProps();
    }

}
