package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lewis, 16-1-6
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtCommonSchemaDao extends BaseMongoDao<CmsMtCommonSchemaModel> {

    /**
     * 根据category id 查询对应的Schema.
     */
    public CmsMtCommonSchemaModel selectComSchema() {


        JSONObject comSchmeaJson = mongoTemplate.findOne("{\"type\":\"cn\"}",collectionName);

        CmsMtCommonSchemaModel comSchemaModel = null;

        if (comSchmeaJson != null) {

            comSchemaModel = new CmsMtCommonSchemaModel();

            List<Map<String, Object>> fieldList = (List<Map<String, Object>>) comSchmeaJson.get("fields");

            List<Field> fields = SchemaJsonReader.readJsonForList(fieldList);

            comSchemaModel.setFields(fields);
        }

        return comSchemaModel;
    }

    public CmsMtCommonSchemaModel selectUsComSchema() {


        JSONObject comSchmeaJson = mongoTemplate.findOne("{\"type\":\"en\"}",collectionName);

        CmsMtCommonSchemaModel comSchemaModel = null;

        if (comSchmeaJson != null) {

            comSchemaModel = new CmsMtCommonSchemaModel();

            List<Map<String, Object>> fieldList = (List<Map<String, Object>>) comSchmeaJson.get("fields");

            List<Field> fields = SchemaJsonReader.readJsonForList(fieldList);

            comSchemaModel.setFields(fields);
        }

        return comSchemaModel;
    }

    public List selectAllProps() {

        Map json = mongoTemplate.findOne("", "{fields:1}", Map.class, collectionName);

        return (List) json.get("fields");
    }
}
