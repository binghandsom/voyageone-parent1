package com.voyageone.service.impl.cms.jumei;
import com.voyageone.service.dao.cms.CmsMtMasterInfoDao;
import com.voyageone.service.daoext.cms.CmsMtMasterInfoDaoExt;
import com.voyageone.service.model.cms.CmsMtMasterInfoModel;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtMasterInfoService {
    @Autowired
    CmsMtMasterInfoDao dao;
    @Autowired
    CmsMtMasterInfoDaoExt daoExt;


    public CmsMtMasterInfoModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsMtMasterInfoModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsMtMasterInfoModel entity) {
        return dao.insert(entity);
    }

    public List<MapModel> getListByWhere(Map<String, Object> map) {
        return daoExt.selectListByWhere(map);
    }

    public Integer getCountByWhere(Map<String, Object> map) {
        return daoExt.selectCountByWhere(map);
    }


}

