package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtComSchemaModel;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lewis on 16-1-6.
 */
@Repository
public class CmsMtCommonSchemaDao  extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsMtComSchemaModel.class;
    }


    /**
     * 根据category id 查询对应的Schema.
     * @return
     */
    public CmsMtComSchemaModel getComSchema() {

        JSONObject comSchmeaJson = mongoTemplate.findOne(collectionName);

        CmsMtComSchemaModel comSchemaModel = null;

        if (comSchmeaJson != null){

            comSchemaModel = new CmsMtComSchemaModel();

            List fieldList = (List)comSchmeaJson.get("fields");

            List<Field> fields = SchemaJsonReader.readJsonForList(fieldList);

            comSchemaModel.setFields(fields);
        }

        return comSchemaModel;

    }


}
