package com.voyageone.web2.cms.views.system.category;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/29.
 * @version 2.0.0
 */
@Service
public class CmsCategoryListService {

    @Autowired
    private CategorySchemaService categorySchemaService;

    public List<JSONObject> getCategoryList(Map<String,Object> param){
        String columnResult="{_id:1,catId:1,catFullPath:1}";
        return categorySchemaService.getSchemaList(columnResult, getSearchParams(param), (Integer) param.get("skip"), (Integer) param.get("limit"));
    }

    public Long getCategoryCount(Map<String,Object> param){
        return categorySchemaService.getCategoryCount(getSearchParams(param));
    }

    public CmsMtCategorySchemaModel getMasterSchemaModelByCatId(String categoryId){
        return categorySchemaService.getCmsMtCategorySchema(categoryId);
    }

    public CmsMtCategorySchemaModel updateCategorySchema(Map<String,Object> schemaModel, String operator){

        CmsMtCategorySchemaModel oldData = getMasterSchemaModelByCatId(schemaModel.get("catId").toString());
        // 检查数据没有没有被更新过
        if(schemaModel.get("modified").toString().equalsIgnoreCase(oldData.getModified())){
            CmsMtCategorySchemaModel masterSchemaModel = null;
            List<Field> fields = SchemaJsonReader.readJsonForList((List) schemaModel.get("fields"));
            String catFullPath = (String) schemaModel.get("catFullPath");
            masterSchemaModel = new CmsMtCategorySchemaModel( schemaModel.get("catId").toString(),catFullPath,fields);
            masterSchemaModel.setModifier(operator);
            masterSchemaModel.setCreater((String) schemaModel.get("creater"));
            masterSchemaModel.setCreated((String) schemaModel.get("created"));
            masterSchemaModel.setModified(DateTimeUtil.getNowTimeStamp());
            masterSchemaModel.set_id(schemaModel.get("_id").toString());

            List<Map<String, Object>> skuList = new ArrayList<>();
            Map<String, Object> skuJson = (Map<String, Object>) schemaModel.get("sku");
            skuList.add(skuJson);
            List<Field> skuFields = SchemaJsonReader.readJsonForList(skuList);
            masterSchemaModel.setSku(skuFields.get(0));
            categorySchemaService.saveCategorySchema(masterSchemaModel);
            return masterSchemaModel;
        }
        throw new BusinessException("4000010",oldData.getModifier());
    }

    /**
     * 获取检索参数
     */
    private String getSearchParams (Map<String,Object> param) {
        StringBuilder sbResult = new StringBuilder();
        String cartName = String.valueOf(param.get("catName"));
        String catId = String.valueOf(param.get("catId"));

        if(!StringUtils.isEmpty(cartName))
            sbResult.append("\"catFullPath\":{$regex:\"").append(cartName).append("\"},");

        if(!StringUtils.isEmpty(catId))
            sbResult.append("\"catId\":\"").append(catId).append("\",");

        return sbResult.toString().length() >0 ? "{" + sbResult.toString().substring(0, sbResult.toString().length()-1) + "}" : "";
    }
}
