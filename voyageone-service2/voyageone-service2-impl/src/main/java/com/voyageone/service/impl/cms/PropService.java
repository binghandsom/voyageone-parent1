package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtPropDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPropModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * cms_mt_prop Service
 *
 * @Author rex.wu
 * @Create 2017-07-17 16:31
 */
@Service
public class PropService extends BaseService {

    @Autowired
    private CmsMtPropDao cmsMtPropDao;

    public List<Map<String, String>> getPropByType(int type) {
        return cmsMtPropDao.selectByType(type);

    }
}
