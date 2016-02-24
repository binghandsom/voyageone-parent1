package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtCommonPropDefModel;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtCommonSchemaDao extends BaseMongoDao{

    @Override
    public Class getEntityClass() {
        return Field.class;
    }

    @Override
    public List<Field> selectAll() {
        List<JSONObject> jsonList = mongoTemplate.findAll(collectionName);
        List<Field> resultList = new ArrayList<>();
        if (jsonList != null) {
            jsonList.forEach(a->{
                Field field = SchemaJsonReader.mapToField(a.get("field"));
                resultList.add(field);
            });
        }
        return resultList;
    }

}
