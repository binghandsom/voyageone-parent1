package com.voyageone.web2.cms.views.system;

import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/29.
 * @version 2.0.0
 */
@Service
public class CmsCategoryListService {

    @Autowired
    CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    public List<JSONObject> getCategoryList(Map<String,Object> param){
        String columnResult="{_id:1,catId:1,catFullPath:1}";
        return cmsMtCategorySchemaDao.getSchemaList(columnResult,null,(Integer)param.get("skip"),(Integer)param.get("limit"));
    }

    public Long getCategoryCount(Map<String,Object> param){
        return cmsMtCategorySchemaDao.getCategoryCount();
    }

    public CmsMtCategorySchemaModel getMasterSchemaModelByCatId(String id){
        return cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(id);
    }
}
