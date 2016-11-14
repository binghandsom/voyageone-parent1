package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtShelvesProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 */
@Service
public class CmsBtShelvesProductService extends BaseService {

    @Autowired
    private CmsBtShelvesProductDao cmsBtShelvesProductDao;

    public List<CmsBtShelvesProductModel> getByShelvesId(Integer shelvesId){
        Map<String, Object> param = new HashedMap();
        param.put("shelvesId",shelvesId);
        return cmsBtShelvesProductDao.selectList(param);
    }

    public int update(CmsBtShelvesProductModel cmsBtShelvesProductModel){
        return cmsBtShelvesProductDao.update(cmsBtShelvesProductModel);
    }

    public Integer insert(CmsBtShelvesProductModel cmsBtShelvesProductModel){
        cmsBtShelvesProductDao.insert(cmsBtShelvesProductModel);
        return cmsBtShelvesProductModel.getId();
    }
}
