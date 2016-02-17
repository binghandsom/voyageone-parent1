package com.voyageone.web2.cms.views.system.category;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.DateTimeUtil;
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

    public JSONObject getMasterSchemaJsonObjectByCatId(String catId){
        return cmsMtCategorySchemaDao.getMasterSchemaJsonObjectByCatId(catId);
    }

    public CmsMtCategorySchemaModel updateCategorySchema(Map<String,Object> schemaModel, String operator){

        CmsMtCategorySchemaModel oldData = getMasterSchemaModelByCatId(schemaModel.get("catId").toString());
        // 检查数据没有没有被更新过
        if(schemaModel.get("modified").toString().equalsIgnoreCase(oldData.getModified())){
            CmsMtCategorySchemaModel masterSchemaModel = null;
            if (schemaModel !=null){
                List<Field> fields = SchemaJsonReader.readJsonForList((List) schemaModel.get("fields"));
                String catFullPath = (String) schemaModel.get("catFullPath");
                masterSchemaModel = new CmsMtCategorySchemaModel( schemaModel.get("catId").toString(),catFullPath,fields);
                masterSchemaModel.setModifier(operator);
                masterSchemaModel.setCreater((String) schemaModel.get("creater"));
                masterSchemaModel.setCreated((String) schemaModel.get("created"));
                masterSchemaModel.setModified(DateTimeUtil.getNowTimeStamp());
                masterSchemaModel.set_id( schemaModel.get("_id").toString());
            }
            cmsMtCategorySchemaDao.update(masterSchemaModel);
            return masterSchemaModel;
        }
        throw new BusinessException("4000010",oldData.getModifier());
    }

}
