package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtBrandsMappingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * CmsMt Brand Service
 *
 * @author chuanyu.liang 2016/06/28
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class CmsMtBrandService extends BaseService {
    @Autowired
    private CmsMtBrandsMappingDao cmsMtBrandsMappingDao;

    /**
     * getModelByMap
     *
     * @param map Map
     * @return CmsMtBrandsMappingModel
     */
    public CmsMtBrandsMappingModel getModelByMap(Map<String, Object> map) {
        return cmsMtBrandsMappingDao.selectOne(map);
    }
}
