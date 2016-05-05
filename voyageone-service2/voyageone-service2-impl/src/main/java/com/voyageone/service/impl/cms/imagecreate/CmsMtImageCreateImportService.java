package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.service.dao.cms.CmsMtImageCreateImportDao;
import com.voyageone.service.daoext.cms.CmsMtImageCreateImportDaoExt;
import com.voyageone.service.model.cms.CmsMtImageCreateImportModel;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CmsMtImageCreateImportService {
    @Autowired
    CmsMtImageCreateImportDaoExt daoExt;
    @Autowired
    CmsMtImageCreateImportDao dao;
    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        return daoExt.getPageByWhere(map);
    }
    public int getCountByWhere(Map<String, Object> map) {
        return daoExt.getCountByWhere(map);
    }
    public CmsMtImageCreateImportModel get(int id) {
        return dao.select(id);
    }
}
