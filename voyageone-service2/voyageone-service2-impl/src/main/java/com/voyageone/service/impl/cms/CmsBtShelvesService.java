package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtShelvesDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 */
@Service
public class CmsBtShelvesService extends BaseService {

    @Autowired
    private CmsBtShelvesDao cmsBtShelvesDao;

    public CmsBtShelvesModel getId(Integer id){
        return cmsBtShelvesDao.select(id);
    }

    public int update(CmsBtShelvesModel cmsBtShelvesModel){
        return cmsBtShelvesDao.update(cmsBtShelvesModel);
    }

    public Integer insert(CmsBtShelvesModel cmsBtShelvesModel){
        cmsBtShelvesDao.insert(cmsBtShelvesModel);
        return cmsBtShelvesModel.getId();
    }

    public List<CmsBtShelvesModel>selectList(Map map){
        return cmsBtShelvesDao.selectList(map);
    }
}
