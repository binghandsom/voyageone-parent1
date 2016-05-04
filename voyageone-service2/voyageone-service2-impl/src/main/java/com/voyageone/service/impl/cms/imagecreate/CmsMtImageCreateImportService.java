package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.service.daoext.cms.CmsMtImageCreateImportDaoExt;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CmsMtImageCreateImportService {
    @Autowired
    CmsMtImageCreateImportDaoExt daoExt;
    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        return daoExt.getPageByWhere(map);
    }
    public int getCountByWhere(Map<String, Object> map)
    {
        return  daoExt.getCountByWhere(map);
    }
}
