package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryExtendFieldModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryExtendFieldModel_Field;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 平台Schema对应的一些想要重构的属性的表的Dao
 * 重构：不想显示表(CmsMtPlatformCategoryInvisibleField)追加field_id='city',本表同步追加field_id='city'
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Repository
public class CmsMtPlatformCategoryExtendFieldDao extends BaseMongoCartDao<CmsMtPlatformCategoryExtendFieldModel> {

    public CmsMtPlatformCategoryExtendFieldModel selectOneByCatId(String catId, int cartId) {
//        JomgoQuery query = new JomgoQuery();
//        query.setQuery("{\"catId\":#}");
//        query.setParameters(catId);
//        return selectOneWithQuery(query, cartId);
        String query = "{'catId':'%s'}";
        JSONObject result = mongoTemplate.findOne(String.format(query, catId), getCollectionName(cartId));

        CmsMtPlatformCategoryExtendFieldModel model = null;
        if (result != null) {
            List<CmsMtPlatformCategoryExtendFieldModel_Field> propsProduct = new ArrayList<>();
            List<CmsMtPlatformCategoryExtendFieldModel_Field> propsItem = new ArrayList<>();

            model = new CmsMtPlatformCategoryExtendFieldModel();
            model.setModifier((String) result.get("modifier"));
            model.setCreater((String) result.get("creater"));
            model.setCreated((String) result.get("created"));
            model.setModified((String) result.get("modified"));
            model.set_id(result.get("_id").toString());
            model.setCartId((Integer) result.get("cartId"));
            model.setCatId((String) result.get("catId"));
            model.setCatFullPath((String) result.get("catFullPath"));
            model.setPropsProduct(propsProduct);
            model.setPropsItem(propsItem);

            setFieldModel(propsProduct, (List) result.get("propsProduct"));
            setFieldModel(propsItem, (List) result.get("propsItem"));
        }

        return model;
    }

    private void setFieldModel(List<CmsMtPlatformCategoryExtendFieldModel_Field> modelProps, List<Map<String, Object>> resProps) {
        if (resProps != null && !resProps.isEmpty()) {
            resProps.forEach(data-> {
                CmsMtPlatformCategoryExtendFieldModel_Field extendField = new CmsMtPlatformCategoryExtendFieldModel_Field();
                extendField.setParentFieldId((String) data.get("parentFieldId"));
                extendField.setParentFieldName((String) data.get("parentFieldName"));
                extendField.setField(SchemaJsonReader.readJsonForObject((Map<String, Object>) data.get("field")));
                modelProps.add(extendField);
            });
        }
    }
}
